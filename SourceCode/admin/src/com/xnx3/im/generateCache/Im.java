package com.xnx3.im.generateCache;

import org.springframework.stereotype.Component;
import com.xnx3.j2ee.generateCache.BaseGenerate;

/**
 * Im相关js缓存数据
 * @author 管雷鸣
 */
@Component
public class Im extends BaseGenerate{
	public Im() {
		useKefu();
	}
	
	public void useKefu(){
		createCacheObject("useKefu");
		cacheAdd(com.xnx3.im.entity.Im.USE_TRUE, "启用");
		cacheAdd(com.xnx3.im.entity.Im.USE_FALSE, "关闭");
		generateCacheFile();
	}
	
}
