package com.xnx3.wangmarket.admin.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.xnx3.DateUtil;
import com.xnx3.file.FileUtil;
import com.xnx3.wangmarket.im.service.ImService;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.func.Log;
import com.xnx3.j2ee.func.Safety;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.cache.GenerateHTML;
import com.xnx3.wangmarket.admin.cache.Template;
import com.xnx3.wangmarket.admin.cache.TemplateCMS;
import com.xnx3.wangmarket.admin.cache.pc.IndexAboutUs;
import com.xnx3.wangmarket.admin.cache.pc.IndexNews;
import com.xnx3.wangmarket.admin.entity.Carousel;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.entity.SiteData;
import com.xnx3.wangmarket.admin.entity.TemplatePageData;
import com.xnx3.wangmarket.admin.service.NewsService;
import com.xnx3.wangmarket.admin.service.SiteColumnService;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.service.TemplateService;
import com.xnx3.wangmarket.admin.vo.IndexVO;
import com.xnx3.wangmarket.admin.vo.SiteColumnTreeVO;
import com.xnx3.wangmarket.admin.vo.SiteRemainHintVO;
import com.xnx3.wangmarket.admin.vo.SiteVO;
import com.xnx3.wangmarket.admin.vo.TemplatePageListVO;
import com.xnx3.wangmarket.admin.vo.TemplatePageVO;
import com.xnx3.wangmarket.admin.vo.TemplateVarVO;
import com.xnx3.wangmarket.admin.vo.bean.TemplateCommon;
import com.xnx3.wangmarket.superadmin.entity.Agency;
import com.xnx3.wangmarket.domain.bean.MQBean;
import com.xnx3.wangmarket.domain.bean.SimpleSite;
import com.xnx3.wangmarket.domain.mq.DomainMQ;

@Service("siteService")
public class SiteServiceImpl implements SiteService {
	@Resource
	private SqlDAO sqlDAO;
	@Resource
	private NewsService newsService;
	@Resource
	private SiteColumnService siteColumnService;
	@Resource
	private TemplateService templateService;
	@Resource
	private ImService imService;

	public void generateSiteIndex(Site site) {
		GenerateHTML gh = new GenerateHTML(site);
		
		String html = null;
		if(site.getClient() - Site.CLIENT_PC == 0){
			//刷新pc首页的时候，需要刷新pc首页中间的那些数据，还需要到数据库里将其查询出来
			
		}else if (site.getClient() - Site.CLIENT_WAP == 0) {
			html = gh.wapIndex();
		}else{
			//估计是CMS了，但是cms不会用到此处。待整理
		}
		
		if(html != null){
			AttachmentFile.putStringFile("site/"+site.getId()+"/index.html", html);
		}
	}
	

	public BaseVO refreshSiteGenerateHtml(HttpServletRequest request) {
		Site site = Func.getCurrentSite();
		
		//生成新闻/图文的栏目列表
		List<SiteColumn> siteColumnList = sqlDAO.findBySqlQuery("SELECT * FROM site_column WHERE siteid = "+site.getId()+" AND used = "+SiteColumn.USED_ENABLE, SiteColumn.class);
		Map<Integer, SiteColumn> siteColumnMap = new HashMap<Integer, SiteColumn>();	//可以根据siteColumn.id取出siteColumn数据对象
		for (int i = 0; i < siteColumnList.size(); i++) {
			SiteColumn siteColumn = siteColumnList.get(i);
			siteColumnMap.put(siteColumn.getId(), siteColumn);
			//如果是新闻列表或者图文列表，则生成列表页面
			if(siteColumn.getType() - SiteColumn.TYPE_NEWS == 0 || siteColumn.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
				newsService.generateListHtml(site, siteColumn);
			}
		}
		
		//生成内容页面
		List<News> listNews = sqlDAO.findBySqlQuery("SELECT news.* FROM news WHERE news.siteid = "+site.getId()+" AND news.status = "+News.STATUS_NORMAL+" ORDER BY news.id DESC", News.class);
		List<NewsData> listNewsData = sqlDAO.findBySqlQuery("SELECT news_data.* FROM news,news_data WHERE news.id = news_data.id AND news.siteid = "+site.getId()+" AND news.status = "+News.STATUS_NORMAL, NewsData.class);
		for (int i = 0; i < listNews.size(); i++) {
			News news = listNews.get(i);
			NewsData newsData = listNewsData.get(i);
			newsService.generateViewHtml(site, news,siteColumnMap.get(news.getCid()), new NewsDataBean(newsData), request);
		}
		
		//首页生成
		if(site.getClient() == Site.CLIENT_WAP){
			generateSiteIndex(site);			//生成WAP首页
		}else{
			/*PC 首页生成*/
			//获取关于我们内容
			News aboutUsNews = sqlDAO.findAloneBySqlQuery("SELECT * FROM news WHERE cid = "+site.getAboutUsCid(), News.class);
			NewsData aboutUsNewsData = null;
			if(aboutUsNews != null){
				aboutUsNewsData = sqlDAO.findAloneBySqlQuery("SELECT * FROM news_data WHERE id = "+aboutUsNews.getId(), NewsData.class);
			}
			
			//找出关于我们、新闻、图文列表的三个栏目
			SiteColumn aboutUsSiteColumn = sqlDAO.findAloneBySqlQuery("SELECT * FROM site_column WHERE id = "+site.getAboutUsCid(), SiteColumn.class);
			SiteColumn NewsSiteColumn = null;
			SiteColumn ImagesSiteColumn = null;
			for (int i = 0; i < siteColumnList.size(); i++) {
				SiteColumn sc = siteColumnList.get(i);
				if(NewsSiteColumn == null && sc.getType() - SiteColumn.TYPE_NEWS == 0){
					NewsSiteColumn = sc;
					continue;
				}
				if(ImagesSiteColumn == null && sc.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
					ImagesSiteColumn = sc;
					continue;
				}
			}
			
			//获取网站的信息，如首页描述等。若没有，则自动创建
			SiteData siteData = sqlDAO.findById(SiteData.class, site.getId());
			if(siteData == null){
				siteData = new SiteData();
				siteData.setId(site.getId());
				siteData.setIndexDescription(site.getName());
				sqlDAO.save(siteData);
			}
			
			//生成PC首页
			generateSitePcIndex(site, aboutUsSiteColumn, NewsSiteColumn, ImagesSiteColumn,siteData);
			
			//刷新关于我们的内容模块
			NewsData ndAU = aboutUsNewsData;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			IndexAboutUs.refreshIndexData(site, aboutUsSiteColumn, aboutUsNews, ndAU.getText());
			
			//拿到新闻、图文的前10条数据
			List<News> newsList = new ArrayList<News>();
			List<News> imagesList = new ArrayList<News>();
			for (int i = 0; i < listNews.size(); i++) {
				if(newsList.size() == 10 && imagesList.size() == 10){
					break;
				}
				
				News news  = listNews.get(i);
				if(newsList.size() < 10 && NewsSiteColumn != null && news.getCid() - NewsSiteColumn.getId() == 0){
					newsList.add(news);
					continue;
				}
				
				if(imagesList.size() < 10 && ImagesSiteColumn != null && news.getCid() - ImagesSiteColumn.getId() == 0){
					imagesList.add(news);
					continue;
				}
			}
			
			//刷新首页的新闻模块
			SiteColumn scNews = NewsSiteColumn;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			IndexNews.refreshIndexData(site, scNews, newsList);
			
			
			//刷新首页的图片模块
			SiteColumn scImages = ImagesSiteColumn;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			IndexNews.refreshIndexData(site, scImages, imagesList);
			
		}
		
		return new BaseVO();
	}

