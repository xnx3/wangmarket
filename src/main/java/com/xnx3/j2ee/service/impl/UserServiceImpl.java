package com.xnx3.j2ee.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.j2ee.entity.*;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.func.Language;
import com.xnx3.j2ee.func.Log;
import com.xnx3.j2ee.func.OSS;
import com.xnx3.j2ee.func.Safety;

@Service
public class UserServiceImpl implements UserService{

	@Resource
	private SqlDAO sqlDAO;

	public SqlDAO getSqlDAO() {
		return sqlDAO;
	}

	public void setSqlDAO(SqlDAO sqlDAO) {
		this.sqlDAO = sqlDAO;
	}

	public User findByPhone(Object phone){
		List<User> list = sqlDAO.findByProperty(User.class, "phone", phone);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 登陆
	 * @param request {@link HttpServletRequest} 
	 * 		<br/>登陆时form表单需提交两个参数：username(用户名/邮箱)、password(密码)
	 * @return {@link BaseVO}
	 */
	public BaseVO loginByUsernameAndPassword(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		return loginByUsernameAndPassword(request, username, password);
	}
	
	public BaseVO loginByUsernameAndPassword(HttpServletRequest request, String username, String password){
		username = Safety.filter(username);
		
		BaseVO baseVO = new BaseVO();
		if(username==null || username.length() == 0 ){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginUserOrEmailNotNull"));
			return baseVO;
		}
		if(password==null || password.length() == 0){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginPasswordNotNull"));
			return baseVO;
		}
		
		//判断是用户名还是邮箱登陆的，进而查询邮箱或者用户名，进行登录
		List<User> l = sqlDAO.findByProperty(User.class, username.indexOf("@")>-1? "email":"username", username);
		
		if(l!=null && l.size()>0){
			User user = l.get(0);
			
			String md5Password = new Md5Hash(password, user.getSalt(),Global.USER_PASSWORD_SALT_NUMBER).toString();
			//检验密码是否正确
			if(md5Password.equals(user.getPassword())){
				//检验此用户状态是否正常，是否被冻结
				if(user.getIsfreeze() == User.ISFREEZE_FREEZE){
					baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginUserFreeze"));
					return baseVO;
				}
				
				user.setLasttime(DateUtil.timeForUnix10());
				user.setLastip(IpUtil.getIpAddress(request));
				sqlDAO.save(user);
				
				UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getUsername());
		        token.setRememberMe(false);
				Subject currentUser = SecurityUtils.getSubject();  
				try {
					currentUser.login(token);  
				} catch ( UnknownAccountException uae ) {
					java.lang.System.out.println("UnknownAccountException:"+uae.getMessage());
				} catch ( IncorrectCredentialsException ice ) {
					java.lang.System.out.println("IncorrectCredentialsException:"+ice.getMessage());
				} catch ( LockedAccountException lae ) {
					java.lang.System.out.println("LockedAccountException:"+lae.getMessage());
				} catch ( ExcessiveAttemptsException eae ) {
					java.lang.System.out.println("ExcessiveAttemptsException:"+eae.getMessage());
				} catch ( org.apache.shiro.authc.AuthenticationException ae ) {  
					java.lang.System.out.println("AuthenticationException:"+ae.getMessage());
				}
//				logDao.insert("USER_LOGIN_SUCCESS");
				baseVO.setBaseVO(BaseVO.SUCCESS, Language.show("user_loginSuccess"));
			}else{
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginPasswordFailure"));
			}
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginUserNotFind"));
		}
		
		return baseVO;
	}
	
