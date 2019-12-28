package com.xnx3.wangmarket.admin.pluginManage.interfaces;

/**
 * 自动加载简单的站点插件表的数据，如cnzz插件，只有一个数据表，插件的数据都存在这个 plugin_cnzz数据表中，就可以用此来作为项目启动时，自动加载数据表的信息
 * @author 管雷鸣
 */
public interface AutoLoadSimpleSitePluginTableDateInterface {
	
	/**
	 * 自动加载简单的插件表的数据，如cnzz插件，只有一个数据表，是否使用以及设置都存在这个 plugin_cnzz数据表中，就可以用此来做
	 * @return 查询数据表的SQL语句。如 cnzz插件，要返回的SQL语句为"SELECT * FROM plugin_cnzz"，系统会将查询出来的结果加载到内存中持久存储
	 */
	public String autoLoadSimpleSitePluginTableDate();
	
}