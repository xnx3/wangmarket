package com.xnx3.im;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xnx3.Lang;
import com.xnx3.MD5Util;
import com.xnx3.StringUtil;
import com.xnx3.im.bean.CacheUserAuth;
import com.xnx3.im.bean.Im;
import com.xnx3.im.bean.Message;
import com.xnx3.im.bean.OnlineListMessage;
import com.xnx3.im.bean.SendMessage;
import com.xnx3.im.bean.SystemMessage;
import com.xnx3.j2ee.func.DB;
import com.xnx3.net.MailUtil;

/**
 * WebSocket 服务端，用来接收客户端的消息、以及发送
 */
@ServerEndpoint("/websocket")
public class WebSocketServer {
	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	//uuid，socket会话的uuid，用两个uuid拼接起来。可以通过此作为一个密钥来进行查看某个对话
	private String uuid;
	
	//当前链接此socket的客户端，用户的id、username、avatar头像、sign签名
	public Long id;
	public String username;
	public String avatar;
	public String sign;
	
	public static final String GROUPID_SUPERADMIN = "1000000001";	//im在线列表中，某个分类的id，超级管理员的分类id
	public static final String GROUPID_PARENT = "2000000001";	//im在线列表中，某个分类的id，我的上级的分类id
	public static final String GROUPID_SUB = "3000000001";	//im在线列表中，某个分类的id，我的下级的分类id
	
	public CacheUserAuth auth;	//当前用户的认证信息
	//安全考虑，用户传递时，若代理商、或者超级管理员登录，是需要传递password进行身份验证的，不然随便一个人利用通道就能当超级管理员看到哪些人在线，并与之交流了
	private String password;
	//是否发送过邮件了，每个socket只会发送一次邮件,若用户询问多次，只有第一次是发送的。便是根据这个来判断。 true：已经发送过了
	private boolean sendEmail = false;
	
	/**
	 * 连接建立成功调用的方法
	 * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		initUserInfo();
		
		//安全检测，判断当前用户是否是真实的，不是冒充的
		//判断当前连接用户是游客还是User中的用户
		if(!this.isAuth(this.id, this.password)){
			//假冒的，踢出去
			System.out.println("假的！");
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		this.uuid = Lang.uuid()+Lang.uuid();
		
		/****** 加入内存缓存 ******/
		//将当前接入的用户，加入缓存中存储
		Global.socketMap.put(this.id, this);
		//判断这个用户是否有上级。
		if(this.auth != null && this.auth.getReferrerid() > 0){
			//有上级，那么将其加入到父子关系关联里
			List<Long> subList = Global.parentRelationMap.get(this.auth.getReferrerid());
			if(subList == null){
				subList = new ArrayList<Long>();	//若为null，则是其上级用户之前没有做过关联。那么在这里进行创建一个新的，避免空指针
			}
			
			subList.add(this.id);
			Global.parentRelationMap.put(this.auth.getReferrerid(), subList);	//将最新的下级用户列表计入 父子关联中
		}
		//当前连接的是代理商
		if(this.auth.isAgent()){
			//将其记录入内存缓存，将其记录为在线
			Global.agencyOnlineMap.put(this.id, "1");
		}
		//判断当前连接的是否是超级管理
		if(this.auth.isSuperAdmin()){
			//当前连接的是超级代理
			//将其记录入内存缓存，将其记录为在线
			Global.superAdminOnlineMap.put(this.id, "1");
		}
		
