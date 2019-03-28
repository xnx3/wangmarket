package com.xnx3.wangmarket.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.SmsLog;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SmsLogService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.wangmarket.superadmin.entity.Agency;
import com.xnx3.wangmarket.superadmin.entity.AgencyData;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.admin.vo.SiteVO;

/**
 * 帮助说明
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/help/")
public class HelpController extends com.xnx3.wangmarket.admin.controller.BaseController {

	/**
	 * 使用入门
	 */
	@RequestMapping("/shiyongrumen${url.suffix}")
	public String shiyongrumen(HttpServletRequest request ,Model model){
		return redirect(Global.get("SITEUSER_FIRST_USE_EXPLAIN_URL"));
	}
	
	/**
	 * 模版开发
	 */
	@RequestMapping("/mobankaifa${url.suffix}")
	public String mobankaifa(HttpServletRequest request ,Model model){
		return redirect(Global.get("SITE_TEMPLATE_DEVELOP_URL"));
	}
	
	
}
