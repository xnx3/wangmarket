package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 消息-消息内容分表，存储消息的主体内容
 * @author 管雷鸣
 */
@Entity
@Table(name = "message_data")
public class MessageData implements java.io.Serializable {
	private Integer id;
	private String content;

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

	@Column(name = "content", nullable = false)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MessageData [id=" + id + ", content=" + content + "]";
	}

}