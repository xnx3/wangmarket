package com.xnx3.j2ee.util;

import com.xnx3.Lang;
import com.xnx3.j2ee.Global;

/**
 * 系统工具,比如获取系统变量的参数
 * @author 管雷鸣
 *
 */
public class SystemUtil extends com.xnx3.SystemUtil{

	/**
	 * 返回 system 表的值，系统变量的值
	 * @param systemVarName 系统变量的name，也就是 system 表的 name 列
	 * @return 值，也就是 system 表的 value 列。如果没有，则会返回null
	 */
	public static String get(String systemVarName){
		return Global.system.get(systemVarName);
	}
	
	/**
	 * 返回 system 表的值（int型的，此取的数据源来源于 {@link #get(String)}，只不过针对Integer进行了二次缓存 ）
	 * @param systemVarName 系统变量的name，也就是 system 表的 name 列
	 * @return 值，也就是 system 表的 value 列。如果没有，会返回0
	 */
	public static int getInt(String systemVarName){
		Integer i = Global.systemForInteger.get(systemVarName);
		if(i == null){
			//没有这个值，那么从system这个原始map中找找
			String s = Global.system.get(systemVarName);
			if(s != null){
				i = Lang.stringToInt(s, 0);
				Global.systemForInteger.put(systemVarName, i);
			}
		}
		if(i == null){
			i = 0;
		}
		Global.systemForInteger.put(systemVarName, i);
		
		return i==null? 0:i;
	}
	
	/**
	 * 当前项目再硬盘的路径，绝对路径。
	 * <br/>返回格式如 /Users/apple/git/wangmarket/target/classes/  最后会加上 /
	 * <br/>如果是在编辑器中开发时运行，返回的是 /Users/apple/git/wangmarket/target/classes/ 这种，最后是有 /target/classes/ 的
	 * <br/>如果是在实际项目中运行，也就是在服务器的Tomcat中运行，返回的是 /mnt/tomcat8/webapps/ROOT/ 这样的，最后是结束到 ROOT/ 下
	 */
	public static String getProjectPath(){
		if(Global.projectPath == null){
			String path = new Global().getClass().getResource("/").getPath();
			Global.projectPath = path.replace("WEB-INF/classes/", "");
			ConsoleUtil.info("projectPath : "+Global.projectPath);
		}
		return Global.projectPath;
	}
	
	
}
