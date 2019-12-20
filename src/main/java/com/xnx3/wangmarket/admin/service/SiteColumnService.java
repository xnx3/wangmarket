package com.xnx3.wangmarket.admin.service;

import java.util.List;
import java.util.Map;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.vo.SiteColumnTreeVO;

/**
 * 站点栏目
 * @author 管雷鸣
 */
public interface SiteColumnService {
	
	/**
	 * 根据siteid查询属于此网站的栏目 排序 rank ASC
	 * @param siteid
	 */
	public List<SiteColumn> findBySiteid(int siteid);
	
	/**
	 * 刷新，重新生成新的栏目js排序、js的栏目信息存储 ，重新生成这两个文件：  siteColumn.js   siteColumnRank.js
	 * @param site
	 */
	public void resetColumnRankAndJs(Site site);
	

	/**
	 * 从当前Session缓存中，拿当前网站的栏目。如果当前没有缓存，那么读数据库缓存栏目数据
	 * @return 栏目树
	 */
	public List<SiteColumnTreeVO> getSiteColumnTreeVOByCache();
	
	/**
	 * 更新Shiro Session中的栏目缓存，单纯只是更新Session缓存，不操作数据库。 CMS建站模式下才有更新的必要，只有CMS模式下才会用到缓存的栏目数据
	 * @param siteColumn 要更新的栏目对象
	 */
	public void updateSiteColumnByCache(SiteColumn siteColumn);
	
	/**
	 * 获取当前网站的栏目列表，从Shiro的Session缓存中
	 * @return map key:siteColumn.id
	 */
	public Map<Integer, SiteColumn> getSiteColumnMapByCache();
	
	/**
	 * 获取当前网站的栏目列表，从Shiro的Session缓存中
	 * @return List
	 */
	public List<SiteColumn> getSiteColumnListByCache();
	
	/**
	 * 根据栏目id，从缓存中取栏目的信息，当然，这个缓存只是缓存当前网站的栏目
	 * @param siteColumnId 要取的栏目的id
	 * @return {@link SiteColumn} 如果没有，则返回null
	 */
	public SiteColumn getSiteColumnByCache(int siteColumnId);
	
	/**
	 * 移除Shiro Session中的栏目缓存，单纯只是更新Session缓存，不操作数据库。 CMS建站模式下才有更新的必要，只有CMS模式下才会用到缓存的栏目数据
	 * @param siteColumnId 要从缓存中移除的栏目id
	 */
	public void deleteSiteColumnByCache(int siteColumnId);
	
	/**
	 * 根据 {@link SiteColumn} 判断独立页面是否存在，若不存在，创建一个独立页面，并生成html缓存页
	 * @param updateName 编辑某个页面时，如果修改了栏目的标题，那么就要修改独立页面的标题
	 */
	public void createNonePage(SiteColumn siteColumn, Site site,boolean updateName);
	
	/**
	 * 刷新 Session 中存储的栏目缓存。清空掉原本的缓存，重新从数据库中读最新的栏目数据并缓存入Session。 v4.7增加
	 */
	public void refreshCache();
}
