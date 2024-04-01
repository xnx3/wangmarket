package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * NewsData entity. 
 * @author 管雷鸣
 */
@Entity
@Table(name = "news_data")
public class NewsData implements java.io.Serializable {
	
	private Integer id;		//对应news.id
	private String text;	//信息内容
	private String extend;	//其他扩展字段，以json形式存在
	


	/** default constructor */
	public NewsData() {
	}

	/** minimal constructor */
	public NewsData(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public NewsData(Integer id, String text) {
		this.id = id;
		this.text = text;
	}

	// Property accessors
	@Id
	@Column(name = "id",columnDefinition="int(11) COMMENT '站点id，对应 site.id'", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "text", columnDefinition="mediumtext COLLATE utf8mb4_unicode_ci COMMENT '信息内容' default ''")
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "extend", columnDefinition="text COMMENT '其他扩展字段，以json形式存在' default ''")
	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	@Override
	public String toString() {
		return "NewsData [id=" + id + ", text=" + text + ", extend=" + extend + "]";
	}

}