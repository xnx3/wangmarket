package com.xnx3.j2ee.pluginManage.interfaces;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Spring MVC 拦截器 扩展的接口
 * @author 管雷鸣
 */
public interface SpringMVCInterceptorInterface extends HandlerInterceptor{
	
	/**
	 * 该拦截器拦截哪些url，如：
	 * 	<ul>
	 * 		<li>/test.do : 只针对/test.do 这个url进行拦截，走这个拦截器</li>
	 * 		<li>/shop/** : 对所有/shop/下的url进行拦截，走这个拦截器</li>
	 * 	</ul>
	 * @return 字符串，值如 "/text.do"
	 */
	public String pathPatterns();
}