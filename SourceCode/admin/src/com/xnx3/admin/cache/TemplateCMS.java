package com.xnx3.admin.cache;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.bean.TagA;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.Page;
import com.xnx3.admin.Func;
import com.xnx3.admin.G;
import com.xnx3.admin.entity.News;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.entity.SiteColumn;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.admin.vo.SiteColumnTreeVO;

/**
 * CMS模版
 * @author 管雷鸣
 */
public class TemplateCMS {
	private static Logger logger = Logger.getLogger(TemplateCMS.class);  
	
	private int linuxTime;	//当前时间戳
	private Site site;			//当前站点信息
	
	/**
	 * CMS模式网站，生成html页面时的命名规则，wang.market的兼容以前版本，255编号以前的使用int规则，免得影响了做SEO的站点。其他的，包括其他服务器或者其他代理使用此程序创建，都是用code规则即可
	 * int:通用模式的数字模式；
	 * code:CMS模式的根据栏目代码生成模式
	 */
	private String generateUrlRule = "int";	 
	
	private boolean editMode = false;	//当前是否为编辑模式，默认为false，不是编辑模式，为生成模式 
	
	/**
	 * 默认editMode为生成模式
	 * @param site
	 */
	public TemplateCMS(Site site) {
		linuxTime = DateUtil.timeForUnix10();
		this.site = site;
		
		if(Global.get("MASTER_SITE_URL") != null && Global.get("MASTER_SITE_URL").equals("http://wang.market/")){
			if(site.getId() - 255 > 0){
				//site.id < 255 的站点，是code模式
				generateUrlRule = "code";
			}
			if(site.getId() - 218 == 0){
				//site.id 218 是qiye1，作为调试使用
				generateUrlRule = "code";
			}
		}else{
			//后台主域名不是wang.market，那可能就是别人在使用这个程序，直接使用code模式生成html文件
			generateUrlRule = "code";
		}
		
	}

	/**
	 * @param editMode 是否为编辑模式
	 * 		<ul>
	 * 			<li>true：为编辑模式，编辑模版页面时使用，不会进行模版变量的通用标签替换等，调用模版变量会输出原始的保存的代码</li>
	 * 			<li>false：为生成模式，调用装载模版变量时，会一次性对其进行通用标签等替换，装载模版变量会生成新的替换好的代码</li>
	 * 		</ul>
	 * @param site
	 */
	public TemplateCMS(Site site, boolean editMode) {
		linuxTime = DateUtil.timeForUnix10();
//		this.templateId = site.getTemplateId();
		this.site = site;
		this.editMode = editMode;
	}
	
	
	/**
	 * 为模版替换为动态标签，替换公共/通用标签
	 * @param text 要替换的模版内容
	 * @return 替换好的内容
	 */
	public String replacePublicTag(String text){
		text = text.replaceAll(regex("OSSUrl"), AttachmentFile.netUrl());	//以废弃，保留，适应以前版本
		text = text.replaceAll(regex("AttachmentFileUrl"), AttachmentFile.netUrl());
		text = text.replaceAll(regex("resUrl"), G.RES_CDN_DOMAIN);
		text = text.replaceAll(regex("linuxTime"), linuxTime+"");
		text = text.replaceAll(regex("masterSiteUrl"), Global.get("MASTER_SITE_URL"));
		
		text = text.replaceAll(regex("siteId"), site.getId()+"");
		text = text.replaceAll(regex("siteDomain"), site.getDomain());
		text = text.replaceAll(regex("siteName"), site.getName());
		text = text.replaceAll(regex("siteTemplateId"), site.getTemplateId()+"");
		
		text = text.replaceAll(regex("site.name"), site.getName());
		text = text.replaceAll(regex("site.id"), site.getId()+"");
		text = text.replaceAll(regex("site.domain"), site.getDomain());
		text = text.replaceAll(regex("site.templateId"), site.getTemplateId()+"");
		text = text.replaceAll(regex("site.username"), site.getUsername());
		text = text.replaceAll(regex("site.phone"), site.getPhone());
		text = text.replaceAll(regex("site.qq"), site.getQq());
		text = text.replaceAll(regex("site.keywords"), site.getKeywords());
		text = text.replaceAll(regex("site.address"), site.getAddress());
		text = text.replaceAll(regex("site.companyName"), site.getCompanyName());
		
		return text;
	}
	
