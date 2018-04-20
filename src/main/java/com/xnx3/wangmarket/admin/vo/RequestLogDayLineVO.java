package com.xnx3.wangmarket.admin.vo;

import net.sf.json.JSONArray;

import com.xnx3.j2ee.vo.BaseVO;

/**
 * 访问统计－当天折线图，24小时，每小时的访问情况
 * @author 管雷鸣
 */
public class RequestLogDayLineVO extends BaseVO{
	JSONArray jsonArrayDate;
	JSONArray jsonArrayFangWen;
	JSONArray jsonArrayFangWenZuoRi;
	
	public JSONArray getJsonArrayDate() {
		return jsonArrayDate;
	}
	public void setJsonArrayDate(JSONArray jsonArrayDate) {
		this.jsonArrayDate = jsonArrayDate;
	}
	public JSONArray getJsonArrayFangWen() {
		return jsonArrayFangWen;
	}
	public void setJsonArrayFangWen(JSONArray jsonArrayFangWen) {
		this.jsonArrayFangWen = jsonArrayFangWen;
	}
	public JSONArray getJsonArrayFangWenZuoRi() {
		return jsonArrayFangWenZuoRi;
	}
	public void setJsonArrayFangWenZuoRi(JSONArray jsonArrayFangWenZuoRi) {
		this.jsonArrayFangWenZuoRi = jsonArrayFangWenZuoRi;
	}
	
}
