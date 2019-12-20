package com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare;

import java.util.ArrayList;
import java.util.List;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 模板还原，模板变量的比较
 * @author 管雷鸣
 *
 */
public class SiteColumnCompare {
	private SiteColumn currentSiteColumn;	//当前网站使用的栏目
	private SiteColumn backupsSiteColumn;	//备份，导入的栏目
	
	/**
	 * 是否有改动
	 * 0：没有改动，不需要还原
	 * 1：有修改，可以还原
	 * 2：当前网站已经删除，可以从备份中创建
	 */
	private int result;
	
	private List<String> updateListInfo = new ArrayList<String>();	//当前项的有过改动说明的列表

	public SiteColumn getCurrentSiteColumn() {
		return currentSiteColumn;
	}

	public void setCurrentSiteColumn(SiteColumn currentSiteColumn) {
		this.currentSiteColumn = currentSiteColumn;
	}

	public SiteColumn getBackupsSiteColumn() {
		return backupsSiteColumn;
	}

	public void setBackupsSiteColumn(SiteColumn backupsSiteColumn) {
		this.backupsSiteColumn = backupsSiteColumn;
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

}
