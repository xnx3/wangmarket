package com.xnx3.wangmarket.admin.pluginManage.interfaces;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.pluginManage.bean.GenerateSiteColumnNewsDatasourceBean;

/**
 * 生成整站时，栏目内容，也就是栏目内容的数据源进行操作。比如栏目生成后，栏目的内容时另一个网站的栏目内容
 * @author 管雷鸣
 */
public interface GenerateSiteColumnNewsDatasourceInterface {
	
	/**
	 * 点击生成整站按钮后，在生成栏目html、栏目内的文章html之前，会触发此处。  
	 * 改动时，可直接改动 columnNewsList、newsDataMap 即可调整过栏目的数据
	 * @param site 生成整站的网站 {@link Site}
	 * @param siteColumn 当前要生成的栏目
	 * @param columnNewsList 当前栏目内的文章数据
	 * @param newsDataMap 当前栏目内的文章详情的数据 key: {@link News}.id  value: {@link NewsDataBean}
	 * @return 处理后的新的 {@link GenerateSiteColumnNewsDatasourceBean}
	 */
	public GenerateSiteColumnNewsDatasourceBean generateSiteColumnNewsDatasource(HttpServletRequest request, Site site, SiteColumn siteColumn, List<News> columnNewsList, Map<Integer, NewsDataBean> newsDataMap);
	
}