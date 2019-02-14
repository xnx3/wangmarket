package com.xnx3.j2ee.service;

import java.util.List;
import java.util.Map;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.util.Sql;

/**
 * 公共查询，直接执行SQL
 * @author 管雷鸣
 *
 */
public interface SqlService {

	/**
	 * 获取查询的信息条数。
	 * <br/>示例：查询user数据表中，id大于1的信息的条数：
	 * <pre>
	 * 	int count = sqlService.count("user", "WHERE id > 1");
	 * </pre>
	 * @param tableName 表名,实际数据表中的数据表名字，非实体类的名字。多个表名中间用,分割，如: "user,message,log"。同样如果是多个表，where参数需要增加关联条件
	 * @param where 查询条件，传入如“WHERE id > 1” ；若没有查询条件，则可以传入null或者""空字符串
	 * @return 统计条数
	 */
	public int count(String tableName,String where);

	/**
	 * 查询列表，通过 {@link Sql} 自动生成查询语句查询信息列表,返回List实体类。通常用于分页列表。
	 * <br/>示例：用户信息的一个分页列表
	 * <pre>
	 *		com.xnx3.j2ee.util.Sql sql = new com.xnx3.j2ee.util.Sql(request);
	 *		//查询user数据表的记录总条数。 传入的user：数据表的名字为user
	 *		int count = sqlService.count("user", sql.getWhere());
	 *		//创建分页，并设定每页显示15条
	 *		com.xnx3.j2ee.util.Page page = new Pacom.xnx3.j2ee.util.Pagege(count, 15, request);
	 *		//创建查询语句，只有SELECT、FROM，原生sql查询。其他的where、limit等会自动拼接
	 *		sql.setSelectFromAndPage("SELECT * FROM user", page);
	 *		//因只查询的一个表，所以可以将查询结果转化为实体类，用List接收。
	 *		List<User> list = sqlService.findBySql(sql, User.class);
	 * </pre>
	 * @param sql 组合好的查询{@link Sql}
	 * @param entityClass 转化为什么实体类返回
	 * @return List 实体类列表
	 */
	public <E> List<E> findBySql(Sql sql, Class<E> entityClass);
	
	/**
	 * 通过原生SQL语句查询,返回List实体类
	 * <br/>示例：查询列出 user 用户表中，id大于1的用户列表信息
	 * <pre>
	 * 		List<User> userList = sqlService.findBySqlQuery("SELECT * FROM user WHERE id > 1", User.class);
	 * <pre>
	 * @param sql 原生SQL查询语句
	 * @param entityClass 转化为什么实体类输出
	 * @return List 实体类列表
	 */
	public <E> List<E> findBySqlQuery(String sqlQuery, Class<E> entityClass);
	
	/**
	 * 传入原生SQL语句，查询返回一个实体类。 会自动在原生SQL语句末尾添加 LIMIT 0,1 进行组合查询语句
	 * @param sqlQuery 查询语句，如 SELECT * FROM user WHERE username = 'xnx3'
	 * @param entityClass 要转换为什么实体类返回，如 User.class
	 * @return 若查询到，返回查询到的对象，若查询不到，返回null
	 */
	public <E> E findAloneBySqlQuery(String sqlQuery,Class<E> entityClass);
	
	/**
	 * 传入 {@link Sql} 查询List列表
	 * @param sql 组合好的{@link Sql}
	 * @return List<Map<String,String>>
	 */
	public List<Map<String,Object>> findMapBySql(Sql sql);
	
	/**
	 * 传入查询的SQL语句
	 * @param sqlQuery SQL语句
	 * @return List<Map<String,String>>
	 */
	public List<Map<String,Object>> findMapBySqlQuery(String sqlQuery);
	
	/**
	 * 添加/修改
	 * @param entity 实体类
	 */
	public void save(Object entity);
	
	/**
	 * 删除
	 * @param entity 实体类
	 */
	public void delete(Object entity);
	
	/**
	 * 根据主键查记录
	 * @param entity 实体类，如 {@link User}.class
	 * @param id 主键id
	 * @return 实体类
	 */
	public <E> E findById(Class<E> c , int id);
	
