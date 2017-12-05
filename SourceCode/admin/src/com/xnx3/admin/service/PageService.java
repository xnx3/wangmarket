package com.xnx3.admin.service;

import com.xnx3.admin.entity.Site;
import com.xnx3.admin.entity.SiteColumn;

/**
 * 独立页面相关
 * @author 管雷鸣
 */
public interface PageService {
	
	/**
	 * 根据 {@link SiteColumn} 判断独立页面是否存在，若不存在，创建一个独立页面，并生成html缓存页
	 * @param updateName 编辑某个页面时，如果修改了栏目的标题，那么就要修改独立页面的标题
	 */
	public void createNonePage(SiteColumn siteColumn, Site site,boolean updateName);
	
}
