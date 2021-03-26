package com.xnx3.j2ee.controller;

import java.awt.Font;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.agencyadmin.util.SessionUtil;

/**
 * 登录、注册
 * @author 管雷鸣
 */
@Controller(value="WMLoginController")
@RequestMapping("/")
public class LoginController extends BaseController {
	@Resource
	private UserService userService;
	@Resource
	private SqlService sqlService;
	

	/**
	 * 验证码图片显示，直接访问此地址可查看图片
	 */
	@RequestMapping("/captcha${url.suffix}")
	public void captcha(HttpServletRequest request,HttpServletResponse response) throws IOException{
		ActionLogUtil.insert(request, "获取验证码显示");

		com.xnx3.media.CaptchaUtil captchaUtil = new com.xnx3.media.CaptchaUtil();
	    captchaUtil.setCodeCount(5);                   //验证码的数量，若不增加图宽度的话，只能是1～5个之间
	    captchaUtil.setFont(new Font("Fixedsys", Font.BOLD, 21));    //验证码字符串的字体
	    captchaUtil.setHeight(18);  //验证码图片的高度
	    captchaUtil.setWidth(110);      //验证码图片的宽度
//	    captchaUtil.setCode(new String[]{"我","是","验","证","码"});   //如果对于数字＋英文不满意，可以自定义验证码的文字！
	    com.xnx3.j2ee.util.CaptchaUtil.showImage(captchaUtil, request, response);
	}
	

	/**
	 * 登录
	 */
	@RequestMapping("login${url.suffix}")
	public String login(HttpServletRequest request){
		if(getUser() != null){
			ActionLogUtil.insert(request, "进入登录页面", "已经登录成功，无需再登录，进行跳转");
			return redirect(com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl());
		}
		if(SystemUtil.get("IW_AUTO_INSTALL_USE").equals("true")){
			//尚未安装，进入安装界面
			return redirect("install/index.do");
		}
		ActionLogUtil.insert(request, "进入登录页面");
		//siteLogin
		return "/wm/login/siteLogin";
	}
	

	
	/**
	 * 退出登录状态
	 */
	@RequestMapping(value="logout${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO logout(HttpServletRequest request,Model model){
		SessionUtil.logout();
		return success();
	}
}