	/**
	 * 获取当前网站的访问域名，若是绑定顶级域名了，先使用绑定的域名
	 * @return
	 */
	private String getDomain(){
		if(site.getBindDomain() != null && site.getBindDomain().length() > 0){
			return "http://"+site.getBindDomain();
		}else{
			return "http://"+site.getDomain()+".wang.market";
		}
	}
	
	
	/**
	 * 装载模版变量
	 * @param text 页面模版的内容，会替换{includeid=}标签，装载入自定义的模版变量
	 * @param 返回装载了通用模版组件的模版内容
	 */
	public String assemblyTemplateVar(String text){
		Pattern p = Pattern.compile(regex("include=(.*?)"));
        Matcher m = p.matcher(text);
        while (m.find()) {
        	String templateVarName = m.group(1);	//模版变量的id
        	//从Session缓存中取模版变量内容
        	String content = null;
        	if(Func.getUserBeanForShiroSession().getTemplateVarMapForOriginal().get(templateVarName) != null){
        		if(editMode){
            		//编辑模式
            		content = Func.getUserBeanForShiroSession().getTemplateVarMapForOriginal().get(templateVarName).getTemplateVarData().getText();
            	}else{
            		//生成模式
            		content = Func.getUserBeanForShiroSession().getTemplateVarCompileDataMap().get(templateVarName);
            	}
        	}else{
        		content = "模版变量"+templateVarName+"不存在";
        	}
//        	String content = G.templateVarMap.get(site.getTemplateName()).get(templateVarName);
            
            String reg = regex("include="+templateVarName);
            text = Template.replaceAll(text, reg, "<!--templateVarStart--><!--templateVarName="+templateVarName+"-->"+content+"<!--templateVarEnd-->");
//            text = text.replaceAll(reg, "<!--templateVarStart--><!--templateVarName="+templateVarName+"-->"+content+"<!--templateVarEnd-->");
        }
		
        return text;
	}
	
	/**
	 * 替换新闻、图文内容详情的相关标签,UEditor编辑器中编辑的文章保存时带过来的标签
	 * @param text
	 * @return
	 */
	public String replaceNewsText(String text) {
		if(text == null){
			return null;
		}
		text = text.replaceAll(regex("prefixUrl"), AttachmentFile.netUrl()+"site/"+site.getId()+"/");
		return text;
	}
	
	/**
	 * 替换 {@link SiteColumn} 的栏目相关标签
	 * @param text 替换筛选的内容
	 * @param siteColumn 站点栏目对象
	 * @return
	 */
	public String replaceSiteColumnTag(String text, SiteColumn siteColumn){
		if(siteColumn == null){
			logger.warn("SiteColumn为空！");
			return text;
		}
		text = text.replaceAll(regex("siteColumn.id"), siteColumn.getId()+"");
		text = text.replaceAll(regex("siteColumn.name"), siteColumn.getName());
		text = text.replaceAll(regex("siteColumn.type"), siteColumn.getType()+"");
		text = text.replaceAll(regex("siteColumn.used"), siteColumn.getUsed()+"");
		text = text.replaceAll(regex("siteColumn.codeName"), siteColumn.getCodeName());
		
		//判断栏目的链接地址
		String url = "";
		if(this.generateUrlRule.equals("code")){
			//code.html
			url = siteColumn.getCodeName()+".html";
		}else{
			//id.html
			if(siteColumn.getType() - SiteColumn.TYPE_NEWS == 0 || siteColumn.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
				//新闻、图文列表页面
				url = "lc"+siteColumn.getId()+"_1.html";
			}else if (siteColumn.getType() - SiteColumn.TYPE_PAGE == 0) {
				//独立页面
				url = "c"+siteColumn.getId()+".html";
			}
		}
		text = text.replaceAll(regex("siteColumn.url"), url);
		
		return text;
	}