	/**
	 * 刷新首页（PC）
	 * @return
	 */
	public BaseVO refreshIndex(Site site){
		BaseVO vo = new BaseVO();
		if(site == null){
			vo.setBaseVO(BaseVO.FAILURE, "传入的站点不存在");
			return vo;
		}
		GenerateHTML gh = new GenerateHTML(site);
		String indexHtml = gh.pcIndex();		//获取到首页的模版，替换掉common的
		
		//获取关于我们内容
		News aboutUsNews = sqlDAO.findAloneBySqlQuery("SELECT * FROM news WHERE cid = "+site.getAboutUsCid(), News.class);
		NewsData aboutUsNewsData = null;
		if(aboutUsNews != null){
			aboutUsNewsData = sqlDAO.findAloneBySqlQuery("SELECT * FROM news_data WHERE id = "+aboutUsNews.getId(), NewsData.class);
		}
		
		//找出新闻、图文列表的三个栏目
		SiteColumn aboutUsSiteColumn = sqlDAO.findAloneBySqlQuery("SELECT * FROM site_column WHERE id = "+site.getAboutUsCid(), SiteColumn.class);
		SiteColumn newsSiteColumn = null;
		SiteColumn imagesSiteColumn = null;
		
		List<SiteColumn> siteColumnList = sqlDAO.findBySqlQuery("SELECT * FROM site_column WHERE siteid = "+site.getId()+" AND used = "+SiteColumn.USED_ENABLE, SiteColumn.class);
		for (int i = 0; i < siteColumnList.size(); i++) {
			SiteColumn sc = siteColumnList.get(i);
			if(newsSiteColumn == null && sc.getType() - SiteColumn.TYPE_NEWS == 0){
				newsSiteColumn = sc;
				continue;
			}
			if(imagesSiteColumn == null && sc.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
				imagesSiteColumn = sc;
				continue;
			}
		}
		
		//关于我们栏目属性替换
		indexHtml = indexHtml.replaceAll("<!--Index_Content_AboutUs_Start--><!--id=0-->", "<!--Index_Content_AboutUs_Start--><!--id="+(aboutUsSiteColumn == null? "0":""+aboutUsSiteColumn.getId())+"-->");
		//新闻栏目属性替换
		indexHtml = indexHtml.replaceAll("<!--Index_Content_NewsList_Start--><!--id=0-->", "<!--Index_Content_NewsList_Start--><!--id="+(newsSiteColumn == null ? "0":""+newsSiteColumn.getId())+"-->");
		//图文栏目属性替换
		indexHtml = indexHtml.replaceAll("<!--Index_Content_NewsImageList_Start--><!--id=0-->", "<!--Index_Content_NewsImageList_Start--><!--id="+(imagesSiteColumn == null ? "0":""+imagesSiteColumn.getId())+"-->");
		
		//获取网站的信息，如首页描述等。若没有，则自动创建
		SiteData siteData = sqlDAO.findById(SiteData.class, site.getId());
		if(siteData == null){
			siteData = new SiteData();
			siteData.setId(site.getId());
			siteData.setIndexDescription(site.getName());
			sqlDAO.save(siteData);
		}
		
		//刷新关于我们的内容模块
		IndexVO  indexVO_aboutus= IndexAboutUs.refreshIndexData(site, aboutUsSiteColumn, aboutUsNews, aboutUsNewsData.getText(), indexHtml);
		if(indexVO_aboutus.getResult() == IndexVO.SUCCESS){
			indexHtml = indexVO_aboutus.getText();
		}
		
		//拿到新闻、图文的前10条数据
		//所有的News表的信息
		List<News> listNews = sqlDAO.findBySqlQuery("SELECT news.* FROM news WHERE news.siteid = "+site.getId()+" AND news.status = "+News.STATUS_NORMAL+" ORDER BY news.id DESC", News.class);
		List<News> newsList = new ArrayList<News>();
		List<News> imagesList = new ArrayList<News>();
		for (int i = 0; i < listNews.size(); i++) {
			if(newsList.size() == 10 && imagesList.size() == 10){
				break;
			}
			
			News news  = listNews.get(i);
			if(newsList.size() < 10 && newsSiteColumn != null && news.getCid() - newsSiteColumn.getId() == 0){
				newsList.add(news);
				continue;
			}
			
			if(imagesList.size() < 10 && imagesSiteColumn != null && news.getCid() - imagesSiteColumn.getId() == 0){
				imagesList.add(news);
				continue;
			}
		}
		
		//刷新首页的新闻模块
		IndexVO indexVO_news = IndexNews.refreshIndexData(site, newsSiteColumn, newsList, indexHtml);
		if(indexVO_news.getResult() == IndexVO.SUCCESS){
			indexHtml = indexVO_news.getText();
		}
		
		//刷新首页的图片模块
		IndexVO indexVO_image = IndexNews.refreshIndexData(site, imagesSiteColumn, imagesList, indexHtml);
		if(indexVO_image.getResult() == IndexVO.SUCCESS){
			indexHtml = indexVO_image.getText();
		}
		
		//生成，写出首页
		gh.generateIndexHtml(indexHtml, siteData);
		
		return new BaseVO();
	}
	

