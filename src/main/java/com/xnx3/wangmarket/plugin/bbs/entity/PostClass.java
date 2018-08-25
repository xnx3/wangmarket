package com.xnx3.wangmarket.plugin.bbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import com.xnx3.j2ee.entity.BaseEntity;

/**
 * 论坛板块
 * @author 管雷鸣
 *
 */
@Entity
@Table(name = "plugin_bbs_post_class")
public class PostClass extends BaseEntity {

	private Integer id;
	private String name;
	public Short isdelete;		//是否被删除，1已删除， 0正常
	private Integer siteid;		//该帖子属于哪个网站，站点，对应 site.id

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}
	
	@Column(name = "isdelete")
	public Short getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Short isdelete) {
		this.isdelete = isdelete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	@Override
	public String toString() {
		return "PostClass [id=" + id + ", name=" + name + ", isdelete=" + isdelete + ", siteid=" + siteid + "]";
	}
	
	
}