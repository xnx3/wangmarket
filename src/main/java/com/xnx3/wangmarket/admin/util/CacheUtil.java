package com.xnx3.wangmarket.admin.util;

import com.xnx3.wangmarket.admin.entity.SiteVar;

/**
 * 缓存相关
 * @author 管雷鸣
 *
 */
public class CacheUtil extends com.xnx3.j2ee.util.CacheUtil{
	/**
	 * 网站全局变量的缓存
	 */
	public static final String SITE_VAR = "SITE:VAR:{siteid}";
	
	/**
	 * 获取某个网站缓存的全局变量的值
	 * @param siteid 要获取的网站的id
	 * @return 如果没有缓存过，那么会返回null
	 */
	public static String getSiteVar(int siteid){
		Object obj = get(SITE_VAR.replace("{siteid}", siteid+"")); 
		return (String) obj;
	}
	
	/**
	 * 设置某个网站的全局变量
	 * @param siteVar 实体类
	 */
	public static void setSiteVar(SiteVar siteVar){
		if(siteVar == null){
			return;
		}
		setWeekCache(SITE_VAR.replace("{siteid}", siteVar.getId()+""), siteVar.getText());
	}
	
}
