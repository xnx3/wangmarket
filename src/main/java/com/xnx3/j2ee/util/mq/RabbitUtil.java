package com.xnx3.j2ee.util.mq;

/**
 * RabbitMQ topic 工具类
 * @author 管雷鸣
 *
 */
public class RabbitUtil extends com.xnx3.rabbitmq.RabbitUtil{

	/**
	 * 设置RabbitMQ相关信息
	 * @param host rabbitMQ所在的ip，如 100.51.15.10
	 * @param username rabbitMQ登陆的username，如rabbitMQ安装后默认的账户 guest
	 * @param password 登陆密码
	 * @param port 端口号，默认为 5672
	 */
	public RabbitUtil(String host, String username, String password, int port) {
		super(host, username, password, port);
	}
}
