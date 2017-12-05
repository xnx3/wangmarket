package com.xnx3.admin.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.admin.cache.GenerateHTML;
import com.xnx3.admin.entity.News;
import com.xnx3.admin.entity.NewsData;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.entity.SiteColumn;
import com.xnx3.admin.service.PageService;

@Service("pageService")
public class PageServiceImpl implements PageService {
	@Resource
	private SqlDAO sqlDAO;
	
	public void createNonePage(SiteColumn siteColumn,Site site,boolean updateName) {
		News news = sqlDAO.findAloneBySqlQuery("SELECT * FROM news WHERE cid = "+siteColumn.getId(), News.class);
		if(news == null){
			//需要创建一个新页面
			news = new News();
			news.setAddtime(DateUtil.timeForUnix10());
			news.setCid(siteColumn.getId());
			news.setCommentnum(0);
			news.setIntro(siteColumn.getName());
			news.setOpposenum(0);
			news.setReadnum(0);
			news.setSiteid(siteColumn.getSiteid());
			news.setStatus(News.STATUS_NORMAL);
			news.setTitle(siteColumn.getName());
			news.setTitlepic(siteColumn.getIcon());
			news.setType(News.TYPE_PAGE);
			news.setUserid(ShiroFunc.getUser().getId());
			sqlDAO.save(news);
			if(news.getId() > 0){
				NewsData newsData = new NewsData();
				newsData.setId(news.getId());
				newsData.setText(news.getTitle());
				sqlDAO.save(newsData);
				
				GenerateHTML gh = new GenerateHTML(site);
				gh.generateViewHtml(site, news, siteColumn, news.getTitle());
			}
		}else{
			if(updateName){
				news.setTitle(siteColumn.getName());
				sqlDAO.save(news);
				
				NewsData newsData = sqlDAO.findById(NewsData.class, news.getId());
				GenerateHTML gh = new GenerateHTML(site);
				gh.generateViewHtml(site, news, siteColumn, newsData.getText());
			}
		}
	}
}
