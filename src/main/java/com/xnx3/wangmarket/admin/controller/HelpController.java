package com.xnx3.wangmarket.admin.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;

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
		ActionLogUtil.insert(request, "网站帮助说明-使用入门");
		return redirect(SystemUtil.get("SITEUSER_FIRST_USE_EXPLAIN_URL"));
	}
	
	/**
	 * 模版开发
	 */
	@RequestMapping("/mobankaifa${url.suffix}")
	public String mobankaifa(HttpServletRequest request ,Model model){
		ActionLogUtil.insert(request, "网站帮助说明-模版开发");
		return redirect(SystemUtil.get("SITE_TEMPLATE_DEVELOP_URL"));
	}
	
	
}
