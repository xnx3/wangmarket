package com.xnx3.j2ee.shiro;

import java.io.Serializable;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

/**
 * Session 管理，session id 可以正常使用cookie中的，也可以使用 token传递的。这里是为了扩展app开发增加的
 * <br/>session id 取值的优先级：
 * <br/> 1. 使用 request header中，name为token 的值
 * <br/> 2. 使用 request params (GET、POST都行)中，name 为 token 的值
 * <br/> 3. 使用 Cookie 中，name 为 iwSID 的值
 * @author 管雷鸣
 *
 */
public class SessionManager extends DefaultWebSessionManager{
	public static final String TOKEN_NAME = "token";	//get、post传递的sessionid的name
	
	public SessionManager() {
        super();
    }

	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		//获取请求头中的 token 的值，如果请求头中有 AUTH_TOKEN 则其值为sessionId。shiro就是通过sessionId 来控制的
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String token = httpRequest.getHeader(TOKEN_NAME);
		if(token == null || token.length() < 3){
			token = request.getParameter(TOKEN_NAME);
		}
		if(token == null || token.length() < 3){
			return super.getSessionId(request, response);
		}else{
			//请求头中如果有 token, 则其值为sessionId
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "request header token");
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
			return token;
		}
		
	}
	
	
	
}
