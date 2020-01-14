 package com.xnx3.wangmarket.admin.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.BaseVO;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.func.TextFilter;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.cache.pc.IndexNews;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.pluginManage.interfaces.manage.NewsPluginManage;
import com.xnx3.wangmarket.admin.service.InputModelService;
import com.xnx3.wangmarket.admin.service.NewsService;
import com.xnx3.wangmarket.admin.service.SiteColumnService;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.service.TemplateService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;
import com.xnx3.wangmarket.admin.vo.NewsVO;
import com.xnx3.wangmarket.admin.vo.SiteColumnTreeVO;
import com.xnx3.wangmarket.admin.vo.bean.NewsInit;
import net.sf.json.JSONObject;

/**
 * 图文、新闻
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/news")
public class NewsController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	@Resource
	private NewsService newsService;
	@Resource
	private TemplateService templateService;
	@Resource
	private SiteColumnService siteColumnService;
	@Resource
	private InputModelService inputModelService;
	
	
	/**
	 * 创建、修改页面提交保存
	 * @return
	 */
	@RequestMapping(value="saveNews${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO saveNews(News s,
			@RequestParam(value = "text", required = false , defaultValue="") String text,
			HttpServletRequest request, HttpServletResponse response, Model model){
		String title = "";
		if(s.getTitle() != null && s.getTitle().length()>0){
			title = StringUtil.filterXss(s.getTitle());
		}else{
			return error("请输入您页面的名字");
		}
		if(title.length() == 0){
			return error("请输入您页面的名字");
		}
		
		Site site = getSite();
		SiteColumn siteColumn = sqlService.findById(SiteColumn.class, s.getCid());
		if(siteColumn == null){
			return error("信息所属栏目不存在");
		}
		if(siteColumn.getSiteid() - site.getId() != 0){
			return error("信息所属栏目不属于您，无法操作");
		}
		
		
		News news;
		NewsData newsData;
		if(s.getId() != null && s.getId() > 0){
			//编辑修改
			news = sqlService.findById(News.class, s.getId());
			if(news == null){
				return error("要操作的页面不存在");
			}
			if(news.getSiteid() - site.getId() != 0){
				return error("页面不属于您，无法操作！");
			}
			newsData = sqlService.findById(NewsData.class, s.getId());
			
			//v4.9增加，提高容错。当news表有，单news_data没有时，自动创建
			if(newsData == null){
				newsData = new NewsData();
				newsData.setId(news.getId());
			}
		}else{
			//新增
			news = new News();
			news.setCid(siteColumn.getId());	//默认没有栏目绑定
			news.setCommentnum(0);
			news.setOpposenum(0);
			news.setReadnum(0);
			news.setStatus(News.STATUS_NORMAL);
			news.setType(SiteColumn.TYPE_LIST);
			news.setUserid(getUserId());
			news.setAddtime(DateUtil.timeForUnix10());
			news.setSiteid(siteColumn.getSiteid());
			
			newsData = new NewsData();
		}
		
		title = StringUtil.filterXss(StringUtil.filterHtmlTag(title));
		if(title != null && title.length() > 60){
			title = title.substring(0, 60);
		}
		
		text = newsService.setText(text, site);
		
		//内容过滤HTML标签
		String textFilterHtml = StringUtil.filterHtmlTag(text);
		textFilterHtml = StringUtil.filterEnglishSpecialSymbol(textFilterHtml);	//过滤英文状态下得特殊符号
		
		/*
		 * 简介
		 * 简介修改有两种模式
		 * 1.表单提交的简介为空时，会自动从正文中截取前160个字符作为简介
		 * 2.表单提交的简介不为空，会将提交的简介过滤HTML标签后保存
		 * 
		 */
		String intro = title;
		//当表单提交的简介没有时，并且之前若保存了信息且数据库的信息的简介也没有时，才会自动截取内容区域的文字
		if(s.getIntro() == null || s.getIntro().length() == 0){
			int textFilterHtmlLength = textFilterHtml.length();
			if(textFilterHtmlLength > 200){
				intro = textFilterHtml.substring(0, 200);
			}else{
				intro = textFilterHtml;
			}
			intro = intro.replaceAll("nbsp", "");	//过滤掉nbsp标签
			news.setIntro(intro);
		}else{
			//修改News，若表单中穿过来的简介是有值得，那么将表单中传过来的简介的数值进行过滤HTML标签后保存入数据库
			news.setIntro(StringUtil.filterHtmlTag(s.getIntro()));
		}
		
		news.setTitle(title);
		news.setReserve1(s.getReserve1() == null? "":s.getReserve1());
		news.setReserve2(s.getReserve2() == null? "":s.getReserve2());
		
		//上传的图片
		String titlepic = StringUtil.filterXss(s.getTitlepic());
		news.setTitlepic(titlepic == null ? "":titlepic);
		
		//插件拦截处理
		try {
			NewsPluginManage.newsSaveBefore(request, news);
		} catch (InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		sqlService.save(news);
		if(news.getId() > 0){
			
			//插件触发
			try {
				NewsPluginManage.newsSaveFinish(request, news);
			} catch (InstantiationException | IllegalAccessException
					| NoSuchMethodException | SecurityException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			
			//v4.6增加
			String extend = "";
			Map<String, String[]> extendMap = new HashMap<String, String[]>();
			for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
				if(entry.getKey().indexOf("extend.") > -1){
//					System.out.println("newssave---->"+entry.getKey());
					//保存入时，将 extend. 过滤掉
					extendMap.put(entry.getKey().replace("extend.", ""), entry.getValue());
				}
			}
			
			//有扩展的自定义字段，则进行json转换
			if(extendMap.size() > 0){
				JSONObject extendJson = JSONObject.fromObject(extendMap);
				extend = extendJson.toString();
			}
			newsData.setExtend(extend);
			
			
			boolean have = TextFilter.filter(request, "文章信息发现涉嫌违规："+news.getTitle(), SystemUtil.get("MASTER_SITE_URL")+"admin/news/view.do?id="+news.getId(), news.getTitle()+textFilterHtml+StringUtil.filterEnglishSpecialSymbol(StringUtil.filterHtmlTag(extend)));
			if(have){
				//写入news的合法性字段
				news.setLegitimate(News.LEGITIMATE_NO);
				sqlService.save(news);
			}
			
			newsData.setId(news.getId());
			newsData.setText(text);
			
			//插件拦截
			try {
				NewsPluginManage.newsDataSaveBefore(request, newsData);
			} catch (InstantiationException | IllegalAccessException
					| NoSuchMethodException | SecurityException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			sqlService.save(newsData);
			
			//插件触发
			try {
				NewsPluginManage.newsDataSaveFinish(request, newsData);
			} catch (InstantiationException | IllegalAccessException
					| NoSuchMethodException | SecurityException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			if(s.getId() == null || s.getId() == 0){
				ActionLogUtil.insertUpdateDatabase(request, news.getId(), "新增文章成功", news.getTitle());
			}else{
				ActionLogUtil.insertUpdateDatabase(request, news.getId(), "修改文章成功", news.getTitle());
			}
			
			/**
			 * 生成静态页面
			 */
			newsService.generateViewHtml(site, news,siteColumn, new NewsDataBean(newsData), request);	//生成当前内容页
			
			//如果是通用模式，还要生成列表页。当然，CMS模式是不会生成列表页跟首页的
			if(!Func.isCMS(site)){
				List<News> newsList = sqlService.findBySqlQuery("SELECT * FROM news WHERE cid = "+siteColumn.getId() + " AND status = "+News.STATUS_NORMAL+" ORDER BY id DESC", News.class);
				//如果是通用模版，全自动式模版
				newsService.generateListHtml(site, siteColumn,newsList, request);		//栏目页面
				if(site.getClient() - Site.CLIENT_PC == 0){
					//是pc模式，还要刷新首页的数据
					IndexNews.refreshIndexData(site, siteColumn, newsList);			//PC首页局部刷新
				}
				
			}else{
//				return success(model, "保存成功！","news/listForTemplate.do?cid="+news.getCid());
			}
			return success();
		}else{
			return error("保存失败！");
		}
		
	}
	
	/**
	 * 根据news.id删除信息
	 */
	@RequestMapping(value="deleteNews${url.suffix}", method = RequestMethod.POST)
	public String deleteNews(HttpServletRequest request,Model model,
			@RequestParam(value = "id", required = false , defaultValue="0") int id){
		NewsVO vo = newsService.deleteNews(id, true);
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			News news = vo.getNews();
			
			SiteColumn siteColumn = sqlService.findById(SiteColumn.class, news.getCid());
			Site site = sqlService.findById(Site.class, siteColumn.getSiteid());
			List<News> newsList = sqlService.findBySqlQuery("SELECT * FROM news WHERE cid = "+siteColumn.getId()+" ORDER BY id DESC", News.class);
			
			//刷新列表页
			newsService.generateListHtml(site, siteColumn);
			
			//刷新首页的列表数据
			IndexNews.refreshIndexData(site, siteColumn, newsList);
			
			//日志
			ActionLogUtil.insertUpdateDatabase(request, news.getId(), "删除文章成功", news.getTitle());
			
			//删除OSS的html、头图文件
			AttachmentUtil.deleteObject("site/"+news.getSiteid()+"/"+news.getId()+".html");
			if(news.getTitlepic() != null && news.getTitlepic().length() > 0 && news.getTitlepic().indexOf("http:") == -1){
				AttachmentUtil.deleteObject("site/"+news.getSiteid()+"/news/"+news.getTitlepic());
			}
			
			//刷新sitemap
			siteService.refreshSiteMap(site);
			
			return success(model, "删除信息成功","news/list.do?cid="+siteColumn.getId()+"&editMode=edit&client=pc");
		}else{
			return error(model, vo.getInfo());
		}
	}
	
	/**
	 * 文章列表
	 */
	@RequestMapping("/listForTemplate${url.suffix}")
	public String list(HttpServletRequest request,Model model){
		Site site = getSite();
		
		//如果传入了cid，获取到当前的siteColumn信息
	    String cidPar = request.getParameter("cid");
	    int cid = 0;
	    SiteColumn siteColumn = null;
	    if(cidPar != null){
	    	cid = Lang.stringToInt(cidPar, 0);
	    	if(cid > 0){
	    		siteColumn = sqlService.findById(SiteColumn.class, cid);
	    		if(siteColumn == null){
	    			return error(model, "要查看的栏目不存在");
	    		}
	    		if(siteColumn.getSiteid() - getSiteId() != 0){
	    			return error(model, "该栏目不属于您，无法查看");
	    		}
	    		model.addAttribute("siteColumn", siteColumn);
	    		ActionLogUtil.insert(request, siteColumn.getId(), "查看指定栏目下的文章列表", siteColumn.getName());
	    	}
	    }
	    if(siteColumn != null){
	    	//如果是CMS模版网站，且使用模板式编辑，那么没有列表页面，直接到模版页面的可视化编辑中
	    	if(Func.isCMS(site) && (siteColumn.getEditMode() == null || siteColumn.getEditMode() - SiteColumn.EDIT_MODE_TEMPLATE == 0)){
	    		model.addAttribute("autoJumpTemplateEdit", "<script>editText('"+siteColumn.getTemplatePageViewName()+"')</script>");	//自动跳转到模版页面的编辑，执行js
	    		return "news/listForTemplate";
	    	}
	    }
	    
	    if(cid == 0){
	    	ActionLogUtil.insert(request, "查看网站内所有文章的列表");
	    }
		
	    Sql sql = new Sql(request);
	    sql.setSearchTable("news");
	    sql.setSearchColumn(new String[]{"type=","title","cid="});
	    sql.appendWhere("siteid = "+getSiteId());
	    int count = sqlService.count("news", sql.getWhere());
	    Page page = new Page(count, SystemUtil.getInt("LIST_EVERYPAGE_NUMBER"), request);
	    //创建查询语句，只有SELECT、FROM，原生sql查询。其他的where、limit等会自动拼接
	    sql.setSelectFromAndPage("SELECT * FROM news", page);
	    
	    //排序方式，通过栏目设置的内容排序，进行判断
	    if(siteColumn != null && siteColumn.getListRank() != null){
	    	if(siteColumn.getListRank() - SiteColumn.LIST_RANK_ADDTIME_ASC == 0){
	    		sql.setDefaultOrderBy("addtime ASC");
	    	}else{
	    		sql.setDefaultOrderBy("addtime DESC");
	    	}
	    }else{
	    	//v4.4版本以前，没有自定义内容排序功能，只有按时间倒序排列
	    	sql.setDefaultOrderBy("addtime DESC");
	    }
	    
	    //因联合查询，结果集是没有实体类与其对应，故而用List<Map>接收
	    List<News> list = sqlService.findBySql(sql, News.class);
	     
	    //从缓存中调取当前网站栏目
	    //从缓存中获取栏目树列表
    	List<SiteColumnTreeVO> siteColumnTreeVOList = siteColumnService.getSiteColumnTreeVOByCache();
	    if(siteColumnTreeVOList.size() > 0){
	    	StringBuffer columnTreeSB = new StringBuffer();
		    for (int i = 0; i < siteColumnTreeVOList.size(); i++) {
		    	SiteColumnTreeVO sct = siteColumnTreeVOList.get(i);
		    	//v4.10 增加 adminNewsUsed
		    	if(sct.getSiteColumn().getUsed() - SiteColumn.USED_UNABLE == 0 || sct.getSiteColumn().getAdminNewsUsed() - SiteColumn.USED_UNABLE == 0){
		    		continue;
		    	}
		    	
		    	//如果有下级栏目，也将下级栏目列出来
		    	if(sct.getList().size() > 0){
		    		columnTreeSB.append("<li class=\"layui-nav-item\" id=\"super"+sct.getSiteColumn().getId()+"\"><a href=\"javascript:;\" class=\"dltitle\">"+sct.getSiteColumn().getName()+"</a><dl class=\"layui-nav-child\" style=\"background-color: #EAEDF1;\">");
		    		for (int j = 0; j < sct.getList().size(); j++) {
		    			SiteColumn s = sct.getList().get(j).getSiteColumn();
		    			if(s == null){
		    				//理论上不存在
		    				continue;
		    			}
		    			//v4.10 增加 adminNewsUsed
		    			if(s.getUsed() - SiteColumn.USED_UNABLE == 0 || s.getAdminNewsUsed() - SiteColumn.USED_UNABLE == 0){
				    		continue;
				    	}
		    			columnTreeSB.append("<dd>"+getLeftNavColumnA(cid, s, sct.getSiteColumn().getId())+"</dd>");
					}
		    		columnTreeSB.append("</dl></li>");
		    		
		    	}else{
		    		columnTreeSB.append("<li class=\"layui-nav-item\">"+getLeftNavColumnA(cid, sct.getSiteColumn(), 0)+"</li>");
		    	}
			}
		    model.addAttribute("columnTreeNav", columnTreeSB.toString());
	    }else{
	    	return error(model, "您现在还没有创建栏目，既然没有栏目，那要管理的内容是属于哪的呢？内容必须有所属的栏目，请先去建立栏目吧","template/welcome.do");
	    }
	    
	    //将数据记录传到页面以供显示
	    model.addAttribute("list", list);
	    //将分页信息传到页面以供显示
	    model.addAttribute("page", page);
	    model.addAttribute("siteDomain", Func.getDomain(site));	//访问域名
	    model.addAttribute("site", site);
	    model.addAttribute("AttachmentFileUrl", AttachmentUtil.netUrl());
	    return "news/listForTemplate";
	}
	
	/**
	 * 根据不同栏目，获取其左侧导航修改内容时的href属性
	 * @param cid 当前用户查看的文章列表所属的栏目id
	 * @param sc 遍历输出栏目列表，当前轮到了哪个栏目，跟上面的cid进行比较，若是一样，则当前选中的是这个栏目
	 * @param superid 当前栏目的上级栏目id
	 * @return
	 */
	private String getLeftNavColumnA(int cid, SiteColumn sc, int superid){
		String href = "";
		
		if(sc.getType() - SiteColumn.TYPE_LIST == 0 || sc.getType() - SiteColumn.TYPE_NEWS == 0 || sc.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
			//判断条件后面带着新闻、图文，纯粹是兼容v4.6以前版本、或者以前的模版
			href = "listForTemplate.do?cid="+sc.getId();
		}else if (sc.getType() - SiteColumn.TYPE_ALONEPAGE == 0 || sc.getType() - SiteColumn.TYPE_PAGE == 0) {
			//判断条件后面带着TYPE_PAGE，纯粹是兼容v4.6以前版本、或者以前的模版
//			href = "newsForTemplate.do?alonePageCid="+sc.getId();	这样直接编辑内容
			href = "listForTemplate.do?cid="+sc.getId();	//这样先进入列表页面
		}else{
			href = "javascript:layer.msg('此栏目类型未知！修改本栏目方式：<br/>栏目管理，找到相应的栏目，进行修改');";
		}
		
		String script = "";	//如果当前栏目为子栏目，此处要将其父栏目下所有子栏目都显示出来
		if(superid != 0 && cid - sc.getId() == 0){
			script = "<script>document.getElementById(\"super"+superid+"\").className+=\" layui-nav-itemed\";</script>";
		}
		
		String selectAStyle = "";
		if(cid > 0){
			if(sc.getId() - cid == 0){
				selectAStyle = " style=\"background-color: #fff; color:#222;\" ";
			}
		}
		
		return "<a href=\""+href+"\" "+selectAStyle+">"+sc.getName()+"</a>"+script;
	}

	/**
	 * 创建、修改新闻， id跟cid必须有一个传递过来。因为要确定 {@link SiteColumn}的属性到底是新闻还是图文
	 * <br/>（原newsForTemplate.do ）
	 * @param id {@link News}.id 若id为空或者0，那么便是新增栏目，那么下面的cid便不能为空
	 * @param cid {@link SiteColumn}.id 若id传入了值，此处可忽略
	 */
	@RequestMapping(value="news${url.suffix}")
	public String news(HttpServletRequest request,
			@RequestParam(value = "id", required = false , defaultValue="0") int id,
			@RequestParam(value = "cid", required = false , defaultValue="0") int cid,
			Model model){
		NewsInit ni = newsService.news(request, id, cid, model);
		if(ni.getResult() == NewsInit.SUCCESS){
			//则加载输入模型。无论CMS模式，还是手机、电脑模式，都要加载，因为都要输入
			String inputModelText = inputModelService.getInputModelTextByIdForNews(ni);
			model.addAttribute("inputModelText", inputModelText);
			
			ActionLogUtil.insert(request, "打开创建、修改文章页面");
			
			//可上传的后缀列表
			model.addAttribute("ossFileUploadImageSuffixList", Global.ossFileUploadImageSuffixList);
			//可上传的文件最大大小(KB)
			model.addAttribute("maxFileSizeKB", AttachmentUtil.getMaxFileSizeKB());
			return "news/newsForTemplate";
		}else{
			return error(model, ni.getInfo());
		}
	}
	
	/**
	 * 根据news.id删除信息,Ajax方式，返回json
	 * @param id 要删除的news.id
	 */
	@RequestMapping(value="deleteNewsForAjax${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public NewsVO deleteNewsForAjax(HttpServletRequest request,HttpServletResponse response, Model model,
			@RequestParam(value = "id", required = false , defaultValue="0") int id){
		NewsVO vo = newsService.deleteNews(id, true);
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			News news = vo.getNews();
			
			//日志
			ActionLogUtil.insertUpdateDatabase(request, news.getId(), "删除文章", news.getTitle());
			
			//删除OSS的html、头图文件
			AttachmentUtil.deleteObject("site/"+news.getSiteid()+"/"+news.getId()+".html");
			if(news.getTitlepic() != null && news.getTitlepic().length() > 0 && news.getTitlepic().indexOf("http:") == -1){
				AttachmentUtil.deleteObject("site/"+news.getSiteid()+"/news/"+news.getTitlepic());
			}
			
			//插件拦截
			try {
				NewsPluginManage.newsDeleteFinish(request, news);
			} catch (InstantiationException | IllegalAccessException
					| NoSuchMethodException | SecurityException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			if(!Func.isCMS(getSite())){
				//如果不是cms模式，那么还要刷新一堆
				SiteColumn siteColumn = sqlService.findById(SiteColumn.class, news.getCid());
				Site site = sqlService.findById(Site.class, siteColumn.getSiteid());
				List<News> newsList = sqlService.findBySqlQuery("SELECT * FROM news WHERE cid = "+siteColumn.getId()+" ORDER BY id DESC", News.class);
				
				//刷新列表页
				newsService.generateListHtml(site, siteColumn);
				
				//刷新首页的列表数据
				IndexNews.refreshIndexData(site, siteColumn, newsList);
			}
		}
		
		return vo;
	}
	
	/**
	 * 文章重定向，用于传入news.id、news.type、news.cid 就能打开网站的前端网页
	 * @param newsId 要打开的文章的news.id
	 * @param cid news.cid
	 * @param type news.type
	 */
	@RequestMapping(value="redirectByNews${url.suffix}")
	public String redirectByNews(Model model,HttpServletRequest request,
			@RequestParam(value = "newsId", required = false , defaultValue="0") int newsId,
			@RequestParam(value = "cid", required = false , defaultValue="0") int cid,
			@RequestParam(value = "type", required = false , defaultValue="0") short type){
		String generateUrlRule = "id";	//url生成模式，分id、code
		Site site = getSite();
		String url = "http://"+Func.getDomain(site)+"/";
		
		//从栏目缓存中，取出栏目信息
		Map<Integer, SiteColumn> columnMap = siteColumnService.getSiteColumnMapByCache();
		SiteColumn sc = columnMap.get(cid);
		if(sc == null){
			return error(model, "文章所属栏目未发现");
		}
		if(sc.getUseGenerateView() - SiteColumn.USED_UNABLE == 0){
			return error(model, "该文章所属的栏目，设置了不生成文章详情页，正在跳至列表页",sc.getCodeName()+".html?domain="+site.getDomain()+"."+G.getFirstAutoAssignDomain());
		}
		if(site.getClient() - Site.CLIENT_CMS == 0){
			//如果是CMS模式网站，则需要判断
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
		}else{
			//如果是电脑模式、手机模式，那肯定是用id模式了
		}
		
		
		//访问的html文件名，不含后缀
		String fileName = "";
		//判断是否是独立页面，若是独立页面，需要用 c +cid .html， 或者使用code.html
		if(type - SiteColumn.TYPE_PAGE == 0 || type - SiteColumn.TYPE_ALONEPAGE == 0){
			if(generateUrlRule.equals("code")){
				fileName = sc.getCodeName();
				url = url + sc.getCodeName() + ".html"; 
			}else{
				fileName = "c" + cid;
				url = url + "c" + cid + ".html";
			}
		}else{
			fileName = newsId+"";
			url = url + newsId + ".html";
		}
		
		ActionLogUtil.insert(request, newsId, "网站管理后台查看文章页面", url);
		return redirect(fileName+".html?domain="+site.getDomain()+"."+G.getFirstAutoAssignDomain());
	}
	

	/**
	 * 通过栏目id来修改文章，当然，要修改的这条文章一定是独立栏目，独立页面的，栏目里面只有一个页面，才能直接定向到要修改的页面上
	 * <br/>v3.0增加
	 * @param cid {@link SiteColumn}.id 要修改的栏目页面的栏目id
	 */
	@RequestMapping("updateNewsByCid${url.suffix}")
	public String updateNewsByCid(HttpServletRequest request,
			@RequestParam(value = "cid", required = false , defaultValue="0") int cid,
			Model model){
		Site site = getSite();
		if(cid == 0){
			return error(model, "要修改哪个栏目呢？");
		}
		//数据库取这个栏目的信息
		SiteColumn column = sqlService.findById(SiteColumn.class, cid);
		if(column == null){
			ActionLogUtil.insertError(request, "要修改的栏目不存在,cid:"+cid);
			return error(model, "要修改的栏目不存在");
		}
		if(column.getSiteid() - site.getId() != 0 ){
			ActionLogUtil.insertError(request, "此栏目不属于您，无法修改！cid:"+cid+", column:"+column.toString());
			return error(model, "此栏目不属于您，无法修改！");
		}
		//取这个news的id
		News news = sqlService.findAloneBySqlQuery("SELECT * FROM news WHERE cid = "+cid, News.class);
		if(news == null){
			ActionLogUtil.insertError(request, "要修改的文章不存在！规则里，在建立栏目类型为独立页面的栏目时，就会自动创建一篇文章，所以，此处既然栏目已经存在了，文章也应该是存在的！很可能在创建独立页面的时候，自动创建文章出错了，或者在哪删除了文章");
			return error(model, "要修改的文章不存在！");
		}
		return redirect("news/news.do?id="+news.getId());
	}
	
	/**
	 * 将某篇文章，转移到其他栏目中去
	 * @param newsid 要转移的文章的id， {@link News}.id
	 * @param columnid 当前要转移的文章所在的栏目id，文章没转移前在哪个栏目
	 */
	@RequestMapping(value="newsChangeColumnForSelectColumn${url.suffix}")
	public String newsChangeColumnForSelectColumn(HttpServletRequest request,Model model,
			@RequestParam(value = "newsid", required = true) int newsid,
			@RequestParam(value = "columnid", required = true) int columnid){
		SiteColumn siteColumn = siteColumnService.getSiteColumnByCache(columnid);
		if(siteColumn != null && (siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_PAGE == 0)){
			return error(model, "文章所属栏目的类型为独立页面，此种类型栏目内的文章无法转移！");
		}
		
		//从缓存中调取当前网站栏目
	    //从缓存中获取栏目树列表
    	List<SiteColumnTreeVO> siteColumnTreeVOList = siteColumnService.getSiteColumnTreeVOByCache();
	    if(siteColumnTreeVOList.size() > 0){
	    	StringBuffer columnTreeSB = new StringBuffer();
		    for (int i = 0; i < siteColumnTreeVOList.size(); i++) {
		    	SiteColumnTreeVO sct = siteColumnTreeVOList.get(i);
		    	
		    	//如果有下级栏目，也将下级栏目列出来
		    	if(sct.getList().size() > 0){
		    		columnTreeSB.append(newsChangeColumnForSelectColumn_Format(sct.getSiteColumn(), columnid, 1, true));
		    		for (int j = 0; j < sct.getList().size(); j++) {
		    			
		    			SiteColumn s = sct.getList().get(j).getSiteColumn();
		    			columnTreeSB.append(newsChangeColumnForSelectColumn_Format(s, columnid, 2, false));
					}
		    		
		    	}else{
		    		columnTreeSB.append(newsChangeColumnForSelectColumn_Format(sct.getSiteColumn(), columnid, 1, false));
		    	}
			}
		    model.addAttribute("columnTreeNav", columnTreeSB.toString());
	    }else{
	    	return error(model, "您现在还没有创建栏目，既然没有栏目，那要管理的内容是属于哪的呢？内容必须有所属的栏目，请先去建立栏目吧");
	    }
	    
	    ActionLogUtil.insert(request, newsid, "打开文章可转移的栏目选择页面");
	    model.addAttribute("newsid", newsid);
		return "news/newsChangeColumnForSelectColumn";
	}
	
	/**
	 * 只服务于  {@link #newsChangeColumnForSelectColumn(HttpServletRequest, Model, int)} 对其栏目进行判断，筛选
	 * @param column 该网站所有栏目，传入后根据栏目进行判断这个栏目是否可以选择进行移动。当然，只能是列表栏目才能进行移动
	 * @param currentColumnId 当前文章所在的栏目id。再移动栏目时，文章当前所在的栏目就是不能选择的
	 * @param grade 栏目级别，1、2、代表几级栏目，如，1便是顶级栏目，2便是2级栏目
	 * @param haveSubColumn 是否有下级栏目 true：有下级栏目
	 */
	private String newsChangeColumnForSelectColumn_Format(SiteColumn column, int currentColumnId,int grade, boolean haveSubColumn){
		boolean edit = false;
		if(haveSubColumn){
			edit = false;
		}else{
			if(column.getType() - SiteColumn.TYPE_LIST == 0 || column.getType() - SiteColumn.TYPE_IMAGENEWS == 0 || column.getType() - SiteColumn.TYPE_NEWS == 0){
				edit = true;
			}else{
				edit = false;
			}
		}
		
		String columnName = filter(column.getName());
		if(grade == 2){
			columnName = "<span style=\"padding-left:15px;\">"+columnName+"</span>";
		}
		
		if(edit){
			return "<tr><td onclick=\"selectColumn("+column.getId()+");\" style=\"cursor:pointer;\">"+columnName+"</td></tr>\n";
		}else{
			return "<tr><td style=\"color:gray; opacity: 0.5;\">"+columnName+"</td></tr>\n";
		}
	}
    
	
	/**
	 * 服务于 {@link #newsChangeColumnForSelectColumn(HttpServletRequest, Model, int, int)} 将文章转移到其他栏目，选择后提交保存
	 * @param newsid 要转移的文章id，news.id
	 * @param targetColumnId 要转移到的目标栏目id
	 */
	@RequestMapping(value="newsChangeColumnForSelectColumnSubmit${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO newsChangeColumnForSelectColumnSubmit(HttpServletRequest request,Model model,
			@RequestParam(value = "newsid", required = true) int newsid,
			@RequestParam(value = "targetColumnId", required = true) int targetColumnId){
		//判断要转移的文章是否存在以及是否属于本人
		News news = sqlService.findById(News.class, newsid);
		if(news == null){
			return error("要转移的文章不存在");
		}
		Site site = getSite();
		if(news.getSiteid() - site.getId() != 0){
			return error("该文章不属于您，无法操作");
		}
		
		//判断要转移至的栏目，是否存在，以及是否属于本人
		SiteColumn column = sqlService.findById(SiteColumn.class, targetColumnId);
		if(column == null){
			return error("目标栏目不存在");
		}
		if(column.getSiteid() - site.getId() != 0){
			return error("目标栏目不属于您，无法操作");
		}
		
		//检测完毕，进行转移栏目
		news.setCid(targetColumnId);
		sqlService.save(news);
		
		ActionLogUtil.insertUpdateDatabase(request, news.getId(), "将文章转移栏目", "将文章"+news.getTitle()+"转移到栏目["+column.getName()+"]中");
		return success();
	}
	
	
	/**
	 * 更改文章发布的时间
	 * @param id news.id
	 * @param addtime 修改后的时间，格式如 2018-12-12 22:22:22
	 */
	@RequestMapping(value="updateAddtime${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO updateAddtime(HttpServletRequest request,HttpServletResponse response, 
			@RequestParam(value = "id", required = false , defaultValue="0") int id,
			@RequestParam(value = "addtime", required = false , defaultValue="") String addtime){
		if(id < 1){
			return error("请传入要操作的文章编号");
		}
		if(addtime.length() == 0){
			return error("请传入修改后的时间");
		}
		
		News news = sqlService.findById(News.class, id);
		if(news == null){
			return error("要操作的文章不存在");
		}
		if(news.getSiteid() - getSiteId() != 0){
			return error("文章不属于您，无法操作");
		}
		
		//将2018-12-12 22:22:22 转化为10位时间戳
		int time = DateUtil.StringToInt(addtime, "yyyy-MM-dd HH:mm:ss");
		news.setAddtime(time);
		
		//插件拦截处理
		try {
			NewsPluginManage.newsSaveBefore(request, news);
		} catch (InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		sqlService.save(news);
		
		//插件拦截处理
		try {
			NewsPluginManage.newsSaveFinish(request, news);
		} catch (InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		//记录日志
		ActionLogUtil.insertUpdateDatabase(request, news.getId(), "更改文章发布时间", time+"");
		
		return success();
	}
	

	/**
	 * v3.11 增加，适应v3.10及以前版本
	 */
	@RequestMapping("*.html")
	public String html(Model model, HttpServletRequest request){
		String htmlFiles = request.getServletPath();
		String[] hfs = htmlFiles.split(".");
		String htmlFile = hfs[0];
		
		return redirect("sites/html.do?htmlFile="+htmlFile);
	}
	
}