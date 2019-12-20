package com.xnx3.j2ee.util;

import com.xnx3.j2ee.util.SessionUtil;

/**
 * 语言相关，比如当前系统的语言包、显示文字调用等
 * @author 管雷鸣
 */
public class LanguageUtil {
	
	/**
	 * 调用语言包中设置的具体内容显示出来
	 * @param key 要显示的语言调用代码
	 * @return 显示的制定语言内容
	 */
	public static String show(String key){
		return com.xnx3.Language.show(getCurrentLanguagePackageName(), key);
	}

	
	/**
	 * 调用语言包中设置的具体内容显示出来
	 * @param key 要显示的语言调用代码
	 * @remark 当前调取显示内容的备注，无任何使用价值，仅仅只是方便开发者读代码
	 * @return 显示的制定语言内容
	 */
	public static String show(String key, String remark){
		return com.xnx3.Language.show(getCurrentLanguagePackageName(), key);
	}
	
	/**
	 * 从Shiro中，获取当前用户的语言包
	 * @return	<li>登陆了，则返回ActiveUser对象
	 * 			<li>未登陆，返回null
	 */
	public static String getCurrentLanguagePackageName(){
		String language = SessionUtil.getLanguagePackageName();
		if(language == null){
			//如果没有制定，那么使用默认的语言包
			return com.xnx3.Language.language_default;
		}else{
			return language;
		}
	}
	
	/**
	 * 设置某个用户当前使用哪种语言
	 * @param languagePackageName 语种、语言包，如chinese、english
	 * @return 设置是否成功，true:成功
	 */
	public static boolean setCurrentLanguagePackageName(String languagePackageName){
		if(com.xnx3.Language.isHaveLanguagePackageName(languagePackageName)){
			SessionUtil.setLanguagePackageName(languagePackageName);
			return true;
		}else{
			return false;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(show("user_loginSuccess"));
	}
}
