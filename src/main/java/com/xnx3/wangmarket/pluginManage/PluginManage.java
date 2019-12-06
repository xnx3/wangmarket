package com.xnx3.wangmarket.pluginManage;

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
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.func.Log;
import com.xnx3.wangmarket.pluginManage.extend.AutoCreateReceiveMQForDomain;
import com.xnx3.wangmarket.pluginManage.extend.AutoLoadSimplePluginTableDateThread;
import com.xnx3.wangmarket.pluginManage.extend.DatabaseLoadFinishThread;

/**
 * 注册的插件管理
 * @author 管雷鸣
 *
 */
@Component
public class PluginManage {
	public PluginManage() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		scanPluginClass();
		
		scanPluginExtend();
	}
	
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
			registerPlugin(clazz);
			Log.info("注册插件："+clazz.getName());
        }
	}
	
	/**
	 * 注册插件
	 * @param c 要注册的插件的class， 如 com.xnx3.wangmarket.plugin.learnExample.Plugin
	 */
	public static void registerPlugin(Class c) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		
		if(c.getAnnotation(PluginRegister.class) != null){
			//v4.12 及之后的版本，使用 com.xnx3.wangmarket.pluginManage.PluginRegister 注解
			PluginRegister plugin = (PluginRegister) c.getAnnotation(PluginRegister.class);
			String pluginId = Func.getPluginId(c.getName());
			if(pluginId == null || pluginId.length() == 0){
				return;
			}
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(plugin);
			try {
				Field value = invocationHandler.getClass().getDeclaredField("memberValues");
				value.setAccessible(true);
		        Map<String, Object> memberValues = (Map<String, Object>) value.get(invocationHandler);
		        memberValues.put("id", pluginId);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
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
		}else{
			//v4.12之前的版本，插件使用 com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister 注解
			com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister pluginOld = (com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister) c.getAnnotation(com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister.class);
			if(pluginOld != null){
				PluginRegister plugin = new PluginRegister() {
					
					@Override
					public Class<? extends Annotation> annotationType() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public String versionMin() {
						// TODO Auto-generated method stub
						return pluginOld.versionMin();
					}
					
					@Override
					public String versionCheckUrl() {
						// TODO Auto-generated method stub
						return pluginOld.versionCheckUrl();
					}
					
					@Override
					public String version() {
						// TODO Auto-generated method stub
						return pluginOld.version();
					}
					
					@Override
					public String menuTitle() {
						// TODO Auto-generated method stub
						return pluginOld.menuTitle();
					}
					
					@Override
					public String menuHref() {
						// TODO Auto-generated method stub
						return pluginOld.menuHref();
					}
					
					@Override
					public String intro() {
						// TODO Auto-generated method stub
						return pluginOld.intro();
					}
					
					@Override
					public String detailUrl() {
						// TODO Auto-generated method stub
						return pluginOld.detailUrl();
					}
					
					@Override
					public boolean applyToSuperAdmin() {
						// TODO Auto-generated method stub
						return pluginOld.applyToSuperAdmin();
					}
					
					
					@Override
					public boolean applyToCMS() {
						// TODO Auto-generated method stub
						return pluginOld.applyToCMS();
					}
					
					@Override
					public boolean applyToAgency() {
						// TODO Auto-generated method stub
						return pluginOld.applyToAgency();
					}

					@Override
					public String id() {
						return pluginOld.id();
					}
				};
				
				if(plugin.applyToCMS()){
	        		cmsSiteClassManage.put(pluginOld.id(), plugin);
	        	}
	        	if(plugin.applyToAgency()){
	        		agencyClassManage.put(pluginOld.id(), plugin);
	        	}
	        	if(plugin.applyToSuperAdmin()){
	        		superAdminClassManage.put(pluginOld.id(), plugin);
	        	}
			}
		}
		
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
	
	/**
	 * 扫描插件扩展，也就是扫描继承了 com.xnx3.wangmarket.pluginManage.PluginExtend 的类
	 */
	public static void scanPluginExtend(){
		List<Class<?>> list = ScanClassUtil.getClasses("com.xnx3.wangmarket.plugin");
		for (int i = 0; i < list.size(); i++) {
			Class c = list.get(i);
			
			//找出继承了 com.xnx3.wangmarket.pluginManage.PluginExtend 的类
			if(c.getSuperclass() != null && c.getSuperclass().equals(PluginExtend.class)){
				//该class 继承了 PluginExtend
				Method[] methods = c.getDeclaredMethods();
				for (int j = 0; j < methods.length; j++) {
					
					Method method = methods[j];
					switch (method.getName()) {
					case "autoLoadSimplePluginTableDate":
						AutoLoadSimplePluginTableDateThread.execute(c, method);
						break;
					case "autoCreateReceiveMQForDomain":
						AutoCreateReceiveMQForDomain.execute(c, method);
						break;
					case "databaseLoadFinish":
						DatabaseLoadFinishThread thread = new DatabaseLoadFinishThread(c, method);
						thread.start();
						break;
					default:
						break;
					}
				}
			}
			
		}
	}
	
}
