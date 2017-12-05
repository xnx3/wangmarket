package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PostClass entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "post_class")
public class PostClass extends BaseEntity {

	private Integer id;
	private String name;
	public Short isdelete;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}
	
	@Column(name = "isdelete")
	public Short getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Short isdelete) {
		this.isdelete = isdelete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "PostClass [getId()=" + getId() + ", getName()=" + getName()
				+ ", getIsdelete()=" + getIsdelete() + "]";
	}
	
}