package com.xnx3.wangmarket.admin.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 输入模型，用户添加新闻、产品等
 */
@Entity
@Table(name = "input_model")
public class InputModel implements java.io.Serializable {
	private Integer id;
	private Integer siteid;
	private String remark;		//备注
	private String text;
	private String codeName;	//模型代码
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public Integer getSiteid() {
		return siteid;
	}
	
	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	@Column(name = "text", columnDefinition="mediumtext COLLATE utf8mb4_unicode_ci")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
}