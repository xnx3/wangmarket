package com.xnx3.j2ee.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.service.SqlCacheService;
import com.xnx3.j2ee.util.CacheUtil;

@Service("sqlCacheService")
public class SqlCacheServiceImpl implements SqlCacheService {
	//{entity}实体类的名字，如 user    {id}主键的内容，如 1
	public static final String CACHE_KEY = "sql:{entity}:{id}";
	
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
		String key = CACHE_KEY.replace("{entity}", entity.getName()).replace("{id}", id.toString());
		//先从缓存中取，看缓存中有没有
		E e = (E) CacheUtil.get(key);
		if(e == null){
			//缓存中没有，那么从mysql中读
			e = sqlDAO.findById(entity, id);
			if(e != null){
				CacheUtil.setWeekCache(key, e);
			}
			System.out.println("chaxun");
		}
		return e;
	}

	@Override
	public void deleteCache(Class entity, Object id) {
		String key = CACHE_KEY.replace("{entity}", entity.getName()).replace("{id}", id.toString());
		CacheUtil.delete(key);
	}

}
