package com.xnx3.wangmarket.admin.vo;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * 站点
 * @author 管雷鸣
 */
public class SiteVO extends BaseVO {
	private Site site;

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
	
}
