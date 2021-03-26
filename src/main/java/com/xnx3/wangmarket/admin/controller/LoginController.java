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
import com.xnx3.Lang;
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
	
	

	/**
	 * 登陆请求验证
	 * @param request {@link HttpServletRequest} 
	 * 		<br/>登陆时form表单需提交三个参数：username(用户名/邮箱)、password(密码)、code（图片验证码的字符）
	 * @return vo.result:
	 * 			<ul>
	 * 				<li>0:失败</li>
	 * 				<li>1:成功</li>
	 * 				<li>11:网站到期/代理到期</li>	
	 * 			</ul>
	 */
	@RequestMapping(value="wangmarketLoginSubmit${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public LoginVO wangmarketLoginSubmit(HttpServletRequest request,Model model){
		LoginVO vo = new LoginVO();
		//验证码校验
		BaseVO capVO = CaptchaUtil.compare(request.getParameter("code"), request);
		if(capVO.getResult() == BaseVO.FAILURE){
			ActionLogUtil.insert(request, "用户名密码模式登录失败", "验证码出错，提交的验证码："+StringUtil.filterXss(request.getParameter("code")));
			vo.setBaseVO(capVO);
			return vo;
		}else{
			//验证码校验通过
			BaseVO baseVO = userService.loginByUsernameAndPassword(request);
			vo.setBaseVO(baseVO);
			if(baseVO.getResult() - BaseVO.SUCCESS == 0){
				//登录成功,BaseVO.info字段将赋予成功后跳转的地址，所以这里要再进行判断
				
				//用于缓存入Session，用户的一些基本信息，比如用户的站点信息、用户的上级代理信息、如果当前用户是代理，还包含当前用户的代理信息等
//				UserBean userBean = new UserBean();
				
				//得到当前登录的用户的信息
				User user = getUser();
				if(user == null) {
					//从数据库取
					user = sqlService.findById(User.class, Lang.stringToInt(baseVO.getInfo(), 0));
				}
				//得到当前用户，在网市场中，user的扩展表 site_user 的信息
				SiteUser siteUser = sqlService.findById(SiteUser.class, user.getId());
				if(siteUser == null){
					siteUser = new SiteUser();
				}
				//缓存进session
				com.xnx3.wangmarket.admin.util.SessionUtil.setSiteUser(siteUser);
				
				//可以根据用户的不同权限，来判断用户登录成功后要跳转到哪个页面
				if(Func.isAuthorityBySpecific(user.getAuthority(), SystemUtil.get("ROLE_SUPERADMIN_ID"))){
					//如果是超级管理员，那么跳转到管理后台
					vo.setInfo("admin/index/index.do");
					ActionLogUtil.insertUpdateDatabase(request, "用户名密码模式登录成功","进入管理后台admin/index/");
				}else{
					/* 自己扩展的 */
					
					/**** 代理相关判断，将其推荐人(上级代理)加入Shiro存储起来 ***/
					if(user.getReferrerid() == null || user.getReferrerid() == 0){
						//网站用户没有发现上级代理，理论上这是不成立的，网站必须是有代理平台开通，这里暂时先忽略
					}else{
						//得到上级的代理信息
						/*
						 * 如果当前用户是代理，那这里是他上级代理的user.id
						 * 如果当前用户是网站管理元，那这里是给他开通网站的上级代理user.id
						 * 如果当前用户只是网站管理员开通的一个子用户，管理网站某个固定功能的，那这里还是这个网站的上级代理user.id
						 */
						int parentAgencyUserid = 0;
						
						//判断一下这个用户是否有user.siteid ，如果有，那肯定就是网站管理员开通的子用户了
						if(siteUser.getSiteid() != null && siteUser.getSiteid() > 0){
							//是网站管理员开通的子用户，那么需要查询 一下网站管理员的用户信息
							User siteAdminUser = sqlService.findById(User.class, user.getReferrerid());
							if(siteAdminUser == null){
								vo.setBaseVO(BaseVO.FAILURE, "登陆失败，未发现您所属网站的管理者");
								return vo;
							}
							parentAgencyUserid = siteAdminUser.getReferrerid();
						}else{
							//如果不是子账户，那直接获取user.referrerid 即可
							parentAgencyUserid = user.getReferrerid();
						}
						Agency parentAgency = sqlService.findAloneBySqlQuery("SELECT * FROM agency WHERE userid = " + parentAgencyUserid, Agency.class);
						SessionUtil.setParentAgency(parentAgency);
						
						if(parentAgency != null){
							//得到上级代理的变长表信息
							AgencyData parentAgencyData = sqlService.findAloneBySqlQuery("SELECT * FROM agency_data WHERE id = " + parentAgency.getId(), AgencyData.class);
							SessionUtil.setParentAgencyData(parentAgencyData);
						}
					}
					
					//当前时间
					int currentTime = DateUtil.timeForUnix10();	
					
					//判断当前用户的权限，是代理还是网站使用者
					if(Func.isAuthorityBySpecific(user.getAuthority(), SystemUtil.get("ROLE_USER_ID"))){
						//普通用户，建站用户，网站使用者
						Site site = null;
						
						//判断是否哟siteid，也就是是否是网站管理的子用户。因为子用户是有user.siteid的
						if(siteUser.getSiteid() != null && siteUser.getSiteid() > 0){
							//是网站管理子用户.既然是有子账户了，那肯定子账户插件是使用了，也就可以进行一下操作了
							site = sqlService.findById(Site.class, siteUser.getSiteid());
							
							//网站子用户，需要读取他拥有哪些权限，也缓存起来
							List<Map<String, Object>> menuList = sqlService.findMapBySqlQuery("SELECT menu FROM plugin_sitesubaccount_user_role WHERE userid = "+user.getId());
							Map<String, String> menuMap = new HashMap<String, String>();
							for (int i = 0; i < menuList.size(); i++) {
								Map<String, Object> menu = menuList.get(i);
								if(menu.get("menu") != null){
									String m = (String) menu.get("menu");
									menuMap.put(m, "1");
								}
							}
							SessionUtil.setSiteMenuRole(menuMap);
							
						}else{
							//是网站管理者，拥有所有权限的
							//得到当前用户站点的相关信息，加入userBean，以存入Session缓存起来
							site = sqlService.findAloneBySqlQuery("SELECT * FROM site WHERE userid = "+getUserId()+" ORDER BY id DESC", Site.class);
							ConsoleUtil.info("网站管理者，拥有网站所有权限:"+user.getUsername());
							
							//将拥有所有功能的管理权限，将功能菜单全部遍历出来，赋予这个用户
							Map<String, String> menuMap = new HashMap<String, String>();
							for (TemplateMenuEnum e : TemplateMenuEnum.values()) {
								menuMap.put(e.id, "1");
							}
							SessionUtil.setSiteMenuRole(menuMap);
						}
						if(site == null){
							vo.setResult(BaseVO.FAILURE);
							vo.setInfo("出错！所管理的网站不存在！");
							return vo;
						}
						//将所管理的网站加入session缓存
						SessionUtil.setSite(site);
						
						//判断网站用户是否是已过期，使用期满，将无法使用
						if(site != null && site.getExpiretime() != null && site.getExpiretime() < currentTime){
							//您的网站已到期。若要继续使用，请续费
							String info = "";
							if(SessionUtil.getParentAgency() != null){
								try {
									info = "您的网站已于 "+DateUtil.dateFormat(site.getExpiretime(), "yyyy-MM-dd")+" 到期！";
								} catch (NotReturnValueException e) {
									e.printStackTrace();
								}
								info = info + ""
										+ "<br/>若要继续使用，请联系："
										+ "<br/>姓名："+SessionUtil.getParentAgency().getName()
										+ "<br/>QQ："+SessionUtil.getParentAgency().getQq()
										+ "<br/>电话："+SessionUtil.getParentAgency().getPhone();
							}
							vo.setBaseVO(11, info);
							SessionUtil.logout();	//退出登录，销毁session
							return vo;
						}
						
						//计算其网站所使用的资源，比如OSS已占用了多少资源
						loginSuccessComputeUsedResource();
						
						ActionLogUtil.insertUpdateDatabase(request, "用户名密码模式登录成功","进入网站管理后台");
					}else{
						//代理
						
						//得到当前用户代理的相关信息，加入userBean，以存入Session缓存起来
						Agency myAgency = sqlService.findAloneBySqlQuery("SELECT * FROM agency WHERE userid = " + getUserId(), Agency.class);
						SessionUtil.setAgency(myAgency);
						if(myAgency != null){
							//得到当前代理用户的变长表信息
							AgencyData myAgencyData = sqlService.findAloneBySqlQuery("SELECT * FROM agency_data WHERE id = " + myAgency.getId(), AgencyData.class);
							SessionUtil.setAgencyData(myAgencyData);
						}
						
						//判断当前代理是否是已过期，使用期满，将无法登录
						if (myAgency != null && myAgency.getExpiretime() != null && myAgency.getExpiretime() < currentTime){
							//您的代理资格已到期。若要继续使用，请联系您的上级
							//BaseVO vo = new BaseVO();
							String info = "";
							try {
								info = "您的代理资格已于 "+DateUtil.dateFormat(myAgency.getExpiretime(), "yyyy-MM-dd")+" 到期！"
										+ "<br/>若要继续使用，请联系："
										+ "<br/>姓名："+SessionUtil.getParentAgency().getName()
										+ "<br/>QQ："+SessionUtil.getParentAgency().getQq()
										+ "<br/>电话："+SessionUtil.getParentAgency().getPhone();
							} catch (NotReturnValueException e) {
								e.printStackTrace();
							}
							vo.setBaseVO(11, info);
							SessionUtil.logout();
							return vo;
						}
						
						ActionLogUtil.insertUpdateDatabase(request, "用户名密码模式登录成功","进入代理后台");
					}
					
					//设置登录成功后，是跳转到总管理后台、代理后台，还是跳转到wap、pc、cms
					vo.setInfo(com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl());	
				}
				
				//将iwSID加入vo返回
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


	/**
	 * 用户登陆成功后，计算其所使用的资源，如OSS占用
	 * <br/>1.计算用户空间大小
	 * <br/>2.设定用户是否可进行上传附件、图片
	 */
	public void loginSuccessComputeUsedResource(){
		//如果这个用户是单纯的网站用户，并且今天并没有过空间计算，那么就要计算其所有的空间了
		final String currentDate = DateUtil.currentDate("yyyyMMdd");
		
		SiteUser siteUser = com.xnx3.wangmarket.admin.util.SessionUtil.getSiteUser();
		Site site = com.xnx3.wangmarket.admin.util.SessionUtil.getSite();
		if((site.getAttachmentUpdateDate() == null) || (getUser().getAuthority().equals(SystemUtil.get("USER_REG_ROLE")) && !site.getAttachmentUpdateDate().equals(currentDate))){
			//计算当前用户下面有多少站点，每个站点的OSS的news文件夹下用了多少存储空间了
			new Thread(new Runnable() {
				public void run() {
					
					//属于该用户的这些网站共占用了多少存储空间去
					long sizeB = AttachmentUtil.getDirectorySize("site/"+site.getId()+"/");
					
					int kb = Math.round(sizeB/1024);
					sqlService.executeSql("UPDATE site SET attachment_update_date = '"+currentDate+"' , attachment_size = "+kb+" WHERE id = "+getUserId());
					site.setAttachmentUpdateDate(currentDate);
					site.setAttachmentSize(kb);
					com.xnx3.wangmarket.admin.util.SessionUtil.setSite(site);
					
					com.xnx3.wangmarket.admin.util.SessionUtil.setAllowUploadForUEditor(kb<site.getAttachmentSizeHave()*1000);
				}
			}).start();
		}else{
			com.xnx3.wangmarket.admin.util.SessionUtil.setAllowUploadForUEditor(site.getAttachmentSize() < site.getAttachmentSizeHave()*1000);
		}
	}
	
}
