package com.xnx3.admin.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 云端模版列表
 * @author 管雷鸣
 */
public class CloudTemplateListVO extends BaseVO {
	List<Map<String, String>> list;
	Map<String, String> mapNameInfo;	//key：name模板名字    value：info模板说明

	public CloudTemplateListVO() {
		mapNameInfo = new HashMap<String, String>();
	}
	
	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	public Map<String, String> getMapNameInfo() {
		return mapNameInfo;
	}

	public void setMapNameInfo(Map<String, String> mapNameInfo) {
		this.mapNameInfo = mapNameInfo;
	}
	
	
	
}
