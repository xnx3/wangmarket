package com.xnx3.wangmarket.admin.vo;

import java.io.Serializable;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.TemplatePage;
import com.xnx3.wangmarket.admin.entity.TemplatePageData;

/**
 * 模版页
 * @author 管雷鸣
 */
public class TemplatePageVO extends BaseVO implements Serializable{
	private TemplatePage templatePage;
	private TemplatePageData templatePageData;
	
	public TemplatePage getTemplatePage() {
		return templatePage;
	}
	public void setTemplatePage(TemplatePage templatePage) {
		this.templatePage = templatePage;
	}
	public TemplatePageData getTemplatePageData() {
		return templatePageData;
	}
	public void setTemplatePageData(TemplatePageData templatePageData) {
		this.templatePageData = templatePageData;
	}
	
}
