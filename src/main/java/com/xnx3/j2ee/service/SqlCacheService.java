package com.xnx3.j2ee.service;

import com.xnx3.j2ee.entity.User;

/**
 * sql查询，首先会先读缓存，如果缓存中没有，再从mysql中取，取出来后再加入缓存。
 * @author 管雷鸣
 *
 */
public interface SqlCacheService {

	/**
	 * 根据主键查记录
	 * @param entity 实体类，如 {@link User}.class
	 * @param id 主键id，object
	 * @return 实体类
	 */
	public <E> E findById(Class<E> entity , Object id);
	
	/**
	 * 删除缓存的记录。这里只是删除缓存的记录而已，并不会影响mysql表的信息。
	 * 可以用来作为更新缓存使用
	 * @param entity 实体类，如 {@link User}.class
	 * @param id 主键id，object
	 */
	public void deleteCache(Class entity, Object id);
}