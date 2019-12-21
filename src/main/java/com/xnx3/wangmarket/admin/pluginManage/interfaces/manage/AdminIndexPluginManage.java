package com.xnx3.wangmarket.admin.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.util.ConsoleUtil;

/**
 * CMS网站管理后台首页的html源码处理
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForAdminIndex")
public class AdminIndexPluginManage {
	//处理html源代码的插件，这里开启项目时，便将有关此的插件加入此处
	public static List<Class<?>> classList;
	static{
		List<Class<?>> allClassList = ScanClassUtil.getClasses("com.xnx3.wangmarket");
		classList = ScanClassUtil.searchByInterfaceName(allClassList, "com.xnx3.wangmarket.pluginManage.interfaces.AdminIndexInterface");
		for (int i = 0; i < classList.size(); i++) {
			ConsoleUtil.info("装载 AdminIndex 插件："+classList.get(i).getName());
		}
	}
	
	/**
	 * 网站管理后台追加的html
	 * @return 要追加到html最后面的 html代码
	 */
	public static String siteAdminIndexManage() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		/**** 针对html源代码处理的插件 ****/
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invoke = null;
			invoke = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("siteAdminIndexAppendHtml",new Class[]{});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invoke, new Object[]{});
			if(o != null && !o.equals("null")){
				sb.append(o.toString());
			}
		}
		return sb.toString();
	}

	/**
	 * 代理后台追加的html
	 * @return 要追加到html最后面的 html代码
	 */
	public static String agencyAdminIndexManage() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		/**** 针对html源代码处理的插件 ****/
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invoke = null;
			invoke = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("agencyAdminIndexAppendHtml",new Class[]{});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invoke, new Object[]{});
			if(o != null && !o.equals("null")){
				sb.append(o.toString());
			}
		}
		return sb.toString();
	}
	

	/**
	 * 超级管理后台追加的html
	 * @return 要追加到html最后面的 html代码
	 */
	public static String superAdminIndexManage() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		/**** 针对html源代码处理的插件 ****/
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invoke = null;
			invoke = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("superAdminIndexAppendHtml",new Class[]{});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invoke, new Object[]{});
			if(o != null && !o.equals("null")){
				sb.append(o.toString());
			}
		}
		return sb.toString();
	}
	
	
}
