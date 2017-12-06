package com.xnx3.j2ee.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.DateUtil;
import com.xnx3.Lang;

/**
 * 每次请求页面时，都会先通过这个
 * @author 管雷鸣
 *
 */
public class SystemInterceptor extends HandlerInterceptorAdapter {
	public static boolean useExecuteTime = false;	//是否使用Controller函数记录执行时间的功能
	public static long recordTime = 0;	//若执行时间超过多少毫秒，就在控制台打印出来，这里的单位是毫秒
	
	static{
		useExecuteTime = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("ExecuteTime.controller.used").equals("true");
		int recordTimeInt = Lang.stringToInt(ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("ExecuteTime.recordTime"), 0);
		recordTime = recordTimeInt;
	}
	
	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(useExecuteTime){
			long startTime = (Long)request.getAttribute("startTime");  
	        long endTime = System.currentTimeMillis();  
	        long executeTime = endTime - startTime;  
	        
	        if(executeTime > recordTime){
	        	System.out.println(DateUtil.currentDate("MM-dd HH:mm:ss")+" ControllerExecuteTime : "+executeTime+" ms , "+handler);
			}
		}
        
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(useExecuteTime){
			long startTime = System.currentTimeMillis();  
	        request.setAttribute("startTime", startTime);  
		}
		return true;
	}
}
