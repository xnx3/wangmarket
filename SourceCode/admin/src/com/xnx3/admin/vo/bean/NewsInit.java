package com.xnx3.admin.vo.bean;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.admin.entity.News;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.entity.SiteColumn;
import com.xnx3.admin.service.NewsService;

/**
 * {@link NewsService#news(javax.servlet.http.HttpServletRequest, int, int, org.springframework.ui.Model)}初始化所返回数值
 * @author 管雷鸣
 */
public class NewsInit extends BaseVO {
	private String pageTitle;	//网页的标题，是添加新闻，还是修改图文
	private News news;
	private SiteColumn siteColumn;
	private Site site;
	private String newsText;	//news.text，内容
	private String titlepicImage;	//当前显示的titlepic的缩略图，会自动加上图片标签，默认高度30px
	
	public NewsInit() {
		titlepicImage = "";
	}
	
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public News getNews() {
		return news;
	}
	public void setNews(News news) {
		this.news = news;
	}
	public SiteColumn getSiteColumn() {
		return siteColumn;
	}
	public void setSiteColumn(SiteColumn siteColumn) {
		this.siteColumn = siteColumn;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	
	public String getNewsText() {
		return newsText;
	}
	public void setNewsText(String newsText) {
		this.newsText = newsText;
	}
	public String getTitlepicImage() {
		return titlepicImage;
	}
	public void setTitlepicImage(String titlepicImage) {
		this.titlepicImage = titlepicImage;
	}
	@Override
	public String toString() {
		return "NewsInit [pageTitle=" + pageTitle + ", news=" + news
				+ ", siteColumn=" + siteColumn + ", site=" + site + "]";
	}
	
}
