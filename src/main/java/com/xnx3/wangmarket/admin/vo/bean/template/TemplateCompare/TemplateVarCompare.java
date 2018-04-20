package com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare;

import java.util.ArrayList;
import java.util.List;
import com.xnx3.wangmarket.admin.entity.TemplateVar;

/**
 * 模板还原，模板变量的比较
 * @author 管雷鸣
 *
 */
public class TemplateVarCompare {
	private TemplateVar currentTemplateVar;	//当前网站使用的模板变量
	private TemplateVar backupsTemplateVar;	//备份，导入的模板变量
	
	private String currentTemplateVarDataText;	//当前网站使用的模板变量内容
	private String backupsTemplateVarDataText;	//备份，导入的模板变量的模板内容
	
	/**
	 * 是否有改动
	 * 0：没有改动，不需要还原
	 * 1：有修改，可以还原
	 * 2：当前网站已经删除，可以从备份中创建
	 */
	private int result;
	
	private List<String> updateListInfo = new ArrayList<String>();	//当前项的有过改动说明的列表

	public TemplateVar getCurrentTemplateVar() {
		return currentTemplateVar;
	}

	public void setCurrentTemplateVar(TemplateVar currentTemplateVar) {
		this.currentTemplateVar = currentTemplateVar;
	}

	public TemplateVar getBackupsTemplateVar() {
		return backupsTemplateVar;
	}

	public void setBackupsTemplateVar(TemplateVar backupsTemplateVar) {
		this.backupsTemplateVar = backupsTemplateVar;
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

	public String getCurrentTemplateVarDataText() {
		return currentTemplateVarDataText;
	}

	public void setCurrentTemplateVarDataText(String currentTemplateVarDataText) {
		this.currentTemplateVarDataText = currentTemplateVarDataText;
	}

	public String getBackupsTemplateVarDataText() {
		return backupsTemplateVarDataText;
	}

	public void setBackupsTemplateVarDataText(String backupsTemplateVarDataText) {
		this.backupsTemplateVarDataText = backupsTemplateVarDataText;
	}

	
}