	/**
	 * 替换news标签
	 * @param newsText 要替换的news标签的模版内容
	 * @param news {@link News}对象，提供具体替换的数据
	 * @param siteColumn {@link SiteColumn}主要用到生成独立页面时，栏目代码作为html的文件名
	 * @return 返回替换好的内容
	 */
	public String replaceNewsTag(String newsText,News news, SiteColumn siteColumn){
		//获取到文章图片
		String titlePic = news.getTitlepic();
		if(titlePic == null){
			titlePic = G.DEFAULT_PC_ABOUT_US_TITLEPIC;
		}
		String text = newsText.replaceAll(regex("news.id"), news.getId()+"");
		text = text.replaceAll(regex("news.addtime"), DateUtil.intToString(news.getAddtime(), "yyyy-MM-dd"));
		text = text.replaceAll(regex("news.title"), news.getTitle());
		text = text.replaceAll(regex("news.intro"), news.getIntro()+"");
		text = text.replaceAll(regex("news.cid"), news.getCid()+"");

		//文章头图在正常访问时，使用相对路径
		if(titlePic.indexOf("http://") == -1){
			titlePic = "news/"+titlePic;
		}
		text = text.replaceAll(regex("news.titlepic"), titlePic);
		
		//news文章url生成模式，替换{news.url}标签
		if(this.generateUrlRule.equals("code")){
			//code.html模式

			if(siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
				//独立页面，直接使用code.html
				text = text.replaceAll(regex("news.url"), siteColumn.getCodeName()+".html");
			}else{
				//列表的某条内容页，则使用通用的id.html
				text = text.replaceAll(regex("news.url"), news.getId()+".html");
			}
		}else{
			//id.html模式
			
			if(siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
				//独立页面，直接使用c+sitecolumn.id.html
				text = text.replaceAll(regex("news.url"), "c"+siteColumn.getId()+".html");
			}else{
				//列表的某条内容页，则使用通用的id.html
				text = text.replaceAll(regex("news.url"), news.getId()+".html");
			}
		}
		
		return text;
	}
	
	/**
	 * 获取模版中的可替换标签
	 * @param regexString 标签英文名
	 * @return
	 */
	public static String regex(String regexString){
		return "\\{"+regexString+"\\}";
	}
	
	/**
	 * 获取列表页模版中的 具体列表项 的模版内容，列表便是有多个列表项才会形成列表
	 * @param listTemplate 列表页模版
	 * @return 列表项模版。若是有列表项模版存在，返回具体列表项模版的内容，如是列表模版中未加入列表项的HTML注释标签，返回""空字符串
	 */
	public String getListItemTemplate(String listTemplate){
		Pattern p = Pattern.compile("<!--TemplateListItemStart-->([\\s|\\S]*?)<!--TemplateListItemEnd-->");
        Matcher m = p.matcher(listTemplate);
        if(m.find()){
        	String itemTemplate = m.group(1);	//得到列表项模版的内容
        	return itemTemplate;
        }
        return "";
	}
	
	/**
	 * 获取列表页模版中的 分页 的模版内容，列表多了，好几页，点击页面跳转便是用的这个
	 * @param listTemplate 列表页模版
	 * @return 列表分页模版。若是有列表分页模版存在，返回具体列表分页模版的内容，如是列表模版中未加入列表分页的HTML注释标签，返回""空字符串
	 */
	public String getListPageTemplate(String listTemplate){
		Pattern p = Pattern.compile("<!--TemplateListPageStart-->([\\s|\\S]*?)<!--TemplateListPageEnd-->");
        Matcher m = p.matcher(listTemplate);
        if(m.find()){
        	String itemTemplate = m.group(1);	//得到列表项模版的内容
        	return itemTemplate;
        }
        return "";
	}
	