	/**
	 * 整站刷新，自定义模版
	 * @return
	 */
	public BaseVO refreshForTemplate(HttpServletRequest request){
		BaseVO vo = new BaseVO();
		Site site = Func.getCurrentSite();
		
		TemplateCMS template = new TemplateCMS(site);
		//取得当前网站所有模版页面
//		TemplatePageListVO templatePageListVO = templateService.getTemplatePageListByCache(request);
		//取得当前网站首页模版页面
		TemplatePageVO templatePageIndexVO = templateService.getTemplatePageIndexByCache(request);
		//取得网站所有栏目信息
		List<SiteColumn> siteColumnList = sqlDAO.findBySqlQuery("SELECT * FROM site_column WHERE siteid = "+site.getId()+" ORDER BY rank ASC", SiteColumn.class);
		//取得网站所有文章News信息
		List<News> newsList = sqlDAO.findBySqlQuery("SELECT * FROM news WHERE siteid = "+site.getId() + " AND status = "+News.STATUS_NORMAL+" ORDER BY addtime DESC", News.class);
		List<NewsData> newsDataList = sqlDAO.findBySqlQuery("SELECT news_data.* FROM news,news_data WHERE news.siteid = "+site.getId() + " AND news.status = "+News.STATUS_NORMAL+" AND news.id = news_data.id ORDER BY news.id DESC", NewsData.class);
		
		//对栏目进行重新调整，以栏目codeName为key，将栏目加入进Map中。用codeName来取栏目
		Map<String, SiteColumn> columnMap = new HashMap<String, SiteColumn>();
		//对文章-栏目进行分类，以栏目codeName为key，将文章List加入进自己对应的栏目。同时，若传入父栏目代码，其栏目下有多个新闻子栏目，会调出所有子栏目的内容（20条以内）
		Map<String, List<News>> columnNewsMap = new HashMap<String, List<News>>();
		for (int i = 0; i < siteColumnList.size(); i++) {	//遍历栏目，将对应的文章加入其所属栏目
			SiteColumn siteColumn = siteColumnList.get(i);
			
			List<News> nList = new ArrayList<News>(); 
			for (int j = 0; j < newsList.size(); j++) {
				News news = newsList.get(j);
				if(news.getCid() - siteColumn.getId() == 0){
					nList.add(news);
					newsList.remove(j);	//将已经加入Map的文章从newsList中移除，提高效率。同时j--。
					j--;
					continue;
				}
			}
			
			//默认是按照时间倒序，但是v4.4以后，用户可以自定义，可以根据时间正序排序，如果不是默认的倒序的话，就需要重新排序
			//这里是某个具体子栏目的排序，父栏目排序调整的在下面
			if(siteColumn.getListRank() != null && siteColumn.getListRank() - SiteColumn.LIST_RANK_ADDTIME_ASC == 0 ){
				Collections.sort(nList, new Comparator<News>() {
		            public int compare(News n1, News n2) {
	                	//按照发布时间正序排序，发布时间越早，排列越靠前
	                	return n1.getAddtime() - n2.getAddtime();
		            }
		        });
			}
			
			columnMap.put(siteColumn.getCodeName(), siteColumn);
			columnNewsMap.put(siteColumn.getCodeName(), nList);
		}
		
		
		//对 newsDataList 网站文章的内容进行调整，调整为map key:newsData.id  value:newsData.text
		Map<Integer, NewsDataBean> newsDataMap = new HashMap<Integer, NewsDataBean>();
		for (int i = 0; i < newsDataList.size(); i++) {
			NewsData nd = newsDataList.get(i);
			newsDataMap.put(nd.getId(), new NewsDataBean(nd));
		}
		
		
		/*
		 * 对栏目进行上下级初始化，找到哪个是父级栏目，哪些是子栏目。并可以根据栏目代码来获取父栏目下的自栏目。 获取栏目树
		 */
		Map<String, SiteColumnTreeVO> columnTreeMap = new HashMap<String, SiteColumnTreeVO>();	//栏目树，根据栏目id获取当前栏目，以及下级栏目
		//首先，遍历父栏目，将最顶级栏目（一级栏目）拿出来
		for (int i = 0; i < siteColumnList.size(); i++) {
			SiteColumn siteColumn = siteColumnList.get(i);
			//根据父栏目代码,判断是否有上级栏目，若没有的话，那就是顶级栏目了，将其加入栏目树
			if(siteColumn.getParentCodeName() == null || siteColumn.getParentCodeName().length() == 0){
				SiteColumnTreeVO scTree = new SiteColumnTreeVO();
				scTree.setSiteColumn(siteColumn);
				scTree.setList(new ArrayList<SiteColumnTreeVO>());
				scTree.setLevel(1);	//顶级栏目，1级栏目
				columnTreeMap.put(siteColumn.getCodeName(), scTree);
			}
		}
		//然后，再遍历父栏目，将二级栏目拿出来
		for (int i = 0; i < siteColumnList.size(); i++) {
			SiteColumn siteColumn = siteColumnList.get(i);
			//判断是否有上级栏目，根据父栏目代码，如果有的话，那就是子栏目了，符合
			if(siteColumn.getParentCodeName() != null && siteColumn.getParentCodeName().length() > 0){
				SiteColumnTreeVO scTree = new SiteColumnTreeVO();
				scTree.setSiteColumn(siteColumn);
				scTree.setList(new ArrayList<SiteColumnTreeVO>());
				scTree.setLevel(2);	//子栏目，二级栏目
				if(columnTreeMap.get(siteColumn.getParentCodeName()) != null){
					columnTreeMap.get(siteColumn.getParentCodeName()).getList().add(scTree);
				}else{
					//没有找到该子栏目的父栏目
				}
			}
		}
		
		
		/*
		 * 栏目树取完了，接着进行对栏目树内，有子栏目的父栏目，进行信息汇总,将子栏目的信息列表，都合并起来，汇总成一个父栏目的
		 */
		//对文章-父栏目进行分类，以栏目codeName为key，将每个子栏目的文章加入进总的所属的父栏目的List中，然后进行排序
		Map<String, List<com.xnx3.wangmarket.admin.bean.News>> columnTreeNewsMap = new HashMap<String, List<com.xnx3.wangmarket.admin.bean.News>>();
		for (Map.Entry<String, SiteColumnTreeVO> entry : columnTreeMap.entrySet()) {
			SiteColumnTreeVO sct = entry.getValue();
			if(sct.getList().size() > 0){
				//有子栏目，才会对其进行数据汇总
				columnTreeNewsMap.put(sct.getSiteColumn().getCodeName(), new ArrayList<com.xnx3.wangmarket.admin.bean.News>());
				
				//遍历其子栏目，将每个子栏目的News信息合并在一块，供父栏目直接调用
				for (int i = 0; i < sct.getList().size(); i++) {
					SiteColumnTreeVO subSct = sct.getList().get(i);	//子栏目的栏目信息
					
					//v4.7版本更新，增加判断，只有栏目类型是列表页面的，才会将子栏目的信息合并入父栏目。
					if(subSct.getSiteColumn().getType() - SiteColumn.TYPE_LIST == 0){
						//将该栏目的News文章，创建一个新的List
						List<com.xnx3.wangmarket.admin.bean.News> nList = new ArrayList<com.xnx3.wangmarket.admin.bean.News>(); 
						List<News> oList = columnNewsMap.get(subSct.getSiteColumn().getCodeName());
						for (int j = 0; j < oList.size(); j++) {
							com.xnx3.wangmarket.admin.bean.News n = new com.xnx3.wangmarket.admin.bean.News();
							News news = oList.get(j);
							n.setNews(news);
							n.setRank(news.getId());
							nList.add(n);
						}
						
						//将新的List，合并入父栏目CodeName的List
						columnTreeNewsMap.get(sct.getSiteColumn().getCodeName()).addAll(nList);
					}
				}
			}
		}
		//合并完后，对每个父栏目的List进行先后顺序排序
		for (Map.Entry<String, SiteColumnTreeVO> entry : columnTreeMap.entrySet()) {
			SiteColumnTreeVO sct = entry.getValue();
			if(sct.getList().size() > 0){
//				Collections.sort(columnTreeNewsMap.get(sct.getSiteColumn().getCodeName()));
				Collections.sort(columnTreeNewsMap.get(sct.getSiteColumn().getCodeName()), new Comparator<com.xnx3.wangmarket.admin.bean.News>() {
		            public int compare(com.xnx3.wangmarket.admin.bean.News n1, com.xnx3.wangmarket.admin.bean.News n2) {
		                if(sct.getSiteColumn().getListRank() != null && sct.getSiteColumn().getListRank() - SiteColumn.LIST_RANK_ADDTIME_ASC == 0){
		                	//按照发布时间正序排序，发布时间越早，排列越靠前
		                	return n2.getNews().getAddtime() - n1.getNews().getAddtime();
		                }else{
		                	//按照发布时间倒序排序，发布时间越晚，排列越靠前
		                	return n1.getNews().getAddtime() - n2.getNews().getAddtime();
		                }
		            }
		        });
				
			}
		}
		//排序完后，将其取出，加入columnNewsMap中，供模版中动态调用父栏目代码，就能直接拿到其的所有子栏目信息数据
		for (Map.Entry<String, SiteColumnTreeVO> entry : columnTreeMap.entrySet()) {
			SiteColumnTreeVO sct = entry.getValue();
			if(sct.getList().size() > 0){
				List<com.xnx3.wangmarket.admin.bean.News> nList = columnTreeNewsMap.get(sct.getSiteColumn().getCodeName());
				for (int i = nList.size()-1; i >= 0 ; i--) {
					columnNewsMap.get(sct.getSiteColumn().getCodeName()).add(nList.get(i).getNews());
				}
			}
		}
		
		
		/*
		 * sitemap.xml
		 */
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<urlset\n"
				+ "\txmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"\n"
				+ "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "\txsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9\n"
				+ "\t\thttp://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">\n";
		
		//加入首页
		String indexUrl = "http://"+Func.getDomain(site);
		xml = xml + getSitemapUrl(indexUrl, "1.00");
		
		/*
		 * 模版替换生成页面的步骤
		 * 1.替换模版变量的标签
		 * 		1.1 通用标签
		 * 		1.2 动态栏目调用标签	（这里要将动态调用标签最先替换。不然动态调用标签非常可能会生成列表，会增加替换通用标签的数量，所以先替换通用标签后，在替换动态模版调用标签）
		 * 2.替换列表页模版、详情页模版的标签
		 * 		2.1 通用标签
		 * 		2.2 动态栏目调用标签
		 * 		2.3 模版变量装载
		 * 
		 * 
		 * 分支－－1->生成首页
		 * 分支－－2->生成栏目列表页、详情页
		 * 分支－－2->生成sitemap.xml
		 */
		
		//1.替换模版变量的标签，并在替换完毕后将其加入Session缓存
//		Map<String, String> varMap = new HashMap<String, String>();
//		Map<String, String> varMap = Func.getUserBeanForShiroSession().getTemplateVarDataMapForOriginal();
		
		//v2.24，将模版变量的比对，改为模版页面
		TemplatePageListVO tplVO = templateService.getTemplatePageListByCache(request);
		if(tplVO == null || tplVO.getList().size() == 0){
			vo.setBaseVO(BaseVO.FAILURE, "当前网站尚未选择/导入/增加模版，生成失败！网站有模版后才能根据模版生成整站！");
			return vo;
		}
		
		//当网站只有一个首页时，是不需要这个的。所以只需要上面的，判断一下是否有模版页就够了。 v2.24更新
		if(Func.getUserBeanForShiroSession().getTemplateVarMapForOriginal() != null){	//v4.7加入，避免只有一个首页时，生成整站第一次报错
			
		}
		for (Map.Entry<String, TemplateVarVO> entry : Func.getUserBeanForShiroSession().getTemplateVarMapForOriginal().entrySet()) {  
			//替换公共标签
			String v = template.replacePublicTag(entry.getValue().getTemplateVarData().getText());
			//替换栏目的动态调用标签
			v = template.replaceSiteColumnBlock(v, columnNewsMap, columnMap, columnTreeMap, true, null, newsDataMap);	
			Func.getUserBeanForShiroSession().getTemplateVarCompileDataMap().put(entry.getKey(), v);
		}
		
		/*
		 * 进行第二步，对列表页模版、详情页模版进行通用标签等的替换，将其处理好。等生成的时候，直接取出来替换news、column即可
		 */
		TemplatePageListVO tpl = templateService.getTemplatePageListByCache(request);	//取缓存中的模版页列表
		Map<String, String> templateCacheMap = new HashMap<String, String>();	//替换好通用标签等的模版都缓存入此。Map<templatePage.name, templatePageData.text>
		for (int i = 0; i < tpl.getList().size(); i++) {
			TemplatePageVO tpVO = tpl.getList().get(i);
			String text = null;	//模版页内容
			if(tpVO.getTemplatePageData() == null){
				//若缓存中没有缓存上模版页详情，那么从数据库中挨个取出来（暂时先这么做，反正RDS剩余。后续将执行单挑SQL将所有页面一块拿出来再分配）（并且取出来后，要加入缓存，之后在点击生成整站，就不用再去数据库取了）
				TemplatePageData tpd = sqlDAO.findById(TemplatePageData.class, tpVO.getTemplatePage().getId());
				if(tpd != null){
					text = tpd.getText();
				}
			}else{
				text = tpVO.getTemplatePageData().getText();
			}
			
			if(text == null){
				vo.setBaseVO(BaseVO.FAILURE, "模版页"+tpVO.getTemplatePage().getName()+"的内容不存在！请先检查此模版页");
				return vo;
			}
			
			//进行2.1、2.2、2.3预操作
			//替换公共标签
			text = template.replacePublicTag(text);
			//替换栏目的动态调用标签
			text = template.replaceSiteColumnBlock(text, columnNewsMap, columnMap, columnTreeMap, true, null, newsDataMap);	
			//装载模版变量
			text = template.assemblyTemplateVar(text);
			
			//预处理后，将其缓存入Map，等待生成页面时直接获取
			templateCacheMap.put(tpVO.getTemplatePage().getName(), text);
		}
		//最后还要将获得到的内容缓存入Session，下次就不用去数据库取了

		
		//生成首页
		String indexHtml = templateCacheMap.get(templatePageIndexVO.getTemplatePage().getName());
		//替换首页中存在的栏目的动态调用标签
		indexHtml = template.replaceSiteColumnBlock(indexHtml, columnNewsMap, columnMap, columnTreeMap, true, null, newsDataMap);
		indexHtml = template.replacePublicTag(indexHtml);	//替换公共标签
		//生成首页保存到OSS或本地盘
		AttachmentFile.putStringFile("site/"+site.getId()+"/index.html", indexHtml);
		
		/*
		 * 生成栏目、内容页面
		 */
		//遍历出所有列表栏目
		for (SiteColumn siteColumn : columnMap.values()) {
			if(siteColumn.getCodeName() == null || siteColumn.getCodeName().length() == 0){
				vo.setBaseVO(BaseVO.FAILURE, "栏目["+siteColumn.getName()+"]的“栏目代码”不存在，请先为其设置栏目代码");
				return vo;
			}
			//取得当前栏目下的News列表
			List<News> columnNewsList = columnNewsMap.get(siteColumn.getCodeName());
			
			//获取当前栏目的内容页模版
			String viewTemplateHtml = templateCacheMap.get(siteColumn.getTemplatePageViewName());
			if(viewTemplateHtml == null){
				vo.setBaseVO(BaseVO.FAILURE, "栏目["+siteColumn.getName()+"]未绑定页面内容模版，请去绑定");
				return vo;
			}
			//替换内容模版中的动态栏目调用(动态标签引用)
			viewTemplateHtml = template.replaceSiteColumnBlock(viewTemplateHtml, columnNewsMap, columnMap, columnTreeMap, false, siteColumn, newsDataMap);	
			
			//如果是新闻或者图文列表，那么才会生成栏目列表页面
			if(siteColumn.getType() - SiteColumn.TYPE_LIST == 0 || siteColumn.getType() - SiteColumn.TYPE_NEWS == 0 || siteColumn.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
				//当前栏目的列表模版
				String listTemplateHtml = templateCacheMap.get(siteColumn.getTemplatePageListName());
				if(listTemplateHtml == null){
					vo.setBaseVO(BaseVO.FAILURE, "栏目["+siteColumn.getName()+"]未绑定模版列表页面，请去绑定");
					return vo;
				}
				//替换列表模版中的动态栏目调用(动态标签引用)
				listTemplateHtml = template.replaceSiteColumnBlock(listTemplateHtml, columnNewsMap, columnMap, columnTreeMap, false, siteColumn, newsDataMap);	
				
				
				//生成其列表页面
				template.generateListHtmlForWholeSite(listTemplateHtml, siteColumn, columnNewsList, newsDataMap);
				
				//XML加入栏目页面
				xml = xml + getSitemapUrl(indexUrl+"/"+template.generateSiteColumnListPageHtmlName(siteColumn, 1)+".html", "0.4");
				
				/*
				 * 生成当前栏目的内容页面
				 */
				//判断栏目属性中，是否设置了生成内容详情页面, v4.7增加
				if(siteColumn.getUseGenerateView() == null || siteColumn.getUseGenerateView() - SiteColumn.USED_ENABLE == 0){
					for (int i = 0; i < columnNewsList.size(); i++) {
						News news = columnNewsList.get(i);
						
						if(siteColumn.getId() - news.getCid() == 0){
							//当前文章是此栏目的，那么生成文章详情。不然是不生成的，免得在父栏目中生成子栏目的页面，导致siteColumn调用出现错误
							//列表页的内容详情页面，还会有上一篇、下一篇的功能
							News upNews = null;
							News nextNews = null;
							if(i > 0){
								upNews = columnNewsList.get(i-1);
							}
							if((i+1) < columnNewsList.size()){
								nextNews = columnNewsList.get(i+1);
							}
							//生成内容页面
							template.generateViewHtmlForTemplateForWholeSite(news, siteColumn, newsDataMap.get(news.getId()), viewTemplateHtml, upNews, nextNews);
							//XML加入内容页面
							xml = xml + getSitemapUrl(indexUrl+"/"+template.generateNewsPageHtmlName(siteColumn, news)+".html", "0.5");
						}
					}
				}
				
			}else if(siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
				//独立页面，只生成内容模版
				if(siteColumn.getEditMode() - SiteColumn.EDIT_MODE_TEMPLATE == 0){
					//模版式编辑，无 news ， 则直接生成
					template.generateViewHtmlForTemplateForWholeSite(null, siteColumn, new NewsDataBean(null), viewTemplateHtml, null, null);
					//独立页面享有更大的权重，赋予其 0.8
					xml = xml + getSitemapUrl(indexUrl+"/"+template.generateNewsPageHtmlName(siteColumn, null)+".html", "0.8");
				}else{
					//UEditor、输入模型编辑方式
					for (int i = 0; i < columnNewsList.size(); i++) {
						News news = columnNewsList.get(i);
						template.generateViewHtmlForTemplateForWholeSite(news, siteColumn, newsDataMap.get(news.getId()), viewTemplateHtml, null, null);
						//独立页面享有更大的权重，赋予其 0.8
						xml = xml + getSitemapUrl(indexUrl+"/"+template.generateNewsPageHtmlName(siteColumn, news)+".html", "0.8");
					}
				}
			}else{
				//其他栏目不管，当然，也没有其他类型栏目了，v4.6版本更新后，CMS模式一共就这两种类型的
			}
		} 
		
		//生成 sitemap.xml
		xml = xml + "</urlset>";
		AttachmentFile.putStringFile("site/"+site.getId()+"/sitemap.xml", xml);
		
		return new BaseVO();
	}
	
