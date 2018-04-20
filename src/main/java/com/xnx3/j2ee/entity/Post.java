package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Post entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "post")
public class Post extends BaseEntity {
	/**
	 * 状态：正常
	 */
	public final static Short STATE_NORMAL=1;
	/**
	 * 审核中
	 */
	public final static Short STATE_AUDITING=2;
	/**
	 * 审核完毕不符合要求
	 */
	public final static Short STATE_INCONGRUENT=3;
	/**
	 * 锁定冻结中
	 */
	public final static Short STATE_LOCK=4;
	
	// Fields

	private Integer id;
	private Integer classid;
	private String title;
	private Integer view;
	private String info;
	private Integer addtime;
	private Integer userid;
	private Short state;
	public Short isdelete;

	@Column(name = "isdelete")
	public Short getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Short isdelete) {
		this.isdelete = isdelete;
	}
	// Constructors

	/** default constructor */
	public Post() {
	}

	/** minimal constructor */
	public Post(Integer classid, String title, Integer view, Integer addtime,
			Integer userid) {
		this.classid = classid;
		this.title = title;
		this.view = view;
		this.addtime = addtime;
		this.userid = userid;
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

	@Column(name = "classid", nullable = false)
	public Integer getClassid() {
		return this.classid;
	}

	public void setClassid(Integer classid) {
		this.classid = classid;
	}

	@Column(name = "title", nullable = false, length = 30)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "view", nullable = false)
	public Integer getView() {
		return this.view;
	}

	public void setView(Integer view) {
		this.view = view;
	}

	@Column(name = "info", length = 60)
	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
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

	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Post [getIsdelete()=" + getIsdelete() + ", getId()=" + getId()
				+ ", getClassid()=" + getClassid() + ", getTitle()="
				+ getTitle() + ", getView()=" + getView() + ", getInfo()="
				+ getInfo() + ", getAddtime()=" + getAddtime()
				+ ", getUserid()=" + getUserid() + ", getState()=" + getState()
				+ "]";
	}
	
}