	/**
	 * 生成当前List页面的HTML代码，更新于附件存储上，可供客户直接访问
	 * <b>前提是传入的 listTemplateHtml 内容页模版已经替换过通用标签、动态栏目调用标签、装载完模版变量了，这里直接进行替换栏目以及文章标签相关</b>
	 * @param listTemplateHtml 当前栏目所使用的列表模版页的模版内容
	 * @param siteColumn {@link SiteColumn} 当前栏目的信息
	 * @param list 当前栏目列表页面的{@link News}列表
	 */
	public void generateListHtmlForWholeSite(String listTemplateHtml, SiteColumn siteColumn, List<News> newList){
		Site site = Func.getCurrentSite();
		int count = newList.size();	//当前列表的总条数
		
		//拿到列表模版中的 列表项 模版
		String listItemTemplate = getListItemTemplate(listTemplateHtml);
		
		int listNum = 12;	//每页多少条，默认12条
		if(siteColumn.getListNum() != null && siteColumn.getListNum() > 0){
			listNum = siteColumn.getListNum();
		}
		Page page = new Page(count, listNum);
		page.setUrlByStringUrl("");
		
		//循环生成每个列表页面
		for (int i = 1; i <= page.getLastPageNumber() && count >= 0; i++) {
			page.setCurrentPageNumber(i);
			
			String currentListHtml = "";	//当前list列表页面的html代码
			//替换分页模版
			currentListHtml = replaceListPageTag(listTemplateHtml,page,siteColumn);	
			
			//如果有模版列表的列表项，那么才会将其列出来
			if(listItemTemplate != null && listItemTemplate.length() > 0){
				//计算当前页面上显示多少条数据
				int currentShowNumber = page.getEveryNumber();
				if(page.getLastPageNumber() == page.getCurrentPageNumber()){
					//如果这是最后一页的话，极有可能这页的数据是不满足每页条数的，那只需要判断出当前页面有几条数据就可
					currentShowNumber = count - page.getLimitStart();
				}
				
				//循环生成当前页面中列表项
				StringBuffer itemsBuffer = new StringBuffer();
				for (int j = 0; j < currentShowNumber; j++) {
					int indexJ = 0;
					//生成html列表模式下
					indexJ = ((page.getCurrentPageNumber()-1)*page.getEveryNumber())+j;
					
					//得到当前列表项对象 News
					News news = newList.get(indexJ);
					//替换每个 列表项 模版，将替换好的加入要显示的列表StringBuffer中
					itemsBuffer.append(replaceNewsTag(listItemTemplate, news, siteColumn));
				}
				currentListHtml = currentListHtml.replaceAll("<!--TemplateListItemStart-->([\\s|\\S]*?)<!--TemplateListItemEnd-->", itemsBuffer.toString());
				
			}
			listTemplateHtml = replaceSiteColumnTag(listTemplateHtml, siteColumn);	//替换栏目相关标签
			
			//写出列表页面的HTML文件
			AttachmentFile.putStringFile("site/"+site.getId()+"/" + generateSiteColumnListPageHtmlName(siteColumn, i) + ".html", currentListHtml);
		}
	}
	
	/**
	 * 通过高级自定义模版，生成内容详情页面，News的页面，包含独立页面、新闻详情、图文详情
	 * <b>适合生成单个页面，会挨个标签都进行替换</b>
	 * @param news 要生成的详情页的 {@link News}
	 * @param siteColumn 要生成的详情页所属的栏目 {@link SiteColumn}
	 * @param text 内容，NewsData.text
	 * @param templateHtml 当前页面使用的内容模版
	 * @param upNews 上一篇文章的 {@link News} 可为空，表示没有上一篇。没有时会显示返回列表
	 * @param nextNews 下一篇文章的 {@link News} 可为空，表示没有下一篇。没有时会显示返回列表
	 */
	public void generateViewHtmlForTemplate(News news, SiteColumn siteColumn, String text, String templateHtml, News upNews, News nextNews) {
		if(templateHtml == null){
			//出错，没有获取到该栏目的模版页
			return;
		}
		Site site = Func.getCurrentSite();
		TemplateCMS template = new TemplateCMS(site);
		String pageHtml = template.assemblyTemplateVar(templateHtml);	//装载模版变量
		pageHtml = template.replaceSiteColumnTag(pageHtml, siteColumn);	//替换栏目相关标签
		pageHtml = template.replacePublicTag(pageHtml);		//替换通用标签
		pageHtml = template.replaceNewsTag(pageHtml, news, siteColumn);	//替换news相关标签
		
		//替换 SEO 相关
		pageHtml = pageHtml.replaceAll(Template.regex("title"), news.getTitle()+"_"+site.getName());
		pageHtml = pageHtml.replaceAll(Template.regex("keywords"), news.getTitle()+","+site.getKeywords());
		pageHtml = Template.replaceAll(pageHtml, Template.regex("description"), news.getIntro());
		
		pageHtml = Template.replaceAll(pageHtml, Template.regex("text"), template.replaceNewsText(text));	//替换新闻内容的详情
		
		String generateUrl = "";
		if(siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
			generateUrl = "site/"+site.getId()+"/"+ generateNewsPageHtmlName(siteColumn, news) +".html";
		}else{
			//若是当前页面是列表页的内容详情时，支持上一页、下一页的功能
			
			String upPage;	//上一页的超链接
			String nextPage;	//下一页的超链接
			if(upNews == null){
				upPage = "<a href=\"lc"+siteColumn.getId()+"_1.html\" target=\"_black\">返回列表</a>";
			}else{
				upPage = "<a href=\""+upNews.getId()+".html\" target=\"_black\">"+upNews.getTitle()+"</a>";
			}
			if(nextNews == null){
				nextPage = "<a href=\"lc"+siteColumn.getId()+"_1.html\" target=\"_black\">返回列表</a>";
			}else{
				nextPage = "<a href=\""+nextNews.getId()+".html\" target=\"_black\">"+nextNews.getTitle()+"</a>";
			}
			
			pageHtml = pageHtml.replaceAll(Template.regex("upPage"), upPage);
			pageHtml = pageHtml.replaceAll(Template.regex("nextPage"), nextPage);
			
			generateUrl = "site/"+site.getId()+"/"+generateNewsPageHtmlName(siteColumn, news)+".html";
		}
		
		AttachmentFile.putStringFile(generateUrl, pageHtml);
	}

