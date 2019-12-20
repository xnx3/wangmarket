package com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare;

import java.util.ArrayList;
import java.util.List;
import com.xnx3.wangmarket.admin.entity.InputModel;

/**
 * 模板还原，输入模型的比较
 * @author 管雷鸣
 *
 */
public class InputModelCompare {
	private InputModel currentInputModel;	//当前网站使用的栏目
	private InputModel backupsInputModel;	//备份，导入的栏目
	
	/**
	 * 是否有改动
	 * 0：没有改动，不需要还原
	 * 1：有修改，可以还原
	 * 2：当前网站已经删除，可以从备份中创建
	 */
	private int result;
	
	private List<String> updateListInfo = new ArrayList<String>();	//当前项的有过改动说明的列表

	public InputModel getCurrentInputModel() {
		return currentInputModel;
	}

	public void setCurrentInputModel(InputModel currentInputModel) {
		this.currentInputModel = currentInputModel;
	}

	public InputModel getBackupsInputModel() {
		return backupsInputModel;
	}

	public void setBackupsInputModel(InputModel backupsInputModel) {
		this.backupsInputModel = backupsInputModel;
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
