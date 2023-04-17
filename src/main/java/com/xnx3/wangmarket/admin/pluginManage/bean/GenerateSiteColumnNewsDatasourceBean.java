package com.xnx3.wangmarket.admin.pluginManage.bean;

import java.util.List;
import java.util.Map;

import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 生成整站时栏目相关的数据
 * @author 管雷鸣
 *
 */
public class GenerateSiteColumnNewsDatasourceBean{
	private SiteColumn siteColumn;	//当前要生成的栏目
	private List<News> columnNewsList;	//当前栏目中的文章列表
	private Map<Integer, NewsDataBean> newsDataMap;	//当前栏目中的文章内容，其中key为news.id
	
	public SiteColumn getSiteColumn() {
		return siteColumn;
	}
	public void setSiteColumn(SiteColumn siteColumn) {
		this.siteColumn = siteColumn;
	}
	public List<News> getColumnNewsList() {
		return columnNewsList;
	}
	public void setColumnNewsList(List<News> columnNewsList) {
		this.columnNewsList = columnNewsList;
	}
	public Map<Integer, NewsDataBean> getNewsDataMap() {
		return newsDataMap;
	}
	public void setNewsDataMap(Map<Integer, NewsDataBean> newsDataMap) {
		this.newsDataMap = newsDataMap;
	}
	@Override
	public String toString() {
		return "GenerateSiteColumnNewsDatasourceBean [siteColumn=" + siteColumn + ", columnNewsList=" + columnNewsList
				+ ", newsDataMap=" + newsDataMap + "]";
	}
	
}
