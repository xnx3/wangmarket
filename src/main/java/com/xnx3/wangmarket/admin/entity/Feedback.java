package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 问题反馈
 * @author 管雷鸣
 */
@Entity
@Table(name = "feedback")
public class Feedback implements java.io.Serializable {

	private Integer id;
	private Integer userid;
	private Integer addtime;
	private String text;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
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