	/**
	 * 生成的siteColumn栏目列表页面的html页面名字。仅仅是名字，不包含.html，不包含其所属的路径
	 * <br/>适用所有栏目列表页面的生成。
	 * @param siteColumn {@link SiteColumn}栏目
	 * @param currentPageNumber 当前是第几页。如果是第一页，可以传入1
	 * @return 生成的栏目列表页的html页面名字。仅仅是名字，不包含.html
	 */
	public String generateSiteColumnListPageHtmlName(SiteColumn siteColumn, int currentPageNumber){
		//写出列表页面的HTML文件
		if(this.generateUrlRule.equals("code")){
			//使用栏目代码作为页面名字
			if(currentPageNumber == 1){
				//第一页，则不加页数后缀，直接使用code.html
				return siteColumn.getCodeName();
			}else{
				//不是第一页，要带上分页符
				return siteColumn.getCodeName() + "_" + currentPageNumber;
			}
		}else{
			//使用栏目id编号作为栏目名字(CMS模式要废弃,兼容原本的)
			return "lc"+siteColumn.getId()+"_"+currentPageNumber;
		}
	}

	
	/**
	 * 生成的news文章的详情页的html页面名字。仅仅是名字，不包含.html，不包含其所属的路径
	 * <br/>适用所有news页面的生成。
	 * @param siteColumn {@link SiteColumn}栏目
	 * @param news {@link News}要生成的页面的news对象
	 * @return 生成的news文章的详情页的html页面名字。仅仅是名字，不包含.html
	 */
	public String generateNewsPageHtmlName(SiteColumn siteColumn, News news){
		if(this.generateUrlRule.equals("code")){
			//使用栏目代码作为页面名字
			
			if(siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
				//独立页面，直接使用栏目代码 code.html
				return siteColumn.getCodeName();
			}else{
				//列表页面，还是使用通用模式的那种编号id.html
				return news.getId()+"";
			}
		}else{
			//使用栏目id编号作为栏目名字(CMS模式要废弃,兼容原本的)
			
			if(siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
				//独立页面，直接使用栏目代码 code.html
				return "c"+news.getCid();
			}else{
				//列表页面，还是使用通用模式的那种编号id.html
				return news.getId()+"";
			}
		}
	}

