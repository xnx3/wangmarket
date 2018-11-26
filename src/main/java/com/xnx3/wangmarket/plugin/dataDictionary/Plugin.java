package com.xnx3.wangmarket.plugin.dataDictionary;
import com.xnx3.wangmarket.admin.pluginManage.anno.PluginRegister;

/**
 * 数据字典，应用于总管理后台
 * @author 管雷鸣
 */
@PluginRegister(id="dataDictionary", version="1.0", menuTitle = "数据字典",menuHref="../../plugin/dataDictionary/tableList.do", applyToSuperAdmin=true, intro="列出Mysql数据库的数据结构，方便查看各个表、字段的说明。另外还有每个表的信息大小、条数统计等。")
public class Plugin{
	
}