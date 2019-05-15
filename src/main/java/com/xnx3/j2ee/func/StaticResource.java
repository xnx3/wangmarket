package com.xnx3.j2ee.func;

import com.xnx3.j2ee.Global;

/**
 * 资源方面，如css、js资源引用
 * @author 管雷鸣
 *
 */
public class StaticResource {
	/**
	 * js、css资源的路径在system表中存储的 name
	 */
	public static final String SYSTEM_NAME_PATH = "STATIC_RESOURCE_PATH";
	
	/**
	 * 获取当前css、js的资源引用路径。如果system表中未设定 RESOURCE_PATH ，那么返回 //res.weiunity.com/   
	 * @return 返回字符串如  //res.weiunity.com/   
	 */
	public static String getPath(){
		String path = Global.get(SYSTEM_NAME_PATH);
		if(path == null || path.length() < 6){
			return "//res.weiunity.com/";
		}else{
			return path;
		}
	}
	
}