	/**
	 * 注册
	 * @param user {@link User} 
	 * 		<br/>表单的用户名(username)、 密码(password)为必填项
	 * @param request {@link HttpServletRequest}
	 * @return {@link BaseVO}
	 */
	public BaseVO reg(User user, HttpServletRequest request) {
		BaseVO baseVO = new BaseVO();
		user.setEmail(Safety.filter(user.getEmail()));
		user.setNickname(Safety.filter(user.getNickname()));
		user.setPhone(Safety.filter(user.getPhone()));
		user.setUsername(Safety.filter(user.getUsername()));
		
		//判断用户名、邮箱、手机号是否有其中为空的
		if(user.getUsername()==null||user.getUsername().equals("")||user.getPassword()==null||user.getPassword().equals("")){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_regDataNotAll"));
		}
		
		//判断用户名、邮箱、手机号是否有其中已经注册了，唯一性
		//邮箱的唯一，仅当邮箱设置了之后，才会判断邮箱的唯一性
		if(user.getEmail() != null && user.getEmail().length() > 0){
			if(sqlDAO.findByProperty(User.class, "email", user.getEmail()).size() > 0){
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_regFailureForEmailAlreadyExist"));
				return baseVO;
			}
		}
		
		//判断用户名唯一性
		if(sqlDAO.findByProperty(User.class, "username", user.getUsername()).size() > 0){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_regFailureForUsernameAlreadyExist"));
			return baseVO;
		}
		
		//判断手机号唯一性
		if(user.getPhone() != null && user.getPhone().length() > 0){
			if(findByPhone(user.getUsername()) != null){
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_regFailureForPhoneAlreadyExist"));
				return baseVO;
			}
		}
		
		user.setRegip(IpUtil.getIpAddress(request));
		user.setLastip(IpUtil.getIpAddress(request));
		user.setRegtime(DateUtil.timeForUnix10());
		user.setLasttime(DateUtil.timeForUnix10());
		user.setNickname(user.getUsername());
		user.setAuthority(Global.system.get("USER_REG_ROLE"));
		user.setCurrency(0);
		user.setReferrerid(0);
		user.setFreezemoney(0F);
		user.setMoney(0F);
		user.setIsfreeze(User.ISFREEZE_NORMAL);
		user.setHead("default.png");
		user.setIdcardauth(User.IDCARDAUTH_NO);
		
		String inviteid = null;
		if(request.getSession().getAttribute("inviteid")!=null){
			inviteid = request.getSession().getAttribute("inviteid").toString();
		}
		
		User referrerUser1 = null;
		if(inviteid!=null&&inviteid.length()>0){
			int referrerid = Lang.stringToInt(inviteid, 0);
			referrerUser1 = sqlDAO.findById(User.class, referrerid);	//一级下线
			if(referrerUser1!=null){
				user.setReferrerid(referrerid);
			}
		}
		

		if(user.getUsername().length() > 20){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_userNameToLong"));
		}
		
		Random random = new Random();
		user.setSalt(random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+"");
        String md5Password = new Md5Hash(user.getPassword(), user.getSalt(),Global.USER_PASSWORD_SALT_NUMBER).toString();
		user.setPassword(md5Password);
		
		sqlDAO.save(user);
		if(user.getId()>0){
			//已注册成功，自动登录成用户
			UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getUsername());
	        token.setRememberMe(false);
			Subject currentUser = SecurityUtils.getSubject();  
			
			try {  
				currentUser.login(token);  
			} catch ( UnknownAccountException uae ) {
			} catch ( IncorrectCredentialsException ice ) {
			} catch ( LockedAccountException lae ) {
			} catch ( ExcessiveAttemptsException eae ) {
			} catch ( org.apache.shiro.authc.AuthenticationException ae ) {  
			}
			
			//赋予该用户系统设置的默认角色
			UserRole userRole = new UserRole();
			userRole.setRoleid(Lang.stringToInt(Global.system.get("USER_REG_ROLE"), 0));
			userRole.setUserid(user.getId());
			sqlDAO.save(userRole);
			
			//推荐人增加奖励
			if(user.getReferrerid()>0){	//是否有直接推荐人
				referrerAddAward(referrerUser1, Global.system.get("INVITEREG_AWARD_ONE"), user);
				
				if(referrerUser1.getReferrerid()>0){	//一级下线有上级推荐人，拿到二级下线
					User referrerUser2 = sqlDAO.findById(User.class, referrerUser1.getReferrerid());
					if(referrerUser2!=null){
						referrerAddAward(referrerUser2, Global.system.get("INVITEREG_AWARD_TWO"), user);
						
						if(referrerUser2.getReferrerid()>0){	//二级下线有上级推荐人，拿到三级下线
							User referrerUser3 = sqlDAO.findById(User.class, referrerUser2.getReferrerid());
							if(referrerUser3!=null){
								referrerAddAward(referrerUser3, Global.system.get("INVITEREG_AWARD_THREE"), user);
								
								if(referrerUser3.getReferrerid()>0){	//三级下线有上级推荐人，拿到四级下线
									User referrerUser4 = sqlDAO.findById(User.class, referrerUser3.getReferrerid());
									if(referrerUser4!=null){
										referrerAddAward(referrerUser4, Global.system.get("INVITEREG_AWARD_FOUR"), user);
									}
								}
							}
						}
					}
				}
			}
			
//				logDao.insert("USER_REGISTER_SUCCESS");
			baseVO.setBaseVO(BaseVO.SUCCESS, user.getId()+"");
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_regFailure"));
		}
		
