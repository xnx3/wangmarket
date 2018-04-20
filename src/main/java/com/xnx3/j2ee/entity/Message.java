package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Message entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "message")
public class Message extends BaseEntity{
	/**
	 * 已读
	 */
	public final static Short MESSAGE_STATE_READ=1;
	/**
	 * 未读
	 */
	public final static Short MESSAGE_STATE_UNREAD=0;

	// Fields

	private Integer id;
	private Integer senderid;
	private Integer recipientid;
	private Integer time;
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
	public Message() {
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

	public Integer getSenderid() {
		return senderid;
	}

	public void setSenderid(Integer senderid) {
		this.senderid = senderid;
	}

	public Integer getRecipientid() {
		return recipientid;
	}

	public void setRecipientid(Integer recipientid) {
		this.recipientid = recipientid;
	}


}