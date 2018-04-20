package com.xnx3.wangmarket.admin.vo;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.News;

/**
 * CMS模式的输入模型相关
 * @author 管雷鸣
 */
public class InputModelVO extends BaseVO {
	private InputModel inputModel;

	public InputModel getInputModel() {
		return inputModel;
	}

	public void setInputModel(InputModel inputModel) {
		this.inputModel = inputModel;
	}
	
}
