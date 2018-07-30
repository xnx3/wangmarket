package com.xnx3.wangmarket.api.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.api.service.KeyManageService;
import com.xnx3.wangmarket.api.vo.UserBeanVO;
import com.xnx3.wangmarket.superadmin.entity.Agency;

/**
 * Api接口相关
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/api")
public class ApiController extends com.xnx3.wangmarket.admin.controller.BaseController {
	@Resource
	private ApiService apiService;
	@Resource
	private SqlService sqlService;
	@Resource
	private KeyManageService keyManageService;


	/**
	 * Api首页，通过后台所看到的
	 */
	@RequestMapping("/index${url.suffix}")
	public String index(HttpServletRequest request ,Model model){
		ActionLogCache.insert(request, "进入我的api首页");
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
		UserBean userBean = new UserBean();
		
		//得到上级的代理信息
		Agency parentAgency = sqlService.findAloneBySqlQuery("SELECT * FROM agency WHERE userid = " + vo.getUser().getReferrerid(), Agency.class);
		userBean.setParentAgency(parentAgency);
		//当前时间
		int currentTime = DateUtil.timeForUnix10();	

		//得到当前用户站点的相关信息，加入userBean，以存入Session缓存起来
		Site site = sqlService.findAloneBySqlQuery("SELECT * FROM site WHERE userid = "+getUserId()+" ORDER BY id DESC", Site.class);
		if(site != null){
			userBean.setSite(site);
		}
		
		//判断网站用户是否是已过期，使用期满，将无法使用
		if(site != null && site.getExpiretime() != null && site.getExpiretime() < currentTime){
			return error(model, "您的网站已到期。若要继续使用，请续费");
		}
		
		//计算其网站所使用的资源，比如OSS已占用了多少资源。下个版本修改
//		loginSuccessComputeUsedResource();
		
		System.out.println(userBean.toString());
		ActionLogCache.insert(request, "api模式登录成功","进入网站管理后台");
		//将用户相关信息加入Shiro缓存
		ShiroFunc.getCurrentActiveUser().setObj(userBean);
		
		String redirect = com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl();
		System.out.println(redirect);
		return redirect(redirect);
	}
	

}
