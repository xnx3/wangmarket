package com.xnx3.wangmarket.im;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.wangmarket.im.bean.CacheUserAuth;
import com.xnx3.wangmarket.im.bean.Im;
import com.xnx3.net.AliyunLogUtil;
import com.xnx3.net.MNSUtil;

/**
 * 全局控制
 * @author 管雷鸣
 */
public class Global {
	
	/**
	 * key： 用户的id，因为游客可能的id会很长，所以这里用long，WebSocketServer.id, 通过客户端用户的id来取socket，当然，如果没有取到，那就是用户不在线了
	 */
	public static Map<java.lang.Long, WebSocketServer> socketMap = new HashMap<Long, WebSocketServer>();
	
	/**
	 * 同上，当然，这个只服务于有自己下级的用户，存储的也都是此种用户
	 * key：用户的id，但是这里只有注册用户的id，无访客的
	 * value:客户自己设置的SiteIm数据表中的信息。
	 * 若value＝null，则未缓存过，需要从数据库找。若数据库种没有找到，则需要创建一个Im对象，设置其haveImSet=false
	 */
	public static Map<Long, Im> imMap = new HashMap<Long, Im>();
	
	/**
	 * key: 其上级用户的id   value: List-websocket.id，list其中的值为socketMap.key，即用户的id。   可以列出当前某个用户id下有多少其下级用户在线
	 */
	public static Map<Long, List<Long>> parentRelationMap = new HashMap<Long, List<Long>>();
	
	/**
	 * 记录当前登录的超级管理员用户（暂时是id<10便是超级管理员用户）
	 * key：代理商的用户id， value：1  1便是在线，其他数值或者null则是不在线
	 * 此主要用于判断超级管理员是否在线使用。若map中不为空，则是在线。
	 */
	public static Map<Long, String> superAdminOnlineMap = new HashMap<Long, String>();
	
	/**
	 * 记录当前登录的代理商
	 * key：代理商的用户id， value，随便的字符串。
	 * 此主要用于普通用户连接上后，判断其上级是否在线使用。若map中不为空，则是在线。
	 */
	public static Map<Long, String> agencyOnlineMap = new HashMap<Long, String>(); 
	
	/**
	 * 检测用户传过来的id是否是真实的，当然，游客除外，不会检测
	 * 会首先从此处判断是否有存储，若为null，则从数据库中查询user.id
	 * 若有值，判断其 haveUser 是否为true，若为true，则有这个用户，进而根据password进行判断是否是真的用户
	 * Key：CacheUserAuth.userid
	 */
	public static Map<Long, CacheUserAuth> cacheUserAuthMap = new HashMap<Long, CacheUserAuth>();
	
	
	/**** 以下直接复制入selfSite项目中 ****/
	
	/**
	 * 管理后台有更改后，通过MNS来更新IM应用
	 */
	public static MNSUtil kefuMNSUtil;
	public static String kefuMNSUtil_queueName = "";
	/**
	 * 日志服务，记录客服对话
	 */
	public static AliyunLogUtil aliyunLogUtil = null;
	
	//根据socketUuid进行查看会话内容的网址，管理后台网址
	public static String previewByTokenUrl;
	
	//webSocket IM的对接url， 在 imConfig.xml 中配置
	public static String websocketUrl = "";
	
	static{
		ConfigManagerUtil c = ConfigManagerUtil.getSingleton("imConfig.xml");
		
		//消息服务
		String useMNS = c.getValue("aliyunMNS_kefu.use");
		if(useMNS != null && useMNS.equals("true")){
			String mns_accessKeyId = c.getValue("aliyunMNS_kefu.accessKeyId");
			String mns_accessKeySecret = c.getValue("aliyunMNS_kefu.accessKeySecret");
			String mns_endpoint = c.getValue("aliyunMNS_kefu.endpoint");
			if(mns_accessKeyId == null || mns_accessKeyId.length() == 0){
				//取数据库的
				mns_accessKeyId = com.xnx3.j2ee.Global.get("ALIYUN_ACCESSKEYID");
			}
			if(mns_accessKeySecret == null || mns_accessKeySecret.length() == 0){
				//取数据库的
				mns_accessKeySecret = com.xnx3.j2ee.Global.get("ALIYUN_ACCESSKEYSECRET");
			}
			if(mns_accessKeyId.length() > 10){
				kefuMNSUtil = new MNSUtil(mns_accessKeyId, mns_accessKeySecret, mns_endpoint);
				kefuMNSUtil_queueName = c.getValue("aliyunMNS_kefu.queueName");
				System.out.println("已开启MNS！若im采用分布式部署，此服务是必须要开启的！");
			}
		}
		
		
		//日志服务
		//加载日志服务
		String useLog = c.getValue("aliyunLogKefu.use");
		if(useLog != null && useLog.equals("true")){
			String log_accessKeyId = c.getValue("aliyunLogKefu.accessKeyId");
			String log_accessKeySecret = c.getValue("aliyunLogKefu.accessKeySecret");
			if(log_accessKeyId == null || log_accessKeyId.length() == 0){
				//取数据库的
				log_accessKeyId = com.xnx3.j2ee.Global.get("ALIYUN_ACCESSKEYID");
			}
			if(log_accessKeySecret == null || log_accessKeySecret.length() == 0){
				//取数据库的
				log_accessKeySecret = com.xnx3.j2ee.Global.get("ALIYUN_ACCESSKEYSECRET");
			}
			if(log_accessKeyId.length() > 10){
				aliyunLogUtil = new AliyunLogUtil(c.getValue("aliyunLogKefu.endpoint"), log_accessKeyId, log_accessKeySecret, c.getValue("aliyunLogKefu.project"), c.getValue("aliyunLogKefu.logstore"));
			}
		}

		previewByTokenUrl = c.getValue("previewByTokenUrl");
		websocketUrl = c.getValue("websocketUrl");
	}
	
}
