package com.xnx3.j2ee.func;


import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.impl.SqlServiceImpl;


/**
 * Java JDBC 操作Mysql， 连接是从spring中取的
 * @author 管雷鸣
 */
public class Sql {
	
	/**
	 * 获取数据库操作 {@link SqlService}
	 */
	public static SqlService getSqlService(){
		return SpringContextUtils.getBean(SqlServiceImpl.class);
	}
	
}
