package com.xnx3.wangmarket.admin.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.cache.TemplateCMS;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 点击生成整站后，再网站生成的html存之前，对html进行处理
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForTemplateInterfaceManage")
public class TemplateInterfaceManage {
	//这里开启项目时，便将有关此的插件加入此处
	public static List<Class<?>> classList;
	static{
		classList = new ArrayList<Class<?>>();
		
		new Thread(new Runnable() {
			public void run() {
				while(SpringUtil.getApplicationContext() == null){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				//当 SpringUtil 被Spring 加载后才会执行
				List<Class<?>> cl = ScanClassUtil.getClasses("com.xnx3.wangmarket");
				classList = ScanClassUtil.searchByInterfaceName(cl, "com.xnx3.wangmarket.admin.pluginManage.interfaces.TemplateInterface");
				for (int i = 0; i < classList.size(); i++) {
					ConsoleUtil.info("装载 TemplateInterface 插件："+classList.get(i).getName());
				}
			}
		}).start();
	}
	
	/**
	 * 对公共标签进行一些附加。相当于是对这些的扩展 http://tag.wscso.com/2936.html
	 * @param text 要替换的源内容
	 * @param template TemplateCMS
	 * @return 如果result成功，则info为text替换完成的内容
	 */
	public static String publicTag(String text, TemplateCMS template) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = null;
			try {
				m = c.getMethod("publicTag",new Class[]{String.class,TemplateCMS.class});  
			}catch (java.lang.NoSuchMethodException e) {
				if(Global.isJarRun) {
					ConsoleUtil.error(c.getSimpleName() + " 初始化异常，系统判断您当前是在开发环境中运行的，这是由于您使用了热部署而导致的，此异常忽略即可。正常线上部署，放到tomcat中运行时就不会再有热部署，不会再有这个问题");
				}
				e.printStackTrace();
				throw new NoSuchMethodException();
			}
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invokeReply, new Object[]{text, template});
			if(o != null){
				BaseVO vo = (BaseVO) o;
				if(vo.getResult() - BaseVO.FAILURE == 0) {
					ConsoleUtil.error("publicTag 异常:"+vo.getInfo());
					return text;
				}else {
					return vo.getInfo();
				}
			}
		}
		return text;
	}
	

	/**
	 * 栏目标签的附加，相当于是对这些的扩展 http://tag.wscso.com/2937.html
	 * @param text 要替换的源内容
	 * @param siteColumn 某个栏目的信息
	 * @param template TemplateCMS
	 * @return 如果result成功，则info为text替换完成的内容
	 */
	public static String siteColumnTag(String text, SiteColumn siteColumn, TemplateCMS template) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = null;
			try {
				m = c.getMethod("siteColumnTag",new Class[]{String.class,SiteColumn.class, TemplateCMS.class});  
			}catch (java.lang.NoSuchMethodException e) {
				if(Global.isJarRun) {
					ConsoleUtil.error(c.getSimpleName() + " 初始化异常，系统判断您当前是在开发环境中运行的，这是由于您使用了热部署而导致的，此异常忽略即可。正常线上部署，放到tomcat中运行时就不会再有热部署，不会再有这个问题");
				}
				e.printStackTrace();
				throw new NoSuchMethodException();
			}
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invokeReply, new Object[]{text, siteColumn, template});
			if(o != null){
				BaseVO vo = (BaseVO) o;
				if(vo.getResult() - BaseVO.FAILURE == 0) {
					ConsoleUtil.error("siteColumnTag 异常:"+vo.getInfo());
					return text;
				}else {
					return vo.getInfo();
				}
			}
		}
		return text;
	}
	
	public static String newsTag(String text,News news, SiteColumn siteColumn, NewsDataBean newsDataBean, TemplateCMS template) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = null;
			try {
				m = c.getMethod("newsTag",new Class[]{String.class,News.class, SiteColumn.class,NewsDataBean.class, TemplateCMS.class});  
			}catch (java.lang.NoSuchMethodException e) {
				if(Global.isJarRun) {
					ConsoleUtil.error(c.getSimpleName() + " 初始化异常，系统判断您当前是在开发环境中运行的，这是由于您使用了热部署而导致的，此异常忽略即可。正常线上部署，放到tomcat中运行时就不会再有热部署，不会再有这个问题");
				}
				e.printStackTrace();
				throw new NoSuchMethodException();
			}
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invokeReply, new Object[]{text, news, siteColumn,newsDataBean, template});
			if(o != null){
				BaseVO vo = (BaseVO) o;
				if(vo.getResult() - BaseVO.FAILURE == 0) {
					ConsoleUtil.error("newsTag 异常:"+vo.getInfo());
					return text;
				}else {
					return vo.getInfo();
				}
			}
		}
		return text;
	}
	
	/**
	 * 写出string文本文件之前，也就是生成html页面之前会先执行的。  
	 * <p>这个接口主要是给第三方系统使用网市场模板系统时，自定义一些模板标签所预留使用的</p>
	 * @param text 写出的文本文件的内容，文本。也就是写出html的内容
	 * @param path 要生成html文件的路径。 传入如 index.html 、sitemap.xml
	 * @return 不管插件是否执行成功，都会返回正常的html的内容。
	 */
	public static String publishHtmlFileBefore(String text, String path, TemplateCMS template) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = null;
			try {
				m = c.getMethod("publishHtmlFileBefore",new Class[]{String.class,String.class, TemplateCMS.class});  
			}catch (java.lang.NoSuchMethodException e) {
				if(Global.isJarRun) {
					ConsoleUtil.error(c.getSimpleName() + " 初始化异常，系统判断您当前是在开发环境中运行的，这是由于您使用了热部署而导致的，此异常忽略即可。正常线上部署，放到tomcat中运行时就不会再有热部署，不会再有这个问题");
				}
				e.printStackTrace();
				throw new NoSuchMethodException();
			}
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invokeReply, new Object[]{text, path, template});
			if(o != null){
				BaseVO vo = (BaseVO) o;
				if(vo.getResult() - BaseVO.FAILURE == 0) {
					ConsoleUtil.error("publishHtmlFileBefore 异常:"+vo.getInfo());
					return text;
				}else {
					return vo.getInfo();
				}
			}
		}
		return text;
	}
	
	
}
