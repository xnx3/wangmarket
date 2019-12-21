package com.xnx3.j2ee.shiro;

import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import com.xnx3.j2ee.entity.Permission;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.RoleService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.impl.RoleServiceImpl;
import com.xnx3.j2ee.service.impl.SqlServiceImpl;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.bean.PermissionTree;

/**
 * shiro登录的realm
 * @author 管雷鸣
 * 
 */
public class CustomRealm extends AuthorizingRealm {
	@Override
	public void setName(String name) {
		super.setName("CustomRealm");
	}
	
	//realm的认证方法，从数据库查询用户信息
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException{
		
		// token是用户输入的用户名和密码 
		// 第一步从token中取出用户名
		String username = (String) token.getPrincipal();
    	
    	SqlService sqlService = SpringUtil.getSqlService();
    	RoleService roleService = SpringUtil.getBean(RoleServiceImpl.class);
    	
    	User user = sqlService.findAloneBySqlQuery("SELECT * FROM user WHERE username = '"+username+"'", User.class);
		
		if(user!=null){
			user.setLasttime(DateUtil.timeForUnix10());
			sqlService.save(user);
		}
    	
        if (user != null) {  
        	com.xnx3.j2ee.bean.ActiveUser activeUser = new com.xnx3.j2ee.bean.ActiveUser();
	    	activeUser.setUser(user);
   
            //根据用户id查询权限url
    		List<Permission> permissions = roleService.findPermissionByUser(user);
    		activeUser.setPermissions(permissions);
    		
			//转换为树状集合
			List<PermissionTree> permissionTreeList = new ShiroFunc().PermissionToTree(new ArrayList<Permission>(), permissions);	
    		activeUser.setPermissionTreeList(permissionTreeList);
    		
    		String md5Password = new Md5Hash(user.getUsername(), user.getSalt(), 2).toString();
    		//将activeUser设置simpleAuthenticationInfo
    		
    		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
    				activeUser, md5Password,ByteSource.Util.bytes(user.getSalt()), this.getName());
    		
    		return simpleAuthenticationInfo;
        }
        
        return null;  
	}
	
	// 用于授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		com.xnx3.j2ee.bean.ActiveUser activeUser = (com.xnx3.j2ee.bean.ActiveUser) principals.getPrimaryPrincipal();
		List<Permission> permissionList = null;
		try {
			permissionList = activeUser.getPermissions();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//单独定一个集合对象 
		List<String> permissions = new ArrayList<String>();
		if(permissionList!=null){
			for(Permission permission:permissionList){
				//将数据库中的权限标签 符放入集合
				permissions.add(permission.getPercode());
			}
		}
		//查到权限数据，返回授权信息(要包括 上边的permissions)
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		//将上边查询到授权信息填充到simpleAuthorizationInfo对象中
		simpleAuthorizationInfo.addStringPermissions(permissions);

		return simpleAuthorizationInfo;
	}
	
	
	//清除缓存
	public void clearCached() {
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);
	}

}
