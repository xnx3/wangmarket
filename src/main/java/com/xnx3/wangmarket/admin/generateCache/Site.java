package com.xnx3.wangmarket.admin.generateCache;

import org.springframework.stereotype.Component;
import com.xnx3.j2ee.generateCache.BaseGenerate;

/**
 * 站点
 * @author 管雷鸣
 */
@Component
public class Site extends BaseGenerate {
	public Site() {
		mShowBanner();
		client();
	}
	
	public void mShowBanner(){
		createCacheObject("mShowBanner");
		cacheAdd(com.xnx3.wangmarket.admin.entity.Site.MSHOWBANNER_SHOW, "显示");
		cacheAdd(com.xnx3.wangmarket.admin.entity.Site.MSHOWBANNER_HIDDEN, "隐藏");
		generateCacheFile();
		
		//weui
		WeUI we = new WeUI();
		we.setObjName("mShowBanner");
		we.appendDataList("显示", com.xnx3.wangmarket.admin.entity.Site.MSHOWBANNER_SHOW+"");
		we.appendDataList("隐藏", com.xnx3.wangmarket.admin.entity.Site.MSHOWBANNER_HIDDEN+"");
		we.generateCacheFile();
	}
	
	public void client(){
		createCacheObject("client");
		cacheAdd(com.xnx3.wangmarket.admin.entity.Site.CLIENT_PC, "电脑端(旧版，已不推荐)");
		cacheAdd(com.xnx3.wangmarket.admin.entity.Site.CLIENT_WAP, "手机端(旧版，已不推荐)");
		cacheAdd(com.xnx3.wangmarket.admin.entity.Site.CLIENT_CMS, "CMS(新版，推荐)");
		generateCacheFile();
	}
	

}
