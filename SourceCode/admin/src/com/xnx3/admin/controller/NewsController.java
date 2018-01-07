package com.xnx3.admin.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.BaseVO;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.func.TextFilter;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.admin.Func;
import com.xnx3.admin.G;
import com.xnx3.admin.cache.pc.IndexNews;
import com.xnx3.admin.entity.News;
import com.xnx3.admin.entity.NewsData;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.entity.SiteColumn;
import com.xnx3.admin.service.InputModelService;
import com.xnx3.admin.service.NewsService;
import com.xnx3.admin.service.SiteColumnService;
import com.xnx3.admin.service.SiteService;
import com.xnx3.admin.service.TemplateService;
import com.xnx3.admin.util.AliyunLog;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.admin.vo.NewsVO;
import com.xnx3.admin.vo.SiteColumnTreeVO;
import com.xnx3.admin.vo.bean.NewsInit;

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
	@RequestMapping("saveNews")
	public String saveNews(News s,
			@RequestParam(value = "text", required = false , defaultValue="") String text,
			HttpServletRequest request,Model model){
		String title = "";
		if(s.getTitle() != null && s.getTitle().length()>0){
			title = filter(s.getTitle());
		}else{
			return error(model, "请输入您页面的名字");
		}
		if(title.length() == 0){
			return error(model, "请输入您页面的名字");
		}
		
		SiteColumn siteColumn = sqlService.findById(SiteColumn.class, s.getCid());
		if(siteColumn == null){
			return error(model, "信息所属栏目不存在");
		}
		if(siteColumn.getUserid() - getUserId() != 0){
			return error(model, "信息所属栏目不属于您，无法操作");
		}
		Site site = getSite();
		
		News news;
		NewsData newsData;
		if(s.getId() != null && s.getId() > 0){
			//编辑修改
			news = sqlService.findById(News.class, s.getId());
			if(news == null){
				return error(model, "要操作的页面不存在");
			}
			if(news.getUserid() != getUserId()){
				return error(model, "页面不属于您，无法操作！");
			}
			newsData = sqlService.findById(NewsData.class, s.getId());
		}else{
			//新增
			news = new News();
			news.setCid(siteColumn.getId());	//默认没有栏目绑定
			news.setCommentnum(0);
			news.setOpposenum(0);
			news.setReadnum(0);
			news.setStatus(News.STATUS_NORMAL);
			news.setType(s.getType()==SiteColumn.TYPE_IMAGENEWS? SiteColumn.TYPE_IMAGENEWS:SiteColumn.TYPE_NEWS);
			news.setUserid(getUserId());
			news.setAddtime(DateUtil.timeForUnix10());
			news.setSiteid(siteColumn.getSiteid());
			
			newsData = new NewsData();
		}
		
		title = StringUtil.filterHtmlTag(title);
		if(title != null && title.length() > 30){
			title = title.substring(0, 30);
		}
		
		text = newsService.setText(text, site);
		
		//内容过滤HTML标签
		String textFilterHtml = StringUtil.filterHtmlTag(text);
		
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
			news.setIntro(intro);
		}else{
			//修改News，若表单中穿过来的简介是有值得，那么将表单中传过来的简介的数值进行过滤HTML标签后保存入数据库
			news.setIntro(StringUtil.filterHtmlTag(s.getIntro()));
		}
		
		news.setTitle(title);
		
		//上传标题图片,只有是图文模式的时候才会有标题图片的上传
		String oldTitlePic = "";	//旧的栏目导航图名字
		UploadFileVO uploadFileVO = AttachmentFile.uploadImage("site/"+site.getId()+"/news/", request, "titlePicFile", G.NEWS_TITLEPIC_MAXWIDTH);
		if(uploadFileVO.getResult() == UploadFileVO.SUCCESS){
			oldTitlePic = (news.getTitlepic()==null||news.getTitlepic().length()==0)? "":news.getTitlepic();
			news.setTitlepic(uploadFileVO.getFileName());
		}
		
		sqlService.save(news);
		if(news.getId() > 0){
			boolean have = TextFilter.filter(request, "网市场文章信息发现涉嫌违规："+news.getTitle(), G.masterSiteUrl+"admin/news/view.do?id="+news.getId(), news.getTitle()+textFilterHtml);
			if(have){
				//写入news的合法性字段
				news.setLegitimate(News.LEGITIMATE_NO);
				sqlService.save(news);
			}
			
			newsData.setId(news.getId());
			newsData.setText(text);
			sqlService.save(newsData);
			
			//如果有旧图，删除掉旧的图片
			if(news.getType() == News.TYPE_IMAGENEWS && oldTitlePic.length() > 0){
				AttachmentFile.deleteObject("site/"+site.getId()+"/news/"+oldTitlePic);
			}
			
			if(s.getId() == null || s.getId() == 0){
				AliyunLog.addActionLog(news.getId(), "新增文章成功，文章："+news.getTitle());
				//刷新sitemap
				siteService.refreshSiteMap(site);
			}else{
				AliyunLog.addActionLog(news.getId(), "修改文章成功，文章："+news.getTitle());
			}
			
			/**
			 * 生成静态页面
			 */
			newsService.generateViewHtml(site, news,siteColumn, text, request);	//生成当前内容页
			
			//如果是通用模式，还要生成列表页。当然，CMS模式是不会生成列表页跟首页的
			if(!Func.isCMS(site)){
				List<News> newsList = sqlService.findBySqlQuery("SELECT * FROM news WHERE cid = "+siteColumn.getId() + " AND status = "+News.STATUS_NORMAL+" ORDER BY id DESC", News.class);
				//如果是通用模版，全自动式模版
				newsService.generateListHtml(site, siteColumn,newsList, request);		//栏目页面
				
				IndexNews.refreshIndexData(site, siteColumn, newsList);			//PC首页局部刷新
				
				String pw = siteService.isPcClient(site)? "pc":"wap";
				if(site.getClient() - Site.CLIENT_WAP == 0){
					//手机页面，那么修改成功后，将直接跳转到内容管理列表中
					return success(model, "保存成功", "news/listForTemplate.do?cid="+news.getCid());
				}else{
					//pc
					return success(model, "保存成功！","news/list.do?cid="+news.getCid()+"&client="+pw+"&editMode=edit");
				}
				
			}else{
				return success(model, "保存成功！","news/listForTemplate.do?cid="+news.getCid());
			}
			
		}else{
			return error(model, "保存失败！");
		}
		
	}
	
	/**
	 * 根据news.id删除信息
	 */
	@RequestMapping("deleteNews")
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
			AliyunLog.addActionLog(news.getId(), "删除文章成功，文章："+news.getTitle());
			
			//删除OSS的html、头图文件
			AttachmentFile.deleteObject("site/"+news.getSiteid()+"/"+news.getId()+".html");
			if(news.getTitlepic() != null && news.getTitlepic().length() > 0 && news.getTitlepic().indexOf("http:") == -1){
				AttachmentFile.deleteObject("site/"+news.getSiteid()+"/news/"+news.getTitlepic());
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
	@RequestMapping("/listForTemplate")
	public String list(HttpServletRequest request,Model model){
		Site site = getSite();
		
		//如果传入了cid，获取到当前的siteColumn信息
	    String cidPar = request.getParameter("cid");
	    int cid = 0;
	    if(cidPar != null){
	    	cid = Lang.stringToInt(cidPar, 0);
	    	if(cid > 0){
	    		SiteColumn siteColumn = sqlService.findById(SiteColumn.class, cid);
	    		if(siteColumn == null){
	    			return error(model, "要查看的栏目不存在");
	    		}
	    		if(siteColumn.getSiteid() - getSiteId() != 0){
	    			return error(model, "该栏目不属于您，无法查看");
	    		}
	    		model.addAttribute("siteColumn", siteColumn);
	    		AliyunLog.addActionLog(siteColumn.getId(), "查看指定栏目下的文章列表，所属栏目："+siteColumn.getName());
	    	}
	    	
	    }
	    
	    if(cid == 0){
	    	AliyunLog.addActionLog(getSiteId(), "查看网站内所有文章的列表");
	    }
		
	    Sql sql = new Sql(request);
	    sql.setSearchTable("news");
	    sql.setSearchColumn(new String[]{"type=","title","cid="});
	    sql.appendWhere("siteid = "+getSiteId());
	    int count = sqlService.count("news", sql.getWhere());
	    Page page = new Page(count, Global.getInt("LIST_EVERYPAGE_NUMBER"), request);
	    //创建查询语句，只有SELECT、FROM，原生sql查询。其他的where、limit等会自动拼接
	    sql.setSelectFromAndPage("SELECT * FROM news", page);
	    
	    //当用户没有选择排序方式时，系统默认排序。
	    sql.setDefaultOrderBy("id DESC");
	    //因联合查询，结果集是没有实体类与其对应，故而用List<Map>接收
	    List<News> list = sqlService.findBySql(sql, News.class);
	     
	    //从缓存中调取当前网站栏目
	    //从缓存中获取栏目树列表
    	List<SiteColumnTreeVO> siteColumnTreeVOList = siteColumnService.getSiteColumnTreeVOByCache();
	    if(siteColumnTreeVOList.size() > 0){
	    	StringBuffer columnTreeSB = new StringBuffer();
		    for (int i = 0; i < siteColumnTreeVOList.size(); i++) {
		    	SiteColumnTreeVO sct = siteColumnTreeVOList.get(i);
		    	
		    	//如果有下级栏目，也将下级栏目列出来
		    	if(sct.getList().size() > 0){
		    		columnTreeSB.append("<li class=\"layui-nav-item\" id=\"super"+sct.getSiteColumn().getId()+"\"><a href=\"javascript:;\" class=\"dltitle\">"+sct.getSiteColumn().getName()+"</a><dl class=\"layui-nav-child\" style=\"background-color: #EAEDF1;\">");
		    		for (int j = 0; j < sct.getList().size(); j++) {
		    			SiteColumn s = sct.getList().get(j).getSiteColumn();
		    			columnTreeSB.append("<dd>"+getLeftNavColumnA(cid, s, sct.getSiteColumn().getId())+"</dd>");
					}
		    		columnTreeSB.append("</dl></li>");
		    		
		    	}else{
		    		columnTreeSB.append("<li class=\"layui-nav-item\">"+getLeftNavColumnA(cid, sct.getSiteColumn(), 0)+"</li>");
		    	}
			}
		    model.addAttribute("columnTreeNav", columnTreeSB.toString());
	    }else{
	    	return error(model, "您现在还没有创建栏目，既然没有栏目，那要管理的内容是属于哪的呢？内容必须有所属的栏目，请先去建立栏目吧");
	    }
	    
	    //将数据记录传到页面以供显示
	    model.addAttribute("list", list);
	    //将分页信息传到页面以供显示
	    model.addAttribute("page", page);
	    model.addAttribute("siteDomain", Func.getDomain(site));	//访问域名
	    model.addAttribute("site", site);
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
		
		if(sc.getType() - SiteColumn.TYPE_NEWS == 0 || sc.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
			href = "listForTemplate.do?cid="+sc.getId();
		}else if (sc.getType() - SiteColumn.TYPE_PAGE == 0) {
//			href = "newsForTemplate.do?alonePageCid="+sc.getId();	这样直接编辑内容
			href = "listForTemplate.do?cid="+sc.getId();	//这样先进入列表页面
		}else{
			href = "javascript:layer.msg('此栏目类型为超链接，无内容。修改本栏目方式：<br/>1.&nbsp;栏目管理，找到相应的栏目，进行修改<br/>2.&nbsp;首页模式，找到相应的栏目，进行修改');";
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
	@RequestMapping("news")
	public String news(HttpServletRequest request,
			@RequestParam(value = "id", required = false , defaultValue="0") int id,
			@RequestParam(value = "cid", required = false , defaultValue="0") int cid,
			Model model){
		NewsInit ni = newsService.news(request, id, cid, model);
		if(ni.getResult() == NewsInit.SUCCESS){
			//则加载输入模型。无论CMS模式，还是手机、电脑模式，都要加载，因为都要输入
			String inputModelText = inputModelService.getInputModelTextByIdForNews(ni);
			model.addAttribute("inputModelText", inputModelText);
			
			AliyunLog.addActionLog(getSiteId(), "打开创建、修改文章页面");
			return "news/newsForTemplate";
		}else{
			return error(model, ni.getInfo());
		}
	}
	
	/**
	 * 根据news.id删除信息,Ajax方式，返回json
	 * @param id 要删除的news.id
	 */
	@RequestMapping("deleteNewsForAjax")
	@ResponseBody
	public NewsVO deleteNewsForAjax(HttpServletRequest request,Model model,
			@RequestParam(value = "id", required = false , defaultValue="0") int id){
		NewsVO vo = newsService.deleteNews(id, true);
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			News news = vo.getNews();
			
			//日志
			AliyunLog.addActionLog(getSiteId(), "删除文章："+news.getTitle());
			
			//删除OSS的html、头图文件
			AttachmentFile.deleteObject("site/"+news.getSiteid()+"/"+news.getId()+".html");
			if(news.getTitlepic() != null && news.getTitlepic().length() > 0 && news.getTitlepic().indexOf("http:") == -1){
				AttachmentFile.deleteObject("site/"+news.getSiteid()+"/news/"+news.getTitlepic());
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
	@RequestMapping("redirectByNews")
	public String redirectByNews(Model model,
			@RequestParam(value = "newsId", required = false , defaultValue="0") int newsId,
			@RequestParam(value = "cid", required = false , defaultValue="0") int cid,
			@RequestParam(value = "type", required = false , defaultValue="0") short type){
		String generateUrlRule = "id";	//url生成模式，分id、code
		Site site = getSite();
		String url = "http://"+Func.getDomain(site)+"/";
		
		if(G.masterSiteUrl != null && G.masterSiteUrl.equals("http://wang.market/")){
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
		
		//判断是否是独立页面，若是独立页面，需要用 c +cid .html， 或者使用code.html
		if(type - SiteColumn.TYPE_PAGE == 0){
			if(generateUrlRule.equals("code")){
				//从栏目缓存中，取出栏目信息
				Map<Integer, SiteColumn> columnMap = siteColumnService.getSiteColumnMapByCache();
				SiteColumn sc = columnMap.get(cid);
				if(sc == null){
					return error(model, "文章所属栏目未发现");
				}
				
				url = url + sc.getCodeName() + ".html"; 
			}else{
				url = url + "c" + cid + ".html";
			}
		}else{
			url = url + newsId + ".html";
		}
		
		AliyunLog.addActionLog(newsId, "网站管理后台查看文章页面", url);
		return redirect(url);
	}
	

	/**
	 * 通过栏目id来修改文章，当然，要修改的这条文章一定是独立栏目，独立页面的，栏目里面只有一个页面，才能直接定向到要修改的页面上
	 * <br/>v3.0增加
	 * @param cid {@link SiteColumn}.id 要修改的栏目页面的栏目id
	 */
	@RequestMapping("updateNewsByCid")
	public String updateNewsByCid(HttpServletRequest request,
			@RequestParam(value = "cid", required = false , defaultValue="0") int cid,
			Model model){
		Site site = getSite();
		if(cid == 0){
			AliyunLog.addActionLog(getSiteId(), "warn", "修改文章页面时，没有传入id");
			return error(model, "要修改哪个栏目呢？");
		}
		//数据库取这个栏目的信息
		SiteColumn column = sqlService.findById(SiteColumn.class, cid);
		if(column == null){
			AliyunLog.addActionLog(getSiteId(), "warn", "要修改的栏目不存在");
			return error(model, "要修改的栏目不存在");
		}
		if(column.getSiteid() - site.getId() != 0 ){
			AliyunLog.addActionLog(getSiteId(), "warn", "此栏目不属于您，无法修改！");
			return error(model, "此栏目不属于您，无法修改！");
		}
		//取这个news的id
		News news = sqlService.findAloneBySqlQuery("SELECT * FROM news WHERE cid = "+cid, News.class);
		if(news == null){
			AliyunLog.addActionLog(getSiteId(), "warn", "要修改的文章不存在！规则里，在建立栏目类型为独立页面的栏目时，就会自动创建一篇文章，所以，此处既然栏目已经存在了，文章也应该是存在的！很可能在创建独立页面的时候，自动创建文章出错了，或者在哪删除了文章");
			return error(model, "要修改的文章不存在！");
		}
		return redirect("news/news.do?id="+news.getId());
	}
	
	/**
	 * 将某篇文章，转移到其他栏目中去
	 * @param newsid 要转移的文章的id， {@link News}.id
	 * @param columnid 当前要转移的文章所在的栏目id，文章没转移前在哪个栏目
	 */
	@RequestMapping("/newsChangeColumnForSelectColumn")
	public String newsChangeColumnForSelectColumn(HttpServletRequest request,Model model,
			@RequestParam(value = "newsid", required = true) int newsid,
			@RequestParam(value = "columnid", required = true) int columnid){
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
		
	    AliyunLog.addActionLog(newsid, "打开文章可转移的栏目选择页面");
	    model.addAttribute("newsid", newsid);
		return "news/newsChangeColumnForSelectColumn";
	}
	
	/**
	 * 只服务于  {@link #newsChangeColumnForSelectColumn(HttpServletRequest, Model, int)} 对其栏目进行判断，筛选
	 * @param column 该网站所有栏目，传入后根据栏目进行判断这个栏目是否可以选择进行移动。当然，只能是列表栏目才能进行移动
	 * @param currentColumnId 当前文章所在的栏目id。再移动栏目时，文章当前所在的栏目就是不能选择的
	 * @param grade 栏目级别，1、2、代表几级栏目，如，1便是顶级栏目，2便是2级栏目
	 * @param haveSubColumn 是否有下级栏目 true：有下级栏目
	 * @return
	 */
	private String newsChangeColumnForSelectColumn_Format(SiteColumn column, int currentColumnId,int grade, boolean haveSubColumn){
		boolean edit = false;
		if(haveSubColumn){
			edit = false;
		}else{
			if(column.getType() - SiteColumn.TYPE_IMAGENEWS == 0 || column.getType() - SiteColumn.TYPE_NEWS == 0){
				edit = true;
			}else{
				edit = false;
			}
		}
		
		String columnName = column.getName();
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
	@RequestMapping("newsChangeColumnForSelectColumnSubmit")
	@ResponseBody
	public BaseVO newsChangeColumnForSelectColumnSubmit(HttpServletRequest request,Model model,
			@RequestParam(value = "newsid", required = true) int newsid,
			@RequestParam(value = "targetColumnId", required = true) int targetColumnId){
		//判断要转移的文章是否存在以及是否属于本人
		News news = sqlService.findById(News.class, newsid);
		if(news == null){
			return error("要转移的文章不存在");
		}
		if(news.getUserid() - getUserId() != 0){
			return error("该文章不属于您，无法操作");
		}
		
		//判断要转移至的栏目，是否存在，以及是否属于本人
		SiteColumn column = sqlService.findById(SiteColumn.class, targetColumnId);
		if(column == null){
			return error("目标栏目不存在");
		}
		if(column.getUserid() - getUserId() != 0){
			return error("目标栏目不属于您，无法操作");
		}
		
		//检测完毕，进行转移栏目
		news.setCid(targetColumnId);
		sqlService.save(news);
		
		AliyunLog.addActionLog(news.getId(), "将文章转移栏目", "将文章"+news.getTitle()+"转移到栏目["+column.getName()+"]中");
		return success();
	}
}