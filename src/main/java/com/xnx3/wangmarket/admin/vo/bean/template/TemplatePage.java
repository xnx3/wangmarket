package com.xnx3.wangmarket.admin.vo.bean.template;

/**
 * 模版导入，导入模版页面的内容，转换为json，对象
 * @author 管雷鸣
 */
public class TemplatePage {
	private com.xnx3.wangmarket.admin.entity.TemplatePage templatePage;
	private String text;	//模版页面的内容
	public com.xnx3.wangmarket.admin.entity.TemplatePage getTemplatePage() {
		return templatePage;
	}
	public void setTemplatePage(com.xnx3.wangmarket.admin.entity.TemplatePage templatePage) {
		this.templatePage = templatePage;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
