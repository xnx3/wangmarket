package com.xnx3.j2ee.func;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.xnx3.StringUtil;

/**
 * RabbitMQ topic 工具类
 * @author 管雷鸣
 *
 */
public class RabbitMQTopicUtil {
	public static final String EXCHANGE_NAME = "wangmarket_topic";
	
    ConnectionFactory factory = null;	//创建连接工厂
    Connection connection = null;	//链接
    Channel channel = null;	//通道
    
    
    
    /**
     * 初始化，即不设置RabbitMQ的相关信息，使用默认的，即 guest 账户及 5672端口
     */
	public RabbitMQTopicUtil() {
		factory = new ConnectionFactory();
	}
	
	/**
	 * 设置RabbitMQ相关信息
	 * @param host rabbitMQ所在的ip，如 100.51.15.10
	 * @param username rabbitMQ登陆的username，如rabbitMQ安装后默认的账户 guest
	 * @param password 登陆密码
	 * @param port 端口号，默认为 5672
	 */
	public RabbitMQTopicUtil(String host, String username, String password, int port) {
		factory = new ConnectionFactory();
		//设置RabbitMQ相关信息
	    factory.setHost(host);
	    factory.setUsername(username);
	    factory.setPassword(password);
	    factory.setPort(port);
	    factory.setAutomaticRecoveryEnabled(true); //设置网络异常重连
	    //factory.setTopologyRecoveryEnabled(true);//设置重新声明交换器，队列等信息。
	}
	
	/**
	 * 获取一个链接
	 * @return
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public Connection getConnection() throws IOException, TimeoutException{
		if(connection == null || !connection.isOpen()){
			connection = factory.newConnection();
		}
		
		return connection;
	}
	
	/**
	 * 获取一个通道
	 * @return
	 * @throws IOException 
	 * @throws TimeoutException 
	 */
	public Channel getChannel() throws IOException, TimeoutException{
		if(channel == null || !channel.isOpen()){
			channel = getConnection().createChannel();
			//声明一个topic交换类型
	        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		}
		return channel;
	}
	

	/**
	 * 关闭通道
	 */
	public void closeChannel(){
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭链接
	 */
    public void closeConnection(){
        try {
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 关闭所有，即关闭通道和链接
     */
    public void closeAll(){
    	closeChannel();
    	closeConnection();
    }

    /**
     * 发送 topic 消息。所有消费端都可以接收到
     * @param routingKey 路由键，如 com.xnx3.wangmarket
     * @param content 发送的消息内容
     * @throws IOException
     * @throws TimeoutException
     */
    public void sendTopicMessage(String routingKey, String content) throws IOException, TimeoutException{
        // 第二个参数为绑定键
        getChannel().basicPublish(EXCHANGE_NAME, routingKey, null, content.getBytes());
	}
	

	public void receive() throws IOException, TimeoutException{
       
        // 当声明队列，不加任何参数，产生的将是一个临时队列，getQueue返回的是队列名称
        String queueA = channel.queueDeclare().getQueue();
        System.out.println("临时队列：" + queueA);
        
        // 第三个参数为“绑定建”
        // * 可以代替一个单词。
        // ＃ 可以替代零个或多个单词。
        channel.queueBind(queueA, EXCHANGE_NAME, "com.xnx3.wangmarket.default");
        Consumer consumerA = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String recv = new String(body, "UTF-8");
                System.out.println("Direct-Receive-A:" + recv);
            }
        };
        channel.basicConsume(queueA, true, consumerA);
	}
}
