package com.xnx3.wangmarket.agencyadmin.entity;

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
	
	/**
	 * 此代理是否允许开通下级代理，是否有开通下级代理的功能，1允许
	 */
	public static final Short ALLOW_CREATE_SUBAGENCY_YES = 1;
	/**
	 * 此代理是否允许开通下级代理，是否有开通下级代理的功能，0不允许
	 */
	public static final Short ALLOW_CREATE_SUBAGENCY_NO = 0;
	
	private Integer id;
	private String name;
	private String phone;
	private Integer userid;
	private String qq;
	private String address;
	private Integer siteSize;		//站点数量，站点余额。1个对应着一个网站/年
	private Integer parentId;		//推荐人id，父级代理的agency.id。若父级代理是总管理，则为0
	private Integer addtime;
	private Integer expiretime;
	private Short state;		//代理状态，1或null正常；2冻结
	private Integer money;		//金额，单位是分，网市场5.0后，上线云版本按量计费
	private Short allowCreateSubAgency;	//此代理是否允许开通下级代理，是否有开通下级代理的功能。0不允许，1允许
	private Short allowSubAgencyCreateSub;	//若此代理允许开通下级代理，开通的下级代理是否允许继续开通其下级代理功能。0不允许，1允许
	
	private Integer version;

	// Constructors

	/** default constructor */
	public Agency() {
		this.money = 0;
		this.allowCreateSubAgency = 0;
		this.allowSubAgencyCreateSub = 0;
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
	
	@Column(name = "money", columnDefinition="int(11) comment '账户余额，单位是分' default '0'")
	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}
	
	@Column(name = "allow_create_sub_agency", columnDefinition="tinyint(1) comment '此代理是否允许开通下级代理，是否有开通下级代理的功能。0不允许，1允许' default '0'")
	public Short getAllowCreateSubAgency() {
		return allowCreateSubAgency;
	}

	public void setAllowCreateSubAgency(Short allowCreateSubAgency) {
		this.allowCreateSubAgency = allowCreateSubAgency;
	}

	
	@Column(name = "allow_sub_agency_create_sub", columnDefinition="tinyint(1) comment '若此代理允许开通下级代理，开通的下级代理是否允许继续开通其下级代理功能。0不允许，1允许' default '0'")
	public Short getAllowSubAgencyCreateSub() {
		return allowSubAgencyCreateSub;
	}

	public void setAllowSubAgencyCreateSub(Short allowSubAgencyCreateSub) {
		this.allowSubAgencyCreateSub = allowSubAgencyCreateSub;
	}

	@Override
	public String toString() {
		return "Agency [id=" + id + ", name=" + name + ", phone=" + phone + ", userid=" + userid + ", qq=" + qq
				+ ", address=" + address + ", siteSize=" + siteSize + ", parentId=" + parentId + ", addtime=" + addtime
				+ ", expiretime=" + expiretime + ", state=" + state + ", money=" + money + ", version=" + version + "]";
	}
	
}