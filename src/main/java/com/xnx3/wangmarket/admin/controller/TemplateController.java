package com.xnx3.wangmarket.admin.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.MD5Util;
import com.xnx3.StringUtil;
import com.xnx3.ZipUtil;
import com.xnx3.file.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.func.Log;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.net.OSSUtil;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.cache.Template;
import com.xnx3.wangmarket.admin.cache.TemplateCMS;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.entity.TemplatePage;
import com.xnx3.wangmarket.admin.entity.TemplatePageData;
import com.xnx3.wangmarket.admin.entity.TemplateVar;
import com.xnx3.wangmarket.admin.entity.TemplateVarData;
import com.xnx3.wangmarket.admin.init.TemplateTemporaryFolder;
import com.xnx3.wangmarket.admin.pluginManage.PluginManage;
import com.xnx3.wangmarket.admin.pluginManage.SitePluginBean;
import com.xnx3.wangmarket.admin.service.InputModelService;
import com.xnx3.wangmarket.admin.service.SiteColumnService;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.service.TemplateService;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.admin.util.TemplateAdminMenuUtil;
import com.xnx3.wangmarket.admin.util.TemplateUtil;
import com.xnx3.wangmarket.admin.util.interfaces.TemplateUtilFileMove;
import com.xnx3.wangmarket.admin.vo.RestoreTemplateSubmitCheckDataVO;
import com.xnx3.wangmarket.admin.vo.TemplateCompareVO;
import com.xnx3.wangmarket.admin.vo.TemplateListVO;
import com.xnx3.wangmarket.admin.vo.TemplatePageListVO;
import com.xnx3.wangmarket.admin.vo.TemplatePageVO;
import com.xnx3.wangmarket.admin.vo.TemplateVO;
import com.xnx3.wangmarket.admin.vo.TemplateVarListVO;
import com.xnx3.wangmarket.admin.vo.TemplateVarVO;
import com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare.InputModelCompare;
import com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare.SiteColumnCompare;
import com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare.TemplatePageCompare;
import com.xnx3.wangmarket.admin.vo.bean.template.TemplateCompare.TemplateVarCompare;
import com.xnx3.wangmarket.superadmin.entity.AgencyData;

/**
 * 模版相关操作
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/template")
public class TemplateController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	@Resource
	private TemplateService templateService;
	@Resource
	private InputModelService inputModelService;
	@Resource
	private SiteColumnService siteColumnService;
	
	/**
	 * 首页，自定义模版模式登陆进来后，首页
	 */
	@RequestMapping("/index${url.suffix}")
	public String index(HttpServletRequest request,Model model){
		TemplatePageVO vo = templateService.getTemplatePageIndexByCache(request);
		if(vo == null || vo.getResult() - TemplatePageVO.FAILURE == 0){
			//当前还没有模版页，那么可能是刚开通网站，还没有模版，默认跳转到选择模版的页面
			model.addAttribute("needSelectTemplate", "1");
		}else{
			//将模版变量装载入Session。 必须要装载，将模版变量缓存入session，以便后面使用
			templateService.loadDatabaseTemplateVarToCache();
			model.addAttribute("templatePage", vo.getTemplatePage());
		}
		
		AliyunLog.addActionLog(getSiteId(), "进入CMS模式网站后台首页-iframe main");
		
		//获取网站后台管理系统有哪些功能插件，也一块列出来,以直接在网站后台中显示出来
		String pluginMenu = "";
		if(PluginManage.cmsSiteClassManage.size() > 0){
			for (Map.Entry<String, SitePluginBean> entry : PluginManage.cmsSiteClassManage.entrySet()) {
				SitePluginBean bean = entry.getValue();
				pluginMenu += "<dd class=\"twoMenu\"><a id=\""+entry.getKey()+"\" class=\"subMenuItem\" href=\"javascript:loadIframeByUrl('"+bean.getMenuHref()+"'), notUseTopTools();\">"+bean.getMenuTitle()+"</a></dd>";
			}
		}
		model.addAttribute("pluginMenu", pluginMenu);
		
		//左侧菜单
		model.addAttribute("menuHTML", TemplateAdminMenuUtil.getLeftMenuHtml());
		
		UserBean userBean = getUserBean();
		User user = getUser();
		model.addAttribute("password", MD5Util.MD5(user.getPassword()));
		model.addAttribute("siteRemainHintJavaScript", siteService.getSiteRemainHintForJavaScript(userBean.getSite(), userBean.getParentAgency()));
		model.addAttribute("siteUrl", Func.getDomain(getSite()));
		model.addAttribute("site", getSite());
		model.addAttribute("parentAgency", getParentAgency());	//上级代理
		model.addAttribute("user", user);
		model.addAttribute("im_kefu_websocketUrl", com.xnx3.wangmarket.im.Global.websocketUrl);
		model.addAttribute("autoAssignDomain", G.getFirstAutoAssignDomain());	//自动分配的域名，如 wang.market
		model.addAttribute("SITEUSER_FIRST_USE_EXPLAIN_URL", Global.get("SITEUSER_FIRST_USE_EXPLAIN_URL"));
		return "template/index";
	}
	
	/**
	 * 登陆成功之后进入的页面，欢迎页面
	 */
	@RequestMapping("/welcome${url.suffix}")
	public String welcome(HttpServletRequest request,Model model){
		AliyunLog.addActionLog(getSiteId(), "进入CMS模式网站后台欢迎页面");
		//上级代理的变长表数据
		AgencyData parentAgencyData = getParentAgencyData();
		
		User user = getUser();
		model.addAttribute("site", getSite());
		model.addAttribute("parentAgency", getParentAgency());	//上级代理
		//上级代理的公告内容，要显示出来的
		model.addAttribute("parentAgencyNotice", parentAgencyData == null ? "":parentAgencyData.getNotice());	
		model.addAttribute("user", user);
		return "template/welcome";
	}
	
	/**
	 * 模版变量列表
	 */
	@RequestMapping("/templateVarList${url.suffix}")
	public String templateVarList(HttpServletRequest request,Model model){
		AliyunLog.addActionLog(getSiteId(), "进入模版变量列表");
		
	    model.addAttribute("list", templateService.getTemplateVarList());
	    return "template/templateVarList";
	}


	/**
	 * 模版页面列表
	 * @param templatePageName 模版页面名字， templatePage.name 如果有传入，则进入列表后会自动进入打开，进入这个页面的编辑模式。 另外，如果传入 templatepage_type_index 则会编辑首页
	 */
	@RequestMapping("/templatePageList${url.suffix}")
	public String templatePageList(HttpServletRequest request,Model model,
			@RequestParam(value = "templatePageName", required = false , defaultValue="") String templatePageName){
		Sql sql = new Sql(request);
	    sql.setSearchTable("template_page");
	    //增加添加搜索字段。这里的搜索字段跟log表的字段名对应
	    sql.setSearchColumn(new String[]{"name"});
	    sql.appendWhere("siteid = "+getSiteId());
	    //查询log数据表的记录总条数
	    int count = sqlService.count("template_page", sql.getWhere());
	    //每页显示100条
	    Page page = new Page(count, 100, request);
	    //创建查询语句，只有SELECT、FROM，原生sql查询。其他的where、limit等会自动拼接
	    sql.setSelectFromAndPage("SELECT * FROM template_page", page);
	    
	    //当用户没有选择排序方式时，系统默认排序。
	    sql.setDefaultOrderBy("id DESC");
	    List<TemplatePage> list = sqlService.findBySql(sql, TemplatePage.class);
	    
	    AliyunLog.addActionLog(getSiteId(), "进入模版页列表");
	    
	    
	    if(templatePageName.length() > 0){
	    	//如果 templatePageName 有值，那么会自动跳转进入编辑内容
	    	if(templatePageName.equals("templatepage_type_index")){
	    		//编辑当前网站的首页
	    		for (int i = 0; i < list.size(); i++) {
	    			TemplatePage tp = list.get(i);
	    			if(tp.getType() - TemplatePage.TYPE_INDEX == 0){
	    				model.addAttribute("autoEditText", "<script>editText('"+tp.getName()+"','"+tp.getType()+"', '"+tp.getEditMode()+"');</script>");
	    				break;
	    			}
				}
	    	}else{
	    		//编辑指定的 templatePage.name 页面
	    		for (int i = 0; i < list.size(); i++) {
	    			TemplatePage tp = list.get(i);
	    			if(tp.getName().equals(templatePageName)){
	    				model.addAttribute("autoEditText", "<script>editText('"+tp.getName()+"','"+tp.getType()+"', '"+tp.getEditMode()+"');</script>");
	    				break;
	    			}
				}
	    	}
	    }
	    
	    //将数据记录传到页面以供显示
	    model.addAttribute("list", list);
	    return "template/templatePageList";
	}
	
	/**
	 * 添加／编辑模版变量
	 * @param templateVarName 要编辑得模版变量名字，添加时无需传入此项
	 * @param templateName 模版名字，要给哪个模版添加模版变量。修改时无需传入此项
	 */
	@RequestMapping("templateVar${url.suffix}")
	public String templateVar(HttpServletRequest request,Model model,
			@RequestParam(value = "templateVarName", required = false , defaultValue="") String templateVarName,
			@RequestParam(value = "templateName", required = false , defaultValue="") String templateName
			){
		templateVarName = filter(templateVarName);
		templateName = filter(templateName);
		Site site = getSite();
		
		//如果是编辑模式
		if(templateVarName.length() > 0){
			TemplateVar templateVar = sqlService.findAloneBySqlQuery("SELECT * FROM template_var WHERE siteid = "+site.getId()+" AND var_name = '"+templateVarName+"'", TemplateVar.class);
			if(templateVar == null){
				return error(model, "没有发现属于您的、名字为"+templateVarName+"的模版变量");
			}
			
			AliyunLog.addActionLog(getSiteId(), "编辑模式打开模版变量："+templateVarName);
			model.addAttribute("templateVar", templateVar);
		}else{
			if(templateName.length()<0){
				return error(model, "您要给哪个模版添加模版变量呢");
			}
			
			AliyunLog.addActionLog(getSiteId(), "打开新建模版变量页面，准备为模版："+templateName+"新建模版变量");
			model.addAttribute("templateName", templateName);
		}
		
		return "template/templateVar";
	}
	
	/**
	 * 保存模版变量
	 */
	@RequestMapping(value="saveTemplateVar${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO saveTemplateVar(TemplateVar templateVarInput, 
			@RequestParam(value = "text", required = false , defaultValue="") String text,
			HttpServletRequest request,Model model){
		Site site = getSite();
		
		TemplateVar templateVar;
		TemplateVarData templateVarData = null;
		if(templateVarInput.getId() == null || templateVarInput.getId() == 0){
			//添加
			templateVar = new TemplateVar();
			templateVar.setAddtime(DateUtil.timeForUnix10());
			templateVar.setUserid(getUserId());
			templateVar.setTemplateName(getSite().getTemplateName());
			templateVar.setSiteid(site.getId());
			
			templateVarData = new TemplateVarData();
		}else{
			//修改
			templateVar = sqlService.findById(TemplateVar.class, templateVarInput.getId());
			if(templateVar.getSiteid() - site.getId() != 0){
				return error("不属于您，无法修改");
			}
			
			templateVarData = sqlService.findById(TemplateVarData.class, templateVar.getId());
		}
		templateVar.setUpdatetime(DateUtil.timeForUnix10());
		templateVar.setVarName(filter(templateVarInput.getVarName()));	//过滤掉空格等。因为此会在Sql原生语句中出现
		templateVar.setRemark(filter(templateVarInput.getRemark()));
		
		sqlService.save(templateVar);
		if(templateVar.getId() != null && templateVar.getId() > 0){
			//保存成功，再判断templateVarData是否是添加，若是添加的话，得赋予其id
			if(templateVarData.getId() == null || templateVarData.getId() == 0){
				templateVarData.setId(templateVar.getId());
			}
			templateVarData.setText(text);
			sqlService.save(templateVarData);
			//刷新缓存
			templateService.updateTemplateVarForCache(templateVar, templateVarData);
			
			AliyunLog.addActionLog(templateVar.getId(), "保存模版变量："+templateVar.getVarName());
		}
		
		return success(templateVar.getId()+"");
	}
	

	/**
	 * 添加／编辑模版页面
	 * @param pageName 要编辑得模版页面名字，添加时不要传入此项
	 */
	@RequestMapping("templatePage${url.suffix}")
	public String templatePage(HttpServletRequest request,Model model,
			@RequestParam(value = "pageName", required = false , defaultValue="") String pageName
			){
		pageName = filter(pageName);
		TemplatePage templatePage;
		//如果是编辑模式
		if(pageName.length() > 0){
			templatePage = sqlService.findAloneBySqlQuery("SELECT * FROM template_page WHERE siteid = "+getSiteId()+" AND name = '"+pageName+"'", TemplatePage.class);
			if(templatePage == null){
				return error(model, "要修改的模版页面不存在");
			}
			String text = templateService.getTemplatePageTextByCache(templatePage.getId(), request);	//模版页面的内容
			
			AliyunLog.addActionLog(templatePage.getId(), "进入编辑模版页面："+templatePage.getName());
			
			model.addAttribute("text", text);
		}else{
			AliyunLog.addActionLog(0, "进入新建模版页面");
			
			templatePage = new TemplatePage();
			templatePage.setEditMode(TemplatePage.EDIT_MODE_CODE);
		}
		
		model.addAttribute("templatePage", templatePage);
		return "template/templatePage";
	}
	

	/**
	 * 保存模版页面
	 */
