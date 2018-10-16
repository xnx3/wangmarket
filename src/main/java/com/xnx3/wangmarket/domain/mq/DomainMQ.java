package com.xnx3.wangmarket.domain.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.xnx3.wangmarket.admin.util.RabbitUtil;

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
				RabbitUtil.receive("com.xnx3.wangmarket.plugin."+pluginId, new DefaultConsumer(rabbitUtil.rabbitMQTopicUtil.getChannel()){
					public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
						String content = new String(body, "UTF-8");
						receiveDomainMQ.receive(content);
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
	
}
