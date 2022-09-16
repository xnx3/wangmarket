package com.xnx3.wangmarket.admin.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * News entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "exchange")
public class Exchange implements java.io.Serializable {
	
	/**
	 * 申请中，用户已提交申请，等待管理员或者客服审核
	 */
	public final static Short STATUS_APPLY_ING = 2;
	/**
	 * 审核完毕，已完成
	 */
	public final static Short STATUS_APPLY_SUCCESS_FINISH = 3;
	/**
	 * 审核完毕，已拒绝
	 */
	public final static Short STATUS_APPLY_REFUSE_FINISH = 4;
	
	// Fields
	private Integer id;
	private Integer userid;
	private Integer addtime;
	private String type;
	private Short status;
	private Integer siteid;
	private Integer goodsid;
	private String userRemark;
	private String kefuRemark;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "userid", columnDefinition="int(11) COMMENT '' default '0'")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "addtime", columnDefinition="int(11) COMMENT ''")
	public Integer getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "type", columnDefinition="varchar(255) COMMENT '' default ''")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "status", columnDefinition="smallint(6) COMMENT '' default '0'")
	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "siteid", columnDefinition="int(11) COMMENT '' default '0'")
	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	@Column(name = "user_remark", columnDefinition="varchar(255) COMMENT '' default ''")
	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	@Column(name = "kefu_remark", columnDefinition="varchar(255) COMMENT '' default ''")
	public String getKefuRemark() {
		return kefuRemark;
	}

	public void setKefuRemark(String kefuRemark) {
		this.kefuRemark = kefuRemark;
	}

	@Column(name = "goodsid", columnDefinition="int(11) COMMENT '' default '0'")
	public Integer getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(Integer goodsid) {
		this.goodsid = goodsid;
	}

	@Override
	public String toString() {
		return "Exchange [id=" + id + ", userid=" + userid + ", addtime=" + addtime + ", type=" + type + ", status="
				+ status + ", siteid=" + siteid + ", goodsid=" + goodsid + ", userRemark=" + userRemark
				+ ", kefuRemark=" + kefuRemark + "]";
	}
	
}