package com.xnx3.wangmarket.plugin.dataDictionary;

import com.xnx3.j2ee.pluginManage.PluginRegister;

/**
 * 数据字典，应用于总管理后台
 * @author 管雷鸣
 */
@PluginRegister(version="1.2.0", menuTitle = "数据字典",menuHref="/plugin/dataDictionary/tableList.do", applyToSuperAdmin=true, intro="列出Mysql数据库的数据结构，方便查看各个表、字段的说明。另外还有每个表的信息大小、条数统计等。", versionMin="5.0")
public class Plugin{
	
}