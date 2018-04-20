package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PostData entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "post_data")
public class PostData implements java.io.Serializable {

	// Fields

	private Integer postid;
	private String text;

	// Constructors

	/** default constructor */
	public PostData() {
	}

	/** full constructor */
	public PostData(Integer postid, String text) {
		this.postid = postid;
		this.text = text;
	}

	// Property accessors
	@Id
	@Column(name = "postid", unique = true, nullable = false)
	public Integer getPostid() {
		return this.postid;
	}

	public void setPostid(Integer postid) {
		this.postid = postid;
	}

	@Column(name = "text", nullable = false, length = 65535)
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}