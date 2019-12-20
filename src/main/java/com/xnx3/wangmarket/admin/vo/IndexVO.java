package com.xnx3.wangmarket.admin.vo;

import com.xnx3.j2ee.vo.BaseVO;

/**
 * 进行首页替换时，用到的一些传递
 * @author 管雷鸣
 */
public class IndexVO extends BaseVO {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
