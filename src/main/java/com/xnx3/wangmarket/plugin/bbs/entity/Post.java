package com.xnx3.wangmarket.plugin.bbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import com.xnx3.j2ee.entity.BaseEntity;

/**
 * Post entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "plugin_bbs_post")
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
	
	private Integer id;
	private Integer classid; 	//发帖分类,帖子所属分类，对应 postClass.id
	private String title;		//帖子标题
	private Integer view;		//查看次数
	private String info;		//简介
	private Integer addtime;	//发布时间
	private Integer userid;		//帖子的发布用户，对应 user.id
//	private Short state;		//状态，0:已删除，1:正常，2:审核中，3:审核完毕不符合要求，4:锁定冻结中，不允许回复
	private Integer siteid;		//该帖子属于哪个网站，站点，对应 site.id
	private Short isdelete;		//是否被删除，1已删除， 0正常
	
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

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	public Short getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Short isdelete) {
		this.isdelete = isdelete;
	}
	
}