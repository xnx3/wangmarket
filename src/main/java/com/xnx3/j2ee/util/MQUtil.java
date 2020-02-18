package com.xnx3.j2ee.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.xnx3.Lang;
import com.xnx3.j2ee.util.ApplicationPropertiesUtil;
import com.xnx3.j2ee.util.mq.JavaMQUtil;
import com.xnx3.j2ee.util.mq.MQReceive;
import com.xnx3.j2ee.util.mq.RabbitUtil;

import net.sf.json.JSONObject;

/**
 * MQ 消息推送。
 * 消息推送有两种推送方式:
 * 如果项目分布式部署，那么 application.properties 中 spring.rabbitmq.host 等就需要配置，当配置好了这些，就可以使用rabbitmq 进行消息推送。此种消息接收处理，是异步的。其中 RabbitMQ 服务器搭建，参考 http://help.wscso.com/6924.html
 * 如果项目单机的，后续有可能会用分布式，但是前期很长一段时间都是单机的，但又想有mq功能，以便到时候方便的扩展分布式，那么 application.properties 中 spring.rabbitmq.host 等 spring.rabbitmq. 开头的，都注释掉，这种方式是使用管雷鸣自己写的Java模拟的MQ，消息接收处理，是同步的，是同一个线程执行。
 * @author 管雷鸣
 */
public class MQUtil {
	public static boolean rabbitIsUse = false;	//是否启用rabbitmq，若是ture，则是启用，弱势false，则是不启用，用java本身模拟的
	public static RabbitUtil rabbitUtil;
	static{
		rabbitUtil = new RabbitUtil();
		String host = ApplicationPropertiesUtil.getProperty("spring.rabbitmq.host");
		if(host == null){
			rabbitIsUse = false;
		}else{
			int port = Lang.stringToInt(ApplicationPropertiesUtil.getProperty("spring.rabbitmq.port"), 5672);
			String username = ApplicationPropertiesUtil.getProperty("spring.rabbitmq.username");
			String password = ApplicationPropertiesUtil.getProperty("spring.rabbitmq.password");
			
			rabbitUtil = new com.xnx3.j2ee.util.mq.RabbitUtil(host, username, password, port);
			rabbitIsUse = true;
		}
	}
	
	/**
	 * 发送信息到 MQ 服务器，让MQ服务器通知到各个相关的项目中
	 * @param pluginId 插件的id，如 kefu、formManage、learnExample、newsSearch 等，可以理解为某种通道。
	 * @param text 发送到插件的内容，一般是 JSON格式，可以使用此 net.sf.json.JSONObject.fromObject(Object....).toString() 将对象转化为字符串格式进行传输
	 */
	public static void send(String pluginId, String content){
		if(rabbitIsUse){
			//使用 rabbitMQ
			try {
				rabbitUtil.sendTopicMessage("com.xnx3.wangmarket.plugin."+pluginId, content);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}else{
			//不使用 RabbitMQ,使用Java模拟的
			JavaMQUtil.sendMessage(pluginId, content);
		}
	}

	
	/**
	 * 接收 MQ服务器 或者其他项目发送过来的信息
	 * @param pluginId 插件的id，如 kefu、formManage、learnExample、newsSearch 等
	 * @param mqReceive 接收到信息后，执行的操作。MQ服务器将消息推送到各个实现了此方法的上面，这里便是接收、处理MQ服务器发送的消息
	 */
	public static void receive(String pluginId, MQReceive mqReceive){
		if(rabbitIsUse){
			//使用 rabbitMQ
			// 当声明队列，不加任何参数，产生的将是一个临时队列，getQueue返回的是队列名称
	        String queue;
			try {
				queue = rabbitUtil.getChannel().queueDeclare().getQueue();
				ConsoleUtil.info("创建临时队列， routingKey："+"com.xnx3.wangmarket.plugin."+pluginId+" , queue: "+queue);
				rabbitUtil.getChannel().queueBind(queue, RabbitUtil.EXCHANGE_NAME, "com.xnx3.wangmarket.plugin."+pluginId);
				rabbitUtil.getChannel().basicConsume(queue, true, new DefaultConsumer(rabbitUtil.getChannel()){
					public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
						String content = new String(body, "UTF-8");
						mqReceive.receive(content);
		            }
				});
			} catch (IOException | TimeoutException e) {
				e.printStackTrace();
			}
			
		}else{
			//不使用 RabbitMQ
			JavaMQUtil.addReceiveListener(pluginId, mqReceive);
		}
	}
	
}
