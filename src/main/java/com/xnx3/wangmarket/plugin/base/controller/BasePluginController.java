package com.xnx3.wangmarket.plugin.base.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.Func;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UserVO;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.plugin.api.service.KeyManageService;
import com.xnx3.wangmarket.plugin.api.vo.UserBeanVO;
import com.xnx3.wangmarket.superadmin.entity.Agency;

/**
 * 所有插件的 Controller 都继承此
 * @author 管雷鸣
 */
public class BasePluginController extends com.xnx3.wangmarket.admin.controller.BaseController {
//	@Resource
//	private SqlService sqlService;

	/**
	 * 根据user表的authority字段的值，用户是否具有某个指定的角色(Role)
	 * @param authority User表的authority
	 * @param roleId role.id判断用户是否具有某个指定的角色，这里便是那个指定的角色，判断用户是否是授权了这个角色。多个用,分割，如传入  2,3,4
	 * 				<br/>
	 * @return true：是,用户拥有此角色
	 */
	public boolean isAuthorityBySpecific(String authority, String roleId){
		return Func.isAuthorityBySpecific(authority, roleId);
	}
	
	/**
	 * 根据user表的authority字段的值，用户是否具有某个指定的角色(Role)
	 * @param authority User表的authority
	 * @param roleId role.id判断用户是否具有某个指定的角色，这里便是那个指定的角色，判断用户是否是授权了这个角色。
	 * 				<br/>
	 * @return true：是,用户拥有此角色
	 */
	public boolean isAuthorityBySpecific(String authority, int roleId){
		return isAuthorityBySpecific(authority, roleId+"");
	}
	
	/**
	 * 判断当前用户是否有某个角色权限
	 * @param roleId 判断是否拥有的角色id，对应 Role.id
	 * @return 
	 * 		<ul>
	 * 			<li>若有这个权限，则返回true</li>
	 * 			<li>若没有这个权限，或未登陆，则返回false</li>
	 * 		</ul>
	 */
	public boolean haveAuthority(int roleId){
		User user = getUser();
		if(user == null){
			return false;
		}
		return isAuthorityBySpecific(user.getAuthority(), roleId);
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
	
	
}
