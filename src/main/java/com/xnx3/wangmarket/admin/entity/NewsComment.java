package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * NewsComment entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "news_comment")
public class NewsComment implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer newsid;
	private Integer userid;
	private Integer addtime;
	private String text;

	// Constructors

	/** default constructor */
	public NewsComment() {
	}

	/** full constructor */
	public NewsComment(Integer newsid, Integer userid, Integer addtime,
			String text) {
		this.newsid = newsid;
		this.userid = userid;
		this.addtime = addtime;
		this.text = text;
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

	@Column(name = "newsid")
	public Integer getNewsid() {
		return this.newsid;
	}

	public void setNewsid(Integer newsid) {
		this.newsid = newsid;
	}

	@Column(name = "userid")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "addtime")
	public Integer getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "text", length = 200)
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}