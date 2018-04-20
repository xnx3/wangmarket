package com.xnx3.wangmarket.admin.vo;

import net.sf.json.JSONArray;

import com.xnx3.j2ee.vo.BaseVO;

/**
 * 日志服务，列出具体列表
 * @author 管雷鸣
 */
public class RequestLogItemListVO extends BaseVO {
	JSONArray list;

	public JSONArray getList() {
		return list;
	}

	public void setList(JSONArray list) {
		this.list = list;
	}
	
	
}
