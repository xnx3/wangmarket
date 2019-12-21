package com.xnx3.j2ee.controller.admin;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.util.SystemUtil;

/**
 * 在线支付日志管理
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/payLog")
public class PayLogAdminController_ extends BaseController{
	@Resource
	private SqlService sqlService;
	
	/**
	 * 日志列表
	 */
	@RequiresPermissions("adminPayLogList")
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchTable("pay_log");
		sql.setSearchColumn(new String[]{"userid=","orderno","channel"});
		int count = sqlService.count("pay_log", sql.getWhere());
		Page page = new Page(count, SystemUtil.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT pay_log.*,(SELECT user.nickname FROM user WHERE user.id=pay_log.userid) AS nickname FROM pay_log ", page);
		sql.setDefaultOrderBy("pay_log.id DESC");
		List<Map<String, Object>> list = sqlService.findMapBySql(sql);
		
		ActionLogUtil.insert(request, "总管理后台-在线支付日志管理列表","第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("page", page);
		model.addAttribute("list", list);
		return "/iw/admin/payLog/list";
	}
	
}
