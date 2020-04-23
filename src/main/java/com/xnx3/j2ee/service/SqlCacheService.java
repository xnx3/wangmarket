package com.xnx3.j2ee.service;

import java.util.List;

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
	 * 可以用来作为更新缓存使用。
	 * 这里删除的缓存，是 {@link #findById(Class, Object)} 中产生的缓存数据
	 * @param entity 实体类，如 {@link User}.class
	 * @param id 主键id，object
	 */
	public void deleteCacheById(Class entity, Object id);
	
	
	/**
	 * 根据字段名查一条值，取一条记录。
	 * 1. value会自动进行sql注入过滤
	 * 2. 会先从缓存中取，如果缓存中没有，再从mysql中取。取出来如果有值，放入缓存
	 * @param entity {@link Class} 实体类，如 {@link User}.class
	 * @param propertyName 数据表字段名(Hibernate 语句的字段名，驼峰命名,非数据库的字段名)
	 * @param value 值
	 * @return {@link List} 实体类
	 */
	public <E> E findAloneByProperty(Class<E> entity,String propertyName, Object value);
	
	/**
	 * 删除缓存的记录。这里只是删除缓存的记录而已，并不会影响mysql表的信息。
	 * 可以用来作为更新缓存使用。
	 * 这里删除的缓存，是 {@link #findAloneByProperty(Class, String, Object)} 中产生的缓存数据
	 * @param entity {@link Class} 实体类，如 {@link User}.class
	 * @param propertyName 数据表字段名(Hibernate 语句的字段名，驼峰命名,非数据库的字段名)
	 * @param value 值
	 */
	public void deleteCacheByProperty(Class entity,String propertyName, Object value);
	
}