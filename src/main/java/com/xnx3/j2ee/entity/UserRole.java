package com.xnx3.j2ee.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户-角色权限 的关联，某个用户属于哪个角色。
 * 本系统这里一个用户只有一个角色（如果想有多个角色，可以自行改动controller中的逻辑）
 */
@Entity
@Table(name = "user_role")
public class UserRole implements java.io.Serializable {
	
	private Integer id;			//自动编号
	private Integer userid;		//用户的id，对应user.id
	private Integer roleid;		//角色的id，对应role.id
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getRoleid() {
		return roleid;
	}
	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	@Override
	public String toString() {
		return "UserRole [id=" + id + ", userid=" + userid + ", roleid=" + roleid + "]";
	}
}