		return baseVO;
	}
	
	/**
	 * 注册成功后下线充值奖励
	 * @param user 要充值的下线user
	 * @param addCurrency_ 增加的货币值
	 * @param regUser 注册的用户的用户名
	 */
	private void referrerAddAward(User user,String addCurrency_,User regUser){
		int addCurrency = Lang.stringToInt(addCurrency_, 0);
		if(addCurrency>0){
			user.setCurrency(user.getCurrency()+addCurrency);
			sqlDAO.save(user);
//			logDao.insert(regUser.getId(), "USER_INVITEREG_AWARD", addCurrency+"");
		}
	}
	
	/**
	 * 注册
	 * @param user {@link User} 
	 * 		<br/>表单的用户名(username)、 密码(password)为必填项
	 * @param request {@link HttpServletRequest}
	 * @return {@link BaseVO}
	 */
	public void regInit(HttpServletRequest request) {
		String inviteid_ = request.getParameter("inviteid");
		if(inviteid_!=null&&inviteid_.length()>0){
			int inviteid = Lang.stringToInt(inviteid_, 0);
			
			User user = sqlDAO.findById(User.class, inviteid);
			if(user!=null){
				request.getSession().setAttribute("inviteid", inviteid); 	//邀请人id
			}
		}
	}

	/**
	 * 手机号＋动态验证码登陆
	 * @param request {@link HttpServletRequest} 
	 * 		<br/>登陆时form表单需提交两个参数：phone(手机号)、code(手机收到的动态验证码)
	 * @return {@link BaseVO}
	 */
	public BaseVO loginByPhoneAndCode(HttpServletRequest request) {
		BaseVO baseVO = new BaseVO();
		String phone = Safety.filter(request.getParameter("phone"));
		String code = Safety.filter(request.getParameter("code"));
		if(phone==null || phone.length() != 11){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneAndCodePhoneFailure"));
			return baseVO;
		}
		if(code==null || code.length() != 6){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneAndCodeCodeFailure"));
			return baseVO;
		}
		
		int queryAddtime = 0;
		if(SmsLog.codeValidity > 0){
			int currentTime = DateUtil.timeForUnix10();
			queryAddtime = currentTime-SmsLog.codeValidity;
		}
		SmsLog smsLog = findByPhoneAddtimeUsedTypeCode(phone, queryAddtime, SmsLog.USED_FALSE, SmsLog.TYPE_LOGIN,code);
    	if(smsLog != null){
    		User user = findByPhone(phone);
    		int userid = 0;
    		if(user != null && user.getId() != null){
    			userid = user.getId();
    		}
    		
    		/****更改SmsLog状态*****/
    		smsLog.setUserid(userid);
    		smsLog.setUsed(SmsLog.USED_TRUE);
    		sqlDAO.save(smsLog);
    		
    		//如果没有用户，则直接返回失败提示
    		if(user == null){
    			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneAndCodeRegFailure"));
    			return baseVO;
    		}
    		
    		//检验此用户状态是否正常，是否被冻结
			if(user.getIsfreeze() == User.ISFREEZE_FREEZE){
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginUserFreeze"));
				return baseVO;
			}
    		
    		
    		/*******更改User状态******/
    		user.setLasttime(DateUtil.timeForUnix10());
    		user.setLastip(IpUtil.getIpAddress(request));
    		sqlDAO.save(user);
    		
			UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getUsername());
	        token.setRememberMe(false);
			Subject currentUser = SecurityUtils.getSubject();  
			
			try {  
				currentUser.login(token);  
			} catch ( UnknownAccountException uae ) {
				uae.printStackTrace();
			} catch ( IncorrectCredentialsException ice ) {
				ice.printStackTrace();
			} catch ( LockedAccountException lae ) {
				lae.printStackTrace();
			} catch ( ExcessiveAttemptsException eae ) {
				eae.printStackTrace();
			} catch ( org.apache.shiro.authc.AuthenticationException ae ) { 
				ae.printStackTrace();
			}
			
//			logDao.insert("USER_LOGIN_SUCCESS");
			baseVO.setBaseVO(BaseVO.SUCCESS, Language.show("user_loginSuccess"));
			return baseVO;
    	}else{
    		baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneAndCodeCodeNotFind"));
    		return baseVO;
    	}
	}
	

	/**
	 * 根据手机号、是否使用，类型，以及发送时间，查询符合的数据列表，即查询验证码是否存在
	 * @param phone 手机号
	 * @param addtime 添加使用，即发送时间，查询数据的时间大于此时间
	 * @param used 是否使用，如 {@link SmsLog#USED_FALSE}
	 * @param type 短信验证码类型，如 {@link SmsLog#TYPE_LOGIN}
	 * @param code 短信验证码
	 * @return 若查询到验证码存在，返回 {@link SmsLog}，若查询不到，返回null，即验证码不存在
	 */
	private SmsLog findByPhoneAddtimeUsedTypeCode(String phone,int addtime,Short used,Short type,String code){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("phone", phone);
		parameterMap.put("addtime", addtime);
		parameterMap.put("used", used);
		parameterMap.put("type", type);
		parameterMap.put("code", code);
		List<SmsLog> list = sqlDAO.findByHql("from SmsLog as model where model.phone= :phone and model.addtime > :addtime and model.used = :used and model.type = :type and model.code = :code", parameterMap, 0);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	
	/**
	 * 手机号登陆，会自动检测上次登陆的ip，若上次登陆的ip跟当前的ip一样，则这个手机用户登陆成功
	 * @param request {@link HttpServletRequest} 
	 * 		<br/>登陆时form表单需提交两个参数：phone(手机号)、code(手机收到的动态验证码)
	 * @return {@link BaseVO}
	 */
	public BaseVO loginByPhone(HttpServletRequest request) {
		BaseVO baseVO = new BaseVO();
		String phone = Safety.filter(request.getParameter("phone"));
		if(phone==null){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhonePhoneFailure"));
			return baseVO;
		}else{
			phone = phone.replaceAll(" ", "");
			if(phone.length() != 11){
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhonePhoneFailure"));
				return baseVO;
			}
		}
		
		User user = findByPhone(phone);
		if(user == null){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneUserNotFind"));
			return baseVO;
		}
		
		//ip检测
		String ip = IpUtil.getIpAddress(request);
		if(!(user.getLastip().equals(ip) || user.getRegip().equals(ip))){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneIpFailure"));
			return baseVO;
		}
		
		//检验此用户状态是否正常，是否被冻结
		if(user.getIsfreeze() == User.ISFREEZE_FREEZE){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginUserFreeze"));
			return baseVO;
		}
		Log.debug("检验此用户状态是否正常，是否被冻结，未冻结，正常");
		
		/*******更改User状态******/
		user.setLasttime(DateUtil.timeForUnix10());
		user.setLastip(IpUtil.getIpAddress(request));
		sqlDAO.save(user);
		Log.debug("更新User状态，更新后的User为："+user);
		
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getUsername());
        token.setRememberMe(false);
		Subject currentUser = SecurityUtils.getSubject();  
		
		try {  
			currentUser.login(token);  
		} catch ( UnknownAccountException uae ) {
			uae.printStackTrace();
		} catch ( IncorrectCredentialsException ice ) {
			ice.printStackTrace();
		} catch ( LockedAccountException lae ) {
			lae.printStackTrace();
		} catch ( ExcessiveAttemptsException eae ) {
			eae.printStackTrace();
		} catch ( org.apache.shiro.authc.AuthenticationException ae ) { 
			ae.printStackTrace();
		}
		
//		logDao.insert("USER_LOGIN_SUCCESS");
		baseVO.setBaseVO(BaseVO.SUCCESS, Language.show("user_loginSuccess"));
		return baseVO;
	}

	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
