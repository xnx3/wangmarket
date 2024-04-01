package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 问题反馈 - 目前未用到，先保留
 * @author 管雷鸣
 */
@Entity
@Table(name = "feedback")
public class Feedback implements java.io.Serializable {

	private Integer id;		//自动编号
	private Integer userid;	//反馈用户，对应 user.id
	private Integer addtime; //反馈的时间
	private String text;		//反馈的内容

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", columnDefinition="int(11) COMMENT '自动编号'", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "userid", columnDefinition="int(11) COMMENT '' default '0'")
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "addtime", columnDefinition="int(11) COMMENT ''")
	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "text", columnDefinition="varchar(255) COMMENT '' default ''")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Feedback [id=" + id + ", userid=" + userid + ", addtime="
				+ addtime + ", text=" + text + "]";
	}
	
}