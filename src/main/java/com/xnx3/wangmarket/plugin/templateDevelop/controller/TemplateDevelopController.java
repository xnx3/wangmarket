package com.xnx3.wangmarket.plugin.templateDevelop.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.ZipUtil;
import com.xnx3.file.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.func.Language;
import com.xnx3.j2ee.func.Log;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.Template;
import com.xnx3.wangmarket.admin.entity.TemplatePage;
import com.xnx3.wangmarket.admin.entity.TemplateVar;
import com.xnx3.wangmarket.admin.service.TemplateService;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.admin.vo.TemplatePageListVO;
import com.xnx3.wangmarket.plugin.base.controller.BasePluginController;

import net.sf.json.JSONObject;

/**
 * 模版相关操作
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/templateDevelop/")
public class TemplateDevelopController extends BasePluginController {
	@Resource
	private SqlService sqlService;
	@Resource
	private TemplateService templateService;
	
	/*
	 * 导出路径。
	 * 如果是在开发环境，则是 static/template/   
	 * 如果是在实际运行环境，则是 WEB-INF/classes/static/template/
	 * 使用 getExportPath() 获取
	 */
	private static String exportPath = null;
	private static String getExportPath(){
		if(exportPath == null){
			if(Global.getProjectPath().indexOf("/target/classes") > -1){
				//在开发环境
				exportPath = "websiteTemplate/";
				//开发环境下，自动创建 target/classes/ 下的 websiteTemplate 文件夹
				File file = new File(Global.getProjectPath()+"websiteTemplate/");
				if(!file.exists()){
					//如果不存在，那么创建
					file.mkdir();
					Log.info("开发环境下，自动创建 websiteTemplate 文件夹，在 target/classes/ 下");
				}
			}else{
				//在实际使用环境
				exportPath = "websiteTemplate/";
			}
		}
		Log.info("exportPath:"+exportPath);
		return exportPath;
	}
	
	
	/**
	 * 远程获取模版，或者本地导入的还原的模版，进行还原比对，比对成功后，依赖此处从Session中取得比对的结果，显示给用户看，方便用户选择要还原哪个项
	 */
	@RequestMapping("index${url.suffix}")
	public String index(HttpServletRequest request, Model model){
		if(!haveSiteAuth()){
			return error(model, "无权操作");
		}
		
		if(!clientCheck(request)){
			return error(model, "模版开发仅限本地运行使用！仅限 localhost 访问时可用。");
		}
		
		Site site = getSite();
		model.addAttribute("site", site);
		
		//是否已经有模版了
		TemplatePageListVO tpvo = templateService.getTemplatePageListByCache(request);
		model.addAttribute("haveTemplate", (tpvo.getList() == null || tpvo.getList().size() == 0)? "0":"1");	//1已经有模版了， 0还没有模版，可以进行导入
		
		//取得当前项目的路径，定位到 ROOT/ 下
		String projectPath = Global.getProjectPath();
		//判断一下，如果是Windows的，需要将最开始第一个字符的/给去掉
		if(System.getProperties().getProperty("os.name").trim().toLowerCase().indexOf("window") > -1){
			if(projectPath.indexOf("/") == 0){
				projectPath = projectPath.substring(1, projectPath.length());
			}
		}
		model.addAttribute("projectPath", projectPath);
		
		//模版所在文件夹
		model.addAttribute("exportPath", getExportPath());
		
		return "plugin/templateDevelop/index";
	}
	
	/**
	 * 保存模版编码（templateName）
	 */
	@RequestMapping(value="saveTemplateName${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO saveTemplateName(HttpServletRequest request, 
			@RequestParam(value = "templateName", required = false, defaultValue="") String templateName){
		if(!haveSiteAuth()){
			return error("无权操作");
		}
		
		if(!clientCheck(request)){
			return error( "模版开发仅限本地运行使用！");
		}
		
		Site site = getSite();
		if(site.getTemplateName() != null && site.getTemplateName().length() > 0){
			return error("当前已有模版编码，不可再次设置！");
		}
		templateName = StringUtil.filterXss(templateName);
		if(templateName.length() == 0){
			return error("您还没填写模版编码呢");
		}
		
		//在 template 数据表中判断是否存在这个模版，若不存在，则创建
		Template template = sqlService.findAloneByProperty(Template.class, "name", templateName);
		if(template != null){
			return error("模版已存在！请重新起个名吧");
		}
		template = new Template();
		template.setAddtime(DateUtil.timeForUnix10());
		template.setName(templateName);
		template.setUserid(getUserId());
		sqlService.save(template); 	//保存模版
		
		//将当前站点使用模版设置为此模版
		site = sqlService.findById(Site.class, site.getId());
		site.setTemplateName(templateName);
		sqlService.save(site);
		setSite(site);
		
		//将当前站点使用的模版变量、模版页面全部设置为绑定这个模版
		sqlService.updateByHql(TemplatePage.class, "templateName", templateName, "siteid", site.getId());
		sqlService.updateByHql(TemplateVar.class, "templateName", templateName, "siteid", site.getId());
		
		//创建模版编码的文件夹
		File file = new File(Global.getProjectPath()+getExportPath()+templateName);
		file.mkdir();	//创建这个文件夹
		//创建 css 文件夹
		new File(Global.getProjectPath()+getExportPath()+templateName+"/css").mkdir();
		//创建 js 文件夹
		new File(Global.getProjectPath()+getExportPath()+templateName+"/js").mkdir();
		//创建 images 文件夹
		new File(Global.getProjectPath()+getExportPath()+templateName+"/images").mkdir();
		
		AliyunLog.addActionLog(site.getId(), "模版开发-保存模版编码");
		
		return success();
	}
	
	
	/**
	 * 导出模版文件（包含template.wscso + 资源文件）
	 * @throws IOException 
	 */
	@RequestMapping("exportTemplate${url.suffix}")
	@ResponseBody
	public BaseVO exportTemplate(HttpServletRequest request,HttpServletResponse response, Template temp) throws IOException{
		if(!haveSiteAuth()){
			return error("无权操作");
		}
		if(!clientCheck(request)){
			return error("模版开发仅限本地运行使用！");
		}
		
		Site site = getSite();
		//判断模版名字是否存在
		if(site.getTemplateName() == null || site.getTemplateName().length() == 0 || site.getTemplateName().equals("null")){
			return error("请先设置第一步，设置模版名字");
		}
		
		//模版信息赋予
		Template template = sqlService.findAloneByProperty(Template.class, "name", site.getTemplateName());
		if(template == null){
			//如果模版不存在，也就是第一步不是正当创建的，那么重新创建一个模版吧
			template = new Template();
		}
		template.setCompanyname(temp.getCompanyname());
		template.setName(site.getTemplateName());
		template.setRemark(temp.getRemark());
		template.setSiteurl(temp.getSiteurl());
		template.setTerminalDisplay(temp.getTerminalDisplay());
		template.setTerminalIpad(temp.getTerminalIpad());
		template.setTerminalMobile(temp.getTerminalMobile());
		template.setTerminalPc(temp.getTerminalPc());
		template.setType(temp.getType());
		template.setUsername(temp.getUsername());
		sqlService.save(template);
		
		
		//导出的目录，相对路径
		String exportPath = getExportPath()+site.getTemplateName()+"/";
		//初始化创建模版所在文件夹，避免模版不存在导致出错。正常流程中，这个文件夹是已经存在的
		AttachmentFile.directoryInit(exportPath);
		//创建下载链接的存放文件
		AttachmentFile.directoryInit("plugin_data/templateDevelop/");
		
		//判断一下是否已经有这个 zip 模版文件了，如果已经有了，那么删除掉
		if(FileUtil.exists(Global.getProjectPath()+"plugin_data/templateDevelop/template_"+site.getTemplateName()+".zip")){
			//如果文件存在，那么删除掉
			FileUtil.deleteFile(Global.getProjectPath()+"plugin_data/templateDevelop/template_"+site.getTemplateName()+".zip");
		}
		
		//导出 template.wscso ，放入 模版资源文件中，以便统一打包
		BaseVO vo = templateService.exportTemplate(request);
		if(vo.getResult() - BaseVO.FAILURE == 0){
			//失败
			return vo;
		}
		//写出 template.wscso
		FileUtil.write(Global.getProjectPath()+exportPath+"template.wscso", vo.getInfo(), FileUtil.UTF8);
		
		//将这些文件打包，变为 zip文件
		String downUrl = Global.get("MASTER_SITE_URL")+"plugin_data/templateDevelop/template_"+site.getTemplateName()+".zip";
		try {
			ZipUtil.zip(Global.getProjectPath()+exportPath, Global.getProjectPath()+"plugin_data/templateDevelop/", "template_"+site.getTemplateName()+".zip");
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
		
		
		return success(downUrl);
	}


	/**
	 * 上传preview.jpg图片接口
	 */
	@RequestMapping(value="uploadPreview${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public UploadFileVO uploadPreview(Model model,HttpServletRequest request){
		UploadFileVO uploadFileVO = new UploadFileVO();
		if(!haveSiteAuth()){
			uploadFileVO.setBaseVO(UploadFileVO.FAILURE, "无权操作");
			return uploadFileVO;
		}
		if(!clientCheck(request)){
			uploadFileVO.setBaseVO(UploadFileVO.FAILURE, "模版开发仅限本地运行使用！");
			return uploadFileVO;
		}
		
		Site site = getSite();
		
		//判断一下对方使用的是OSS还是本地存储，如果没用本地存储，提醒用户要使用本地存储的
		if(!AttachmentFile.isMode(AttachmentFile.MODE_LOCAL_FILE)){
			uploadFileVO.setBaseVO(UploadFileVO.FAILURE, "存储方式请使用服务器本地存储。请按照我们的说明，使用本地一键运行包进行模版开发！");
			return uploadFileVO;
		}
		
		
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
			List<MultipartFile> imageList = multipartRequest.getFiles("image");  
			if(imageList.size()>0 && !imageList.get(0).isEmpty()){
				MultipartFile multi = imageList.get(0);
				try {
					uploadFileVO = AttachmentFile.put(getExportPath()+site.getTemplateName()+"/preview.jpg", multi.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				uploadFileVO.setResult(UploadFileVO.NOTFILE);
				uploadFileVO.setInfo(Language.show("oss_uploadNotFile"));
			}
	    }else{
	    	uploadFileVO.setResult(UploadFileVO.NOTFILE);
			uploadFileVO.setInfo(Language.show("oss_uploadNotFile"));
	    }
		
		
		if(uploadFileVO.getResult() == UploadFileVO.SUCCESS){
			//上传成功，写日志
			AliyunLog.addActionLog(getSiteId(), "模版开发，上传preview.jpg："+uploadFileVO.getUrl());
		}
		
		return uploadFileVO;
	}

	
	/**
	 * 检查客户端情况。是否是本地进行访问的
	 * @param request
	 * @return true 是本地进行访问的
	 */
	private boolean clientCheck(HttpServletRequest request){
		String domain = request.getServerName().toLowerCase();
		if(domain.equals("localhost") || domain.equals("127.0.0.1")){
			return true;
		}else{
			return false;
		}
	}
}