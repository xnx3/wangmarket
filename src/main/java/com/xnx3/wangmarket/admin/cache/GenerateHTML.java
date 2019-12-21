package com.xnx3.wangmarket.admin.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.xnx3.DateUtil;
import com.xnx3.FileUtil;
import com.xnx3.bean.TagA;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.entity.SiteData;

/**
 * 生成 HTML 缓存页面
 * @author 管雷鸣
 */
public class GenerateHTML {
	
	private int linuxTime;	//当前时间戳
	public int templateId;	//当前所用模版的编号
	private Site site;			//当前站点信息
	private boolean editMode;	//是否是编辑模式，默认不是编辑模式，用户端前台查看，正常网站浏览模式
	public static Map<String, String> commonMap = new HashMap<String, String>();	//common带标签的内容缓存，每个项只会引入一次，以后再次使用会直接从这里取而不必再进行读文件。 key:tmeplateid_filename(无.html)   value:html文件的内容
	private String columnAList = null;	//当前站点的Site.column_id拼接出来的超链接内容区域，放到<nav>标签中的
	
	public GenerateHTML(Site site) {
		linuxTime = DateUtil.timeForUnix10();
		if(site.getTemplateId() != null){
			this.templateId = site.getTemplateId();
		}else{
			this.templateId = 0;
		}
		this.site = site;
		this.editMode = false;
	}
	
	/**
	 * 设置当前是否是编辑模式，用户编辑
	 * @param editMode
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	/**
	 * 获取编辑模式下，wap首页的html
	 * @return
	 */
	public String wapIndex(){
		String pageHtml = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+templateId+"/index.html");
		pageHtml = assemblyCommon(pageHtml);	//装载通用组件
		pageHtml = replacePublicTag(pageHtml);		//替换通用标签
		
		pageHtml = pageHtml.replaceAll(regex("title"), site.getName());
		
