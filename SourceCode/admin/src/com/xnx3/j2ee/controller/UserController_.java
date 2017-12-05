package com.xnx3.j2ee.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.UserService;

/**
 * 用户User的相关操作
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/user")
public class UserController_ extends BaseController {
	@Resource
	private UserService userService;

	/**
	 * 用户退出，页面跳转提示。
	 */
	@RequestMapping("logout")
	public String logout(Model model, HttpServletRequest request){
		ActionLogCache.insert(request, "注销登录");
		userService.logout();
		return success(model, "注销登录成功", "login.do");
	}
}
