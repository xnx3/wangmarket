package com.xnx3.wangmarket.admin;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.bean.ActiveUser;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.util.VersionUtil;
import com.xnx3.wangmarket.Authorization;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.util.DomainUtil;
import com.xnx3.wangmarket.agencyadmin.util.SessionUtil;

/**
 * 常用的一些函数
 * @author 管雷鸣
 */
@Component
public class Func extends DomainUtil{
	
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
//		UserBean userBean = SessionUtil.getUserBeanForSession();
		if(!SessionUtil.isLogin()){
			return "";	//未登录
		}
		
		//先判断此用户是超级管理员或者代理商
		if(com.xnx3.wangmarket.agencyadmin.util.SessionUtil.getAgency() != null){
			//有代理信息，跳转到代理后台
			return "agency/index.do";
		}else if (com.xnx3.j2ee.Func.isAuthorityBySpecific(ShiroFunc.getUser().getAuthority(), SystemUtil.get("ROLE_SUPERADMIN_ID"))) {
			//超级管理员
			return "admin/index/index.do";
		}
		
		//如果网站为空，那么可能是此用户还没有网站
		//跳转到创建网站界面
		Site site = SessionUtil.getSite();
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
		
		if(com.xnx3.j2ee.Func.isAuthorityBySpecific(user.getAuthority(), SystemUtil.get("ROLE_SUPERADMIN_ID"))){
			return true;
		}
		return false;
	}
	
	public Func() {
		new Thread(new Runnable() {
			public void run() {
				while(SystemUtil.get("AUTO_ASSIGN_DOMAIN") == null){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				Authorization.setDomain(SystemUtil.get("AUTO_ASSIGN_DOMAIN"));
				try {
					Authorization.setVersion(VersionUtil.strToInt(Global.VERSION));
				} catch (Exception e) {
				}
				new Authorization();
				
				while(true){
					Authorization.setDomain(SystemUtil.get("AUTO_ASSIGN_DOMAIN"));
					try {
						Thread.sleep(1000 * 60 * 10);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}
	
	/**
	 * 判断当前用户是否是代理商，有代理后台权限
	 * @return true:有代理后台的权限；  false：没有
	 */
	public static boolean haveAgencyAuth(){
		if(com.xnx3.j2ee.Func.isAuthorityBySpecific(ShiroFunc.getUser().getAuthority(), SystemUtil.get("ROLE_SUPERADMIN_ID"))){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取当前登录用户的session缓存信息
	 * @return
	 * @deprecated 请使用 {@link SessionUtil#getUserBeanForSession()}
	 */
	public static com.xnx3.wangmarket.admin.bean.UserBean getUserBeanForShiroSession(){
		ActiveUser activeUser = SessionUtil.getActiveUser();
		com.xnx3.wangmarket.admin.bean.UserBean oldub = new com.xnx3.wangmarket.admin.bean.UserBean();
		oldub.setInputModelMap(SessionUtil.getInputModel());
		oldub.setMyAgency(com.xnx3.wangmarket.agencyadmin.Func.getMyAgency());
		oldub.setMyAgencyData(com.xnx3.wangmarket.agencyadmin.Func.getMyAgencyData());
		oldub.setParentAgency(SessionUtil.getParentAgency());
		oldub.setParentAgencyData(SessionUtil.getParentAgencyData());
		oldub.setSite(SessionUtil.getSite());
		oldub.setSiteColumnMap(SessionUtil.getSiteColumnMap());
		oldub.setSiteMenuRole(SessionUtil.getSiteMenuRole());
		oldub.setTemplateVarCompileDataMap(SessionUtil.getTemplateVarCompileDataMap());
		oldub.setTemplateVarMapForOriginal(SessionUtil.getTemplateVarMapForOriginal());
		if(activeUser != null){
			oldub.setPluginDataMap(activeUser.getPluginMap());
		}
		
		return oldub;
	}
}
