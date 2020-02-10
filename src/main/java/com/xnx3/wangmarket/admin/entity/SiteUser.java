package com.xnx3.wangmarket.admin.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xnx3.j2ee.entity.User;

/**
 * 站点独有的用户相关信息，原本 {@link User} 的扩展。相当于在User表中又增加了几个字段，只是不破坏原本User表而已，所以又增加了一个数据表
 * @author 管雷鸣
 */
@Entity
@Table(name = "site_user")
public class SiteUser implements Serializable{
	private Integer id;		//用户id，对应 User.id
	
	//v4.9增加,v5.0版本从user表中转移到site_user,此用户拥有哪个站点的管理权。网站开通子账号会用到这个。如果这个有值，那么就是子账号了。对应 site.id
	private Integer siteid;
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "siteid", columnDefinition="int(11) comment 'v4.9增加,v5.0版本从user表中转移到site_user,此用户拥有哪个站点的管理权。网站开通子账号会用到这个。如果这个有值，那么就是子账号了。对应 site.id'")
	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}
	
	
}	
