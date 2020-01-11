package com.xnx3.wangmarket.admin.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.xnx3.BaseVO;
import com.xnx3.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.wangmarket.admin.util.SessionUtil;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.cache.GenerateHTML;
import com.xnx3.wangmarket.admin.cache.Template;
import com.xnx3.wangmarket.admin.cache.TemplateCMS;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.pluginManage.interfaces.manage.NewsPluginManage;
import com.xnx3.wangmarket.admin.service.NewsService;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.service.TemplateService;
import com.xnx3.wangmarket.admin.util.TemplateUtil;
import com.xnx3.wangmarket.admin.vo.NewsVO;
import com.xnx3.wangmarket.admin.vo.bean.NewsInit;
import net.sf.json.JSONObject;

@Service("newsService")
public class NewsServiceImpl implements NewsService {

	@Resource
	private SqlDAO sqlDAO;
	@Resource
	private SiteService siteService;
	@Resource
	private TemplateService templateService;
	
	public void generateListHtml(Site site, SiteColumn siteColumn,List<News> newsList, HttpServletRequest request){
		int count = newsList.size();	//总条数
		Page page = new Page(count, G.PAGE_WAP_NUM);
		page.setUrlByStringUrl("");
		
		//如果是之前的通用模版，装载通用模版的配套方案
		String listHtml = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+site.getTemplateId()+"/"+(siteColumn.getType()==SiteColumn.TYPE_NEWS? "news":"newsimage")+"_list.html");
		String listItem = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+site.getTemplateId()+"/module/"+(siteColumn.getType()==SiteColumn.TYPE_NEWS? "news":"newsimage")+"_list_item.html");
		GenerateHTML gh = new GenerateHTML(site);
		listHtml = gh.assemblyCommon(listHtml);	//装载通用组件
		listHtml = gh.replacePublicTag(listHtml);		//替换通用标签
		
		
		//循环生成每个页面
		for (int i = 1; i <= page.getLastPageNumber() && count >= 0; i++) {
			page.setCurrentPageNumber(i);
			
			String currentListHtml = generateListHtml(listHtml, listItem, page, siteColumn, count, newsList, site, false);
			gh.generateListHtml(currentListHtml, siteColumn, i);
		}
	}
	
	
	public void generateListHtml(Site site, SiteColumn siteColumn) {
		List<News> list = sqlDAO.findBySqlQuery("SELECT * FROM news WHERE cid = "+siteColumn.getId() + " AND status = "+News.STATUS_NORMAL, News.class);
		int count = list.size();	//总条数
		Page page = new Page(count, G.PAGE_WAP_NUM);
		page.setUrlByStringUrl("");
		
		String listHtml = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+site.getTemplateId()+"/"+(siteColumn.getType()==SiteColumn.TYPE_NEWS? "news":"newsimage")+"_list.html");
		String listItem = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+site.getTemplateId()+"/module/"+(siteColumn.getType()==SiteColumn.TYPE_NEWS? "news":"newsimage")+"_list_item.html");
		GenerateHTML gh = new GenerateHTML(site);
		listHtml = gh.assemblyCommon(listHtml);	//装载通用组件
		listHtml = gh.replacePublicTag(listHtml);		//替换通用标签
		
		//循环生成每个页面
		for (int i = 1; i <= page.getLastPageNumber() && count >= 0; i++) {
			page.setCurrentPageNumber(i);
			
			String currentListHtml = generateListHtml(listHtml, listItem, page, siteColumn, count, list, site, false);
			gh.generateListHtml(currentListHtml, siteColumn, i);
		}
		
	}
	
	/**
	 * 编辑模式下的列表页面html生成
	 */
	public String generateListHtml(Page page, SiteColumn siteColumn, int count, List<News> list, Site site){
		String listHtml = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+site.getTemplateId()+"/"+(siteColumn.getType()==SiteColumn.TYPE_NEWS? "news":"newsimage")+"_list.html");
		String listItem = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+site.getTemplateId()+"/module/"+(siteColumn.getType()==SiteColumn.TYPE_NEWS? "news":"newsimage")+"_list_item.html");
		GenerateHTML gh = new GenerateHTML(site);
		gh.setEditMode(true);
		listHtml = gh.assemblyCommon(listHtml);	//装载通用组件
		listHtml = gh.replacePublicTag(listHtml);		//替换通用标签
		
		return generateListHtml(listHtml, listItem, page, siteColumn, count, list, site, true);
	}
	
	/**
	 * 生成当前List页面的HTML代码，编辑模式/正常模式通用底层
	 * @param listHtml 模版中以替换好通用标签的 html 代码
	 * @param page {@link Page}
	 * @param siteColumn {@link SiteColumn}
	 * @param count 列表一共有多少条
	 * @param list 当前列表页面的{@link News}列表
	 * @param site {@link Site}
	 * @param edit 是否是编辑模式下，true为编辑模式下
	 * @return 列表页面当前页的HTML代码
	 */
	private String generateListHtml(String listHtml, String listItem, Page page, SiteColumn siteColumn, int count, List<News> list, Site site, boolean edit){
		GenerateHTML gh = new GenerateHTML(site);
		gh.setEditMode(edit);
		String currentListHtml = gh.replaceListTag(listHtml,page,siteColumn);			//替换list专用标签,转为当前页面的html接收，不影响总体html
		//计算当前页面上显示多少条数据
		int currentShowNumber = page.getEveryNumber();
		if(page.getLastPageNumber() == page.getCurrentPageNumber()){
			//如果这是最后一页的话，极有可能这页的数据是不满足每页条数的，那只需要判断出当前页面有几条数据就可
			currentShowNumber = count - page.getLimitStart();
		}
		
		//循环生成页面中的新闻/图文列
		String itemsString = "";
		for (int j = 0; j < currentShowNumber; j++) {
			int indexJ = 0;
			if(edit){
				//编辑模式下
				indexJ = j;
			}else{
				//生成html列表模式下
				indexJ = ((page.getCurrentPageNumber()-1)*page.getEveryNumber())+j;
			}
			
			News news = list.get(indexJ);
			itemsString = itemsString + gh.replaceNewsListItem(listItem, news);
		}
		
		currentListHtml = currentListHtml.replaceAll(GenerateHTML.regex("listItem"), itemsString);
		
		if(edit){
			Template temp = new Template(site);
			currentListHtml = temp.replaceForEditModeTag(currentListHtml);
		}
		return currentListHtml;
	}

	public void generateViewHtml(Site site, News news, SiteColumn siteColumn, NewsDataBean newsDataBean, HttpServletRequest request) {
		if(Func.isCMS(site)){
			//如果使用的是新一代自定义模版，那么用新的生成模式
			generateViewHtmlForTemplate(site, news, siteColumn, newsDataBean, request);
		}else{
			//如果使用的是通用简单模版，直接使用原来的生成
			GenerateHTML gh = new GenerateHTML(site);
			gh.generateViewHtml(site, news, siteColumn, newsDataBean.getText());
		}
	}

	/**
	 * CMS模式，生成内容详情页面，News的页面，包含独立页面、新闻详情、图文详情。
	 * 这个可以其他网站调用别的网站生成，并非非得是当前登录网站
	 * @param site 要生成内容详情页面的站点。
	 * @param news 要生成的详情页的 {@link News}
	 * @param siteColumn 要生成的详情页所属的栏目 {@link SiteColumn}
	 * @param newsDataBean news_data 的整理及数据初始化
	 */
	public void generateViewHtmlForTemplate(Site site, News news, SiteColumn siteColumn, NewsDataBean newsDataBean, HttpServletRequest request) {
		//获取到当前页面使用的模版
		String templateHtml = templateService.getTemplatePageTextByCache(siteColumn.getTemplatePageViewName(), request);
		
//		Site site = SessionUtil.getSite();
		TemplateCMS template = new TemplateCMS(site, TemplateUtil.getTemplateByName(site.getTemplateName()));
		//template.generateViewHtmlForTemplate(news, siteColumn, newsDataBean, templateHtml, null, null);
		
		
		if(templateHtml == null){
			//出错，没有获取到该栏目的模版页
			return;
		}
//		String pageHtml = template.assemblyTemplateVar(templateHtml);	//装载模版变量
		String pageHtml = template.assemblyTemplateVar(templateHtml, templateService.getTemplateVarAndDataByDatabase(site).getCompileMap());	//装载模版变量
		pageHtml = template.replaceSiteColumnTag(pageHtml, siteColumn);	//替换栏目相关标签
		pageHtml = template.replacePublicTag(pageHtml);		//替换通用标签
		pageHtml = template.replaceNewsTag(pageHtml, news, siteColumn, newsDataBean);	//替换news相关标签
		
		//替换 SEO 相关
		pageHtml = pageHtml.replaceAll(Template.regex("title"), news.getTitle()+"_"+site.getName());
		pageHtml = pageHtml.replaceAll(Template.regex("keywords"), news.getTitle()+","+site.getKeywords());
		pageHtml = Template.replaceAll(pageHtml, Template.regex("description"), news.getIntro());
		
		template.generateNewsHtml(news, siteColumn, null, null, pageHtml, newsDataBean);
	}

	public NewsInit news(HttpServletRequest request, int id, int cid, Model model) {
		NewsInit n = new NewsInit();
		Site site = SessionUtil.getSite();
		
		News news = null;
		NewsData newsData = null;
		if(id > 0){
			news = sqlDAO.findById(News.class, id);
			if(news == null){
				n.setBaseVO(BaseVO.FAILURE, "要编辑的信息不存在");
				return n;
			}
			if(news.getSiteid() - site.getId() != 0){
				n.setBaseVO(BaseVO.FAILURE, "要编辑的信息不属于您，无法操作");
				return n;
			}
			
			cid = news.getCid();
			newsData = sqlDAO.findById(NewsData.class, news.getId());
			n.setPageTitle("修改信息");
		}else{
			news = new News();
			n.setPageTitle("新增信息");
		}
		
		SiteColumn siteColumn = null;
		if(cid == 0){
			n.setBaseVO(BaseVO.FAILURE, "请传入信息所属栏目");
			return n;
		}else{
			siteColumn = sqlDAO.findById(SiteColumn.class, cid);
			if(siteColumn == null){
				n.setBaseVO(BaseVO.FAILURE, "所属栏目不存在！");
				return n;
			}
			if(siteColumn.getSiteid() - site.getId() != 0){
				n.setBaseVO(BaseVO.FAILURE, "栏目不属于您，无法操作");
				return n;
			}
			news.setCid(siteColumn.getId());
			news.setType(siteColumn.getType());
			
			//type_list 是v4.6 针对CMS模式新增加的状态，以此替代原本的新闻信息、图文信息两种类型。这里是对以前版本的兼容，判断是属于哪种类型，将其设置到最新的
			if(Func.isCMS(site)){
				if(siteColumn.getType() - SiteColumn.TYPE_NEWS == 0){
					siteColumn.setEditUseText(SiteColumn.USED_ENABLE);
					siteColumn.setType(SiteColumn.TYPE_LIST);
					sqlDAO.save(siteColumn);
				}
				if(siteColumn.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
					siteColumn.setEditUseText(SiteColumn.USED_ENABLE);
					siteColumn.setEditUseTitlepic(SiteColumn.USED_ENABLE);
					siteColumn.setType(SiteColumn.TYPE_LIST);
					sqlDAO.save(siteColumn);
				}
				if(siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
					siteColumn.setEditUseText(SiteColumn.USED_ENABLE);
					siteColumn.setType(SiteColumn.TYPE_ALONEPAGE);
					sqlDAO.save(siteColumn);
				}
			}
			
			
			
			model.addAttribute("siteColumn",siteColumn);
		}
		n.setSiteColumn(siteColumn);
		
		n.setSite(site);
		if(id > 0){
			String titlepicImage = "";
			if(news.getTitlepic() != null && news.getTitlepic().length() > 0){
				if(news.getTitlepic().indexOf("http://") == 0 || news.getTitlepic().indexOf("https://") == 0 || news.getTitlepic().indexOf("//") == 0){
					titlepicImage = news.getTitlepic();
				}else{
					titlepicImage = AttachmentUtil.netUrl()+"site/"+site.getId()+"/news/"+news.getTitlepic();
				}
			}
			n.setTitlepicImage(titlepicImage);
			model.addAttribute("titlepicImage", "<img src=\""+titlepicImage+"\" height=\"30\" />");
			
			//v4.6增加 ，自定义扩展
			NewsDataBean newsDataBean = new NewsDataBean(newsData);
//			GenerateHTML gh = new GenerateHTML(site);
//			n.setNewsText(gh.restoreNewsText(newsData.getText()));
			model.addAttribute("text", newsDataBean.getText());
			JSONObject json = newsDataBean.getExtendJson();
			if(json != null){
				Iterator<String> it = json.keys(); 
				while(it.hasNext()){
					// 获得key
					String key = it.next(); 
					String value = json.getString(key);    
				}
			}
			n.setNewsDataBean(newsDataBean);
		}
		
		n.setNews(news);
		model.addAttribute("news", news);
		
		//设置上传后的图片、附件所在的个人路径
		SessionUtil.setUeUploadParam1(site.getId()+"");
		
		return n;
	}

	public NewsVO deleteNews(int id, boolean authCheck) {
		NewsVO baseVO = new NewsVO();
		if(id == 0){
			baseVO.setBaseVO(BaseVO.FAILURE, "请传入要删除信息的编号");
			return baseVO;
		}
		
		News news = sqlDAO.findById(News.class, id);
		if(news == null){
			baseVO.setBaseVO(BaseVO.FAILURE, "信息不存在");
			return baseVO;
		}
		
		Site site = SessionUtil.getSite();
		//需要验证此信息是本人发布
		if(authCheck){
			if(news.getSiteid() - site.getId() != 0){
				baseVO.setBaseVO(BaseVO.FAILURE, "信息不属于你，无权删除");
				return baseVO;
			}
		}
		//获取当前信息所属的栏目
		SiteColumn siteColumn = sqlDAO.findById(SiteColumn.class, news.getCid());
		if(siteColumn != null){
			if(siteColumn.getType() - SiteColumn.TYPE_PAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0){
				baseVO.setBaseVO(BaseVO.FAILURE, "该信息所属的栏目，栏目类型为独立页面，无法删除此信息。如果你想删除，可以删除其所属的栏目，此信息自然就没了");
				return baseVO;
			}
		}else{
			//如果siteColumn 为null，那么可能就是栏目已经被删除了，当然，这条信息自然也就可以删除掉的
		}
		sqlDAO.delete(news);
		
		NewsData newsData = sqlDAO.findById(NewsData.class, id);
		if(newsData == null){
			baseVO.setBaseVO(BaseVO.FAILURE, "信息内容不存在");
			return baseVO;
		}
		sqlDAO.delete(newsData);
		
		//删除titlepic文件
		if(news.getTitlepic() != null && news.getTitlepic().indexOf("http://") == -1){
			AttachmentUtil.deleteObject("site/"+news.getSiteid()+"/news/"+news.getTitlepic());
		}
		
		baseVO.setNews(news);
		return baseVO;
	}

	public String setText(String text, Site site) {
		GenerateHTML gh = new GenerateHTML(site);
		return gh.restoreNewsText(text);
	}


}
