package com.xnx3.j2ee.util;

import com.xnx3.StringUtil;
import com.xnx3.j2ee.pluginManage.PluginManage;

/**
 * 插件相关的工具类
 * @author 管雷鸣
 *
 */
public class PluginUtil extends PluginManage{


	/**
	 * 传入插件某个类的包路径，返回插件的id
	 * @param packageStr 某个插件某个类的包路径，如 com.xnx3.wangmarket.plugin.cnzz.Plugin
	 * @return 插件的id，比如 cnzz。  返回null或者空字符串，则是未找到
	 */
	public static String getPluginId(String packageStr){
		if(packageStr == null){
			return "";
		}
		String pluginId = StringUtil.subString(packageStr, "com.xnx3.wangmarket.plugin.", ".");
		if(pluginId == null){
			return pluginId;
		}
		
		//判断一下是否含有 _domain
		int d = pluginId.indexOf("_domain");
		if(d > -1 && pluginId.length() - 7 - d == 0){
			//是包含 _domain 的，去掉最后的 _domain
			pluginId = pluginId.substring(0, pluginId.length() - 7);
		}
		return pluginId;
	}
}
