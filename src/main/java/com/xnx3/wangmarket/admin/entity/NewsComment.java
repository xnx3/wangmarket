package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * NewsComment entity. 
 * @author 管雷鸣
 */
@Entity
@Table(name = "news_comment")
public class NewsComment implements java.io.Serializable {

	// Fields
	private Integer id;
	private Integer newsid;		//关联news.id
	private Integer userid;		//关联user.id，评论用户的id
	private Integer addtime;	//添加时间
	private String text;		//评论内容

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
	@Column(name = "id",columnDefinition="int(11) COMMENT '自动编号'", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "newsid", columnDefinition="int(11) COMMENT '关联news.id' default '0'")
	public Integer getNewsid() {
		return this.newsid;
	}

	public void setNewsid(Integer newsid) {
		this.newsid = newsid;
	}

	@Column(name = "userid", columnDefinition="int(11) COMMENT '关联user.id，评论用户的id' default '0'")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "addtime", columnDefinition="int(11) COMMENT '添加时间'")
	public Integer getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "text", columnDefinition="varchar(200) COMMENT '评论内容' default ''")
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "NewsComment [id=" + id + ", newsid=" + newsid + ", userid=" + userid + ", addtime=" + addtime
				+ ", text=" + text + "]";
	}

}