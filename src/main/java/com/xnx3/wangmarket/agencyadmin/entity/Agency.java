package com.xnx3.wangmarket.agencyadmin.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

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
	private String name;					//代理商的公司名字（或个人姓名）
	private String phone;					//手机号
	private Integer userid;					//当前代理是属于哪个用户
	private String qq;						//联系QQ
	private String address;					//办公地址
	private Integer siteSize;				//站点数量，站点余额。1个对应着一个网站/年
	private Integer parentId;				//推荐人id，父级代理的agency.id。若父级代理是总管理，则为0
	private Integer addtime;				//添加，开通时间，10位时间戳
	private Integer expiretime;				//代理资格的过期时间，10位时间戳
	private Short state;					//代理状态，1或null正常；2冻结
	private Integer money;					//金额，单位是分，网市场5.0后，上线云版本按量计费
	private Short allowCreateSubAgency;		//此代理是否允许开通下级代理，是否有开通下级代理的功能。0不允许，1允许
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

	@Column(name = "name", columnDefinition = "char(38) COMMENT '代理商的公司名字（或个人姓名）' default ''")
	public String getName() {
		if(name != null){
			return name.trim();
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "phone", columnDefinition = "char(20) COMMENT '手机号' default ''")
	public String getPhone() {
		if(phone != null){
			return phone.trim();
		}
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "userid", columnDefinition = "int(11) COMMENT '当前代理是属于哪个用户' default '0'")
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "qq", columnDefinition = "char(13) COMMENT '联系QQ' default ''")
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "address", columnDefinition = "char(80) COMMENT '办公地址' default ''")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "site_size", columnDefinition = "int(11) COMMENT '站点数量，站点余额。1个对应着一个网站/年' default '0'")
	public Integer getSiteSize() {
		return siteSize;
	}

	public void setSiteSize(Integer siteSize) {
		this.siteSize = siteSize;
	}

	@Version
	@Column(name = "version", columnDefinition = "int(11) COMMENT '' default '0'")
	public Integer getVersion() {
		return this.version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Column(name = "parent_id", columnDefinition = "int(11) COMMENT '推荐人id，父级代理的agency.id。若父级代理是总管理，则为0' default '0'")
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Column(name = "addtime", columnDefinition = "int(11) COMMENT '添加，开通时间，10位时间戳'")
	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "expiretime", columnDefinition = "int(11) COMMENT '代理资格的过期时间，10位时间戳'")
	public Integer getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(Integer expiretime) {
		this.expiretime = expiretime;
	}

	@Column(name = "state", columnDefinition = "tinyint(2) COMMENT '代理状态，1或null正常；2冻结' default '0'")
	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}
	
	@Column(name = "money", columnDefinition = "int(11) COMMENT '金额，单位是分，网市场5.0后，上线云版本按量计费' default '0'")
	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}
	
	@Column(name = "allow_create_sub_agency", columnDefinition = "tinyint(1) COMMENT '此代理是否允许开通下级代理，是否有开通下级代理的功能。0不允许，1允许' default '0'")
	public Short getAllowCreateSubAgency() {
		return allowCreateSubAgency;
	}

	public void setAllowCreateSubAgency(Short allowCreateSubAgency) {
		this.allowCreateSubAgency = allowCreateSubAgency;
	}

	
	@Column(name = "allow_sub_agency_create_sub", columnDefinition = "tinyint(1) COMMENT '若此代理允许开通下级代理，开通的下级代理是否允许继续开通其下级代理功能。0不允许，1允许' default '0'")
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