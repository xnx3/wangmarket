package com.xnx3.j2ee.service;

import java.util.List;

import com.xnx3.j2ee.bean.RoleMark;
import com.xnx3.j2ee.entity.Permission;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 角色、权限相关
 * @author 管雷鸣
 *
 */
public interface RoleService {
	/**
	 * 获取制定角色id下的资源列表，获取某个角色拥有哪些资源
	 * @param roleId 要获取的资源id ， 对应role.id
	 * @return 该角色的资源列表
	 */
	public List<Permission> findPermissionByRoleId(Integer roleId);
	
	
	/**
	 * 修改某个角色下所拥有的资源，提交保存的操作。将角色下的资源进行修改保存
	 * @param roleId 修改的角色id，对应role.id
	 * @param permission 角色对应的资源列表，会将传入的这个字符串进行分割成数个单独的permission.id，然后存入role_permission表。这里传入的值如“1,2,3,55,35”，即form表单提交的多选框
	 */
	public BaseVO saveRolePermission(int roleId, String permission);
	
	/**
	 * 获取某个用户拥有哪些角色，在所有的角色列表中将用户拥有的角色标记出来。当然，列出来的是当前系统的所有的角色。只不过用户拥有的角色会被特殊标记
	 * @param userid 要查看的用户的id，对应 user.id
	 * @return 标记出用户拥有哪几个角色的，当前系统的所有角色列表
	 */
	public List<RoleMark> getUserRoleMarkList(int userid);
	
	/**
	 * 保存用户拥有哪些角色，多用于form表单提交后，多选框将选中的角色保存
	 * @param userid 要修改的用户的id，对应user.id
	 * @param role 字符串，如权限多选框提交列表，如 1,2,3,4,5
	 */
	public BaseVO saveUserRole(int userid, String role);
	
	/**
	 * 获取制定用户下的资源列表，获取某个角色拥有哪些资源
	 * @param user 要获取的用户的对象，其实主要用其 {@link User#getAuthority()}
	 * @return 该用户所授权的的资源列表
	 */
	public List<Permission> findPermissionByUser(User user);
}