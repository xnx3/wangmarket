package com.xnx3.wangmarket.admin.bean;

/**
 * 站点全局变量，后台进行管理时，会将json格式转化为对象格式，显示给用户
 * @author 管雷鸣
 */
public class SiteVarBean {
	private String name;
	private String description;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
