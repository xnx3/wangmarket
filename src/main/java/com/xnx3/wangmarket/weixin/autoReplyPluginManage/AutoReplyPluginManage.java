package com.xnx3.wangmarket.weixin.autoReplyPluginManage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.xnx3.ScanClassUtil;
import com.xnx3.wangmarket.weixin.autoReplyPluginManage.Reply;
import com.xnx3.weixin.bean.MessageReceive;

/**
 * 插件管理
 * @author 管雷鸣
 *
 */
@Component
public class AutoReplyPluginManage {
	//自动回复的插件，这里开启项目时，便将有关此的插件加入此处
	public static List<Class<?>> autoReplyClassList;
	static{
		List<Class<?>> classList = ScanClassUtil.getClasses("com.xnx3.wangmarket");
		autoReplyClassList = ScanClassUtil.searchByInterfaceName(classList, "com.xnx3.wangmarket.weixin.interfaces.AutoReply");
	}
	
	public static void autoReply(HttpServletRequest request, HttpServletResponse response, MessageReceive messageReceive) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
		/**** 针对微信自动回复插件 ****/
		Reply reply = new Reply(response, messageReceive.getFromUserName(), messageReceive.getToUserName());
		for (int i = 0; i < AutoReplyPluginManage.autoReplyClassList.size(); i++) {
			Class<?> c = AutoReplyPluginManage.autoReplyClassList.get(i);
			Object invokeReply = null;
			invokeReply = c.newInstance();
			//运用newInstance()来生成这个新获取方法的实例  
			Method m = c.getMethod("reply",new Class[]{HttpServletRequest.class, HttpServletResponse.class, MessageReceive.class, Reply.class});	//获取要调用的init方法  
			//动态构造的Method对象invoke委托动态构造的InvokeTest对象，执行对应形参的add方法
			m.invoke(invokeReply, new Object[]{request, response, messageReceive, reply});
		}
	}
	
}
