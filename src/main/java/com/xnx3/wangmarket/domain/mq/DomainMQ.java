package com.xnx3.wangmarket.domain.mq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.xnx3.wangmarket.domain.bean.PluginMQ;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.util.RabbitUtil;
import com.xnx3.wangmarket.domain.util.PluginCache;
import net.sf.json.JSONObject;

/**
 * 插件间的通信，admin 、 domain 之间的通信
 * @author 管雷鸣
 *
 */
public class DomainMQ {
	public static RabbitUtil rabbitUtil;
	static{
		rabbitUtil = new RabbitUtil();
	}
	
	/**
	 * 发送信息到 domain 项目的中
	 * @param pluginId 插件的id，如 kefu、formManage、learnExample、newsSearch 等
	 * @param text 发送到插件的内容，一般是 json.toString()
	 */
	public static void send(String pluginId, String content){
		System.out.println(content);
		if(RabbitUtil.isUse){
			//使用 rabbitMQ
			RabbitUtil.sendTopicMessage("com.xnx3.wangmarket.plugin."+pluginId, content);
		}else{
			//不使用 RabbitMQ
			JavaMQUtil.sendMessage(pluginId, content);
		}
	}

	
	
	/**
	 * 接收 admin 或者其他项目发送过来的 mq 信息
	 * @param pluginId 插件的id，如 kefu、formManage、learnExample、newsSearch 等
	 * @param receiveDomainMQ 接收到信息后，执行的操作。一个pluginid只能绑定一个 receiveDomainMQ
	 */
	public static void receive(String pluginId, ReceiveDomainMQ receiveDomainMQ){
		if(RabbitUtil.isUse){
			//使用 rabbitMQ
			try {
				RabbitUtil.receive("com.xnx3.wangmarket.plugin."+pluginId, new DefaultConsumer(rabbitUtil.rabbitUtil.getChannel()){
					public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
						String content = new String(body, "UTF-8");
						try {
							receiveDomainMQ.receive(content);
						} catch (Exception e) {
							e.printStackTrace();
						}
		            }
				});
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}else{
			//不使用 RabbitMQ
			JavaMQUtil.addReceiveListener(pluginId, new JavaMQReceive() {
				public void receive(String content) {
					receiveDomainMQ.receive(content);
				}
			});
		}
		
	}
	
	
	/**
	 * 接收 admin 或者其他项目发送过来的 mq 信息
	 * @param pluginId 插件的id，如 kefu、formManage、learnExample、newsSearch 等
	 * @param receiveDomainMQ 接收到信息后，执行的操作。一个pluginid只能绑定一个 receiveDomainMQ
	 */
	public static void receive(String pluginId){
		if(RabbitUtil.isUse){
			//使用 rabbitMQ
			try {
				RabbitUtil.receive("com.xnx3.wangmarket.plugin."+pluginId, new DefaultConsumer(rabbitUtil.rabbitUtil.getChannel()){
					public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
						String content = new String(body, "UTF-8");
						receiveContentDispose(pluginId, content);
		            }
				});
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}else{
			//不使用 RabbitMQ
			JavaMQUtil.addReceiveListener(pluginId, new JavaMQReceive() {
				public void receive(String content) {
					receiveContentDispose(pluginId, content);
				}
			});
		}
		
	}
	
	/**
	 * 接收 admin 或者其他项目发送过来的 mq 信息后，在这里进行处理
	 * @param pluginId 插件的id，如 kefu、formManage、learnExample、newsSearch 等
	 * @param content MQ接收到的消息内容，json格式。除了siteid、domain、bindDomain 这三个外，其他的json数据都会同步到插件map中进行长久缓存。也就是这个消息内容中，没用的数据不要传递过来
	 */
	protected static void receiveContentDispose(String pluginId, String content){
		PluginMQ mq = new PluginMQ(content);
		/**  更新插件信息  **/
		Map<String,Object> map = PluginCache.getPluginMap(mq.getSiteid(), pluginId);
		if(map == null){
			map = new HashMap<String,Object>();
		}
		
		JSONObject json = mq.getMqContentJson();
        Iterator<String> iterator = json.keys();
        while(iterator.hasNext()){
        	String key = (String) iterator.next();
        	
        	//排除不是 siteid、domain、bindDomain 这三个
        	if(!key.equals("siteid") && !key.equals("domain") && !key.equals("bindDomain")){
        		String value = null;
	        	if(json.get(key) == null){
	        		value = "";
	        	}else{
	        		value = json.get(key).toString();
	        	}
	        	map.put(key, value);
        	}
        }

		PluginCache.setPluginMap(mq.getSiteid(), pluginId, map);
	}
}
