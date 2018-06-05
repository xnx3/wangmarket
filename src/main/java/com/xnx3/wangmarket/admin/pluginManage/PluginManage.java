package com.xnx3.wangmarket.admin.pluginManage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;
import com.xnx3.wangmarket.admin.util.ScanClass;

/**
 * 插件管理
 * @author 管雷鸣
 *
 */
@Component
public class PluginManage {
	
	public PluginManage() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		scanPluginClass();
	}
	
	/**
	 * 网站管理后台相关的插件
	 * key：插件名字，如自定义表单插件，便是 formManage
	 */
	public static Map<String, SitePluginBean> siteClassManage;
	static{
		siteClassManage = new HashMap<String, SitePluginBean>();
	}
	
	/**
	 * 扫描插件注册类
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void scanPluginClass() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		Set<Class<?>> clazzs = ScanClass.getClasses("com.xnx3.wangmarket");
        if (clazzs == null) {
//        	Log.info("扫描插件注册，无插件");
            return;
        }
        
        for (Class<?> clazz : clazzs) {
            // 获取类上的注解
            Annotation[] annos = clazz.getAnnotations();
            
            for (int j = 0; j < annos.length; j++) {
				if(annos[j].annotationType().getSimpleName().equals("PluginRegister")){
					//找到插件注册类了，进行注册插件
					registerPlugin(clazz);
				}
			}
        }
        
        
	}
	
	public static void registerPlugin(Class c) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		PluginRegister an = (PluginRegister) c.getAnnotation(PluginRegister.class);
        if(an != null){
        	SitePluginBean sitePlugin = new SitePluginBean(c);
        	siteClassManage.put(sitePlugin.getId(), sitePlugin);	//将之加入内存中持久化，以便随时使用
        	
        	 //执行 init 方法
        	 Object invokeTester = c.newInstance();                                 //运用newInstance()来生成这个新获取方法的实例  
             Method initMethod = c.getMethod("init",new Class[]{});	//获取要调用的init方法  
             boolean result = (Boolean) initMethod.invoke(invokeTester);      
        	 System.out.println(result);
        }
	}
	
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		scanPluginClass();
	}
}
