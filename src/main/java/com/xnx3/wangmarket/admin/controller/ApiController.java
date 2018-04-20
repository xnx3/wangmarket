package com.xnx3.wangmarket.admin.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.ApiService;

/**
 * Api接口相关
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/api")
public class ApiController extends com.xnx3.wangmarket.admin.controller.BaseController {
	@Resource
	private ApiService apiService;

	/**
	 * Api首页，通过后台所看到的
	 */
	@RequestMapping("/index${url.suffix}")
	public String index(HttpServletRequest request ,Model model){
		ActionLogCache.insert(request, "进入我的api首页");
		model.addAttribute("key", apiService.getKey());
		return "api/index";
	}
	
	
}
