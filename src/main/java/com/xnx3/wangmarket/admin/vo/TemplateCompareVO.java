package com.xnx3.wangmarket.admin.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare.InputModelCompare;
import com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare.SiteColumnCompare;
import com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare.TemplatePageCompare;
import com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare.TemplateVarCompare;

/**
 * 新导入的模板，与网站当前的模板的比较，是否有过修改，有过删除，是否需要还原
 * 主要用于筛查出那些有过修改，排出可还原的数据项（列表）
 * @author 管雷鸣
 */
public class TemplateCompareVO extends BaseVO{
	private TemplateVO buckupsTemplateVO;	//备份的模板的信息
	
	/**
	 	模板页面		
		List<具体可以还原哪些模板，TemplatePageCompare>
			TemplatePageCompare 每个List包含的
	 */
	private List<TemplatePageCompare> templatePageList;

	
	/**
	 	模板变量
	 */
	private List<TemplateVarCompare> templateVarList;
	
	//输入模型，同上
	private List<InputModelCompare> inputModelList;
	
	//栏目，同上
	private List<SiteColumnCompare> siteColumnList;

	public TemplateCompareVO() {
		templatePageList = new ArrayList<TemplatePageCompare>();
		templateVarList = new ArrayList<TemplateVarCompare>();
		inputModelList = new ArrayList<InputModelCompare>();
		siteColumnList = new ArrayList<SiteColumnCompare>();
	}
	
	public List<TemplatePageCompare> getTemplatePageList() {
		return templatePageList;
	}

	public void setTemplatePageList(List<TemplatePageCompare> templatePageList) {
		this.templatePageList = templatePageList;
	}

	public List<TemplateVarCompare> getTemplateVarList() {
		return templateVarList;
	}

	public void setTemplateVarList(List<TemplateVarCompare> templateVarList) {
		this.templateVarList = templateVarList;
	}

	public List<InputModelCompare> getInputModelList() {
		return inputModelList;
	}

	public void setInputModelList(List<InputModelCompare> inputModelList) {
		this.inputModelList = inputModelList;
	}

	public List<SiteColumnCompare> getSiteColumnList() {
		return siteColumnList;
	}

	public void setSiteColumnList(List<SiteColumnCompare> siteColumnList) {
		this.siteColumnList = siteColumnList;
	}

	public TemplateVO getBuckupsTemplateVO() {
		return buckupsTemplateVO;
	}

	public void setBuckupsTemplateVO(TemplateVO buckupsTemplateVO) {
		this.buckupsTemplateVO = buckupsTemplateVO;
	}
	
	
	
}