//			logDao.insert("USER_LOGOUT");
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
		}
	}


	public BaseVO freezeUser(int id) {
		BaseVO baseVO = new BaseVO();
		if(id > 0){
			User user = sqlDAO.findById(User.class, id);
			if(user == null){
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_freezeUserIsNotFind"));
			}else{
				user.setIsfreeze(User.ISFREEZE_FREEZE);
				sqlDAO.save(user);
			}
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_freezeUserPleaseEntryId"));
		}
		
		return baseVO;
	}

	public BaseVO unfreezeUser(int id) {
		BaseVO baseVO = new BaseVO();
		if(id > 0){
			User user = sqlDAO.findById(User.class, id);
			if(user == null){
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_unfreezeUserIsNotFind"));
			}else{
				user.setIsfreeze(User.ISFREEZE_NORMAL);
				sqlDAO.save(user);
			}
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_unfreezeUserPleaseEntryId"));
		}
		return baseVO;
	}

	public UploadFileVO updateHeadByOSS(MultipartFile head) {
		UploadFileVO uploadFileVO = new UploadFileVO();
		if(head == null || head.isEmpty()){
			uploadFileVO.setBaseVO(BaseVO.FAILURE, Language.show("user_uploadHeadImageNotFind"));
			return uploadFileVO;
		}
		
		User user = ShiroFunc.getUser();
		String fileSuffix = "png";
		fileSuffix = Lang.findFileSuffix(Safety.filter(head.getOriginalFilename()));
		String newHead = Lang.uuid()+"."+fileSuffix;
		try {
			uploadFileVO = AttachmentFile.put(Global.get("USER_HEAD_PATH")+newHead, head.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			uploadFileVO.setBaseVO(BaseVO.FAILURE, e.getMessage());
			return uploadFileVO;
		}
		
		User u = sqlDAO.findById(User.class, user.getId());
		//删除之前的头像
		if(u.getHead() != null && u.getHead().length() > 0 && !u.getHead().equals("default.png")){
			AttachmentFile.deleteObject("image/head/"+u.getHead());
		}
		
		u.setHead(newHead);
		sqlDAO.save(u);
		ShiroFunc.getUser().setHead(newHead);
		
//		logDao.insert("USER_UPDATEHEAD");
		
		return uploadFileVO;
	}

	public BaseVO updateSex(HttpServletRequest request) {
		BaseVO baseVO = new BaseVO();
		String sex = Safety.filter(request.getParameter("sex"));
		if(sex == null || sex.length()<0){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_updateSexSexNotIsNull"));
			return baseVO;
		}
		if(!(sex.equals("男") || sex.equals("女"))){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_updateSexEntryFailure"));
			return baseVO;
		}
		User u = sqlDAO.findById(User.class, ShiroFunc.getUser().getId());
		u.setSex(sex);
		sqlDAO.save(u);
//		logDao.insert("USER_UPDATE_SEX", ShiroFunc.getUser().getSex()+"修改为"+sex);
		ShiroFunc.getUser().setSex(sex);
		
		return baseVO; 
	}

	public BaseVO updateNickname(HttpServletRequest request) {
		BaseVO baseVO = new BaseVO();
		String nickname = StringUtil.filterXss(request.getParameter("nickname"));
		if(nickname == null){
			nickname = "";
		}
		if(nickname.length()==0){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_updateNicknameNotNull"));
			return baseVO;
		}
		if(nickname.length()>15){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_updateNicknameSizeFailure"));
			return baseVO;
		}
		
		User u = sqlDAO.findById(User.class, ShiroFunc.getUser().getId());
		u.setNickname(nickname);
		sqlDAO.save(u);
		ShiroFunc.getUser().setNickname(nickname);
		baseVO.setInfo(nickname);
		
		return baseVO;
	}

	public BaseVO updateSign(HttpServletRequest request) {
		BaseVO baseVO = new BaseVO();
		String sign = request.getParameter("sign");
		if(sign == null){
			sign = "";
		}
		//过滤html标签、sql注入、xss
		sign = Safety.filter(StringUtil.filterHtmlTag(sign));
		if(sign.length()>40){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_updateSignSizeFailure"));
			return baseVO;
		}
		
		User u = sqlDAO.findById(User.class, ShiroFunc.getUser().getId());
		u.setSign(sign);
		sqlDAO.save(u);
		ShiroFunc.getUser().setSign(sign);
		
		return baseVO;
	}

	public UploadFileVO updateHeadByOSS(HttpServletRequest request,String formFileName) {
		return updateHeadByOSS(request, formFileName, 0);
	}

	public UploadFileVO updateHeadByOSS(HttpServletRequest request,
			String formFileName, int maxWidth) {
		UploadFileVO uploadFileVO = new UploadFileVO();
		MultipartFile multipartFile = null;
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
			List<MultipartFile> imageList = multipartRequest.getFiles(formFileName);  
			if(imageList.size() == 0){
				Log.debug("上传头像时，未发现头像 ------"+Language.show("user_uploadHeadImageNotFind"));
				uploadFileVO.setResult(UploadFileVO.NOTFILE);
				uploadFileVO.setInfo(Language.show("user_uploadHeadImageNotFind"));
				return uploadFileVO;
			}else{
				Log.debug("上传头像，已发现头像的multipartFile");
				multipartFile = imageList.get(0);
			}
	    }
		
		if(multipartFile == null || multipartFile.isEmpty()){
			uploadFileVO.setBaseVO(BaseVO.FAILURE, Language.show("user_uploadHeadImageNotFind"));
			Log.debug("上传头像的multipartFile为空，不存在上传的头像 ------"+Language.show("user_uploadHeadImageNotFind"));
			return uploadFileVO;
		}
		
		User user = ShiroFunc.getUser();
		uploadFileVO = OSS.uploadImageByMultipartFile(Global.get("USER_HEAD_PATH"), multipartFile, maxWidth);
		if(uploadFileVO.getResult() - UploadFileVO.FAILURE == 0){
			return uploadFileVO;
		}
		
		User u = sqlDAO.findById(User.class, user.getId());
		//删除之前的头像
		if(u.getHead() != null && u.getHead().length() > 0 && !u.getHead().equals("default.png")){
			AttachmentFile.deleteObject(Global.get("USER_HEAD_PATH")+u.getHead());
		}
		
		u.setHead(uploadFileVO.getFileName());
		sqlDAO.save(u);
		ShiroFunc.getUser().setHead(uploadFileVO.getFileName());
		
		return uploadFileVO;
	}

	public BaseVO loginByUserid(HttpServletRequest request, int userid) {
		BaseVO baseVO = new BaseVO();
		
		User user = sqlDAO.findById(User.class, userid);
		if(user == null){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneUserNotFind"));
			return baseVO;
		}
		
		//ip检测
		String ip = IpUtil.getIpAddress(request);
		if(!(user.getLastip().equals(ip) || user.getRegip().equals(ip))){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneIpFailure"));
			return baseVO;
		}
		
		//检验此用户状态是否正常，是否被冻结
		if(user.getIsfreeze() == User.ISFREEZE_FREEZE){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginUserFreeze"));
			return baseVO;
		}
		
		/*******更改User状态******/
		user.setLasttime(DateUtil.timeForUnix10());
		user.setLastip(IpUtil.getIpAddress(request));
		sqlDAO.save(user);
		
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getUsername());
        token.setRememberMe(false);
		Subject currentUser = SecurityUtils.getSubject();  
		
		try {  
			currentUser.login(token);  
		} catch ( UnknownAccountException uae ) {
			uae.printStackTrace();
		} catch ( IncorrectCredentialsException ice ) {
			ice.printStackTrace();
		} catch ( LockedAccountException lae ) {
			lae.printStackTrace();
		} catch ( ExcessiveAttemptsException eae ) {
			eae.printStackTrace();
		} catch ( org.apache.shiro.authc.AuthenticationException ae ) { 
			ae.printStackTrace();
		}
		
