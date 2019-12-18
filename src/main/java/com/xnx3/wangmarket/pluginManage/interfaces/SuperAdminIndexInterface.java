package com.xnx3.wangmarket.pluginManage.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.domain.bean.RequestInfo;
import com.xnx3.wangmarket.domain.bean.SimpleSite;

/**
 * 超级管理后台首页相关接口
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