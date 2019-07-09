package com.xnx3.wangmarket.admin.pluginManage;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.func.Log;
import com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister;

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
	
	//网站管理后台相关的插件，key：插件名字，如自定义表单插件，便是 formManage
	public static Map<String, SitePluginBean> cmsSiteClassManage;
	public static Map<String, SitePluginBean> wapSiteClassManage;	//这种模式废弃了
	public static Map<String, SitePluginBean> pcSiteClassManage;	//这种模式废弃了
	public static Map<String, SitePluginBean> agencyClassManage;
	public static Map<String, SitePluginBean> superAdminClassManage;
	static{
		cmsSiteClassManage = new HashMap<String, SitePluginBean>();
		wapSiteClassManage = new HashMap<String, SitePluginBean>();
		pcSiteClassManage = new HashMap<String, SitePluginBean>();
		agencyClassManage = new HashMap<String, SitePluginBean>();
		superAdminClassManage = new HashMap<String, SitePluginBean>();
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
		List<Class<?>> classList = ScanClassUtil.getClassSearchAnnotationsName(ScanClassUtil.getClasses("com.xnx3.wangmarket"), "PluginRegister");
        for (Class<?> clazz : classList) {
        	//找到插件注册类了，进行注册插件
			registerPlugin(clazz);
			Log.info("注册插件："+clazz.getName());
        }
	}
	
	public static void registerPlugin(Class c) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		PluginRegister an = (PluginRegister) c.getAnnotation(PluginRegister.class);
        if(an != null){
        	SitePluginBean sitePlugin = new SitePluginBean(c);
        	if(sitePlugin.isApplyToCMS()){
        		cmsSiteClassManage.put(sitePlugin.getId(), sitePlugin);	//将之加入内存中持久化，以便随时使用
        	}
        	if(sitePlugin.isApplyToWAP()){
        		wapSiteClassManage.put(sitePlugin.getId(), sitePlugin);	//将之加入内存中持久化，以便随时使用
        	}
        	if(sitePlugin.isApplyToPC()){
        		pcSiteClassManage.put(sitePlugin.getId(), sitePlugin);	//将之加入内存中持久化，以便随时使用
        	}
        	if(sitePlugin.isApplyToAgency()){
        		agencyClassManage.put(sitePlugin.getId(), sitePlugin);	//将之加入内存中持久化，以便随时使用
        	}
        	if(sitePlugin.isApplyToSuperAdmin()){
        		superAdminClassManage.put(sitePlugin.getId(), sitePlugin);	//将之加入内存中持久化，以便随时使用
        	}
        	
//        	 //执行 init 方法
//        	 Object invokeTester = c.newInstance();                                 //运用newInstance()来生成这个新获取方法的实例  
//             Method initMethod = c.getMethod("init",new Class[]{});	//获取要调用的init方法  
//             boolean result = (Boolean) initMethod.invoke(invokeTester);      
        }
	}
	
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		scanPluginClass();
	}
}
