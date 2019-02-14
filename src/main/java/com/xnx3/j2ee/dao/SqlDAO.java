package com.xnx3.j2ee.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import org.hibernate.query.Query;
//import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.util.Sql;

/**
 * 通用的
 * @author 管雷鸣
 */
@Transactional
@Repository
public class SqlDAO {
	
    @PersistenceContext
    EntityManager entityManager;  
	
	/**
	 * 获取查询的信息条数
	 * @param tableName 表名,多个表中间用,分割，如: "user,message,log"。同样如果是多个表，where参数需要增加关联条件
	 * @param where 查询条件，传入如“WHERE id > 1”可直接使用 {@link Sql#getWhere(javax.servlet.http.HttpServletRequest, String[], String)} 来组合
	 * @return 统计条数
	 */
	public int count(String tableName,String where){
		if(where == null){
			where = "";
		}
		String queryString = "SELECT count(*) FROM "+tableName+" "+where;
		
		javax.persistence.Query query = entityManager.createNativeQuery(queryString);
		List list = query.getResultList();
        entityManager.close();
        
        String count = list.get(0).toString();
        return Lang.stringToInt(count, 0);
	}

	/**
	 * 传入 {@link Sql} 查询List列表
	 * @param sql 组合好的{@link Sql}
	 * @return List Map<String,String>
	 */
	public List<Map<String,Object>> findMapBySql(Sql sql){
//		try {
//			Query queryObject = getCurrentSession().createSQLQuery(sql.getSql()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//			return queryObject.list();
//		} catch (RuntimeException re) {
//			throw re;
//		}
		
        return findMapBySqlQuery(sql.getSql());
	}
	
	/**
	 * 传入查询的原生SQL语句
	 * @param sqlQuery 原生SQL语句
	 * @return List<Map<String,String>>
	 */
	public List<Map<String,Object>> findMapBySqlQuery(String sqlQuery){
//		try {
//			Query queryObject = getCurrentSession().createSQLQuery(sqlQuery).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//			return queryObject.list();
//		} catch (RuntimeException re) {
//			throw re;
//		}
		
		javax.persistence.Query query = entityManager.createNativeQuery(sqlQuery);
//		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.unwrap(Query.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String,Object>> list= query.getResultList();
        entityManager.close();
        return list;
	}
	
	/**
	 * 通过原生SQL语句查询,返回List实体类
	 * @param sql 原生SQL查询语句
	 * @param entityClass 转化为什么实体类输出
	 * @return List 实体类列表
	 */
	public <E> List<E> findBySqlQuery(String sqlQuery,Class<E> entityClass) {
        javax.persistence.Query query = entityManager.createNativeQuery(sqlQuery, entityClass);
        List<E> list= query.getResultList();
        entityManager.close();
        return list;
	}
	
