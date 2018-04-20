package com.xnx3.j2ee.service.impl;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.xnx3.Lang;
import com.xnx3.MD5Util;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.UserVO;

@Service
public class ApiServiceImpl implements ApiService {
	@Resource
	private SqlDAO sqlDAO;

	public SqlDAO getSqlDAO() {
		return sqlDAO;
	}

	public void setSqlDAO(SqlDAO sqlDAO) {
		this.sqlDAO = sqlDAO;
	}

	public UserVO identityVerify(String key) {
		UserVO vo = new UserVO();
		/*
		 * 验证Key的格式
		 */
		if(key.length() < 128 || key.indexOf("_") == -1){
			vo.setBaseVO(UserVO.FAILURE, "key错误1");
			return vo;
		}
		
		String[] ks = key.split("_");
		if(ks[0].length() == 0 || ks[1].length() == 0){
			vo.setBaseVO(UserVO.FAILURE, "key错误2");
			return vo;
		}
		
		int userid = Lang.stringToInt(ks[0], 0);
		if(userid == 0){
			vo.setBaseVO(UserVO.FAILURE, "key错误3");
			return vo;
		}
		String pwd = StringUtil.removeBlank(ks[1]);
		if(pwd.length() != 128){
			vo.setBaseVO(UserVO.FAILURE, "key错误4");
			return vo;
		}
		
		/*
		 * 验证Key是否存在
		 * 获取代理商的user信息
		 */
		User user = sqlDAO.findById(User.class, userid);
		if(user == null){
			//统一提示，避免被利用
			vo.setBaseVO(UserVO.FAILURE, "key错误5");
			return vo;	
		}
		if(!passwordMD5(user.getPassword()).equals(pwd)){
			vo.setBaseVO(UserVO.FAILURE, "key错误6");
			return vo;
		}
		
		vo.setUser(user);
		return vo;
	}


	/**
	 * 将32位的 user.password 密码进行再加密，生成128位加密字符串
	 * @param password user.password 密码
	 * @return 新生成的128位加密字符串
	 */
	private static String passwordMD5(String password){
		String p1 = password.substring(0, 8);
		String p2 = password.substring(8, 16);
		String p3 = password.substring(16, 24);
		String p4 = password.substring(24, 32);
		
		return MD5Util.MD5(p1)+MD5Util.MD5(p2)+MD5Util.MD5(p3)+MD5Util.MD5(p4);
	}

	public UserVO identityVerifyAndSession(String key) {
		UserVO vo = identityVerify(key);
		if(vo.getResult() - UserVO.FAILURE == 0){
			return vo;
		}
		
		UsernamePasswordToken token = new UsernamePasswordToken(vo.getUser().getUsername(), vo.getUser().getUsername());
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
		
		return vo;
	}

	public String getKey() {
		User user = ShiroFunc.getUser();
		if(user == null){
			return null;
		}
		
		return user.getId()+"_"+passwordMD5(user.getPassword());
	}
	
}