	public TemplateCommon getTemplateCommonHtml(Site site, String title, Model model) {
		if(site == null){
			site = new Site();
			site.setId(0);
			site.setName("");
			site.setTemplateId(1);
		}
		
		TemplateCommon tc = new TemplateCommon();
		
		GenerateHTML gh = new GenerateHTML(site);
		Template temp = new Template(site);
		gh.setEditMode(true);
		
		String headHtml = FileUtil.read(Global.getProjectPath()+"/static/template/"+gh.templateId+"/common/head.html");
		headHtml = gh.replacePublicTag(headHtml);
		headHtml = headHtml.replaceAll(GenerateHTML.regex("title"), title);
		headHtml = temp.replaceForEditModeTag(headHtml);
		
		String topHtml = FileUtil.read(Global.getProjectPath()+"/static/template/"+gh.templateId+"/common/top.html");
		topHtml = gh.replacePublicTag(topHtml);
		topHtml = topHtml.replaceAll(GenerateHTML.regex("title"), title);
		topHtml = temp.replaceForEditModeTag(topHtml);
		
		String footHtml = FileUtil.read(Global.getProjectPath()+"/static/template/"+gh.templateId+"/common/foot.html");
		footHtml = gh.replacePublicTag(footHtml);
		footHtml = temp.replaceForEditModeTag(footHtml);
		
		model.addAttribute("headHtml", headHtml);
		model.addAttribute("topHtml", topHtml);
		model.addAttribute("footHtml", footHtml);
		
		return tc;
	}

