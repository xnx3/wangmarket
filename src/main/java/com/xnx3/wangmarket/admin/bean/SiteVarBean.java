package com.xnx3.wangmarket.admin.bean;

import net.sf.json.JSONObject;

/**
 * 站点全局变量，后台进行管理时，会将json格式转化为对象格式，显示给用户
 * @author 管雷鸣
 */
public class SiteVarBean {
	private String name;
	private String description;
	private String value;
	
	public SiteVarBean() {
	}
	
	/**
	 * @param name 全局变量的名字
	 * @param json 全局变量的值、描述
	 */
	public SiteVarBean(String name, JSONObject json) {
		if(name == null){
			return;
		}
		this.name = name;
		
		if(json == null){
			return;
		}
		if(json.get("value") != null){
			this.description = json.getString("description");
			this.value = json.getString("value");
		}
	}
	
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
