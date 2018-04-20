package com.xnx3.j2ee.generateCache;

import org.springframework.stereotype.Component;

/**
 * 用户相关数据缓存生成
 * @author 管雷鸣
 */
@Component
public class User extends BaseGenerate {
	public User() {
		isfreeze();
	}
	
	/**
	 * log.type 值－描述 缓存
	 */
	public void isfreeze(){
		createCacheObject("isfreeze");
    	cacheAdd(com.xnx3.j2ee.entity.User.ISFREEZE_NORMAL, "正常");
    	cacheAdd(com.xnx3.j2ee.entity.User.ISFREEZE_FREEZE, "冻结");
		generateCacheFile();
	}
	
}
