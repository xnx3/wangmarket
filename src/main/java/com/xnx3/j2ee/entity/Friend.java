package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Friend entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "friend")
public class Friend implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer self;
	private Integer other;

	// Constructors

	/** default constructor */
	public Friend() {
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

}