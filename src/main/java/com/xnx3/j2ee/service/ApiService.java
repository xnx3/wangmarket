package com.xnx3.j2ee.service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.xnx3.j2ee.entity.Role;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.UserVO;

/**
 * Api接口相关。做Api接口会用到此
 * @author 管雷鸣
 *
 */
public interface ApiService {
	
	/**
	 * api接口身份校验，仅仅只是验证身份，不会存储session
	 * @param key 要校验的key， id_password的4次加密，128长度的字符
	 * @return {@link UserVO} 成功：vo.getResult = success
	 */
	public UserVO identityVerify(String key);
	
	/**
	 * api接口身份校验，+ 身份校验成功将 User 信息存储到 session，如正常登录状态
	 * @param key 要校验的key， id_password的4次加密，128长度的字符
	 * @return {@link UserVO} 成功：vo.getResult = success
	 */
	public UserVO identityVerifyAndSession(String key);
	
	/**
	 * 获取用户登录后的，用于自动登录的唯一标示。每个用户的登录标示都是唯一的，当用户更改密码后，登录标示也会跟随更改。
	 * <br/>必须是登录用户才可使用。因为登录标示是user.id ＋ user.password 构成的
	 * @return 
	 * 		<ul>
	 * 			<li>若已登录：如： 123_asdhjashdjasdk...128位长度字符串</li>
	 * 			<li>若未登录：返回 null</li>
	 * 		</ul>
	 */
	public String getKey();
}