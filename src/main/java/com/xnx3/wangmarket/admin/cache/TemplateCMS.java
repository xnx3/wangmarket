package com.xnx3.wangmarket.admin.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.bean.TagA;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.json.JSONUtil;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.util.SessionUtil;
import com.xnx3.wangmarket.admin.util.TemplateUtil;
import com.xnx3.wangmarket.admin.vo.SiteColumnTreeVO;
import com.xnx3.wangmarket.admin.vo.TemplateVO;
import com.xnx3.wangmarket.admin.vo.TemplateVarVO;

import net.sf.json.JSONObject;

/**
 * CMS模版
 * @author 管雷鸣
 */
public class TemplateCMS {
	private int linuxTime;	//当前时间戳
	private Site site;			//当前站点信息
	private com.xnx3.wangmarket.admin.entity.Template template;	//当前站点所使用的模版。注意，有的站点是没有的，比如自定义模版的用户，那么 {templatePath} 默认就用本地的。 这个对象永远不会为null，即使为null，也会默认创建一个出来，设定 resourceImport
	private JSONObject siteVarJson;	//站点的全局变量，使用 setSiteVar 来赋予
	private String templatePathDomain = null;	//当前模版所使用的资源文件路径，如本地的则是 ....../websiteTemplate/.....，  云端则是 //cdn.weiunity.com/websiteTemplate/...  根据template 来判断，判断是从数据库中取的还是从云端同步模版取的。如果是从数据库取的，肯定是用本地资源，如果是云端取的，当然就是云端资源了
	
	public final static String TEMPLATE_CLOUD_PATH;	//云端资源库路径，云端引用的js、css都是通过这个。格式如： http://cloudtemplate.weiunity.com/
	//模版引用路径。v4.7增加，值如 http://wang.market/template/
	public final static String TEMPLATE_PRIVATE_PATH;
	static{
		TEMPLATE_PRIVATE_PATH = SystemUtil.get("ATTACHMENT_FILE_URL")+"websiteTemplate/";
		TEMPLATE_CLOUD_PATH = "//cloudtemplate.weiunity.com/websiteTemplate/";
	}
	
	/**
	 * 获取当前模版所使用的资源文件路径
	 * @return 如 //cloudtemplate.weiunity.com/websiteTemplate/templateName/
	 */
	public String getTemplatePath(){
		if(templatePathDomain == null){
			//判断 template 是从数据库取的，还是从云端取的。
			//首先判断是否是从本地模版库取的
			if(this.template != null && TemplateUtil.databaseTemplateMapForName.get(this.template.getName()) != null){
				//本地模版库有，则是从从数据库取的
				templatePathDomain = TEMPLATE_PRIVATE_PATH;
			}else{
				//本地模版库没有，则认为是从云端模版库取的。
				templatePathDomain = TEMPLATE_CLOUD_PATH;
			}
			templatePathDomain = templatePathDomain + site.getTemplateName() + "/";
		}
		return templatePathDomain;
	}
	
	/**
	 * CMS模式网站，生成html页面时的命名规则，wang.market的兼容以前版本，255编号以前的使用int规则，免得影响了做SEO的站点。其他的，包括其他服务器或者其他代理使用此程序创建，都是用code规则即可
	 * int:通用模式的数字模式；
	 * code:CMS模式的根据栏目代码生成模式
	 */
	private String generateUrlRule = "int";	 
	
	private boolean editMode = false;	//当前是否为编辑模式，默认为false，不是编辑模式，为生成模式 
	
	
	public TemplateCMS(Site site, TemplateVO templateVO) {
		com.xnx3.wangmarket.admin.entity.Template template = null;
		if(templateVO != null && templateVO.getTemplate() != null){
			template = templateVO.getTemplate();
		}
		this.templateCMS(site, template);
	}
	
	/**
	 * 默认editMode为生成模式
	 * @param site
	 */
	public TemplateCMS(Site site, com.xnx3.wangmarket.admin.entity.Template template) {
		this.templateCMS(site, template);
	}
	
