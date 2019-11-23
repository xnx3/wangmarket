package com.xnx3.wangmarket.admin.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.j2ee.Global;

/**
 * 帮助说明
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/help/")
public class HelpController extends com.xnx3.wangmarket.admin.controller.BaseController {

	/**
	 * 使用入门
	 */
	@RequestMapping("/shiyongrumen${url.suffix}")
	public String shiyongrumen(HttpServletRequest request ,Model model){
		return redirect(Global.get("SITEUSER_FIRST_USE_EXPLAIN_URL"));
	}
	
	/**
	 * 模版开发
	 */
	@RequestMapping("/mobankaifa${url.suffix}")
	public String mobankaifa(HttpServletRequest request ,Model model){
		return redirect(Global.get("SITE_TEMPLATE_DEVELOP_URL"));
	}
	
	
}
