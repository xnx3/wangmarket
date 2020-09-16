package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 网站全局变量。每个网站都会有自己的全局变量设置。真正使用时，会先有Java读入缓存，使用缓存中数据
 * @author 管雷鸣
 *
 */
@Entity()
@Table(name = "site_var")
public class SiteVar {
	public static final String TYPE_TEXT = "text";	//文本输入
	public static final String TYPE_IMAGE = "image";	//单个图片上传输入
	public static final String TYPE_IMAGE_GROUP = "imagegroup";	//多个图片上传输入
	public static final String TYPE_SELECT = "select";	//select 下拉选择
	public static final String TYPE_NUMBER = "number";	//整数型输入
	
	private Integer id;		//站点id，对应 Site.id
	private String text;	//站点全局变量的具体变量具体数据，json对象格式,如 {"姓名":"管雷鸣","个人网站":"www.xnx3.com"}
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "text", columnDefinition="mediumtext COLLATE utf8mb4_unicode_ci COMMENT '当前模版页面的模版内容'")
	public String getText() {
		if(text == null){
			return "{}";
		}
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "SiteVar [id=" + id + ", text=" + text + "]";
	}
}
