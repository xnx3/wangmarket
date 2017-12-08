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
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;

/**
 * 手机验证码相关，比如手机登陆时的验证码
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/smslog")
public class SmsLogAdminController_ extends BaseController {
	
	@Resource
	private SqlService sqlService;
	
	@RequiresPermissions("adminSmsLogList")
	@RequestMapping("list")
	public String list(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"phone","used=","userid="});
		int count = sqlService.count("sms_log", sql.getWhere());
		Page page = new Page(count, Global.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT * FROM sms_log", page);
		sql.setDefaultOrderBy("sms_log.id DESC");
		List<SmsLog> list = sqlService.findBySql(sql, SmsLog.class);
		
		model.addAttribute("page", page);
		model.addAttribute("list", list);
		return "/iw/admin/smslog/list";
	}
	
}
