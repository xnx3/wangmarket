package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * NewsId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class NewsId implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer supportnum;
	private Integer sharenum;

	// Constructors

	/** default constructor */
	public NewsId() {
	}

	/** full constructor */
	public NewsId(Integer id, Integer supportnum, Integer sharenum) {
		this.id = id;
		this.supportnum = supportnum;
		this.sharenum = sharenum;
	}

	// Property accessors

	@Column(name = "id", nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "supportnum", nullable = false)
	public Integer getSupportnum() {
		return this.supportnum;
	}

	public void setSupportnum(Integer supportnum) {
		this.supportnum = supportnum;
	}

	@Column(name = "sharenum", nullable = false)
	public Integer getSharenum() {
		return this.sharenum;
	}

	public void setSharenum(Integer sharenum) {
		this.sharenum = sharenum;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof NewsId))
			return false;
		NewsId castOther = (NewsId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getSupportnum() == castOther.getSupportnum()) || (this
						.getSupportnum() != null
						&& castOther.getSupportnum() != null && this
						.getSupportnum().equals(castOther.getSupportnum())))
				&& ((this.getSharenum() == castOther.getSharenum()) || (this
						.getSharenum() != null
						&& castOther.getSharenum() != null && this
						.getSharenum().equals(castOther.getSharenum())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37
				* result
				+ (getSupportnum() == null ? 0 : this.getSupportnum()
						.hashCode());
		result = 37 * result
				+ (getSharenum() == null ? 0 : this.getSharenum().hashCode());
		return result;
	}

}