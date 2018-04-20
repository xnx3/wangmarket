package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * MessageData entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "message_data")
public class MessageData implements java.io.Serializable {

	// Fields

	private Integer id;
	private String content;

	// Constructors

	/** default constructor */
	public MessageData() {
	}

	/** full constructor */
	public MessageData(Integer id, String content) {
		this.id = id;
		this.content = content;
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

	@Column(name = "content", nullable = false, length = 400)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}