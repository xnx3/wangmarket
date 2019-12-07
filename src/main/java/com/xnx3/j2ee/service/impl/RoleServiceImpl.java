package com.xnx3.j2ee.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xnx3.Lang;
import com.xnx3.j2ee.service.RoleService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.bean.RoleMark;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.entity.Permission;
import com.xnx3.j2ee.entity.Role;
import com.xnx3.j2ee.entity.RolePermission;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.entity.UserRole;
@Service
public class RoleServiceImpl implements RoleService{
	
	@Resource
	private SqlDAO sqlDAO;
	
	public SqlDAO getSqlDAO() {
		return sqlDAO;
	}

	public void setSqlDAO(SqlDAO sqlDAO) {
		this.sqlDAO = sqlDAO;
	}

	public List<Permission> findPermissionByRoleId(Integer roleId) {
		List<Permission> list = sqlDAO.findBySqlQuery("SELECT permission.* FROM permission,role_permission WHERE role_permission.permissionid=permission.id AND role_permission.roleid="+roleId, Permission.class);
		return list;
	}

	public BaseVO saveRolePermission(int roleId, String permission) {
		if(roleId==0){
			return BaseVO.failure("传入的编号不正确！");
		}
		
		if(permission == null || permission.trim().length() == 0){
			//避免出现空指针
			permission = "";
		}
		List<RolePermission> myRolePermissionList = sqlDAO.findBySqlQuery("SELECT * FROM role_permission WHERE roleid = " + roleId, RolePermission.class);		//此角色原先的权限
		
		/***增加资源***/
		String permissionArray[] = permission.split(",");		//此角色新编辑的权限
		for (int i = 0; i < permissionArray.length; i++) {
			int pid = Lang.stringToInt(permissionArray[i], 0);
			if(pid>0){
				boolean haveRP=false;	//是否数据库中已经存在了这条资源－角色纪录
				for (int j = 0; j < myRolePermissionList.size(); j++) {
					RolePermission myrp=myRolePermissionList.get(j);
					if(myrp.getPermissionid()==pid){
						haveRP=true;
						break;
					}
				}
				
				if(!haveRP){
					RolePermission rp = new RolePermission();
					rp.setRoleid(roleId);
					rp.setPermissionid(pid);
					sqlDAO.save(rp);
				}
			}
		}
		
		/***删除资源，删除原先有的，新编辑后没有的***/
		for (int i = 0; i < myRolePermissionList.size(); i++) {
			RolePermission myrp=myRolePermissionList.get(i);
			
			boolean haveRP=false;	//是否数据库中有这条纪录，但是新提交修改的并没有这条纪录。默认为没有这条纪录
			for (int j = 0; j < permissionArray.length; j++) {
				int pid = Lang.stringToInt(permissionArray[j], 0);
				if(pid>0&&myrp.getPermissionid()==pid){
					haveRP=true;
					break;
				}
			}
			
			if(!haveRP){
				sqlDAO.delete(myrp);
			}
		}
		
		return BaseVO.success();
	}

	public List<RoleMark> getUserRoleMarkList(int userid) {
		//标记用户当前拥有的角色
		List<RoleMark> roleMarkList = new ArrayList<RoleMark>();
		
		//用户自己有哪些角色
		List<Role> myList = sqlDAO.findBySqlQuery("SELECT role.* FROM user_role,role WHERE user_role.roleid=role.id AND user_role.userid="+userid, Role.class);
		//当下系统里所有权限列表
		List<Role> allList = sqlDAO.findAll(Role.class);
		
		for (int i = 0; i < allList.size(); i++) {
			Role role = allList.get(i);
			RoleMark roleMark = new RoleMark();
			roleMark.setRole(role);
			
			for (int j = 0; j < myList.size(); j++) {
				Role myRole = myList.get(j);
				if(myRole.getId()==role.getId()){
					roleMark.setSelected(true);
					break;
				}
			}
			roleMarkList.add(roleMark);
		}
		
		return roleMarkList;
	}

	public BaseVO saveUserRole(int userid, String role) {
		if(userid==0){
			return BaseVO.failure("传入的用户id不正确");
		}
		User user = sqlDAO.findById(User.class, userid);
		if(user == null){
			return BaseVO.failure("用户不存在！");
		}
		//此用户原先的权限
		List<UserRole> myUserRoleList = sqlDAO.findByProperty(UserRole.class, "userid", userid);
		
		/***增加资源***/
		String roleArray[] = role.split(",");		//此角色新编辑的权限
		for (int i = 0; i < roleArray.length; i++) {
			int rid = Lang.stringToInt(roleArray[i], 0);
			if(rid>0){
				boolean haveUR=false;	//是否数据库中已经存在了这条资源－角色纪录
				for (int j = 0; j < myUserRoleList.size(); j++) {
					UserRole myUR=myUserRoleList.get(j);
					if(myUR.getRoleid()==rid){
						haveUR=true;
						break;
					}
				}
				
				if(!haveUR){
					UserRole ur = new UserRole();
					ur.setRoleid(rid);
					ur.setUserid(userid);
					sqlDAO.save(ur);
				}
			}
		}
		
		/***删除资源，删除原先有的，新编辑后没有的***/
		for (int i = 0; i < myUserRoleList.size(); i++) {
			UserRole myUR=myUserRoleList.get(i);
			
			boolean haveUR=false;	//是否数据库中有这条纪录，但是新提交修改的并没有这条纪录。默认为没有这条纪录
			for (int j = 0; j < roleArray.length; j++) {
				int rid = Lang.stringToInt(roleArray[j], 0);
				if(rid>0&&myUR.getRoleid()==rid){
					haveUR=true;
					break;
				}
			}
			
			if(!haveUR){
				sqlDAO.delete(myUR);
			}
		}
		
		user.setAuthority(role);
		sqlDAO.save(user);
		
		return BaseVO.success();
	}

	public List<Permission> findPermissionByUser(User user) {
		List<Permission> list = null;
		String roles = user.getAuthority();
		if(roles == null || roles.length() == 0){
			list = new ArrayList<Permission>();
			return list;
		}
		
		list = sqlDAO.findBySqlQuery("SELECT permission.* FROM permission,role_permission WHERE role_permission.permissionid=permission.id AND role_permission.roleid IN("+roles+")", Permission.class);
		return list;
	}
	
}
