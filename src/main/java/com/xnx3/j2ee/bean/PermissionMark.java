package com.xnx3.j2ee.bean;

import java.io.Serializable;

import com.xnx3.j2ee.entity.Permission;

/**
 * 角色权限体系-资源标示
 * @author 管雷鸣
 *
 */
public class PermissionMark implements Serializable{
	private boolean selected;	//是否选中
	private Permission permission;
	
	public PermissionMark() {
		this.selected=false;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean isSelected) {
		this.selected = isSelected;
	}
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}	
	
	
}