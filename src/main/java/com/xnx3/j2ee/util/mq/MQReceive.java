package com.xnx3.j2ee.util.mq;

/**
 * MQ 接收信息接口。无论是rabbitmq、还是java模拟的mq，都是用这个接收
 * @author 管雷鸣
 *
 */
public interface MQReceive {
	
	/**
	 * 这里接收的为字符串，可以解析为JSON格式后使用
	 * @param content 接收的字符串数据
	 */
	public void receive(String content);
}