//	@Deprecated
//	@RequestMapping(value="saveTemplatePageOld", method = RequestMethod.POST)
//	@ResponseBody
//	public BaseVO saveTemplatePageOld(TemplatePage templatePageInput, 
//			@RequestParam(value = "text", required = false , defaultValue="") String text,
//			HttpServletRequest request,Model model){
//		
//		TemplatePage templatePage;
//		TemplatePageData templatePageData = null;
//		if(templatePageInput.getId() == null || templatePageInput.getId() == 0){
//			//添加
//			templatePage = new TemplatePage();
//			templatePage.setUserid(getUserId());
//			templatePage.setSiteid(getSiteId());
//			
//			templatePageData = new TemplatePageData();
//		}else{
//			//修改
//			templatePage = sqlService.findById(TemplatePage.class, templatePageInput.getId());
//			if(templatePage.getUserid() - getUserId() != 0){
//				return error("不属于您，无法修改");
//			}
//			
//			templatePageData = sqlService.findById(TemplatePageData.class, templatePage.getId());
//		}
//		
//		templatePage.setName(filter(templatePageInput.getName()));
//		sqlService.save(templatePage);
//		if(templatePage.getId() != null && templatePage.getId() > 0){
//			//保存成功，再判断templateVarData是否是添加，若是添加的话，得赋予其id
//			if(templatePageData.getId() == null || templatePageData.getId() == 0){
//				templatePageData.setId(templatePage.getId());
//			}
//			templatePageData.setText(text);
//			sqlService.save(templatePageData);
//			
//			//刷新当前用户的模版页面缓存
//			templateService.updateTemplatePageForCache(templatePage, templatePageData, request);
//			
//			AliyunLog.addActionLog(getSiteId(), "保存模版页成功（已废弃接口）");
//		}
//		
//		return success(templatePage.getId()+"");
//	}
	

	/**
	 * 保存模版页面，只保存{@link TemplatePage}，不保存模版页面的内容，内容单独保存
	 * @param templatePageInput 要保存的{@link TemplatePage}信息
	 */
	@RequestMapping(value="saveTemplatePage${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO saveTemplatePage(TemplatePage templatePageInput,
			HttpServletRequest request,Model model){
		Site site = getSite();
		
		//要保存的模版页实例
		TemplatePage templatePage;
		
		//如果时编辑，修改
		if(templatePageInput.getId() != null && templatePageInput.getId() > 0){
			templatePage = sqlService.findById(TemplatePage.class, templatePageInput.getId());
			if(templatePage == null){
				return error("要修改的模版页不存在！");
			}
			if(templatePage.getSiteid() - site.getId() != 0){
				return error("不属于您，无法操作！");
			}
		}else{
			//添加
			templatePage = new TemplatePage();
			templatePage.setUserid(getUserId());
			templatePage.setSiteid(site.getId());
			templatePage.setTemplateName(site.getTemplateName());
		}
		
		//判断当前模版页的名字是否存在了，因为同一个模版下，是不能有重名的模版页的
		String templateNameWhere = "";
		if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
			templateNameWhere = " AND template_name = '"+site.getTemplateName()+"'";
		}
		//当前模版页是保存还是新键。若是保存的话，要排除当前页面的名字重名问题
		String idWhere = (templatePageInput.getId() != null && templatePageInput.getId() > 0) ? " AND id <> " + templatePageInput.getId() : "";
		int count = sqlService.count("template_page", "WHERE siteid = " + site.getId() + " AND name = '" + Sql.filter(templatePageInput.getName()) + "'" + templateNameWhere + idWhere);
		if(count > 0){
			return error("页面名字" + templatePageInput.getName() + "已存在！请换个页面名吧");
		}
		
		//判断模版是否是首页模版，进而判断，首页模版只能有一个
		if(templatePageInput.getType() - TemplatePage.TYPE_INDEX == 0){
			TemplatePageVO indexVO = templateService.getTemplatePageIndexByCache(request);
			if(indexVO.getResult() - TemplatePageVO.SUCCESS == 0){
				TemplatePage itp = indexVO.getTemplatePage();
				//发现首页模版了，判断其是否是同一个。若不是同一个，那就是存在多个首页模版，是不允许的
				//判断当前是增加还是修改
				if(templatePageInput.getId() != null && templatePageInput.getId() > 0){
					//是修改，判断其id跟现有的首页模版id是否相同，若不相同，那肯定就是新的首页模版
					if(templatePageInput.getId() - itp.getId() != 0){
						return error("首页模版每个网站只能存在一个哦！");
					}
				}else{
					//是新增得，那肯定就不行了,都有首页模版了
					return error("首页模版每个网站只能存在一个哦！");
				}
			}
		}
		
		
		templatePage.setName(filter(templatePageInput.getName()));
		templatePage.setType(templatePageInput.getType());
		templatePage.setRemark(filter(templatePageInput.getRemark()));
		templatePage.setEditMode(templatePageInput.getEditMode());
		sqlService.save(templatePage);
		if(templatePage.getId() != null && templatePage.getId() > 0){
			
			//保存成功，判断是否是新增，若是新增，那么还要增加默认模版页内容
			if(templatePageInput.getId() == null || templatePageInput.getId() == 0){
				//默认增加模版页内容
				TemplatePageData templatePageData = new TemplatePageData();
				templatePageData.setId(templatePage.getId());
				templatePageData.setText(Template.newHtml);
				sqlService.save(templatePageData);
			}
			
			//日志
			AliyunLog.addActionLog(templatePage.getId(), "保存模版页成功，模版页："+templatePage.getName());
			
			//刷新Session缓存
			templateService.updateTemplatePageForCache(templatePage, null, request);
			
			return success(templatePage.getId()+"");
		}else{
			AliyunLog.addActionLog(templatePage.getId(), "保存模版页失败，模版页："+templatePage.getName());
			return error("保存失败");
		}
	}
	
	/**
	 * 获取指定模版变量的内容
	 * @param varName 模版变量变量名字
	 * @throws IOException
	 */
	@RequestMapping("getTemplateVarText${url.suffix}")
	public String getTemplateVarText(Model model,HttpServletRequest request,
			@RequestParam(value = "varName", required = false, defaultValue="") String varName
			) throws IOException{
		String text = null;
		varName = filter(varName);
		
		if(varName.length() == 0){
			//新增
			text = "";
		}else{
			TemplateVarVO tvvo = templateService.getTemplateVarByCache(varName);
			if(tvvo.getResult() - TemplateVarVO.FAILURE == 0){
				text = tvvo.getInfo();
			}else{
				text = tvvo.getTemplateVarData().getText();
			}
		}
		
		model.addAttribute("text", text);
		return "template/getTemplateVarText";
	}
	
	
	/**
	 * 获取指定模版页面的内容，用于iframe内，htmledit修改
	 * @param pageName 要获取的模版页面名字，对应 {@link TemplatePage}.name
	 * @throws IOException
	 */
	@RequestMapping("getTemplatePageText${url.suffix}")
	public String getTemplatePageText(Model model,HttpServletRequest request,
			@RequestParam(value = "pageName", required = true) String pageName
			) throws IOException{
		Site site = getSite();
		String html = null;
		pageName = filter(pageName);
		
		TemplatePageVO vo = templateService.getTemplatePageByNameForCache(request, pageName);
		if(vo.getResult() - TemplatePageVO.FAILURE == 0){
			return error(model, vo.getInfo());
		}
		
		//判断一下，是否这个模版页面是刚建立的，还没有模版页内容
		if(vo.getTemplatePageData() == null){
			//没有模版页的内容。
//			if(vo.getTemplatePage().getEditMode() != null || vo.getTemplatePage().getEditMode() - TemplatePage.EDIT_MODE_VISUAL == 0){
				//判断一下，如果是可视化模式，需要增加默认的html模版页面
				vo.setTemplatePageData(new TemplatePageData());
				vo.getTemplatePageData().setText(Template.newHtml);
//			}else{
//				//如果是代码模式，那么就是空字符串就行了
//				vo.setTemplatePageData(new TemplatePageData());
//				vo.getTemplatePageData().setText("");
//			}
		}
		
		//判断是代码模式，还是智能模式
		if(vo.getTemplatePage().getEditMode() != null && vo.getTemplatePage().getEditMode() - TemplatePage.EDIT_MODE_CODE == 0){
			//代码模式，那么直接赋予 templatePageData.text 即可
			html = vo.getTemplatePageData().getText();
			AliyunLog.addActionLog(vo.getTemplatePageData().getId(), "代码编辑获取指定模版页内容", pageName);
		}else{
			//如果是智能模式，那么要装载模版变量、可视化编辑等
			//装载模版变量
			if(Func.getUserBeanForShiroSession().getTemplateVarMapForOriginal() == null){
				//判断一下，缓存中是否有模版变量，若所没有，那么要缓存
				templateService.getTemplateVarAndDateListByCache();
			}
			
			com.xnx3.wangmarket.admin.cache.TemplateCMS temp = new com.xnx3.wangmarket.admin.cache.TemplateCMS(site, true);
			html = temp.assemblyTemplateVar(vo.getTemplatePageData().getText());
			
			// {templatePath} 替换
			TemplateCMS templateCMS = new TemplateCMS(site, TemplateUtil.getTemplateByName(site.getTemplateName()));
			html = html.replaceAll(TemplateCMS.regex("templatePath"), templateCMS.getTemplatePath());
			
			//自动在</head>之前，加入htmledit.js
			String yuming = "//"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
			html = html.replace("</head>", "<!--XNX3HTMLEDIT--><script>var masterSiteUrl='"+Global.get("MASTER_SITE_URL")+"'; var htmledit_upload_url='"+yuming+"template/uploadImage.do?t="+DateUtil.timeForUnix13()+"'; </script><script src=\"//res.weiunity.com/htmledit/htmledit.js\"></script></head>");
			
			AliyunLog.addActionLog(vo.getTemplatePageData().getId(), "可视化编辑获取指定模版页内容", pageName);
		}
		
		model.addAttribute("pageName", pageName);
		model.addAttribute("html", html);
		return "template/getTemplatePageText";
	}
	
	/**
	 * 保存模版页面的模版内容
	 * @param pageName 要获取的源代码的页面名字，如传入index，则会自动获取index.html的源代码返回
	 * @param html 修改后的网站首页的html源代码 
	 */
	@RequestMapping(value="saveTemplatePageText${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO saveTemplatePageText(HttpServletRequest request,
			@RequestParam(value = "pageName", required = false, defaultValue="") String pageName,
			@RequestParam(value = "html", required = true) String html){
		pageName = filter(pageName);
		TemplatePageVO vo = templateService.saveTemplatePageText(pageName, html, request);
		
		AliyunLog.addActionLog(0, "获取指定模版页"+(vo.getResult() - BaseVO.SUCCESS == 0 ? "成功":"失败")+"，模版页："+pageName);
		
		BaseVO baseVO = new BaseVO();
		baseVO.setBaseVO(vo.getResult(), vo.getInfo());
		return baseVO;
	}
	
	/**
	 * 获取当前网站的模版变量，返回列表为： {include=...} 备注  的形式，供制作模版页时随时复制
	 */
	@RequestMapping("templateVarListForUsed${url.suffix}")
	public String templateVarListForUsed(HttpServletRequest request,Model model){
		Site site = getSite();
		Sql sql = new Sql(request);
	    sql.setSearchTable("template_var");
	    //增加添加搜索字段。这里的搜索字段跟log表的字段名对应
	    sql.setSearchColumn(new String[]{"name"});
	    sql.appendWhere("siteid = "+site.getId() + " AND template_name = '"+ getSite().getTemplateName() +"'");
	    //查询log数据表的记录总条数
	    int count = sqlService.count("template_var", sql.getWhere());
	    //每页显示100条
	    Page page = new Page(count, 100, request);
	    //创建查询语句，只有SELECT、FROM，原生sql查询。其他的where、limit等会自动拼接
	    sql.setSelectFromAndPage("SELECT * FROM template_var", page);
	    
	    //当用户没有选择排序方式时，系统默认排序。
	    sql.setDefaultOrderBy("id DESC");
	    List<TemplateVar> list = sqlService.findBySql(sql, TemplateVar.class);
	    
	    AliyunLog.addActionLog(getSiteId(), "获取当前网站的模版变量列表，供编辑模版页使用");
	    
	    model.addAttribute("list", list);
	    return "template/templateVarListForUsed";
	}
	
	/**
	 * 删除模版变量
	 * @param id 要删除的模版变量的id
	 */
	@RequestMapping(value="deleteTemplateVar${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO deleteTemplateVar(@RequestParam(value = "id", required = true) int id){
		TemplateVar templateVar = (TemplateVar) sqlService.findById(TemplateVar.class , id);
		if(templateVar == null){
			return error("要删除的模版变量不存在！");
		}
		Site site = getSite();
		if(templateVar.getSiteid() - site.getId() != 0){
			return error("模版变量不属于你，无法删除");
		}
		
		sqlService.delete(templateVar);	//删除数据库的
		templateService.deleteTemplateVarForCache(templateVar.getId());	//删除缓存中的
		//删除 template_var_data 中的数据。 v4.5 更新，盖亚科技-罗浪提醒。
		sqlService.executeSql("DELETE FROM template_var_data WHERE id="+id);	
		
		AliyunLog.addActionLog(templateVar.getId(), "删除模版变量："+templateVar.getTemplateName());
		
		return success();
	}
	

	/**
	 * 删除模版页
	 * @param id 要删除的模版页的id
	 */
	@RequestMapping(value="deleteTemplatePage${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO deleteTemplatePage(HttpServletRequest request,
			@RequestParam(value = "id", required = true) int id){
		TemplatePage templatePage = sqlService.findById(TemplatePage.class , id);
		if(templatePage == null){
			return error("要删除的模版页不存在！");
		}
		Site site = getSite();
		if(templatePage.getSiteid() - site.getId() != 0){
			return error("模版页不属于你，无法删除");
		}
		
		sqlService.delete(templatePage);
		//删除模版页面的具体内容数据。v4.5版本增加
		sqlService.executeSql("DELETE FROM template_page_data WHERE id = "+templatePage.getId());
		
		//Session缓存中，要将其删除
		templateService.deleteTemplatePageForCache(templatePage.getId(), request);
		
		AliyunLog.addActionLog(templatePage.getId(), "删除模版页："+templatePage.getName());
		
		return success();
	}
	
	
	/**
	 * 导出当前网站当前的模版文件，包含模版页面，模版变量、栏目
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("exportTemplate${url.suffix}")
	public void exportTemplate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, UnsupportedEncodingException {
		BaseVO vo = templateService.exportTemplate(request);
		String fileName = "template"+DateUtil.currentDate("yyyyMMdd_HHmm")+".wscso".toString(); // 文件的默认保存名
		//读到流中
		InputStream inStream = new ByteArrayInputStream(vo.getInfo().getBytes("UTF-8"));  
		
		AliyunLog.addActionLog(getSiteId(), "导出当前网站当前的模版文件");
		
		// 设置输出的格式
		response.reset();
		response.setContentType("bin");
		response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	        // 循环取出流中的数据
		byte[] b = new byte[1000];
		int len;
		try {
		    while ((len = inStream.read(b)) > 0)
		        response.getOutputStream().write(b, 0, len);
		    inStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	

	/**
	 * 用户自行上传一个模版文件，将当前网站应用此模版。
	 * <br/>模版文件包含模版页面，模版变量、栏目
	 * @param templateFile input上传的模版文件的name
	 */
	@RequestMapping(value="uploadImportTemplate${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public void uploadImportTemplate(HttpServletResponse response, HttpServletRequest request, 
			@RequestParam("templateFile") MultipartFile multipartFile){
		if(multipartFile == null){
			responseJson(response, BaseVO.FAILURE, "请选择要导入的模版");
			return;
		}
		
		String wscsoTemplateText = null;	//wscso后缀的模版文件
		
		//判断导入的模版文件格式，是wscso还是zip格式
		
		//判断一下上传文件大小
		int lengthKB = 0;
		try {
			lengthKB = (int) Math.ceil(multipartFile.getInputStream().available()/1024);
		} catch (IOException e1) {
			e1.printStackTrace();
			responseJson(response, BaseVO.FAILURE, "未获取到所导入的文件大小");
			return;
		}
		
		//获取上传的文件的后缀
		String fileSuffix = Lang.findFileSuffix(multipartFile.getOriginalFilename()).toLowerCase();
		if(fileSuffix.equals("wscso")){
			//wscso的限制在配置文件applilcation.properties的大小以内
			if(lengthKB > AttachmentFile.getMaxFileSizeKB()){
				//超过
				responseJson(response, BaseVO.FAILURE, "纯模版文件最大限制"+AttachmentFile.getMaxFileSizeKB()+"KB以内");
				return;
			}
			try {
				wscsoTemplateText = StringUtil.inputStreamToString(multipartFile.getInputStream(), "UTF-8");
			} catch (IOException e) {
				Log.error("获取到的，导入模版没有内容");
				e.printStackTrace();
				responseJson(response, BaseVO.FAILURE, "所获取到所导入的模版未发现模版内容");
				return;
			}
		}else if (fileSuffix.equals("zip")) {
			//v4.9增加，禁止zip模版通过此处上传。统一通过总管理后台-功能插件-模版库进行导入
			if(true){
				responseJson(response, BaseVO.FAILURE, "已禁止上传zip格式模版！您可通过总管理后台-功能插件-模版库进行导入");
				return;
			}
			
			//上传的是模版文件，包含素材，将其上传到服务器本地 ， v4.7 增加
			//zip的限制在50MB以内
			if(lengthKB > 50*1025){
				//超过50MB
				responseJson(response, BaseVO.FAILURE, "最大限制50MB以内");
				return;
			}
			
			String fileName = DateUtil.timeForUnix13()+"_"+StringUtil.getRandom09AZ(20);
			File file = new File(TemplateTemporaryFolder.folderPath+fileName+".zip");
			try {
				multipartFile.transferTo(file);
			} catch (IllegalStateException | IOException e1) {
				e1.printStackTrace();
				responseJson(response, BaseVO.FAILURE, e1.getMessage());
				return;
			}
			
			//将其解压到同文件夹中
			try {
				ZipUtil.unzip(TemplateTemporaryFolder.folderPath+fileName+".zip",TemplateTemporaryFolder.folderPath+fileName+"/");
			} catch (IOException e) {
				e.printStackTrace();
			}
			//既然已经解压出来了，那么删除掉zip文件
			file.delete();
			//判断一下解压出来的文件中，是否存在 template.wscso 这个模版文件
			File wscsoFile = new File(TemplateTemporaryFolder.folderPath+fileName+"/template.wscso");
			if(wscsoFile.exists()){
				wscsoTemplateText = FileUtil.read(wscsoFile, FileUtil.UTF8);
			}else{
				//不存在，那就报错吧
				responseJson(response, BaseVO.FAILURE, "template.wscso模版文件未发现！");
				return;
			}
				
			TemplateVO tvo = new TemplateVO();
			//导入JSON，生成对象
			tvo.importText(FileUtil.read(TemplateTemporaryFolder.folderPath+fileName+"/template.wscso", FileUtil.UTF8));
			//v4.7版本以后，导出的都会有 tvo.template 对象
			String templateName = tvo.getTemplate().getName();
			//判断数据库中，是否已经有这个模版了
			com.xnx3.wangmarket.admin.entity.Template template = sqlService.findAloneByProperty(com.xnx3.wangmarket.admin.entity.Template.class, "name", templateName);
			if(template == null){
				//为空，没有这个模版，这个是正常的，可以将模版资源文件导入
				//将zip解压出来的文件，进行过滤，过滤掉不合法的后缀文件，将合法的文件后缀转移到新建立的模版文件夹中去
				new TemplateUtil(templateName, new TemplateUtilFileMove() {
					public void move(String path, InputStream inputStream) {
						AttachmentFile.put(path, inputStream);
					}
				}).filterTemplateFile(new File(TemplateTemporaryFolder.folderPath+fileName+"/"));
				//将这个模版模版信息记录入数据库
				template = tvo.getTemplate();
				template.setIscommon(com.xnx3.wangmarket.admin.entity.Template.ISCOMMON_NO); 	//用户自己导入的，默认是私有的，不加入公共模版库。除非通过模版中心来指定（模版中心属于授权版本）
				template.setUserid(getUserId());
				sqlService.save(template);
				
			}else{
				//不为空，已经有这个模版了，那么就不可以导入资源文件，只导入 wscso 文件就可以了
				System.out.println("已经有这个模版的资源文件了，忽略:"+template.getName());
			}
		}
		
		//将 wscso 模版文件导入
		BaseVO vo = templateService.importTemplate(wscsoTemplateText, true, request);
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			AliyunLog.addActionLog(getSiteId(), "本地导入模版文件成功！");
		}else{
			AliyunLog.addActionLog(getSiteId(), "本地导入模版文件失败！");
		}
		
		responseJson(response, vo.getResult(), vo.getInfo());
	}
	
	/**
	 * 通过res.weiunity.com的CDN获取制定的模版，远程获取模版文件，将当前网站应用此模版。
	 * <br/>模版文件包含模版页面，模版变量、栏目
	 */
	@RequestMapping(value="remoteImportTemplate${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO remoteImportTemplate(HttpServletRequest request, Model model,
			@RequestParam(value = "templateName", required = false , defaultValue="") String templateName){
		templateName = filter(templateName);
		if(templateName.length() == 0){
			return error("请选择模版");
		}
		
		com.xnx3.wangmarket.admin.entity.Template template = TemplateUtil.getTemplateByName(templateName);
		if(template == null){
			return error("模版不存在");
		}
		
		BaseVO wscvo = TemplateUtil.getTemplateWscso(template);
		if(wscvo.getResult() - BaseVO.FAILURE == 0){
			return wscvo;
		}
		
		BaseVO vo = templateService.importTemplate(wscvo.getInfo(), true, request);
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			AliyunLog.addActionLog(getSiteId(), "模版导入成功！");
		}
		return vo;
	}
	
	/**
	 * 选择模版，选择模版导入，CMS模式下的选择模版，纯粹展示页面
	 */
	@RequestMapping("/selectTemplate${url.suffix}")
	public String selectTemplate(HttpServletRequest request,Model model){
		TemplatePageVO vo = templateService.getTemplatePageIndexByCache(request);
		
		AliyunLog.addActionLog(getSiteId(), "打开CMS模式下的模版选择界面");
		if(vo.getResult() - TemplatePageVO.SUCCESS == 0){
			//如果有模版了，会进入模板还原页面
			return redirect("template/restoreTemplate.do");
		}else{
			//若没有模版，才会出现选择模版的界面

			//最大上传大小，单位 KB
			model.addAttribute("maxFileSizeKB", AttachmentFile.getMaxFileSizeKB());
			model.addAttribute("AttachmentFileUrl", AttachmentFile.netUrl());
			return "template/selectTemplate";
		}
	}
	

	/**
	 * 上传图片接口
	 */
	@RequestMapping(value="uploadImage${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public UploadFileVO uploadImage(Model model,HttpServletRequest request){
		UploadFileVO uploadFileVO = new UploadFileVO();
		if(getSite() == null){
			uploadFileVO.setBaseVO(UploadFileVO.FAILURE, "请先登录");
			return uploadFileVO;
		}
		
		uploadFileVO = AttachmentFile.uploadImage("site/"+getSiteId()+"/templateimage/", request, "image", 0);
		if(uploadFileVO.getResult() == UploadFileVO.SUCCESS){
			//上传成功，写日志
			AliyunLog.addActionLog(getSiteId(), "CMS模式下，模版页自由上传图片成功："+uploadFileVO.getFileName());
		}
		
		return uploadFileVO;
	}
	

	/**
	 * 自定义模版的整站刷新
	 */
	@RequestMapping(value="refreshForTemplate${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO refreshForTemplate(Model model,HttpServletRequest request){
		AliyunLog.addActionLog(getSiteId(), "CMS模式下，刷新生成整站");
		
		//提前判断一下，是否使用的是阿里云OSS存储，并且没有配置OSS
		if(AttachmentFile.isMode(AttachmentFile.MODE_ALIYUN_OSS) && OSSUtil.getOSSClient() == null){
			return error("请先访问 /install/index.do 进行安装，此依赖OSS使用");
		}
		
		return siteService.refreshForTemplate(request);
	}
	
	/**
	 * 已经有过模板了，还原模板,展示，选择要还原的模板的项。
	 * 此主要起展示作用，并没有实际功能
	 */
	@RequestMapping("restoreTemplate${url.suffix}")
	public String restoreTemplate(Model model,HttpServletRequest request){
		Site site = getSite();
		AliyunLog.addActionLog(site.getId(), "打开还原模板选择页面");
		
		//判断当前用户的模板是使用的云端的，还是本地导入的
		boolean usedYunTemplate = false;	//若是云端模板，则为true
		
		if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
			//取出模版库（云端+本地）的模版
			com.xnx3.wangmarket.admin.entity.Template template = TemplateUtil.getTemplateByName(site.getTemplateName());
			if(template != null && template.getWscsoDownUrl() != null && template.getWscsoDownUrl().indexOf("//") > -1){
				usedYunTemplate = true;
				model.addAttribute("template", template);
			}
		}
		
		model.addAttribute("usedYunTemplate", usedYunTemplate);
		model.addAttribute("site", site);
		return "template/restoreTemplate";
	}
	

	/**
	 * 已经有过模板了，还原模板,展示，选择要还原的模板的项。
	 * 此主要起展示作用，并没有实际功能
	 * @throws IOException 
	 */
	@RequestMapping(value="restoreTemplateByLocalhostFile${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public void restoreTemplateByLocalhostFile(Model model,HttpServletRequest request,HttpServletResponse response,
			@RequestParam("templateFile") MultipartFile multipartFile) throws IOException{
		JSONObject json = new JSONObject();
		
		//还原的模版字符串内容
		String backupsTemplateText = StringUtil.inputStreamToString(multipartFile.getInputStream(), "UTF-8");
		
		TemplateCompareVO tcv = new TemplateCompareVO();
		
		TemplateVO templateVO = new TemplateVO();
		if(!templateVO.importText(backupsTemplateText)){
			json.put("result", BaseVO.FAILURE);
			json.put("info", "导入失败！模版解析出错");
		}else{
			tcv = restoreTemplateCompare(request, templateVO);
			if(tcv.getResult() - TemplateCompareVO.FAILURE == 0){
				json.put("result", BaseVO.FAILURE);
				json.put("info", tcv.getInfo());
			}else{
				AliyunLog.addActionLog(getSiteId(), "本地还原模版进行比对预览");
				
				//将比对好的存入Session，方便在其他页面直接显示
				request.getSession().setAttribute("comparePreviewTCV", tcv);
				request.getSession().setAttribute("comparePreviewTemplateVO", templateVO);
				
				json.put("result", BaseVO.SUCCESS);
				json.put("info", "成功");
			}
		}
		
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try { 
	        out = response.getWriter();  
	        out.append(json.toString());
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }
	    }  
	}
	

	/**
	 * 已经有过模板了，还原模板。这里通过远程，云端进行还原模板
	 * <br/>模版文件包含模版页面，模版变量、栏目、输入模型
	 */
	@RequestMapping(value="restoreTemplateByRemote${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO restoreTemplateByRemote(HttpServletRequest request, Model model){
		TemplateCompareVO tcv = new TemplateCompareVO();
		Site site = getSite();
		
		if(site.getTemplateName() == null || site.getTemplateName().length() == 0){
			return error("当前网站未使用云端模板！云端还原失败");
		}
		
		com.xnx3.wangmarket.admin.entity.Template template = TemplateUtil.getTemplateByName(site.getTemplateName());
		if(template == null){
			//并非云端模版
			return error("模版不存在");
		}
		BaseVO tvo = TemplateUtil.getTemplateWscso(template);
		if(tvo.getResult() - BaseVO.FAILURE == 0){
			//出错，直接返回
			return tvo;
		}
		
		TemplateVO templateVO = new TemplateVO();
		templateVO.importText(tvo.getInfo());
		tcv = restoreTemplateCompare(request, templateVO);
		if(tcv.getResult() - TemplateCompareVO.FAILURE == 0){
			return error(tcv.getInfo());
		}
		
		AliyunLog.addActionLog(getSiteId(), "云端还原模版进行比对预览");
		
		//将比对好的存入Session，方便在其他页面直接显示
		request.getSession().setAttribute("comparePreviewTCV", tcv);
		request.getSession().setAttribute("comparePreviewTemplateVO", templateVO);
		
		return success();
	}
	
	/**
	 * 远程获取模版，或者本地导入的还原的模版，进行还原比对，比对成功后，依赖此处从Session中取得比对的结果，显示给用户看，方便用户选择要还原哪个项
	 */
	@RequestMapping("restoreTemplateComparePreview${url.suffix}")
	public String restoreTemplateComparePreview(HttpServletRequest request, Model model){
		model.addAttribute("tcv", request.getSession().getAttribute("comparePreviewTCV"));
		model.addAttribute("templateVO", request.getSession().getAttribute("comparePreviewTemplateVO"));
		
		return "template/restoreTemplateComparePreview";
	}
	
	/**
	 * 还原模板，将当前网站的模板于导入的网站模板进行比较，判断其是否有改动
	 * @param tvo 模板内容，要还原的模板的内容
	 */
	private TemplateCompareVO restoreTemplateCompare(HttpServletRequest request, TemplateVO tvo){
		TemplateCompareVO templateCompareVO = new TemplateCompareVO();
		
		//将用户导入的模板存入Session，以便用户选中要还原的内容后，准备还原时直接取
		request.getSession().setAttribute("tvo", tvo);
		
		Site site = Func.getCurrentSite();
		
		//首先，判断当前模板是否可以还原到当前网站中
		if(tvo.getPlugin() != null && tvo.getPlugin().length() > 1){
			//插件模式，不用比对是否模版一致
		}else{
			//模版模式，要比对模版，必须模版一致才可以
			if(!StringUtil.StringEqual(site.getTemplateName(), tvo.getTemplateName())){
				templateCompareVO.setBaseVO(TemplateCompareVO.FAILURE, "当前网站使用的模板与导入的模板不属于同一个模板，无法还原！");
				return templateCompareVO;
			}
		}
		
		/*
		 * 加载网站的模板页面，模板变量，输入模型，栏目，拿到当前网站的基本信息
		 */
		//当前网站的模板页面列表
		TemplatePageListVO templatePageListVO = templateService.getTemplatePageAndDateListByCache(request);
		//将之转化为Map，方便取 (key:模板页面名字，name)
		Map<String, TemplatePageVO> templatePageMap = new HashMap<String, TemplatePageVO>();
		for (int i = 0; i < templatePageListVO.getList().size(); i++) {
			TemplatePageVO tpvo = templatePageListVO.getList().get(i);
			templatePageMap.put(tpvo.getTemplatePage().getName(), tpvo);
		}
		
		//当前网站的模板变量列表
		TemplateVarListVO templateVarListVO = templateService.getTemplateVarListByCache();
		//将之转化为Map，方便取（Key：模板变量代码varname）
		Map<String, TemplateVarVO> templateVarMap = new HashMap<String, TemplateVarVO>();
		for (int i = 0; i < templateVarListVO.getList().size(); i++) {
			TemplateVarVO tvvo = templateVarListVO.getList().get(i);
			templateVarMap.put(tvvo.getTemplateVar().getVarName(), tvvo);
		}
		
		//当前网站的输入模型列表
		List<InputModel> inputModelList = inputModelService.getInputModelListForSession();
		//将之转化为map,方便取（key：codeName）
		Map<String, InputModel> inputModelMap = new HashMap<String, InputModel>();
		for (int i = 0; i < inputModelList.size(); i++) {
			InputModel im = inputModelList.get(i);
			inputModelMap.put(im.getCodeName(), im);
		}
		
		//当前网站的栏目列表
		Map<Integer, SiteColumn> columnCacheMap = siteColumnService.getSiteColumnMapByCache();
		//将之转化为map，方便取（key：栏目代码codeName）
		Map<String, SiteColumn> columnMap = new HashMap<String, SiteColumn>();
		for (Map.Entry<Integer, SiteColumn> entry : columnCacheMap.entrySet()) {
			SiteColumn sc = entry.getValue();
			columnMap.put(sc.getCodeName(), sc);
		}
		
		
		//进行模板页面的比对
		for (int i = 0; i < tvo.getTemplatePageList().size(); i++) {
			com.xnx3.wangmarket.admin.vo.bean.template.TemplatePage templatePageBean = tvo.getTemplatePageList().get(i);
			TemplatePage tp = templatePageBean.getTemplatePage();
			
			//对比结果做记录
			TemplatePageCompare tpCompare = new TemplatePageCompare();
			tpCompare.setBackupsTemplatePage(tp);
			
			//取出当前网站使用的此模板页面
			TemplatePageVO tpVOOld = templatePageMap.get(tp.getName());
			if(tpVOOld == null){
				tpCompare.setResult(2);
			}else{
				//存在模板页面
				tpCompare.setCurrentTemplatePage(tpVOOld.getTemplatePage());
				
				//进行每项对比，看是否有改动
				if(tp.getType() - tpVOOld.getTemplatePage().getType() != 0){
					tpCompare.setResult(1);
					tpCompare.getUpdateListInfo().add("type");
				}
				if(tpVOOld.getTemplatePageData() != null && StringUtil.StringEqual(templatePageBean.getText(), tpVOOld.getTemplatePageData().getText(), true)){
					//相等
				}else{
					tpCompare.setResult(1);
					tpCompare.getUpdateListInfo().add("text");
					tpCompare.setCurrentTemplatePageDataText(tpVOOld.getTemplatePageData().getText());
					tpCompare.setBackupsTemplatePageDataText(templatePageBean.getText());
				}
			}
			
			templateCompareVO.getTemplatePageList().add(tpCompare);
		}
		
		//进行模板变量对比
		for (int i = 0; i < tvo.getTemplateVarList().size(); i++) {
			com.xnx3.wangmarket.admin.vo.bean.template.TemplateVar templateVarBean = tvo.getTemplateVarList().get(i);
			TemplateVar tv = templateVarBean.getTemplateVar();
			
			//对比结果做记录
			TemplateVarCompare tvCompare = new TemplateVarCompare();
			tvCompare.setBackupsTemplateVar(tv);
			
			//取出当前网站使用的此模板页面
			TemplateVarVO tvVOOld = templateVarMap.get(tv.getVarName());
			if(tvVOOld == null){
				tvCompare.setResult(2);
			}else{
				//存在
				tvCompare.setCurrentTemplateVar(tvVOOld.getTemplateVar());
				//进行每项对比，看是否有改动
				if(tvVOOld.getTemplateVarData()!= null && StringUtil.StringEqual(templateVarBean.getText(), tvVOOld.getTemplateVarData().getText(), true)){
					//相等
				}else{
					tvCompare.setResult(1);
					tvCompare.getUpdateListInfo().add("text");
					tvCompare.setCurrentTemplateVarDataText(tvVOOld.getTemplateVarData().getText());
					tvCompare.setBackupsTemplateVarDataText(templateVarBean.getText());
				}
			}
			
			templateCompareVO.getTemplateVarList().add(tvCompare);
		}
		
		//进行栏目对比
		for (int i = 0; i < tvo.getSiteColumnList().size(); i++) {
			SiteColumn sc = tvo.getSiteColumnList().get(i);
			
			//对比结果做记录
			SiteColumnCompare scCompare = new SiteColumnCompare();
			scCompare.setBackupsSiteColumn(sc);
			
			//取出网站当前使用的此栏目信息
			SiteColumn scOld = columnMap.get(sc.getCodeName());
			if(scOld == null){
				scCompare.setResult(2);
			}else{
				//存在
				scCompare.setCurrentSiteColumn(scOld);
				//进行每项对比，看每项(基础数据)是否有改动
				if(!StringUtil.StringEqual(sc.getIcon(), scOld.getIcon())){
					scCompare.setResult(1);
					scCompare.getUpdateListInfo().add("icon");
				}
				if(!StringUtil.StringEqual(sc.getInputModelCodeName(), scOld.getInputModelCodeName(), true, true)){
					scCompare.setResult(1);
					scCompare.getUpdateListInfo().add("inputModelCodeName");
				}
				if(!StringUtil.StringEqual(sc.getParentCodeName(), scOld.getParentCodeName())){
					scCompare.setResult(1);
					scCompare.getUpdateListInfo().add("parentCodeName");
				}
				
				//如果其中有一个是null，另一个是0，那么认为这两个是一样的，无需还原
				if((sc.getListNum() == null && scOld.getListNum() != null && scOld.getListNum() == 0 ) || (scOld.getListNum() == null && sc.getListNum() != null && sc.getListNum() == 0 )){
					//无需还原
				}else if(!IntegerEqual(sc.getListNum(), scOld.getListNum())){
					scCompare.setResult(1);
					scCompare.getUpdateListInfo().add("listNum");
				}
				
				//v2.24增加,编辑模式
				if((sc.getEditMode() == null && scOld.getEditMode() != null && scOld.getEditMode() == 0) || scOld.getEditMode() == null && sc.getEditMode() != null && sc.getEditMode() == 0){
					//0 == null ，无需还原
				}else if(!ShortEqual(sc.getEditMode(), scOld.getEditMode())){
					scCompare.setResult(1);
					scCompare.getUpdateListInfo().add("editMode");
				}
				
				if(scOld.getType() == null || sc.getType() - scOld.getType() != 0){
					scCompare.setResult(1);
					scCompare.getUpdateListInfo().add("type");
				}
				if(scOld.getUsed() == null || sc.getUsed() - scOld.getUsed() != 0){
					scCompare.setResult(1);
					scCompare.getUpdateListInfo().add("used");
				}
				//判断其选择的模板是否有改动
				if(!StringUtil.StringEqual(sc.getTemplatePageListName(), scOld.getTemplatePageListName(), true)){
					scCompare.setResult(1);
					scCompare.getUpdateListInfo().add("templatePageListName");
				}
				if(!StringUtil.StringEqual(sc.getTemplatePageViewName(), scOld.getTemplatePageViewName(), true)){
					scCompare.setResult(1);
					scCompare.getUpdateListInfo().add("templatePageViewName");
				}
				
			}
			
			templateCompareVO.getSiteColumnList().add(scCompare);
		}
		
		//进行输入模型对比
		for (int i = 0; i < tvo.getInputModelList().size(); i++) {
			InputModel im = tvo.getInputModelList().get(i);
			
			//对比结果做记录
			InputModelCompare imCompare = new InputModelCompare();
			imCompare.setBackupsInputModel(im);
			
			//取出网站当前使用的此输入模型的信息
			InputModel imOld = inputModelMap.get(im.getCodeName());
			if(imOld == null){
				imCompare.setResult(2);
			}else{
				//存在
				imCompare.setCurrentInputModel(imOld);
				//对比
				if(!StringUtil.StringEqual(im.getText(), imOld.getText(), true, true)){
					imCompare.setResult(1);
					imCompare.getUpdateListInfo().add("text");
				}
			}
			
			templateCompareVO.getInputModelList().add(imCompare);
		}
		
		//设置还原的模板
		templateCompareVO.setBuckupsTemplateVO(tvo);
		
		return templateCompareVO;
	}
	
	/**
	 * 还原模板，表单提交，执行还原操作
	 */
	@RequestMapping(value="restoreTemplateSubmit${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO restoreTemplateSubmit(Model model,HttpServletRequest request){
		AliyunLog.addActionLog(getSiteId(), "还原模板，执行还原操作");
		
		Site site = getSite();
		
		//获取要还原的备份模板数据，从Session中获取
		TemplateVO tvo = (TemplateVO) request.getSession().getAttribute("tvo");
		if(tvo == null){
			return error("出错！要还原的模板数据不存在，可以将您的操作进行问题反馈");
		}
		
		//获取便利选中的项
		Map<String,String[]> map = (Map<String,String[]>)request.getParameterMap();  
		List<String> paramList = new ArrayList<String>();
        for(String name:map.keySet()){  
           paramList.add(name);
        }
		
		//获取用户选择好的要还原的模板的每项
		RestoreTemplateSubmitCheckDataVO data = new RestoreTemplateSubmitCheckDataVO(paramList);
	
		
		//模板页面
		if(data.getTemplatePageNameList().size() > 0){
			Map<String, TemplatePage> templatePageMap = new HashMap<String, TemplatePage>();	//当前网站的模板页面， key：name
			Map<Integer, TemplatePageData> templatePageDataMap = new HashMap<Integer, TemplatePageData>();	//当前网站的模板页面， key：templatePage.id
			Map<String, com.xnx3.wangmarket.admin.vo.bean.template.TemplatePage> backupsTemplatePageMap = new HashMap<String, com.xnx3.wangmarket.admin.vo.bean.template.TemplatePage>();	//要还原的备份的模板内容，之后用到时直接从map取   key:name
			
			//若是制定了还原模板页面，则先从数据库中将其获取到最新信息
			//模版名字检索，是否是使用的导入的模版，若是使用的导入的模版，则只列出导入的模版页面
		    String templateNameWhere = "";
		    if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
		    	templateNameWhere = " AND template_page.template_name = '"+ site.getTemplateName() +"'";
		    }
		    List<TemplatePage> templatePageList = sqlService.findBySqlQuery("SELECT * FROM template_page WHERE siteid = " + site.getId() + templateNameWhere, TemplatePage.class);
		    List<TemplatePageData> templatePageDataList = sqlService.findBySqlQuery("SELECT template_page_data.* FROM template_page_data,template_page WHERE template_page.id = template_page_data.id AND siteid = " + site.getId() + templateNameWhere, TemplatePageData.class);
			
		    //将用户当前网站的模板页面，list转化为map，方便取
			for (int i = 0; i < templatePageList.size(); i++) {
				TemplatePage tp = templatePageList.get(i);
				templatePageMap.put(tp.getName(), tp);
			}
			for (int i = 0; i < templatePageDataList.size(); i++) {
				TemplatePageData tpd = templatePageDataList.get(i);
				templatePageDataMap.put(tpd.getId(), tpd);
			}
			
			//将要还原的备份信息，list转化为map，方便取
			for (int i = 0; i < tvo.getTemplatePageList().size(); i++) {
				com.xnx3.wangmarket.admin.vo.bean.template.TemplatePage tpBean = tvo.getTemplatePageList().get(i);
				backupsTemplatePageMap.put(tpBean.getTemplatePage().getName(), tpBean);
			}
			
			//进行备份的还原操作
			for (int i = 0; i < data.getTemplatePageNameList().size(); i++) {
				String templatePageName = data.getTemplatePageNameList().get(i);
				
				TemplatePage backupsTp = backupsTemplatePageMap.get(templatePageName).getTemplatePage();	//导入的备份模板的
				TemplatePage tp = templatePageMap.get(templatePageName);	//网站本身的
				if(tp == null){
					//如果是现在网站中删除了，那么在创建一个
					tp = new TemplatePage();
					tp.setName(backupsTp.getName());
					tp.setSiteid(site.getId());
					tp.setTemplateName(site.getTemplateName());
					tp.setUserid(site.getUserid());
				}
				tp.setRemark(filter(backupsTp.getRemark()));
				tp.setType(backupsTp.getType());
				sqlService.save(tp);
				if(tp.getId() != null && tp.getId() > 0){
					//模板页面属性保存成功，再保存内容数据
					TemplatePageData tpd = templatePageDataMap.get(tp.getId());
					if(tpd == null){
						tpd = new TemplatePageData();
						tpd.setId(tp.getId());
					}
					tpd.setText(backupsTemplatePageMap.get(templatePageName).getText());
					sqlService.save(tpd);
					
					//刷新当前模板页面的Session缓存数据
					templateService.updateTemplatePageForCache(tp, tpd, request);
				}
			}
		}
		
		
		//模板变量
		if(data.getTemplateVarNameList().size() > 0){
			Map<String, TemplateVar> templateVarMap = new HashMap<String, TemplateVar>();	//当前网站的模板变量， key：varName
			Map<Integer, TemplateVarData> templateVarDataMap = new HashMap<Integer, TemplateVarData>();	//当前网站的模板变量， key：templateVar.id
			Map<String, com.xnx3.wangmarket.admin.vo.bean.template.TemplateVar> backupsTemplateVarMap = new HashMap<String, com.xnx3.wangmarket.admin.vo.bean.template.TemplateVar>();	//要还原的备份的模板变量内容，之后用到时直接从map取   key:varName
			
			//若是制定了还原模板变量，则先从数据库中将其获取到最新信息
			//模版变量的变量名字检索，是否是使用的导入的模版，若是使用的导入的模版，则只列出导入的模版变量
		    String templateNameWhere = "";
		    if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
		    	templateNameWhere = " AND template_var.template_name = '"+ site.getTemplateName() +"'";
		    }
		    List<TemplateVar> templateVarList = sqlService.findBySqlQuery("SELECT * FROM template_var WHERE siteid = " + site.getId() + templateNameWhere, TemplateVar.class);
		    List<TemplateVarData> templateVarDataList = sqlService.findBySqlQuery("SELECT template_var_data.* FROM template_var_data,template_var WHERE template_var.id = template_var_data.id AND siteid = " + site.getId() + templateNameWhere, TemplateVarData.class);
			
		    //将用户当前网站的模板变量，list转化为map，方便取
			for (int i = 0; i < templateVarList.size(); i++) {
				TemplateVar tv = templateVarList.get(i);
				templateVarMap.put(tv.getVarName(), tv);
			}
			for (int i = 0; i < templateVarDataList.size(); i++) {
				TemplateVarData tvd = templateVarDataList.get(i);
				templateVarDataMap.put(tvd.getId(), tvd);
			}
			
			//将要还原的备份信息，list转化为map，方便取
			for (int i = 0; i < tvo.getTemplateVarList().size(); i++) {
				com.xnx3.wangmarket.admin.vo.bean.template.TemplateVar tvBean = tvo.getTemplateVarList().get(i);
				backupsTemplateVarMap.put(tvBean.getTemplateVar().getVarName(), tvBean);
			}
			
			//进行备份的还原操作
			for (int i = 0; i < data.getTemplateVarNameList().size(); i++) {
				String templateVarCodeName = data.getTemplateVarNameList().get(i);
				
				TemplateVar backupsTv = backupsTemplateVarMap.get(templateVarCodeName).getTemplateVar();	//导入的备份模板的
				TemplateVar tv = templateVarMap.get(templateVarCodeName);	//网站本身的
				if(tv == null){
					//如果是现在网站中删除了，那么在创建一个
					tv = new TemplateVar();
					tv.setAddtime(DateUtil.timeForUnix10());
					tv.setUpdatetime(tv.getAddtime());
					tv.setSiteid(site.getId());
					tv.setTemplateName(site.getTemplateName());
					tv.setVarName(filter(templateVarCodeName));
					tv.setUserid(site.getUserid());
					tv.setRemark(filter(backupsTv.getRemark()));
					
					//反正修改没有，就放到添加的这里面保存
					sqlService.save(tv);
				}
				
				
				if(tv.getId() != null && tv.getId() > 0){
					//模板变量属性保存成功，再保存内容数据
					TemplateVarData tvd = templateVarDataMap.get(tv.getId());
					if(tvd == null){
						tvd = new TemplateVarData();
						tvd.setId(tv.getId());
					}
					tvd.setText(backupsTemplateVarMap.get(templateVarCodeName).getText());
					sqlService.save(tvd);
					
					//刷新当前模板变量的Session缓存数据
					templateService.updateTemplateVarForCache(tv, tvd);
				}
			}
		}
		
		
		
		//输入模型
		if(data.getInputModelCodeNameList().size() > 0){
			Map<String, InputModel> inputModelMap = new HashMap<String, InputModel>();	//当前网站的输入模型， key：codeName
			Map<String, InputModel> backupsInputModelMap = new HashMap<String, InputModel>();	//要还原的备份的输入模型，之后用到时直接从map取   key:codeName
			
			//若是指定了还原输入模型，则先从数据库中将其获取到最新信息
		    List<InputModel> inputModelList = sqlService.findBySqlQuery("SELECT * FROM input_model WHERE siteid = " + site.getId(), InputModel.class);
			
		    //将用户当前网站的输入模型，list转化为map，方便取
			for (int i = 0; i < inputModelList.size(); i++) {
				InputModel im= inputModelList.get(i);
				inputModelMap.put(im.getCodeName(), im);
			}
			
			//将要还原的备份信息，list转化为map，方便取
			for (int i = 0; i < tvo.getInputModelList().size(); i++) {
				InputModel im = tvo.getInputModelList().get(i);
				backupsInputModelMap.put(im.getCodeName(), im);
			}
			
			//进行备份的还原操作
			for (int i = 0; i < data.getInputModelCodeNameList().size(); i++) {
				String inputModelCodeName = data.getInputModelCodeNameList().get(i);
				
				InputModel backupsIm = backupsInputModelMap.get(inputModelCodeName);	//导入的备份模板的
				InputModel im = inputModelMap.get(inputModelCodeName);	//网站本身的
				if(im == null){
					//如果是现在网站中删除了，那么在创建一个
					im = new InputModel();
					im.setCodeName(inputModelCodeName);
					im.setRemark(filter(backupsIm.getRemark()));
					im.setSiteid(site.getId());
				}
				im.setText(backupsIm.getText());
				sqlService.save(im);
			}
			
			//清空现在Session中存储的输入模型，待使用时重新从数据库中取
			Func.getUserBeanForShiroSession().setInputModelMap(null);
		}
		
		//网站栏目
		if(data.getSiteColumnCodeNameList().size() > 0){
			Map<String, SiteColumn> siteColumnMap = new HashMap<String, SiteColumn>();	//当前网站的栏目， key：codeName
			Map<String, SiteColumn> backupsSiteColumnMap = new HashMap<String, SiteColumn>();	//要还原的备份的栏目，之后用到时直接从map取   key:codeName
			
			//若是制定了还原栏目，则先从数据库中将其获取到最新信息
		    List<SiteColumn> siteColumnList = sqlService.findBySqlQuery("SELECT * FROM site_column WHERE siteid = " + site.getId(), SiteColumn.class);
			
		    //将用户当前网站的栏目，list转化为map，方便取
			for (int i = 0; i < siteColumnList.size(); i++) {
				SiteColumn sc = siteColumnList.get(i);
				siteColumnMap.put(sc.getCodeName(), sc);
			}
			
			//将要还原的备份信息，list转化为map，方便取
			for (int i = 0; i < tvo.getSiteColumnList().size(); i++) {
				SiteColumn sc = tvo.getSiteColumnList().get(i);
				backupsSiteColumnMap.put(sc.getCodeName(), sc);
			}
			
			//进行备份的还原操作
			for (int i = 0; i < data.getSiteColumnCodeNameList().size(); i++) {
				String siteColmnCodeName = data.getSiteColumnCodeNameList().get(i);
				
				SiteColumn backupsSc = backupsSiteColumnMap.get(siteColmnCodeName);	//导入的备份模板的
				SiteColumn sc = siteColumnMap.get(siteColmnCodeName);	//网站本身的
				boolean isCreate = false;	//当前栏目是否是从新创建，如果之前不存在，需要重新创建，此为true
				if(sc == null){
					//如果是现在网站中删除了，那么在创建一个
					sc = new SiteColumn();
					sc.setCodeName(filter(siteColmnCodeName));
					sc.setRank(0);
					sc.setSiteid(site.getId());
					sc.setUserid(site.getUserid());
					isCreate = true;
				}
				sc.setIcon(filter(backupsSc.getIcon()));
				sc.setInputModelCodeName(filter(backupsSc.getInputModelCodeName()));
				sc.setListNum(backupsSc.getListNum());
				sc.setName(filter(backupsSc.getName()));
				sc.setParentCodeName(filter(backupsSc.getParentCodeName()));
				sc.setRank(backupsSc.getRank());
				sc.setTemplatePageListName(filter(backupsSc.getTemplatePageListName()));
				sc.setTemplatePageViewName(filter(backupsSc.getTemplatePageViewName()));
				sc.setType(backupsSc.getType());
				sc.setUrl(filter(backupsSc.getUrl()));
				sc.setUsed(backupsSc.getUsed());
				if(backupsSc.getEditMode() == null){
					//如果是之前版本的，没有编辑方式，默认设置的是系统的输入模式，ueditor编辑器
					sc.setEditMode(SiteColumn.EDIT_MODE_INPUT_MODEL);
				}else{
					sc.setEditMode(backupsSc.getEditMode());
				}
				sqlService.save(sc);
				if(isCreate){
					//如果创建新的栏目，那么判断一下，如果是独立页面类型的栏目，还要创建内容页面
					siteColumnService.createNonePage(sc, site, false);
				}
				
				//刷新当前Session缓存数据
				siteColumnService.updateSiteColumnByCache(sc);
			}
		}
		
		return success();
	}
	
	/**
	 * 两个Integer类型的值比较是否相等。可传入null对比
	 * @param i1 第一个Integer数
	 * @param i2 第二个Integer数
	 * @return true：相等
	 */
	public static boolean ShortEqual(Short i1, Short i2){
		//先进行为空判断
		if(i1 == null && i2 != null){
			return false;
		}
		if(i1 != null && i2 == null){
			return false;
		}
		if(i1 == null && i2 == null){
			return true;
		}
		
		//都有值，进行值得对比
		if(i1 - i2 == 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 两个Integer类型的值比较是否相等。可传入null对比
	 * @param i1 第一个Integer数
	 * @param i2 第二个Integer数
	 * @return true：相等
	 */
	public static boolean IntegerEqual(Integer i1, Integer i2){
		//先进行为空判断
		if(i1 == null && i2 != null){
			return false;
		}
		if(i1 != null && i2 == null){
			return false;
		}
		if(i1 == null && i2 == null){
			return true;
		}
		
		//都有值，进行值得对比
		if(i1 - i2 == 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 模版插件，浏览云端可用的模版插件列表，选择插件安装
	 */
	@RequestMapping("/templatePlugin${url.suffix}")
	public String templatePlugin(HttpServletRequest request,Model model){
		AliyunLog.addActionLog(getSiteId(), "打开CMS模式下的模版插件选择界面");
		return "template/templatePlugin";
	}
	

	/**
	 * 导入模版插件，弹出对比窗口
	 * <br/>模版文件包含模版页面，模版变量、栏目、输入模型
	 */
	@RequestMapping(value="restoreTemplatePluginByRemote${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO restoreTemplatePluginByRemote(HttpServletRequest request, Model model, 
			@RequestParam(value = "pluginName", required = false, defaultValue="") String pluginName){
		TemplateCompareVO tcv = new TemplateCompareVO();
		Site site = getSite();
		
		if(!Func.isCMS(site)){
			return error("当前网站非CMS模式，无法使用模版插件");
		}
		
		if(pluginName.length() == 0){
			return error("请选择使用那个模版插件");
		}
		
		HttpUtil http = new HttpUtil(HttpUtil.UTF8);
		HttpResponse hr = http.get(G.RES_CDN_DOMAIN+"template_plugin/"+pluginName+"/template.wscso");
		if(hr.getCode() - 404 == 0){
			return error("云端模版插件不存在");
		}
		
		TemplateVO templateVO = new TemplateVO();
		templateVO.importText(hr.getContent());
		tcv = restoreTemplateCompare(request, templateVO);
		if(tcv.getResult() - TemplateCompareVO.FAILURE == 0){
			return error(tcv.getInfo());
		}
		
		AliyunLog.addActionLog(getSiteId(), "云端模版插件进行比对预览");
		
		//将比对好的存入Session，方便在其他页面直接显示
		request.getSession().setAttribute("comparePreviewTCV", tcv);
		request.getSession().setAttribute("comparePreviewTemplateVO", templateVO);
		
		return success();
	}
	

	/**
	 * 获取可用模版列表接口
	 */
	@RequestMapping(value="getTemplateList${url.suffix}")
	@ResponseBody
	public TemplateListVO getTemplateList(HttpServletRequest request, Model model, 
			@RequestParam(value = "type", required = false, defaultValue="-1") int type){
		Map<String, com.xnx3.wangmarket.admin.entity.Template> map = TemplateUtil.getTemplateList(type);
		List<com.xnx3.wangmarket.admin.entity.Template> list = new ArrayList<com.xnx3.wangmarket.admin.entity.Template>();
		
		for (Map.Entry<String, com.xnx3.wangmarket.admin.entity.Template> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		
		
		TemplateListVO vo = new TemplateListVO();
		vo.setList(list);
		vo.setResult(BaseVO.SUCCESS);
		return vo;
	}
	
	
}