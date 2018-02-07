package com.xnx3.im.service;

import com.xnx3.im.entity.Im;


/**
 * im 相关
 * @author 管雷鸣
 */
public interface ImService {
	
	/**
	 * 取得当前网站的 Im 对象。从缓存中取。若缓存中没有，则从数据库取，若数据库中也没有，则自动创建一个默认不使用客服的im，取出后进行缓存。
	 * <br/>若用户未登录，则不会缓存，只是取出一个new Im()
	 */
	public Im getImByCache();
	
	/**
	 * 取得当前网站的 Im 对象。从数据库中取。若数据库中没有，用户还没设置过，那么自动new一个出来
	 * <br/>这里主要是在修改某个东西保存时用到。取最新的im对象，可直接使用 sqlService.save()进行更新
	 */
	public Im getImByDB();
	
	
	/**
	 * 更新 Shiro 中的 SiteIm 缓存
	 */
	public void updateImForCache(Im im);
	
	/**
	 * 更新IM的应用服务，将某个用户的IM改动加入消息队列，以便其他IM应用更新
	 * @param haveImSet 当前是否使用im
	 * @param im 当前修改的属性
	 */
	public void updateIMServer(boolean haveImSet, Im im);
}
