package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * site entitiy
 * @author 管雷鸣
 */
@Entity
@Table(name = "site_data")
public class SiteData implements java.io.Serializable {
	private Integer id;
	private String indexDescription;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		if(this.id == null){
			return 0;
		}
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "index_description")
	public String getIndexDescription() {
		return indexDescription;
	}

	public void setIndexDescription(String indexDescription) {
		this.indexDescription = indexDescription;
	}


}