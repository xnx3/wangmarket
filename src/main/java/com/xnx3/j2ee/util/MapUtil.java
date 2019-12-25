package com.xnx3.j2ee.util;

import java.util.HashMap;
import java.util.Map;

import com.xnx3.Lang;

/**
 * map工具类
 * @author 管雷鸣
 */
public class MapUtil {
	Map<String, Object> map;
	public MapUtil(Map<String, Object> map) {
		this.map = map;
	}
	
	/**
	 * 取map中的值，如果值不存在，返回空字符串 ""
	 * @param key 要取的值的key
	 * @return 值
	 */
	public String getString(String key){
		return getString(key, "");
	}
	
	/**
	 * 取map中的值，如果值不存在，则返回 defaultValue 的值
	 * @param key 要取的值的key
	 * @param defaultValue 如果 key 不存在，那么返回这里的值
	 * @return 值
	 */
	public String getString(String key, String defaultValue){
		if(map.get(key) == null){
			return defaultValue;
		}
		return map.get(key).toString();
	}

	/**
	 * 取map中的值，如果值不存在，返回 0
	 * @param key 要取的值的key
	 * @return 值
	 */
	public int getInt(String key){
		return getInt(key, 0);
	}
	
	/**
	 * 取map中的值，如果值不存在，或者值不是int型，返回 defaultValue
	 * @param key 要取的值的key
	 * @param defaultValue 如果值不存在或者不是int型，返回这里的值
	 * @return 值
	 */
	public int getInt(String key, int defaultValue){
		if(map.get(key) == null){
			return defaultValue;
		}
		
		try {
			return Lang.stringToInt(map.get(key).toString(), defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 获取原始 map
	 */
	public Map<String, Object> getMap(){
		return this.map;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("");
		for(Map.Entry<String, Object> entry : this.map.entrySet()){
			sb.append(entry.getKey()+"="+entry.getValue()+";");
		}
		return sb.toString();
	}
	
	
}
