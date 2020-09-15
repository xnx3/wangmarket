package com.xnx3.wangmarket.admin.bean;

import com.xnx3.wangmarket.admin.entity.SiteVar;

import net.sf.json.JSONObject;

/**
 * 站点全局变量，后台进行管理时，会将json格式转化为对象格式，显示给用户
 * @author 管雷鸣
 */
public class SiteVarBean {
	private String name;		//变量名，通过 {var.name} 调用的
	private String description;	//描述，填写的备注说明
	private String value;		//变量的值
	private String type;		//变量类型，输入类型，如 文本输入、图片输入等
	private String valueItems;	//值的可选项,多个项之间用,分割
	private String title;		//文字标题，字数2~5个字的标题
	
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
			
			if(json.get("type") == null){
				this.type = SiteVar.TYPE_TEXT;
			}else{
				this.type = json.getString("type");
			}
			if(json.get("valueItems") == null){
				this.valueItems = "";
			}else{
				this.valueItems = json.getString("valueItems");
			}
			if(json.get("title") == null){
				this.title = this.description;
			}else{
				this.title = json.getString("title");
			}
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValueItems() {
		return valueItems;
	}

	public void setValueItems(String valueItems) {
		this.valueItems = valueItems;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "SiteVarBean [name=" + name + ", description=" + description + ", value=" + value + ", type=" + type
				+ ", valueItems=" + valueItems + ", title=" + title + "]";
	}

}