	public SiteVO saveSite(Site s, int siteUserId, HttpServletRequest request) {
		SiteVO baseVO = new SiteVO();
		String name = "";
		if(s.getName() != null && s.getName().length()>0){
			name = Safety.filter(s.getName());
		}
		if(name.length() == 0){
			baseVO.setBaseVO(BaseVO.FAILURE, "您的站点叫什么名字呢?");
			return baseVO;
		}
		
		Site site;
		if(s.getId() != null && s.getId() > 0){
			site = sqlDAO.findById(Site.class, s.getId());
			if(site == null){
				baseVO.setBaseVO(BaseVO.FAILURE, "要编辑的站点不存在");
				return baseVO;
			}
			if(site.getUserid() != siteUserId){
				baseVO.setBaseVO(BaseVO.FAILURE, "站点不属于您，无法操作！");
				return baseVO;
			}
		}else{
			site = new Site();
			site.setAddtime(DateUtil.timeForUnix10());
			site.setUserid(siteUserId);
			site.setDomain(siteUserId+DateUtil.currentDate("mmss"));
		}
		
		site.setAddress(Safety.filter(s.getAddress()));
		site.setCompanyName(Safety.filter(s.getCompanyName()));
		site.setUsername(Safety.filter(s.getUsername()));
		site.setName(name);
		site.setmShowBanner(s.getmShowBanner());
		site.setPhone(Safety.filter(s.getPhone()));
		site.setQq(Safety.filter(s.getQq()));
		site.setBindDomain(s.getBindDomain()==null? "":Safety.filter(s.getBindDomain()));
		site.setExpiretime(s.getExpiretime());
		
		if(s.getClient() - Site.CLIENT_PC == 0){
			//通用模版，电脑站
			site.setClient(s.getClient());
		}else if(s.getClient() - Site.CLIENT_CMS == 0){
			//高级自定义模版CMS
			site.setTemplateName(Safety.filter(s.getTemplateName()));
			site.setClient(s.getClient());
		}else{
			//剩下的都是通用模版，手机站
			site.setClient(Site.CLIENT_WAP);
		}
		
		
		//非CMS类型，将会赋予其一个模版
		if(s.getClient() - Site.CLIENT_CMS != 0){
			if(s.getTemplateId() != null && s.getTemplateId() > 0){
				site.setTemplateId(s.getTemplateId());
			}else if(s.getId() == null || s.getId() == 0) {
				//如果是新增，默认添加一个模版
				if(site.getClient() - Site.CLIENT_PC == 0){
					site.setTemplateId(G.TEMPLATE_PC_DEFAULT);
				}else{
					site.setTemplateId(G.TEMPLATE_WAP_DEFAULT);
				}
			}
			site.setTemplateName("");  //避免sqlite 数据库中为null
		}else{
			//CMS类型，填充一些数据
			site.setTemplateId(0);
		}
		
		//若没有设置关键词，自动创建关键词
		String keywords = s.getKeywords();
		if(keywords == null || keywords.length() == 0){
			keywords = site.getName();
			if(site.getCompanyName() != null && !site.getName().equals(site.getCompanyName()) && site.getCompanyName().length() > 0){
				keywords = keywords + "," + site.getCompanyName();
			}
			if(site.getUsername() != null && !site.getName().equals(site.getUsername()) && site.getUsername().length() > 0){
				keywords = keywords + "," + site.getUsername();
			}
		}
		site.setKeywords(Safety.filter(keywords));		
		
		//填充默认数据
		if(site.getLogo() == null){
			site.setLogo("");
		}
		if(site.getColumnId() == null){
			site.setColumnId("");
		}
		if(site.getUseKefu() == null){
			site.setUseKefu((short) 0);
		}
		if(site.getAboutUsCid() == null){
			site.setAboutUsCid(0);
		}
		
		sqlDAO.save(site);
		if(site.getId() > 0){
			List<SiteColumn> columnList = new ArrayList<SiteColumn>();
			
			if(s.getId() > 0){
				//看是否是新增加的站点，若只是修改
				
				if(s.getClient() - Site.CLIENT_CMS == 0){
					//CMS类型，无需变动
				}else{
					//通用模版，要有刷新
					
					//修改,只需要变化首页即可
					generateSiteIndex(site);
				}
			}else{
				//新增站点
				
				if(s.getClient() - Site.CLIENT_CMS == 0){
					/*
					 * v2.7更改，创建CMS时，只是创建一个空白的CMS网站。模版的选择在登录某个CMS网站后进行操作
					 */
				}else{
					//如果是通用网站模版
					
					//新增
					
					//站点创建完毕后，要给站点默认设置description首页描述
					String description = site.getName();
					if(site.getCompanyName() != null && !site.getName().equals(site.getCompanyName()) && site.getCompanyName().length() > 0){
						description = description + "，隶属于" + site.getCompanyName();
					}
					if(site.getAddress() != null && !site.getName().equals(site.getAddress()) && site.getAddress().length() > 0){
						description = description + "，地理位置位于" + site.getAddress();
					}
					if(site.getUsername() != null && !site.getName().equals(site.getUsername()) && site.getUsername().length() > 0){
						description = description + "，业务负责人：" + site.getUsername();
					}
					if(site.getPhone() != null && site.getPhone().length() > 0){
						description = description + "，联系电话：" + site.getPhone();
					}
					SiteData siteData = new SiteData();
					siteData.setId(site.getId());
					siteData.setIndexDescription(description);
					sqlDAO.save(siteData);
					
					//站点创建完毕后，默认给站点添加一个导航栏目：关于我们
					SiteColumn sc = new SiteColumn();
//					sc.setIcon(G.RES_CDN_DOMAIN+"default_image/aboutUs.jpg");
					sc.setIcon(Global.get("MASTER_SITE_URL")+"default/aboutUs.jpg");
					sc.setName("关于我们");
					sc.setParentid(0);
					sc.setRank(1);
					sc.setSiteid(site.getId());
					sc.setType(SiteColumn.TYPE_PAGE);
					sc.setUsed(SiteColumn.USED_ENABLE);
					sc.setUserid(siteUserId);
					sqlDAO.save(sc);
					
					//默认给导航栏目：关于我们，创建一个独立页面
					News news = new News();
					news.setAddtime(DateUtil.timeForUnix10());
					news.setCid(sc.getId());
					news.setIntro(sc.getName());
					news.setSiteid(site.getId());
					news.setStatus(News.STATUS_NORMAL);
					news.setTitle(sc.getName());
//					news.setTitlepic(G.RES_CDN_DOMAIN+"default_image/aboutUs.jpg");
					news.setTitlepic(Global.get("MASTER_SITE_URL")+"default/aboutUs.jpg");
					
					news.setType(News.TYPE_PAGE);
					news.setUserid(siteUserId);
					news.setLegitimate(News.LEGITIMATE_OK);
					sqlDAO.save(news);
					NewsData newsData = new NewsData(news.getId(), news.getIntro());
					sqlDAO.save(newsData);
					
					//默认给站点添加一个Banner轮播图,创建轮播图数据js缓存
					List<Carousel> carouselList = new ArrayList<Carousel>();
					Carousel carousel = new Carousel();
					carousel.setAddtime(DateUtil.timeForUnix10());
//					carousel.setImage(AttachmentFile.netUrl()+"res/default_carousel/car_1.jpg");
					carousel.setImage(AttachmentFile.netUrl()+"default/banner.jpg");
					carousel.setIsshow(Carousel.ISSHOW_SHOW);
					carousel.setSiteid(site.getId());
					carousel.setUserid(siteUserId);
					carousel.setUrl("");
					carousel.setType(Carousel.TYPE_DEFAULT_PAGEBANNER);
					sqlDAO.save(carousel);
					carouselList.add(carousel);
					new com.xnx3.wangmarket.admin.cache.Site().carousel(carouselList,site);	//创建轮播图缓存数据
					
					//将新增加的关于我们的SiteColumn.id更新入Site中
					site.setAboutUsCid(sc.getId());
					sqlDAO.save(site);
					
					if(site.getClient() == Site.CLIENT_PC){
						/****        PC start      ***/
						//生成PC首页
						GenerateHTML gh = new GenerateHTML(site);
						Template temp = new Template(site);
						String index = gh.pcIndex();		//获取到首页的模版，替换掉common的
						
						//生成新闻栏目
						SiteColumn newsSiteColumn = new SiteColumn();
						newsSiteColumn.setIcon(AttachmentFile.netUrl()+"image/default_newsColumn.png");
						newsSiteColumn.setName("新闻咨询");
						newsSiteColumn.setParentid(0);
						newsSiteColumn.setRank(2);
						newsSiteColumn.setSiteid(site.getId());
						newsSiteColumn.setType(SiteColumn.TYPE_NEWS);
						newsSiteColumn.setUrl("");
						newsSiteColumn.setUsed(SiteColumn.USED_ENABLE);
						newsSiteColumn.setUserid(site.getUserid());
						sqlDAO.save(newsSiteColumn);
						//给新闻栏目默认添加一条欢庆信息
						News xwNews = new News();
						xwNews.setAddtime(DateUtil.timeForUnix10());
						xwNews.setCid(newsSiteColumn.getId());
						xwNews.setSiteid(site.getId());
						xwNews.setStatus(News.STATUS_NORMAL);
						xwNews.setTitle("热烈庆祝"+site.getName()+"网站成立");
						xwNews.setIntro(xwNews.getTitle()+",各族人民前来欢庆。");
						xwNews.setTitlepic(AttachmentFile.netUrl()+"image/default_news_titlepic.png");
						xwNews.setType(News.TYPE_NEWS);
						xwNews.setUserid(siteUserId);
						xwNews.setLegitimate(News.LEGITIMATE_OK);
						sqlDAO.save(xwNews);
						NewsData xwNewsData = new NewsData(xwNews.getId(), xwNews.getIntro());
						sqlDAO.save(xwNewsData);
						
						//加入 Site.columnId
						site.setColumnId(getSiteColumnId(newsSiteColumn, site));
						
						//生成产品展示栏目
						SiteColumn imageSiteColumn = new SiteColumn();
						imageSiteColumn.setIcon(AttachmentFile.netUrl()+"image/default_project_column.png");
						imageSiteColumn.setName("产品展示");
						imageSiteColumn.setParentid(0);
						imageSiteColumn.setRank(3);
						imageSiteColumn.setSiteid(site.getId());
						imageSiteColumn.setType(SiteColumn.TYPE_IMAGENEWS);
						imageSiteColumn.setUrl("");
						imageSiteColumn.setUsed(SiteColumn.USED_ENABLE);
						imageSiteColumn.setUserid(site.getUserid());
						sqlDAO.save(imageSiteColumn);
						//给产品展示栏目默认添加一条欢庆信息
						News productNews = new News();
						productNews.setAddtime(DateUtil.timeForUnix10());
						productNews.setCid(imageSiteColumn.getId());
						productNews.setSiteid(site.getId());
						productNews.setStatus(News.STATUS_NORMAL);
						productNews.setTitle("热烈庆祝"+site.getName()+"网站成立");
						productNews.setIntro(xwNews.getTitle()+",各族人民前来欢庆。");
						productNews.setTitlepic(AttachmentFile.netUrl()+"image/default_news_titlepic.png");
						productNews.setType(News.TYPE_IMAGENEWS);
						productNews.setUserid(siteUserId);
						productNews.setLegitimate(News.LEGITIMATE_OK);
						sqlDAO.save(productNews);
						NewsData productNewsData = new NewsData(productNews.getId(), productNews.getIntro());
						sqlDAO.save(productNewsData);
						
						//加入 Site.columnId
						site.setColumnId(getSiteColumnId(imageSiteColumn, site));
						site.setDomain("s"+site.getId());
						sqlDAO.save(site);
						
						//生成联系我们
						SiteColumn lxSC = new SiteColumn();
						lxSC.setIcon(AttachmentFile.netUrl()+"image/lianxi.jpeg");
						lxSC.setName("联系我们");
						lxSC.setParentid(0);
						lxSC.setRank(4);
						lxSC.setSiteid(site.getId());
						lxSC.setType(SiteColumn.TYPE_PAGE);
						lxSC.setUsed(SiteColumn.USED_ENABLE);
						lxSC.setUserid(siteUserId);
//						lxSC.setClient(SiteColumn.CLIENT_PC);
						sqlDAO.save(lxSC);
						//默认给导航栏目：联系我们，创建一个独立页面
						News lxNews = new News();
						lxNews.setAddtime(DateUtil.timeForUnix10());
						lxNews.setCid(lxSC.getId());
						lxNews.setIntro(lxSC.getName());
						lxNews.setSiteid(site.getId());
						lxNews.setStatus(News.STATUS_NORMAL);
						lxNews.setTitle(lxSC.getName());
						lxNews.setTitlepic(lxSC.getIcon());
						lxNews.setType(News.TYPE_PAGE);
						lxNews.setUserid(siteUserId);
						lxNews.setLegitimate(News.LEGITIMATE_OK);
						sqlDAO.save(lxNews);
						NewsData lxNewsData = new NewsData(lxNews.getId(), lxNews.getIntro());
						sqlDAO.save(lxNewsData);
						
						//刷新栏目js数据以及排序
						siteColumnService.resetColumnRankAndJs(site);
						
						/***     PC end     ****/
					}else{
						//WAP
						columnList.add(sc);
						new com.xnx3.wangmarket.admin.cache.Site().siteColumn(columnList,site); //siteColumn.js
						new com.xnx3.wangmarket.admin.cache.Site().siteColumnRank(site, sc.getId()+"");	//siteColumnRank.js
					}
					
					//更新当前Session的缓存Site的信息
					Func.getUserBeanForShiroSession().setSite(site);
					
					//新增需要刷新全站，生成所有页面
					refreshSiteGenerateHtml(request);
				}
				
				//刷新增加域名缓存
				//更新域名服务器
				MQBean mqBean = new MQBean();
				mqBean.setType(MQBean.TYPE_NEW_SITE);
				mqBean.setSimpleSite(new SimpleSite(site));
				updateDomainServers(mqBean);
			}
			
			//更新当前Session缓存。如果是api接口开通网站，session是空的。所以要加null判断
			if(Func.getUserBeanForShiroSession() != null){
				Func.getUserBeanForShiroSession().setSite(site);
			}
			
			//创建数据js缓存
			new com.xnx3.wangmarket.admin.cache.Site().site(site, imService.getImByCache());				//site.js
			
			baseVO.setBaseVO(BaseVO.SUCCESS, s.getId()>0? "保存网站成功！":"创建网站成功！");
			baseVO.setSite(site);
			return baseVO;
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, "保存失败！");
			return baseVO;
		}
	}
	
	public String getSiteColumnId(SiteColumn sc, Site site){
		//修改Site.column_id
		//若栏目为新闻或者图文列表，且Site表里没有记录该栏目，则将该栏目的id加入Site表的栏目id中
		if(sc.getType() - SiteColumn.TYPE_IMAGENEWS == 0 || sc.getType() - SiteColumn.TYPE_NEWS == 0){
			//判断该栏目的状态是否是启用状态，若是启用状态，需要将该栏目的id加入Site表
			if(sc.getUsed() - SiteColumn.USED_ENABLE == 0){
				if(site.getColumnId() == null || site.getColumnId().indexOf(","+sc.getId()+",") == -1){	
					if(site.getColumnId() == null){
						site.setColumnId(","+sc.getId()+",");
					}else{
						site.setColumnId(site.getColumnId()+sc.getId()+",");
					}
					if(site.getColumnId().length() < 80){
						return site.getColumnId();
					}
				}
			}else{
				//若是状态不启用，需要检查Site表中是否有此栏目的存储，若有，需要将其去掉
				if(site.getColumnId() != null && site.getColumnId().indexOf(","+sc.getId()+",") > -1){
					site.setColumnId(site.getColumnId().replaceAll(","+sc.getId()+",", ","));
					return site.getColumnId();
				}
			}
		}
		return null;
	}

