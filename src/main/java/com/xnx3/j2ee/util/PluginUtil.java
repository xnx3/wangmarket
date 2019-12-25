package com.xnx3.j2ee.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.pluginManage.PluginManage;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.util.RabbitUtil;
import com.xnx3.wangmarket.domain.bean.PluginMQ;
import com.xnx3.wangmarket.domain.mq.JavaMQUtil;
import net.sf.json.JSONObject;

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
	

	/**
	 * 发送信息到 domain 项目的中
	 * @param pluginId 插件的id，如 kefu、formManage、learnExample、newsSearch 等
	 * @param site 当前站点信息 {@link Site}
	 * @param entityObj 实体类，要传递更新的实体类的信息。这里传递的实体类的key，是数据表中列的列名，而不是驼峰命名的。
	 */
	public static void sendMQMessage(String pluginId, Site site, Object entityObj){
		Map<String, Object> map = EntityUtil.entityToMap(entityObj);
		String content = new PluginMQ(site).jsonAppend(JSONObject.fromObject(map)).toString();
		
		if(RabbitUtil.isUse){
			//使用 rabbitMQ
			RabbitUtil.sendTopicMessage("com.xnx3.wangmarket.plugin."+pluginId, content);
		}else{
			//不使用 RabbitMQ
			JavaMQUtil.sendMessage(pluginId, content);
		}
	}
}
