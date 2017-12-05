package com.xnx3.admin.controller.client;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.admin.controller.BaseController;

/**
 * 登陆相关
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class ClientLoginController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private UserService userService;
	
	/**
	 * Client登陆接口
	 */
	@RequestMapping("userLoginForClient")
	@ResponseBody
	public BaseVO userLoginForClient(HttpServletRequest request,Model model){
		return userService.loginByUsernameAndPassword(request);
	}
}
