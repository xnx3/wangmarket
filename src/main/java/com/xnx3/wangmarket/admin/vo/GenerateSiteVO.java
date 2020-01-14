package com.xnx3.wangmarket.admin.vo;

import java.util.List;
import java.util.Map;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 点击生成整站完成后，当前的数据
 * @author 管雷鸣
 *
 */
public class GenerateSiteVO extends BaseVO {
	Map<String, SiteColumn> siteColumnMap;	//当前网站的所有栏目。 key: {@link SiteColumn}.code，  value:{@link SiteColumn}
	Map<String, List<News>> newsMap;		//当前网站的文章数据， 以栏目codeName为key，将文章List加入进自己对应的栏目。同时，若传入父栏目代码，其栏目下有多个新闻子栏目，会调出所有子栏目的内容
	Map<Integer, NewsDataBean> newsDataMap;	//当前网站的文章详情的数据 key: {@link News}.id  value: {@link NewsDataBean}

	public Map<String, SiteColumn> getSiteColumnMap() {
		return siteColumnMap;
	}
	public void setSiteColumnMap(Map<String, SiteColumn> siteColumnMap) {
		this.siteColumnMap = siteColumnMap;
	}
	public Map<String, List<News>> getNewsMap() {
		return newsMap;
	}
	public void setNewsMap(Map<String, List<News>> newsMap) {
		this.newsMap = newsMap;
	}
	public Map<Integer, NewsDataBean> getNewsDataMap() {
		return newsDataMap;
	}
	public void setNewsDataMap(Map<Integer, NewsDataBean> newsDataMap) {
		this.newsDataMap = newsDataMap;
	}
	
	
}
