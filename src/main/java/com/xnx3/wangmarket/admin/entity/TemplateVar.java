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

	private Integer id;				//自动编号
	private String templateName;	//所属的模版名字
	private String varName;			//当前模版变量的名字,模板变量代码
	private Integer userid;			//所属用户的id
	private Integer addtime;		//添加时间
	private Integer updatetime;		//最后修改时间
	private String remark;			//备注
	private Integer siteid;			//对应的站点id
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "template_name", columnDefinition = "varchar(20) COMMENT '所属的模版名字' default ''")
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	@Column(name = "var_name", columnDefinition = "char(20) COMMENT '当前模版变量的名字,模板变量代码' default ''")
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	@Column(name = "userid", columnDefinition = "int(11) COMMENT '所属用户的id' default '0'")
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	@Column(name = "addtime", columnDefinition = "int(11) COMMENT '添加时间'")
	public Integer getAddtime() {
		return addtime;
	}
	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}
	@Column(name = "updatetime", columnDefinition = "int(11) COMMENT '最后修改时间'")
	public Integer getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Integer updatetime) {
		this.updatetime = updatetime;
	}
	@Column(name = "remark", columnDefinition = "char(30) COMMENT '备注' default ''")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "siteid", columnDefinition = "int(11) COMMENT '对应的站点id' default '0'")
	public Integer getSiteid() {
		return siteid;
	}
	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}
	
	@Override
	public String toString() {
		return "TemplateVar [id=" + id + ", templateName=" + templateName + ", varName=" + varName + ", userid="
				+ userid + ", addtime=" + addtime + ", updatetime=" + updatetime + ", remark=" + remark + ", siteid="
				+ siteid + "]";
	}
	
}