	//构造方法都会调用此初始化
	private void templateCMS(Site site, com.xnx3.wangmarket.admin.entity.Template template){
		linuxTime = DateUtil.timeForUnix10();
		this.site = site;
		
		this.template = template;
		
		if(SystemUtil.get("MASTER_SITE_URL") != null && SystemUtil.get("MASTER_SITE_URL").equals("http://wang.market/")){
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
		this.site = site;
		this.editMode = editMode;
	}
	
	/**
	 * 设置站点全局变量
	 * @param siteVarJson
	 */
	public void setSiteVar(JSONObject siteVarJson){
		if(siteVarJson == null){
			this.siteVarJson = new JSONObject();
			return;
		}
		
		this.siteVarJson = siteVarJson;
	}
	
	/**
	 * 为模版替换为动态标签，替换公共/通用标签
	 * @param text 要替换的模版内容
	 * @return 替换好的内容
	 */
	public String replacePublicTag(String text){
		//v5.1增加，全局变量, v5.2有底部移到顶部
		text = replaceSiteVar(text);
				
		text = Template.replaceAll(text, regex("OSSUrl"), AttachmentUtil.netUrl());	//以废弃，保留，适应以前版本
		text = Template.replaceAll(text, regex("AttachmentFileUrl"), AttachmentUtil.netUrl());
		text = Template.replaceAll(text, regex("resUrl"), AttachmentUtil.netUrl());
		text = Template.replaceAll(text, regex("linuxTime"), linuxTime+"");
		text = Template.replaceAll(text, regex("masterSiteUrl"), SystemUtil.get("MASTER_SITE_URL"));
		
		text = Template.replaceAll(text, regex("siteId"), site.getId()+"");
		text = Template.replaceAll(text, regex("siteDomain"), site.getDomain());
		text = Template.replaceAll(text, regex("siteName"), site.getName());
		text = Template.replaceAll(text, regex("siteTemplateId"), site.getTemplateId()+"");
		
		text = Template.replaceAll(text, regex("site.name"), site.getName());
		text = Template.replaceAll(text, regex("site.id"), site.getId()+"");
		text = Template.replaceAll(text, regex("site.domain"), site.getDomain());
		text = Template.replaceAll(text, regex("site.templateId"), site.getTemplateId()+"");
		text = Template.replaceAll(text, regex("site.username"), site.getUsername());
		text = Template.replaceAll(text, regex("site.phone"), site.getPhone());
		text = Template.replaceAll(text, regex("site.qq"), site.getQq());
		text = Template.replaceAll(text, regex("site.keywords"), site.getKeywords());
		text = Template.replaceAll(text, regex("site.address"), site.getAddress());
		text = Template.replaceAll(text, regex("site.companyName"), site.getCompanyName());
		
		text = Template.replaceAll(text, regex("masterSiteUrl"), SystemUtil.get("MASTER_SITE_URL"));
		//v4.7增加
		text = Template.replaceAll(text, regex("templatePath"), getTemplatePath());
		
		
		return text;
	}
	
	/**
	 * v5.1增加，全局变量
	 * @param text 要进行替换的字符串
	 * @return 将全局变量已经替换的字符串
	 */
	public String replaceSiteVar(String text){
		if(this.siteVarJson == null){
			return text;
		}
		if(text == null){
			return null;
		}
		
		//如果 {var.xxx} 存在，则需要替换
    	if(text.indexOf("{var.") > -1){
    		Pattern p = Pattern.compile(regex("var\\.(.*?)"));
            Matcher m = p.matcher(text);
            while (m.find()) {
            	String name = m.group(1);	//全局变量的name
                String reg = regex("var."+name);
                String value = null;
                if(this.siteVarJson.get(name) != null){
                	value = this.siteVarJson.getJSONObject(name).getString("value");
                }else{
                	value = "";
                }
                text = Template.replaceAll(text, reg, value);
            }
    	}
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
//	public String assemblyTemplateVar(String text){
//		Pattern p = Pattern.compile(regex("include=(.*?)"));
//        Matcher m = p.matcher(text);
//        while (m.find()) {
//        	String templateVarName = m.group(1);	//模版变量的id
//        	//从Session缓存中取模版变量内容
//        	String content = null;
//        	if(Func.getUserBeanForShiroSession().getTemplateVarMapForOriginal().get(templateVarName) != null){
//        		if(editMode){
//            		//编辑模式
//            		content = Func.getUserBeanForShiroSession().getTemplateVarMapForOriginal().get(templateVarName).getTemplateVarData().getText();
//            	}else{
//            		//生成模式
//            		content = Func.getUserBeanForShiroSession().getTemplateVarCompileDataMap().get(templateVarName);
//            	}
//        	}else{
//        		content = "模版变量"+templateVarName+"不存在";
//        	}
//            
//            String reg = regex("include="+templateVarName);
//            text = Template.replaceAll(text, reg, "<!--templateVarStart--><!--templateVarName="+templateVarName+"-->"+content+"<!--templateVarEnd-->");
//        }
//		
//        return text;
//	}
	
	/**
	 * 编辑模式下，也就是在网站管理后台编辑时，装载模版变量
	 * @param text 页面模版的内容，会替换{includeid=}标签，装载入自定义的模版变量
	 * @param 返回装载了通用模版组件的模版内容
	 */
	public String assemblyTemplateVarByOriginal(String text, Map<String, TemplateVarVO> templateVarVOMap){
		Pattern p = Pattern.compile(regex("include=(.*?)"));
        Matcher m = p.matcher(text);
        while (m.find()) {
        	String templateVarName = m.group(1);	//模版变量的id
        	//从Session缓存中取模版变量内容
        	String content = null;
        	if(templateVarVOMap.get(templateVarName) != null){
        		//编辑模式
        		content = templateVarVOMap.get(templateVarName).getTemplateVarData().getText();
        	}else{
        		content = "模版变量"+templateVarName+"不存在";
        	}
            
            String reg = regex("include="+templateVarName);
            text = Template.replaceAll(text, reg, "<!--templateVarStart--><!--templateVarName="+templateVarName+"-->"+content+"<!--templateVarEnd-->");
        }
		
        return text;
	}
	

	/**
	 * 生成模式下，装载模版变量。也就是点击生成整站时，进行的调用
	 * @param text 页面模版的内容，会替换{includeid=}标签，装载入自定义的模版变量
	 * @param map TemplateVar_data.text 
	 * @param 返回装载了通用模版组件的模版内容
	 */
	public String assemblyTemplateVar(String text, Map<String, String> map){
		Pattern p = Pattern.compile(regex("include=(.*?)"));
        Matcher m = p.matcher(text);
        while (m.find()) {
        	String templateVarName = m.group(1);	//模版变量的id
        	//从Session缓存中取模版变量内容
        	String content = null;
        	if(map.get(templateVarName) != null){
        		//生成模式
        		content = map.get(templateVarName);
        	}else{
        		content = "模版变量"+templateVarName+"不存在";
        	}
            
            String reg = regex("include="+templateVarName);
            text = Template.replaceAll(text, reg, "<!--templateVarStart--><!--templateVarName="+templateVarName+"-->"+content+"<!--templateVarEnd-->");
        }
		
        return text;
	}
	
	/**
	 * 替换新闻、图文内容详情的相关标签,UEditor编辑器中编辑的文章保存时带过来的标签
	 * @param text
	 * @return 如果为null则返回空字符串
	 */
	public String replaceNewsText(String text) {
		if(text == null){
			return "";
		}
		text = Template.replaceAll(text, regex("prefixUrl"), AttachmentUtil.netUrl()+"site/"+site.getId()+"/");
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
			return text;
		}
		text = Template.replaceAll(text, regex("siteColumn.id"), siteColumn.getId()+"");
		text = Template.replaceAll(text, regex("siteColumn.name"), siteColumn.getName());
		text = Template.replaceAll(text, regex("siteColumn.type"), siteColumn.getType()+"");
		text = Template.replaceAll(text, regex("siteColumn.used"), siteColumn.getUsed()+"");
		text = Template.replaceAll(text, regex("siteColumn.codeName"), siteColumn.getCodeName());
		text = Template.replaceAll(text, regex("siteColumn.parentCodeName"), (siteColumn.getParentCodeName() == null || siteColumn.getParentCodeName().equals("")) ? siteColumn.getCodeName() : siteColumn.getParentCodeName());
		//v4.7
		text = Template.replaceAll(text, regex("siteColumn.icon"), (siteColumn.getIcon() == null? "":siteColumn.getIcon()).replace("{templatePath}", getTemplatePath()));
		//v5.1
		text = Template.replaceAll(text, regex("siteColumn.keywords"), (siteColumn.getKeywords() == null? "":siteColumn.getKeywords()));
		text = Template.replaceAll(text, regex("siteColumn.description"), (siteColumn.getDescription() == null? "":siteColumn.getDescription()));
		
		//判断栏目的链接地址
		String url = "";
		if(this.generateUrlRule.equals("code")){
			//code.html
			url = siteColumn.getCodeName()+".html";
		}else{
			//id.html
			if(siteColumn.getType() - SiteColumn.TYPE_LIST == 0 || siteColumn.getType() - SiteColumn.TYPE_NEWS == 0 || siteColumn.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
				//新闻、图文列表页面
				url = "lc"+siteColumn.getId()+"_1.html";
			}else if (siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_PAGE == 0) {
				//独立页面
				url = "c"+siteColumn.getId()+".html";
			}
		}
		text = Template.replaceAll(text, regex("siteColumn.url"), url);
		
		return text;
	}


	/**
	 * 替换news标签
	 * @param newsText 要替换的news标签的模版内容
	 * @param news {@link News}对象，提供具体替换的数据
	 * @param siteColumn {@link SiteColumn}主要用到生成独立页面时，栏目代码作为html的文件名
	 * @param newsDataBean news_data数据表的加工
	 * @return 返回替换好的内容
	 */
	public String replaceNewsTag(String newsText,News news, SiteColumn siteColumn, NewsDataBean newsDataBean){
		//获取到文章图片
		String titlePic = news.getTitlepic();
		if(titlePic == null){
//			titlePic = G.DEFAULT_PC_ABOUT_US_TITLEPIC;
			titlePic = SystemUtil.get("MASTER_SITE_URL")+"default/aboutUs.jpg";
		}
		String text = Template.replaceAll(newsText, regex("news.id"), news.getId()+"");
		text = Template.replaceAll(text, regex("news.addtime"), DateUtil.intToString(news.getAddtime(), "yyyy-MM-dd"));
		text = Template.replaceAll(text, regex("news.title"), news.getTitle());
		text = Template.replaceAll(text, regex("news.intro"), news.getIntro()+"");
		text = Template.replaceAll(text, regex("news.cid"), news.getCid()+"");
		
		//v4.5增加以下两个预留字段
		text = Template.replaceAll(text, regex("news.reserve1"), news.getReserve1());
		text = Template.replaceAll(text, regex("news.reserve2"), news.getReserve2());
		
		//文章头图在正常访问时，使用相对路径  
		if(titlePic.indexOf("//") == 0 || titlePic.indexOf("http://") == 0 || titlePic.indexOf("https://") == 0){
			//用的绝对路径，这里不用做任何补充
		}else{
			//用的相对路径，要加个东西了，不过CMS模式的好像没有这样的了。v4.8版本更新检测到此处，此处属于基本废弃功能，暂时先保留
			titlePic = "news/"+titlePic;
		}
		text = Template.replaceAll(text, regex("news.titlepic"), titlePic);
		
		//news文章url生成模式，替换{news.url}标签
		if(this.generateUrlRule.equals("code")){
			//code.html模式

			if(siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
				//独立页面，直接使用code.html
				text = Template.replaceAll(text, regex("news.url"), siteColumn.getCodeName()+".html");
			}else{
				//列表的某条内容页，则使用通用的id.html
				text = Template.replaceAll(text, regex("news.url"), news.getId()+".html");
			}
		}else{
			//id.html模式
			
			if(siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
				//独立页面，直接使用c+sitecolumn.id.html
				text = Template.replaceAll(text, regex("news.url"), "c"+siteColumn.getId()+".html");
			}else{
				//列表的某条内容页，则使用通用的id.html
				text = Template.replaceAll(text, regex("news.url"), news.getId()+".html");
			}
		}
		
		
		//v4.7 增加 {news.addtime.day} 、 {news.addtime.month} 、 {news.addtime.year} 、 hour 、 minute
		if(text.indexOf("{news.addtime.") > -1){
			long time = news.getAddtime();
			Calendar c = new GregorianCalendar();
			c.setTime(new Date(time * 1000));
			
			text = Template.replaceAll(text, regex("news.addtime.year"), c.get(Calendar.YEAR)+"");
			text = Template.replaceAll(text, regex("news.addtime.month"), (c.get(Calendar.MONTH) + 1)+"");
			text = Template.replaceAll(text, regex("news.addtime.day"), c.get(Calendar.DAY_OF_MONTH)+"");
			text = Template.replaceAll(text, regex("news.addtime.hour"), c.get(Calendar.HOUR_OF_DAY)+"");
			text = Template.replaceAll(text, regex("news.addtime.minute"), c.get(Calendar.MINUTE)+"");
			text = Template.replaceAll(text, regex("news.addtime.second"), c.get(Calendar.SECOND)+"");
		}
		
		//v4.9版本增加，提高容错
		if(newsDataBean == null){
			//如果newsDataBean为空，则是文章在 news表中有，但是在 news_data 表中没有！正常情况下是不会存在的，除非出错！那么进行日志打印。
			ConsoleUtil.error("文章在news中有，在news_data中没有！文章id:"+news.getId());
			//因为已经没有 news_data 表的数据了，直接将替换了news表的数据返回就可以了
			return text;
		}
		
		text = Template.replaceAll(text, Template.regex("news.text"), replaceNewsText(newsDataBean.getText()));	//替换新闻内容的详情
		
		//v4.6增加
		//如果模版里面使用了 extend 调取，那么进行此相关替换
		if(text.indexOf("{news.extend.") > -1){
			Pattern p = Pattern.compile(Template.regex("news.extend.(\\w*?)"));
	        Matcher m = p.matcher(text);
	        //用map来过滤重复的key 。 这里value是没什么用的
	        Map<String, Boolean> map = new HashMap<String, Boolean>();
	        while(m.find()){
	        	String key = m.group(1);	//得到key，以便组合 {news.extend.?}
	        	map.put(key, true);	//将得到的key加入map中，以起到排重的作用
	        }
	        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
	        	text = Template.replaceAll(text, Template.regex("news.extend."+entry.getKey()), newsDataBean.getExtendJson(entry.getKey()));	//替换新闻内容的详情
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
	 * @param newsDataMap 对 newsDataList 网站文章的内容进行调整，调整为map key:newsData.id  value:NewsDataBean
	 * @param columnMapForId 栏目map，以栏目id为key，用id来取栏目
	 */
	public void generateListHtmlForWholeSite(String listTemplateHtml, SiteColumn siteColumn, List<News> newList, Map<Integer, NewsDataBean> newsDataMap, Map<Integer, SiteColumn> columnMapForId){
//		Site site = SessionUtil.getSite();
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
					//替换文章标签
					String newsHTML = replaceNewsTag(listItemTemplate, news, siteColumn, newsDataMap.get(news.getId()));
					//判断列表项里面是否有栏目标签，如果有的话，还要替换栏目标签。替换的栏目标签为当前文章所属的栏目
					if(newsHTML.indexOf("siteColumn") > -1){
						//替换当前文章的直属栏目信息，栏目标签
						newsHTML = replaceSiteColumnTag(newsHTML, columnMapForId.get(news.getCid()));
					}
					
					itemsBuffer.append(newsHTML);
				}
				
				currentListHtml = Template.replaceAll(currentListHtml, "<!--TemplateListItemStart-->([\\s|\\S]*?)<!--TemplateListItemEnd-->", itemsBuffer.toString());
			}
			
			//写出列表页面的HTML文件
			AttachmentUtil.putStringFile("site/"+site.getId()+"/" + generateSiteColumnListPageHtmlName(siteColumn, i) + ".html", currentListHtml);
		}
	}
	
