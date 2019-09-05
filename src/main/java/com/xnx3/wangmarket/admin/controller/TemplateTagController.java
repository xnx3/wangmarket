package com.xnx3.wangmarket.admin.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xnx3.DateUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
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
	 * 跳转通用标签说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "common${url.suffix}")
	public String common(Model model){
		//当前使用的站点
		Site site = getSite();
		//调用主站的域名
		String masterSiteUrl = Global.get("MASTER_SITE_URL");
		//静态资源的域名
		String templatePath = Global.get("ATTACHMENT_FILE_URL");
		//当前时间的时间戳
		Integer linuxTime = DateUtil.dateToInt10(new Date());
		
		model.addAttribute("site", site);
		model.addAttribute("masterSiteUrl", masterSiteUrl);
		model.addAttribute("templatePath", templatePath);
		model.addAttribute("linuxTime", linuxTime);
		return "templateTag/common";
	}
	
	/**
	 * 跳转动态栏目调用标签说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "dynamic${url.suffix}")
	public String dynamic(Model model){
		
		return "templateTag/dynamic";
	}
	
	/**
	 * 跳转栏目标签说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "column${url.suffix}")
	public String column(Model model){
		//当前站点
		Site site = getSite();
		//站点下的栏目
		SiteColumn siteColumn = sqlService.findAloneByProperty(SiteColumn.class, "siteid", site.getId());
		
		model.addAttribute("siteColumn", siteColumn);
		return "templateTag/column";
	}
	
	/**
	 * 跳转文章信息标签说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "news${url.suffix}")
	public String news(Model model){
		//当前站点
		Site site = getSite();
		//站点下的栏目
		SiteColumn siteColumn = sqlService.findAloneByProperty(SiteColumn.class, "siteid", site.getId());
		//通过站点ID跟栏目ID找出对应的文章内容
		List<News> newsList = sqlService.findBySqlQuery("SELECT * FROM news WHERE siteid = " + site.getId() + " AND cid = " + siteColumn.getId(), News.class);
		News news = newsList.get(0);
		
		model.addAttribute("news", news);
		return "templateTag/news";
	}
	
	/**
	 * 跳转详情页独有标签说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "details${url.suffix}")
	public String details(){
		
		return "templateTag/details";
	}
	
	/**
	 * 跳转列表页独有标签说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "list${url.suffix}")
	public String list(){
		
		return "templateTag/list";
	}
	
	/**
	 * 跳转分页标签说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "page${url.suffix}")
	public String page(){
		
		return "templateTag/page";
	}
	
	/**
	 * 跳转首页模板说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "home${url.suffix}")
	public String home(){
		
		return "templateTag/home";
	}
	
	/**
	 * 跳转静态资源引用方式说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "resource${url.suffix}")
	public String resource(){
		
		return "templateTag/resource";
	}
	
	/**
	 * 跳转扩展字段说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "extend${url.suffix}")
	public String extend(){
		
		return "templateTag/extend";
	}
	
	/**
	 * 跳转模板变量说明页面
	 * @author 张洪岩
	 */
	@RequestMapping(value = "var${url.suffix}")
	public String var(){
		
		return "templateTag/varForUsed";
	}
	
}