//	public SiteVO updateWapTemplateRefreshHtml(Site site, HttpServletRequest request) {
//		SiteVO siteVO = saveSite(site,site.getUserid(), request);
//		if(siteVO.SUCCESS == siteVO.getResult()){
//			//因为以前注册的站点模版没变过来，暂时是刷新全站
//			refreshSiteGenerateHtml(request, site);
//		}
//		return siteVO;
//	}

	public void generateSitePcIndex(Site site,SiteColumn aboutUsColumn, SiteColumn newsColumn, SiteColumn imagesColumn,SiteData siteData) {
		GenerateHTML gh = new GenerateHTML(site);
		
		String html = gh.pcIndex();
		//关于我们
		html = html.replaceAll("<!--Index_Content_AboutUs_Start--><!--id=0-->", "<!--Index_Content_AboutUs_Start--><!--id="+(aboutUsColumn == null? "0":""+aboutUsColumn.getId())+"-->");
		//新闻
		html = html.replaceAll("<!--Index_Content_NewsList_Start--><!--id=0-->", "<!--Index_Content_NewsList_Start--><!--id="+(newsColumn == null ? "0":""+newsColumn.getId())+"-->");
		//图片
		html = html.replaceAll("<!--Index_Content_NewsImageList_Start--><!--id=0-->", "<!--Index_Content_NewsImageList_Start--><!--id="+(imagesColumn == null ? "0":""+imagesColumn.getId())+"-->");
		
		gh.generateIndexHtml(html, siteData);
	}

	public boolean isPcClient(Site site) {
		if(site.getClient() == null){
			return false;
		}else{
			if(site.getClient() == Site.CLIENT_PC){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public void refreshSiteMap(Site site){
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<urlset\n"
				+ "\txmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"\n"
				+ "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "\txsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9\n"
				+ "\t\thttp://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">\n";
		
		//加入首页
		String indexUrl = "";
		if(site.getBindDomain() != null && site.getBindDomain().length() > 3){
			indexUrl = "http://"+site.getBindDomain();
		}else{
			indexUrl = "http://"+site.getDomain()+".wang.market";
		}
		xml = xml + getSitemapUrl(indexUrl, "1.00");
		
		
		//新闻/图文栏目列表
		List<SiteColumn> siteColumnList = sqlDAO.findBySqlQuery("SELECT * FROM site_column WHERE siteid = "+site.getId()+" AND used = "+SiteColumn.USED_ENABLE, SiteColumn.class);
//		Map<Integer, SiteColumn> siteColumnMap = new HashMap<Integer, SiteColumn>();	//可以根据siteColumn.id取出siteColumn数据对象
		for (int i = 0; i < siteColumnList.size(); i++) {
			SiteColumn siteColumn = siteColumnList.get(i);
			
			//XML加入栏目页面
			xml = xml + getSitemapUrl(indexUrl+"/lc"+siteColumn.getId()+"_1.html", "0.4");
		}
		
		//生成内容页面
		List<News> listNews = sqlDAO.findBySqlQuery("SELECT news.* FROM news WHERE news.siteid = "+site.getId()+" AND news.status = "+News.STATUS_NORMAL+" ORDER BY news.id DESC", News.class);
		for (int i = 0; i < listNews.size(); i++) {
			News news = listNews.get(i);
			
			//XML加入内容页面
			if(news.getType() - News.TYPE_PAGE == 0){
				//独立页面享有更大的权重，赋予其 0.8
				xml = xml + getSitemapUrl(indexUrl+"/c"+news.getCid()+".html", "0.8");
			}else{
				//普通列表的内容页面，0.5
				xml = xml + getSitemapUrl(indexUrl+"/"+news.getId()+".html", "0.5");
			}
		}
		
		xml = xml + "</urlset>";
		
		//生成 sitemap.xml
		AttachmentFile.putStringFile("site/"+site.getId()+"/sitemap.xml", xml);
	}
	
	/**
	 * SiteMap生成的url项 
	 * @param loc
	 * @param priority 如 1.00 、 0.5
	 * @return
	 */
	private String getSitemapUrl(String loc, String priority){
		if(loc.indexOf("http:") == -1){
			loc = "http://"+loc;
		}
		return "<url>\n"
				+ "\t<loc>"+loc+"</loc>\n"
				+ "\t<priority>"+priority+"</priority>\n"
				+ "</url>\n";
	}

	public SiteVO findByIdForCurrentUser(int id) {
		SiteVO vo = new SiteVO();
		
		if(id == 0){
			vo.setBaseVO(SiteVO.FAILURE, "请传入要操作的站点编号");
			return vo;
		}
		Site site = sqlDAO.findById(Site.class, id);
		if(site == null){
			vo.setBaseVO(SiteVO.FAILURE, "站点不存在");
			return vo;
		}
		if(site.getUserid() - ShiroFunc.getUser().getId() != 0){
			vo.setBaseVO(SiteVO.FAILURE, "站点不属于您，您无法操作");
			return vo;
		}
		
		vo.setSite(site);
		return vo;
	}

	public SiteRemainHintVO getSiteRemainHint(Site site, Agency agency) {
		SiteRemainHintVO vo = new SiteRemainHintVO();
		
		//v2.25 有时候会出现agency为null的情况,如果这样，默认讲result 设为正常模式未到期，不会出现什么提示
		if(agency == null){
			vo.setResult(SiteRemainHintVO.SUCCESS);
			return vo;
		}
		
		vo.setCompanyName(agency.getName());
		vo.setPhone(agency.getPhone());
		vo.setQq(agency.getQq());
		
		//计算当前网站的过期时间，当网站过期时间低于两个月时，会出现即将到期的提示
		if(site.getExpiretime() != null && site.getExpiretime() > 0){
			//获取当前网站还有多久过期，获取剩余时间的秒数
			int remain = site.getExpiretime() - DateUtil.timeForUnix10();
			
			//当前时间 + 两个月以后的时间，于过期时间比较  //1 * 60 * 60 * 24 * 30 * 2 两个月
			if(remain < 5184000){
				//如果剩余时间不到连个月了，那么会有提示
				//计算还剩多久了
				if(remain < 1){
					vo.setResult(3);
					vo.setRemainTimeString("已到期");
				}else{
					vo.setResult(SiteRemainHintVO.FAILURE);
					vo.setRemainTimeString("还剩<span style=\"font-size: 38px; font-style: oblique; padding: 5px; color:red; padding-right: 12px;\">" + (int)Math.ceil(remain/86400) + "</span>天到期");
				}
			}
		}
		
		return vo;
	}

	public String getSiteRemainHintForJavaScript(Site site, Agency agency) {
		SiteRemainHintVO vo = getSiteRemainHint(site, agency);
		if(vo.getResult() - SiteRemainHintVO.FAILURE == 0){
			return 	  "<script type=\"text/javascript\">"
					+ "layer.open({ type: 1 , title: '网站即将到期，续费提示', offset: 'rb' ,content: '<div style=\"padding: 10px 30px; padding-bottom: 60px;\">网站" + vo.getRemainTimeString() + "！"
							+ "<br/>请联系"+agency.getName()+"续费"
							+ "<br/>联系电话："+agency.getPhone()
							+ "<br/>联系QQ："+agency.getQq()+""
							+ "<br/>所在地址："+agency.getAddress()+"</div>' ,shade: 0});"
					+ "</script>";
		}
		
		return "";
	}
	
	public void updateDomainServers(MQBean mqBean) {
//		SimpleSite ss = new SimpleSite(site);
//		JSONObject json = JSONObject.fromObject(ss);
		
		//先更新当前应用内存中的
//		com.xnx3.wangmarket.domain.G.putDomain(ss.getDomain(), ss);
//		if(ss.getBindDomain() != null && ss.getBindDomain().length() > 1){
//			com.xnx3.wangmarket.domain.G.putBindDomain(ss.getBindDomain(), ss);
//		}
		
		DomainMQ.send("domain", JSONObject.fromObject(mqBean).toString());
		
		//若用户使用了分布式，那么就要用MNS同步域名改动
//		if(com.xnx3.wangmarket.domain.G.domainMNSUtil != null){
//			com.xnx3.wangmarket.domain.G.domainMNSUtil.putMessage(com.xnx3.wangmarket.domain.G.mnsDomain_queueName, json.toString());
//		}
	}
	
}
