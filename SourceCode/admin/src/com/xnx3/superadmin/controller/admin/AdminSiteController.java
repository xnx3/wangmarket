package com.xnx3.superadmin.controller.admin;

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
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.admin.G;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.service.SiteService;

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
	@RequestMapping("list")
	public String list(HttpServletRequest request, Model model){
		Sql sql = new Sql(request);
		sql.setSearchTable("site");
		sql.setSearchColumn(new String[]{"userid=","name","phone","client=","bind_domain","domain"});
		int count = sqlService.count("site", sql.getWhere());
		Page page = new Page(count, G.PAGE_WAP_NUM, request);
		sql.setSelectFromAndPage("SELECT * FROM site", page);
		sql.setOrderBy("site.id DESC");
		List<Site> list = sqlService.findBySql(sql, Site.class);
		
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "admin/site/list";
	}
	

	/**
	 * 网站详情
	 * @param id News.id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("adminSiteView")
	@RequestMapping("view")
	public String view(@RequestParam(value = "id", required = true , defaultValue="") int id, Model model){
		Site site = (Site) sqlService.findById(Site.class, id);
		
		model.addAttribute("site", site);
		return "admin/site/view";
	}
}