	/**
	 * 通过高级自定义模版，生成内容详情页面，News的页面，包含独立页面、新闻详情、图文详情
	 * <b>适合生成单个页面，会挨个标签都进行替换</b>
	 * @param news 要生成的详情页的 {@link News}
	 * @param siteColumn 要生成的详情页所属的栏目 {@link SiteColumn}
	 * @param newsDataBean news_data整理的json对象
	 * @param templateHtml 当前页面使用的内容模版
	 * @param upNews 上一篇文章的 {@link News} 可为空，表示没有上一篇。没有时会显示返回列表
	 * @param nextNews 下一篇文章的 {@link News} 可为空，表示没有下一篇。没有时会显示返回列表
	 */
//	public void generateViewHtmlForTemplate(News news, SiteColumn siteColumn, NewsDataBean newsDataBean, String templateHtml, News upNews, News nextNews) {
//		if(templateHtml == null){
//			//出错，没有获取到该栏目的模版页
//			return;
//		}
//		String pageHtml = assemblyTemplateVar(templateHtml);	//装载模版变量
//		pageHtml = replaceSiteColumnTag(pageHtml, siteColumn);	//替换栏目相关标签
//		pageHtml = replacePublicTag(pageHtml);		//替换通用标签
//		pageHtml = replaceNewsTag(pageHtml, news, siteColumn, newsDataBean);	//替换news相关标签
//		
//		//替换 SEO 相关
//		pageHtml = Template.replaceAll(pageHtml, Template.regex("title"), news.getTitle()+"_"+site.getName());
//		pageHtml = Template.replaceAll(pageHtml, Template.regex("keywords"), news.getTitle()+","+site.getKeywords());
//		pageHtml = Template.replaceAll(pageHtml, Template.regex("description"), news.getIntro());
//		
//		generateNewsHtml(news, siteColumn, upNews, nextNews, pageHtml, newsDataBean);
//	}

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
		if(news == null){
			news = new News();
		}
		if(news.getId() == null){
			news.setId(0);
		}
		
