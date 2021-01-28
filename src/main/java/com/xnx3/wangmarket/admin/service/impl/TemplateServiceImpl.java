package com.xnx3.wangmarket.admin.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.SafetyUtil;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.cache.Template;
import com.xnx3.wangmarket.admin.cache.TemplateCMS;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.entity.SiteVar;
import com.xnx3.wangmarket.admin.entity.TemplatePage;
import com.xnx3.wangmarket.admin.entity.TemplatePageData;
import com.xnx3.wangmarket.admin.entity.TemplateVarData;
import com.xnx3.wangmarket.admin.pluginManage.interfaces.manage.GenerateSitePluginManage;
import com.xnx3.wangmarket.admin.service.InputModelService;
import com.xnx3.wangmarket.admin.service.SiteColumnService;
import com.xnx3.wangmarket.admin.service.SiteVarService;
import com.xnx3.wangmarket.admin.service.TemplateService;
import com.xnx3.wangmarket.admin.util.SessionUtil;
import com.xnx3.wangmarket.admin.util.TemplateUtil;
import com.xnx3.wangmarket.admin.vo.GenerateSiteVO;
import com.xnx3.wangmarket.admin.vo.SiteColumnTreeVO;
import com.xnx3.wangmarket.admin.vo.TemplatePageListVO;
import com.xnx3.wangmarket.admin.vo.TemplatePageVO;
import com.xnx3.wangmarket.admin.vo.TemplateVO;
import com.xnx3.wangmarket.admin.vo.TemplateVarAndDataMapVO;
import com.xnx3.wangmarket.admin.vo.TemplateVarListVO;
import com.xnx3.wangmarket.admin.vo.TemplateVarVO;

@Service("TemplateService")
public class TemplateServiceImpl implements TemplateService {
	public static String sessionTemplatePageListVO = "templatePageListVO";	//Session中存储模版页面列表，session的名字
	//新建模版页面的默认内容
	public static String html = "<!DOCTYPE html>\n"
			+ "<html>\n"
			+ "<head>\n"
			+ "<meta charset=\"utf-8\">\n"
			+ "<title>模版页面</title>\n"
			+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n"
			+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n"
			+ "<!--XNX3HTMLEDIT--></head>\n"
			+ "<body>\n\n\n"
			+ "模版的内容\n\n\n</body>\n"
			+ "</html>";
	@Resource
	private SqlDAO sqlDAO;
	@Resource
	private InputModelService inputModelService;
	@Resource
	private SiteColumnService siteColumnService;
	@Resource
	private SiteVarService siteVarService;
	

	public TemplatePageListVO getTemplatePageListByCache(HttpServletRequest request) {
		Site site = SessionUtil.getSite();
		TemplatePageListVO vo = (TemplatePageListVO) request.getSession().getAttribute(sessionTemplatePageListVO);
		if(vo == null){
			//登陆后第一次取，缓存中还尚未存储，那么读数据表，取出后存入缓存
			
			//根据登录网站当前所使用的模板名字，来进行筛选要取出的模板页面
		    String templateNameWhere = "";
		    if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
		    	templateNameWhere = " AND template_page.template_name = '"+ site.getTemplateName() +"'";
		    }
			
		    //从数据库中取
			List<TemplatePage> templateList = sqlDAO.findBySqlQuery("SELECT * FROM template_page WHERE siteid = "+site.getId() + templateNameWhere, TemplatePage.class);
			
			List<TemplatePageVO> templatePageVOList = new ArrayList<TemplatePageVO>();
			for (int i = 0; i < templateList.size(); i++) {
				TemplatePageVO templatePageVO = new TemplatePageVO(); 
				templatePageVO.setTemplatePage(templateList.get(i));
				templatePageVOList.add(templatePageVO);
			}
			
			//加入缓存中存储。只加入了templatePage，data未加入，使用时用哪个再加入哪个
			vo = new TemplatePageListVO();
			vo.setList(templatePageVOList);
			request.getSession().setAttribute(sessionTemplatePageListVO, vo);
		}
		
