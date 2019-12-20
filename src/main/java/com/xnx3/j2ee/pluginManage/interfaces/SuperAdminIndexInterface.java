package com.xnx3.j2ee.pluginManage.interfaces;


/**
 * 超级管理后台首页 相关接口
 * @author 管雷鸣
 *
 */
public interface SuperAdminIndexInterface {
	
	/**
	 * 将 超级管理后台首页 的html页面源代码进行追加，在后面再追加上一些html代码。当然，这些代码都是在body、html结束标签后追加的
	 * @return 要追加到超级管理后台首页html最后面的 html代码
	 */
	public String superAdminIndexAppendHtml();
}