package com.xnx3.im.bean;

import com.xnx3.DateUtil;
import com.xnx3.Lang;

/**
 * 用户间的消息回复，客户端接收消息。如两个人、群组等消息交互
 * @author 管雷鸣
 */
public class Message {
	private String id;	//此消息的唯一id
	
	/* 此两个socket，为接收方的socket标示 */
	private long socketId;	//Socket服务的id，编号。用户客户端跟socket建立链接，此时的该用户的id便会作为socket的id，若用户没有id则默认分配一个uuid
	private String socketUuid;	//socket连接的uuid，两个uuid加起来的字符串
	
	/* 对话的消息id，双方对话的这个id是一样的。一个此id对应者一个会话(双方) */
//	private String chatUuid;	
	
	private String sendUserName;	//发送者，用户名
	private String sendAvatar;	//发送者，用户头像
	private long sendId;			//发送者ID，消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
	private String content;	//消息内容，当然，也是发送者发出的消息内容
	
	private long receiveId;		//消息接收者ID
	private String receiveAvatar;	//消息接收者，用户头像
	private String receiveUserName;	//消息接收者，用户名
	private String receiveType;		//消息接收者，聊天窗口来源类型，从发送消息传递的to里面获取。friend普通用户发消息、group群聊、kefu客服模式
	
	private long timestamp;		//服务端时间戳, 此消息产生的时间。注意：JS中的时间戳是精确到毫秒，如果你返回的是标准的 unix 时间戳，记得要 *1000

	public Message() {
		this.id = Lang.uuid();
		this.timestamp = DateUtil.timeForUnix13();
	}
	
	public String getid() {
		return id;
	}
	public long getSocketId() {
		return socketId;
	}

	public void setSocketId(long socketId) {
		this.socketId = socketId;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getSendAvatar() {
		return sendAvatar;
	}

	public void setSendAvatar(String sendAvatar) {
		this.sendAvatar = sendAvatar;
	}

	public long getSendId() {
		return sendId;
	}

	public void setSendId(long sendId) {
		this.sendId = sendId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(long receiveId) {
		this.receiveId = receiveId;
	}

	public String getReceiveAvatar() {
		return receiveAvatar;
	}

	public void setReceiveAvatar(String receiveAvatar) {
		this.receiveAvatar = receiveAvatar;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getSocketUuid() {
		return socketUuid;
	}

	public void setSocketUuid(String socketUuid) {
		this.socketUuid = socketUuid;
	}

}
