package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 某个模版页的具体内容
 */
@Entity
@Table(name = "template_page_data")
public class TemplatePageData implements java.io.Serializable {
	private Integer id;
	private String text;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "text", columnDefinition="mediumtext COLLATE utf8mb4_unicode_ci")
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}