package com.xnx3.wangmarket.system;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import com.xnx3.j2ee.pluginManage.interfaces.SpringMVCInterceptorInterface;

/**
 * 响应头的设置相关，比如设置 X-Frame-Options
 * @author 管雷鸣
 */
public class ResponseHeaderInterceptor implements SpringMVCInterceptorInterface{

	@Override
	public List<String> pathPatterns() {
		List<String> list = new ArrayList<String>();
 		list.add("/sites/**");
 		list.add("/news/**");
 		list.add("/column/**");
 		list.add("/help/**");
 		list.add("/inputModel/**");
 		list.add("/install/**");
 		list.add("/template/**");
 		list.add("/templateTag/**");
 		list.add("/agency/**");
 		list.add("/superadmin/**");
 		return list;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return SpringMVCInterceptorInterface.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		SpringMVCInterceptorInterface.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		SpringMVCInterceptorInterface.super.afterCompletion(request, response, handler, ex);
	}
	
}
