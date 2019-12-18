package com.xnx3.j2ee.func;

import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.shiro.UserBean;

/**
 * session 操作
 * @author 管雷鸣
 *
 */
public class SessionUtil extends ShiroFunc{
	
	/**
	 * 从Shrio的Session中获取当前用户的缓存信息
	 */
	public static UserBean getUserBeanForSession(){
		return getUserBeanForShiroSession();
	}
}
