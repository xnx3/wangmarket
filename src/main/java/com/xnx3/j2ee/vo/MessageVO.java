package com.xnx3.j2ee.vo;

import com.xnx3.j2ee.entity.Message;
import com.xnx3.j2ee.entity.User;

/**
 * 组合好的Message，包含 {@link Message}、 {@link MessageData}
 * <br/>首先判断getResult()是否是 {@link BaseVO#SUCCESS}，若是，才可以调取其他的值。若不是，可通过getInfo()获取错误信息
 * @author 管雷鸣
 *
 */
public class MessageVO extends BaseVO {
	private Message message;
	private User senderUser;
	private User recipientUser;
	private String content;	//信息的内容
	
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public User getSenderUser() {
		return senderUser;
	}
	public void setSenderUser(User senderUser) {
		this.senderUser = senderUser;
	}
	public User getRecipientUser() {
		return recipientUser;
	}
	public void setRecipientUser(User recipientUser) {
		this.recipientUser = recipientUser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
