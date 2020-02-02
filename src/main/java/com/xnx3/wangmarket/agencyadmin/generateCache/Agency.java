package com.xnx3.wangmarket.agencyadmin.generateCache;

import org.springframework.stereotype.Component;
import com.xnx3.j2ee.generateCache.BaseGenerate;

/**
 * 站点栏目导航
 * @author 管雷鸣
 */
@Component(value="agencyadminAgency")
public class Agency extends BaseGenerate {
	public Agency() {
		allowCreateSubAgency();
		allowSubAgencyCreateSub();
	}
	
	public void allowCreateSubAgency(){
		createCacheObject("allowCreateSubAgency");
		cacheAdd(com.xnx3.wangmarket.agencyadmin.entity.Agency.ALLOW_CREATE_SUBAGENCY_YES, "允许");
		cacheAdd(com.xnx3.wangmarket.agencyadmin.entity.Agency.ALLOW_CREATE_SUBAGENCY_NO, "禁止");
		generateCacheFile();
	}
	
	public void allowSubAgencyCreateSub(){
		createCacheObject("allowSubAgencyCreateSub");
		cacheAdd(com.xnx3.wangmarket.agencyadmin.entity.Agency.ALLOW_CREATE_SUBAGENCY_YES, "允许");
		cacheAdd(com.xnx3.wangmarket.agencyadmin.entity.Agency.ALLOW_CREATE_SUBAGENCY_NO, "禁止");
		generateCacheFile();
	}
	
}
