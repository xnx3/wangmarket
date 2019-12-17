package com.xnx3.j2ee.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 总管理后台-权限管理-具体资源，也就是具体功能。每个类中的某个方法都是一个资源。
 */
@Entity
@Table(name = "permission")
public class Permission implements java.io.Serializable {

	private Integer id;			//自动编号
	private String description;	//描述信息，备注，只是给后台设置权限的人看的
	private String url;			//资源url，目前没用到这个字段
	private Integer parentId;	//上级资源的id，对应的也是 permission.id
	private String name;		//名字，这个资源权限的名字，显示给用户的
	private String percode;		//shiro中，与 @RequiresPermissions 所标注对应，这里也就是这个注解所标注的值
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getPercode() {
		return percode;
	}
	public void setPercode(String percode) {
		this.percode = percode;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Permission [id=" + id + ", description=" + description + ", url=" + url + ", parentId=" + parentId
				+ ", name=" + name + ", percode=" + percode + "]";
	}
}