		/*发送推送通知，更新在线用户列表*/
		//首先，若不是游客，则更新自己的在线用户列表。因为游客是没有在线用户的。
		if(!this.auth.isVisit()){
			onlineStateChange_noticeOneself();
		}
		updateOtherOnlineList();
	}
	
	/**
	 * 更新其他人的在线用户列表（自己的在线用户列表不在此处）。当socket打开或者关闭时都是执行此处
	 */
	private void updateOtherOnlineList(){
		//更新此socket连接的，上级的在线用户列表
		onlineStateChange_noticeParentUser();
		//更新此socket连接的，下级的在线用户列表。 
		if(!this.auth.isVisit()){
			//只要不是访客(最下级)，都有下级用户
			onlineStateChange_noticeSubUser();
		}
		//推送到超级管理员，更新超级管理员的在线用户列表
		onlineStateChange_noticeSuperAdmin();
		//如果是超级管理员，还要推送所有用户
		if(this.auth.isSuperAdmin()){
			onlineStateChange_noticeAllUser();
		}
	}
	
	/**
	 * 初始化当前链接socket的用户信息，取出get传递过来的一些用户数据
	 *  @param get传递过来的参数
	 */
	public void initUserInfo(){
		Map<String, List<String>> param = session.getRequestParameterMap();
		if(param != null && param.size() > 0){
			if(param.get("password") != null && param.get("password").size() > 0){
				this.password = param.get("password").toArray()[0].toString();
			}else{
				this.password = "";
			}
			if(param.get("id") != null && param.get("id").size() > 0){
				this.id = Lang.stringToLong(param.get("id").toArray()[0].toString(), 0);
			}else{
				this.id = 0L;
			}
			//当前登录用户的昵称，若是登录用户，会覆盖掉get传入的值
			if(param.get("username") != null && param.get("username").size() > 0){
				this.username = param.get("username").toArray()[0].toString();
			}
			//当前登录用户的头像，若是登录用户，会覆盖掉get传入的值
			if(param.get("avatar") != null && param.get("avatar").size() > 0){
				this.avatar = param.get("avatar").toArray()[0].toString();
			}else{
				this.avatar = "http://res.weiunity.com/image/imqq.jpg";
			}
			//用户的签名
			if(param.get("sign") != null && param.get("sign").size() > 0){
				this.sign = param.get("sign").toArray()[0].toString();
			}else{
				this.sign = "";
			}
			
		}
	}

	/**
	 * 若已确定此用户是网站的正式用户（总管理、代理商、建站客户）那么可以使用此来判断用户是否正确，是否跟密码匹配。匹配上后会回去到用户的授权信息，如用户的权限
	 * @param userid user.id
	 * @param password 数据表中存储的user.password
	 * @return true：成功，是数据库中真实的用户
	 */
	private boolean isAuth(long userid, String password){
		//安全考虑，得先将其转换为long
		if(userid > 0){
			CacheUserAuth auth;
			
			//判断其是否要从数据库中找。真实的用户id是int型的，而访客的是long，是13位及以上的
			if(userid < Integer.MAX_VALUE){
				//是正常的网站用户，进行用户真实身份识别
				//从内存缓存中找
				auth = Global.cacheUserAuthMap.get(userid);
				if(auth == null){
					//缓存中没有记录，那么从数据库中取
					Map<String, String> map = null;
					try {
						map = DB.getValue("SELECT id,password,authority,referrerid FROM user WHERE id = "+ userid);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					auth = new CacheUserAuth();
					if(map == null || map.size() == 0){
						//没有这个id得用户，这个用户是假冒的！
						//规划缓存假冒用户列表，找时候先找正常缓存，在找假冒缓存，都没有在找数据库
						return false;
					}else{
						auth.setUserid(Lang.stringToLong(map.get("id"), 0));
						auth.setPassword(map.get("password"));
						auth.setAuthority(Lang.stringToInt(map.get("authority"), 0));
						auth.setReferrerid(Lang.stringToLong(map.get("referrerid"), 0));
					}
					System.out.println("-------database---"+auth);
					//将其存入缓存
					Global.cacheUserAuthMap.put(this.id, auth);
				}
				
				//当前有这个uid存在，那么进行密码的校验
				if(auth.isHaveUser()){
					if(password != null && MD5Util.MD5(auth.getPassword()).equals(password)){
						//权限认证通过，用户是合法的，将此缓存的auth赋予此socket
						this.auth = auth;
						return true;
					}else{
						//密码校验失败，密码不匹配！可能是假冒的
						return false;
					}
				}else{
					//这个uid不存在，那一定也是假的！
					return false;
				}
			}else{
				//是访客，无固定身份，但是要有上级的用户id
				auth = new CacheUserAuth();
				
				//若获取到的用户id还大于int的最大值，那么肯定就是游客了，游客也会认为是认证通过，不属于非法用户。但是游客必须有上级id，也就是get传入parentId
				auth.setUserid(this.id);
				//从get传入的参数里得到访客的上级用户id
				long parentId = 0;
				Map<String, List<String>> param = session.getRequestParameterMap();
				if(param != null && param.size() > 0){
					//当前登录用户的上级id
					if(param.get("parentId") != null && param.get("parentId").size() > 0){
						parentId = Lang.stringToLong(param.get("parentId").toArray()[0].toString(), 0);
					}
				}
				if(parentId > 0){
					//有上级，这个访客有效，是正常的游客
					auth.setReferrerid(parentId);
					this.auth = auth;
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
		System.out.println("close----"+this.auth);
		if(this.auth == null){
			System.out.println("非法用户，自动关闭");
			return;
		}
		/****** 清理内存缓存记录 ******/
		//将当前接入的用户，移除
		Global.socketMap.remove(this.id);
		//判断这个用户是否有上级。
		if(this.auth != null && this.auth.getReferrerid() > 0){
			//有上级，那么要将其父子关系关联里，移除
			List<Long> subList = Global.parentRelationMap.get(this.auth.getReferrerid());
			if(subList == null){
				subList = new ArrayList<Long>();	//若为null，则是其上级用户之前没有做过关联。那么在这里进行创建一个新的，避免空指针
			}
			for (int i = 0; i < subList.size(); i++) {
				if(subList.get(i).equals(this.id)){
					subList.remove(i);
				}
			}
			Global.parentRelationMap.put(this.auth.getReferrerid(), subList);	//将当前用户的上级用户，最新的下级用户列表计入 父子关联中
		}
		//当前连接的是代理商
		if(this.auth.isAgent()){
			//将其代理商在线的内存缓存去除
			Global.agencyOnlineMap.remove(this.id);
		}
		//判断当前连接的是否是超级管理
		if(this.auth.isSuperAdmin()){
			//当前连接的是超级管理
			//将其从超级管理在线列表记录中移除
			Global.superAdminOnlineMap.remove(this.id);
		}
		
		/*发送推送通知，更新在线用户列表*/
		updateOtherOnlineList();
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 * @throws IOException 
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		JSONObject json = JSONObject.fromObject(message);
		Message msg = new Message();
		msg.setContent(StringUtil.filterXss(json.getJSONObject("mine").getString("content")));
		msg.setSendAvatar(StringUtil.filterXss(json.getJSONObject("mine").getString("avatar")));
		msg.setSendId(json.getJSONObject("mine").getLong("id"));
		msg.setSendUserName(StringUtil.filterXss(json.getJSONObject("mine").getString("username")));
		
		msg.setSocketId(this.id);
		msg.setSocketUuid(this.uuid);
		
		if(json.getJSONObject("to") != null){
			JSONObject to = json.getJSONObject("to");
			if(to.get("avatar") != null){
				msg.setReceiveAvatar(StringUtil.filterXss(to.getString("avatar")));
			}
			if(to.get("id") != null){
				msg.setReceiveId(to.getLong("id"));
			}
			if(to.get("type") != null){
				msg.setReceiveType(StringUtil.filterXss(to.getString("type")));
			}
			if(to.get("username") != null){
				msg.setReceiveUserName(StringUtil.filterXss(to.getString("username")));
			}
			if(to.get("name") != null){
				msg.setReceiveUserName(StringUtil.filterXss(to.getString("name")));
			}
		}
		
		//记录信息
		KefuLog.insert(msg);
		
		//收到消息后，向目标方发送消息
		//首先查找目标方是否在socket链接
		WebSocketServer socket = Global.socketMap.get(msg.getReceiveId());
		if(socket == null){
			//对方已下线，那么进行判断，对方是游客还是网站用户若是注册用户的话，需要进行邮件发送提醒
			if(msg.getReceiveId() < Integer.MAX_VALUE){
				//是注册用户，那么从缓存中取接收方用户设置的Im
				Im im = Global.imMap.get(msg.getReceiveId());
				if(im == null){
					//缓存中没有记录，那么从数据库中取
					Map<String, String> map = null;
					try {
						map = DB.getValue("SELECT auto_reply,use_off_line_email,email FROM im WHERE userid = "+ msg.getReceiveId());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					im = new Im();
					if(map == null || map.size() == 0){
						//没有这个id得SiteIm设置，那么就是此用户没有设置自己的自动回复策略
						im.setHaveImSet(false);
					}else{
						//有，用户设置了自己的自动回复策略
						im.setHaveImSet(true);
						im.setEmail(map.get("email"));
						im.setUseOffLineEmail(map.get("use_off_line_email") != null && map.get("use_off_line_email").equals("1"));
						im.setAutoReply(map.get("auto_reply"));
					}
					//将im缓存
					Global.imMap.put(msg.getReceiveId(), im);
				}
				
				if(im.isHaveImSet()){
					//使用自动回复策略
					//进行自动回复
					sendReply(im.getAutoReply(), msg);
					//进行邮件提醒
					if(im.isUseOffLineEmail()){
						sendMail(msg, im.getEmail());
					}
				}
			}else{
				//若没有设置自动回复策略，则回复默认的文字
				sendReply("抱歉，对方已下线！", msg);
			}
		}else{
			socket.sendMessageContent(msg.getContent(), msg);
		}
		
	}
	
	/**
	 * 访客聊天时，若是对方下线了，还会向对方发送邮件提醒
	 * @param msg 对方发送的消息
	 * @param email 我不在线，要将对方发送的消息发送到这个Email地址上
	 */
	public void sendMail(Message msg, String email){
		//进行email的初步检测，判断是否合格
		if(email != null && email.length() > 3 && email.indexOf("@") > 0){
			//判断其是否发送过邮件了
			if(!this.sendEmail){
				String url = Global.previewByTokenUrl+"im/previewByToken.do?token="+this.uuid;
				MailUtil.sendHtmlMail(email, "您的网站有访客向您咨询了", "<span style=\"padding-right:10px; font-weight: bold;\">"+msg.getSendUserName()+"</span>向您发起咨询，内容为：<div style=\"padding:10px;\">"+msg.getContent()+"</div><br/><hr/>提示：此访客跟你说话时，只会在说第一句时向您发送邮件提醒，其余说的内容，您可点击此连接直接查看 <br/><a href=\""+url+"\">"+url+"</a>");
				this.sendEmail = true;
			}
		}
	}
	
	
	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		if(error.getMessage() != null && error.getMessage().indexOf("java.io.IOException") > -1){
			System.out.println("onError"+error.getMessage());
		}else{
			error.printStackTrace();
		}
	}

	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException{
		this.session.getBasicRemote().sendText(message);
	}
	
	/**
	 * 用户发送消息后，给该发送消息的用户一条回复
	 * @param content 回复的消息内容
	 * @param message {@link Message}当前用户发出的消息对象，利用此用户发出的消息对象，拿到接收方的id，以此来拿到对话窗口的id
	 * @throws IOException 
	 */
	public void sendReply(String content, Message message) throws IOException{
		SystemMessage sm = new SystemMessage();
		sm.setContent(content);
		sm.setId(message.getReceiveId());
		sm.setType("friend");
		JSONObject json = JSONObject.fromObject(sm);
		sendMessage(json.toString());
	}
	
	/**
	 * 发送消息
	 * @param messageContent 消息的内容，对话的内容
	 * @param otherMessage 对方发过来的消息体， {@link Message}，会提取对方的id、用户名等
	 * @throws IOException
	 */
	public void sendMessageContent(String messageContent, Message otherMessage) throws IOException{
		SendMessage sendMessage = new SendMessage(otherMessage);
		JSONObject json = JSONObject.fromObject(sendMessage);
		sendMessage(json.toString());
	}
	
	/**
	 * 发送消息。当双方聊天时，一方说话发送消息，会通过这个自动发送到另一方。
	 * @param otherMessage 对方发过来的消息体， {@link Message}
	 * @throws IOException
	 */
	public void sendMessage(Message message) throws IOException{
		SendMessage sendMessage = new SendMessage(message);
		JSONObject json = JSONObject.fromObject(sendMessage);
		sendMessage(json.toString());
	}

	/**
	 * 在线状态改变，任何用户上线（网站访客除外），或者关闭链接下线时触发。将通知超级管理员（官方客服）用户
	 * @throws IOException 
	 */
	public void onlineStateChange_noticeSuperAdmin(){
		//第一步，列出当前所有在线的用户，将其组合成json数据，以更新用户客户端的数据
		List<OnlineListMessage> list = new ArrayList<OnlineListMessage>();
		for (Map.Entry<Long, WebSocketServer> entry : Global.socketMap.entrySet()) {
			//entry.getValue().id
			WebSocketServer socket = entry.getValue();
			if(!socket.auth.isVisit()){
				OnlineListMessage ol = new OnlineListMessage();
				ol.setAvatar(socket.avatar);
				ol.setId(socket.id);
				ol.setSign(socket.sign);
				if(socket.auth.isAgent()){
					ol.setUsername("代理："+socket.username);
					ol.setGroupid(GROUPID_SUB);  //无论是网站使用者，还是代理，都归总到我的下级里
				}else if (socket.auth.isUser()) {
					ol.setUsername("网站："+socket.username);
					ol.setGroupid(GROUPID_SUB);  //无论是网站使用者，还是代理，都归总到我的下级里
				}else if (socket.auth.isSuperAdmin()) {
					ol.setUsername(socket.username);
					ol.setGroupid(GROUPID_SUPERADMIN);
				}
				list.add(ol);
			}
		}
		JSONObject json = new JSONObject();
		json.put("type", "onlineList");
		json.put("list", JSONArray.fromObject(list));
		
		
		//第二步，对所有在线的超级管理进行群发操作
		for (Map.Entry<Long, String> entry : Global.superAdminOnlineMap.entrySet()) {
			WebSocketServer ws = Global.socketMap.get(entry.getKey());
			try {
				ws.sendMessage(json.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 在线状态改变，当超级管理员上线、下线时，将通知所有注册的用户改变自己的在线用户列表（只有游客除外）
	 * @throws IOException 
	 */
	public void onlineStateChange_noticeAllUser(){
		//列出当前所有在线的用户，让所有用户都要更新自己的在用户列表
		for (Map.Entry<Long, WebSocketServer> entry : Global.socketMap.entrySet()) {
			//entry.getValue().id
			WebSocketServer socket = entry.getValue();
			if(!socket.auth.isVisit()){
				socket.onlineStateChange_noticeOneself();
			}
		}
	}
	
	/**
	 * 在线状态改变，通知当前socket连接的上级用户。改变当前上级用户的在线列表
	 * @throws IOException 
	 */
	public void onlineStateChange_noticeParentUser(){
		//判断当前socket连接用户是否有上级存在，更新其上级
		if(this.auth.getReferrerid() > 0){
			//有上级。取得其上级用户的信息
			WebSocketServer parentWS = Global.socketMap.get(this.auth.getReferrerid());
			//判断当前连接用户的上级用户是否在线
			if(parentWS != null){
				parentWS.onlineStateChange_noticeOneself();
			}else{
				//上级不在线，那么忽略
			}
		}
	}
	
	/**
	 * 在线状态改变，通知当前socket连接的下级用户。改变当前下级用户的在线列表。当然，如果有下级用户的话
	 * @throws IOException 
	 */
	public void onlineStateChange_noticeSubUser(){
		//判断当前socket连接用户是否有下级存在，更新其下级
		List<Long> subList = Global.parentRelationMap.get(this.id);
		if(subList != null && subList.size() > 0){
			//有下级在线，遍历所有其下级用户的socket
			for (int i = 0; i < subList.size(); i++) {
				WebSocketServer subWS = Global.socketMap.get(subList.get(i));
				//判断当前连接用户的上级用户是否在线
				if(subWS != null){
					subWS.onlineStateChange_noticeOneself();
				}else{
					//下级不在线，那么忽略
				}
			}
		}
	}

	/**
	 * 在线状态改变，通知自己，改变当前自己能看的在线用户列表
	 * @throws IOException 
	 */
	public void onlineStateChange_noticeOneself(){
		//用于统计当前parent在线的下级用户的信息，直接通过socket返回到客户端
		List<OnlineListMessage> onlineList = new ArrayList<OnlineListMessage>();
		
		
		//判断当前用户是否有下级用户。若有下级用户，需统计出下级用户的在线列表
		List<Long> subList = Global.parentRelationMap.get(this.id);
		if(subList == null){
			subList = new ArrayList<Long>();	//赋值，免得造成空指针
		}
		for (int i = 0; i < subList.size(); i++) {
			WebSocketServer socket = Global.socketMap.get(subList.get(i));
			OnlineListMessage ol = new OnlineListMessage();
			ol.setAvatar(socket.avatar);
			ol.setId(socket.id);
			ol.setSign(socket.sign);
			ol.setUsername(socket.username);
			ol.setGroupid(GROUPID_SUB);
			onlineList.add(ol);
		}
		
		//判断当前用户是否有其上级用户，若有，则要判断其上级用户是否在线
		if(this.auth.getReferrerid() > 0){
			WebSocketServer parentWS = Global.socketMap.get(this.auth.getReferrerid());
			if(parentWS != null){
				//上级用户在线，将当前socket连接的上级在线用户也要显示出来
				OnlineListMessage ol = new OnlineListMessage();
				ol.setAvatar(parentWS.avatar);
				ol.setId(parentWS.id);
				ol.setSign(parentWS.sign);
				ol.setUsername(parentWS.username);
				ol.setGroupid(GROUPID_PARENT);
				onlineList.add(ol);
			}
		}
		
		//判断当前是否有超级管理员（或客服）在线
		for (Map.Entry<Long, String> entry : Global.superAdminOnlineMap.entrySet()) {
			WebSocketServer ws = Global.socketMap.get(entry.getKey());
			OnlineListMessage ol = new OnlineListMessage();
			ol.setAvatar(ws.avatar);
			ol.setId(ws.id);
			ol.setSign(ws.sign);
			ol.setUsername(ws.username);
			ol.setGroupid(GROUPID_SUPERADMIN);
			onlineList.add(ol);
		}
		
		
		//组合准备返回的数据
		JSONObject json = new JSONObject();
		json.put("type", "onlineList");
		json.put("list", JSONArray.fromObject(onlineList));
		try {
			this.sendMessage(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public String toString() {
		return "WebSocketServer [id=" + id
				+ ", username=" + username + ", avatar=" + avatar + ", sign="
				+ sign + "]";
	}
	
	
}
