package com.xnx3.wangmarket.admin.generateCache;

import org.springframework.stereotype.Component;
import com.xnx3.j2ee.generateCache.BaseGenerate;

/**
 * 轮播图
 * @author 管雷鸣
 */
@Component
public class Carousel extends BaseGenerate {
	public Carousel() {
		isshow();
		type();
	}
	
	/**
	 * 是否显示
	 */
	public void isshow(){
		createCacheObject("isshow");
		cacheAdd(com.xnx3.wangmarket.admin.entity.Carousel.ISSHOW_SHOW , "显示");
		cacheAdd(com.xnx3.wangmarket.admin.entity.Carousel.ISSHOW_HIDDEN , "隐藏");
		generateCacheFile();
	}
	
	/**
	 * 类型
	 */
	public void type(){
		createCacheObject("type");
		cacheAdd(com.xnx3.wangmarket.admin.entity.Carousel.TYPE_DEFAULT_PAGEBANNER , "内页Banner");
		cacheAdd(com.xnx3.wangmarket.admin.entity.Carousel.TYPE_INDEXBANNER , "首页Banner");
		generateCacheFile();
	}
}
