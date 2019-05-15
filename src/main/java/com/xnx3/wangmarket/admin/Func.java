package com.xnx3.wangmarket.admin;

import javax.servlet.http.HttpServletRequest;

import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.shiro.ActiveUser;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * 常用的一些函数
 * @author 管雷鸣
 */
public class Func {
	
	/**
	 * 从Shrio的Session中获取当前用户的代理相关信息、站点信息、以及当前用户的上级的代理相关信息
	 * @return {@link AgencyBean} 或 null
	 */
	public static UserBean getUserBeanForShiroSession(){
		ActiveUser au = ShiroFunc.getCurrentActiveUser();
		if(au == null){
			return null;
		}
		UserBean userBean = (UserBean) au.getObj();
		if(userBean == null){
			return null;
		}else{
			return userBean;
		}
	}
	
	/**
	 * 判断是wap模式还是pc模式。若没有传递 client=pc，其余的统一认为是wap模式
	 * @param request
	 * @return true:WAP； false:PC
	 */
	public static boolean getPcOrWap(HttpServletRequest request){
		String client = request.getParameter("client");
		if(client == null || client.length() == 0){
			return true;
		}else{
			if(client.equals("pc")){
				return false;
			}else{
				return true;
			}
		}
	}
	
	/**
	 * 获取当前用户登陆的站点信息。若是不存在，则返回null
	 * @return
	 */
	public static Site getCurrentSite(){
		UserBean userBean = getUserBeanForShiroSession();
		if(userBean == null){
			return null;
		}
		if(userBean.getSite() == null){
			return null;
		}else{
			return userBean.getSite();
		}
	}
	
	/**
	 * 判断是否是CMS类型的建站
	 * @return
	 */
	public static boolean isCMS(Site site){
		//类型便是CMS类型。以后这个为判断标准
		if(site.getClient() - Site.CLIENT_CMS == 0){
			return true;
		}
		//通用模版编号为0
		if(site.getTemplateId() == null || site.getTemplateId() == 0){
			return true;
		}
		//有使用模版
		if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
			return true;
		}
		
		return false;
	}
	

	/**
	 * 获取当前网站的访问域名。若是绑定顶级域名了，优先使用顶级域名。
	 * @return 访问域名，如 leiwen.wang.market
	 */
	public static String getDomain(Site site){
		if(site == null){
			return "";
		}
		if(site.getBindDomain() != null && site.getBindDomain().length() > 3){
			return site.getBindDomain();
		}else{
			return site.getDomain()+".wang.market";
		}
	}
	
	/**
	 * 重定向跳转至当前登录用户的网站所属类型(wap\pc\cms)的控制台，或者代理后台、超级管理员后台，又或创建网站的页面
	 * @return 重定向跳转网址。返回包含：
	 * 		<ul>
	 * 			<li>template/index.do</li>
	 * 			<li>sites/editPcIndex.do</li>
	 * 			<li>sites/editWapIndex.do</li>
	 * 			<li>admin/index/index.do 代理后台，总管理后台都是用这个</li>
	 * 		</ul>
	 */
	public static String getConsoleRedirectUrl(){
		UserBean userBean = Func.getUserBeanForShiroSession();
		if(userBean == null){
			return "";	//未登录
		}
		
		//先判断此用户是超级管理员或者代理商
		if(userBean.getMyAgency() != null){
			//有代理信息，跳转到代理后台
			return "admin/index/index.do";
		}else if (com.xnx3.j2ee.Func.isAuthorityBySpecific(ShiroFunc.getUser().getAuthority(), Global.get("ROLE_SUPERADMIN_ID"))) {
			//超级管理员
			return "admin/index/index.do";
		}
		
		//如果网站为空，那么可能是此用户还没有网站
		//跳转到创建网站界面
		Site site = userBean.getSite();
		if(site == null){
			//既不是代理，也不是超级管理员，那肯定就是用户权限了。用户权限没有网站，那就跳转到网站创建页面
			//v3.9以后，这种情况是不存在的。账号跟网站是一块创建的
			//临时这个链接
			return "template/index.do";
		}
		
		if(site.getClient() - Site.CLIENT_CMS == 0){
			return "template/index.do";
		}else if(site.getClient() - Site.CLIENT_PC == 0){
			return "sitePc/index.do";
		}else if(site.getClient() - Site.CLIENT_WAP == 0){
			return "siteWap/index.do";
		}else{
			System.out.println("--------Func.getConsoleRedirectUrl 未发现是神马的。siteid:"+site.getId());
		}
		return "";
	}
	
	/**
	 * 判断当前用户是否是超级管理员，有总管理后台权限
	 * @return true:有总管理后台的权限；  false：没有
	 */
	public static boolean haveSuperAdminAuth(){
		User user = ShiroFunc.getUser();
		if(user == null){
			//未登陆，那就直接是false
			return false;
		}
		
		if(com.xnx3.j2ee.Func.isAuthorityBySpecific(user.getAuthority(), Global.get("ROLE_SUPERADMIN_ID"))){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 判断当前用户是否是代理商，有代理后台权限
	 * @return true:有代理后台的权限；  false：没有
	 */
	public static boolean haveAgencyAuth(){
		if(com.xnx3.j2ee.Func.isAuthorityBySpecific(ShiroFunc.getUser().getAuthority(), Global.get("ROLE_SUPERADMIN_ID"))){
			return true;
		}
		return false;
	}
}
