package com.xnx3.j2ee.func;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xnx3.j2ee.dao.SqlDAO;
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
	
//	static SqlDAO sqlDao;
//	static{
//		sqlDao = SpringContextUtils.getBean(SqlDAO.class);
//	}
	
//	public static Connection getConn() throws SQLException{
//		Connection conn = dataSource.getConnection();
//		return conn;
//	}
	
//	public static ResultSet getResultSet(String Sql) throws SQLException{
//		PreparedStatement pstat = getConn().prepareStatement(Sql);  
//        ResultSet rs = pstat.executeQuery();  
//        return rs;
//	}
	
//	/**
//	 * 执行一条查询语句，返回一行。仅仅只是返回第一行！
//	 * @param Sql 查询的原生Mysql的语句，若是可能多行的话最好加limit 1
//	 * @return 结果，组合成map。key便是表中的列名，根据列名来取值
//	 * @throws SQLException
//	 */
//	public static Map<String, String> getValue(String Sql) throws SQLException{
//		Map<String, String> map = new HashMap<String, String>();
//		
//		PreparedStatement pstat = getConn().prepareStatement(Sql);  
//        ResultSet rs = pstat.executeQuery();
//        //如果有这行存在
//        if(rs.first()){
//        	 ResultSetMetaData rsmd = rs.getMetaData();
//             int count=rsmd.getColumnCount();
//             for(int i=0;i<count;i++){
//            	 map.put(rsmd.getColumnName(i+1), rs.getString(i+1));
//             }
//        }
//        
//        return map;
//	}
	
}
