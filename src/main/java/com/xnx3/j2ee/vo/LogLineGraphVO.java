package com.xnx3.j2ee.vo;

import net.sf.json.JSONArray;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 折线图的数据返回，主要是总管理后台的日志管理中，统计图表的数据
 * @author 管雷鸣
 */
public class LogLineGraphVO extends BaseVO{
	JSONArray nameArray;
	JSONArray dataArray;
	JSONArray dataArray2;
	
	public JSONArray getNameArray() {
		return nameArray;
	}
	public void setNameArray(JSONArray nameArray) {
		this.nameArray = nameArray;
	}
	public JSONArray getDataArray() {
		return dataArray;
	}
	public void setDataArray(JSONArray dataArray) {
		this.dataArray = dataArray;
	}
	public JSONArray getDataArray2() {
		return dataArray2;
	}
	public void setDataArray2(JSONArray dataArray2) {
		this.dataArray2 = dataArray2;
	}
	
	
}