	/**
	 * 执行生成整站，通过高级自定义模版，生成内容详情页面
	 * <b>前提是传入的 templateHtml 内容页模版已经替换过通用标签、动态栏目调用标签、装载完模版变量了，这里直接进行替换栏目以及文章标签相关</b>
	 * @param news 要生成的详情页的 {@link News}
	 * @param siteColumn 要生成的详情页所属的栏目 {@link SiteColumn}
	 * @param text 内容，NewsData.text
	 * @param templateHtml 当前页面使用的内容模版
	 * @param upNews 上一篇文章的 {@link News} 可为空，表示没有上一篇。没有时会显示返回列表
	 * @param nextNews 下一篇文章的 {@link News} 可为空，表示没有下一篇。没有时会显示返回列表
	 */
	public void generateViewHtmlForTemplateForWholeSite(News news, SiteColumn siteColumn, String text, String templateHtml, News upNews, News nextNews) {
		if(templateHtml == null){
			//出错，没有获取到该栏目的模版页
			return;
		}
		Site site = Func.getCurrentSite();
		TemplateCMS template = new TemplateCMS(site);
		String pageHtml = template.replaceSiteColumnTag(templateHtml, siteColumn);	//替换栏目相关标签
		pageHtml = template.replaceNewsTag(pageHtml, news, siteColumn);	//替换news相关标签
		
		pageHtml = Template.replaceAll(pageHtml, Template.regex("text"), template.replaceNewsText(text));	//替换新闻内容的详情
		
		String generateUrl = "";
		if(siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
			generateUrl = "site/"+site.getId()+"/"+generateNewsPageHtmlName(siteColumn, news)+".html";
		}else{
			//若是当前页面是列表页的内容详情时，支持上一页、下一页的功能
			
			String upPage;	//上一页的超链接
			String nextPage;	//下一页的超链接
			
			
			if(upNews == null){
				if(this.generateUrlRule.equals("code")){
					//code.html
					upPage = "<a href=\""+siteColumn.getCodeName()+".html\" target=\"_black\">返回列表</a>";
				}else{
					//id.html
					upPage = "<a href=\"lc"+siteColumn.getId()+"_1.html\" target=\"_black\">返回列表</a>";
				}
			}else{
				upPage = "<a href=\""+upNews.getId()+".html\" target=\"_black\">"+upNews.getTitle()+"</a>";
			}
			if(nextNews == null){
				if(this.generateUrlRule.equals("code")){
					//code.html
					nextPage = "<a href=\""+siteColumn.getCodeName()+".html\" target=\"_black\">返回列表</a>";
				}else{
					//id.html
					nextPage = "<a href=\"lc"+siteColumn.getId()+"_1.html\" target=\"_black\">返回列表</a>";
				}	
			}else{
				nextPage = "<a href=\""+nextNews.getId()+".html\" target=\"_black\">"+nextNews.getTitle()+"</a>";
			}
			
			
			pageHtml = pageHtml.replaceAll(Template.regex("upPage"), upPage);
			pageHtml = pageHtml.replaceAll(Template.regex("nextPage"), nextPage);
			
			generateUrl = "site/"+site.getId()+"/"+generateNewsPageHtmlName(siteColumn, news)+".html";
		}
		AttachmentFile.putStringFile(generateUrl, pageHtml);
	}
	
	/**
	 * 替换 列表模版页面 的分页标签
	 * @param pageTemplateHtml 待替换的分页模版的内容
	 * @return 替换好的分页内容
	 */
	public String replaceListPageTag(String pageTemplateHtml,Page page,SiteColumn siteColumn){
		if(pageTemplateHtml == null || pageTemplateHtml.length() == 0){
			//若列表模版中没有使用到分页标签，那么就不需要再进行替换
			return "";
		}
		String text = replaceSiteColumnTag(pageTemplateHtml, siteColumn);
		text = text.replaceAll(regex("page.allRecordNumber"), page.getAllRecordNumber()+"");
		text = text.replaceAll(regex("page.currentPageNumber"), page.getCurrentPageNumber()+"");
		text = text.replaceAll(regex("page.lastPageNumber"), page.getLastPageNumber()+"");
		text = text.replaceAll(regex("page.haveNextPage"), page.isHaveNextPage()+"");
		text = text.replaceAll(regex("page.haveUpPage"), page.isHaveUpPage()+"");
		
		if(generateUrlRule.equals("code")){
			//code.html模式
			text = text.replaceAll(regex("page.firstPage"), siteColumn.getCodeName()+".html");
			if(page.getNextPageNumber() > 1){
				//当下一页大于一页时，才会有页数，否则第一页是没有页数的
				text = text.replaceAll(regex("page.nextPage"), siteColumn.getCodeName()+"_"+page.getNextPageNumber()+".html");
			}else{
				text = text.replaceAll(regex("page.nextPage"), siteColumn.getCodeName()+".html");
			}
			
			if(page.getLastPageNumber() > 1){
				//当最后一页大于一页时，才会有页数，否则第一页是没有页数的
				text = text.replaceAll(regex("page.lastPage"), siteColumn.getCodeName()+"_"+page.getLastPageNumber()+".html");
			}else{
				text = text.replaceAll(regex("page.lastPage"), siteColumn.getCodeName()+".html");
			}
			
			if(page.getUpPageNumber() == 1){
				//如果上一页是第一页的话，是不加后面的_?的
				text = text.replaceAll(regex("page.upPage"), siteColumn.getCodeName()+".html");
			}else{
				text = text.replaceAll(regex("page.upPage"), siteColumn.getCodeName()+"_"+page.getUpPageNumber()+".html");
			}
			
			//上几页的页码跳转
			String upList = "";
			for (int i = 0; i < page.getUpList().size(); i++) {
				TagA tagA = page.getUpList().get(i);
				if(tagA.getPageNumber() - 1 == 0){
					//第一页不用加末尾的_？第几页标志
					upList = upList + "<li class=\"page_pageList\"><a href="+siteColumn.getCodeName()+".html>"+tagA.getTitle()+"</a></li>";
				}else{
					upList = upList + "<li class=\"page_pageList\"><a href="+siteColumn.getCodeName()+"_"+tagA.getPageNumber()+".html>"+tagA.getTitle()+"</a></li>";
				}
			}
			text = text.replaceAll(regex("page.upList"), upList);
			
			//下几页的页码跳转
			String nextList = "";
			for (int i = 0; i < page.getNextList().size(); i++) {
				TagA tagA = page.getNextList().get(i);
				nextList = nextList + "<li class=\"page_pageList\"><a href="+siteColumn.getCodeName()+"_"+tagA.getPageNumber()+".html>"+tagA.getTitle()+"</a></li>";
			}
			text = text.replaceAll(regex("page.nextList"), nextList);
		}else{
			//通用的id.html模式
			text = text.replaceAll(regex("page.firstPage"), "lc"+siteColumn.getId()+"_1.html");
			text = text.replaceAll(regex("page.nextPage"), "lc"+siteColumn.getId()+"_"+page.getNextPageNumber()+".html");
			text = text.replaceAll(regex("page.lastPage"), "lc"+siteColumn.getId()+"_"+page.getLastPageNumber()+".html");
			text = text.replaceAll(regex("page.upPage"), "lc"+siteColumn.getId()+"_"+page.getUpPageNumber()+".html");
			
			//上几页的页码跳转
			String upList = "";
			for (int i = 0; i < page.getUpList().size(); i++) {
				TagA tagA = page.getUpList().get(i);
				upList = upList + "<li class=\"page_pageList\"><a href=lc"+siteColumn.getId()+"_"+tagA.getPageNumber()+".html>"+tagA.getTitle()+"</a></li>";
			}
			text = text.replaceAll(regex("page.upList"), upList);
			
			//下几页的页码跳转
			String nextList = "";
			for (int i = 0; i < page.getNextList().size(); i++) {
				TagA tagA = page.getNextList().get(i);
				nextList = nextList + "<li class=\"page_pageList\"><a href=lc"+siteColumn.getId()+"_"+tagA.getPageNumber()+".html>"+tagA.getTitle()+"</a></li>";
			}
			text = text.replaceAll(regex("page.nextList"), nextList);
		}
		
		return text;
	}
	
