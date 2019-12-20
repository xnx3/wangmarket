package com.xnx3.wangmarket.admin.vo;

import java.util.List;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.TemplateVar;

/**
 * 模版变量列表，不包含模版变量内容
 * @author 管雷鸣
 */
public class TemplateVarListVO extends BaseVO {
//	private List<TemplateVar> list;
//
//	public List<TemplateVar> getList() {
//		return list;
//	}
//
//	public void setList(List<TemplateVar> list) {
//		this.list = list;
//	}
	
	private List<TemplateVarVO> list;

	public List<TemplateVarVO> getList() {
		return list;
	}

	public void setList(List<TemplateVarVO> list) {
		this.list = list;
	}
	
}
