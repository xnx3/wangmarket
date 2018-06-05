package com.xnx3.wangmarket.admin.pluginManage;


public interface PluginInterface {
	
	/**
	 * 初始化。项目会在开启时自动运行此方法。用于初始化
	 * @return 初始化是否成功的结果。 
	 * 	<ul>
	 * 		<li>true：执行成功</li>
	 * 		<li>false：执行失败</li>
	 * 	</ul>
	 */
	public boolean init();
	
	/**
	 * 安装插件
	 */
	public boolean install();
	
	/**
	 * 卸载插件
	 */
	public boolean uninstall();
	
}
