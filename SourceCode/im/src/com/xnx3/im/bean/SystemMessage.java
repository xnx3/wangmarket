package com.xnx3.im.bean;

/**
 * 系统消息回复，客户端接收消息。如系统回复某个用户信息
 * @author 管雷鸣
 */
public class SystemMessage {
	private long id;		//聊天窗口ID，用户id（包含游客id ）
	private String type;	//聊天窗口类型,如friend
	private String content;	//信息内容
	private boolean system = true;	//是否是系统消息
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isSystem() {
		return system;
	}

	@Override
	public String toString() {
		return "SystemMessageReply [id=" + id + ", type=" + type + ", content="
				+ content + ", system=" + system + "]";
	}
	
}
