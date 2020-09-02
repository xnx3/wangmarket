package com.xnx3.wangmarket.superadmin.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.xnx3.StringUtil;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.wangmarket.superadmin.bean.Application;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 缓存云端插件库的插件信息
 * @author 李鑫
 */
@Component
public class YunPluginMessageCache {
	
	/**
	 *  云端插件库的全部插件列表
	 *  key：插件id  value：插件对象
	 */
	public static Map<String, Application> applicationMap= new HashMap<String, Application>();
	
	/**
	 * 插件列表的分页信息
	 */
	public static String page = "";
	
	/**
	 * 插件列表
	 */
	public static List<Application> applicationList = new LinkedList<Application>();

	/**
	 * 设置固定的间隔想云插件库服务器更新数据
	 * @author 李鑫
	 */
	public YunPluginMessageCache() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						// 刷新云插件信息
						refreshYunPluginLibrary();
						// 设置每间隔一个小时进行一次更新数据
						Thread.sleep(60 * 60 * 1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						//延迟1分钟，避免异常，沾满磁盘
						Thread.sleep(60 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
		}).start();;
	}
	
	
	/**
	 * 更新云端插件库的插件信息
	 * @author 李鑫
	 */
	@SuppressWarnings("rawtypes")
	public boolean refreshYunPluginLibrary() {
		// 创建临时保存信息的对象
		Map<String, Application> shortTimeMap = new HashMap<String, Application>();
		List<Application> shortTimeList = new LinkedList<Application>();
		String shortTimePage = "";
		HttpUtil httpUtil = new HttpUtil("UTF-8");
		// 查询云端插件库获取插件信息
		HttpResponse response = httpUtil.get(com.xnx3.wangmarket.superadmin.Global.APPLICATION_API+"?action=list");
		// 请求失败返回false
		if(response.getCode() - 200 != 0) {
			return false;
		}
		String content = StringUtil.utf8ToString(response.getContent());
		// 对请求获得信息进行封装
		JSONObject contentJsonObject = JSONObject.fromObject(content);
		if(contentJsonObject.getInt("result") == 0) {
			return false;
		}
		// 获取插件列表信息
		JSONArray applicationArray = contentJsonObject.getJSONArray("list");
		// 循环遍历插件列表转化为实体类保存到临时对象中
		Iterator iterator = applicationArray.iterator();
		while (iterator.hasNext()) {
			JSONObject app = (JSONObject) iterator.next();
			Application application = (Application) JSONObject.toBean(app, Application.class);
			shortTimeMap.put(application.getId(), application);
		}
		/*
		 *  检查临时对象是否可用，可用将信息赋予静态变量
		 */
		if(shortTimeMap.size() > 0) {
			applicationMap = shortTimeMap;
			applicationMap.forEach((key, value) -> {
				shortTimeList.add(value);
			});
			applicationList = shortTimeList;
		}
		shortTimePage = contentJsonObject.getString("page");
		if(shortTimePage != null && !shortTimePage.trim().equals("")) {
			page = shortTimePage;
		}
		return true;
	}
}
