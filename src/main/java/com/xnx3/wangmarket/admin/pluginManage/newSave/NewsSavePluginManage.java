package com.xnx3.wangmarket.admin.pluginManage.newSave;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import com.xnx3.ScanClassUtil;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.weixin.bean.MessageReceive;

/**
 * 文章保存时，针对news、news_date 的预处理插件
 * @author 管雷鸣
 *
 */
@Component
public class NewsSavePluginManage {
	//自动回复的插件，这里开启项目时，便将有关此的插件加入此处
	public static List<Class<?>> classList;
	static{
		List<Class<?>> cl = ScanClassUtil.getClasses("com.xnx3.wangmarket");
		classList = ScanClassUtil.searchByInterfaceName(cl, "com.xnx3.wangmarket.admin.pluginManage.newSave.NewsSaveInterface");
	}
	
	/**
	 * 拦截 news 数据进行处理
	 * @param request
	 * @param response
	 * @param news
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static News interceptNews(HttpServletRequest request, HttpServletResponse response, News news) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("interceptNews",new Class[]{HttpServletRequest.class, HttpServletResponse.class, News.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			m.invoke(invokeReply, new Object[]{request, response, news});
		}
		return news;
	}
	
	public static NewsData interceptNewsData(HttpServletRequest request, HttpServletResponse response, NewsData newsData) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		for (int i = 0; i < classList.size(); i++) {
			Class<?> c = classList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("interceptNewsData",new Class[]{HttpServletRequest.class, HttpServletResponse.class,NewsData.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			m.invoke(invokeReply, new Object[]{request, response, newsData});
		}
		return newsData;
	}
}
