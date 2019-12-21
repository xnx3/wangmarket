package com.xnx3.j2ee.util;

import com.xnx3.j2ee.func.SpringContextUtils;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.impl.SqlServiceImpl;

/**
 * 数据库
 * @author 管雷鸣
 */
public class SqlServiceUtil {
	
	/**
	 * 获取数据库操作 {@link SqlService}
	 */
	public static SqlService getSqlService(){
		return SpringContextUtils.getBean(SqlServiceImpl.class);
	}
	
}
