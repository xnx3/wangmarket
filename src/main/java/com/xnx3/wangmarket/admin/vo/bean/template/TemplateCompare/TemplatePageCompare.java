package com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare;

import java.util.ArrayList;
import java.util.List;
import com.xnx3.wangmarket.admin.entity.TemplatePage;

/**
 * 模板还原，模板页面的比较
 * @author 管雷鸣
 *
 */
public class TemplatePageCompare {
	private TemplatePage currentTemplatePage;	//当前网站使用的模板页面
	private TemplatePage backupsTemplatePage;	//备份，导入的模板页面
	
	private String currentTemplatePageDataText;	//当前网站使用的模板内容
	private String backupsTemplatePageDataText;	//备份，导入的模板页面的模板内容
	
	/**
	 * 是否有改动
	 * 0：没有改动，不需要还原(默认)
	 * 1：有修改，可以还原
	 * 2：当前网站已经删除，可以从备份中创建
	 */
	private int result = 0;
	
	private List<String> updateListInfo = new ArrayList<String>();	//当前项的有过改动说明的列表

	public TemplatePage getCurrentTemplatePage() {
		return currentTemplatePage;
	}

	public void setCurrentTemplatePage(TemplatePage currentTemplatePage) {
		this.currentTemplatePage = currentTemplatePage;
	}

	public TemplatePage getBackupsTemplatePage() {
		return backupsTemplatePage;
	}

	public void setBackupsTemplatePage(TemplatePage backupsTemplatePage) {
		this.backupsTemplatePage = backupsTemplatePage;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public List<String> getUpdateListInfo() {
		return updateListInfo;
	}

	public void setUpdateListInfo(List<String> updateListInfo) {
		this.updateListInfo = updateListInfo;
	}

	public String getCurrentTemplatePageDataText() {
		return currentTemplatePageDataText;
	}

	public void setCurrentTemplatePageDataText(String currentTemplatePageDataText) {
		this.currentTemplatePageDataText = currentTemplatePageDataText;
	}

	public String getBackupsTemplatePageDataText() {
		return backupsTemplatePageDataText;
	}

	public void setBackupsTemplatePageDataText(String backupsTemplatePageDataText) {
		this.backupsTemplatePageDataText = backupsTemplatePageDataText;
	}
	
	
}
