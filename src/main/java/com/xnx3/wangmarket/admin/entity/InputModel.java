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
	private Integer siteid;		//对应site.id
	private String remark;		//备注说明
	private String text;		//输入模型的内容
	private String codeName;	//模型代码，每个网站的模型代码是唯一的
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "siteid", columnDefinition="int(11) COMMENT '对应site.id' default '0'")
	public Integer getSiteid() {
		return siteid;
	}
	
	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	@Column(name = "text", columnDefinition="mediumtext COLLATE utf8mb4_unicode_ci COMMENT '输入模型的内容'")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "remark", columnDefinition="varchar(30) COMMENT '备注说明' default ''")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "code_name", columnDefinition="char(30) COMMENT '模型代码，每个网站的模型代码是唯一的' default ''")
	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	@Override
	public String toString() {
		return "InputModel [id=" + id + ", siteid=" + siteid + ", remark=" + remark + ", text=" + text + ", codeName="
				+ codeName + "]";
	}
	
}