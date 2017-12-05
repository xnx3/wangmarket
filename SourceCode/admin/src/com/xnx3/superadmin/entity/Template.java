package com.xnx3.superadmin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模版
 * @author 管雷鸣
 */
@Entity
@Table(name = "template")
public class Template implements java.io.Serializable {

	private Integer id;			//自动编号
	private String name;			//所属的模版名字
	private Integer userid;		//所属用户的id
	private String remark;		//备注说明
	private Integer siteid;		//模版站，生成此模版的网站、同时也作为案例网站,对应site.id
	private Integer addtime;		//模版添加时间
	private Integer updatetime;	//模版的最后更新时间
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getSiteid() {
		return siteid;
	}
	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}
	public Integer getAddtime() {
		return addtime;
	}
	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}
	public Integer getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Integer updatetime) {
		this.updatetime = updatetime;
	}

}