		return pageHtml;
	}
	
	
	/**
	 * 获取编辑模式下，pc首页的html
	 * @return
	 */
	public String pcIndex(){
		String pageHtml = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+templateId+"/index.html");
		pageHtml = assemblyCommon(pageHtml);	//装载通用组件
		pageHtml = replacePublicTag(pageHtml);		//替换通用标签
		
		pageHtml = pageHtml.replaceAll(regex("title"), site.getName());
		
		return pageHtml;
	}
	
	/**
	 * 为模版替换为动态标签，替换公共/通用标签
	 * @param text 要替换的模版内容
	 * @return 替换好的内容
	 */
	public String replacePublicTag(String text){
		//OSSUrl以废弃，使用AttachmentFileUrl，表示附件等文件所在的资源访问url
		text = text.replaceAll(regex("OSSUrl"), AttachmentUtil.netUrl());	//已废弃
		text = text.replaceAll(regex("AttachmentFileUrl"), AttachmentUtil.netUrl());
		text = text.replaceAll(regex("resUrl"), AttachmentUtil.netUrl());
		text = text.replaceAll(regex("linuxTime"), linuxTime+"");
		text = text.replaceAll(regex("masterSiteUrl"), SystemUtil.get("MASTER_SITE_URL"));
		
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
		
		publicTag_init_ColumnAList();
		text = text.replaceAll(regex("columnAList"), this.columnAList);
		
		if(editMode){
			//编辑模式
			text = text.replaceAll(regex("basePath"), SystemUtil.get("MASTER_SITE_URL"));
			text = text.replaceAll(regex("edit"), "edit");
			text = text.replaceAll(regex("ossEditUrl"), AttachmentUtil.netUrl()+"site/"+site.getId()+"/");
			text = text.replaceAll(regex("editLinuxTime"), "?v="+DateUtil.timeForUnix10());
		}else{
			text = text.replaceAll(regex("basePath"), AttachmentUtil.netUrl()+"site/"+site.getId()+"/");
			text = text.replaceAll(regex("edit"), "");
			text = text.replaceAll(regex("ossEditUrl"), "");
			text = text.replaceAll(regex("editLinuxTime"), "");
		}
		
		return text;
	}
	
	/**
	 * 初始化通用替换标签的 columnAList 标签
	 */
	private void publicTag_init_ColumnAList(){
		if(this.columnAList == null){
			this.columnAList = "";
			String cal = site.getColumnId();
			if(cal != null && cal.indexOf(",") > -1){
				String calA[] = cal.split(",");
				for (int i = 0; i < calA.length; i++) {
					if(calA[i].length() > 0){
						this.columnAList = this.columnAList + "<a href=\"lc"+calA[i]+"_1.html\">"+calA[i]+"</a>";
					}
				}
			}
		}
	}
	
	/**
	 * 装载common文件夹下的通用组件，WAP、PC通用
	 * @param text 页面模版的内容，会将这里面所存在的调用common的模版装载组合
	 * @param 返回装载了通用模版组件的模版内容
	 */
	public String assemblyCommon(String text){
		Pattern p = Pattern.compile(regex("include=(.*?)"));
        Matcher m = p.matcher(text);
        while (m.find()) {
        	String templateVarName = m.group(1);	//模版变量的id
            
        	//查看再内存中是否有此项内容了，若没有，先将模版装载入内存，以后直接从内存取
        	String key = templateId+templateVarName;
        	String templateVarText = commonMap.get(key);
//            if(templateVarText == null){
            if(true){
            	templateVarText = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+templateId+"/common/"+templateVarName+".html");
            	commonMap.put(key,templateVarText);
            }
            
            String reg = regex("include="+templateVarName);
            text = text.replaceAll(reg, templateVarText);
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
	 * 替换 {@link SiteColumn} 的栏目相关标签
	 * @param text 替换筛选的内容
	 * @param siteColumn 站点栏目对象
	 * @return
	 */
	public String replaceSiteColumnTag(String text, SiteColumn siteColumn){
		if(siteColumn == null){
			return text;
		}
		text = text.replaceAll(regex("siteColumn.id"), siteColumn.getId()+"");
		text = text.replaceAll(regex("siteColumn.name"), siteColumn.getName());
		text = text.replaceAll(regex("siteColumn.type"), siteColumn.getType()+"");
		text = text.replaceAll(regex("siteColumn.used"), siteColumn.getUsed()+"");
		
		//判断栏目的链接地址
		String url = siteColumn.getUrl();
		if(siteColumn.getType() - SiteColumn.TYPE_NEWS == 0 || siteColumn.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
			url = "lc"+siteColumn.getId()+"_1.html";
		}else if (siteColumn.getType() - SiteColumn.TYPE_PAGE == 0) {
			url = "c"+siteColumn.getId()+".html";
		}
		if(url == null){
			url = "#";
		}
		text = text.replaceAll(regex("siteColumn.url"), url);
		
		return text;
	}
	
	/**
	 * 替换 list 列表页面的分页标签
	 * @param text
	 * @return
	 */
	public String replaceListTag(String text,Page page,SiteColumn siteColumn){
		text = replaceSiteColumnTag(text, siteColumn);
		text = text.replaceAll(regex("page.allRecordNumber"), page.getAllRecordNumber()+"");
		text = text.replaceAll(regex("page.currentPageNumber"), page.getCurrentPageNumber()+"");
		text = text.replaceAll(regex("page.lastPageNumber"), page.getLastPageNumber()+"");
		text = text.replaceAll(regex("page.haveNextPage"), page.isHaveNextPage()+"");
		text = text.replaceAll(regex("page.haveUpPage"), page.isHaveUpPage()+"");
		if(editMode){
			//编辑模式下
			text = text.replaceAll(regex("page.firstPage"), "list.do?cid="+siteColumn.getId()+"&client=wap&editMode=edit&currentPage=1");
			text = text.replaceAll(regex("page.upPage"), "list.do?cid="+siteColumn.getId()+"&client=wap&editMode=edit&currentPage="+page.getUpPageNumber());
			text = text.replaceAll(regex("page.nextPage"), "list.do?cid="+siteColumn.getId()+"&client=wap&editMode=edit&currentPage="+page.getNextPageNumber());
			text = text.replaceAll(regex("page.lastPage"), "list.do?cid="+siteColumn.getId()+"&client=wap&editMode=edit&currentPage="+page.getLastPageNumber());
			
			//上几页的页码跳转
			String upList = "";
			for (int i = 0; i < page.getUpList().size(); i++) {
				TagA tagA = page.getUpList().get(i);
				upList = upList + "<li class=\"page_pageList\"><a href="+tagA.getHref()+">"+tagA.getTitle()+"</a></li>";
			}
			text = text.replaceAll(regex("page.upList"), upList);
			
			//下几页的页码跳转
			String nextList = "";
			for (int i = 0; i < page.getNextList().size(); i++) {
				TagA tagA = page.getNextList().get(i);
				nextList = nextList + "<li class=\"page_pageList\"><a href="+tagA.getHref()+">"+tagA.getTitle()+"</a></li>";
			}
			text = text.replaceAll(regex("page.nextList"), nextList);
		}else{
			//正常访问模式下
			text = text.replaceAll(regex("page.firstPage"), "lc"+siteColumn.getId()+"_1.html");
			text = text.replaceAll(regex("page.upPage"), "lc"+siteColumn.getId()+"_"+page.getUpPageNumber()+".html");
			text = text.replaceAll(regex("page.nextPage"), "lc"+siteColumn.getId()+"_"+page.getNextPageNumber()+".html");
			text = text.replaceAll(regex("page.lastPage"), "lc"+siteColumn.getId()+"_"+page.getLastPageNumber()+".html");
			
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
	 * 替换News表的相关数据。List列表页面的列表项News相关参数
	 * @param itemText
	 * @param news
	 * @return
	 */
	public String replaceNewsListItem(String itemText,News news){
		//获取到文章图片
		String titlePic = news.getTitlepic();
		if(titlePic == null){
//			titlePic = G.DEFAULT_PC_ABOUT_US_TITLEPIC;
			titlePic = SystemUtil.get("MASTER_SITE_URL")+"default/aboutUs.jpg";
		}
		String text = itemText.replaceAll(GenerateHTML.regex("news.id"), news.getId()+"");
		text = text.replaceAll(GenerateHTML.regex("news.addtime"), DateUtil.intToString(news.getAddtime(), "yyyy-MM-dd"));
		text = Template.replaceAll(text, GenerateHTML.regex("news.title"), news.getTitle()+"");
		text = Template.replaceAll(text, GenerateHTML.regex("news.intro"), news.getIntro()+"");
		
		if(editMode){
			//编辑模式
			if(titlePic.indexOf("http://") == -1){
				titlePic = AttachmentUtil.netUrl()+"site/"+news.getSiteid()+"/news/"+titlePic;
			}
			text = text.replaceAll(GenerateHTML.regex("news.titlepic"), titlePic);
			text = text.replaceAll(GenerateHTML.regex("news.url"), "news.do?id="+news.getId()+"&editMode=edit");	//client=wap& 去掉，自动根据浏览器识别
		}else{
			//正常访问模式
			//文章头图在正常访问时，使用相对路径
			if(titlePic.indexOf("http://") == -1){
				titlePic = "news/"+titlePic;
			}
			text = text.replaceAll(GenerateHTML.regex("news.titlepic"), titlePic);
			text = text.replaceAll(GenerateHTML.regex("news.url"), news.getId()+".html");
		}
		
		return text;
	}
	
	
	/**
	 * 获取生成好的PC首页的HTML源代码
	 * @return 若地址么有东西，则返回空字符串""
	 */
	public String getGeneratePcIndexHtml(){
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return AttachmentUtil.getTextByPath("site/"+site.getId()+"/index.html");
	}
	
	/**
	 * 生成网站首页
	 * @param htmlText 如果是PC首页，则需要传入要声称页面的内容。毕竟PC首页会牵扯到替换模版某个模块，不能全局替换，替换处理再 pc/下
	 * 				如果是WAP首页，则不需要传此参数
	 * @param siteData {@link SiteData}，单个模块刷新时可传null
	 */
	public void generateIndexHtml(String htmlText,SiteData siteData){
		if(htmlText == null){
			//加入htmledit时，刷新首页报空指针，加此
			return;
		}
		
		if(site.getClient() - Site.CLIENT_PC == 0){
			//电脑页面
//			html = gh.pcIndex();
			//刷新pc首页的时候，需要刷新pc首页中间的那些数据，还需要到数据库里将其查询出来
			
			htmlText = htmlText.replaceAll("<!--templateStyleCss-->", "<link href=\""+AttachmentUtil.netUrl()+"template/"+site.getTemplateId()+"/style.css\" rel=\"stylesheet\" type=\"text/css\">");
		}else{
			htmlText = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+templateId+"/index.html");
			htmlText = assemblyCommon(htmlText);	//装载通用组件
			htmlText = replacePublicTag(htmlText);		//替换通用标签
		}
		
		//替换 SEO 相关
		htmlText = htmlText.replaceAll(regex("title"), site.getName());
		htmlText = htmlText.replaceAll(regex("keywords"), site.getKeywords());
		htmlText = htmlText.replaceAll(regex("description"), siteData == null? site.getName():siteData.getIndexDescription());
		
		generateIndexHtml(htmlText);
	}
	
	/**
	 * 生成栏目页面
	 * @param htmlText 栏目页面的HTML代码
	 * @param siteColumn 栏目属性
	 * @param pageNumber 当前在第几页
	 */
	public void generateListHtml(String htmlText, SiteColumn siteColumn, int pageNumber) {
		
		//替换 SEO 相关
		htmlText = htmlText.replaceAll(regex("title"), siteColumn.getName()+"_"+site.getName());
		htmlText = htmlText.replaceAll(regex("keywords"), siteColumn.getName()+","+site.getKeywords());
		htmlText = htmlText.replaceAll(regex("description"), siteColumn.getName());	//临时暂时这么放吧
		
		AttachmentUtil.putStringFile("site/"+site.getId()+"/lc"+siteColumn.getId()+"_"+pageNumber+".html", htmlText);
	}
	
	/**
	 * 生成内容详情页面，News的页面，包含独立页面、新闻详情、图文详情
	 * @param site
	 * @param news
	 * @param siteColumn
	 * @param text 内容，NewsData.text
	 */
	public void generateViewHtml(Site site, News news, SiteColumn siteColumn, String text) {
		String pageHtml = FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+templateId+"/page.html");
		pageHtml = assemblyCommon(pageHtml);	//装载通用组件
		pageHtml = replaceSiteColumnTag(pageHtml, siteColumn);	//替换栏目相关标签
		pageHtml = replacePublicTag(pageHtml);		//替换通用标签
		pageHtml = replaceNewsListItem(pageHtml, news);	//替换news相关标签
		
		//替换 SEO 相关
		pageHtml = pageHtml.replaceAll(regex("title"), news.getTitle()+"_"+site.getName());
		pageHtml = pageHtml.replaceAll(regex("keywords"), news.getTitle()+","+site.getKeywords());
		pageHtml = Template.replaceAll(pageHtml, regex("description"), news.getIntro()+"");
		
		
		pageHtml = Template.replaceAll(pageHtml, regex("text"), replaceNewsText(text));	//替换新闻内容的详情
		
		String generateUrl = "";
		if(news.getType() - News.TYPE_PAGE == 0){
			generateUrl = "site/"+site.getId()+"/c"+news.getCid()+".html";
		}else{
			generateUrl = "site/"+site.getId()+"/"+news.getId()+".html";
		}
		AttachmentUtil.putStringFile(generateUrl, pageHtml);
	}
	
	/**
	 * 替换新闻、图文内容详情的相关标签
	 * @param text
	 * @return
	 */
	public String replaceNewsText(String text) {
		if(text == null){
			return "";
		}
		text = Template.replaceAll(text, regex("prefixUrl"), AttachmentUtil.netUrl()+"site/"+site.getId()+"/");
		return text;
	}
	
	/**
	 * 还原新闻、图文内容的相关标签，如将绝对路径的图片、附件，还原为标签形式的，存入数据库
	 * @param text UEditor编辑器内保存的文本数据
	 * @return
	 */
	public String restoreNewsText(String text) {
		if(text == null){
			return "";
		}
		text = text.replaceAll("\\{prefixUrl\\}", AttachmentUtil.netUrl()+"site/"+site.getId()+"/");
		return text;
	}
	
//	
	/**
	 * 生成首页,只是将首页的内容输出存储到OSS上，不进行什么替换
	 * @param htmlText
	 */
	public void generateIndexHtml(String htmlText){
		AttachmentUtil.putStringFile("site/"+site.getId()+"/index.html", htmlText);
	}
}