	/**
	 * 传入原生SQL语句，查询返回一个实体类。 会自动在原生SQL语句末尾添加 LIMIT 0,1 进行组合查询语句
	 * @param sqlQuery 查询语句，如 SELECT * FROM user WHERE username = 'xnx3'
	 * @param entityClass 要转换为什么实体类返回，如 User.class
	 * @return 若查询到，返回查询到的对象(需强制转化为想要的实体类)，若查询不到，返回null
	 */
	public <E> E findAloneBySqlQuery(String sqlQuery,Class<E> entityClass){
		if(sqlQuery.toUpperCase().indexOf(" LIMIT ") == -1){
			sqlQuery = sqlQuery + " LIMIT 0,1";
		}
		List<E> list = findBySqlQuery(sqlQuery, entityClass);
		if(list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 添加/修改
	 * @param entity 实体类
	 */
	public void save(Object entity) {
		entityManager.persist(entity);
	}

	/**
	 * 删除
	 * @param entity 实体类
	 */
	public void delete(Object entity) {
		entityManager.remove(entityManager.merge(entity));
	}

	/**
	 * 根据主键查记录
	 * @param entity 实体类 如 {@link User}.class
	 * @param id 主键id
	 * @return 实体类
	 */
	public <E> E findById(Class<E> c , int id) {
		return entityManager.find(c,id);
	}
//	
//	/**
//	 * 根据实体类对象的赋值查纪录列表
//	 * @param obj 实体类
//	 * @return List 实体类
//	 */
//	public <E> List<E> findByExampless(Object entity) {
//		try {
//			List results = getCurrentSession()
//					.createCriteria(entity.getClass().getCanonicalName())
//					.add(create(entity)).list();
//			return results;
//		} catch (RuntimeException re) {
//			throw re;
//		}
//		
//		
////		 javax.persistence.Query query = entityManager.find(entity.getClass(), entity);
////	        List<E> list= query.getResultList();
////	        entityManager.close();
////	        return list;
//	}

	/**
	 * 根据字段名查值。value会自动进行sql注入过滤
	 * @param c {@link Class} 实体类，如 {@link User}.class
	 * @param propertyName 数据表字段名(Hibernate 语句的字段名)
	 * @param value 值
	 * @return {@link List} 实体类
	 */
	public <E> List<E> findByProperty(Class<E> c,String propertyName, Object value) {
		String hql = "FROM "+c.getSimpleName()+" c WHERE c."+propertyName+" = :c1";
 		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("c1", value);
		return findByHql(hql, parameterMap, 0);
	}
	
	
	/**
	 * 根据字段名查一条值，取一条记录。 value会自动进行sql注入过滤
	 * @param c {@link Class} 实体类，如 {@link User}.class
	 * @param propertyName 数据表字段名(Hibernate 语句的字段名)
	 * @param value 值
	 * @return {@link List} 实体类 。 若没有查到，则返回null
	 */
	public <E> E findAloneByProperty(Class<E> c,String propertyName, Object value){
		String hql = "FROM "+c.getSimpleName()+" c WHERE c."+propertyName+" = :c1";
		javax.persistence.Query query=entityManager.createQuery(hql);
		query.setParameter("c1", value);
	    query.setMaxResults(1);
		List<E> list= query.getResultList();
//        entityManager.close();
        
		if(list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 执行原生SQL语句
	 * @param sql 要执行的SQL语句
	 * @return query.executeUpdate()的返回值，即Sql语句成功更新的条数
	 */
	public int executeSql(String sql){    
		javax.persistence.Query query = entityManager.createNativeQuery(sql);
		return query.executeUpdate();
    }
	
	/**
	 * 数据表的某项数值+1
	 * @param tableName 数据表名称
	 * @param fieldName 执行＋1的项
	 * @param where 条件，如 id=5
	 */
	public void addOne(String tableName, String fieldName, String where) {
		executeSql("UPDATE "+tableName+" SET "+fieldName+" = "+fieldName+"+1 WHERE "+where);
	}

	/**
	 * 数据表的某项数值-1
	 * @param tableName 数据表名称
	 * @param fieldName 执行＋1的项
	 * @param where 条件，如 id=5
	 */
	public void subtractOne(String tableName, String fieldName, String where) {
		executeSql("UPDATE "+tableName+" SET "+fieldName+" = "+fieldName+"-1 WHERE "+where);
	}
	

	/**
	 * 查询列表,返回实体类 List<Entity>
	 * @param tableName 要查询哪个数据表的所有数据
	 * @param entityClass 转化为什么实体类
	 * @return List<Entity>
	 */
	public <E> List<E> findAll(Class<E> entityClass) {
//		try {
//			Query queryObject = getCurrentSession().createSQLQuery("SELECT * FROM "+getDatabaseTableName(entityClass)).addEntity(entityClass);
//			return queryObject.list();
//		} catch (RuntimeException re) {
//			throw re;
//		}
		return findBySqlQuery("SELECT * FROM "+getDatabaseTableName(entityClass), entityClass);
	}
	
	public static SqlDAO getFromApplicationContext(ApplicationContext ctx) {
		return (SqlDAO) ctx.getBean("SqlDAO");
	}
	
	public EntityManager getEntityManager(){
		return entityManager;
	}

	/**
	 * 通过hql语句进行查询。示例：
	 * <pre>
	 * 		String hql = "FROM User u WHERE u.username = :username";
	 * 		Map&lt;String, Object&gt; parameterMap = new HashMap&lt;String, Object&gt;();
     *		parameterMap.put("username", "guanleiming");
     *		parameterMap.put("age", "26");
     *		findByHql(hql, parameterMap)
	 * </pre>
	 * @param hql hql语句，如 FROM User
	 * @param parameterMap hql中的查询条件
	 * @param maxNumber 最大查询条数，同 limit ， 0为不限制
	 * @return list
	 */
    public List findByHql(String hql, Map<String, Object> parameterMap, int maxNumber) {
        javax.persistence.Query query=entityManager.createQuery(hql);
        for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
        	query.setParameter(entry.getKey(),entry.getValue());
        }
        if(maxNumber > 0){
        	query.setMaxResults(maxNumber);
        }
        List list= query.getResultList();
        entityManager.close();
        return list;
    }
	
    /**
	 * 通过hql语句执行sql。示例：
	 * <pre>
	 * 		String hql = "update User u set u.nickname=:nickname WHERE u.username = :username";
	 * 		Map&lt;String, Object&gt; parameterMap = new HashMap&lt;String, Object&gt;();
     *		parameterMap.put("nickname", "guan");
     *		parameterMap.put("username", "guanleiming");
     *		executeByHql(hql, parameterMap)
	 * </pre>
	 * @param hql 执行的hql语句，如 update User u set u.nickname=:nickname WHERE u.id = 1
	 * @param parameterMap hql中的变量值
	 * @return 执行此语句后，数据库中更新的记录条数
	 */
    public int executeByHql(String hql, Map<String, Object> parameterMap) {
    	System.out.println(hql);
        javax.persistence.Query query=entityManager.createQuery(hql);
        for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
        	query.setParameter(entry.getKey(),entry.getValue());
        }
        return query.executeUpdate();
    }
	
    

    /**
	 * HQL update语句，更改值。会自动进行sql注入过滤。
	 * <br/>其组合的hql语句便是：
	 * <pre>
	 * update Template c set t.setPropertyName = setPropertyValue WHERE c.wherePropertyName = wherePropertyValue
	 * </pre>
	 * <br/>使用如：
	 * <pre>
	 *  //将当前站点使用的模版变量、模版页面全部设置为绑定这个模版
	 *	updateByHql(TemplatePage.class, "templateName", templateName, "siteid", site.getId());
	 * </pre>
	 * @param c {@link Class} 实体类，如 {@link User}.class
	 * @param setPropertyName 要更新字段的数据表字段名(entity实体类的驼峰写法的字段名)
	 * @param setPropertyValue 要更新字段的值，新的值。会自动过滤sql注入
	 * @param wherePropertyName WHERE 查询条件的数据表字段名(entity实体类的驼峰写法的字段名)
	 * @param wherePropertyValue WHERE 查询条件的数据表字段名的值，条件的值。会自动过滤sql注入
	 * @return 执行此语句后，数据库中更新的记录条数
	 */
	public int updateByHql(Class c, String setPropertyName, String setPropertyValue, String wherePropertyName, Object wherePropertyValue) {
		String hql = "update "+c.getSimpleName()+" c set c."+setPropertyName+"= :c1 WHERE c."+wherePropertyName+" = :c2";
 		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("c1", setPropertyValue);
		parameterMap.put("c2", wherePropertyValue);
		return executeByHql(hql, parameterMap);
	}
	
    
	/**
	 * 从JPA的实体类中，获取数据库表的名字
	 * @param c 实体类，如 User.class
	 * @return 此实体类的数据表的原名
	 */
	public static String getDatabaseTableName(Class c){
		Table table = (Table) c.getAnnotation(javax.persistence.Table.class);
		String tableName = null;
		if(table != null && table.name() != null && table.name().length() > 0){
			tableName = table.name();
		}else{
			tableName = StringUtil.firstCharToLowerCase(c.getSimpleName());
		}
		return tableName;
	}
}