	/**
	 * 替换<!--SiteColumn_Start--> SiteColumn的区块
	 * @param templateHTML 模版的HTML代码
	 * @param columnNewsMap 对文章-栏目进行分类，以栏目codeName为key，将文章List加入进自己对应的栏目
	 * @param columnMap 以栏目codeName为key，将栏目加入进Map中。用codeName来取栏目
	 * @param columnTreeMap 栏目树，划分一级(顶级)、二级栏目 
	 */
	public String replaceSiteColumnBlock(String templateHTML, Map<String, List<News>> columnNewsMap, Map<String, SiteColumn> columnMap, Map<String, SiteColumnTreeVO> columnTreeMap){
		Pattern p = Pattern.compile("<!--SiteColumn_Start-->([\\s|\\S]*?)<!--SiteColumn_End-->");
        Matcher m = p.matcher(templateHTML);
        while(m.find()){
        	String siteColumnTemplate = m.group(1);	//得到其中某个栏目的模版内容
        	String codeName = Template.getConfigValue(siteColumnTemplate, "codeName");	//当前的栏目代码codeName
        	
        	SiteColumn siteColumn = columnMap.get(codeName);
        	if(siteColumn == null){
        		siteColumnTemplate = "栏目代码"+codeName+"不存在";
        	}else{
        		
        		//如果<!--List_Start-->存在，将其内的模版提取出来
//        		String itemTemp = null;
//        		int number = 6;	//默认最大显示的列表条数
//            	if(siteColumnTemplate.indexOf("<!--List_Start-->") > -1){
//            		number = Template.getConfigValue(siteColumnTemplate, "number", 6);		//当前显示多少条记录，最多显示10条
//            		itemTemp = Template.getAnnoCenterString(siteColumnTemplate, "List");		//取得list的列表项内容
//            	}
        		
            	//如果<!--subColumnList_Start-->存在，需要调出其子栏目列表
            	if(siteColumnTemplate.indexOf("<!--SubColumnList_Start-->") > -1){
            		String subColumnListTemp = Template.getAnnoCenterString(siteColumnTemplate, "SubColumnList");		//取得SubColumnList的子栏目，的每项展示的模版内容
            		
            		int number = Template.getConfigValue(subColumnListTemp, "number", 6);		//当前显示多少条记录
                	String itemTemp = Template.getAnnoCenterString(subColumnListTemp, "List");		//取得list的列表项内容
            		
            		StringBuffer itemBuffer = new StringBuffer();	//显示的栏目
            		List<SiteColumnTreeVO> scList = columnTreeMap.get(codeName).getList();	//取得子栏目的数据源
            		for (int i = 0; i < scList.size(); i++) {
            			//获取子栏目信息
            			SiteColumn subColumn = scList.get(i).getSiteColumn();
            			//取到子栏目中，某个栏目的，替换掉栏目信息后的内容(如果其中有信息列表标签，还要替换)
            			String subColumnNewsTemp = replaceSiteColumnTag(subColumnListTemp, subColumn);
            			
            			//替换其中引用的子栏目文章
            			String str = replaceSiteColumnBlock_replaceNews(subColumnNewsTemp, number, itemTemp, columnNewsMap, subColumn);
            			//将子栏目替换栏目标签后，加入 itemBuffer
            			itemBuffer.append(str);
            		}
            		siteColumnTemplate = StringUtil.subStringReplace(siteColumnTemplate, "<!--SubColumnList_Start-->", "<!--SubColumnList_End-->", itemBuffer.toString());
            	}
            	
            	//如果<!--List_Start-->存在，则需要News信息列表
            	if(siteColumnTemplate.indexOf("<!--List_Start-->") > -1){
            		int number = Template.getConfigValue(siteColumnTemplate, "number", 6);		//当前显示多少条记录，最多显示10条
                	String itemTemp = Template.getAnnoCenterString(siteColumnTemplate, "List");		//取得list的列表项内容
                	
                	siteColumnTemplate = replaceSiteColumnBlock_replaceNews(siteColumnTemplate, number, itemTemp, columnNewsMap, siteColumn);
                	
//                	StringBuffer itemBuffer = new StringBuffer();	//显示的列表内容
//                	List<News> list = columnNewsMap.get(codeName);	//获取要显示的列表的数据源
//            		for (int i = 0; i < list.size() && i < number; i++) {
//            			itemBuffer.append(replaceNewsTag(itemTemp, list.get(i), siteColumn));
//            		}
//            		siteColumnTemplate = StringUtil.subStringReplace(siteColumnTemplate, "<!--List_Start-->", "<!--List_End-->", itemBuffer.toString());
            	}
            	
            	//替换栏目标签
            	siteColumnTemplate = replaceSiteColumnTag(siteColumnTemplate, siteColumn);		
        	}
        	
        	//将其替换入原始的模版内容中
        	templateHTML = StringUtil.subStringReplace(templateHTML, "<!--SiteColumn_Start-->", "<!--SiteColumn_End-->", siteColumnTemplate);
        }
		
        return templateHTML;
	}
	
