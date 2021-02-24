package com.xnx3.wangmarket.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.j2ee.Func;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.SmsLog;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SmsService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.CaptchaUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.LoginVO;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;
import com.xnx3.wangmarket.agencyadmin.entity.AgencyData;
import com.xnx3.wangmarket.agencyadmin.util.SessionUtil;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteUser;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;
import com.xnx3.wangmarket.admin.util.TemplateAdminMenu.TemplateMenuEnum;

/**
 * 登录、注册
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class LoginController extends com.xnx3.wangmarket.admin.controller.BaseController {
	@Resource
	private UserService userService;
	@Resource
	private SmsService smsService;
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	@Resource
	private ApiService apiService;

	/**
	 * 注册页面 
	 * @deprecated v4.12起，废弃reg，使用 plugin/phoneCreateSite/reg.do?inviteid=123 自助开通网站
	 */
	@RequestMapping("/reg${url.suffix}")
	public String reg(HttpServletRequest request ,Model model){
		if(SystemUtil.getInt("ALLOW_USER_REG") == 0){
			return error(model, "系统已禁止用户自行注册");
		}
		//判断用户是否已注册，已注册的用户将出现提示，已登录，无需注册
		if(getUser() != null){
			return error(model, "您已登陆，无需注册");
		}
		
		userService.regInit(request);
		ActionLogUtil.insert(request, "进入注册页面reg.do，进行redirect至手机号开通网站插件的注册", "inviteid="+request.getParameter("inviteid"));
		return redirect("plugin/phoneCreateSite/reg.do?inviteid="+request.getParameter("inviteid"));
	}
	
	
	/**
	 * 通过手机验证注册开通网站
	 * @param inviteid 上级id，推荐者id，user.id
	 * @deprecated v4.12起，废弃此，使用 plugin/phoneCreateSite/reg.do?inviteid=123 自助开通网站
	 */
	@RequestMapping("regByPhone${url.suffix}")
	public String regByPhone(HttpServletRequest request, Model model){
		if(SystemUtil.getInt("ALLOW_USER_REG") == 0){
			return error(model, "系统已禁止用户自行注册");
		}
		
		if(getUser() != null){
			//已登陆
			return redirect(com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl());
		}
		userService.regInit(request);	//注册记录下线
		
		ActionLogUtil.insert(request, "进入注册页面reg.do，进行redirect至手机号开通网站插件的注册", "inviteid="+request.getParameter("inviteid"));
		return redirect("plugin/phoneCreateSite/reg.do?inviteid="+request.getParameter("inviteid"));
	}
	

	/**
	 * 通过api接口登陆网站 
	 * @param key 参考 http://api.wscso.com/2779.html
	 * @deprecated 请使用 /plugin/api/loginApi.do
	 */
	@RequestMapping("loginApi${url.suffix}")
	public String loginApi(HttpServletRequest request, Model model,
			@RequestParam(value = "key", required = false , defaultValue="") String key){
		if(getUser() != null){
			//已登陆
			return redirect(com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl());
		}
		
		UserVO vo = apiService.identityVerifyAndSession(key);
		if(vo.getResult() - UserVO.FAILURE == 0){
			return error(model, vo.getInfo());
		}
		
		//得到上级的代理信息
		Agency parentAgency = sqlService.findAloneBySqlQuery("SELECT * FROM agency WHERE userid = " + getUser().getReferrerid(), Agency.class);
		SessionUtil.setParentAgency(parentAgency);
		if(parentAgency != null){
			//得到上级代理的变长表信息
			AgencyData parentAgencyData = sqlService.findAloneBySqlQuery("SELECT * FROM agency_data WHERE id = " + parentAgency.getId(), AgencyData.class);
			SessionUtil.setParentAgencyData(parentAgencyData);
		}
		
		//当前时间
		int currentTime = DateUtil.timeForUnix10();	

		//得到当前用户站点的相关信息，加入userBean，以存入Session缓存起来
		Site site = sqlService.findAloneBySqlQuery("SELECT * FROM site WHERE userid = "+getUserId()+" ORDER BY id DESC", Site.class);
		if(site != null){
			SessionUtil.setSite(site);
		}
		
		//判断网站用户是否是已过期，使用期满，将无法使用
		if(site != null && site.getExpiretime() != null && site.getExpiretime() < currentTime){
			return error(model, "您的网站已到期。若要继续使用，请续费");
		}
		
		//计算其网站所使用的资源，比如OSS已占用了多少资源。下个版本修改
//		loginSuccessComputeUsedResource();
		
		ActionLogUtil.insert(request, "api模式登录成功","进入网站管理后台");
		
		return redirect(com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl());
	}
	
}
