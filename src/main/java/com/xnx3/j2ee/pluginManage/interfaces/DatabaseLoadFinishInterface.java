package com.xnx3.j2ee.pluginManage.interfaces;

import com.xnx3.j2ee.Global;

/**
 * 当数据库( system 数据表 )加载完毕后，触发执行
 * @author 管雷鸣
 *
 */
public interface DatabaseLoadFinishInterface {
	/**
	 * tomcat启动后，会自动加载数据库信息（system 数据表的数据到 {@link Global#system}中），这里便是tomcat启动完、加载完数据库数据后，执行此方法。
	 * <br/>可以理解为tomcat启动，会在启动后30秒左右执行此方法。
	 */
	public void databaseLoadFinish();
}
