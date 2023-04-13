package com.xnx3.wangmarket.admin.pluginManage.interfaces.manage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.xnx3.BaseVO;
import com.xnx3.ScanClassUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.vo.NewsVO;

/**
 * CMS网站管理后台首页的html源码处理
 * @author 管雷鸣
 *
 */
@Component(value="NewsEditPluginManage")
public class NewsEditPluginManage {
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
				classList = ScanClassUtil.searchByInterfaceName(allClassList, "com.xnx3.wangmarket.admin.pluginManage.interfaces.NewsEditInterface");
				for (int i = 0; i < classList.size(); i++) {
					ConsoleUtil.info("装载 NewsEditInterface 插件："+classList.get(i).getName());
				}
			}
		}).start();
	}
	
	/**
	 * 在内容管理-编辑内容时，右侧可以展开更多编辑项，这个就是在编辑项上进行的追加html的输入项
	 * 注意，追加的js是在html最末尾追加的
	 * @param news 当前编辑的文章，对应 news 数据表
	 * @param siteColumn 当前编辑文章所属的栏目
	 * @param site 当前编辑的文章所属的站点
	 * @return 要追加的html
	 */
	public static String newsEditAppendHtml(News news, SiteColumn siteColumn, Site site) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		/**** 针对html源代码处理的插件 ****/
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invoke = null;
			invoke = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例
			Method m = c.getMethod("newsEditAppendHtml",new Class[]{News.class, SiteColumn.class, Site.class});	//获取要调用的init方法
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invoke, new Object[]{news, siteColumn, site});
			if(o != null && !o.equals("null")){
				sb.append(o.toString());
			}
		}
		return sb.toString();
	}

	/**
	 * 拦截 News 进行预处理。这里是在保存入数据库之前拦截下来，进行处理，处理完后将其存入数据库
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，新增文章，点击保存时</li>
	 * 		<li>内容管理中，编辑文章，点击保存时</li>
	 * 	</ul>
	 * @param news 当前要处理的 {@link News}
	 * @return 已处理过的 news，会将其进行保存进数据库。如果返回 result 失败，则info返回失败原因，并且用户端会讲这个原因展示出保存失败的提醒。
	 */
	public static NewsVO newsEditSaveBefore(HttpServletRequest request, News news) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		NewsVO vo = new NewsVO();
		vo.setNews(news);
		
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invoke = null;
			invoke = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例
			Method m = c.getMethod("newsEditSaveBefore",new Class[]{HttpServletRequest.class, News.class});	//获取要调用的init方法
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			Object o = m.invoke(invoke, new Object[]{request ,vo.getNews()});
			if(o != null && !o.equals("null")){
				NewsVO pluginVO = (NewsVO) o;
				if(pluginVO.getResult() - BaseVO.FAILURE == 0) {
					return pluginVO;
				}else if(pluginVO.getNews() != null) {
					vo.setNews(pluginVO.getNews());
				}
			}
		}
		return vo;
	}
}
