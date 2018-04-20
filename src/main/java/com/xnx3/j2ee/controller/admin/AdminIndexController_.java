package com.xnx3.j2ee.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xnx3.MD5Util;
import com.xnx3.j2ee.Func;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.entity.User;

/**
 * 管理后台首页
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/index")
public class AdminIndexController_ extends BaseController{
	
	/**
	 * 管理后台首页
	 */
	@RequiresPermissions("adminIndexIndex")
	@RequestMapping("index${url.suffix}")
	public String index(HttpServletRequest request, Model model){
		//登录成功后，管理后台的主题页面，默认首页的url
		String url = "admin/user/list.do"; 
		
		//这里可以根据不同的管理级别，来指定显示默认是什么页面
		if(Func.isAuthorityBySpecific(getUser().getAuthority(), Global.get("ROLE_SUPERADMIN_ID"))){
			//有超级管理员权限
			url = "admin/user/list.do";
		}else{
			//代理
			url = "agency/index.do";
		}
		
		User user = getUser();
		model.addAttribute("password", MD5Util.MD5(user.getPassword()));
		model.addAttribute("user", user);
		model.addAttribute("indexUrl", url);	//首页(欢迎页)url
		return "/iw_update/admin/index/index";
	}
	
}
