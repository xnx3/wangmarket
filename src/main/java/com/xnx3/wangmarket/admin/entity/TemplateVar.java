package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模版变量
 * @author 管雷鸣
 */
@Entity
@Table(name = "template_var")
public class TemplateVar implements java.io.Serializable {

	private Integer id;			//自动编号
	private String templateName;	//所属的模版名字
	private String varName;		//当前模版变量的名字,模板变量代码
	private Integer userid;		//所属用户的id
	private Integer addtime;		//添加时间
	private Integer updatetime;	//最后修改时间
	private String remark;		//备注
	private Integer siteid;		//对应的站点id
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getAddtime() {
		return addtime;
	}
	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}
	public Integer getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Integer updatetime) {
		this.updatetime = updatetime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getSiteid() {
		return siteid;
	}
	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}
}