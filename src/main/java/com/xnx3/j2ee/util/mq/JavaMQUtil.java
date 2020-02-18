package com.xnx3.j2ee.util.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xnx3.DateUtil;

/**
 * Java单机，模拟MQ进行消息推送
 * @author 管雷鸣
 *
 */
public class JavaMQUtil {

	/**
	 * 发送信息后，会自动调取这里的
	 * key: plugin id ，如 kefu 、 formManage 、 newsSearch
	 * 
	 */
	public static Map<String, List<MQReceive>> receiveInterfaceMap = new HashMap<String, List<MQReceive>>();
	

	/**
	 * 发送信息到 domain 项目的插件中
	 * @param pluginId 插件的id，如 kefu、formManage、learnExample、newsSearch 等
	 * @param text 发送到插件的内容，一般是 json.toString()
	 */
	public static void sendMessage(String pluginId, String content){
		JavaQueueBean queue = new JavaQueueBean();
		queue.setAddtime(DateUtil.timeForUnix10());
		queue.setContent(content);
		
		if(receiveInterfaceMap.get(pluginId) != null){
			List<MQReceive> list = receiveInterfaceMap.get(pluginId);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).receive(content);
			}
		}
	}
	
	/**
	 * 添加信息接收的监听
	 * @param pluginId 插件的id，如 kefu 、 formManage 、 newsSearch
	 * @param receive 接收到信息后的逻辑实现，接收到的信息要干什么用
	 */
	public static void addReceiveListener(String pluginId, MQReceive receive){
		List<MQReceive> list = receiveInterfaceMap.get(pluginId);
		if(list == null){
			list = new ArrayList<MQReceive>();
		}
		list.add(receive);
		receiveInterfaceMap.put(pluginId, list);
	}
}