	/**
	 * 根据实体类对象的赋值查纪录列表
	 * @param obj 实体类
	 * @return List 实体类
	 */
//	public <E> List<E> findByExample(Object entity);
	
	/**
	 * 根据字段名查值。value会自动进行sql注入过滤
	 * @param c {@link Class} 实体类，如 {@link User}.class
	 * @param propertyName 数据表字段名(Hibernate 语句的字段名)
	 * @param value 值
	 * @return {@link List} 实体类
	 */
	public <E> List<E> findByProperty(Class<E> c,String propertyName, Object value);
	
	/**
	 * 根据字段名查一条值，取一条记录。value会自动进行sql注入过滤
	 * @param c {@link Class} 实体类，如 {@link User}.class
	 * @param propertyName 数据表字段名(Hibernate 语句的字段名，驼峰命名,非数据库的字段名)
	 * @param value 值
	 * @return {@link List} 实体类
	 */
	public <E> E findAloneByProperty(Class<E> c,String propertyName, Object value);
	
	/**
	 * 执行原生SQL语句
	 * @param sql 要执行的SQL语句
	 * @return query.executeUpdate()的返回值，即Sql语句成功更新的条数
	 */
	public int executeSql(String sql);
	
	/**
	 * 数据表的某项数值+1
	 * @param tableName 数据表名称。实际数据表中的数据表名字，非实体类的名字。
	 * @param fieldName 执行＋1的项
	 * @param where 条件，如 id=5
	 */
	public void addOne(String tableName,String fieldName,String where);
	
	/**
	 * 数据表的某项数值-1
	 * @param tableName 数据表名称。实际数据表中的数据表名字，非实体类的名字。
	 * @param fieldName 执行＋1的项
	 * @param where 条件，如 id=5
	 */
	public void subtractOne(String tableName,String fieldName,String where);

	/**
	 * 查询某个数据表的所有信息, 返回实体类列表 List<Entity> 
	 * <br/>相当于 SELECT * FROM tableName
	 * @param entityClass 从哪个实体类关联的数据表取数据，转化为什么实体类。如，传入 User.class
	 * @return List<Entity>
	 */
	public <E> List<E> findAll(Class<E> entityClass);
	
	/**
	 * 获取当前Hibernate的Session对象
	 * @return {@link Session}当前hibernate的Session
	 */
//	public Session getCurrentSession();
	

	/**
	 * 通过hql语句进行查询。示例：
	 * <pre>
	 * 		String hql = "FROM User u WHERE u.username = :username";
	 * 		Map&lt;String, Object&gt; parameterMap = new HashMap&lt;String, Object&gt;();
     *		parameterMap.put("username", "guanleiming");
     *		parameterMap.put("age", "26");
     *		findByHql(hql, parameterMap)
	 * </pre>
	 * @param hql hql语句，如 FROM User u WHERE u.username=:username
	 * @param parameterMap hql中的查询条件
	 * @return list
	 */
    public List findByHql(String hql, Map<String, Object> parameterMap);
    

	/**
	 * 通过hql语句进行查询。示例：
	 * <pre>
	 * 		String hql = "FROM User u WHERE u.username = :username";
	 * 		Map&lt;String, Object&gt; parameterMap = new HashMap&lt;String, Object&gt;();
     *		parameterMap.put("username", "guanleiming");
     *		parameterMap.put("age", "26");
     *		findByHql(hql, parameterMap)
	 * </pre>
	 * @param hql hql语句，如 FROM User u WHERE u.username=:username
	 * @param parameterMap hql中的查询条件
	 * @param maxNumber 最大查询条数，同 limit ， 0为不限制
	 * @return list
	 */
    public List findByHql(String hql, Map<String, Object> parameterMap, int maxNumber);
    
    
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
    public int executeByHql(String hql, Map<String, Object> parameterMap);
    
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
	public int updateByHql(Class c, String setPropertyName, String setPropertyValue, String wherePropertyName, Object wherePropertyValue);
}