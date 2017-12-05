package com.xnx3.im.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 网站的im在线客服设置
 */
@Entity
@Table(name = "im")
public class Im implements java.io.Serializable {
	private Integer id;		//自动编号
	private Integer userid;	//所属用户，属于哪个用户，对应 User.id
	private String autoReply; //无客服在线时，自动回复的文字。若为空，或者空字符串，则不开启此功能
	private Short useOffLineEmail;	//是否使用离线时的邮件通知提醒，默认为0，不启用。  1为启用
	private String email;		//邮件内容，若上面开启邮件提醒通知了，那么这里是必须要填写的，不然是要通知谁
	private Short useKefu;	//是否开启在线客服功能。0不开启，默认；  1开启。原先在site中的，现在转移到此处，不仅仅只是网站有客服功能
	
	/**
	 * 是否启用，true：1  启用
	 */
	public static final Short USE_TRUE = 1;
	/**
	 * 是否启用，false：0  不启用
	 */
	public static final Short USE_FALSE = 0;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAutoReply() {
		return autoReply;
	}

	public void setAutoReply(String autoReply) {
		this.autoReply = autoReply;
	}

	public Short getUseOffLineEmail() {
		return useOffLineEmail;
	}

	public void setUseOffLineEmail(Short useOffLineEmail) {
		this.useOffLineEmail = useOffLineEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Short getUseKefu() {
		return useKefu;
	}

	public void setUseKefu(Short useKefu) {
		this.useKefu = useKefu;
	}


}