		return vo;
	}
	
	public TemplatePageListVO getTemplatePageListByDatabase(Site site) {
		TemplatePageListVO vo = new TemplatePageListVO();
			
		//根据登录网站当前所使用的模板名字，来进行筛选要取出的模板页面
	    String templateNameWhere = "";
	    if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
	    	templateNameWhere = " AND template_page.template_name = '"+ site.getTemplateName() +"'";
	    }
		
	    //从数据库中取
		List<TemplatePage> templateList = sqlDAO.findBySqlQuery("SELECT * FROM template_page WHERE siteid = "+site.getId() + templateNameWhere, TemplatePage.class);
		
		List<TemplatePageVO> templatePageVOList = new ArrayList<TemplatePageVO>();
		for (int i = 0; i < templateList.size(); i++) {
			TemplatePageVO templatePageVO = new TemplatePageVO(); 
			templatePageVO.setTemplatePage(templateList.get(i));
			templatePageVOList.add(templatePageVO);
		}
		
		vo.setList(templatePageVOList);
		return vo;
	}

	public BaseVO updateTemplatePageForCache(TemplatePage templatePage,
			TemplatePageData templatePageData, HttpServletRequest request) {
		TemplatePageListVO vo = (TemplatePageListVO) request.getSession().getAttribute(sessionTemplatePageListVO);
		//判断一下，当然，这个应该不会为空，避免空指针
		if(vo == null){
			vo = getTemplatePageListByCache(request);
		}
		
		List<TemplatePageVO> templatePageVOList = vo.getList();
		boolean find = false;
		for (int i = 0; i < templatePageVOList.size(); i++) {
			TemplatePageVO templatePageVO = templatePageVOList.get(i);
			if(templatePageVO.getTemplatePage().getId() - templatePage.getId() == 0){
				templatePageVO.setTemplatePage(templatePage);
				//如果用户单纯只是更改templatePage的属性，是不会修改data的内容的。若不修改data内容，是不传递data的。这里判断一下，免得吧之前的覆盖为null
				if(templatePageData != null){
					templatePageVO.setTemplatePageData(templatePageData);
				}
				find = true;
				break;
			}
		}
		//如果再session缓存中没有找到此项要更新的模版页，那么可能这个模版页是新增的
		if(find == false){
			TemplatePageVO tpv = new TemplatePageVO();
			tpv.setTemplatePage(templatePage);
			tpv.setTemplatePageData(templatePageData);
			templatePageVOList.add(tpv);
		}
		
		request.getSession().setAttribute(sessionTemplatePageListVO, vo);
		return new BaseVO();
	}

	public String getTemplatePageTextByCache(int templatePageId, HttpServletRequest request) {
		TemplatePageListVO vo = (TemplatePageListVO) request.getSession().getAttribute(sessionTemplatePageListVO);
		//判断一下，当然，这个应该不会为空，避免空指针
		if(vo == null){
			vo = getTemplatePageListByCache(request);
		}
		
		List<TemplatePageVO> templatePageVOList = vo.getList();
		for (int i = 0; i < templatePageVOList.size(); i++) {
			TemplatePageVO templatePageVO = templatePageVOList.get(i);
			if(templatePageVO.getTemplatePage().getId() - templatePageId == 0){
				if(templatePageVO.getTemplatePageData() != null){
					return templatePageVO.getTemplatePageData().getText();
				}else{
					TemplatePageData tpd = sqlDAO.findById(TemplatePageData.class, templatePageId);
					if(tpd == null){
						tpd = new TemplatePageData();
						tpd.setId(templatePageId);
						tpd.setText(Template.newHtml);
					}
					templatePageVO.setTemplatePageData(tpd);
					request.getSession().setAttribute(sessionTemplatePageListVO, vo);	//保存缓存
					
					return tpd.getText();
				}
			}
		}
		
		return null;
	}

	public TemplatePageVO saveTemplatePageText(String fileName, String html, HttpServletRequest request) {
		TemplatePageVO vo = new TemplatePageVO();
		
		fileName = SafetyUtil.filter(fileName);
		if(fileName == null || fileName.length() == 0){
			vo.setBaseVO(BaseVO.FAILURE, "出错，你要修改的是哪个模版呢？");
			return vo;
		}
		
		Site site = SessionUtil.getSite();
		
		
		TemplatePage templatePage = sqlDAO.findAloneBySqlQuery("SELECT * FROM template_page WHERE siteid = "+site.getId()+" AND name = '"+fileName+"'", TemplatePage.class);
		if(templatePage == null){
			vo.setBaseVO(BaseVO.FAILURE, "要保存的模版页不存在！请手动复制保存好您当前所编辑的模版页内容！");
			return vo;
		}
		TemplatePageData templatePageData = sqlDAO.findAloneBySqlQuery("SELECT * FROM template_page_data WHERE id = "+templatePage.getId(), TemplatePageData.class);
		if(templatePageData == null){
			//没有发现内容，则是添加
			templatePageData = new TemplatePageData();
			templatePageData.setId(templatePage.getId());
		}
		
		
		//将 {templatePath} 标签进行动态替换，将路径还原回标签形态。
		//v4.9将其迁移到外面，虽说代码模式不可能有这个，但是万一有bug呢，放到外面保险点
		TemplateCMS templateCMS = new TemplateCMS(site, TemplateUtil.getTemplateByName(site.getTemplateName()));
		html = html.replaceAll(templateCMS.getTemplatePath(), "{templatePath}");
		
		
		//判断是代码模式，还是智能模式
		if(templatePage.getEditMode() != null && templatePage.getEditMode() - TemplatePage.EDIT_MODE_CODE == 0){
			
			//代码模式编辑，那么直接保存即可，不需要卸载模版变量等
			
		}else{
			//可视化编辑，需要提取模版变量、过滤 htmledit 等
			
			//判断其是否是进行了自由编辑，替换掉其中htmledit加入的js跟css文件，也就是替换掉<!--XNX3HTMLEDIT-->跟</head>中间的所有字符，网页源码本身<!--XNX3HTMLEDIT-->跟</head>是挨着的
			int htmledit_start = html.indexOf("<!--XNX3HTMLEDIT-->");
			if(htmledit_start > 0){
				int htmledit_head = html.indexOf("</head>");
				if(htmledit_head == -1){
					//出错了，忽略
				}else{
					//成功，进行过滤中间的htmledit加入的js跟css
					html = html.substring(0, htmledit_start)+html.substring(htmledit_head, html.length());
				}
				
				//contenteditable=true去掉
				if(html.indexOf("<body contenteditable=\"true\">") > -1){
					html = html.replace("<body contenteditable=\"true\">", "<body>");
				}else{
					html = html.replaceAll(" contenteditable=\"true\"", "");
				}
			}
			
			
			//如果这个页面中使用了模版变量，保存时，将模版变量去掉，变回模版调用形式{includeid=},卸载变量模版
			if(html.indexOf("<!--templateVarStart-->") > -1){
				Template temp = new Template(site);
				Pattern p = Pattern.compile("<!--templateVarStart-->([\\s|\\S]*?)<!--templateVarEnd-->");
		        Matcher m = p.matcher(html);
		        while (m.find()) {
		        	String templateVarText = m.group(1);	//<!--templateVarName=-->+模版变量的内容
		        	String templateVarName = temp.getConfigValue(templateVarText, "templateVarName");	//模版变量的名字
		        	templateVarName = Sql.filter(templateVarName);
		        	
		        	//替换出当前模版变量的内容，即去掉templateVarText注释
		        	templateVarText = templateVarText.replace("<!--templateVarName="+templateVarName+"-->", "");
		        	
		        	//判断模版变量是否有过变动，当前用户是否修改过模版变量，如果修改过，将修改过的模版变量保存
		        	//从内存中取模版变量内容
		        	String content = null;
		        	BaseVO tvVO = getTemplateVarByCache(templateVarName);
		        	if(tvVO.getResult() - BaseVO.SUCCESS == 0){
		        		content = tvVO.getInfo();
		        	}else{
		        		vo.setBaseVO(BaseVO.FAILURE, "其中使用的模版变量“"+templateVarName+"”不存在！保存失败，请检查修改后再尝试保存");
		    			return vo;
		        	}
//		        	String content = G.templateVarMap.get(site.getTemplateName()).get(templateVarName);
		        	
		        	//讲用户保存的跟内存中的模版变量内容比较，是否一样，若不一样，要将当前的保存
		        	if(!(content.replaceAll("\r|\n|\t", "").equals(templateVarText.replaceAll("\r|\n|\t", "")))){
		        		//不一样，进行保存
		        		
		        	    //模版名字检索，是否是使用的导入的模版，若是使用的导入的模版，则只列出导入的模版变量
		        	    String templateNameWhere = "";
		        	    if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
		        	    	templateNameWhere = " AND template_var.template_name = '"+ site.getTemplateName() +"'";
		        	    }
		        		com.xnx3.wangmarket.admin.entity.TemplateVar tv = sqlDAO.findAloneBySqlQuery("SELECT * FROM template_var WHERE siteid = " + site.getId() + templateNameWhere + " AND var_name = '"+templateVarName+"'", com.xnx3.wangmarket.admin.entity.TemplateVar.class);
		        		if(tv != null){
		        			tv.setUpdatetime(DateUtil.timeForUnix10());
			        		sqlDAO.save(tv);
			        		
			        		TemplateVarData templateVarData = sqlDAO.findById(TemplateVarData.class, tv.getId());
			        		templateVarData.setText(templateVarText);
			        		sqlDAO.save(templateVarData);
			        		
			        		//保存完后，要将其更新到全局缓存中
			        		updateTemplateVarForCache(tv, templateVarData);
		        		}
		        	}
		        	
//		            将用户保存的当前的模版页面，模版变量摘除出来，还原为模版调用的模式
		            html = html.replaceAll("<!--templateVarStart--><!--templateVarName="+templateVarName+"-->([\\s|\\S]*?)<!--templateVarEnd-->", "{include="+templateVarName+"}");
		        }
			}
			
		}
		
		templatePageData.setText(html);
		sqlDAO.save(templatePageData);
		
		//刷新Session缓存
		updateTemplatePageForCache(templatePage, templatePageData, request);
		
		vo.setTemplatePage(templatePage);
		vo.setTemplatePageData(templatePageData);
		return vo;
	}

	public String getTemplatePageTextByCache(String templatePageName,
			HttpServletRequest request) {
		templatePageName = SafetyUtil.filter(templatePageName);
		TemplatePageListVO vo = (TemplatePageListVO) request.getSession().getAttribute(sessionTemplatePageListVO);
		//判断一下，当然，这个应该不会为空，避免空指针
		if(vo == null){
			vo = getTemplatePageListByCache(request);
		}
		
		List<TemplatePageVO> templatePageVOList = vo.getList();
		for (int i = 0; i < templatePageVOList.size(); i++) {
			TemplatePageVO templatePageVO = templatePageVOList.get(i);
			if(templatePageVO.getTemplatePage().getName().equals(templatePageName)){
				if(templatePageVO.getTemplatePageData() != null){
					return templatePageVO.getTemplatePageData().getText();
				}else{
					TemplatePageData tpd = sqlDAO.findById(TemplatePageData.class, templatePageVO.getTemplatePage().getId());
					templatePageVO.setTemplatePageData(tpd);
					request.getSession().setAttribute(sessionTemplatePageListVO, vo);	//保存缓存
					
					return tpd.getText();
				}
			}
		}
		
		return null;
	}

	public TemplatePageVO getTemplatePageIndexByCache(HttpServletRequest request) {
		TemplatePageListVO templatePageListVO  = getTemplatePageListByCache(request);
		return getTemplatePageIndexByTemplatePageListVO(templatePageListVO);
	}
	
	/**
	 * 从 {@link TemplatePageListVO} 中，找出首页模版
	 * @param templatePageListVO {@link TemplatePageListVO}
	 * @return {@link TemplatePageVO} 如果没有，那么 vo.result == FAILURE
	 */
	private TemplatePageVO getTemplatePageIndexByTemplatePageListVO(TemplatePageListVO templatePageListVO) {
		for (int i = 0; i < templatePageListVO.getList().size(); i++) {
			TemplatePageVO templatePageVO = templatePageListVO.getList().get(i);
			if(templatePageVO.getTemplatePage().getType() - TemplatePage.TYPE_INDEX == 0){
				return templatePageVO;
			}
		}
		
		//没有发现首页模版，那么创建一个新的对象，返回
		TemplatePageVO templatePageVO = new TemplatePageVO();
		templatePageVO.setBaseVO(TemplatePageVO.FAILURE, "未发现首页模版");
		return templatePageVO;
	}
	
	/**
	 * 通过高级自定义模版，生成内容详情页面，News的页面，包含独立页面、新闻详情、图文详情
	 * @param news 要生成的详情页的 {@link News}
	 * @param siteColumn 要生成的详情页所属的栏目 {@link SiteColumn}
	 * @param newsDataBean news_data 的整理及数据初始化
	 */
	public void generateViewHtmlForTemplate(Site site, News news, SiteColumn siteColumn, NewsDataBean newsDataBean, HttpServletRequest request) {
		//获取到当前页面使用的模版
		String templateHtml = getTemplatePageTextByCache(siteColumn.getTemplatePageViewName(), request);
		if(templateHtml == null){
			//出错，没有获取到该栏目的模版页
			return;
		}
		TemplateCMS template = new TemplateCMS(site, TemplateUtil.getTemplateByName(site.getTemplateName()));
		template.setSiteVar(siteVarService.getVar(site.getId()));
		String pageHtml = template.assemblyTemplateVar(templateHtml, getTemplateVarAndDataByDatabase(site).getCompileMap());	//装载模版变量
		pageHtml = template.replaceSiteColumnTag(pageHtml, siteColumn);	//替换栏目相关标签
		pageHtml = template.replacePublicTag(pageHtml);		//替换通用标签
		pageHtml = template.replaceNewsTag(pageHtml, news, siteColumn, newsDataBean);	//替换news相关标签
		
		//替换 SEO 相关
		pageHtml = pageHtml.replaceAll(Template.regex("title"), news.getTitle()+"_"+site.getName());
		pageHtml = pageHtml.replaceAll(Template.regex("keywords"), news.getTitle()+","+site.getKeywords());
		pageHtml = pageHtml.replaceAll(Template.regex("description"), news.getIntro());
		
		pageHtml = pageHtml.replaceAll(Template.regex("text"), template.replaceNewsText(newsDataBean.getText()));	//替换新闻内容的详情
		
		String generateUrl = "";
		if(news.getType() - News.TYPE_PAGE == 0){
			generateUrl = "site/"+site.getId()+"/c"+news.getCid()+".html";
		}else{
			generateUrl = "site/"+site.getId()+"/"+news.getId()+".html";
		}
		AttachmentUtil.putStringFile(generateUrl, pageHtml);
	}

	public void updateTemplateVarForCache(com.xnx3.wangmarket.admin.entity.TemplateVar templateVar,TemplateVarData templateVarData) {
		if(SessionUtil.getTemplateVarMapForOriginal() == null || SessionUtil.getTemplateVarCompileDataMap() == null){
			loadDatabaseTemplateVarToCache();
		}
		SessionUtil.getTemplateVarCompileDataMap().put(templateVar.getVarName(), templateVarData.getText());
		
		TemplateVarVO templateVarVO = new TemplateVarVO();
		templateVarVO.setTemplateVar(templateVar);
		templateVarVO.setTemplateVarData(templateVarData);
		SessionUtil.getTemplateVarMapForOriginal().put(templateVar.getVarName(), templateVarVO);
	}

	public void loadDatabaseTemplateVarToCache() {
		if(SessionUtil.getTemplateVarMapForOriginal() == null){
			Site site = SessionUtil.getSite();
			
			TemplateVarAndDataMapVO vo = getTemplateVarAndDataByDatabase(site);
			if(vo.getResult() - TemplateVarAndDataMapVO.SUCCESS == 0){
				SessionUtil.setTemplateVarCompileDataMap(vo.getCompileMap());
				SessionUtil.setTemplateVarMapForOriginal(vo.getTemplateVarMapForOriginal());
			}
		}
	}
	
	/**
	 * 更换模版，改变模版。同时也会将栏目复制过去.
	 * <br/>若此模版当前站点未使用过，会将模版页面跟模版变量复制过来。若此模版该网站之前使用过了，只需吧site.templateName改动一下名字即可
	 * @param templateName 要变为的模版名字
	 * @param copySiteColumn 是否再改变为新模版的同时，将其模版所拥有的栏目也一并复制过来。只对之前没有使用过的模版会复制栏目，若以前使用过此模版了，那么不会再复制栏目
	 * 				<ul>
	 * 					<li>true：复制</li>
	 * 					<li>false：不复制</li>
	 * 				</ul>
	 * @return {@link BaseVO}
	 * @deprecated
	 */
	public BaseVO changeTemplate(Site mysite, String templateName, boolean copySiteColumn){
		BaseVO vo = new BaseVO();
		templateName = SafetyUtil.filter(templateName);
		if(templateName.length() == 0){
			vo.setBaseVO(BaseVO.FAILURE, "请选择更改为的模版");
			return vo;
		}
		
		
		/*
		 *  
		 *  此 template 模版不在此处。另外此接口以废弃，若要启用，需增加template数据表，并将以下注释开启
		 *  
		 *  
		 */
		
		//得到用户要更换的模版
//		com.xnx3.wangmarket.admin_.entity.Template template = sqlDAO.findAloneBySqlQuery("SELECT * FROM template WHERE name = '"+templateName+"'", com.xnx3.wangmarket.admin_.entity.Template.class);
//		if(template == null){
//			vo.setBaseVO(BaseVO.FAILURE, "模版不存在!");
//			return vo;
//		}
		
//		//判断当前用户是否已经使用过此模版了
//		int tp = sqlDAO.count("template_page", "WHERE siteid="+mysite.getId()+" AND template_name = '"+template.getName()+"'");
//		if(tp > 0){
//			//已经用过，那么不用再生成栏目、独立页面、模版页、模版变量
//		}else{
//			//之前没用过此模版
//			
//			//获得选择的模版的站点
//			Site site = (Site) sqlDAO.findById(Site.class, template.getSiteid());
//			if(site == null){
//				vo.setBaseVO(BaseVO.FAILURE, "出错，模版站未找到！");
//				return vo;
//			}
//			
//			//复制TemplatePage模版页
//			List<Map<String, Object>> templatePageList = sqlDAO.findMapBySqlQuery("SELECT template_page.name, template_page.type, template_page_data.text FROM template_page,template_page_data WHERE template_page.id = template_page_data.id AND template_page.siteid = "+site.getId());
//			for (int i = 0; i < templatePageList.size(); i++) {
//				Map<String, Object> map = templatePageList.get(i);
//				TemplatePage ntp = new TemplatePage();
//				ntp.setName((String) map.get("name"));
//				ntp.setSiteid(mysite.getId());
//				ntp.setTemplateName(template.getName());
//				ntp.setType(new Short(map.get("type").toString()));
//				ntp.setUserid(mysite.getUserid());
//				sqlDAO.save(ntp);
//				if(ntp.getId() == null || ntp.getId() == 0){
//					System.out.println("模版页复制出错，没保存成功进数据库:"+ntp.toString());
//				}else{
//					//保存模版页面的详情内容
//					TemplatePageData tpd = new TemplatePageData();
//					tpd.setId(ntp.getId());
//					tpd.setText((String) map.get("text"));
//					sqlDAO.save(tpd);
//				}
//			}
//			
//			//复制TemplateVar模版变量
//			//模版名字检索，是否是使用的导入的模版，若是使用的导入的模版，则只列出导入的模版变量
//		    String templateNameWhere = "";
//		    if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
//		    	templateNameWhere = " AND template_var.template_name = '"+ site.getTemplateName() +"'";
//		    }
//			List<Map<String, Object>> templateVarList = sqlDAO.findMapBySqlQuery("SELECT template_var.remark, template_var.var_name, template_var_data.text FROM template_var,template_var_data WHERE template_var.id = template_var_data.id AND template_var.siteid = "+site.getId() + templateNameWhere);
//			for (int i = 0; i < templateVarList.size(); i++) {
//				Map<String, Object> map = templateVarList.get(i);
//				com.xnx3.wangmarket.admin.entity.TemplateVar tv = new com.xnx3.wangmarket.admin.entity.TemplateVar();
//				tv.setAddtime(DateUtil.timeForUnix10());
//				tv.setRemark((String) map.get("remark"));
//				tv.setTemplateName(template.getName());
//				tv.setUpdatetime(tv.getAddtime());
//				tv.setUserid(mysite.getUserid());
//				tv.setVarName((String) map.get("var_name"));
//				sqlDAO.save(tv);
//				if(tv.getId() == null || tv.getId() == 0){
//					System.out.println("模版变量复制出错，没保存成功进数据库:"+tv.toString());
//				}else{
//					//保存模版页面的详情内容
//					TemplateVarData tvd = new TemplateVarData();
//					tvd.setId(tv.getId());
//					tvd.setText((String) map.get("text"));
//					sqlDAO.save(tvd);
//				}
//			}
//			
//			//是否也将此新模版栏目也一块复制过来
//			if(copySiteColumn){
//				//拿到模版网站下所有可用的栏目
//				List<SiteColumn> siteColumnList = sqlDAO.findBySqlQuery("SELECT * FROM site_column WHERE siteid = "+site.getId()+" AND used = "+SiteColumn.USED_ENABLE + " ORDER BY rank ASC", SiteColumn.class);
//				
//				for (int i = 0; i < siteColumnList.size(); i++) {
//					SiteColumn sc = siteColumnList.get(i);	//要复制的目标栏目
//					
//					//创建栏目，将栏目复制一份，再当前网站创建栏目
//					SiteColumn nsc = new SiteColumn();
//					nsc.setName(sc.getName());
//					nsc.setRank(sc.getRank());
//					nsc.setUsed(sc.getUsed());
//					nsc.setSiteid(mysite.getId());
//					nsc.setUserid(mysite.getUserid());
//					nsc.setType(sc.getType());
//					nsc.setTemplatePageListName(sc.getTemplatePageListName());
//					nsc.setTemplatePageViewName(sc.getTemplatePageViewName());
//					nsc.setCodeName(sc.getCodeName());
//					nsc.setParentCodeName(sc.getParentCodeName());
//					nsc.setListNum(sc.getListNum());
//					nsc.setEditMode(sc.getEditMode() == null ? 0:sc.getEditMode());
//					sqlDAO.save(nsc);
//					
//					if(sc.getType() - SiteColumn.TYPE_NEWS == 0 || sc.getType() - SiteColumn.TYPE_IMAGENEWS == 0){
//						//列表页面，包括：新闻列表、图文列表，这里只需要将栏目复制过去就行了
//					}else if (sc.getType() - SiteColumn.TYPE_PAGE == 0) {
//						//独立页面，需要将栏目复制过去，至于栏目下的单条news，到时候根据栏目的名字自动生成一个模拟的news、news_data，其他的让用户自己去改就行了
//						if(nsc.getId() == null || nsc.getId() == 0){
//							System.out.println("创建栏目失败！！"+nsc.toString());
//						}else{
//							//栏目下创建News
//							News news = new News();
//							news.setAddtime(DateUtil.timeForUnix10());
//							news.setCid(nsc.getId());
//							news.setIntro(nsc.getName());
//							news.setSiteid(mysite.getId());
//							news.setStatus(News.STATUS_NORMAL);
//							news.setTitle(nsc.getName());
//							news.setType(News.TYPE_PAGE);
//							news.setUserid(mysite.getUserid());
//							sqlDAO.save(news);
//							if(news.getId() != null && news.getId() > 0){
//								//创建 NewsData
//								NewsData newsData = new NewsData();
//								newsData.setId(news.getId());
//								newsData.setText("这是内容，请自行更改");
//								sqlDAO.save(newsData);
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		//将此用户的site的templateName改变为更换的模版名
//		sqlDAO.executeSql("UPDATE site SET template_name = '"+template.getName()+"' WHERE id = "+mysite.getId());
//		
		return vo;
	}

	public BaseVO addNewDefaultTemplatePageForIndex(Site site) {
		BaseVO vo = new BaseVO();

		TemplatePage templatePage = new TemplatePage();
		templatePage.setName("index");
		templatePage.setSiteid(site.getId());
		templatePage.setTemplateName(site.getTemplateName());		//若是网站没有使用做好的模版，自己做的，那么模版名字为空即可，可能是null，也可能是空字符
		templatePage.setType(TemplatePage.TYPE_INDEX);
		templatePage.setUserid(site.getUserid());
		sqlDAO.save(templatePage);
		if(templatePage.getId() != null && templatePage.getId() > 0){
			TemplatePageData tpd = new TemplatePageData();
			tpd.setId(templatePage.getId());
			tpd.setText(html);
			sqlDAO.save(tpd);
		}else{
			vo.setBaseVO(BaseVO.FAILURE, "首页模版创建失败");
		}
		
		return vo;
	}

	public TemplateVarVO getTemplateVarByCache(String templateVarName) {
		TemplateVarVO vo = new TemplateVarVO();
		if(SessionUtil.getTemplateVarMapForOriginal() == null){
			loadDatabaseTemplateVarToCache();
		}
		if(SessionUtil.getTemplateVarMapForOriginal().get(templateVarName) == null){
			vo.setBaseVO(TemplateVarVO.FAILURE, "模版变量不存在");
		}else{
			vo = SessionUtil.getTemplateVarMapForOriginal().get(templateVarName);
		}
		return vo;
	}

	public BaseVO exportTemplate(HttpServletRequest request) {
		BaseVO vo = new BaseVO();
		
		//我当前登录的站点信息
		Site site = SessionUtil.getSite();
		
		//获得TemplatePage模版页
		TemplatePageListVO tplVO = getTemplatePageAndDateListByCache(request);
		List<Map<String, Object>> templatePageList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < tplVO.getList().size(); i++) {
			TemplatePageVO tpv = tplVO.getList().get(i);
			TemplatePage tp = tpv.getTemplatePage();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", StringUtil.StringToUtf8(tp.getName()));
			map.put("remark", StringUtil.StringToUtf8(tp.getRemark()));
			map.put("type", tp.getType());
			map.put("text", StringUtil.StringToUtf8(tpv.getTemplatePageData().getText()));
			map.put("editMode", tp.getEditMode() == null? TemplatePage.EDIT_MODE_VISUAL:tp.getEditMode());
			templatePageList.add(map);
		}
		
		
		//获得TemplateVar模版变量
		TemplateVarListVO tvlVO = getTemplateVarAndDateListByCache();
		List<Map<String, Object>> templateVarList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < tvlVO.getList().size(); i++) {
			TemplateVarVO tvv = tvlVO.getList().get(i);
			com.xnx3.wangmarket.admin.entity.TemplateVar tv = tvv.getTemplateVar();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("remark", StringUtil.StringToUtf8(tv.getRemark()));
			map.put("var_name", StringUtil.StringToUtf8(tv.getVarName()));
			map.put("text", StringUtil.StringToUtf8(tvv.getTemplateVarData().getText()));
			templateVarList.add(map);
		}
		
		
		//获得栏目，原始的
		List<SiteColumn> siteColumnList_Original = siteColumnService.getSiteColumnListByCache();
		//经过编码、无用字段过滤的
		List<SiteColumn> siteColumnList = new ArrayList<SiteColumn>();
		//将栏目进行UTF8编码操作
		for (int i = 0; i < siteColumnList_Original.size(); i++) {
			SiteColumn sc_ori = siteColumnList_Original.get(i);
			
			SiteColumn sc = new SiteColumn();
			sc.setName(StringUtil.StringToUtf8(sc_ori.getName()));
			sc.setTemplatePageListName(StringUtil.StringToUtf8(sc_ori.getTemplatePageListName()));
			sc.setTemplatePageViewName(StringUtil.StringToUtf8(sc_ori.getTemplatePageViewName()));
			sc.setCodeName(StringUtil.StringToUtf8(sc_ori.getCodeName()));
			sc.setParentCodeName(StringUtil.StringToUtf8(sc_ori.getParentCodeName()));
			sc.setInputModelCodeName(StringUtil.StringToUtf8(sc_ori.getInputModelCodeName()));
			sc.setRank(sc_ori.getRank());
			sc.setUsed(sc_ori.getUsed());
			sc.setType(sc_ori.getType());
			sc.setListNum(sc_ori.getListNum());
			sc.setEditMode(sc_ori.getEditMode());
			sc.setInputModelCodeName(sc_ori.getInputModelCodeName());
			sc.setListRank(sc_ori.getListRank() == null? SiteColumn.LIST_RANK_ADDTIME_ASC:sc_ori.getListRank());
			//v4.6增加的四个自定义的，内容管理中的输入框
			sc.setEditUseExtendPhotos(sc_ori.getEditUseExtendPhotos() == null? SiteColumn.USED_UNABLE:sc_ori.getEditUseExtendPhotos());
			sc.setEditUseIntro(sc_ori.getEditUseIntro() == null? SiteColumn.USED_UNABLE:sc_ori.getEditUseIntro());
			sc.setEditUseText(sc_ori.getEditUseText() == null? SiteColumn.USED_UNABLE:sc_ori.getEditUseText());
			sc.setEditUseTitlepic(sc_ori.getEditUseTitlepic() == null? SiteColumn.USED_UNABLE:sc_ori.getEditUseTitlepic());
			//v4.7，增加是否生成内容页面
			sc.setUseGenerateView(sc_ori.getUseGenerateView() == null? SiteColumn.USED_ENABLE:sc_ori.getUseGenerateView());
			sc.setIcon(sc_ori.getIcon() == null? "":sc_ori.getIcon());
			//v5.1，seo相关，另外补上，显示中的几项配置
			sc.setKeywords(sc_ori.getKeywords() == null? "":sc_ori.getKeywords());
			sc.setDescription(sc_ori.getDescription() == null? "":sc_ori.getDescription());
			sc.setTemplateCodeColumnUsed(sc_ori.getTemplateCodeColumnUsed());
			sc.setAdminNewsUsed(sc_ori.getAdminNewsUsed());
			
			siteColumnList.add(sc);
		}
		
		//获得自定义输入模型，原始的，网站中使用的
		List<InputModel> inputModelList_Original = inputModelService.getInputModelListForSession();
		//自定义输入模型，经过UTF8编码替换过的，保存到模版的
		List<InputModel> inputModelList = new ArrayList<InputModel>();
		for (int i = 0; i < inputModelList_Original.size(); i++) {
			InputModel im_ori = inputModelList_Original.get(i);

			InputModel im = new InputModel();
			im.setCodeName(StringUtil.StringToUtf8(im_ori.getCodeName()));
			im.setRemark(StringUtil.StringToUtf8(im_ori.getRemark()));
			im.setText(StringUtil.StringToUtf8(im_ori.getText()));
			inputModelList.add(im);
		}
		
		//全局变量,v5.1增加
		JSONObject siteVarJson = siteVarService.getVar(site.getId());
		JSONObject utf8SiteVar = new JSONObject();
		Iterator iter = siteVarJson.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            JSONObject textObj = (JSONObject)entry.getValue();
            
            JSONObject uft8SubJson = new JSONObject();
            Iterator subIter = textObj.entrySet().iterator();
            while (subIter.hasNext()) {
            	Map.Entry subEntry = (Map.Entry) subIter.next();
            	uft8SubJson.put(subEntry.getKey(), StringUtil.StringToUtf8((String) subEntry.getValue()));
            }
            utf8SiteVar.put(entry.getKey(), uft8SubJson);
        }
		
		
		JSONObject jo = new JSONObject();
		jo.put("systemVersion", G.VERSION);	// 当前系统版本号
		jo.put("time", DateUtil.timeForUnix10());	//导出的时间，10为时间戳
		jo.put("templateName", StringUtil.StringToUtf8(site.getTemplateName()));	//当前模版的名字
		jo.put("sourceUrl", StringUtil.StringToUtf8(Func.getDomain(site))); 	//模版来源的网站，从那个网站导出来的，可以作为预览网站
		jo.put("useUtf8Encode", "true");	//设置使用UTF8编码将内容进行转码

		//v4.7增加，从数据库template中取 templateName 这个模版，看是否有
		com.xnx3.wangmarket.admin.entity.Template template = sqlDAO.findAloneByProperty(com.xnx3.wangmarket.admin.entity.Template.class, "name", site.getTemplateName());
		if(template != null){
			JSONObject tempJson = new JSONObject();
			tempJson.put("addtime", template.getAddtime());	//模版制作时间
			tempJson.put("companyname", template.getCompanyname() == null ? "":StringUtil.StringToUtf8(template.getCompanyname()));
			tempJson.put("iscommon", template.getIscommon());
			tempJson.put("previewUrl", template.getPreviewUrl() == null ? "":StringUtil.StringToUtf8(template.getPreviewUrl()));
			tempJson.put("remark", StringUtil.StringToUtf8(template.getRemark()));		//模版的备注
			tempJson.put("siteurl", template.getSiteurl() == null ? "":StringUtil.StringToUtf8(template.getSiteurl()));	//开发者网站url
			tempJson.put("terminalDisplay", template.getTerminalDisplay());
			tempJson.put("terminalIpad", template.getTerminalIpad());
			tempJson.put("terminalMobile", template.getTerminalMobile());
			tempJson.put("terminalPc", template.getTerminalPc());
			tempJson.put("type", template.getType());
			tempJson.put("username", template.getUsername() == null ? "":StringUtil.StringToUtf8(template.getUsername()));	//开发模版的用户的姓名
			tempJson.put("name", StringUtil.StringToUtf8(template.getName()));
			tempJson.put("previewPic", StringUtil.StringToUtf8(template.getPreviewPic()));
			tempJson.put("wscsoDownUrl", StringUtil.StringToUtf8(template.getWscsoDownUrl()));
			tempJson.put("zipDownUrl", StringUtil.StringToUtf8(template.getZipDownUrl()));
			
			//导出的模版所使用的js、css等资源文件的所在。如果没有指定，默认使用的是本地的资源，私有资源，v4.7.1增加
			//v4.8从新调整，废弃
			tempJson.put("resourceImport", (template.getResourceImport() != null? template.getResourceImport() : com.xnx3.wangmarket.admin.entity.Template.RESOURCE_IMPORT_PRIVATE));
			
			
			jo.put("template", tempJson);
		}
		
		jo.put("templatePageList", templatePageList);
		jo.put("templateVarList", templateVarList);
		jo.put("siteColumnList", siteColumnList);
		jo.put("inputModelList", inputModelList);
		jo.put("siteVar", utf8SiteVar);
		
		vo.setInfo(jo.toString());
		return vo;
	}

	public BaseVO importTemplate(String templateText, boolean copySiteColumn, HttpServletRequest request) {
		BaseVO vo = new BaseVO();
		
		//判断当前网站是否已经有模版了
		TemplatePageListVO tpl = getTemplatePageListByCache(request);
		if(tpl.getList().size() > 0){
			vo.setBaseVO(BaseVO.FAILURE, "该网站已有模版了，无需再次导入。可按 F5 键刷新");
			return vo;
		}
		
		TemplateVO tvo = new TemplateVO();
		//导入JSON，生成对象
		tvo.importText(templateText);
		
		//创建TemplatePage模版页
		for (int i = 0; i < tvo.getTemplatePageList().size(); i++) {
			com.xnx3.wangmarket.admin.vo.bean.template.TemplatePage templatePageBean = tvo.getTemplatePageList().get(i);
			TemplatePage ntp = templatePageBean.getTemplatePage();
			sqlDAO.save(ntp);
			if(ntp.getId() == null || ntp.getId() == 0){
				System.out.println("模版页复制出错，没保存成功进数据库:"+ntp.toString());
			}else{
				//保存模版页面的详情内容
				TemplatePageData tpd = new TemplatePageData();
				tpd.setId(ntp.getId());
				tpd.setText(templatePageBean.getText());
				sqlDAO.save(tpd);
			}
		}
		
		//创建TemplateVar模版变量
		for (int i = 0; i < tvo.getTemplateVarList().size(); i++) {
			com.xnx3.wangmarket.admin.vo.bean.template.TemplateVar templateVarBean = tvo.getTemplateVarList().get(i);
			com.xnx3.wangmarket.admin.entity.TemplateVar tv = templateVarBean.getTemplateVar();
			sqlDAO.save(tv);
			if(tv.getId() == null || tv.getId() == 0){
				System.out.println("模版变量复制出错，没保存成功进数据库:"+tv.toString());
			}else{
				//保存模版页面的详情内容
				TemplateVarData tvd = new TemplateVarData();
				tvd.setId(tv.getId());
				tvd.setText(templateVarBean.getText());
				sqlDAO.save(tvd);
			}
		}
		

		//导入自定义输入模型
		for (int i = 0; i < tvo.getInputModelList().size(); i++) {
			InputModel inputModel = tvo.getInputModelList().get(i);
			sqlDAO.save(inputModel);
		}
		
		//导入全局变量,v5.1增加
		if(tvo.getSiteVarJson().size() > 0){
			sqlDAO.executeSql("DELETE FROM site_var WHERE id = "+tvo.getCurrentSite().getId());	//避免当前网站已经设置过全局变量，先执行一次delete删除
			SiteVar siteVar = new SiteVar();
			siteVar.setId(tvo.getCurrentSite().getId());
			siteVar.setText(tvo.getSiteVarJson().toString());
			sqlDAO.save(siteVar);
		}

		//是否也将此新模版栏目也一块复制过来
		if(copySiteColumn){
			for (int i = 0; i < tvo.getSiteColumnList().size(); i++) {
				SiteColumn siteColumn = tvo.getSiteColumnList().get(i);
				sqlDAO.save(siteColumn);
				
				if(siteColumn.getType() - SiteColumn.TYPE_NEWS == 0 || siteColumn.getType() - SiteColumn.TYPE_IMAGENEWS == 0  || siteColumn.getType() - SiteColumn.TYPE_LIST == 0){
					//列表页面，包括：新闻列表、图文列表，这里只需要将栏目复制过去就行了
				}else if (siteColumn.getType() - SiteColumn.TYPE_PAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0) {
					//独立页面，需要将栏目复制过去，至于栏目下的单条news，到时候根据栏目的名字自动生成一个模拟的news、news_data，其他的让用户自己去改就行了
					if(siteColumn.getId() == null || siteColumn.getId() == 0){
						System.out.println("创建栏目失败！！"+siteColumn.toString());
					}else{
						//栏目下创建News
						News news = new News();
						news.setAddtime(DateUtil.timeForUnix10());
						news.setCid(siteColumn.getId());
						news.setIntro(siteColumn.getName());
						news.setSiteid(tvo.getCurrentSite().getId());
						news.setStatus(News.STATUS_NORMAL);
						news.setTitle(siteColumn.getName());
						news.setType(SiteColumn.TYPE_ALONEPAGE);
						news.setUserid(tvo.getCurrentSite().getUserid());
						sqlDAO.save(news);
						if(news.getId() != null && news.getId() > 0){
							//创建 NewsData
							NewsData newsData = new NewsData();
							newsData.setId(news.getId());
							newsData.setText("这是" + news.getTitle() + "的内容，请登录网站管理后台，找到内容管理，自行修改这些内容");
							sqlDAO.save(newsData);
						}
					}
				}
			}
		}
		
		//判断一下是否是导入模版，如果是导入模版，则要更新站点信息。如果只是导入模版插件，则不需要
		if(tvo.getPlugin() == null || tvo.getPlugin().length() < 2){
			//导入的是网站模版
			
			//最后，将当前网站得使用模版site.templateName变为当前模版得名字
			Site s = sqlDAO.findById(Site.class, tvo.getCurrentSite().getId());
			s.setTemplateName(tvo.getTemplateName());
			sqlDAO.save(s);
			//更新站点的Session缓存
			SessionUtil.setSite(s);
		}else{
			//导入的是模版插件
			
		}
		
		//更新模版变量缓存
		reloadTemplateVarCache(request);
		
		//更新模版页面缓存
		reloadTemplatePageCache(request);
		
		return vo;
	}
	
	/**
	 * 重新加载模版页面的缓存数据
	 * @param request
	 */
	public void reloadTemplatePageCache(HttpServletRequest request){
		request.getSession().setAttribute(sessionTemplatePageListVO, null);
		getTemplatePageListByCache(request);
	}
	
	/**
	 * 重新加载模版变量的缓存数据。
	 * <br/>包含生成整站时，已替换了通用动态标签的已编译的模版变量
	 * @param request
	 */
	public void reloadTemplateVarCache(HttpServletRequest request){
		//先清空掉
		SessionUtil.setTemplateVarMapForOriginal(null);
		SessionUtil.setTemplateVarCompileDataMap(null);
		//再加载入缓存
		getTemplateVarAndDateListByCache();
	}
	
	public List<com.xnx3.wangmarket.admin.entity.TemplateVar> getTemplateVarList(){
		Site site = SessionUtil.getSite();

	    //模版名字检索，是否是使用的导入的模版，若是使用的导入的模版，则只列出导入的模版变量
	    String templateNameWhere = "";
	    if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
	    	templateNameWhere = " AND template_var.template_name = '"+ site.getTemplateName() +"'";
	    }
	    
	    return sqlDAO.findBySqlQuery("SELECT * FROM template_var WHERE siteid = "+site.getId() + templateNameWhere + " ORDER BY id DESC", com.xnx3.wangmarket.admin.entity.TemplateVar.class);
	}

	public BaseVO deleteTemplatePageForCache(int templatePageId, HttpServletRequest request) {
		TemplatePageListVO vo = getTemplatePageListByCache(request);
		int deleteId = -1;	//要删除得list得下表
		for (int i = 0; i < vo.getList().size(); i++) {
			if(vo.getList().get(i).getTemplatePage().getId() - templatePageId == 0){
				deleteId = i;
				break;
			}
		}
		
		//如果在缓存中发现了它，才会删除，并更新缓存，否则不用理会
		if(deleteId > -1){
			vo.getList().remove(deleteId);
			//删除后重新讲起加入Session
			request.getSession().setAttribute(sessionTemplatePageListVO, vo);
		}
		
		return new BaseVO();
	}

	public TemplatePageVO getTemplatePageByNameForCache(HttpServletRequest request, String templatePageName) {
		TemplatePageVO vo = new TemplatePageVO();
		
		if(templatePageName == null || templatePageName.length() == 0){
			vo.setBaseVO(TemplatePageVO.FAILURE, "要调取的模版页的名字不能为空");
		}
		
		TemplatePageListVO tpl = getTemplatePageListByCache(request);
		for (int i = 0; i < tpl.getList().size(); i++) {
			TemplatePageVO tpVO = tpl.getList().get(i);
			if(templatePageName.equals(tpVO.getTemplatePage().getName())){
				TemplatePageData tpd = new TemplatePageData();
				String tpd_S = getTemplatePageTextByCache(tpVO.getTemplatePage().getId(), request);
				tpd.setId(tpVO.getTemplatePage().getId());
				tpd.setText(tpd_S == null ? Template.newHtml : tpd_S);
				tpVO.setTemplatePageData(tpd);
				return tpVO;
			}
		}
		
		vo.setBaseVO(TemplatePageVO.FAILURE, "要调取的模版页不存在");
		return vo;
	}

	public TemplateVarListVO getTemplateVarListByCache() {
		if(SessionUtil.getTemplateVarMapForOriginal() == null){
			loadDatabaseTemplateVarToCache();
		}
		
		TemplateVarListVO listVO = new TemplateVarListVO();
		
		List<TemplateVarVO> list = new ArrayList<TemplateVarVO>();
		for (Map.Entry<String, TemplateVarVO> entry : SessionUtil.getTemplateVarMapForOriginal().entrySet()) {
			list.add(entry.getValue());
		}
		listVO.setList(list);
		return listVO;
	}

	public TemplatePageListVO getTemplatePageAndDateListByCache(
			HttpServletRequest request) {
		//获取到templatePageList的列表
		TemplatePageListVO vo = getTemplatePageListByCache(request);
		
		//对pageList的列表进行循环检测，看是否有Data为null的,若有为null的，则进行查数据表进行补全
		boolean usedSqlQuery = false;	//是否有必要使用SQL查询数据库，重新拉取TemplatePageData的list数据
		for (int i = 0; i < vo.getList().size(); i++) {
			if(vo.getList().get(i).getTemplatePageData() == null){
				usedSqlQuery = true;	//有某项为空，需要重新从数据库拉取TemplatePageData的list列表
				break;
			}
		}
		
		if(usedSqlQuery){
			Site site = SessionUtil.getSite();
			
			//根据登录网站当前所使用的模板名字，来进行筛选要取出的模板页面
		    String templateNameWhere = "";
		    if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
		    	templateNameWhere = " AND template_page.template_name = '"+ site.getTemplateName() +"'";
		    }
		    
		    //从数据库中拉取 pageData的列表
			List<TemplatePageData> tpdList = sqlDAO.findBySqlQuery("SELECT template_page_data.* FROM template_page, template_page_data WHERE template_page.id = template_page_data.id AND siteid = "+site.getId() + templateNameWhere, TemplatePageData.class);
		
			//将拉取的pageData列表，由List转换为Map，方便随时取。map key: template_page.id
			Map<Integer, TemplatePageData> tpdMap = new HashMap<Integer, TemplatePageData>();
			for (int i = 0; i < tpdList.size(); i++) {
				TemplatePageData tpd = tpdList.get(i);
				tpdMap.put(tpd.getId(), tpd);
			}
			
			//对 TemplatePageListVO 的list进行排查，进行 templatePageData进行重新填充覆盖
			for (int i = 0; i < vo.getList().size(); i++) {
				TemplatePageVO tpvo = vo.getList().get(i);
				tpvo.setTemplatePageData(tpdMap.get(tpvo.getTemplatePage().getId()));
			}
			
		}
		
		return vo;
	}
	

	public TemplateVarListVO getTemplateVarAndDateListByCache() {
		//获取到templateVarList的列表
		TemplateVarListVO vo = getTemplateVarListByCache();
		
		//对varList的列表进行循环检测，看是否有Data为null的,若有为null的，则进行查数据表进行补全
		boolean usedSqlQuery = false;	//是否有必要使用SQL查询数据库，重新拉取TemplatePageData的list数据
		for (int i = 0; i < vo.getList().size(); i++) {
			if(vo.getList().get(i).getTemplateVarData() == null){
				usedSqlQuery = true;	//有某项为空，需要重新从数据库拉取TemplatePageData的list列表
				break;
			}
		}
		
		if(usedSqlQuery){
			Site site = SessionUtil.getSite();
			
			//根据登录网站当前所使用的模板名字，来进行筛选要取出的模板页面
		    String templateNameWhere = "";
		    if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
		    	templateNameWhere = " AND template_var.template_name = '"+ site.getTemplateName() +"'";
		    }
		    
		    //从数据库中拉取 varData的列表
			List<TemplateVarData> tvdList = sqlDAO.findBySqlQuery("SELECT template_var_data.* FROM template_var, template_var_data WHERE template_var.id = template_var_data.id AND siteid = "+site.getId() + templateNameWhere, TemplateVarData.class);
		
			//将拉取的varData列表，由List转换为Map，方便随时取。map key: template_var.id
			Map<Integer, TemplateVarData> tvdMap = new HashMap<Integer, TemplateVarData>();
			for (int i = 0; i < tvdList.size(); i++) {
				TemplateVarData tvd = tvdList.get(i);
				tvdMap.put(tvd.getId(), tvd);
			}
			
			//对 TemplateVarListVO 的list进行排查，进行 templateVarData进行重新填充覆盖
			for (int i = 0; i < vo.getList().size(); i++) {
				TemplateVarVO tvvo = vo.getList().get(i);
				tvvo.setTemplateVarData(tvdMap.get(tvvo.getTemplateVar().getId()));
			}
			
		}
		
		return vo;
	}

	public BaseVO deleteTemplateVarForCache(int templateVarId) {
		String deleteVarName = null;	//要删除的模版变量
		for (Map.Entry<String, TemplateVarVO> entry : SessionUtil.getTemplateVarMapForOriginal().entrySet()) {
			if(entry.getValue().getTemplateVar().getId() - templateVarId == 0){
				deleteVarName = entry.getValue().getTemplateVar().getVarName();
			}
		}
		
		SessionUtil.getTemplateVarMapForOriginal().remove(deleteVarName);
		if(SessionUtil.getTemplateVarCompileDataMap() != null && SessionUtil.getTemplateVarCompileDataMap().get(deleteVarName) != null){
			SessionUtil.getTemplateVarCompileDataMap().remove(deleteVarName);
		}
		return new BaseVO();
	}
	
