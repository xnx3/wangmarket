package com.xnx3.wangmarket.admin.vo;

import java.io.Serializable;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.TemplateVar;
import com.xnx3.wangmarket.admin.entity.TemplateVarData;

/**
 * 模版变量
 * @author 管雷鸣
 */
public class TemplateVarVO extends BaseVO implements Serializable{
	private TemplateVar templateVar;
	private TemplateVarData templateVarData;

	public TemplateVar getTemplateVar() {
		return templateVar;
	}

	public void setTemplateVar(TemplateVar templateVar) {
		this.templateVar = templateVar;
	}

	public TemplateVarData getTemplateVarData() {
		return templateVarData;
	}

	public void setTemplateVarData(TemplateVarData templateVarData) {
		this.templateVarData = templateVarData;
	}
	
}