//		logDao.insert("USER_LOGIN_SUCCESS");
		baseVO.setBaseVO(BaseVO.SUCCESS, Language.show("user_loginSuccess"));
		return baseVO;
	}

	public BaseVO loginForUserId(HttpServletRequest request, int userId) {
		BaseVO baseVO = new BaseVO();
		User user = sqlDAO.findById(User.class, userId);
		if(user == null){
			Log.debug("用户不存在");
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginByPhoneUserNotFind"));
			return baseVO;
		}
		
		//检验此用户状态是否正常，是否被冻结
		if(user.getIsfreeze() == User.ISFREEZE_FREEZE){
			Log.debug("此用户被冻结，无法设置为登陆用户");
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("user_loginUserFreeze"));
			return baseVO;
		}
		
		/*******更改User状态******/
		user.setLasttime(DateUtil.timeForUnix10());
		user.setLastip(IpUtil.getIpAddress(request));
		sqlDAO.save(user);
		Log.debug("设置指定userId为登陆用户，设置后得User："+user);
		
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getUsername());
        token.setRememberMe(false);
		Subject currentUser = SecurityUtils.getSubject();  
		
		try {  
			currentUser.login(token);  
		} catch ( UnknownAccountException uae ) {
			uae.printStackTrace();
		} catch ( IncorrectCredentialsException ice ) {
			ice.printStackTrace();
		} catch ( LockedAccountException lae ) {
			lae.printStackTrace();
		} catch ( ExcessiveAttemptsException eae ) {
			eae.printStackTrace();
		} catch ( org.apache.shiro.authc.AuthenticationException ae ) { 
			ae.printStackTrace();
		}
		
