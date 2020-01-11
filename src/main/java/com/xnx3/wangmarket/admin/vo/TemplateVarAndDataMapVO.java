package com.xnx3.wangmarket.admin.vo;

import java.util.Map;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 当前网站的模版变量信息。以map的方式获取
 * @author 管雷鸣
 *
 */
public class TemplateVarAndDataMapVO extends BaseVO{
	private Map<String, String> compileMap;	//可替换的，要存入缓存的map  var_name－text
	private Map<String, TemplateVarVO> templateVarMapForOriginal;	//数据库中获取到的原始信息

	public Map<String, String> getCompileMap() {
		return compileMap;
	}
	public void setCompileMap(Map<String, String> compileMap) {
		this.compileMap = compileMap;
	}
	public Map<String, TemplateVarVO> getTemplateVarMapForOriginal() {
		return templateVarMapForOriginal;
	}
	public void setTemplateVarMapForOriginal(Map<String, TemplateVarVO> templateVarMapForOriginal) {
		this.templateVarMapForOriginal = templateVarMapForOriginal;
	}
	
	
	
}
