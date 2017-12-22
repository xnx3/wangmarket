package com.xnx3.admin.controller;

import java.io.ByteArrayInputStream;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xnx3.DateUtil;
import com.xnx3.MD5Util;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.OSS;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.net.OSSUtil;
import com.xnx3.admin.Func;
import com.xnx3.admin.G;
import com.xnx3.admin.bean.UserBean;
import com.xnx3.admin.cache.Template;
import com.xnx3.admin.entity.InputModel;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.entity.SiteColumn;
import com.xnx3.admin.entity.TemplatePage;
import com.xnx3.admin.entity.TemplatePageData;
import com.xnx3.admin.entity.TemplateVar;
import com.xnx3.admin.entity.TemplateVarData;
import com.xnx3.admin.service.InputModelService;
import com.xnx3.admin.service.SiteColumnService;
import com.xnx3.admin.service.SiteService;
import com.xnx3.admin.service.TemplateService;
import com.xnx3.admin.util.AliyunLog;
import com.xnx3.admin.vo.RestoreTemplateSubmitCheckDataVO;
import com.xnx3.admin.vo.TemplateCompareVO;
import com.xnx3.admin.vo.TemplatePageListVO;
import com.xnx3.admin.vo.TemplatePageVO;
import com.xnx3.admin.vo.TemplateVO;
import com.xnx3.admin.vo.TemplateVarListVO;
import com.xnx3.admin.vo.TemplateVarVO;
import com.xnx3.admin.vo.bean.template.TemplateCompare.InputModelCompare;
import com.xnx3.admin.vo.bean.template.TemplateCompare.SiteColumnCompare;
import com.xnx3.admin.vo.bean.template.TemplateCompare.TemplatePageCompare;
import com.xnx3.admin.vo.bean.template.TemplateCompare.TemplateVarCompare;

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
	@RequestMapping("/index")
	public String index(HttpServletRequest request,Model model){
		TemplatePageVO vo = templateService.getTemplatePageIndexByCache(request);
		if(vo == null || vo.getResult() - TemplatePageVO.FAILURE == 0){
			//当前还没有模版页，那么可能是刚开通网站，还没有模版，默认跳转到选择模版的页面
			model.addAttribute("needSelectTemplate", "1");
		}else{
			//将模版变量装载入Session
			templateService.loadDatabaseTemplateVarToCache();
			
			model.addAttribute("templatePage", vo.getTemplatePage());
		}
		
		AliyunLog.addActionLog(getSiteId(), "进入CMS模式网站后台首页");
		
		UserBean userBean = getUserBean();
		User user = getUser();
		model.addAttribute("password", MD5Util.MD5(user.getPassword()));
		model.addAttribute("siteRemainHintJavaScript", siteService.getSiteRemainHintForJavaScript(userBean.getSite(), userBean.getParentAgency()));
		model.addAttribute("siteUrl", Func.getDomain(getSite()));
		model.addAttribute("site", getSite());
		model.addAttribute("user", user);
		return "template/index";
	}
	
	/**
	 * 模版变量列表
	 */
	@RequestMapping("/templateVarList")
	public String templateVarList(HttpServletRequest request,Model model){
		AliyunLog.addActionLog(getSiteId(), "进入模版变量列表");
		
	    model.addAttribute("list", templateService.getTemplateVarList());
	    return "template/templateVarList";
	}


	/**
	 * 模版页面列表
	 */
	@RequestMapping("/templatePageList")
	public String templatePageList(HttpServletRequest request,Model model){
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
	    
	    //将数据记录传到页面以供显示
	    model.addAttribute("list", list);
	    return "template/templatePageList";
	}
	
	/**
	 * 添加／编辑模版变量
	 * @param templateVarName 要编辑得模版变量名字，添加时无需传入此项
	 * @param templateName 模版名字，要给哪个模版添加模版变量。修改时无需传入此项
	 */
	@RequestMapping("templateVar")
	public String templateVar(HttpServletRequest request,Model model,
			@RequestParam(value = "templateVarName", required = false , defaultValue="") String templateVarName,
			@RequestParam(value = "templateName", required = false , defaultValue="") String templateName
			){
		templateVarName = filter(templateVarName);
		templateName = filter(templateName);
		
		//如果是编辑模式
		if(templateVarName.length() > 0){
			TemplateVar templateVar = sqlService.findAloneBySqlQuery("SELECT * FROM template_var WHERE userid = "+getUserId()+" AND var_name = '"+templateVarName+"'", TemplateVar.class);
			if(templateVar == null){
				return error(model, "没有发现属于您的、名字为"+templateVarName+"的模版变量");
			}
			TemplateVarData templateVarData = sqlService.findById(TemplateVarData.class, templateVar.getId()); 
			
			AliyunLog.addActionLog(getSiteId(), "编辑模式打开模版变量："+templateVarName);
			
			model.addAttribute("templateVar", templateVar);
			model.addAttribute("text", templateVarData.getText());
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
	@RequestMapping("saveTemplateVar")
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
			if(templateVar.getUserid() - getUserId() != 0){
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
	@RequestMapping("templatePage")
	public String templatePage(HttpServletRequest request,Model model,
			@RequestParam(value = "pageName", required = false , defaultValue="") String pageName
			){
		//如果是编辑模式
		if(pageName.length() > 0){
			TemplatePage templatePage = sqlService.findAloneBySqlQuery("SELECT * FROM template_page WHERE siteid = "+getSiteId()+" AND name = '"+Sql.filter(pageName)+"'", TemplatePage.class);
			if(templatePage == null){
				return error(model, "要修改的模版页面不存在");
			}
			String text = templateService.getTemplatePageTextByCache(templatePage.getId(), request);	//模版页面的内容
			
			AliyunLog.addActionLog(templatePage.getId(), "进入编辑模版页面："+templatePage.getName());
			
			model.addAttribute("templatePage", templatePage);
			model.addAttribute("text", text);
		}else{
			AliyunLog.addActionLog(0, "进入新建模版页面");
		}
		
		return "template/templatePage";
	}
	

	/**
	 * 保存模版页面
	 */
	@Deprecated
	@RequestMapping("saveTemplatePageOld")
	@ResponseBody
	public BaseVO saveTemplatePageOld(TemplatePage templatePageInput, 
			@RequestParam(value = "text", required = false , defaultValue="") String text,
			HttpServletRequest request,Model model){
		
		TemplatePage templatePage;
		TemplatePageData templatePageData = null;
		if(templatePageInput.getId() == null || templatePageInput.getId() == 0){
			//添加
			templatePage = new TemplatePage();
			templatePage.setUserid(getUserId());
			templatePage.setSiteid(getSiteId());
			
			templatePageData = new TemplatePageData();
		}else{
			//修改
			templatePage = sqlService.findById(TemplatePage.class, templatePageInput.getId());
			if(templatePage.getUserid() - getUserId() != 0){
				return error("不属于您，无法修改");
			}
			
			templatePageData = sqlService.findById(TemplatePageData.class, templatePage.getId());
		}
		
		templatePage.setName(filter(templatePageInput.getName()));
		sqlService.save(templatePage);
		if(templatePage.getId() != null && templatePage.getId() > 0){
			//保存成功，再判断templateVarData是否是添加，若是添加的话，得赋予其id
			if(templatePageData.getId() == null || templatePageData.getId() == 0){
				templatePageData.setId(templatePage.getId());
			}
			templatePageData.setText(text);
			sqlService.save(templatePageData);
			
			//刷新当前用户的模版页面缓存
			templateService.updateTemplatePageForCache(templatePage, templatePageData, request);
			
			AliyunLog.addActionLog(getSiteId(), "保存模版页成功（已废弃接口）");
		}
		
		return success(templatePage.getId()+"");
	}
	

	/**
	 * 保存模版页面，只保存{@link TemplatePage}，不保存模版页面的内容，内容单独保存
	 * @param templatePageInput 要保存的{@link TemplatePage}信息
	 */
	@RequestMapping("saveTemplatePage")
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
	 * 获取指定模版页面的内容，用于iframe内，htmledit修改
	 * @param pageName 要获取的模版页面名字，对应 {@link TemplatePage}.name
	 * @throws IOException
	 */
	@RequestMapping("getTemplatePageText")
	public String getTemplatePageText(Model model,HttpServletRequest request,
			@RequestParam(value = "pageName", required = true) String pageName
			) throws IOException{
		String html = null;
		
		TemplatePageVO vo = templateService.getTemplatePageByNameForCache(request, pageName);
		if(vo.getResult() - TemplatePageVO.FAILURE == 0){
			return error(model, vo.getInfo());
		}
		
		//判断一下，是否这个模版页面是刚建立的，还没有模版页内容
		if(vo.getTemplatePageData() == null){
			vo.setTemplatePageData(new TemplatePageData());
			vo.getTemplatePageData().setText(Template.newHtml);
		}
		
		//装载模版变量
		com.xnx3.admin.cache.TemplateCMS temp = new com.xnx3.admin.cache.TemplateCMS(getSite(), true);
		html = temp.assemblyTemplateVar(vo.getTemplatePageData().getText());
		
		//自动在</head>之前，加入htmledit.js
		html = html.replace("</head>", "<!--XNX3HTMLEDIT--><script>var masterSiteUrl='"+G.masterSiteUrl+"'; </script><script src=\"http://res.weiunity.com/htmledit/htmledit.js\"></script></head>");
		AliyunLog.addActionLog(vo.getTemplatePageData().getId(), "获取指定模版页内容，模版页："+StringUtil.filterXss(pageName));
		
		model.addAttribute("pageName", pageName);
		model.addAttribute("html", html);
		return "template/getTemplatePageText";
	}
	
	/**
	 * 保存模版页面的模版内容
	 * @param pageName 要获取的源代码的页面名字，如传入index，则会自动获取index.html的源代码返回
	 * @param html 修改后的网站首页的html源代码 
	 */
	@RequestMapping("saveTemplatePageText")
	@ResponseBody
	public BaseVO saveTemplatePageText(HttpServletRequest request,
			@RequestParam(value = "pageName", required = false, defaultValue="") String pageName,
			@RequestParam(value = "html", required = true) String html){
		TemplatePageVO vo = templateService.saveTemplatePageText(pageName, html, request);
		
		AliyunLog.addActionLog(0, "获取指定模版页"+(vo.getResult() - BaseVO.SUCCESS == 0 ? "成功":"失败")+"，模版页："+StringUtil.filterXss(pageName));
		
		BaseVO baseVO = new BaseVO();
		baseVO.setBaseVO(vo.getResult(), vo.getInfo());
		return baseVO;
	}
	
	/**
	 * 获取当前网站的模版变量，返回列表为： {include=...} 备注  的形式，供制作模版页时随时复制
	 */
	@RequestMapping("templateVarListForUsed")
	public String templateVarListForUsed(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
	    sql.setSearchTable("template_var");
	    //增加添加搜索字段。这里的搜索字段跟log表的字段名对应
	    sql.setSearchColumn(new String[]{"name"});
	    sql.appendWhere("userid = "+getUserId() + " AND template_name = '"+ getSite().getTemplateName() +"'");
	    //查询log数据表的记录总条数
	    int count = sqlService.count("site", sql.getWhere());
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
	@RequestMapping("deleteTemplateVar")
	@ResponseBody
	public BaseVO deleteTemplateVar(@RequestParam(value = "id", required = true) int id){
		TemplateVar templateVar = (TemplateVar) sqlService.findById(TemplateVar.class , id);
		if(templateVar == null){
			return error("要删除的模版变量不存在！");
		}
		if(templateVar.getUserid() - getUserId() != 0){
			return error("模版变量不属于你，无法删除");
		}
		
		sqlService.delete(templateVar);	//删除数据库的
		templateService.deleteTemplateVarForCache(templateVar.getId());	//删除缓存中的
		
		AliyunLog.addActionLog(templateVar.getId(), "删除模版变量："+templateVar.getTemplateName());
		
		return success();
	}
	

	/**
	 * 删除模版页
	 * @param id 要删除的模版页的id
	 */
	@RequestMapping("deleteTemplatePage")
	@ResponseBody
	public BaseVO deleteTemplatePage(HttpServletRequest request,
			@RequestParam(value = "id", required = true) int id){
		TemplatePage templatePage = sqlService.findById(TemplatePage.class , id);
		if(templatePage == null){
			return error("要删除的模版页不存在！");
		}
		if(templatePage.getUserid() - getUserId() != 0){
			return error("模版页不属于你，无法删除");
		}
		
		sqlService.delete(templatePage);
		
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
	@RequestMapping("exportTemplate")
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
	@RequestMapping("uploadImportTemplate")
	@ResponseBody
	public void uploadImportTemplate(HttpServletResponse response, HttpServletRequest request, 
			@RequestParam("templateFile") MultipartFile multipartFile) throws IOException{
		JSONObject json = new JSONObject();
		if(multipartFile == null){
			json.put("result", BaseVO.FAILURE);
			json.put("info", "请选择要导入的模版");
		}else{
			String templateText = StringUtil.inputStreamToString(multipartFile.getInputStream(), "UTF-8");
			BaseVO vo = templateService.importTemplate(templateText, true);
			json.put("result", BaseVO.SUCCESS);
			json.put("info", "成功");
			
			if(vo.getResult() - BaseVO.SUCCESS == 0){
				//导入完毕后，还要刷新当前的模版页面、模版变量缓存。这里清空缓存，下次使用时从新从数据库加载最新的
				request.getSession().setAttribute("templatePageListVO", null);
				Func.getUserBeanForShiroSession().setTemplateVarCompileDataMap(null);
				Func.getUserBeanForShiroSession().setTemplateVarMapForOriginal(null);
				
				AliyunLog.addActionLog(getSiteId(), "本地导入模版文件成功！");
			}else{
				AliyunLog.addActionLog(getSiteId(), "本地导入模版文件失败！");
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
	 * 通过res.weiunity.com的CDN获取制定的模版，远程获取模版文件，将当前网站应用此模版。
	 * <br/>模版文件包含模版页面，模版变量、栏目
	 */
	@RequestMapping("remoteImportTemplate")
	@ResponseBody
	public BaseVO remoteImportTemplate(HttpServletRequest request, Model model,
			@RequestParam(value = "templateName", required = false , defaultValue="") String templateName){
		if(templateName.length() == 0){
			return error("请选择要远程获取的模版");
		}
		
		HttpUtil http = new HttpUtil(HttpUtil.UTF8);
		HttpResponse hr = http.get(G.RES_CDN_DOMAIN+"template/"+templateName+"/template.wscso");
		if(hr.getCode() - 404 == 0){
			return error("模版不存在");
		}
		
		BaseVO vo = templateService.importTemplate(hr.getContent(), true);
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			//导入完毕后，还要刷新当前的模版页面、模版变量缓存。这里清空缓存，下次使用时从新从数据库加载最新的
			request.getSession().setAttribute("templatePageListVO", null);
			Func.getUserBeanForShiroSession().setTemplateVarCompileDataMap(null);
			Func.getUserBeanForShiroSession().setTemplateVarMapForOriginal(null);
			
			AliyunLog.addActionLog(getSiteId(), "云端导入模版文件成功！");
		}
		return vo;
	}
	
	/**
	 * 选择模版，选择模版导入，CMS模式下的选择模版，纯粹展示页面
	 */
	@RequestMapping("/selectTemplate")
	public String selectTemplate(HttpServletRequest request,Model model){
		TemplatePageVO vo = templateService.getTemplatePageIndexByCache(request);
		
		AliyunLog.addActionLog(getSiteId(), "打开CMS模式下的模版选择界面");
		if(vo.getResult() - TemplatePageVO.SUCCESS == 0){
			//如果有模版了，会进入模板还原页面
			return redirect("template/restoreTemplate.do");
		}else{
			//若没有模版，才会出现选择模版的界面
			return "template/selectTemplate";
		}
	}
	

	/**
	 * 上传图片接口
	 */
	@RequestMapping("uploadImage")
	@ResponseBody
	public UploadFileVO uploadImage(Model model,HttpServletRequest request){
		UploadFileVO uploadFileVO = new UploadFileVO();
		if(getSite() == null){
			uploadFileVO.setBaseVO(UploadFileVO.FAILURE, "请先登录");
			return uploadFileVO;
		}
		
		uploadFileVO = OSS.uploadImage("site/"+getSiteId()+"/templateimage/", request, "image");
		if(uploadFileVO.getResult() == UploadFileVO.SUCCESS){
			//上传成功，写日志
			AliyunLog.addActionLog(getSiteId(), "CMS模式下，模版页自由上传图片成功："+uploadFileVO.getFileName());
		}
		
		return uploadFileVO;
	}
	

	/**
	 * 自定义模版的整站刷新
	 */
	@RequestMapping("refreshForTemplate")
	@ResponseBody
	public BaseVO refreshForTemplate(Model model,HttpServletRequest request){
		AliyunLog.addActionLog(getSiteId(), "CMS模式下，刷新生成整站");
		if(OSSUtil.getOSSClient() == null){
			return error("请先访问 /install/index.do 进行安装，此依赖OSS使用");
		}
		
		return siteService.refreshForTemplate(request);
	}
	
	/**
	 * 已经有过模板了，还原模板,展示，选择要还原的模板的项。
	 * 此主要起展示作用，并没有实际功能
	 */
	@RequestMapping("restoreTemplate")
	public String restoreTemplate(Model model,HttpServletRequest request){
		AliyunLog.addActionLog(getSiteId(), "打开还原模板选择页面");
		
		//判断当前用户的模板是使用的云端的，还是本地导入的
		boolean usedYunTemplate = false;	//若是云端模板，则为true
		Site site = getSite();
		if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
			//判断当前网站使用的模板是否在云端模板库中（是否是云端模板）
			if(G.cloudTemplateListVO.getMapNameInfo().get(site.getTemplateName()) != null){
				usedYunTemplate = true;
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
	@RequestMapping("restoreTemplateByLocalhostFile")
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
	@RequestMapping("restoreTemplateByRemote")
	@ResponseBody
	public BaseVO restoreTemplateByRemote(HttpServletRequest request, Model model){
		TemplateCompareVO tcv = new TemplateCompareVO();
		Site site = getSite();
		
		if(site.getTemplateName() == null || site.getTemplateName().length() == 0){
			return error("当前网站未使用云端模板！云端还原失败");
		}
		
		HttpUtil http = new HttpUtil(HttpUtil.UTF8);
		HttpResponse hr = http.get(G.RES_CDN_DOMAIN+"template/"+site.getTemplateName()+"/template.wscso");
		if(hr.getCode() - 404 == 0){
			return error("云端模版不存在");
		}
		
		TemplateVO templateVO = new TemplateVO();
		templateVO.importText(hr.getContent());
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
	@RequestMapping("restoreTemplateComparePreview")
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
		
		//首先，判断当前模板是否可以还原到当前网站中
		Site site = Func.getCurrentSite();
		if(!StringUtil.StringEqual(site.getTemplateName(), tvo.getTemplateName())){
			templateCompareVO.setBaseVO(TemplateCompareVO.FAILURE, "当前网站使用的模板与导入的模板不属于同一个模板，无法还原！");
			return templateCompareVO;
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
			com.xnx3.admin.vo.bean.template.TemplatePage templatePageBean = tvo.getTemplatePageList().get(i);
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
			com.xnx3.admin.vo.bean.template.TemplateVar templateVarBean = tvo.getTemplateVarList().get(i);
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
	@RequestMapping("restoreTemplateSubmit")
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
			Map<String, com.xnx3.admin.vo.bean.template.TemplatePage> backupsTemplatePageMap = new HashMap<String, com.xnx3.admin.vo.bean.template.TemplatePage>();	//要还原的备份的模板内容，之后用到时直接从map取   key:name
			
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
				com.xnx3.admin.vo.bean.template.TemplatePage tpBean = tvo.getTemplatePageList().get(i);
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
			Map<String, com.xnx3.admin.vo.bean.template.TemplateVar> backupsTemplateVarMap = new HashMap<String, com.xnx3.admin.vo.bean.template.TemplateVar>();	//要还原的备份的模板变量内容，之后用到时直接从map取   key:varName
			
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
				com.xnx3.admin.vo.bean.template.TemplateVar tvBean = tvo.getTemplateVarList().get(i);
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
				if(sc == null){
					//如果是现在网站中删除了，那么在创建一个
					sc = new SiteColumn();
					sc.setCodeName(filter(siteColmnCodeName));
					sc.setRank(0);
					sc.setSiteid(site.getId());
					sc.setUserid(site.getUserid());
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
	
	
}