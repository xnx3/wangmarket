package com.xnx3.j2ee.util;

import com.rabbitmq.client.ConnectionFactory;

/**
 * RabbitMQ topic 工具类
 * @author 管雷鸣
 * @deprecated 请使用 {@link MQUtil}
 *
 */
public class RabbitUtil extends com.xnx3.j2ee.util.mq.RabbitUtil{
	 
    /**
     * 初始化，即不设置RabbitMQ的相关信息，使用默认的，即 guest 账户及 5672端口
     */
	public RabbitUtil() {
		factory = new ConnectionFactory();
	}
	
	/**
	 * 设置RabbitMQ相关信息
	 * @param host rabbitMQ所在的ip，如 100.51.15.10
	 * @param username rabbitMQ登陆的username，如rabbitMQ安装后默认的账户 guest
	 * @param password 登陆密码
	 * @param port 端口号，默认为 5672
	 */
	public RabbitUtil(String host, String username, String password, int port) {
		factory = new ConnectionFactory();
		//设置RabbitMQ相关信息
	    factory.setHost(host);
	    factory.setUsername(username);
	    factory.setPassword(password);
	    factory.setPort(port);
	    factory.setAutomaticRecoveryEnabled(true); //设置网络异常重连
	    //factory.setTopologyRecoveryEnabled(true);//设置重新声明交换器，队列等信息。
	}
}
