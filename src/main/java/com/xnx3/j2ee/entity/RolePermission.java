package com.xnx3.j2ee.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 总管理后台-权限管理-角色。如普通用户、超级管理员
 */
@Entity
@Table(name = "role_permission")
public class RolePermission implements java.io.Serializable {

	private Integer id;				//自动编号id
	private Integer permissionid;	//资源id，permission.id，一个角色可以拥有多个permission资源
	private Integer roleid;			//角色id，role.id，一个角色可以拥有多个permission资源
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPermissionid() {
		return permissionid;
	}
	public void setPermissionid(Integer permissionid) {
		this.permissionid = permissionid;
	}
	public Integer getRoleid() {
		return roleid;
	}
	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	@Override
	public String toString() {
		return "RolePermission [id=" + id + ", permissionid=" + permissionid + ", roleid=" + roleid + "]";
	}
}