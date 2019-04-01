package com.xnx3.wangmarket.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.SmsLog;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SmsLogService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.wangmarket.superadmin.entity.Agency;
import com.xnx3.wangmarket.superadmin.entity.AgencyData;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.admin.vo.SiteVO;

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
	private SmsLogService smsLogService;
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	@Resource
	private ApiService apiService;

	/**
	 * 注册页面 
	 */
	@RequestMapping("/reg${url.suffix}")
	public String reg(HttpServletRequest request ,Model model){
		if(Global.getInt("ALLOW_USER_REG") == 0){
			return error(model, "系统已禁止用户自行注册");
		}
		//判断用户是否已注册，已注册的用户将出现提示，已登录，无需注册
		if(getUser() != null){
			return error(model, "您已登陆，无需注册");
		}
		
		
		userService.regInit(request);
		ActionLogCache.insert(request, "进入注册页面reg.do，进行redirect至regByPhone.do");
		return redirect("regByPhone.do");
	}
	
	
	/**
	 * 通过手机验证注册开通网站
	 * @param inviteid 上级id，推荐者id，user.id
	 */
	@RequestMapping("regByPhone${url.suffix}")
	public String regByPhone(HttpServletRequest request, Model model){
		if(Global.getInt("ALLOW_USER_REG") == 0){
			return error(model, "系统已禁止用户自行注册");
		}
		
		if(getUser() != null){
			//已登陆
			return redirect(com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl());
		}
		userService.regInit(request);	//注册记录下线
		
		AliyunLog.addActionLog(getSiteId(), "打开根据手机号注册页面");
		return "regByPhone";
	}
	

	/**
	 * 利用阿里云短信通道发送手机号注册的验证码。在用户自行注册开通网站时使用
	 * @param request {@link HttpServletRequest}
	 * 			<br/>form表单需提交参数：phone(发送到的手机号)
	 */
	@RequestMapping(value="sendPhoneRegCodeByAliyun${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO sendPhoneRegCodeByAliyun(HttpServletRequest request){
		if(Global.getInt("ALLOW_USER_REG") == 0){
			return error("系统已禁止用户自行注册");
		}
		if(G.aliyunSMSUtil == null){
			return error("系统未开起短信发送服务");
		}
		
		BaseVO vo = new BaseVO();
		
		//验证图片验证码是否正确
//		BaseVO vo = Captcha.compare(request.getParameter("code"), request);
//		if(vo.getResult() == BaseVO.SUCCESS){
			//判断手机号是否已被注册使用了
			String phone = filter(request.getParameter("phone"));
			if(phone == null || phone.length() < 3){
				return error("请输入正确的手机号");
			}
			if(userService.findByPhone(phone) != null){
				return error("此手机号已注册过了！请更换一个手机号吧");
			}
			
			vo = smsLogService.sendByAliyunSMS(request, G.aliyunSMSUtil, G.AliyunSMS_SignName, G.AliyunSMS_Login_TemplateCode,  filter(request.getParameter("phone")), SmsLog.TYPE_REG);
			AliyunLog.addActionLog(getSiteId(), "获取手机号验证码"+(vo.getResult() - BaseVO.SUCCESS == 0 ? "成功":"失败")+"，用户获取验证码的手机号："+request.getParameter("phone"));
			if(vo.getResult() - BaseVO.SUCCESS == 0){
				//如果成功，将info的验证码去掉
				vo.setInfo("获取成功！");
			}
//		}else{
//			AliyunLog.addActionLog(getSiteId(), "图片验证码错误！用户想要获取验证码的手机号："+request.getParameter("phone"));
//		}
		
		return vo;
	}


	/**
	 * 用户开通账户并创建网站，进行提交保存
	 * @param username 用户名
	 * @param email 邮箱，可为空
	 * @param password 密码
	 * @param phone 手机号
	 * @param code 手机验证码
	 * @param clilent 网站类型
	 */
	@RequestMapping(value="userCreateSite${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO userCreateSite(HttpServletRequest request,
			@RequestParam(value = "username", required = false , defaultValue="") String username,
			@RequestParam(value = "email", required = false , defaultValue="") String email,
			@RequestParam(value = "password", required = false , defaultValue="") String password,
			@RequestParam(value = "phone", required = false , defaultValue="") String phone,
			@RequestParam(value = "code", required = false , defaultValue="") String code,
			@RequestParam(value = "clilent", required = false , defaultValue="3") Short client
			){
		if(Global.getInt("ALLOW_USER_REG") == 0){
			return error("抱歉，当前禁止用户自行注册开通网站！");
		}
		username = StringUtil.filterXss(username);
		email = filter(email);
		phone = filter(phone);
		code = filter(code);
		
		//判断用户的短信验证码
		BaseVO verifyVO = smsLogService.verifyPhoneAndCode(phone, code, SmsLog.TYPE_REG, 300);
		if(verifyVO.getResult() - BaseVO.FAILURE == 0){
			return verifyVO;
		}
		
		//注册用户
		User user = new User();
		user.setUsername(username);
		user.setPhone(phone);
		user.setEmail(email);
		user.setPassword(password);
		user.setOssSizeHave(G.REG_GENERAL_OSS_HAVE);
		BaseVO userVO = userService.reg(user, request);
		if(userVO.getResult() - BaseVO.FAILURE == 0){
			return userVO;
		}
		
		//为此用户设置其自动登录成功
		int userid = Lang.stringToInt(userVO.getInfo(), 0);
		if(userid == 0){
			ActionLogCache.insert(request, "warn", "自助开通网站，自动创建账号出现问题。info:"+userVO.getInfo());
			return error("自动创建账号出现问题");
		}
		BaseVO loginVO = userService.loginByUserid(request,userid);
		if(loginVO.getResult() - BaseVO.FAILURE == 0){
			return loginVO;
		}
		ShiroFunc.getCurrentActiveUser().setObj(new UserBean());
		
		//开通网站
		Site site = new Site();
		site.setExpiretime(DateUtil.timeForUnix10() + 31622400);	//到期，一年后，366天后
		site.setClient(client);
		site.setPhone(phone);
		site.setName("网站名字");
		SiteVO siteVO = siteService.saveSite(site, userid, request);
		AliyunLog.addActionLog(userid, "自助创建网站提交保存",(siteVO.getResult() - SiteVO.SUCCESS == 0 ? "成功":"失败")+",username:"+user.getUsername());
		if(siteVO.getResult() - SiteVO.SUCCESS == 0){
			/**
			 * 免费通道
			 */
			
			return success();
		}else{
			return error(siteVO.getInfo());
		}
	}
	

	/**
	 * 通过手机验证注册开通网站
	 * @param inviteid 上级id，推荐者id，user.id
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
		
		//用于缓存入Session，用户的一些基本信息，比如用户的站点信息、用户的上级代理信息、如果当前用户是代理，还包含当前用户的代理信息等
		UserBean userBean = new UserBean();
		
		//得到上级的代理信息
		Agency parentAgency = sqlService.findAloneBySqlQuery("SELECT * FROM agency WHERE userid = " + getUser().getReferrerid(), Agency.class);
		userBean.setParentAgency(parentAgency);
		if(parentAgency != null){
			//得到上级代理的变长表信息
			AgencyData parentAgencyData = sqlService.findAloneBySqlQuery("SELECT * FROM agency_data WHERE id = " + parentAgency.getId(), AgencyData.class);
			userBean.setParentAgencyData(parentAgencyData);
		}
		
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
		
		ActionLogCache.insert(request, "api模式登录成功","进入网站管理后台");
		//将用户相关信息加入Shiro缓存
		ShiroFunc.getCurrentActiveUser().setObj(userBean);
		
		return redirect(com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl());
	}
	
	
}
