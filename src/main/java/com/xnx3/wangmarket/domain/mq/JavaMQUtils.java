package com.xnx3.wangmarket.domain.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.util.ConsoleUtil;

/**
 * Java map 模拟的 MQ 功能
 * 因为这个只是给 domain 项目，集成部署，只是部署到单台服务器使用，所以消费端只有一个（此不支持多个消费端）
 */
public class JavaMQUtils {
	
	/**
	 * 发送信息后，会自动调取这里的
	 * key: plugin id ，如 kefu 、 formManage 、 newsSearch
	 * 
	 */
	public static Map<String, JavaMQReceive> receiveInterfaceMap = new HashMap<String, JavaMQReceive>();
	
	/**
	 * 缓存的 mq 队列
	 * key: plugin id ，如 kefu 、 formManage 、 newsSearch
	 * value：JavaQueueBean 的 list 列表，即这个插件的信息列表
	 * 根据插件的id来获取具体其内的消息
	 */
	public static Map<String, List<JavaQueueBean>> cacheMap = new HashMap<String, List<JavaQueueBean>>(); 
	
	/**
	 * queue 信息的过期时间，单位为秒
	 */
	public final static int EXPIRE_TIME = 1;	
	
	static{
		System.out.println("static");
		new Thread(new Runnable() {
			public void run() {
				System.out.println("start domain update thread -- javamq");
				ConsoleUtil.info("start domain update thread -- javamq");
				
				while(true){
					try {
						Thread.sleep(EXPIRE_TIME*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					int currentTime = DateUtil.timeForUnix10();	//当前时间
					for (Map.Entry<String, List<JavaQueueBean>> entry : cacheMap.entrySet()) {
						List<JavaQueueBean> list = entry.getValue();
						if(list != null && list.size() > 0){
							//取最上面的一个，也就是最早加入的，因为即使过期，肯定是最早加入的开始过期。一直从最早加入的开始遍历，直到遍历到不过期的信息为止
							JavaQueueBean queue = list.get(0);
							while(queue != null && currentTime - queue.getAddtime() > EXPIRE_TIME){
								//已过期，那么就将其删除掉
								list.remove(0);
								//将指针指向删除后的第一个，也就是 queue
								if(list.size() > 0){
									queue = list.get(0);
								}else{
									queue = null;
								}
							}
							//遍历完之后，重新将其赋予 cacheMap
							cacheMap.put(entry.getKey(), list);
						}
					}
				}
			}
		}).start();
	}
	
	
	/**
	 * 发送信息到 domain 项目的插件中
	 * @param pluginId 插件的id，如 kefu、formManage、learnExample、newsSearch 等
	 * @param text 发送到插件的内容，一般是 json.toString()
	 */
	public static void sendMessage(String pluginId, String content){
		if(cacheMap.get(pluginId) == null){
			cacheMap.put(pluginId, new ArrayList<JavaQueueBean>());
		}
		
		JavaQueueBean queue = new JavaQueueBean();
		queue.setAddtime(DateUtil.timeForUnix10());
		queue.setContent(content);
		cacheMap.get(pluginId).add(queue);
		
		if(receiveInterfaceMap.get(pluginId) != null){
			receiveInterfaceMap.get(pluginId).receive(content);
		}
		
	}
	
	/**
	 * 添加信息接收的监听
	 * @param pluginId 插件的id，如 kefu 、 formManage 、 newsSearch
	 * @param receive 接收到信息后的逻辑实现，接收到的信息要干什么用
	 */
	public static void addReceiveListener(String pluginId, JavaMQReceive receive){
		receiveInterfaceMap.put(pluginId, receive);
	}
}
