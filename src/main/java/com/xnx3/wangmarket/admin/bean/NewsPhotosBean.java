package com.xnx3.wangmarket.admin.bean;

import net.sf.json.JSONObject;

/**
 * news 表的 photos 图集
 * @author 管雷鸣
 *
 */
public class NewsPhotosBean {
	private String photos;			//news_data.photos 存储的数据
	private JSONObject photoJson;	//将 photos 转化为的json字符串。如果里面没有内容，这里是null
	
	public NewsPhotosBean(String photos) {
		this.photos = photos;
		//判断一下，如果里面有内容，才会进行json转换
		if(photos == null || photos.length() < 2){
			return;
		}
		
		photoJson = JSONObject.fromObject(photos);
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}
	
	/**
	 * 将 photos 转化为的json字符串。如果里面没有内容，这里是null
	 * @return
	 */
	public JSONObject getPhotoJson() {
		return photoJson;
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
	public String getPhotoJson(String key) {
		if(photoJson == null){
			return "";
		}
		if(photoJson.get(key) == null){
			return "";
		}
		return photoJson.getString(key);
	}
	
	public void setPhotoJson(JSONObject photoJson) {
		this.photoJson = photoJson;
	}
	
	
}
