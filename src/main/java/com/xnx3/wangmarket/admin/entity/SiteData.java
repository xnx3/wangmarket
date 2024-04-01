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
	@Column(name = "id", columnDefinition="int(11) COMMENT '站点编号，对应 site.id'", unique = true, nullable = false)
	public Integer getId() {
		if(this.id == null){
			return 0;
		}
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "index_description", columnDefinition = "varchar(400) COMMENT '首页的描述' default ''")
	public String getIndexDescription() {
		return indexDescription;
	}

	public void setIndexDescription(String indexDescription) {
		this.indexDescription = indexDescription;
	}


}