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
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.cache.GenerateHTML;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;

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
		ActionLogUtil.insert(request, "进入通用手机网站控制台首页");
		
		//获取网站后台管理系统有哪些功能插件，也一块列出来,以直接在网站后台中显示出来
		String pluginMenu = "";
		//v4.12更改，已废弃
//		if(PluginManage.wapSiteClassManage.size() > 0){
//			for (Map.Entry<String, SitePluginBean> entry : PluginManage.wapSiteClassManage.entrySet()) {
//				SitePluginBean bean = entry.getValue();
//				pluginMenu += "<dd><a id=\""+entry.getKey()+"\" class=\"subMenuItem\" href=\"javascript:loadIframeByUrl('"+bean.getMenuHref()+"'), notUseTopTools();\">"+bean.getMenuTitle()+"</a></dd>";
//			}
//		}
		model.addAttribute("pluginMenu", pluginMenu);
		
		UserBean userBean = getUserBean();
		User user = getUser();
		model.addAttribute("siteRemainHintJavaScript", siteService.getSiteRemainHintForJavaScript(userBean.getSite(), userBean.getParentAgency()));
		model.addAttribute("siteUrl", "http://"+Func.getDomain(getSite()));
		model.addAttribute("password", MD5Util.MD5(user.getPassword()));
		model.addAttribute("user", user);
		model.addAttribute("site", getSite());
		model.addAttribute("parentAgency", getParentAgency());	//上级代理
		//pc、wap 已废弃
//		model.addAttribute("im_kefu_websocketUrl", com.xnx3.wangmarket.im.Global.websocketUrl);
		model.addAttribute("autoAssignDomain", G.getFirstAutoAssignDomain());	//自动分配的域名，如 wang.market
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
		
		ActionLogUtil.insert(request, "打开通用手机网站，首页可视化编辑模式");
		
		return "siteWap/editIndex";
	}
	
}