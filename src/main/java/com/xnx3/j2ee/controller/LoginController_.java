package com.xnx3.j2ee.controller;

import java.awt.Font;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.LoginVO;
import com.xnx3.media.CaptchaUtil;

/**
 * 登录、注册
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class LoginController_ extends BaseController {
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
		
		CaptchaUtil captchaUtil = new CaptchaUtil();
	    captchaUtil.setCodeCount(5);                   //验证码的数量，若不增加图宽度的话，只能是1～5个之间
	    captchaUtil.setFont(new Font("Fixedsys", Font.BOLD, 21));    //验证码字符串的字体
	    captchaUtil.setHeight(18);  //验证码图片的高度
	    captchaUtil.setWidth(110);      //验证码图片的宽度
//	    captchaUtil.setCode(new String[]{"我","是","验","证","码"});   //如果对于数字＋英文不满意，可以自定义验证码的文字！
	    com.xnx3.j2ee.util.CaptchaUtil.showImage(captchaUtil, request, response);
	}
	
	/**
	 * 登陆页面
	 */
//	@RequestMapping("login${url.suffix}")
	public String login(HttpServletRequest request,Model model){
		if(getUser() != null){
			ActionLogUtil.insert(request, "进入登录页面", "已经登录成功，无需再登录，进行跳转");
			return redirect("");
		}
		
		ActionLogUtil.insert(request, "进入登录页面");
		return "iw_update/login/login";
	}

	/**
	 * 登陆请求验证
	 * @param request {@link HttpServletRequest} 
	 * 		<br/>登陆时form表单需提交三个参数：username(用户名/邮箱)、password(密码)、code（图片验证码的字符）
	 * @return vo.result:
	 * 			<ul>
	 * 				<li>0:失败</li>
	 * 				<li>1:成功</li>
	 * 			</ul>
	 */
//	@RequestMapping(value="loginSubmit${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public LoginVO loginSubmit(HttpServletRequest request,Model model){
		LoginVO vo = new LoginVO();
		
		//验证码校验
		BaseVO capVO = com.xnx3.j2ee.util.CaptchaUtil.compare(request.getParameter("code"), request);
		if(capVO.getResult() == BaseVO.FAILURE){
			ActionLogUtil.insert(request, "用户名密码模式登录失败", "验证码出错，提交的验证码："+StringUtil.filterXss(request.getParameter("code")));
			vo.setBaseVO(capVO);
			return vo;
		}else{
			//验证码校验通过
			
			BaseVO baseVO =  userService.loginByUsernameAndPassword(request);
			vo.setBaseVO(baseVO);
			if(baseVO.getResult() == BaseVO.SUCCESS){
				ActionLogUtil.insert(request, "用户名密码模式登录成功");
				
				//登录成功,BaseVO.info字段将赋予成功后跳转的地址，所以这里要再进行判断
				vo.setInfo("admin/index/index.do");
				
				//将sessionid加入vo返回
				HttpSession session = request.getSession();
				vo.setToken(session.getId());
				
				//加入user信息
				vo.setUser(getUser());
			}else{
				ActionLogUtil.insert(request, "用户名密码模式登录失败",baseVO.getInfo());
			}
			
			return vo;
		}
	}
	
}
