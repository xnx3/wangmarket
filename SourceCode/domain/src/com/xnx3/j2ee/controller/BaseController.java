package com.xnx3.j2ee.controller;
import org.springframework.ui.Model;
import com.xnx3.j2ee.Global;

/**
 * 所有Controller父类
 * @author 管雷鸣
 */
public class BaseController {
	
	/**
	 * 成功提示中转页面
	 * @param model
	 * @param info 提示信息
	 * @param redirectUrl 提示后要跳转至的页面，支持：
	 * 				<ul>
	 * 					<li>null	返回之前页面，js的返回上个页面
	 * 					<li>user/info.do	内网页面，前面无须/，默认自动补齐之前路径
	 * 					<li>http://www.xnx3.com	外网页面，写全即可
	 * 				</ul>
	 * @return View
	 */
	public String success(Model model , String info , String redirectUrl){
		return prompt(model, info,redirectUrl, Global.PROMPT_STATE_SUCCESS);
	}
	
	/**
	 * 成功提示中转页面
	 * @param model {@link Model}
	 * @param info 提示信息
	 * @return View
	 */
	public String success(Model model , String info){
		return prompt(model, info,null, Global.PROMPT_STATE_SUCCESS);
	}
	
	
	/**
	 * 错误提示中专页面
	 * @param model
	 * @param info 提示的信息
	 * @param redirectUrl 提示后要跳转至的页面，支持：
	 * 				<ul>
	 * 					<li>null	返回之前页面，js的返回上个页面
	 * 					<li>user/info.do	内网页面，前面无须/，默认自动补齐之前路径
	 * 					<li>http://www.xnx3.com	外网页面，写全即可
	 * 				</ul>
	 * @return View
	 */
	public String error(Model model , String info,String redirectUrl){
		return prompt(model, info,redirectUrl, Global.PROMPT_STATE_ERROR);
	}
	
	/**
	 * 错误提示中专页面
	 * @param model {@link Model}
	 * @param info 提示的信息
	 * @return View
	 */
	public String error(Model model,String info){
		return error(model, info, null);
	}
	
	/**
	 * 提示信息中专页面
	 * @param model org.springframework.ui.Model
	 * @param info 显示的提示信息
	 * @param redirectUrl 提示后要跳转至的页面，支持：
	 * 				<ul>
	 * 					<li>null	返回之前页面，js的返回上个页面
	 * 					<li>user/info.do	内网页面，前面无须/，默认自动补齐之前路径
	 * 					<li>http://www.xnx3.com	外网页面，写全即可
	 * 				</ul>
	 * @param state 状态，成功提示还是错误提示
	 * 				<ul>
	 * 					<li> {@link #PROMPT_STATE_SUCCESS }
	 * 					<li> {@link #PROMPT_STATE_ERROR }
	 * 				<ul>
	 * @return View
	 */
	public String prompt(Model model , String info,String redirectUrl, int state){
		model.addAttribute("info", info);
		model.addAttribute("state", state);
		
		if(redirectUrl==null||redirectUrl.length()==0){
			redirectUrl = "javascript:history.go(-1);";
		}

		model.addAttribute("redirectUrl", redirectUrl);
		
		return "/publicPage/prompt";
	}
	
	/**
	 * 重定向跳转页面
	 * @param redirectUtl 重定向到的url地址
	 * 				<ul>
	 * 					<li>站内跳转，如：user/info.do	内网页面，前面无须/，默认自动补齐之前路径。此便是跳转到当前项目根目录下/user/info.do页面，
	 * 					<li>站外跳转，如：http://www.xnx3.com	外网页面，写全即可
	 * 				</ul>
	 * @return 组合后的字符串，如：redirect:/admin/user/list.do
	 */
	public String redirect(String redirectUtl) {
		if(redirectUtl != null && redirectUtl.indexOf("http")>-1){
			return "redirect:"+redirectUtl;
		}else{
			return "redirect:/"+redirectUtl;
		}
	}

}
