package com.xnx3.wangmarket.admin.pluginManage;

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
import com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister;

/**
 * 注册的插件管理
 * @author 管雷鸣
 * @deprecated 已废弃，v5.0以前版本使用的， 5.0以后使用 com.xnx3.j2ee.plugin.PluginManage
 */
public class PluginManage {
	
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
	}
	
	/**
	 * 注册插件，这里只是注册v5.0以前的版本插件
	 * @param c 要注册的插件的class， 如 com.xnx3.wangmarket.plugin.learnExample.Plugin
	 */
	public static void registerPlugin(Class c) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister pluginOld = (com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister) c.getAnnotation(com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister.class);
		if(pluginOld != null){
			com.xnx3.j2ee.pluginManage.PluginRegister plugin = new com.xnx3.j2ee.pluginManage.PluginRegister() {
				
				@Override
				public Class<? extends Annotation> annotationType() {
					return null;
				}
				
				@Override
				public String versionMin() {
					return pluginOld.versionMin();
				}
				
				@Override
				public String versionCheckUrl() {
					return pluginOld.versionCheckUrl();
				}
				
				@Override
				public String version() {
					return pluginOld.version();
				}
				
				@Override
				public String menuTitle() {
					return pluginOld.menuTitle();
				}
				
				@Override
				public String menuHref() {
					return pluginOld.menuHref();
				}
				
				@Override
				public String intro() {
					return pluginOld.intro();
				}
				
				@Override
				public String detailUrl() {
					return pluginOld.detailUrl();
				}
				
				@Override
				public boolean applyToSuperAdmin() {
					return pluginOld.applyToSuperAdmin();
				}
				
				
				@Override
				public boolean applyToCMS() {
					return pluginOld.applyToCMS();
				}
				
				@Override
				public boolean applyToAgency() {
					return pluginOld.applyToAgency();
				}

				@Override
				public String id() {
					return pluginOld.id();
				}
			};
			
			if(plugin.applyToCMS()){
        		com.xnx3.j2ee.pluginManage.PluginManage.cmsSiteClassManage.put(pluginOld.id(), plugin);
        	}
        	if(plugin.applyToAgency()){
        		com.xnx3.j2ee.pluginManage.PluginManage.agencyClassManage.put(pluginOld.id(), plugin);
        	}
        	if(plugin.applyToSuperAdmin()){
        		com.xnx3.j2ee.pluginManage.PluginManage.superAdminClassManage.put(pluginOld.id(), plugin);
        	}
		}
		
	}
	
}
