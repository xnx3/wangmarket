package com.xnx3.wangmarket.domain.util;

import java.util.Map;
import com.xnx3.j2ee.util.EntityUtil;
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
public class PluginUtil extends com.xnx3.j2ee.util.PluginUtil{

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
