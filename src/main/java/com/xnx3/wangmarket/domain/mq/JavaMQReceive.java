package com.xnx3.wangmarket.domain.mq;

/**
 * javaMQ 接收信息接口
 * @author 管雷鸣
 *
 */
public interface JavaMQReceive {
	
	public void receive(String content);
}