		if(this.generateUrlRule.equals("code")){
			//使用栏目代码作为页面名字
			
			if(siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
				//独立页面，直接使用栏目代码 code.html
				return siteColumn.getCodeName();
			}else{
				//列表页面，还是使用通用模式的那种编号id.html
				return news.getId()+"";
			}
		}else{
			//使用栏目id编号作为栏目名字(CMS模式要废弃,兼容原本的)
			if(siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
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
	 * @param newsDataBean news_data 整理的，加入了json的数据对象
	 * @param templateHtml 当前页面使用的内容模版
	 * @param upNews 上一篇文章的 {@link News} 可为空，表示没有上一篇。没有时会显示返回列表
	 * @param nextNews 下一篇文章的 {@link News} 可为空，表示没有下一篇。没有时会显示返回列表
	 */
	public void generateViewHtmlForTemplateForWholeSite(News news, SiteColumn siteColumn, NewsDataBean newsDataBean, String templateHtml, News upNews, News nextNews) {
		if(templateHtml == null){
			//出错，没有获取到该栏目的模版页
			return;
		}
//		TemplateCMS template = new TemplateCMS(site);
		String pageHtml = replaceSiteColumnTag(templateHtml, siteColumn);	//替换栏目相关标签
		if(news != null){
			pageHtml = replaceNewsTag(pageHtml, news, siteColumn, newsDataBean);	//替换news相关标签
		}
		
		generateNewsHtml(news, siteColumn, upNews, nextNews, pageHtml, newsDataBean);
	}
	
	/**
	 * 生成文章详情页面的html页面
	 * @param news 要生成的详情页的 {@link News}
	 * @param siteColumn 要生成的详情页所属的栏目 {@link SiteColumn}
	 * @param upNews 上一篇文章的 {@link News} 可为空，表示没有上一篇。没有时会显示返回列表
	 * @param nextNews 下一篇文章的 {@link News} 可为空，表示没有下一篇。没有时会显示返回列表
	 * @param pageHtml 当前页面使用的内容模版（替换过一些东西的）
	 * @param newsDataBean news_data 整理的，加入了json的数据对象
	 */
	public void generateNewsHtml(News news, SiteColumn siteColumn, News upNews, News nextNews, String pageHtml, NewsDataBean newsDataBean){
		//v4.9增加，提高容错
		if(newsDataBean == null){
			newsDataBean = new NewsDataBean(new NewsData());
		}
		pageHtml = Template.replaceAll(pageHtml, Template.regex("text"), replaceNewsText(newsDataBean.getText()));	//替换新闻内容的详情。新版本中这个标签已经废弃，改用了 {news.text}
		
		String generateUrl = "";
		if(siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
			generateUrl = "site/"+site.getId()+"/"+generateNewsPageHtmlName(siteColumn, news)+".html";
		}else{
			//若是当前页面是列表页的内容详情时，支持上一页、下一页的功能
			
			String upPage;	//上一页的超链接a标签
			String nextPage;	//下一页的超链接a标签
			String upPageUrl;	//上一页的url
			String nextPageUrl;
			
			if(upNews == null){
				if(this.generateUrlRule.equals("code")){
					//code.html
					upPage = "<a href=\""+siteColumn.getCodeName()+".html\" target=\"_black\">返回列表</a>";
					upPageUrl = siteColumn.getCodeName()+".html";
				}else{
					//id.html
					upPage = "<a href=\"lc"+siteColumn.getId()+"_1.html\" target=\"_black\">返回列表</a>";
					upPageUrl = siteColumn.getId()+"_1.html";
				}
			}else{
				upPage = "<a href=\""+upNews.getId()+".html\" target=\"_black\">"+upNews.getTitle()+"</a>";
				upPageUrl = upNews.getId()+".html";
			}
			if(nextNews == null){
				if(this.generateUrlRule.equals("code")){
					//code.html
					nextPage = "<a href=\""+siteColumn.getCodeName()+".html\" target=\"_black\">返回列表</a>";
					nextPageUrl = siteColumn.getCodeName()+".html";
				}else{
					//id.html
					nextPage = "<a href=\"lc"+siteColumn.getId()+"_1.html\" target=\"_black\">返回列表</a>";
					nextPageUrl = siteColumn.getId()+"_1.html";
				}	
			}else{
				nextPage = "<a href=\""+nextNews.getId()+".html\" target=\"_black\">"+nextNews.getTitle()+"</a>";
				nextPageUrl = nextNews.getId()+".html";
			}
			
			pageHtml = Template.replaceAll(pageHtml, Template.regex("upPage"), upPage);
			pageHtml = Template.replaceAll(pageHtml, Template.regex("nextPage"), nextPage);
			pageHtml = Template.replaceAll(pageHtml, Template.regex("upPageUrl"), upPageUrl);
			pageHtml = Template.replaceAll(pageHtml, Template.regex("nextPageUrl"), nextPageUrl);
			
			generateUrl = "site/"+site.getId()+"/"+generateNewsPageHtmlName(siteColumn, news)+".html";
		}
		AttachmentUtil.putStringFile(generateUrl, pageHtml);
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
		text = Template.replaceAll(text, regex("page.allRecordNumber"), page.getAllRecordNumber()+"");
		text = Template.replaceAll(text, regex("page.currentPageNumber"), page.getCurrentPageNumber()+"");
		text = Template.replaceAll(text, regex("page.lastPageNumber"), page.getLastPageNumber()+"");
		text = Template.replaceAll(text, regex("page.haveNextPage"), page.isHaveNextPage()+"");
		text = Template.replaceAll(text, regex("page.haveUpPage"), page.isHaveUpPage()+"");
		
		if(generateUrlRule.equals("code")){
			//code.html模式
			text = Template.replaceAll(text, regex("page.firstPage"), siteColumn.getCodeName()+".html");
			if(page.getNextPageNumber() > 1){
				//当下一页大于一页时，才会有页数，否则第一页是没有页数的
				text = Template.replaceAll(text, regex("page.nextPage"), siteColumn.getCodeName()+"_"+page.getNextPageNumber()+".html");
			}else{
				text = Template.replaceAll(text, regex("page.nextPage"), siteColumn.getCodeName()+".html");
			}
			
			if(page.getLastPageNumber() > 1){
				//当最后一页大于一页时，才会有页数，否则第一页是没有页数的
				text = Template.replaceAll(text, regex("page.lastPage"), siteColumn.getCodeName()+"_"+page.getLastPageNumber()+".html");
			}else{
				text = Template.replaceAll(text, regex("page.lastPage"), siteColumn.getCodeName()+".html");
			}
			
			if(page.getUpPageNumber() == 1){
				//如果上一页是第一页的话，是不加后面的_?的
				text = Template.replaceAll(text, regex("page.upPage"), siteColumn.getCodeName()+".html");
			}else{
				text = Template.replaceAll(text, regex("page.upPage"), siteColumn.getCodeName()+"_"+page.getUpPageNumber()+".html");
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
			text = Template.replaceAll(text, regex("page.upList"), upList);
			
			//下几页的页码跳转
			String nextList = "";
			for (int i = 0; i < page.getNextList().size(); i++) {
				TagA tagA = page.getNextList().get(i);
				nextList = nextList + "<li class=\"page_pageList\"><a href="+siteColumn.getCodeName()+"_"+tagA.getPageNumber()+".html>"+tagA.getTitle()+"</a></li>";
			}
			text = Template.replaceAll(text, regex("page.nextList"), nextList);
		}else{
			//通用的id.html模式
			text = Template.replaceAll(text, regex("page.firstPage"), "lc"+siteColumn.getId()+"_1.html");
			text = Template.replaceAll(text, regex("page.nextPage"), "lc"+siteColumn.getId()+"_"+page.getNextPageNumber()+".html");
			text = Template.replaceAll(text, regex("page.lastPage"), "lc"+siteColumn.getId()+"_"+page.getLastPageNumber()+".html");
			text = Template.replaceAll(text, regex("page.upPage"), "lc"+siteColumn.getId()+"_"+page.getUpPageNumber()+".html");
			
			//上几页的页码跳转
			String upList = "";
			for (int i = 0; i < page.getUpList().size(); i++) {
				TagA tagA = page.getUpList().get(i);
				upList = upList + "<li class=\"page_pageList\"><a href=lc"+siteColumn.getId()+"_"+tagA.getPageNumber()+".html>"+tagA.getTitle()+"</a></li>";
			}
			text = Template.replaceAll(text, regex("page.upList"), upList);
			
			//下几页的页码跳转
			String nextList = "";
			for (int i = 0; i < page.getNextList().size(); i++) {
				TagA tagA = page.getNextList().get(i);
				nextList = nextList + "<li class=\"page_pageList\"><a href=lc"+siteColumn.getId()+"_"+tagA.getPageNumber()+".html>"+tagA.getTitle()+"</a></li>";
			}
			text = Template.replaceAll(text, regex("page.nextList"), nextList);
		}
		
		return text;
	}
	
	/**
	 * 替换<!--SiteColumn_Start--> SiteColumn的区块
	 * @param templateHTML 模版的HTML代码
	 * @param columnNewsMap 对文章-栏目进行分类，以栏目codeName为key，将文章List加入进自己对应的栏目
	 * @param columnMap 以栏目codeName为key，将栏目加入进Map中。用codeName来取栏目
	 * @param columnTreeMap 栏目树，划分一级(顶级)、二级栏目 
	 * @param codeRetainDynamicTag 栏目代码 codeName=...是否保留动态标签，如果codeName为动态标签，包含{，
	 * 		<ul>
	 * 			<li>true：保留，不进行替换，原样返回</li>
	 * 			<li>false：不保留，进行替换，将替换好的内容返回</li>
	 * 		</ul>
	 * @param currentSiteColumn 服务于codeRetainDynamicTag，若允许替换动态标签时，动态栏目标签则会从此处来取。即当codeRetainDynamicTag为true时，此处也是用不到的，为null即可
	 * @param newsDataMap 对 newsDataList 网站文章的内容进行调整，调整为map key:newsData.id  value:newsData.text
	 */
	public String replaceSiteColumnBlock(String templateHTML, Map<String, List<News>> columnNewsMap, Map<String, SiteColumn> columnMap, Map<String, SiteColumnTreeVO> columnTreeMap, boolean codeRetainDynamicTag, SiteColumn currentSiteColumn, Map<Integer, NewsDataBean> newsDataMap){
		Pattern p = Pattern.compile("<!--SiteColumn_Start-->([\\s|\\S]*?)<!--SiteColumn_End-->");
        Matcher m = p.matcher(templateHTML);
        while(m.find()){
        	String siteColumnTemplate = m.group(1);	//得到其中某个栏目的模版内容
        	String codeName = Template.getConfigValue(siteColumnTemplate, "codeName");	//当前的栏目代码codeName
        	if(codeName.indexOf("{") > -1){
        		if(codeRetainDynamicTag){
        			//如果有动态标签，则保留此动态标签，不在替换范围内
        			//此次保留，不尽兴替换
        			continue;
        		}else{
        			if(currentSiteColumn != null){
        				//将动态调取的标签，替换为实际的值
        				codeName = replaceSiteColumnTag(codeName, currentSiteColumn);
        			}
        		}
        	}
        	
        	SiteColumn siteColumn = columnMap.get(codeName);
        	
        	//如果
        	if((siteColumn != null) || (siteColumn == null && codeName.equals(""))){
        		//如果指定栏目存在
        		//    或者codeName为空，即不传入，或没有codeName这个栏目标签，那么就是调用所有一级栏目
        		
        		//如果<!--subColumnList_Start-->存在，需要调出其子栏目列表
            	if(siteColumnTemplate.indexOf("<!--SubColumnList_Start-->") > -1){
            		String subColumnListTemp = Template.getAnnoCenterString(siteColumnTemplate, "SubColumnList");		//取得SubColumnList的子栏目，的每项展示的模版内容
            		
            		int number = Template.getConfigValue(subColumnListTemp, "number", 6);		//当前显示多少条记录
            		int start_number = Template.getConfigValue(subColumnListTemp, "start_number", 0);		//当前显示多少条记录
                	String itemTemp = Template.getAnnoCenterString(subColumnListTemp, "List");		//取得list的列表项内容
            		
            		StringBuffer itemBuffer = new StringBuffer();	//显示的栏目
            		List<SiteColumnTreeVO> scList = null;
            		if(codeName.length() == 0){
            			//调用所有顶级栏目
            			scList = new ArrayList<SiteColumnTreeVO>();
            			for (Map.Entry<String, SiteColumnTreeVO> entry : columnTreeMap.entrySet()) {
            				SiteColumnTreeVO sct = entry.getValue();
            				if(sct.getLevel() == 1 && sct.getSiteColumn().getUsed() - SiteColumn.USED_ENABLE == 0){
            					//是1级栏目，且栏目为显示（启用）状态
            					scList.add(sct);
            				}
            			}
            			
            			//一级栏目因为存储于map，是没有排序的，所以取一级栏目，取出后进行排序
            			Collections.sort(scList, new Comparator<SiteColumnTreeVO>() {
            	            public int compare(SiteColumnTreeVO sc1, SiteColumnTreeVO sc2) {
            	            	//按照发布时间正序排序，发布时间越早，排列越靠前
            	            	return sc1.getSiteColumn().getRank() - sc2.getSiteColumn().getRank();
            	            }
            	        });
            			
            		}else{
            			//调用二级栏目
            			SiteColumnTreeVO stvo = columnTreeMap.get(codeName);
            			if(stvo != null){
            				scList = stvo.getList();	//取得子栏目的数据源
            			}else{
            				scList = new ArrayList<SiteColumnTreeVO>();
            			}
            		}
//            		List<SiteColumnTreeVO> scList = columnTreeMap.get(codeName).getList();	//取得子栏目的数据源
            		for (int i = 0; i < scList.size(); i++) {
            			//获取子栏目信息
            			SiteColumn subColumn = scList.get(i).getSiteColumn();
            			//栏目状态为启用时，才会列出来。隐藏的则不会列出
            			
            			//v4.10 当栏目的显示（旧的 used 字段）为不显示、或者 templateCodeColumnUsed 为不显示
            			if((subColumn.getUsed() != null && subColumn.getUsed() - SiteColumn.USED_UNABLE == 0) || (subColumn.getTemplateCodeColumnUsed() != null && subColumn.getTemplateCodeColumnUsed() - SiteColumn.USED_UNABLE == 0)){
            				//不显示，那么直接吧模版代码赋予 subColumnNewsTemp ,准备进行下一步的文章替换
            			}else{
            				//显示
            				//取到子栏目中，某个栏目的，替换掉栏目信息后的内容(如果其中有信息列表标签，还要替换)
                			String subColumnNewsTemp = replaceSiteColumnTag(subColumnListTemp, subColumn);
                			String str = replaceSiteColumnBlock_replaceNews(subColumnNewsTemp,start_number, number, itemTemp, columnNewsMap, subColumn, newsDataMap);
                			//将子栏目替换栏目标签后，加入 itemBuffer
                			itemBuffer.append(str);
            			}
            			
            		}
            		siteColumnTemplate = StringUtil.subStringReplace(siteColumnTemplate, "<!--SubColumnList_Start-->", "<!--SubColumnList_End-->", itemBuffer.toString());
            	}
            	
        	}
        	
        	if(siteColumn == null){
//        		siteColumnTemplate = "栏目代码"+codeName+"不存在";
        	}else{
            	//如果<!--List_Start-->存在，则需要News信息列表
            	if(siteColumnTemplate.indexOf("<!--List_Start-->") > -1){
            		int number = Template.getConfigValue(siteColumnTemplate, "number", 6);		//当前显示多少条记录
            		int start_number = Template.getConfigValue(siteColumnTemplate, "start_number", 0);		//当前从第几条开始显示
            		String itemTemp = Template.getAnnoCenterString(siteColumnTemplate, "List");		//取得list的列表项内容
                	siteColumnTemplate = replaceSiteColumnBlock_replaceNews(siteColumnTemplate,start_number, number, itemTemp, columnNewsMap, siteColumn, newsDataMap);
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
	 * @param start_number 当前文章列表显示的记录，从第几条开始显示，如 1，则略过第一条，从第二条开始显示。
	 * @param number 当前文章列表显示多少条记录
	 * @param itemTemp 取得list的列表项内容，包含动态标签的列表的模板
	 * @param columnNewsMap key:栏目代码， value：文章列表
	 * @param siteColumn 当前栏目
	 * @param newsDataMap 对 newsDataList 网站文章的内容进行调整，调整为map key:newsData.id  value:newsDataBean
	 * @return 替换好的html
	 */
	private String replaceSiteColumnBlock_replaceNews(String newsListTemplate, int start_number, int number, String itemTemp, Map<String, List<News>> columnNewsMap, SiteColumn siteColumn, Map<Integer, NewsDataBean> newsDataMap){
		if(newsListTemplate == null){
			return "";
		}
		//如果<!--List_Start-->存在，则需要News信息列表
    	if(newsListTemplate.indexOf("<!--List_Start-->") > -1){
        	StringBuffer itemBuffer = new StringBuffer();	//显示的列表内容
        	List<News> list = columnNewsMap.get(siteColumn.getCodeName());	//获取要显示的列表的数据源
    		
        	if(start_number < 0){
        		start_number = 0;
        	}
        	int zongshu = number + start_number;	//从start_number 开始，去到第几个为止。
        	for (int i = start_number; i < list.size() && i < zongshu; i++) {
    			News news = list.get(i);
        		itemBuffer.append(replaceNewsTag(itemTemp, news, siteColumn, newsDataMap.get(news.getId())));
    		}
    		newsListTemplate = StringUtil.subStringReplace(newsListTemplate+" ", "<!--List_Start-->", "<!--List_End-->", itemBuffer.toString());
    	}
    	return newsListTemplate;
	}
	
}
