package com.xnx3.admin.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.SmsLog;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.func.Captcha;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SmsLogService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.superadmin.entity.Agency;
import com.xnx3.admin.Func;
import com.xnx3.admin.G;
import com.xnx3.admin.bean.UserBean;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.service.SiteService;
import com.xnx3.admin.util.AliyunLog;
import com.xnx3.admin.vo.SiteVO;

/**
 * Api接口相关
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/api")
public class ApiController extends com.xnx3.admin.controller.BaseController {
	@Resource
	private ApiService apiService;

	/**
	 * Api首页，通过后台所看到的
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request ,Model model){
		ActionLogCache.insert(request, "进入我的api首页");
		model.addAttribute("key", apiService.getKey());
		return "api/index";
	}
	
	
}
