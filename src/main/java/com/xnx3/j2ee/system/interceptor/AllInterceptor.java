package com.xnx3.j2ee.system.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xnx3.ConfigManagerUtil;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.func.Log;
import com.xnx3.j2ee.func.StaticResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器，对所有动作拦截
 * @author 管雷鸣
 */
public class AllInterceptor implements HandlerInterceptor {
	public static boolean useExecuteTime = false;	//是否使用Controller函数记录执行时间的功能
	public static long recordTime = 0;	//若执行时间超过多少毫秒，就在控制台打印出来，这里的单位是毫秒
	
	static{
		useExecuteTime = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("ExecuteTime.controller.used").equals("true");
		int recordTimeInt = Lang.stringToInt(ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("ExecuteTime.recordTime"), 0);
		recordTime = recordTimeInt;
	}
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
    	//第一次访问时，先判断system数据表中，ATTACHMENT_FILE_URL有没有设置，若没有设置，第一次访问的同时，会设置此参数。
    	if(Global.get("ATTACHMENT_FILE_URL") == null || Global.get("ATTACHMENT_FILE_URL").length() == 0){
    		String url="http://" + request.getServerName() //服务器地址    
        	        + ":"     
        	        + request.getServerPort()           //端口号    
        	        + "/";    
        	Log.info("project request url : " + url);
        	Global.system.put("ATTACHMENT_FILE_URL", url);
    	}
    	
    	if(useExecuteTime){
			long startTime = System.currentTimeMillis();  
	        request.setAttribute("startTime", startTime);  
		}
    	
        return true;
    }
 
    
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	if(useExecuteTime){
			long startTime = (Long)request.getAttribute("startTime");  
	        long endTime = System.currentTimeMillis();  
	        long executeTime = endTime - startTime;  
	        
	        if(executeTime > recordTime){
	        	System.out.println(DateUtil.currentDate("MM-dd HH:mm:ss")+" ControllerExecuteTime : "+executeTime+" ms , "+handler);
			}
		}
    	
    	//v4.10 增加，资源文件css、js的引用路径，是本地引用，还是cdn引用
    	String static_resource_path = StaticResource.getPath();
    	if(modelAndView != null){
    		modelAndView.addObject("STATIC_RESOURCE_PATH", static_resource_path);
    	}
    }
 

	@Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
 
    }
}