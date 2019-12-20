package com.xnx3.wangmarket.admin.pluginManage.interfaces;


/**
 * 管理后台首页（网站管理后台、代理后台、超级管理后台） 相关接口
 * @author 管雷鸣
 *
 */
public interface AdminIndexInterface {
	/**
	 * 将 CMS网站管理后台首页 的html页面源代码进行追加，在后面再追加上一些html代码。当然，这些代码都是在body、html结束标签后追加的
	 * @return 要追加到CMS网站管理后台首页html最后面的 html代码码
	 */
	public String siteAdminIndexAppendHtml();
	
	/**
	 * 将 代理后台首页 的html页面源代码进行追加，在后面再追加上一些html代码。当然，这些代码都是在body、html结束标签后追加的
	 * @return 要追加到超级管理后台首页html最后面的 html代码
	 */
	public String agencyAdminIndexAppendHtml();
	
	/**
	 * 将 超级管理后台首页 的html页面源代码进行追加，在后面再追加上一些html代码。当然，这些代码都是在body、html结束标签后追加的
	 * @return 要追加到超级管理后台首页html最后面的 html代码
	 */
	public String superAdminIndexAppendHtml();
}