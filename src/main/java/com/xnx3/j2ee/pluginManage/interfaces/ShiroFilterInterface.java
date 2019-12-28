package com.xnx3.j2ee.pluginManage.interfaces;

import java.util.Map;

/**
 * Shiro 权限，哪个url、目录需要登录，哪个不需要登录，在这里修改
 * @author 管雷鸣
 */
public interface ShiroFilterInterface {
	
	/**
	 * 哪个url、目录需要登录，哪个不需要登录，在这里修改。如果不设置的url，新增的目录默认是被拦截的
	 * @param filterChainDefinitionMap 现在 Shiro 中，针对url的设定
	 * 			<ul>
	 * 				<li>map.key 要拦截的url，如 /site/template.do 、  /site**  、  /*.do</li>
	 * 				<li>map.value 要执行什么拦截，取值：
	 * 					<ul>
	 * 						anon:用户未登录状态下，不进行拦截，让其能正常访问
	 * 					</ul>
	 * 				</li>
	 * 			</ul>
	 * @return 更改后的针对url的设定，会讲这个返回值重新赋予 Shiro
	 */
	public Map<String, String> shiroFilter(Map<String, String> filterChainDefinitionMap);
	
	
}