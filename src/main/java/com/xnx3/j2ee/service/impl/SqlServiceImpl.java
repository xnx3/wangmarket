package com.xnx3.j2ee.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.Sql;

@Service("sqlService")
public class SqlServiceImpl implements SqlService {
	
	@Resource
	private SqlDAO sqlDAO;
	
	public SqlDAO getSqlDAO() {
		return sqlDAO;
	}

	public void setSqlDAO(SqlDAO sqlDAO) {
		this.sqlDAO = sqlDAO;
	}

	public int count(String tableName, String where) {
		return sqlDAO.count(tableName, where);
	}

	public <E> List<E> findBySql(Sql sql, Class<E> entityClass){
		return sqlDAO.findBySqlQuery(sql.getSql(), entityClass);
	}

	public <E> List<E> findBySqlQuery(String sqlQuery, Class<E> entityClass) {
		return sqlDAO.findBySqlQuery(sqlQuery, entityClass);
	}
	
	
	public List<Map<String, Object>> findMapBySql(Sql sql) {
		return sqlDAO.findMapBySql(sql);
	}

	public List<Map<String,Object>> findMapBySqlQuery(String sqlQuery){
		return sqlDAO.findMapBySqlQuery(sqlQuery);
	}
	
	public void save(Object entity) {
		sqlDAO.save(entity);
	}

	public void delete(Object entity) {
		sqlDAO.delete(entity);
	}

	public <E> E findById(Class<E> c , int id){
		return sqlDAO.findById(c, id);
	}

//	public List findByExample(Object entity) {
//		return sqlDAO.findByExample(entity);
//	}

	public List findByProperty(Class c, String propertyName, Object value) {
		return sqlDAO.findByProperty(c, propertyName, value); 
	}

	public <E> E findAloneByProperty(Class<E> c,String propertyName, Object value){
		return sqlDAO.findAloneByProperty(c, propertyName, value); 
	}
	
	public int executeSql(String sql) {
		return sqlDAO.executeSql(sql);
	}

	public void addOne(String tableName, String fieldName, String where) {
		sqlDAO.addOne(tableName, fieldName, where);
	}

	public void subtractOne(String tableName, String fieldName, String where) {
		sqlDAO.subtractOne(tableName, fieldName, where);
	}

	public Object findAloneBySqlQuery(String sqlQuery, Class entityClass) {
		return sqlDAO.findAloneBySqlQuery(sqlQuery, entityClass);
	}
	
	public <E> List<E> findAll(Class<E> entityClass) {
		return sqlDAO.findAll(entityClass);
	}
//	
//	public Session getCurrentSession() {
//		return sqlDAO.getCurrentSession();
//	}
	
	public List findByHql(String hql, Map<String, Object> parameterMap){
		return sqlDAO.findByHql(hql, parameterMap, 0);
	}
	public List findByHql(String hql, Map<String, Object> parameterMap, int maxNumber){
		return sqlDAO.findByHql(hql, parameterMap, maxNumber);
	}
	
	public int executeByHql(String hql, Map<String, Object> parameterMap) {
		return sqlDAO.executeByHql(hql, parameterMap);
	}
	public int updateByHql(Class c, String setPropertyName, String setPropertyValue, String wherePropertyName, Object wherePropertyValue){
		return sqlDAO.updateByHql(c, setPropertyName, setPropertyValue, wherePropertyName, wherePropertyValue);
	}
}
