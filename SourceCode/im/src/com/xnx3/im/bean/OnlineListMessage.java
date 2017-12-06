package com.xnx3.im.bean;


/**
 * 在线列表，用于服务端每当有用户上线或者离线后，即当在线用户发生变化时，向所有当前在线的用户进行推送消息，更新用户客户端的在线用户列表
 * @author 管雷鸣
 */
public class OnlineListMessage {
	private String type;		//消息类型，这里的类型便是 onlineList
	private String username;
	private long id;
	private String avatar;
	private String sign;
	private String groupid;	//在线列表中，所属于哪个组
	
	public String getType() {
		return type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	@Override
	public String toString() {
		return "OnlineListMessage [type=" + type + ", username=" + username
				+ ", id=" + id + ", avatar=" + avatar + ", sign=" + sign + "]";
	}
}
