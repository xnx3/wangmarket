package com.xnx3.wangmarket.agencyadmin.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 站币变化记录表
 * @author 管雷鸣
 */
@Entity
@Table(name = "site_size_change")
public class SiteSizeChange implements java.io.Serializable {

	private Integer id;
	private Integer agencyId;
	private Integer siteSizeChange;
	private Integer changeBefore;
	private Integer changeAfter;
	private Integer goalid;
	private Integer userid;
	private Integer addtime;


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "agency_id", columnDefinition = "int(11) COMMENT '' default '0'")
	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	@Column(name = "site_size_change", columnDefinition = "int(11) COMMENT '' default '0'")
	public Integer getSiteSizeChange() {
		return siteSizeChange;
	}

	public void setSiteSizeChange(Integer siteSizeChange) {
		this.siteSizeChange = siteSizeChange;
	}

	@Column(name = "change_before", columnDefinition = "int(11) COMMENT '' default '0'")
	public Integer getChangeBefore() {
		return changeBefore;
	}

	public void setChangeBefore(Integer changeBefore) {
		this.changeBefore = changeBefore;
	}

	@Column(name = "goalid", columnDefinition = "int(11) COMMENT '' default '0'")
	public Integer getGoalid() {
		return goalid;
	}

	public void setGoalid(Integer goalid) {
		this.goalid = goalid;
	}

	@Column(name = "userid", columnDefinition = "int(11) COMMENT '' default '0'")
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "addtime", columnDefinition = "int(11) COMMENT ''")
	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "change_after", columnDefinition = "int(11) COMMENT '' default '0'")
	public Integer getChangeAfter() {
		return changeAfter;
	}

	public void setChangeAfter(Integer changeAfter) {
		this.changeAfter = changeAfter;
	}

	@Override
	public String toString() {
		return "SiteSizeChange [id=" + id + ", agencyId=" + agencyId + ", siteSizeChange=" + siteSizeChange
				+ ", changeBefore=" + changeBefore + ", changeAfter=" + changeAfter + ", goalid=" + goalid + ", userid="
				+ userid + ", addtime=" + addtime + "]";
	}

}