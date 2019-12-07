package com.xnx3.j2ee.bean;

import java.util.List;


/**
 * 角色权限体系-资源树（权限），两级
 * @author 管雷鸣
 *
 */
public class PermissionTree {
	
	private PermissionMark permissionMark;	//顶级permission
	private List<PermissionMark> list;	//下级的Permission资源列表
	public PermissionMark getPermissionMark() {
		return permissionMark;
	}
	public void setPermissionMark(PermissionMark permissionMark) {
		this.permissionMark = permissionMark;
	}
	public List<PermissionMark> getList() {
		return list;
	}
	public void setList(List<PermissionMark> list) {
		this.list = list;
	}
	
	
}


