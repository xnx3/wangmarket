package com.xnx3.wangmarket.domain.pluginManage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.func.Log;
import com.xnx3.wangmarket.domain.bean.RequestInfo;
import com.xnx3.wangmarket.domain.bean.SimpleSite;

/**
 * 插件管理
 * @author 管雷鸣
 *
 */
@Component
public class DomainPluginManage {
	//处理html源代码的插件，这里开启项目时，便将有关此的插件加入此处
	public static List<Class<?>> domainClassList;
	static{
		List<Class<?>> classList = ScanClassUtil.getClasses("com.xnx3.wangmarket");
		domainClassList = ScanClassUtil.searchByInterfaceName(classList, "com.xnx3.wangmarket.domain.pluginManage.interfaces.DomainVisitInterface");
		for (int i = 0; i < domainClassList.size(); i++) {
			Log.info("装载插件："+domainClassList.get(i).getName());
		}
	}
	
	public static String manage(String html, SimpleSite simpleSite, RequestInfo requestInfo) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		/**** 针对html源代码处理的插件 ****/
//		VisitInterface 
		
		for (int i = 0; i < domainClassList.size(); i++) {
			Class<?> c = domainClassList.get(i);
			Object invoke = null;
			invoke = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("htmlManage",new Class[]{String.class, SimpleSite.class, RequestInfo.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invoke, new Object[]{html, simpleSite, requestInfo});
			html = o.toString();
		}
		return html;
	}
	
}
