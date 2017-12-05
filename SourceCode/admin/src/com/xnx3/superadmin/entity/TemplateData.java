package com.xnx3.superadmin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模版使用说明等非固定长度的数据
 * @author 管雷鸣
 */
@Entity
@Table(name = "template_data")
public class TemplateData implements java.io.Serializable {

	private Integer id;			//对应Template.id
	private String name;		//说明
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}