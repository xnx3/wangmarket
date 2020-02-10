package com.xnx3.wangmarket.admin.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 模版页列表
 * @author 管雷鸣
 */
public class TemplatePageListVO implements Serializable{
	private List<TemplatePageVO> list;

	public List<TemplatePageVO> getList() {
		return list;
	}

	public void setList(List<TemplatePageVO> list) {
		this.list = list;
	}
	
}
