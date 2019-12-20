package com.xnx3.wangmarket.admin.generateCache;

import org.springframework.stereotype.Component;
import com.xnx3.j2ee.generateCache.BaseGenerate;

/**
 * 站点栏目导航
 * @author 管雷鸣
 */
@Component
public class News extends BaseGenerate {
	public News() {
		status();
		type();
		legitimate();
	}
	
	public void status(){
		createCacheObject("status");
		cacheAdd(com.xnx3.wangmarket.admin.entity.News.STATUS_NORMAL, "显示");
		cacheAdd(com.xnx3.wangmarket.admin.entity.News.STATUS_HIDDEN, "隐藏");
		generateCacheFile();
	}
	
	public void type(){
		createCacheObject("type");
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteColumn.TYPE_NEWS, "新闻信息");
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteColumn.TYPE_IMAGENEWS, "图文信息");
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteColumn.TYPE_PAGE, "独立页面");
//		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteColumn.TYPE_LEAVEWORD, "留言板");
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteColumn.TYPE_HREF, "超链接");
//		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteColumn.TYPE_TEXT, "纯文字");
		
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteColumn.TYPE_LIST, "信息列表");
		cacheAdd(com.xnx3.wangmarket.admin.entity.SiteColumn.TYPE_ALONEPAGE, "独立页面");
		generateCacheFile();
	}
	
	/**
	 * 是否涉嫌内容违规
	 */
	public void legitimate(){
		createCacheObject("legitimate");
		cacheAdd(com.xnx3.wangmarket.admin.entity.News.LEGITIMATE_OK, "合法");
		cacheAdd(com.xnx3.wangmarket.admin.entity.News.LEGITIMATE_NO, "涉嫌");
		generateCacheFile();
	}
}
