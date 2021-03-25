package com.xnx3.wangmarket.superadmin.controller.admin;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.SiteService;

/**
 * 网站管理
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/site")
public class AdminSiteController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private UserService userService;
	@Resource
	private SiteService siteService;
	
	
	/**
	 * 网站列表
	 */
	@RequiresPermissions("adminSiteList")
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request, Model model){
		Sql sql = new Sql(request);
		sql.setSearchTable("site");
		sql.setSearchColumn(new String[]{"userid=","name","phone","client=","bind_domain","domain"});
		int count = sqlService.count("site", sql.getWhere());
		Page page = new Page(count, G.PAGE_WAP_NUM, request);
		sql.setSelectFromAndPage("SELECT * FROM site", page);
		sql.setOrderBy("site.id DESC");
		List<Site> list = sqlService.findBySql(sql, Site.class);
		
		ActionLogUtil.insert(request, "总管理后台，网站管理,网站列表");
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "/superadmin/site/list";
	}
	

	/**
	 * 网站详情
	 * @param id News.id
	 */
	@RequiresPermissions("adminSiteView")
	@RequestMapping("view${url.suffix}")
	public String view(HttpServletRequest request, Model model,
			@RequestParam(value = "id", required = true , defaultValue="") int id){
		Site site = sqlService.findById(Site.class, id);
		ActionLogUtil.insert(request, site.getId(), "总管理后台，网站管理,网站详情", site.getName());
		model.addAttribute("site", site);
		return "/superadmin/site/view";
	}
}
