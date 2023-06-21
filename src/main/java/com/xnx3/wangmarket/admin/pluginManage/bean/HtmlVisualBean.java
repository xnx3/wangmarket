package com.xnx3.wangmarket.admin.pluginManage.bean;

/**
 * html可视化编辑插件
 * @author 管雷鸣
 *
 */
public class HtmlVisualBean implements java.io.Serializable{
	private String id;	//插件的唯一标识，如 HtmlVisualEditor
	private String name; //插件名字，如某某可视化
	private String url; //插件js的url地址，这里可以时绝对路径，也可以是相对路径，如 /plugin/HtmlVisualEditor/plugin.js
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "HtmlVisualBean [name=" + name + ", url=" + url + "]";
	}
	
}
