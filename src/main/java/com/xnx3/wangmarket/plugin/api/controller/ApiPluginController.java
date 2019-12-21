package com.xnx3.wangmarket.plugin.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.wangmarket.plugin.api.service.KeyManageService;
import com.xnx3.wangmarket.plugin.api.vo.UserBeanVO;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;
import com.xnx3.wangmarket.agencyadmin.entity.AgencyData;
import com.xnx3.wangmarket.agencyadmin.util.SessionUtil;

/**
 * Api接口相关
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/api/")
public class ApiPluginController extends com.xnx3.wangmarket.admin.controller.BaseController {
	@Resource
	private ApiService apiService;
	@Resource
	private SqlService sqlService;
	@Resource
	private KeyManageService keyManageService;
	@Resource
	private UserService userService;


	/**
	 * Api首页，通过后台所看到的
	 */
	@RequestMapping("/index${url.suffix}")
	public String index(HttpServletRequest request ,Model model){
		User user = getUser();
		if(user == null){
			return error(model, "请先登录", "login.do");
		}
		
		ActionLogUtil.insert(request, "进入我的api首页");
		model.addAttribute("key", apiService.getKey());
		return "plugin/api/index";
	}
	

	/**
	 * 通过key，进行登陆
	 * @param inviteid 上级id，推荐者id，user.id
	 */
	@RequestMapping("loginApi${url.suffix}")
	public String loginApi(HttpServletRequest request, Model model,
			@RequestParam(value = "key", required = false , defaultValue="") String key){
		if(getUser() != null){
			//已登陆
			return redirect(com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl());
		}
		
		UserBeanVO vo = keyManageService.verify(key);
//		UserVO vo = apiService.identityVerifyAndSession(key);
		if(vo.getResult() - UserVO.FAILURE == 0){
			return error(model, vo.getInfo());
		}
		
		
		//用于缓存入Session，用户的一些基本信息，比如用户的站点信息、用户的上级代理信息、如果当前用户是代理，还包含当前用户的代理信息等
//		UserBean userBean = new UserBean();
		
		//得到上级的代理信息
		Agency parentAgency = sqlService.findAloneBySqlQuery("SELECT * FROM agency WHERE userid = " + vo.getUser().getReferrerid(), Agency.class);
		SessionUtil.setParentAgency(parentAgency);
		if(parentAgency != null){
			//得到上级代理的变长表信息
			AgencyData parentAgencyData = sqlService.findAloneBySqlQuery("SELECT * FROM agency_data WHERE id = " + parentAgency.getId(), AgencyData.class);
			SessionUtil.setParentAgencyData(parentAgencyData);
		}
		//当前时间
		int currentTime = DateUtil.timeForUnix10();	

		//得到当前用户站点的相关信息，加入userBean，以存入Session缓存起来
		SessionUtil.setSite(vo.getSite());
		SessionUtil.setAgency(vo.getAgency());
		
		//判断网站用户是否是已过期，使用期满，将无法使用
		if(vo.getSite() != null && vo.getSite().getExpiretime() != null && vo.getSite().getExpiretime() < currentTime){
			return error(model, "您的网站已到期。若要继续使用，请续费");
		}
		
		ActionLogUtil.insert(request, vo.getUser().getId(), "api模式登录成功");
		
		//设置当前用户状态为登陆状态
		BaseVO lvo = userService.loginForUserId(request, vo.getUser().getId());
		if(lvo.getResult() - BaseVO.FAILURE == 0){
			return error(model, lvo.getInfo());
		}
		
		String redirect = com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl();
		System.out.println("redirect:"+redirect);
		return redirect(redirect);
	}

}
