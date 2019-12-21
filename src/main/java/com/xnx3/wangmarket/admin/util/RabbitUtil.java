package com.xnx3.wangmarket.admin.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Consumer;
import com.xnx3.Lang;
import com.xnx3.j2ee.func.ApplicationProperties;
import com.xnx3.j2ee.func.RabbitMQTopicUtil;
import com.xnx3.j2ee.util.ConsoleUtil;

/**
 * RabbitMQ 工具类
 * @author 管雷鸣
 *
 */
@Component
public class RabbitUtil{
	public static boolean isUse = false;	//是否启用rabbitmq，若是ture，则是启用
	public static com.xnx3.j2ee.util.RabbitUtil rabbitUtil = null;
	
	public RabbitUtil() {
		String host = ApplicationProperties.getProperty("spring.rabbitmq.host");
		if(host == null){
			isUse = false;
			return;
		}
		int port = Lang.stringToInt(ApplicationProperties.getProperty("spring.rabbitmq.port"), 5672);
		String username = ApplicationProperties.getProperty("spring.rabbitmq.username");
		String password = ApplicationProperties.getProperty("spring.rabbitmq.password");
		
		rabbitUtil = new com.xnx3.j2ee.util.RabbitUtil(host, username, password, port);
		isUse = true;
	}
	
	/*
     * 发送 topic 消息。所有消费端都可以接收到
     * @param routingKey 路由键，如 com.xnx3.wangmarket.plugin.qqkefu
     * @param content 发送的消息内容
	 */
	public static void sendTopicMessage(String routingKey, String content){
		try {
			rabbitUtil.sendTopicMessage(routingKey, content);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 绑定接收方法。一个 routingKey 只能绑定一个 Consumer
	 * @param routingKey
	 * @param consumer new DefaultConsumer(rabbitUtil.rabbitMQTopicUtil.getChannel())
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static void receive(String routingKey, Consumer consumer) throws IOException, TimeoutException{
		// 当声明队列，不加任何参数，产生的将是一个临时队列，getQueue返回的是队列名称
        String queue = rabbitUtil.getChannel().queueDeclare().getQueue();
        ConsoleUtil.info("创建临时队列， routingKey："+routingKey+" , queue: "+queue);
        
        rabbitUtil.getChannel().queueBind(queue, RabbitMQTopicUtil.EXCHANGE_NAME, routingKey);
        rabbitUtil.getChannel().basicConsume(queue, true, consumer);
	}
	
}
