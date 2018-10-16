package com.xnx3.wangmarket.plugin.formManage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 自定义表单 form 的分表
 * @author 管雷鸣
 */
@Entity
@Table(name = "form_data")
public class FormData implements java.io.Serializable {

	private Integer id;			//对应form.id
	private String text;		//表单提交的具体数据，json格式,如 {"姓名":"管雷鸣","个人网站":"www.xnx3.com"}


	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "FormData [id=" + id + ", text=" + text + "]";
	}

}