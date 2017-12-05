package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PostComment entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "post_comment")
public class PostComment extends BaseEntity{

	// Fields

	private Integer id;
	private Integer postid;
	private Integer addtime;
	private Integer userid;
	private String text;
	public Short isdelete;

	// Constructors

	/** default constructor */
	public PostComment() {
	}

	@Column(name = "isdelete")
	public Short getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Short isdelete) {
		this.isdelete = isdelete;
	}
	
	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "postid", nullable = false)
	public Integer getPostid() {
		return this.postid;
	}

	public void setPostid(Integer postid) {
		this.postid = postid;
	}

	@Column(name = "addtime", nullable = false)
	public Integer getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "userid", nullable = false)
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "text", nullable = false, length = 200)
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}