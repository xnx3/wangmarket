package com.xnx3.wangmarket.admin.bean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * news 表的 extend 其他字段扩展-只能用于详情页面中使用
 * @author 管雷鸣
 *
 */
public class NewsExtendBean {
	private JSONObject extendJson;	//将 extend 转化为的json字符串。如果里面没有内容，这里是null
	
	public NewsExtendBean(String extend) {
		//判断一下，如果里面有内容，才会进行json转换
		if(extend == null || extend.length() < 2){
			return;
		}
		
		extendJson = JSONObject.fromObject(extend);
	}
	
	
	/**
	 * 通过 key 来获取 value 数据
	 * @param key ，如 title、titlepic 
	 * @return <ul>
	 * 				<li>如果photojson 为null，则返回 "" 空字符串</li>
	 * 				<li>如果取的key在photojson中不存在，则返回 "" 空字符串</li>
	 * 				<li>如果取的key在photojson中存在，则返回具体的value字符串</li>
	 * 			</ul>
	 */
	public String getExtendJson(String key) {
		if(extendJson == null){
			return "";
		}
		if(extendJson.get(key) == null){
			return "";
		}
		
		JSONArray jsonArray = extendJson.getJSONArray(key);
		if(jsonArray.size() == 1){
			//如果里面只有一个值，那么就将具体值返回
			return jsonArray.getString(0);
		}else{
			//如果里面有多个值，那么返回的将是数组
			return jsonArray.toString();
		}
	}
	
	
}
