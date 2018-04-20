package com.xnx3.wangmarket.admin.vo.bean.template;
/**
 * 模版导入，导入模版变量的内容，转换为json，对象
 * @author 管雷鸣
 */
public class TemplateVar {
	private com.xnx3.wangmarket.admin.entity.TemplateVar templateVar;
	private String text;
	
	public com.xnx3.wangmarket.admin.entity.TemplateVar getTemplateVar() {
		return templateVar;
	}
	public void setTemplateVar(com.xnx3.wangmarket.admin.entity.TemplateVar templateVar) {
		this.templateVar = templateVar;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

}
