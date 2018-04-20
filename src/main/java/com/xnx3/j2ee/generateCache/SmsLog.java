package com.xnx3.j2ee.generateCache;

import org.springframework.stereotype.Component;

/**
 * 发送短信验证码的日志相关缓存
 * @author 管雷鸣
 *
 */
@Component
public class SmsLog extends BaseGenerate {
	public SmsLog() {
		used();
	}
	
	/**
	 * log.type 值－描述 缓存
	 */
	public void used(){
		createCacheObject("used");
		cacheAdd(com.xnx3.j2ee.entity.SmsLog.USED_TRUE,"已使用");
		cacheAdd(com.xnx3.j2ee.entity.SmsLog.USED_FALSE,"未使用");
		generateCacheFile();
	}
	
}
