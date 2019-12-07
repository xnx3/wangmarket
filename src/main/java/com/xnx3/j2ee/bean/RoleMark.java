package com.xnx3.j2ee.bean;

import com.xnx3.j2ee.entity.Role;
/**
 * 角色权限体系-角色标示
 * @author 管雷鸣
 *
 */
public class RoleMark {
	
	private boolean selected;	//是否选中
	private Role role;
	
	public RoleMark() {
		this.selected=false;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
