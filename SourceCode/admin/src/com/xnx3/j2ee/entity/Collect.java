package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Collect entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "collect")
public class Collect implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer userid;
	private Integer othersid;
	private Integer addtime;

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

}