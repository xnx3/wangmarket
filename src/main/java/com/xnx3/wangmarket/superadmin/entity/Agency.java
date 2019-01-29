package com.xnx3.wangmarket.superadmin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Version;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 代理 entity. 
 * @author 管雷鸣
 */
@Entity
@Table(name = "agency")
public class Agency implements java.io.Serializable {
	
	/**
	 * 代理状态，正常
	 */
	public static final Short STATE_NORMAL = 1;
	/**
	 * 代理状态，冻结，暂停
	 */
	public static final Short STATE_FREEZE = 2;
	
	private Integer id;
	private String name;
	private String phone;
	private Integer userid;
	private Integer regOssHave;
	private Integer ossPrice;
	private String qq;
	private String address;
	private Integer siteSize;
	private Integer parentId;
	private Integer addtime;
	private Integer expiretime;
	private Short state;	//代理状态，1或null正常；2冻结
	
	private Integer version;

	// Constructors

	/** default constructor */
	public Agency() {
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

	public String getName() {
		if(name != null){
			return name.trim();
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		if(phone != null){
			return phone.trim();
		}
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	@Column(name = "reg_oss_have")
	public Integer getRegOssHave() {
		return regOssHave;
	}

	public void setRegOssHave(Integer regOssHave) {
		this.regOssHave = regOssHave;
	}
	@Column(name = "oss_price")
	public Integer getOssPrice() {
		return ossPrice;
	}

	public void setOssPrice(Integer ossPrice) {
		this.ossPrice = ossPrice;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getSiteSize() {
		return siteSize;
	}

	public void setSiteSize(Integer siteSize) {
		this.siteSize = siteSize;
	}

	@Version
	public Integer getVersion() {
	    return this.version;
	}
	public void setVersion(Integer version) {
	    this.version = version;
	}
	
	@Override
	public String toString() {
		return "Agency [id=" + id + ", name=" + name + ", phone=" + phone
				+ ", userid=" + userid + ", regOssHave=" + regOssHave
				+ ", ossPrice=" + ossPrice + ", qq=" + qq + ", address="
				+ address + ", siteSize=" + siteSize + "]";
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	public Integer getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(Integer expiretime) {
		this.expiretime = expiretime;
	}

	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}

}