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
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;

/**
 * 文章保存时，针对news、news_date 的预处理插件
 * @author 管雷鸣
 *
 */
@Component(value="PluginManageForNews")
public class NewsPluginManage {
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
				classList = ScanClassUtil.searchByInterfaceName(cl, "com.xnx3.wangmarket.admin.pluginManage.interfaces.NewsInterface");
				
				for (int i = 0; i < classList.size(); i++) {
					ConsoleUtil.info("装载 news 插件："+classList.get(i).getName());
				}
			}
		}).start();
		
	}
	
	/**
	 * 拦截 News 进行预处理。这里是在保存入数据库之前拦截下来，进行处理，处理完后将其存入数据库
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，新增文章，点击保存时</li>
	 * 		<li>内容管理中，编辑文章，点击保存时</li>
	 * 		<li>内容管理中，修改文章的发布时间</li>
	 * 	</ul>
	 * @param news 要处理的 {@link News}
	 * @return 已处理过的 news，会将其进行保存进数据库
	 */
	public static News newsSaveBefore(HttpServletRequest request, News news) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("newsSaveBefore",new Class[]{HttpServletRequest.class, News.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			m.invoke(invokeReply, new Object[]{request, news});
		}
		return news;
	}
	

	/**
	 * 这里是已经保存入数据库之后，进行处理
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，新增文章，保存成功后</li>
	 * 		<li>内容管理中，编辑文章，保存成功后</li>
	 * 		<li>内容管理中，修改文章的发布时间，保存成功后</li>
	 * 	</ul>
	 * @param news 当前操作的{@link News}
	 */
	public static void newsSaveFinish(HttpServletRequest request, News news) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("newsSaveFinish",new Class[]{HttpServletRequest.class, News.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			m.invoke(invokeReply, new Object[]{request, news});
		}
	}
	
	/**
	 * 拦截 NewsData 进行预处理。这里是在保存入数据库之前拦截下来，进行处理，处理完后将其存入数据库
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，新增文章，点击保存时</li>
	 * 		<li>内容管理中，编辑文章，点击保存时</li>
	 * 	</ul>
	 * @param newsData 要处理的 {@link NewsData}
	 * @return 已处理过的 newsData，会将其进行保存进数据库
	 */
	public static NewsData newsDataSaveBefore(HttpServletRequest request, NewsData newsData) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("newsDataSaveBefore",new Class[]{HttpServletRequest.class, NewsData.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			m.invoke(invokeReply, new Object[]{request, newsData});
		}
		return newsData;
	}
	
	/**
	 * 拦截 NewsData 进行预处理。这里是在保存入数据库之前拦截下来，进行处理，处理完后将其存入数据库
	 * 在以下情况，触发此方法：
	 * 	<ul>
	 * 		<li>内容管理中，新增文章，保存成功后</li>
	 * 		<li>内容管理中，编辑文章，保存成功后</li>
	 * 	</ul>
	 * @param newsData 当前保存的 {@link NewsData}
	 */
	public static void newsDataSaveFinish(HttpServletRequest request, NewsData newsData) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("newsDataSaveFinish",new Class[]{HttpServletRequest.class, NewsData.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			m.invoke(invokeReply, new Object[]{request, newsData});
		}
	}
	
	
	/**
	 * 删除文章
	 * @param news {@link News} 对象,已删除的文章信息
	 */
	public static void newsDeleteFinish(HttpServletRequest request, News news) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("newsDeleteFinish",new Class[]{HttpServletRequest.class,News.class});  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			m.invoke(invokeReply, new Object[]{request, news});
		}
	}
}
