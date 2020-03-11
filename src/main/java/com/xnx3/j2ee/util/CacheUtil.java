package com.xnx3.j2ee.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存工具。
 * 若使用Redis，在 application.properties 中配置了redis，那么这里是使用redis进行的缓存
 * 如果没有使用redis，那么这里使用的是 Hashmap 进行的缓存
 * @author 管雷鸣
 *
 */
public class CacheUtil {
	private static Map<String, Object> map;	//当不用redis时，缓存用
	public static final int EXPIRETIME = 30*24*60*60;	//30天，默认过期时间
	static{
		map = new HashMap<String, Object>();
	}
	
	/**
	 * 设置缓存
	 * @param key 设置时，多个可以用英文字符:分隔开，就如 user:guanleiming   user:lixin  。同时杜绝一个key对应的value过大的情况！一个value尽可能不要超过10KB
	 * @param value 缓存的值。坚决杜绝value过大，一个value尽可能不要超过10KB，如果太大，建议利用key进行拆分，如 key 为 user.1 存放用户编号为1的缓存信息
	 */
	public static void set(String key, Object value){
		if(RedisUtil.isUse()){
			//使用redis
			RedisUtil.setObject(key, value);
		}else{
			//使用 map
			map.put(key, value);
		}
	}
	
	/**
	 * 设置缓存
	 * @param key 设置时，多个可以用英文字符:分隔开，就如 user:guanleiming   user:lixin  。同时杜绝一个key对应的value过大的情况！一个value尽可能不要超过10KB
	 * @param value 缓存的值。坚决杜绝value过大，一个value尽可能不要超过10KB，如果太大，建议利用key进行拆分，如 key 为 user.1 存放用户编号为1的缓存信息
	 * @param expiretime 当前key-value的过期时间，单位是秒。比如设定为2，则超过2秒后没使用，会自动删除调。注意，只有使用redis时此参数才会有效
	 */
	public static void set(String key, Object value, int expiretime){
		if(RedisUtil.isUse()){
			//使用redis
			RedisUtil.setObject(key, value, expiretime);
		}else{
			//使用 map
			map.put(key, value);
		}
	}
	
	
	/**
	 * 获取缓存信息
	 * @param key 
	 * @return 如果缓存中没有，会返回 null
	 */
	public static Object get(String key){
		if(RedisUtil.isUse()){
			//使用redis
			return RedisUtil.getObject(key);
		}else{
			//使用 map
			return map.get(key);
		}
	}
	
	/**
	 * 从缓存中，删除某个key
	 * @param key 要删除的缓存的key
	 */
	public static void delete(String key){
		if(RedisUtil.isUse()){
			//使用redis
			RedisUtil.delkeyObject(key);
		}else{
			//使用 map
			if(map.get(key) != null){
				map.remove(key);
			}
		}
	}
}
