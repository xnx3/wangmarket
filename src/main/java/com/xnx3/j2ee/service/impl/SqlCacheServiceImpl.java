package com.xnx3.j2ee.service.impl;

import java.util.List;

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
	//通过制定表table、where条件，来缓存。 {where} 查询条件
	public static final String CACHE_KEY_BY_WHERE  = "sql:{entity}:where:{where}";
	
	
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
				CacheUtil.setYearCache(key, e);
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
				CacheUtil.setYearCache(key, e);
			}
		}
		return e;
	}

	@Override
	public void deleteCacheByProperty(Class entity, String propertyName, Object value) {
		String key = CACHE_KEY_BY_PROPERTY.replace("{entity}", entity.getName()).replace("{property}", propertyName).replace("{value}", value.toString());
		CacheUtil.delete(key);
	}

	@Override
	public <E> E findBySql(Class<E> entity, String where) {
		String tableName = SqlDAO.getDatabaseTableName(entity);
		
		String key = CACHE_KEY_BY_WHERE.replace("{entity}", entity.getName()).replace("{where}", sqlWhereToKey(where));
		//先从缓存中取，看缓存中有没有
		E e = (E) CacheUtil.get(key);
		if(e == null){
			//缓存中没有，那么从mysql中读
			List<E> list = sqlDAO.findBySqlQuery("SELECT * FROM "+tableName+" WHERE "+where+" LIMIT 0,1", entity);
			if(list != null && list.size() > 0){
				e = list.get(0);
				CacheUtil.setYearCache(key, e);
			}
		}
		return e;
	}

	@Override
	public void deleteCacheBySql(Class entity, String where) {
		String key = CACHE_KEY_BY_WHERE.replace("{entity}", entity.getName()).replace("{where}", sqlWhereToKey(where));
		CacheUtil.delete(key);
	}
	
	/**
	 * 将 sql 的where 转化为 redis 缓存的key
	 * @param where 传入如 userid = 1 AND s = '23'
	 * @return 返回如 userid_1ANDs_23
	 */
	private static String sqlWhereToKey(String where){
		String key_where = where.replaceAll("=", "_")
				.replaceAll("'", "")
				.replaceAll("\\s+", "");
		return key_where;
	} 
}
