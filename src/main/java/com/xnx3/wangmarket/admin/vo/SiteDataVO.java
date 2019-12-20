package com.xnx3.wangmarket.admin.vo;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteData;

/**
 * 站点 其他信息
 * @author 管雷鸣
 */
public class SiteDataVO extends BaseVO {
	private Site site;
	private SiteData siteData;

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public SiteData getSiteData() {
		return siteData;
	}

	public void setSiteData(SiteData siteData) {
		this.siteData = siteData;
	}
	
}
