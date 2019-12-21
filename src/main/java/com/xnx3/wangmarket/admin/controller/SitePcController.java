package com.xnx3.wangmarket.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.DateUtil;
import com.xnx3.MD5Util;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.cache.GenerateHTML;
import com.xnx3.wangmarket.admin.cache.Template;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;

/**
 * 通用网站－PC端电脑网站，site.client 为pc网站的
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/sitePc/")
public class SitePcController extends BaseController {

	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	
	/**
	 * 电脑网站控制台首页
	 */
	@RequestMapping("index${url.suffix}")
	public String index(HttpServletRequest request, Model model){
		ActionLogUtil.insert(request, "进入通用电脑网站控制台首页");
		
		//获取网站后台管理系统有哪些功能插件，也一块列出来,以直接在网站后台中显示出来
		String pluginMenu = "";
		//v4.12更改，已废弃
//		if(PluginManage.pcSiteClassManage.size() > 0){
//			for (Map.Entry<String, SitePluginBean> entry : PluginManage.pcSiteClassManage.entrySet()) {
//				SitePluginBean bean = entry.getValue();
//				pluginMenu += "<dd><a id=\""+entry.getKey()+"\" class=\"subMenuItem\" href=\"javascript:loadIframeByUrl('"+bean.getMenuHref()+"'), notUseTopTools();\">"+bean.getMenuTitle()+"</a></dd>";
//			}
//		}
		model.addAttribute("pluginMenu", pluginMenu);
		
		UserBean userBean = getUserBean();
		User user = getUser();
		model.addAttribute("siteRemainHintJavaScript", siteService.getSiteRemainHintForJavaScript(userBean.getSite(), userBean.getParentAgency()));
		model.addAttribute("siteUrl", "http://"+Func.getDomain(getSite()));
		model.addAttribute("user", user);
		model.addAttribute("password", MD5Util.MD5(user.getPassword()));		//跟im应用交互验证
		model.addAttribute("site", getSite());
		model.addAttribute("parentAgency", getParentAgency());	//上级代理
		//pc、wap 已废弃
//		model.addAttribute("im_kefu_websocketUrl", com.xnx3.wangmarket.im.Global.websocketUrl);
		model.addAttribute("autoAssignDomain", G.getFirstAutoAssignDomain());	//自动分配的域名，如 wang.market
		return "sitePc/index";
	}
	

	/**
	 * 预览PC站点首页，并进行修改操作
	 * 若GET传入参数 reloadCleanCache=1 ，则强制刷新，相当于CTRL+F5，清除缓存。不过此依赖于indexedit.js，纯粹是js控制 。
	 */
	@RequestMapping("editIndex${url.suffix}")
	public String editIndex(HttpServletRequest request,Model model){
		Site site = getSite();
		
		ActionLogUtil.insert(request, "进入通用电脑网站预览PC站点首页，并进行修改操作");
		
		GenerateHTML gh = new GenerateHTML(site);
		gh.setEditMode(true);
		String templateHtml = gh.pcIndex();
		
		//预览，生成好的URL
		String previewHtml = gh.getGeneratePcIndexHtml();
		if(previewHtml == null || previewHtml.equals("")){
			previewHtml = templateHtml;
		}else{
			/*将一些相对路径的变为绝对路径*/
			//JS缓存数据路径转换
			Template temp = new Template(site);
			previewHtml = temp.replaceForEditModeTag(previewHtml);
		}
		
		//检查是否存再indexedit.js，如果用户自定义首页将其删除了，再将其加入
		if(previewHtml.indexOf("indexedit.js") == -1){
			previewHtml = previewHtml + "<script src=\""+SystemUtil.get("ATTACHMENT_FILE_URL")+"js/admin/indexedit.js\"></script>";
		}
		
		//v3.0调试使用
		
		//电脑模式网站跟手机模式网站不同的是，首页会有产品展示内容、新闻内容。为了不每次加载首页都去数据库取，那么这里采用静态页面修改对比的方式进行。所以，要将site.js等缓存的数据文件进行替换，将其加入版本后，以免缓存。当然， 后续考虑会使用cms的缓存模式
		long time = DateUtil.timeForUnix13();
		previewHtml = previewHtml.replace("site.js\">", "site.js?v="+time+"\">");
		previewHtml = previewHtml.replace("siteColumn.js\">", "siteColumn.js?v="+time+"\">");
		previewHtml = previewHtml.replace("siteColumn.js\">", "siteColumn.js?v="+time+"\">");
		previewHtml = previewHtml.replace("siteColumnRank.js\">", "siteColumnRank.js?v="+time+"\">");
		previewHtml = previewHtml.replace("carouselList.js\">", "carouselList.js?v="+time+"\">");
		//切换模版后版面混乱的问题
		previewHtml = previewHtml.replace("styleedit.css\"", "styleedit.css?v="+time+"\"");
		
		
		model.addAttribute("html", previewHtml);
		return "sitePc/editIndex";
	}
	


	/**
	 * 首页，点击左侧菜单的高级设置生效
	 */
	@RequestMapping("popupGaoJiSet${url.suffix}")
	public String popupGaoJiSet(HttpServletRequest request,Model model){
		ActionLogUtil.insert(request, "通用电脑网站模式下，点击左侧菜单打开高级模式页面");
		return "sitePc/popup_gaoJiSet";
	}
	

	/**
	 * 首页，点击左侧菜单的SEO优化调出
	 */
	@RequestMapping("seo${url.suffix}")
	public String seo(HttpServletRequest request,Model model){
		ActionLogUtil.insert(request, "通用电脑网站模式下，点击左侧菜单打开SEO优化页面");
		return "sitePc/popup_seo";
	}
	
	/**
	 * v3.11 增加，适应v3.10及以前版本
	 */
	@RequestMapping("*.html")
	public String html(Model model, HttpServletRequest request){
		String htmlFiles = request.getServletPath();
		String[] hfs = htmlFiles.split("/");
		String fileName = hfs[hfs.length-1];	//得到文件名，如 1234.html
		
		String[] fns = fileName.split("\\.");
		String htmlFile = fns[0];
		
		ActionLogUtil.insert(request, "*.html", "htmlFile="+htmlFile);
		return redirect("sites/html.do?htmlFile="+htmlFile);
	}
	

}