package com.xnx3.wangmarket.admin.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.pluginManage.bean.HtmlVisualBean;

/**
 * html 可视化编辑的插件，主要在网站管理后台-模版管理-模版页面 的编辑时使用。
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForHtmlVisual")
public class HtmlVisualPluginManage {
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
				classList = ScanClassUtil.searchByInterfaceName(cl, "com.xnx3.wangmarket.admin.pluginManage.interfaces.HtmlVisualInterface");
				
				for (int i = 0; i < classList.size(); i++) {
					ConsoleUtil.info("装载 HtmlVisual 插件："+classList.get(i).getName());
				}
			}
		}).start();
		
	}
	
	/**
	 * @param site 当前操作的网站
	 * @param basevo.info 返回js插件的url。这里可以时绝对路径，也可以是相对路径，如 /plugin/HtmlVisualEditor/plugin.js
	 * 		如果vo返回null，则是没有使用插件
	 */
	public static List<HtmlVisualBean> htmlVisualEditBefore(HttpServletRequest request, Site site) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		List<HtmlVisualBean> list = new ArrayList<HtmlVisualBean>();
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例
			Method m = c.getMethod("htmlVisualEditBefore",new Class[]{HttpServletRequest.class, Site.class});	//获取要调用的init方法
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object obj = m.invoke(invokeReply, new Object[]{request, site});
			if(obj != null){
				HtmlVisualBean bean = (HtmlVisualBean) obj;
				if(bean != null && bean.getUrl() != null) {
					list.add(bean);
				}
				
			}
		}
		
		return list;
	}
	

}
