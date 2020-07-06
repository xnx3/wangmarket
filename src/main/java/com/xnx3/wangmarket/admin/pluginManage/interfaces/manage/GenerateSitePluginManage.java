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
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 文章保存时，针对news、news_date 的预处理插件
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForGenerateSite")
public class GenerateSitePluginManage {
	//自动回复的插件，这里开启项目时，便将有关此的插件加入此处
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
				classList = ScanClassUtil.searchByInterfaceName(cl, "com.xnx3.wangmarket.admin.pluginManage.interfaces.GenerateSiteInterface");
				for (int i = 0; i < classList.size(); i++) {
					ConsoleUtil.info("装载 GenerateSite 插件："+classList.get(i).getName());
				}
			}
		}).start();
	}
	
	/**
	 * 点击生成整站按钮后，并且尚未生成整站之前，触发此方法
	 * @param site 生成整站的网站 {@link Site}
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static BaseVO generateSiteBefore(HttpServletRequest request, Site site) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("generateSiteBefore",new Class[]{HttpServletRequest.class,Site.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invokeReply, new Object[]{request, site});
			if(o != null){
				BaseVO vo = (BaseVO) o;
				//如果某一个插件返回了失败，那就以失败来算
				if(vo.getResult() - BaseVO.FAILURE == 0){
					return vo;
				}
			}
		}
		return new BaseVO();
	}
	
	/**
	 * 点击生成整站按钮后，已经生成完成整站，触发的接口
	 * @param site 生成整站的网站 {@link Site}
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void generateSiteFinish(HttpServletRequest request, Site site, Map<String, SiteColumn> siteColumnMap, Map<String, List<News>> newsMap, Map<Integer, NewsDataBean> newsDataMap) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("generateSiteFinish",new Class[]{HttpServletRequest.class, Site.class, Map.class, Map.class, Map.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			m.invoke(invokeReply, new Object[]{request, site, siteColumnMap, newsMap, newsDataMap});
		}
	}
	
}
