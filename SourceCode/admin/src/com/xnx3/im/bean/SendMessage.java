package com.xnx3.im.bean;
/**
 * 向别人发送消息，消息体是 {@link Message}，相当于是对 {@link Message}进行格式化,筛选出有用的信息发送到对方socket客户端
 * @author 管雷鸣
 */
public class SendMessage {
	private String username;
	private String avatar;
	private long id;
	private String type;
	private String content;
	private String cid;
	private boolean mine;
	private long fromid;
	private long timestamp;
	
	public SendMessage(Message message) {
		this.username = message.getSendUserName();
		this.avatar = message.getSendAvatar();
		this.id = message.getSendId();
		this.type = message.getReceiveType();
		this.content = message.getContent();
		this.cid = message.getid();
		this.fromid = message.getSendId();
		this.timestamp = message.getTimestamp();
		
		this.mine = false;
	}

	public String getUsername() {
		return username;
	}

	public String getAvatar() {
		return avatar;
	}

	public long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getContent() {
		return content;
	}

	public String getCid() {
		return cid;
	}

	public boolean isMine() {
		return mine;
	}

	public long getFromid() {
		return fromid;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "SendMessage [username=" + username + ", avatar=" + avatar
				+ ", id=" + id + ", type=" + type + ", content=" + content
				+ ", cid=" + cid + ", mine=" + mine + ", fromid=" + fromid
				+ ", timestamp=" + timestamp + "]";
	}
	
}
