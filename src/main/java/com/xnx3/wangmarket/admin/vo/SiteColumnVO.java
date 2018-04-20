package com.xnx3.wangmarket.admin.vo;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 栏目
 * @author 管雷鸣
 *
 */
public class SiteColumnVO extends BaseVO{
	private SiteColumn siteColumn;

	public SiteColumn getSiteColumn() {
		return siteColumn;
	}

	public void setSiteColumn(SiteColumn siteColumn) {
		this.siteColumn = siteColumn;
	}
	
}
