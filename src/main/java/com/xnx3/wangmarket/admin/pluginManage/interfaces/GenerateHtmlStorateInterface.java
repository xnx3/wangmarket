package com.xnx3.wangmarket.admin.pluginManage.interfaces;

import javax.servlet.http.HttpServletRequest;

import com.xnx3.wangmarket.admin.cache.generateSite.GenerateHtmlInterface;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * 点击生成整站后，自定义将网站生成的html存放到哪里。如存放到obs、ftp等
 * @author 管雷鸣
 */
public interface GenerateHtmlStorateInterface {
	
	/**
	 * 获取 {@link GenerateHtmlInterface} 接口的实现。如果没有插件实现此接口，那么返回null
	 */
	public GenerateHtmlInterface getGenerateHtmlInterfaceImpl(HttpServletRequest request, Site site);
	
}