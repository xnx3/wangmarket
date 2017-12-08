package com.xnx3.im.bean;

/**
 * 用户设置的相关信息,某些数据对应数据表 im 
 * @author 管雷鸣
 */
public class Im {
	private long userid;		//当前im设置是属于哪个注册用户的，长度在int中。大于int的便是游客，不在im设置之内
	private boolean haveImSet;	//当前用户是否有SiteIm的设置，是否在这个表中能根据userid找到值。若能找到，则此为true
	private boolean useOffLineEmail;	//是否开启使用离线后的邮件提醒，true：开启
	private String email;	//自动发送邮件提醒，发送到的邮件地址
	private String autoReply;	//若对方离线，进行自动回复的内容
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAutoReply() {
		return autoReply;
	}
	public void setAutoReply(String autoReply) {
		this.autoReply = autoReply;
	}
	public boolean isHaveImSet() {
		return haveImSet;
	}
	public void setHaveImSet(boolean haveImSet) {
		this.haveImSet = haveImSet;
	}
	public boolean isUseOffLineEmail() {
		return useOffLineEmail;
	}
	public void setUseOffLineEmail(boolean useOffLineEmail) {
		this.useOffLineEmail = useOffLineEmail;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	@Override
	public String toString() {
		return "Im [userid=" + userid + ", haveImSet=" + haveImSet
				+ ", useOffLineEmail=" + useOffLineEmail + ", email=" + email
				+ ", autoReply=" + autoReply + "]";
	}
	
}
