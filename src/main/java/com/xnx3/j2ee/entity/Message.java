package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 消息表，发送消息
 * @author 管雷鸣
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

	private Integer id;
	private Integer senderid;		//自己的userid，发信人的userid，若是为0则为系统信息
	private Integer recipientid;	//给谁发信，这就是谁的userid，目标用户的userid，收信人id
	private Integer time;			//此信息的发送时间
	private Short state;			//0:未读，1:已读
	public Short isdelete;			//是否已经被删除。0正常，1已删除，
	

	/** default constructor */
	public Message() {
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

	@Column(name = "isdelete")
	public Short getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Short isdelete) {
		this.isdelete = isdelete;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", senderid=" + senderid + ", recipientid=" + recipientid + ", time=" + time
				+ ", state=" + state + ", isdelete=" + isdelete + "]";
	}

}