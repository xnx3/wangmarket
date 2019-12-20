package com.xnx3.j2ee.util;

import com.xnx3.StringUtil;
import com.xnx3.j2ee.util.Sql;

/**
 * 安全方面操作
 * @author 管雷鸣
 */
public class SafetyUtil {
	
	/**
	 * 进行xss、sql注入等过滤，常用于用户输入
	 * @param text 要过滤得字符串
	 * @return 过滤好的字符
	 */
	public static String filter(String text){
		return StringUtil.filterXss(Sql.filter(text));
	}
	
	/**
	 * 进行xss攻击过滤，常用语用户输入
	 * @author 李鑫
	 * @param text 要过滤得字符串
	 * @return 过滤好的字符
	 */
	public static String xssFilter(String text) {
		return StringUtil.filterXss(text);
	}
}
