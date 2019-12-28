package com.xnx3.wangmarket.domain.pluginManage.interfaces;

import com.xnx3.wangmarket.domain.bean.RequestInfo;
import com.xnx3.wangmarket.domain.bean.SimpleSite;

/**
 * 访客在浏览网页时，拦截更改访客看到的html页面
 * @author 管雷鸣
 */
public interface CreateReceiveMQInterface {
	
	/**
	 * 自动创建 domain 项目中，mq接收数据的监听。
	 */
	public void createReceiveMQForDomain();
	
}