	/**
	 * 服务于 {@link #replaceSiteColumnBlock(String, Map, Map, Map)}
	 * @param newsListTemplate 栏目中调取出文章列表的模版
	 * @param number 当前文章列表显示多少条记录
	 * @param itemTemp 取得list的列表项内容，包含动态标签的列表的模板
	 * @param columnNewsMap key:栏目代码， value：文章列表
	 * @param siteColumn 当前栏目
	 * @return 替换好的html
	 */
	private String replaceSiteColumnBlock_replaceNews(String newsListTemplate, int number, String itemTemp, Map<String, List<News>> columnNewsMap, SiteColumn siteColumn){
		//如果<!--List_Start-->存在，则需要News信息列表
    	if(newsListTemplate.indexOf("<!--List_Start-->") > -1){
        	StringBuffer itemBuffer = new StringBuffer();	//显示的列表内容
        	List<News> list = columnNewsMap.get(siteColumn.getCodeName());	//获取要显示的列表的数据源
    		for (int i = 0; i < list.size() && i < number; i++) {
    			itemBuffer.append(replaceNewsTag(itemTemp, list.get(i), siteColumn));
    		}
    		newsListTemplate = StringUtil.subStringReplace(newsListTemplate+" ", "<!--List_Start-->", "<!--List_End-->", itemBuffer.toString());
    	}
    	return newsListTemplate;
	}
	
}
