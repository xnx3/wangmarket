package com.xnx3.wangmarket.agencyadmin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 代理 agency 的变长字段
 */
@Entity
@Table(name = "agency_data")
public class AgencyData implements java.io.Serializable {

	private Integer id;	//对应agency.id
	private String notice;	//公告	

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
	
}