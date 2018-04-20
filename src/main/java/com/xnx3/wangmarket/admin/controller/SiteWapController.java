package com.xnx3.wangmarket.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.MD5Util;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.cache.GenerateHTML;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.AliyunLog;

/**
 * 通用网站－手机端电脑网站，site.client 为wap网站的
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/siteWap/")
public class SiteWapController extends BaseController {

	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	
	/**
	 * 手机网站控制台首页
	 */
	@RequestMapping("index${url.suffix}")
	public String index(HttpServletRequest request, Model model){
		AliyunLog.addActionLog(getSiteId(), "进入通用手机网站控制台首页");
		
		UserBean userBean = getUserBean();
		User user = getUser();
		model.addAttribute("siteRemainHintJavaScript", siteService.getSiteRemainHintForJavaScript(userBean.getSite(), userBean.getParentAgency()));
		model.addAttribute("siteUrl", "http://"+Func.getDomain(getSite()));
		model.addAttribute("password", MD5Util.MD5(user.getPassword()));
		model.addAttribute("user", user);
		model.addAttribute("site", getSite());
		model.addAttribute("parentAgency", getParentAgency());	//上级代理
		return "siteWap/index";
	}
	

	/**
	 * 预览站点首页，并进行修改操作。可视化修改首页
	 */
	@RequestMapping("editIndex${url.suffix}")
	public String editIndex(HttpServletRequest request,Model model){
		Site site = getSite();
		
		GenerateHTML gh = new GenerateHTML(site);
		gh.setEditMode(true);
		String html = gh.wapIndex();
		model.addAttribute("html", html);
		
		AliyunLog.addActionLog(getSiteId(), "打开通用手机网站，首页可视化编辑模式");
		
		return "siteWap/editIndex";
	}
	
}