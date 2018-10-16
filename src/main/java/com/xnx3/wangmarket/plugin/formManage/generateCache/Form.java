package com.xnx3.wangmarket.plugin.formManage.generateCache;

import org.springframework.stereotype.Component;
import com.xnx3.j2ee.generateCache.BaseGenerate;

/**
 * 信息反馈相关
 * @author 管雷鸣
 */
@Component
public class Form extends BaseGenerate {
	public Form() {
		state();
	}
	
	public void state(){
		createCacheObject("state");
		cacheAdd(com.xnx3.wangmarket.plugin.formManage.entity.Form.STATE_READ, "已读");
		cacheAdd(com.xnx3.wangmarket.plugin.formManage.entity.Form.STATE_UNREAD, "未读");
		generateCacheFile();
	}
}
