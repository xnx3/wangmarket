package com.xnx3.j2ee.pluginManage.interfaces;

import java.util.ArrayList;
import java.util.List;

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
	 * @return 拦截的url列表，这里的url将都会走这个拦截器。返回如
	 * 	<pre>
	 * 		List&lt;String&gt; list = new ArrayList&lt;String&gt;();
	 * 		list.add("/test.do");
	 * 		list.add("/test2.do");
	 * 		list.add("/shop/**");
	 * 		return list;
	 * 	</pre>
	 */
	public List<String> pathPatterns();
}