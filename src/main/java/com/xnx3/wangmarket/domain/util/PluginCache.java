package com.xnx3.wangmarket.domain.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 站点插件相关，某个站点下有哪些插件使用。缓存插件信息。生命周期同tomcat
 * @author 管雷鸣
 */
public class PluginCache {
	/**
	 * map
	 * 	第一个key：integer,对应着 siteid 网站编号
	 *  第二个key：String，对应着插件的id，如 kefu ，可以根据插件id来快速取信息
	 *  第三个key：String，插件自定义的一些参数的key，比如 在线客服的 templateName 、 use
	 *  
	 *  value：具体插件自定义参数的值
	 *  <br/>注意，不建议直接操作此参数，建议使用 get、set方法操作
	 */
	public static Map<Integer, Map<String, Map<String,Object>>> pluginCacheMap = new HashMap<Integer, Map<String,Map<String,Object>>>();
	
	/**
	 * 获取插件自定义设置
	 * @param siteid 网站的id， site.id
	 * @param pluginId 插件的id，例如 kefu 
	 * @return 返回该插件自定义设置的一些key-value的参数。 这一，这里获取到的map的key，对应的是数据表的字段名，而不是实体类中驼峰命名的字段！
	 */
	public static Map<String, Object> getPluginMap(Integer siteid, String pluginId){
		if(siteid == null){
			return null;
		}
		if(pluginId == null){
			return null;
		}
		
		Map<String, Map<String,Object>> map = pluginCacheMap.get(siteid);
		if(map == null){
			return null;
		}
		
		return map.get(pluginId);
	}
	
	/**
	 * 更新系统缓存中，某个站点中，某个插件，其自定义map设定的参数。会永久缓存，生命周期为tomcat的生命周期
	 * @param siteid 站点id， site.id
	 * @param pluginId 插件id，例如 kefu 
	 * @param pluginMap 该插件自定义设置的一些key-value的参数
	 * @return 成功true，失败false
	 */
	public static boolean setPluginMap(Integer siteid, String pluginId, Map<String, Object> pluginMap){
		if(siteid == null){
			return false;
		}
		if(pluginId == null){
			return false;
		}
		
		Map<String, Map<String,Object>> map = pluginCacheMap.get(siteid);
		if(map == null){
			map = new HashMap<String, Map<String,Object>>();
			map.put(pluginId, pluginMap);
			pluginCacheMap.put(siteid, map);
		}else{
			pluginCacheMap.get(siteid).put(pluginId, pluginMap);
		}
		return true;
	}
	
	/**
	 * 更新系统缓存中，某个站点中，某个插件，其自定义的某个key-value参数。会永久缓存，生命周期为tomcat的生命周期
	 * @param siteid 站点id， site.id
	 * @param pluginId 插件id，例如 kefu 
	 * @param key 该插件自定义key-value的key
	 * @param value 该插件自定义key-value的value
	 * @return 成功true，失败false
	 */
	public static boolean setPluginValue(Integer siteid, String pluginId, String key, Object value){
		if(siteid == null){
			return false;
		}
		if(pluginId == null){
			return false;
		}
		
		Map<String, Map<String,Object>> map = pluginCacheMap.get(siteid);
		if(map == null){
			return false;
		}
		
		if(map.get(pluginId) == null){
			Map<String, Object> pluginMap = new HashMap<String, Object>();
			pluginMap.put(key, value);
			pluginCacheMap.get(siteid).put(pluginId, pluginMap);
		}else{
			pluginCacheMap.get(siteid).get(pluginId).put(key, value);
		}
		return true;
	}
	
}
