package com.xnx3.wangmarket.admin.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.cache.generateSite.GenerateHtmlInterface;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 文章保存时，针对news、news_date 的预处理插件
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForGenerateHtmlStorateInterfaceManage")
public class GenerateHtmlStorateInterfaceManage {
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
				classList = ScanClassUtil.searchByInterfaceName(cl, "com.xnx3.wangmarket.admin.pluginManage.interfaces.GenerateHtmlStorateInterface");
				for (int i = 0; i < classList.size(); i++) {
					ConsoleUtil.info("装载 GenerateHtmlStorateInterface 插件："+classList.get(i).getName());
				}
			}
		}).start();
	}
	
	/**
	 * 生成整站，获取的 {@link GenerateHtmlInterface} 的实现
	 * 如果没有插件实现，那么此接口会返回null
	 */
	public static GenerateHtmlInterface getGenerateHtmlInterfaceImpl(HttpServletRequest request, Site site) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = null;
			try {
				m = c.getMethod("getGenerateHtmlInterfaceImpl",new Class[]{HttpServletRequest.class,Site.class});	//获取要调用的init方法  
			}catch (java.lang.NoSuchMethodException e) {
				if(Global.isJarRun) {
					ConsoleUtil.error(c.getSimpleName() + " 初始化异常，系统判断您当前是在开发环境中运行的，这是由于您使用了热部署而导致的，此异常忽略即可。正常线上部署，放到tomcat中运行时就不会再有热部署，不会再有这个问题");
				}
				e.printStackTrace();
				throw new NoSuchMethodException();
			}
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invokeReply, new Object[]{request, site});
			if(o != null){
				GenerateHtmlInterface impl = (GenerateHtmlInterface) o;
				return impl;
			}
		}
		return null;
	}
	
	
}
