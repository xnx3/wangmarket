package com.xnx3.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * NewsData entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "news_data")
public class NewsData implements java.io.Serializable {

	// Fields

	private Integer id;
	private String text;

	// Constructors

	/** default constructor */
	public NewsData() {
	}

	/** minimal constructor */
	public NewsData(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public NewsData(Integer id, String text) {
		this.id = id;
		this.text = text;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "text", length = 16777215)
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}