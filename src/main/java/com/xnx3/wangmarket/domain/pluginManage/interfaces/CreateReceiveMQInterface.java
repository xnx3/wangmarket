package com.xnx3.wangmarket.domain.pluginManage.interfaces;


/**
 * MQ通信相关
 * @author 管雷鸣
 */
public interface CreateReceiveMQInterface {
	
	/**
	 * 自动创建 domain 项目中，mq接收数据的监听。
	 */
	public void createReceiveMQForDomain();
	
}
