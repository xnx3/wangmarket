package com.xnx3.j2ee.pluginManage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.xnx3.ClassUtil;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.PluginUtil;

/**
 * 注册的插件管理
 * @author 管雷鸣
 *
 */
public class PluginManage {
	//网站管理后台相关的插件，key：插件名字，如自定义表单插件，便是 formManage
	public static Map<String, PluginRegister> cmsSiteClassManage;
	public static Map<String, PluginRegister> agencyClassManage;
	public static Map<String, PluginRegister> superAdminClassManage;
	static{
		cmsSiteClassManage = new HashMap<String, PluginRegister>();
		agencyClassManage = new HashMap<String, PluginRegister>();
		superAdminClassManage = new HashMap<String, PluginRegister>();
	}
	
	/**
	 * 扫描插件注册类
	 */
	public static void scanPluginClass() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		List<Class<?>> classList = ScanClassUtil.getClassSearchAnnotationsName(ScanClassUtil.getClasses("com.xnx3.wangmarket"), "PluginRegister");
        for (Class<?> clazz : classList) {
        	//找到插件注册类了，进行注册插件
        	if(clazz.getAnnotation(PluginRegister.class) != null){
        		registerPlugin(clazz);
    			ConsoleUtil.info("注册插件："+clazz.getName());
        	}
        }
        
        //判断一下，如果有 com.xnx3.wangmarket.admin 包，那么就扫描这个5.0之前的插件
        if(ClassUtil.classExist("com.xnx3.wangmarket.admin.pluginManage.PluginManage")){
        	Class c;
			try {
				c = Class.forName("com.xnx3.wangmarket.admin.pluginManage.PluginManage");
				Object invoke = c.newInstance();
				//运用newInstance()来生成这个新获取方法的实例  
				Method m = c.getMethod("scanPluginClass",new Class[]{});	//获取要调用的init方法  
				//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
				m.invoke(invoke, new Object[]{});
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 移除某个插件在缓存中的信息。注意，这个只是吧注解注册进缓存中的信息移除了，正常浏览时 功能插件 中也是没有这个插件了，但是class文件还是有的。这个并不是移除class文件
	 */
	public static void removePluginCache(String pluginId){
		if(pluginId == null || pluginId.length() == 0){
			return;
		}
		if(cmsSiteClassManage.get(pluginId) != null){
			cmsSiteClassManage.remove(pluginId);
		}
		if(agencyClassManage.get(pluginId) != null){
			agencyClassManage.remove(pluginId);
		}
		if(superAdminClassManage.get(pluginId) != null){
			superAdminClassManage.remove(pluginId);
		}
	}
	
	/**
	 * 注册插件
	 * @param c 要注册的插件的class， 如 com.xnx3.wangmarket.plugin.learnExample.Plugin
	 * @return true:成功； false:失败
	 */
	public static boolean registerPlugin(Class c) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		PluginRegister plugin = (PluginRegister) c.getAnnotation(PluginRegister.class);
		if(plugin == null){
			return false;
		}
		String pluginId = PluginUtil.getPluginId(c.getName());
		if(pluginId == null || pluginId.length() == 0){
			return false;
		}
		//自动获取id，并赋予注解中
		InvocationHandler invocationHandler = Proxy.getInvocationHandler(plugin);
		try {
			Field value = invocationHandler.getClass().getDeclaredField("memberValues");
			value.setAccessible(true);
	        Map<String, Object> memberValues = (Map<String, Object>) value.get(invocationHandler);
	        memberValues.put("id", pluginId);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return false;
		}
		
		if(plugin != null){
			if(plugin.applyToCMS()){
        		cmsSiteClassManage.put(plugin.id(), plugin);
        	}
        	if(plugin.applyToAgency()){
        		agencyClassManage.put(plugin.id(), plugin);
        	}
        	if(plugin.applyToSuperAdmin()){
        		superAdminClassManage.put(plugin.id(), plugin);
        	}
		}
		
		return true;
	}
	
	/**
	 * 获取当前系统内所有已注册的插件
	 * @return {@link Map} 如果为空，则 map.size() == 0 。 map不会返回null
	 * 		<ul>
	 * 			<li>key: pluginId ，如  kefu 、api、learnExample </li>
	 * 			<li>value: {@link PluginRegister}</li>
	 * 		</ul>
	 */
	public static Map<String, PluginRegister> getAllInstallPlugin(){
		//将所有当前安装的插件，都加入这个map中。因为有可能一个插件在总管理后台中出现，也会在代理后台中出现，用map也会进行排重，所以不用list
		Map<String, PluginRegister> pluginMap = new HashMap<String, PluginRegister>();
		
		//将 总管理后台中的插件加入 pluginMap
		for (Map.Entry<String, PluginRegister> entry : PluginManage.superAdminClassManage.entrySet()) {
			pluginMap.put(entry.getKey(), entry.getValue());
		}
		//将 代理后台中的插件加入 pluginMap
		for (Map.Entry<String, PluginRegister> entry : PluginManage.agencyClassManage.entrySet()) {
			pluginMap.put(entry.getKey(), entry.getValue());
		}
		//将 网站管理后台中的插件加入 pluginMap
		for (Map.Entry<String, PluginRegister> entry : PluginManage.cmsSiteClassManage.entrySet()) {
			pluginMap.put(entry.getKey(), entry.getValue());
		}
		
		return pluginMap;
	}
	
}

/**
 * 项目启动后，自动开启插件扫描
 * @author 管雷鸣
 */
@Component
class PluginScan{
	public PluginScan() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		PluginManage.scanPluginClass();
	}
}