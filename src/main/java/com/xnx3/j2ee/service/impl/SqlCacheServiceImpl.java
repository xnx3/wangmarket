package com.xnx3.j2ee.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.service.SqlCacheService;
import com.xnx3.j2ee.util.CacheUtil;

@Service("sqlCacheService")
public class SqlCacheServiceImpl implements SqlCacheService {
	//通过主键查询出来的实体类信息的缓存key， {entity}实体类的名字，如 user    {id}主键的内容，如 1
	public static final String CACHE_KEY_BY_ID  = "sql:{entity}:id:{id}";
	//通过指定字段查询出来的实体类信息的缓存key， {entity}实体类的名字，如 user   {property} 实体类中驼峰字段的名字，如 username   {value}查询的字段的值，如 1
	public static final String CACHE_KEY_BY_PROPERTY  = "sql:{entity}:{property}:{value}";
	
	@Resource
	private SqlDAO sqlDAO;
	
	public SqlDAO getSqlDAO() {
		return sqlDAO;
	}

	public void setSqlDAO(SqlDAO sqlDAO) {
		this.sqlDAO = sqlDAO;
	}

	@Override
	public <E> E findById(Class<E> entity, Object id) {
		String key = CACHE_KEY_BY_ID.replace("{entity}", entity.getName()).replace("{id}", id.toString());
		//先从缓存中取，看缓存中有没有
		E e = (E) CacheUtil.get(key);
		if(e == null){
			//缓存中没有，那么从mysql中读
			e = sqlDAO.findById(entity, id);
			if(e != null){
				CacheUtil.setWeekCache(key, e);
			}
		}
		return e;
	}

	@Override
	public void deleteCacheById(Class entity, Object id) {
		String key = CACHE_KEY_BY_ID.replace("{entity}", entity.getName()).replace("{id}", id.toString());
		CacheUtil.delete(key);
	}


	@Override
	public <E> E findAloneByProperty(Class<E> entity, String propertyName, Object value) {
		String key = CACHE_KEY_BY_PROPERTY.replace("{entity}", entity.getName()).replace("{property}", propertyName).replace("{value}", value.toString());
		//先从缓存中取，看缓存中有没有
		E e = (E) CacheUtil.get(key);
		if(e == null){
			//缓存中没有，那么从mysql中读
			e = (E) sqlDAO.findAloneByProperty(entity, propertyName, value);
			if(e != null){
				CacheUtil.setWeekCache(key, e);
			}
		}
		return e;
	}

	@Override
	public void deleteCacheByProperty(Class entity, String propertyName, Object value) {
		String key = CACHE_KEY_BY_PROPERTY.replace("{entity}", entity.getName()).replace("{property}", propertyName).replace("{value}", value.toString());
		CacheUtil.delete(key);
	}

}
