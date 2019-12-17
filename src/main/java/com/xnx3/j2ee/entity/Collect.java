package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户关注表，用户可互相关注
 * @author 管雷鸣
 */
@Entity
@Table(name = "collect")
public class Collect implements java.io.Serializable {

	private Integer id;		//自增id
	private Integer userid;		//用户id，发起方的user.id，发起关注的人
	private Integer othersid;	//userid这个人关注的其他用户，被关注人
	private Integer addtime; 	//增加时间，也就是关注的时间

	// Constructors

	/** default constructor */
	public Collect() {
	}

	/** full constructor */
	public Collect(Integer userid, Integer othersid, Integer addtime) {
		this.userid = userid;
		this.othersid = othersid;
		this.addtime = addtime;
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

	@Column(name = "userid")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "othersid")
	public Integer getOthersid() {
		return this.othersid;
	}

	public void setOthersid(Integer othersid) {
		this.othersid = othersid;
	}

	@Column(name = "addtime")
	public Integer getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Override
	public String toString() {
		return "Collect [id=" + id + ", userid=" + userid + ", othersid=" + othersid + ", addtime=" + addtime + "]";
	}

}