package com.xnx3.wangmarket.admin.bean;

import com.xnx3.wangmarket.admin.entity.NewsData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * NewsData 对象，在生成网站是，先将其转化为json对象。这里便是
 * @author 管雷鸣
 *
 */
public class NewsDataBean {
	public int id;	//对应news.id
	public String text;	//对应news_data.text
	public JSONObject extendJson;	//对应news_data.extend，只不过不同的是，这里是将 extend转化为了json对象，需要什么数据直接从这里取
	public JSONArray photoJsonArray;	//将 photos 转化为的json字符串。如果里面没有内容，这里是null
	
	public NewsDataBean(NewsData newsData) {
		if(newsData == null){
			return;
		}
		
		//如果这个newsData只是new出来，还没赋予值，那么需要执行另一种初始化
		if(newsData.getId() == null){
			this.id = 0;
			this.text = "";
		}else{
			this.id = newsData.getId();
			this.text = newsData.getText();
		}
		
		
		//判断一下，如果extend里面有内容，才会进行json转换
		if(newsData.getExtend() != null && newsData.getExtend().length() > 2){
			this.extendJson = JSONObject.fromObject(newsData.getExtend());
		}
	}

	public int getId() {
		return id;
	}

	public String getText() {
		if(text == null){
			return "";
		}
		return text;
	}

	public JSONObject getExtendJson() {
		return extendJson;
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
	
	
	public JSONArray getPhotoJsonArray() {
		return photoJsonArray;
	}
	
	
}
