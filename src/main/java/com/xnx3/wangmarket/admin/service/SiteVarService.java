package com.xnx3.wangmarket.admin.service;

import com.xnx3.j2ee.util.CacheUtil;
import com.xnx3.wangmarket.admin.entity.SiteVar;

import net.sf.json.JSONObject;

/**
 * 站点全局变量
 * @author 管雷鸣
 */
public interface SiteVarService {
	
	/**
	 * 获取某个网站的全局变量
	 * <br/>获取顺序：先从 {@link CacheUtil}缓存中获取，缓存中没有，再从数据库获取，获取到后存入缓存。
	 * @param siteid 要获取的网站的id
	 * @return 如果这个站点没有全局变量，那么返回一个 new JSONObject，总之不会返回null
	 */
	public JSONObject getVar(int siteid);
	
	/**
	 * 获取某个网站的全局变量。如果一个动作中频繁获取多次，建议使用 {@link #getVar(int)} 获取到所有全局变量后，再逐个取。
	 * <br/>获取顺序：先从 {@link CacheUtil}缓存中获取，缓存中没有，再从数据库获取，获取到后存入缓存。
	 * @param siteid 要获取的网站的id
	 * @param key 获取的变量的名字
	 * @return 如果这个站点没有这个全局变量，那么返回""空字符串，总之不会返回null
	 */
	public JSONObject getVar(int siteid, String key);
	
	/**
	 * 设置某个网站的全局变量。
	 * <br/>会同时更新到缓存、 并且更新到数据库
	 * @param siteid 要设置的网站的id
	 * @param key 要设置的全局变量的key
	 * @param siteVar 要设置的全局变量
	 */
	public void setVar(int siteid, SiteVar siteVar);
}
