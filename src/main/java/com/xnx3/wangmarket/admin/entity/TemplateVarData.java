package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模版变量的具体变量内容
 */
@Entity
@Table(name = "template_var_data")
public class TemplateVarData implements java.io.Serializable {

	private Integer id;		//模版变量的id，对应 templateVar.id
	private String text;	//模版变量的内容
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "text", columnDefinition="mediumtext COLLATE utf8mb4_unicode_ci COMMENT '模版变量的内容'")
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}