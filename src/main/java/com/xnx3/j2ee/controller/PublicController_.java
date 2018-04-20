package com.xnx3.j2ee.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 通用的一些
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class PublicController_ extends BaseController {
	
	/**
	 * 403
	 */
	@RequestMapping("403${url.suffix}")
	public String error403(HttpServletRequest request){
		return "iw/403";
	}
	
	/**
	 * 404
	 */
	@RequestMapping("404${url.suffix}")
	public String error404(HttpServletRequest request){
		return "iw/404";
	}
	
	/**
	 * 500
	 */
	@RequestMapping("500${url.suffix}")
	public String error500(HttpServletRequest request){
		return "iw/500";
	}
	
}
