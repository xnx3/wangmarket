package com.xnx3.wangmarket.domain.pluginManage.interfaces;

import com.xnx3.wangmarket.domain.bean.RequestInfo;
import com.xnx3.wangmarket.domain.bean.SimpleSite;

/**
 * 访客在浏览网页时，拦截更改访客看到的html页面
 * @author 管雷鸣
 */
public interface DomainVisitInterface {
	
	/**
	 * 将 网站显示的html页面源代码进行处理。如追加、替换、排查……
	 * @param html 用户访问的，要显示出来的,原始的html页面源代码
	 * @param simpleSite 用户所访问的网站，站点的信息
	 * @param requestInfo 访问用户的信息，及访问的具体页面等信息
	 * @return 处理好的 html页面源代码，返回的html页面源代码。用户看到的html页面源代码
	 */
	public String htmlManage(String html, SimpleSite simpleSite, RequestInfo requestInfo);
	
}
