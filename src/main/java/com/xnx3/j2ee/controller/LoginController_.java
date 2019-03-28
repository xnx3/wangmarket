package com.xnx3.j2ee.controller;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.j2ee.Func;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.func.Captcha;
import com.xnx3.j2ee.func.Log;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.LoginVO;
import com.xnx3.media.CaptchaUtil;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.util.TemplateAdminMenu.TemplateMenuEnum;
import com.xnx3.wangmarket.superadmin.entity.Agency;
import com.xnx3.wangmarket.superadmin.entity.AgencyData;

/**
 * 登录、注册
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class LoginController_ extends com.xnx3.wangmarket.admin.controller.BaseController {
	@Resource
	private UserService userService;
	@Resource
	private SqlService sqlService;
	

	/**
	 * 验证码图片显示，直接访问此地址可查看图片
	 */
	@RequestMapping("/captcha${url.suffix}")
	public void captcha(HttpServletRequest request,HttpServletResponse response) throws IOException{
		ActionLogCache.insert(request, "获取验证码显示");
		
		CaptchaUtil captchaUtil = new CaptchaUtil();
	    captchaUtil.setCodeCount(5);                   //验证码的数量，若不增加图宽度的话，只能是1～5个之间
	    captchaUtil.setFont(new Font("Fixedsys", Font.BOLD, 21));    //验证码字符串的字体
	    captchaUtil.setHeight(18);  //验证码图片的高度
	    captchaUtil.setWidth(110);      //验证码图片的宽度
//	    captchaUtil.setCode(new String[]{"我","是","验","证","码"});   //如果对于数字＋英文不满意，可以自定义验证码的文字！
	    Captcha.showImage(captchaUtil, request, response);
	}
	
	/**
	 * 登陆页面
	 */
	@RequestMapping("login${url.suffix}")
	public String login(HttpServletRequest request,Model model){
		//检测 MASTER_SITE_URL、 ATTACHMENT_FILE_URL 是否已经设置，即是否已经install安装了
		if(Global.get("MASTER_SITE_URL") == null || Global.get("MASTER_SITE_URL").length() == 0 || Global.get("ATTACHMENT_FILE_URL") == null || Global.get("ATTACHMENT_FILE_URL").length() == 0){
			return error(model, "监测到您尚未安装系统！请先根据提示进行安装", "install/index.do");
		}
		
		if(getUser() != null){
			ActionLogCache.insert(request, "进入登录页面", "已经登录成功，无需再登录，进行跳转");
			return redirect(com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl());
		}
		
		ActionLogCache.insert(request, "进入登录页面");
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
	 * 				<li>11:网站到期/代理到期</li>	
	 * 			</ul>
	 */
	@RequestMapping("loginSubmit${url.suffix}")
	@ResponseBody
	public LoginVO loginSubmit(HttpServletRequest request,Model model){
		LoginVO vo = new LoginVO();
		
		//验证码校验
		BaseVO capVO = Captcha.compare(request.getParameter("code"), request);
		if(capVO.getResult() == BaseVO.FAILURE){
			ActionLogCache.insert(request, "用户名密码模式登录失败", "验证码出错，提交的验证码："+StringUtil.filterXss(request.getParameter("code")));
			vo.setBaseVO(capVO);
			return vo;
		}else{
			//验证码校验通过
			
			BaseVO baseVO =  userService.loginByUsernameAndPassword(request);
			vo.setBaseVO(baseVO);
			if(baseVO.getResult() == BaseVO.SUCCESS){
				//登录成功,BaseVO.info字段将赋予成功后跳转的地址，所以这里要再进行判断
				
				//用于缓存入Session，用户的一些基本信息，比如用户的站点信息、用户的上级代理信息、如果当前用户是代理，还包含当前用户的代理信息等
				UserBean userBean = new UserBean();
				
				//得到当前登录的用户的信息
				User user = getUser();
				//可以根据用户的不同权限，来判断用户登录成功后要跳转到哪个页面
				if(Func.isAuthorityBySpecific(user.getAuthority(), Global.get("ROLE_SUPERADMIN_ID"))){
					//如果是超级管理员，那么跳转到管理后台
					vo.setInfo("admin/index/index.do");
					ActionLogCache.insert(request, "用户名密码模式登录成功","进入管理后台admin/index/");
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
						if(user.getSiteid() != null && user.getSiteid() > 0){
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
						userBean.setParentAgency(parentAgency);
						
						if(parentAgency != null){
							//得到上级代理的变长表信息
							AgencyData parentAgencyData = sqlService.findAloneBySqlQuery("SELECT * FROM agency_data WHERE id = " + parentAgency.getId(), AgencyData.class);
							userBean.setParentAgencyData(parentAgencyData);
						}
					}
					
					//当前时间
					int currentTime = DateUtil.timeForUnix10();	
					
					//判断当前用户的权限，是代理还是网站使用者
					if(Func.isAuthorityBySpecific(user.getAuthority(), Global.get("ROLE_USER_ID"))){
						//普通用户，建站用户，网站使用者
						Site site = null;
						
						//判断是否哟siteid，也就是是否是网站管理的子用户。因为子用户是有user.siteid的
						if(user.getSiteid() != null && user.getSiteid() > 0){
							//是网站管理子用户.既然是有子账户了，那肯定子账户插件是使用了，也就可以进行一下操作了
							site = sqlService.findById(Site.class, user.getSiteid());
							
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
							userBean.setSiteMenuRole(menuMap);
							
						}else{
							//是网站管理者，拥有所有权限的
							//得到当前用户站点的相关信息，加入userBean，以存入Session缓存起来
							site = sqlService.findAloneBySqlQuery("SELECT * FROM site WHERE userid = "+getUserId()+" ORDER BY id DESC", Site.class);
							Log.info("网站管理者，拥有网站所有权限:"+user.getUsername());
							
							//将拥有所有功能的管理权限，将功能菜单全部遍历出来，赋予这个用户
							Map<String, String> menuMap = new HashMap<String, String>();
							for (TemplateMenuEnum e : TemplateMenuEnum.values()) {
								menuMap.put(e.id, "1");
							}
							userBean.setSiteMenuRole(menuMap);
							
						}
						if(site == null){
							vo.setResult(BaseVO.FAILURE);
							vo.setInfo("出错！所管理的网站不存在！");
							return vo;
						}
						//将所管理的网站加入session缓存
						userBean.setSite(site);
						
						//判断网站用户是否是已过期，使用期满，将无法使用
						if(site != null && site.getExpiretime() != null && site.getExpiretime() < currentTime){
							//您的网站已到期。若要继续使用，请续费
							String info = "";
							try {
								info = "您的网站已于 "+DateUtil.dateFormat(site.getExpiretime(), "yyyy-MM-dd")+" 到期！"
										+ "<br/>若要继续使用，请联系："
										+ "<br/>姓名："+userBean.getParentAgency().getName()
										+ "<br/>QQ："+userBean.getParentAgency().getQq()
										+ "<br/>电话："+userBean.getParentAgency().getPhone();
							} catch (NotReturnValueException e) {
								e.printStackTrace();
							}
							vo.setBaseVO(11, info);
							ShiroFunc.getCurrentActiveUser().setObj(null);  	//清空 Session信息
							return vo;
						}
						
						//计算其网站所使用的资源，比如OSS已占用了多少资源
						loginSuccessComputeUsedResource();
						
						ActionLogCache.insert(request, "用户名密码模式登录成功","进入网站管理后台");
					}else{
						//代理
						
						//得到当前用户代理的相关信息，加入userBean，以存入Session缓存起来
						Agency myAgency = sqlService.findAloneBySqlQuery("SELECT * FROM agency WHERE userid = " + getUserId(), Agency.class);
						userBean.setMyAgency(myAgency);
						if(myAgency != null){
							//得到当前代理用户的变长表信息
							AgencyData myAgencyData = sqlService.findAloneBySqlQuery("SELECT * FROM agency_data WHERE id = " + myAgency.getId(), AgencyData.class);
							userBean.setMyAgencyData(myAgencyData);
						}
						
						//判断当前代理是否是已过期，使用期满，将无法登录
						if (myAgency != null && myAgency.getExpiretime() != null && myAgency.getExpiretime() < currentTime){
							//您的代理资格已到期。若要继续使用，请联系您的上级
							//BaseVO vo = new BaseVO();
							String info = "";
							try {
								info = "您的代理资格已于 "+DateUtil.dateFormat(myAgency.getExpiretime(), "yyyy-MM-dd")+" 到期！"
										+ "<br/>若要继续使用，请联系："
										+ "<br/>姓名："+userBean.getParentAgency().getName()
										+ "<br/>QQ："+userBean.getParentAgency().getQq()
										+ "<br/>电话："+userBean.getParentAgency().getPhone();
							} catch (NotReturnValueException e) {
								e.printStackTrace();
							}
							vo.setBaseVO(11, info);
							ShiroFunc.getCurrentActiveUser().setObj(null);  	//清空 Session信息
							return vo;
						}
						
						ActionLogCache.insert(request, "用户名密码模式登录成功","进入代理后台");
					}
					
					//设置登录成功后，是跳转到总管理后台、代理后台，还是跳转到wap、pc、cms
					vo.setInfo(com.xnx3.wangmarket.admin.Func.getConsoleRedirectUrl());	
				}
				
				//将用户相关信息加入Shiro缓存
				ShiroFunc.getCurrentActiveUser().setObj(userBean);
				
				//将iwSID加入vo返回
				HttpSession session = request.getSession();
				vo.setIwSID(session.getId());
			}else{
				ActionLogCache.insert(request, "用户名密码模式登录失败",baseVO.getInfo());
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
		//获取其下有多少网站
		final List<Site> list = sqlService.findBySqlQuery("SELECT * FROM site WHERE userid = "+getUserId(), Site.class);
		
		//如果这个用户是单纯的网站用户，并且今天并没有过空间计算，那么就要计算其所有的空间了
		final String currentDate = DateUtil.currentDate("yyyyMMdd");
	
		
		if((getUser().getOssUpdateDate() == null) || (getUser().getAuthority().equals(Global.get("USER_REG_ROLE")) && !getUser().getOssUpdateDate().equals(currentDate))){
			//计算当前用户下面有多少站点，每个站点的OSS的news文件夹下用了多少存储空间了
			new Thread(new Runnable() {
				public void run() {
					
					//属于该用户的这些网站共占用了多少存储空间去
					long sizeB = 0;
					try {
						for (int i = 0; i < list.size(); i++) {
							sizeB += AttachmentFile.getDirectorySize("site/"+list.get(i).getId()+"/");
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("你应该是还没配置开通OSS吧~~要想上传图片上传附件，还是老老实实，访问 /instal/index.do 进行安装吧");
					}
					int kb = Math.round(sizeB/1024);
					sqlService.executeSql("UPDATE user SET oss_update_date = '"+currentDate+"' , oss_size = "+kb+" WHERE id = "+getUserId());
					ShiroFunc.getUser().setOssSize(kb);
					
					ShiroFunc.setUEditorAllowUpload(kb<ShiroFunc.getUser().getOssSizeHave()*1000);
				}
			}).start();
		}else{
			ShiroFunc.setUEditorAllowUpload(getUser().getOssSize()<ShiroFunc.getUser().getOssSizeHave()*1000);
		}
	}
	
	
	
}