//	
//	public TemplateVO getTemplateForDatabase(String name){
//		com.xnx3.wangmarket.admin.entity.Template template = null;
//		if(name != null){
//			template = TemplateUtil.databaseTemplateMapForName.get(name);
//			if(template == null){
//				template = sqlDAO.findAloneByProperty(com.xnx3.wangmarket.admin.entity.Template.class, "name", name);
//				if(template != null){
//					//将其加入map进行缓存
//					TemplateUtil.databaseTemplateMapForName.put(name, template);
//				}
//			}
//		}
//		
//		TemplateVO vo = new TemplateVO();
//		if(template == null){
//			vo.setBaseVO(BaseVO.FAILURE, "模版不存在");
//		}else{
//			vo.setTemplate(template);
//		}
//		return vo;
//	}
	

	/**
	 * 生成整站html
	 * @param site 如果不传入，则是使用Session缓存的,， 如果传入，则是某个网站操作其他的网站生成，都是从数据库读取的
	 */
	public GenerateSiteVO generateSiteHTML(HttpServletRequest request, Site site){
		GenerateSiteVO vo = new GenerateSiteVO();
		boolean useDatabase = false;	//是否是从数据库中取数据，false不从数据库取，从缓存取。 true从数据库中取。这种情况是操作别人的网站时 
		if(site == null){
			site = SessionUtil.getSite();
			useDatabase = false;
		}else{
			useDatabase = true;
		}
		
		if(site == null){
			vo.setBaseVO(BaseVO.FAILURE, "尚未登陆");
			return vo;
		}
		
		TemplateCMS template = new TemplateCMS(site, TemplateUtil.getTemplateByName(site.getTemplateName()));
		template.setSiteVar(siteVarService.getVar(site.getId()));
		//取得当前网站所有模版页面
//		TemplatePageListVO templatePageListVO = templateService.getTemplatePageListByCache(request);
		//取得当前网站首页模版页面
		TemplatePageVO templatePageIndexVO = null;
		TemplatePageListVO templatePageListVO = null;
		if(useDatabase){
			templatePageListVO = getTemplatePageListByDatabase(site);
		}else{
			templatePageListVO = getTemplatePageListByCache(request);
		}
		templatePageIndexVO = getTemplatePageIndexByTemplatePageListVO(templatePageListVO);
		
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
		
		//对栏目进行缓存，以栏目id为key，将栏目加入进Map中。用id来取栏目。 同 columnMap. v4.7.1增加
		Map<Integer, SiteColumn> columnMapForId = new HashMap<Integer, SiteColumn>();
		for (Map.Entry<String, SiteColumn> entry : columnMap.entrySet()) { 
			columnMapForId.put(entry.getValue().getId(), entry.getValue());
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
		
		
		//v2.24，将模版变量的比对，改为模版页面
		TemplatePageListVO tplVO = getTemplatePageListByCache(request);
		if(tplVO == null || tplVO.getList().size() == 0){
			vo.setBaseVO(BaseVO.FAILURE, "当前网站尚未选择/导入/增加模版，生成失败！网站有模版后才能根据模版生成整站！");
			return vo;
		}
		
		//模版变量
		Map<String, TemplateVarVO> templateVarVOMapForOriginal = null;
		Map<String, String> templateVarCompileDataMap = null;
		if(useDatabase){
			TemplateVarAndDataMapVO templateVarAndDataMapVO = getTemplateVarAndDataByDatabase(site);
			templateVarVOMapForOriginal = templateVarAndDataMapVO.getTemplateVarMapForOriginal();
			templateVarCompileDataMap = templateVarAndDataMapVO.getCompileMap();
		}else{
			templateVarVOMapForOriginal = SessionUtil.getTemplateVarMapForOriginal();
			templateVarCompileDataMap = SessionUtil.getTemplateVarCompileDataMap();
			if(templateVarVOMapForOriginal == null){
				//如果为空，避免空指针，重新赋予session值
				templateVarVOMapForOriginal = new HashMap<String, TemplateVarVO>();
				templateVarCompileDataMap = new HashMap<String, String>();
				SessionUtil.setTemplateVarMapForOriginal(templateVarVOMapForOriginal);
				SessionUtil.setTemplateVarCompileDataMap(templateVarCompileDataMap);
			}
		}
		
		
		//v4.7加入，避免没有模版变量时，生成整站报错
		for (Map.Entry<String, TemplateVarVO> entry : templateVarVOMapForOriginal.entrySet()) {  
			//替换公共标签
			String v = template.replacePublicTag(entry.getValue().getTemplateVarData().getText());
			//替换栏目的动态调用标签
			v = template.replaceSiteColumnBlock(v, columnNewsMap, columnMap, columnTreeMap, true, null, newsDataMap);
			templateVarCompileDataMap.put(entry.getKey(), v);
		}
		if(!useDatabase){
			//如果不使用数据库，还要将结果缓存进session
			SessionUtil.setTemplateVarCompileDataMap(templateVarCompileDataMap);
		}
		
		/*
		 * 进行第二步，对列表页模版、详情页模版进行通用标签等的替换，将其处理好。等生成的时候，直接取出来替换news、column即可
		 */
//		TemplatePageListVO tpl = templatePageListVO;	//模版页列表
		Map<String, String> templateCacheMap = new HashMap<String, String>();	//替换好通用标签等的模版都缓存入此。Map<templatePage.name, templatePageData.text>
		for (int i = 0; i < templatePageListVO.getList().size(); i++) {
			TemplatePageVO tpVO = templatePageListVO.getList().get(i);
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
//			text = template.assemblyTemplateVar(text);
			text = template.assemblyTemplateVar(text, templateVarCompileDataMap);
			
			//预处理后，将其缓存入Map，等待生成页面时直接获取
			templateCacheMap.put(tpVO.getTemplatePage().getName(), text);
		}
		//最后还要将获得到的内容缓存入Session，下次就不用去数据库取了

		
		//生成首页
		String indexHtml = "";	//首页的html代码
		if(templatePageIndexVO.getResult() - BaseVO.FAILURE == 0){
			//没有获取到首页模板，这个模板中没有首页，那么首页给一个默认输出
			indexHtml = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>模板中没有首页模板</title></head><body>	您好，看到此页面，说明您的模板中没有首页模板！你可以登录你网站管理后台，找到左侧模板管理-模板页面，看看里面的类型一栏，是不是没有首页模板</body></html>";
		}else{
			//有首页，生成首页
			indexHtml = templateCacheMap.get(templatePageIndexVO.getTemplatePage().getName());
			//替换首页中存在的栏目的动态调用标签
			indexHtml = template.replaceSiteColumnBlock(indexHtml, columnNewsMap, columnMap, columnTreeMap, true, null, newsDataMap);
			indexHtml = template.replacePublicTag(indexHtml);	//替换公共标签
		}
		//生成首页保存到OSS或本地盘
		AttachmentUtil.putStringFile("site/"+site.getId()+"/index.html", indexHtml);
		
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
					vo.setBaseVO(BaseVO.FAILURE, "栏目["+siteColumn.getName()+"]未绑定模版列表页面，请去绑定，或删除这个栏目");
					return vo;
				}
				//替换列表模版中的动态栏目调用(动态标签引用)
				listTemplateHtml = template.replaceSiteColumnBlock(listTemplateHtml, columnNewsMap, columnMap, columnTreeMap, false, siteColumn, newsDataMap);	
				
				//生成其列表页面
				template.generateListHtmlForWholeSite(listTemplateHtml, siteColumn, columnNewsList, newsDataMap, columnMapForId);
				
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
						}
					}
				}
				
			}else if(siteColumn.getType() - SiteColumn.TYPE_ALONEPAGE == 0 || siteColumn.getType() - SiteColumn.TYPE_PAGE == 0){
				//独立页面，只生成内容模版
				if(siteColumn.getEditMode() - SiteColumn.EDIT_MODE_TEMPLATE == 0){
					//模版式编辑，无 news ， 则直接生成
					template.generateViewHtmlForTemplateForWholeSite(null, siteColumn, new NewsDataBean(null), viewTemplateHtml, null, null);
				}else{
					//UEditor、输入模型编辑方式
					for (int i = 0; i < columnNewsList.size(); i++) {
						News news = columnNewsList.get(i);
						template.generateViewHtmlForTemplateForWholeSite(news, siteColumn, newsDataMap.get(news.getId()), viewTemplateHtml, null, null);
					}
				}
			}else{
				//其他栏目不管，当然，也没有其他类型栏目了，v4.6版本更新后，CMS模式一共就这两种类型的
			}
		} 
		
		vo.setNewsDataMap(newsDataMap);
		vo.setNewsMap(columnNewsMap);
		vo.setSiteColumnMap(columnMap);
		return vo;
	}

	@Override
	public Map<String, TemplateVarVO> getTemplateVarVoMapByCache() {
		if(Func.getUserBeanForShiroSession().getTemplateVarMapForOriginal() == null){
			//判断一下，缓存中是否有模版变量，若所没有，那么要缓存
			getTemplateVarAndDateListByCache();
		}
		//获取Session缓存中的模版变量数据
		Map<String, TemplateVarVO> templateVarVOMap = Func.getUserBeanForShiroSession().getTemplateVarMapForOriginal();
		return templateVarVOMap;
		
	}

	@Override
	public TemplateVarAndDataMapVO getTemplateVarAndDataByDatabase(Site site) {
		TemplateVarAndDataMapVO vo = new TemplateVarAndDataMapVO();
		if(site == null){
			vo.setBaseVO(TemplateVarAndDataMapVO.FAILURE, "网站不存在");
			return vo;
		}
		
		//模版名字检索，是否是使用的导入的模版，若是使用的导入的模版，则只列出导入的模版变量
	    String templateNameWhere = "";
	    if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
	    	templateNameWhere = " AND template_var.template_name = '"+ site.getTemplateName() +"'";
	    }

		//从数据库中取模板变量主表、内容表
		List<com.xnx3.wangmarket.admin.entity.TemplateVar> templateVarList = sqlDAO.findBySqlQuery("SELECT * FROM template_var WHERE siteid = " + site.getId() + templateNameWhere, com.xnx3.wangmarket.admin.entity.TemplateVar.class);
		List<TemplateVarData> templateVarDataList = sqlDAO.findBySqlQuery("SELECT template_var_data.* FROM template_var,template_var_data WHERE template_var.siteid = " + site.getId() + " AND template_var.id = template_var_data.id" + templateNameWhere, TemplateVarData.class);
		//将取到的模板变量内容表进行Map组合， key：templateVar.id
		Map<Integer, TemplateVarData> tvdMap = new HashMap<Integer, TemplateVarData>();
		for (int i = 0; i < templateVarDataList.size(); i++) {
			TemplateVarData templateVarData = templateVarDataList.get(i);
			tvdMap.put(templateVarData.getId(), templateVarData);
		}
		//将模板变量分表、内容表合起来，存入缓存
		Map<String, String> compileMap = new HashMap<String, String>();	//可替换的，要存入缓存的map  var_name－text
		Map<String, TemplateVarVO> templateVarMapForOriginal = new HashMap<String, TemplateVarVO>();
		for (int i = 0; i < templateVarList.size(); i++) {
			com.xnx3.wangmarket.admin.entity.TemplateVar templateVar = templateVarList.get(i);
			TemplateVarVO tvvo = new TemplateVarVO();
			tvvo.setTemplateVar(templateVar);
			tvvo.setTemplateVarData(tvdMap.get(templateVar.getId()));
			
			compileMap.put(templateVar.getVarName(), tvvo.getTemplateVarData().getText());
			templateVarMapForOriginal.put(templateVar.getVarName(), tvvo);
		}
		
		vo.setTemplateVarMapForOriginal(templateVarMapForOriginal);
		vo.setCompileMap(compileMap);
		return vo;
	}
	
}
