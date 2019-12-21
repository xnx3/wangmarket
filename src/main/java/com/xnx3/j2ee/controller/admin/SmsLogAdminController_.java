package com.xnx3.j2ee.controller.admin;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.entity.SmsLog;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.util.SystemUtil;

/**
 * 验证码管理，手机验证码相关，比如手机登陆时的验证码
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/smslog")
public class SmsLogAdminController_ extends BaseController {
	
	@Resource
	private SqlService sqlService;
	
	/**
	 * 验证码发送的列表
	 */
	@RequiresPermissions("adminSmsLogList")
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"phone","used=","userid="});
		int count = sqlService.count("sms_log", sql.getWhere());
		Page page = new Page(count, SystemUtil.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT * FROM sms_log", page);
		sql.setDefaultOrderBy("sms_log.id DESC");
		List<SmsLog> list = sqlService.findBySql(sql, SmsLog.class);
		
		ActionLogUtil.insert(request, "管理后台-验证码发送的列表","第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("page", page);
		model.addAttribute("list", list);
		return "/iw/admin/smslog/list";
	}
	
}
