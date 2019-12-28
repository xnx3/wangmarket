package com.xnx3.j2ee.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.util.ConsoleUtil;

/**
 * Shiro 权限，哪个url、目录需要登录，哪个不需要登录，在这里修改
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForShiroFilter")
public class ShiroFilterPluginManage {
	//处理html源代码的插件，这里开启项目时，便将有关此的插件加入此处
	public static List<Class<?>> classList;
	static{
		List<Class<?>> allClassList = ScanClassUtil.getClasses("com.xnx3");
		classList = ScanClassUtil.searchByInterfaceName(allClassList, "com.xnx3.j2ee.pluginManage.interfaces.ShiroFilterInterface");
		for (int i = 0; i < classList.size(); i++) {
			ConsoleUtil.info("装载 ShiroFilter 插件："+classList.get(i).getName());
		}
	}
	
	/**
	 * Shiro Filter 过滤的url map
	 * @param filterChainDefinitionMap 现在 Shiro 中，针对url的设定
	 * @return 更改后的针对url的设定，会讲这个返回值重新赋予 Shiro
	 */
	public static Map<String, String> manage(Map<String, String> filterChainDefinitionMap) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		/**** 针对html源代码处理的插件 ****/
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invoke = null;
			invoke = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("shiroFilter",new Class[]{Map.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invoke, new Object[]{filterChainDefinitionMap});
			if(o != null && !o.equals("null")){
				filterChainDefinitionMap = (Map<String, String>) o;
			}
		}
		return filterChainDefinitionMap;
	}
	
	
}
