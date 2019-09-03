package com.xnx3.wangmarket.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.wangmarket.admin.service.SiteService;

/**
 * 模版标签，编辑模版页面时，顶部的标签使用提示
 */
@Controller
@RequestMapping("/templateTag")
public class TemplateTagController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	
	
	/**
	 * 通用标签
	 */
	@RequestMapping(value="common${url.suffix}")
	public String common(HttpServletRequest request, Model model){
		
		return "templateTag/common";
	}
	
	/**
	 * 动态栏目调用
	 */
	@RequestMapping(value="dynamic${url.suffix}")
	public String dynamic(HttpServletRequest request, Model model){
		
		return "templateTag/dynamic";
	}
	
	/**
	 * 栏目标签
	 */
	@RequestMapping(value="column${url.suffix}")
	public String column(HttpServletRequest request, Model model){
		
		return "templateTag/column";
	}
	
}