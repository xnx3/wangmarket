package com.xnx3.wangmarket.domain.mq;

/**
 * domain 接收到 mq 信息后，执行的动作
 * @author 管雷鸣
 */
public interface ReceiveDomainMQ {
	
	/**
	 * mq 接收到信息之后执行的逻辑
	 * @param content 接收到的字符串
	 */
	public void receive(String content);
}
