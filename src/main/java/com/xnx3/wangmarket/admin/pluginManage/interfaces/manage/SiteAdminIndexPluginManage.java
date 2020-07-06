package com.xnx3.wangmarket.admin.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;

/**
 * CMS网站管理后台首页的html源码处理
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForSiteAdminIndex")
public class SiteAdminIndexPluginManage {
	//处理html源代码的插件，这里开启项目时，便将有关此的插件加入此处
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
				List<Class<?>> allClassList = ScanClassUtil.getClasses("com.xnx3.wangmarket");
				classList = ScanClassUtil.searchByInterfaceName(allClassList, "com.xnx3.wangmarket.admin.pluginManage.interfaces.SiteAdminIndexInterface");
				for (int i = 0; i < classList.size(); i++) {
					ConsoleUtil.info("装载 SiteAdminIndex 插件："+classList.get(i).getName());
				}
			}
		}).start();
		
	}
	
	/**
	 * 网站管理后台追加的html
	 * @return 要追加到html最后面的 html代码
	 */
	public static String manage() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
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

}