//		logDao.insert("USER_LOGIN_SUCCESS");
		baseVO.setBaseVO(BaseVO.SUCCESS, Language.show("user_loginSuccess"));
		return baseVO;
	}

	public BaseVO updatePassword(int userid, String newPassword) {
		BaseVO baseVO = new BaseVO();
		if(!(userid > 0)){
			return BaseVO.failure("userid is null");
		}
		if(newPassword == null || newPassword.length() == 0){
			return BaseVO.failure("新密码不能为空");
		}
		User user=sqlDAO.findById(User.class, userid);
		
		String md5Password = new Md5Hash(newPassword, user.getSalt(),Global.USER_PASSWORD_SALT_NUMBER).toString();
		user.setPassword(md5Password);
		sqlDAO.save(user);
		
		return baseVO;
	}

	public BaseVO createUser(User user, HttpServletRequest request) {
		//用户名、密码进行xss、sql防注入
		user.setUsername(Safety.filter(user.getUsername()));
		user.setPassword(Safety.filter(user.getPassword()));
		
		//既然是注册新用户，那么用户名、密码一定是不能为空的
		if(user.getUsername()==null||user.getUsername().equals("")){
			return BaseVO.failure("用户名不能为空！");
		}
		if(user.getPassword()==null||user.getPassword().equals("")){
			return BaseVO.failure("密码不能为空！");
		}
		//用户名长度判断
		if(user.getUsername().length() > 20){
			return BaseVO.failure(Language.show("user_userNameToLong"));
		}
		
		//判断用户名、邮箱、手机号是否有其中已经注册了，唯一性
		//邮箱的唯一，仅当邮箱设置了之后，才会判断邮箱的唯一性
		if(user.getEmail() != null && user.getEmail().length() > 0){
			if(sqlDAO.findByProperty(User.class, "email", user.getEmail()).size() > 0){
				return BaseVO.failure(Language.show("user_regFailureForEmailAlreadyExist"));
			}
		}
		//判断用户名唯一性
		if(sqlDAO.findByProperty(User.class, "username", user.getUsername()).size() > 0){
			return BaseVO.failure(Language.show("user_regFailureForUsernameAlreadyExist"));
		}
		//判断手机号唯一性
		if(user.getPhone() != null && user.getPhone().length() > 0){
			if(findByPhone(user.getUsername()) != null){
				return BaseVO.failure(Language.show("user_regFailureForPhoneAlreadyExist"));
			}
		}
		
		if(user.getRegip() == null){
			user.setRegip(IpUtil.getIpAddress(request));
		}
		if(user.getLastip() == null){
			user.setLastip(IpUtil.getIpAddress(request));
		}
		if(user.getRegtime() == null){
			user.setRegtime(DateUtil.timeForUnix10());
		}
		if(user.getLasttime() == null){
			user.setLasttime(DateUtil.timeForUnix10());
		}
		if(user.getNickname() == null){
			user.setNickname(user.getUsername());
		}else{
			user.setNickname(StringUtil.filterXss(Sql.filter(user.getNickname())));
		}
		if(user.getAuthority() == null){
			user.setAuthority(Global.get("USER_REG_ROLE"));
		}
		if(user.getCurrency() == null){
			user.setCurrency(0);
		}
		if(user.getReferrerid() == null){
			//当前登录的用户id
			user.setReferrerid(ShiroFunc.getUserId());
		}
		if(user.getIsfreeze() == null){
			user.setIsfreeze(User.ISFREEZE_NORMAL);
		}
		if(user.getHead() == null){
			user.setHead("default.png");
		}else{
			user.setHead(Safety.filter(user.getHead()));
		}
		if(user.getId() != null){
			user.setId(null);
		}
		
		/* 密码加密，保存 */
		Random random = new Random();
		user.setSalt(random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+""+random.nextInt(10)+"");
		String md5Password = generateMd5Password(user.getPassword(), user.getSalt());
		user.setPassword(md5Password);
		sqlDAO.save(user);
		
		if(user.getId()>0){
			//已注册成功
			
			//赋予该用户系统设置的默认角色
			UserRole userRole = new UserRole();
			userRole.setRoleid(Lang.stringToInt(user.getAuthority(), Global.getInt("USER_REG_ROLE")));
			userRole.setUserid(user.getId());
			sqlDAO.save(userRole);
			
			BaseVO vo = new BaseVO();
			vo.setBaseVO(BaseVO.SUCCESS, user.getId()+"");
			return vo;
		}else{
			return BaseVO.failure(Language.show("user_regFailure"));
		}
	}

	public String getHead(String defaultHead) {
		String head = null;
		
		User user = ShiroFunc.getUser();
		if(user == null){
			head = defaultHead;
		}else{
			if(user.getHead() != null && user.getHead().length() > 10){
				//判断头像是绝对路径还是相对路径的
				if(user.getHead().indexOf("http:") == 0 || user.getHead().indexOf("https:") == 0 || user.getHead().indexOf("//") == 0){
					//如果发现头像是绝对路径，直接将其赋予head，原样返回
					head = user.getHead();
				}else{
					//是相对路径，那就要增加前缀了
					if(user.getHead().equals("default.png")){
						head = defaultHead;
					}else{
						head = AttachmentFile.netUrl() + Global.get("USER_HEAD_PATH") + user.getHead();
					}
				}
			}else{
				head = defaultHead;
			}
		}
		
		return head;
	}
	
	
	
	public String generateMd5Password(String originalPassword, String salt){
        return new Md5Hash(originalPassword, salt ,Global.USER_PASSWORD_SALT_NUMBER).toString();
	}
}
