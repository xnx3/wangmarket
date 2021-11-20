package com.xnx3.wangmarket.superadmin.controller.admin;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.domain.bean.MQBean;
import com.xnx3.wangmarket.domain.bean.SimpleSite;

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
		sql.setSearchColumn(new String[]{"userid=","state=","name","phone","client=","bind_domain","domain"});
		int count = sqlService.count("site", sql.getWhere());
		Page page = new Page(count, G.PAGE_WAP_NUM, request);
		sql.setSelectFromAndPage("SELECT * FROM site", page);
		sql.setOrderByField(new String[]{"id","expiretime","addtime"});
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
	

	/**
	 * 暂停网站，冻结网站。冻结后，site、user数据表都会记录
	 * 暂停后，网站依旧正常计费！
	 * @param siteid 要暂停的网站的site.id
	 */
	@RequiresPermissions("adminSiteList")
	@RequestMapping(value="siteFreeze.json", method= {RequestMethod.POST})
	@ResponseBody
	public BaseVO sitePause(HttpServletRequest request,
			@RequestParam(value = "siteid", required = true) int siteid){
		Site site = sqlService.findById(Site.class, siteid);
		User user = sqlService.findById(User.class, site.getUserid());
		if(user == null){
			return error("用户不存在！");
		}
		
		//判断网站状态是否符合，只有当网站状态为正常时，才可以对网站进行暂停冻结操作
		if(site.getState() - Site.STATE_NORMAL != 0){
			return error("当前网站的状态不符，暂停失败");
		}
		
		site.setState(Site.STATE_FREEZE);
		sqlService.save(site);
		userService.freezeUser(site.getUserid());
		
		//记录操作日志
		ActionLogUtil.insertUpdateDatabase(request, site.getId(), "userid:"+getUser().getId()+",将网站"+site.getName()+"暂停");
		
		//更新域名服务器
		MQBean mqBean = new MQBean();
		mqBean.setType(MQBean.TYPE_STATE);
		mqBean.setSimpleSite(new SimpleSite(site));
		siteService.updateDomainServers(mqBean);
		
		return success();
	}
	

	/**
	 * 解除暂停网站，将暂停的网站恢复正常
	 * @param siteid 要暂停的网站的site.id
	 */
	@RequiresPermissions("adminSiteList")
	@RequestMapping(value="siteUnFreeze.json", method= {RequestMethod.POST})
	@ResponseBody
	public BaseVO siteRemovePause(HttpServletRequest request,
			@RequestParam(value = "siteid", required = true) int siteid){
		Site site = sqlService.findById(Site.class, siteid);
		User user = sqlService.findById(User.class, site.getUserid());
		if(user == null){
			return error("用户不存在！");
		}
		
		//判断网站状态是否符合，只有当网站状态为正常时，才可以对网站进行暂停冻结操作
		if(site.getState() - Site.STATE_FREEZE != 0){
			return error("当前网站的状态不符，暂停失败");
		}
		
		site.setState(Site.STATE_NORMAL);
		sqlService.save(site);
		userService.unfreezeUser(site.getUserid());
		
		//记录操作日志
		ActionLogUtil.insertUpdateDatabase(request, site.getId(), "userid:"+getUser().getId()+",将暂停的网站"+site.getName()+"恢复正常");
		
		//更新域名服务器
		MQBean mqBean = new MQBean();
		mqBean.setType(MQBean.TYPE_STATE);
		mqBean.setSimpleSite(new SimpleSite(site));
		siteService.updateDomainServers(mqBean);
		
		return success();
	}
	
}
