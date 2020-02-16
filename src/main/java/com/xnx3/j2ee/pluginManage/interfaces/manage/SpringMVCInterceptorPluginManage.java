package com.xnx3.j2ee.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.util.ConsoleUtil;

/**
 * Spring MVC 拦截器 扩展的接口
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForSpringMVCInterceptor")
public class SpringMVCInterceptorPluginManage {
	//这里开启项目时，便将有关此的插件加入此处
	private static List<Class<?>> classList;
	/**
	 * map： 
	 * 		key: class、pathPatterns
	 * 		value: HandlerInterceptor、 List<String>
	 */
	public static List<Map<String, Object>> handlerInterceptorList;
	
	static{
		handlerInterceptorList = new ArrayList<Map<String,Object>>();
		
		List<Class<?>> allClassList = ScanClassUtil.getClasses("com.xnx3");
		classList = ScanClassUtil.searchByInterfaceName(allClassList, "com.xnx3.j2ee.pluginManage.interfaces.SpringMVCInterceptorInterface");
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invoke = null;
			try {
				invoke = c.newInstance();
				//运用newInstance()来生成这个新获取方法的实例  
				Method m = c.getMethod("pathPatterns",new Class[]{});
				//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
				Object o = m.invoke(invoke, new Object[]{});
				List<String> pathPatterns = null;
				if(o != null && !o.equals("null")){
					pathPatterns = (List<String>)o;
				}
				if(pathPatterns != null){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("class", (HandlerInterceptor) c.newInstance());
					map.put("pathPatterns", pathPatterns);
					
					SpringMVCInterceptorPluginManage.handlerInterceptorList.add(map);
					ConsoleUtil.info("装载 SpringMVCInterceptor 插件："+classList.get(i).getName());
				}
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
}
