package com.xnx3.wangmarket.agencyadmin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
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

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public Integer getSiteSizeChange() {
		return siteSizeChange;
	}

	public void setSiteSizeChange(Integer siteSizeChange) {
		this.siteSizeChange = siteSizeChange;
	}

	public Integer getChangeBefore() {
		return changeBefore;
	}

	public void setChangeBefore(Integer changeBefore) {
		this.changeBefore = changeBefore;
	}

	public Integer getGoalid() {
		return goalid;
	}

	public void setGoalid(Integer goalid) {
		this.goalid = goalid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	public Integer getChangeAfter() {
		return changeAfter;
	}

	public void setChangeAfter(Integer changeAfter) {
		this.changeAfter = changeAfter;
	}

}