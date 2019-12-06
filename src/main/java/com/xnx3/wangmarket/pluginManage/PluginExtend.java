package com.xnx3.wangmarket.pluginManage;

import com.xnx3.j2ee.Global;

/**
 * 插件扩展功能
 * @author 管雷鸣
 *
 */
public abstract class PluginExtend {
	
	/**
	 * tomcat启动后，会自动加载数据库信息（system 数据表的数据到 {@link Global#system}中），这里便是tomcat启动完、加载完数据库数据后，执行此方法。
	 * <br/>可以理解为tomcat启动，会在启动后30秒左右执行此方法。
	 */
	public void databaseLoadFinish(){}
	
	/**
	 * 自动加载简单的插件表的数据，如cnzz插件，只有一个数据表，是否使用以及设置都存在这个 plugin_cnzz数据表中，就可以用此来做
	 * @return 查询数据表的SQL语句。如 cnzz插件，要返回的SQL语句为"SELECT * FROM plugin_cnzz"，系统会将查询出来的结果加载到内存中持久存储
	 * 		<ul>
	 * 			<li>null、空字符串，则是不使用</li>
	 * 			<li>字符串长度大于2，则是使用，执行sql查询</li>
	 * 		</ul>
	 */
	public String autoLoadSimplePluginTableDate(){
		return null;
	}
	
	/**
	 * 自动创建 domain 项目中，mq接收数据的监听。
	 * @return 若返回true、或者 super.autoCreateReceiveMQForDomain(); 则是使用，开启当前这个插件的 mq 监听，接收最新消息。 若为false，则是不使用mq
	 */
	public boolean autoCreateReceiveMQForDomain(){
		return true;
	}
	
}
