package com.xnx3.wangmarket.admin.util;import org.hibernate.dialect.InnoDBStorageEngine;

public class StringUtil {
	
	/**
	 * 判断字符串是否是英文+数字，如果是则返回true，如果还有别的则返回false
	 * @param str
	 * @return
	 */
	public static boolean isEnglishAndNumber(String str){
		if (str.matches ("[a-zA-Z0-9]+")) {
		  // 字符串只包含英文和数字
			return true;
		} else {
		  // 字符串包含其他字符
			return false;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(isEnglishAndNumber("1asds但是d"));
		
	}
}
