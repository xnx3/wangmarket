package com.xnx3.wangmarket.admin.vo;

import java.util.List;
import java.util.Map;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Template;

/**
 * 模版页面，模版导入，将导入的字符串转化为json，然后将json转化为此对象
 * @author 管雷鸣
 */
public class TemplateListVO extends BaseVO {
	private List<Template> list;	//返回的模版列表

	public List<Template> getList() {
		return list;
	}

	public void setList(List<Template> list) {
		this.list = list;
	}
	
}
