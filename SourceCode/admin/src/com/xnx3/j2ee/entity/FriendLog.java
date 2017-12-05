package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * FriendLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "friend_log")
public class FriendLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer self;
	private Integer other;
	private Integer time;
	private Short state;
	private String ip;

	// Constructors

	/** default constructor */
	public FriendLog() {
	}

	/** full constructor */
	public FriendLog(Integer self, Integer other, Integer time, Short state,
			String ip) {
		this.self = self;
		this.other = other;
		this.time = time;
		this.state = state;
		this.ip = ip;
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

	@Column(name = "self", nullable = false)
	public Integer getSelf() {
		return this.self;
	}

	public void setSelf(Integer self) {
		this.self = self;
	}

	@Column(name = "other", nullable = false)
	public Integer getOther() {
		return this.other;
	}

	public void setOther(Integer other) {
		this.other = other;
	}

	@Column(name = "time", nullable = false)
	public Integer getTime() {
		return this.time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	@Column(name = "state", nullable = false)
	public Short getState() {
		return this.state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	@Column(name = "ip", nullable = false, length = 15)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}