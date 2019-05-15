package com.xnx3.wangmarket.admin.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xnx3.DateUtil;
import com.xnx3.MD5Util;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.bean.UserBean;
import com.xnx3.wangmarket.admin.cache.GenerateHTML;
import com.xnx3.wangmarket.admin.cache.Template;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.pluginManage.PluginManage;
import com.xnx3.wangmarket.admin.pluginManage.SitePluginBean;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.AliyunLog;

/**
 * 通用网站－PC端电脑网站，site.client 为pc网站的
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/sitePc/")
public class SitePcController extends BaseController {

	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	
	/**
	 * 电脑网站控制台首页
	 */
	@RequestMapping("index${url.suffix}")
	public String index(HttpServletRequest request, Model model){
		AliyunLog.addActionLog(getSiteId(), "进入通用电脑网站控制台首页");
		
		//获取网站后台管理系统有哪些功能插件，也一块列出来,以直接在网站后台中显示出来
		String pluginMenu = "";
		if(PluginManage.pcSiteClassManage.size() > 0){
			for (Map.Entry<String, SitePluginBean> entry : PluginManage.pcSiteClassManage.entrySet()) {
				SitePluginBean bean = entry.getValue();
				pluginMenu += "<dd><a id=\""+entry.getKey()+"\" class=\"subMenuItem\" href=\"javascript:loadIframeByUrl('"+bean.getMenuHref()+"'), notUseTopTools();\">"+bean.getMenuTitle()+"</a></dd>";
			}
		}
		model.addAttribute("pluginMenu", pluginMenu);
		
		
		
		UserBean userBean = getUserBean();
		User user = getUser();
		model.addAttribute("siteRemainHintJavaScript", siteService.getSiteRemainHintForJavaScript(userBean.getSite(), userBean.getParentAgency()));
		model.addAttribute("siteUrl", "http://"+Func.getDomain(getSite()));
		model.addAttribute("user", user);
		model.addAttribute("password", MD5Util.MD5(user.getPassword()));		//跟im应用交互验证
		model.addAttribute("site", getSite());
		model.addAttribute("parentAgency", getParentAgency());	//上级代理
		model.addAttribute("im_kefu_websocketUrl", com.xnx3.wangmarket.im.Global.websocketUrl);
		model.addAttribute("autoAssignDomain", G.getFirstAutoAssignDomain());	//自动分配的域名，如 wang.market
		return "sitePc/index";
	}
	

	/**
	 * 预览PC站点首页，并进行修改操作
	 * 若GET传入参数 reloadCleanCache=1 ，则强制刷新，相当于CTRL+F5，清除缓存。不过此依赖于indexedit.js，纯粹是js控制 。
	 */
	@RequestMapping("editIndex${url.suffix}")
	public String editIndex(HttpServletRequest request,Model model){
		Site site = getSite();
		
		AliyunLog.addActionLog(getSiteId(), "进入通用电脑网站预览PC站点首页，并进行修改操作");
		
		GenerateHTML gh = new GenerateHTML(site);
		gh.setEditMode(true);
		String templateHtml = gh.pcIndex();
		
		//预览，生成好的URL
		String previewHtml = gh.getGeneratePcIndexHtml();
		if(previewHtml == null || previewHtml.equals("")){
			previewHtml = templateHtml;
		}else{
			/*将一些相对路径的变为绝对路径*/
			//JS缓存数据路径转换
			Template temp = new Template(site);
			previewHtml = temp.replaceForEditModeTag(previewHtml);
		}
		
		//检查是否存再indexedit.js，如果用户自定义首页将其删除了，再将其加入
		if(previewHtml.indexOf("indexedit.js") == -1){
			previewHtml = previewHtml + "<script src=\""+Global.get("ATTACHMENT_FILE_URL")+"js/admin/indexedit.js\"></script>";
		}
		
		//v3.0调试使用
//		previewHtml = previewHtml.replace("http://res.weiunity.com/js/indexedit.js", "http://localhost:8082/selfSite/js/pc_template/indexedit.js?v="+DateUtil.timeForUnix10());
//		previewHtml = previewHtml.replace("http://res.weiunity.com/template/\"+getTemplateId()+\"/js/footeredit.js", "http://localhost:8082/selfSite/js/linshi/3/footeredit.js?v="+DateUtil.timeForUnix10());
//		previewHtml = previewHtml.replace("http://res.weiunity.com/template/\"+getTemplateId()+\"/styleedit.css", "http://localhost:8082/selfSite/js/linshi/3/styleedit.css");
//		previewHtml = previewHtml.replace("http://res.weiunity.com/template/\"+getTemplateId()+\"/js/headeredit.js", "http://localhost:8082/selfSite/js/linshi/3/headeredit.js");
		
		
		//电脑模式网站跟手机模式网站不同的是，首页会有产品展示内容、新闻内容。为了不每次加载首页都去数据库取，那么这里采用静态页面修改对比的方式进行。所以，要将site.js等缓存的数据文件进行替换，将其加入版本后，以免缓存。当然， 后续考虑会使用cms的缓存模式
		long time = DateUtil.timeForUnix13();
		previewHtml = previewHtml.replace("site.js\">", "site.js?v="+time+"\">");
		previewHtml = previewHtml.replace("siteColumn.js\">", "siteColumn.js?v="+time+"\">");
		previewHtml = previewHtml.replace("siteColumn.js\">", "siteColumn.js?v="+time+"\">");
		previewHtml = previewHtml.replace("siteColumnRank.js\">", "siteColumnRank.js?v="+time+"\">");
		previewHtml = previewHtml.replace("carouselList.js\">", "carouselList.js?v="+time+"\">");
		//切换模版后版面混乱的问题
		previewHtml = previewHtml.replace("styleedit.css\"", "styleedit.css?v="+time+"\"");
		
		
		model.addAttribute("html", previewHtml);
		return "sitePc/editIndex";
	}
	

	/**
	 * (很久没用到了，目前只是配合 commonedit.js 得 htmlSource方法)
	 * 获取指定html页面的源代码，用户弹出框修改
	 * @param fileName 要获取的源代码的页面名字，如传入index，则会自动获取index.html的源代码返回
	 * @param newHtml 是否是新建，默认不传递参数则不是新建，若传递1，则是新建页面
	 * @throws IOException
	 */
//	@RequestMapping("editPageSource.do")
//	public String getPageSource(Model model,
//			@RequestParam(value = "fileName", required = true) String fileName,
//			@RequestParam(value = "newHtml", required = false, defaultValue="0") int newHtml) throws IOException{
//		String html = null;
//		if(newHtml == 1){
//			//新建
//			html = "<!DOCTYPE html>\n"
//					+ "<html>\n"
//					+ "<head>\n"
//					+ "<meta charset=\"utf-8\">\n"
//					+ "<title>网市场新建页面</title>\n"
//					+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n"
//					+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n"
//					+ "<!--XNX3HTMLEDIT--></head>\n"
//					+ "<body>\n\n\n"
//					+ "页面内容\n\n\n</body>\n"
//					+ "</html>";
//		}else{
//			//编辑，读取HTML源代码
//			OSSObject ossObject = OSSUtil.getOSSClient().getObject(OSSUtil.bucketName, "site/"+getSiteId()+"/"+fileName+".html");
//			html = IOUtils.toString(ossObject.getObjectContent(), "UTF-8");
//		}
//		
//		//自动在</head>之前，加入htmledit.js
//		html = html.replace("</head>", "<!--XNX3HTMLEDIT--><script src=\"http://res.weiunity.com/htmledit/htmledit.js\"></script></head>");
//		
//		AliyunLog.addActionLog(getSiteId(), "获取指定html页面："+newHtml+"+ 的源代码，弹出框修改");
//		
//		model.addAttribute("fileName", fileName);
//		model.addAttribute("html", html);
//		return "site/pc_editPageSource";
//	}
	
	/**
	 * 保存站点的html文件
	 * 配合 getPageSource 一块使用。目前用不到，先注释掉。用时可以直接开启注释即可
	 * @param fileName 要获取的源代码的页面名字，如传入index，则会自动获取index.html的源代码返回
	 * @param text 修改后的网站首页的html源代码 
	 */
//	@RequestMapping("savePageSource")
//	public void savePageSource(Model model,HttpServletResponse response,
//			@RequestParam(value = "fileName", required = true) String fileName,
//			@RequestParam(value = "html", required = true) String html){
//		//将之前的那个首页进行缓存
//		Site site = getSite();
//		
//		//判断其是否是进行了自由编辑，替换掉其中htmledit加入的js跟css文件，也就是替换掉<!--XNX3HTMLEDIT-->跟</head>中间的所有字符，网页源码本身<!--XNX3HTMLEDIT-->跟</head>是挨着的
//		int htmledit_start = html.indexOf("<!--XNX3HTMLEDIT-->");
//		if(htmledit_start > 0){
//			int htmledit_head = html.indexOf("</head>");
//			if(htmledit_head == -1){
//				//出错了，忽略
//			}else{
//				//成功，进行过滤中间的htmledit加入的js跟css
//				html = html.substring(0, htmledit_start)+html.substring(htmledit_head, html.length());
//			}
//			
//			//contenteditable=true去掉
//			if(html.indexOf("<body contenteditable=\"true\">") > -1){
//				html = html.replace("<body contenteditable=\"true\">", "<body>");
//			}else{
//				html = html.replaceAll(" contenteditable=\"true\"", "");
//			}
//		}
//		
//		//如果这个页面中使用了模版变量，保存时，将模版变量去掉，变回模版调用形式{includeid=},卸载变量模版
//		if(html.indexOf("<!--templateVarStart-->") > -1){
////			Template temp = new Template(getSite());
//			Pattern p = Pattern.compile("<!--templateVarStart-->([\\s|\\S]*?)<!--templateVarEnd-->");
//	        Matcher m = p.matcher(html);
//	        while (m.find()) {
//	        	String templateVarText = m.group(1);	//<!--templateVarName=-->+模版变量的内容
//	        	String templateVarName = Template.getConfigValue(templateVarText, "templateVarName");	//模版变量的名字
//	        	templateVarName = Sql.filter(templateVarName);
//	        	
//	        	//替换出当前模版变量的内容，即去掉templateVarText注释
//	        	templateVarText = templateVarText.replace("<!--templateVarName="+templateVarName+"-->", "");
//	        	
//	        	//判断模版变量是否有过变动，当前用户是否修改过模版变量，如果修改过，将修改过的模版变量保存
//	        	//从内存中取模版变量内容
//	        	String content = com.xnx3.wangmarket.admin.G.templateVarMap.get(site.getTemplateName()).get(templateVarName);
//	        	
//	        	//讲用户保存的跟内存中的模版变量内容比较，是否一样，若不一样，要将当前的保存
//	        	if(!(content.replaceAll("\r|\n|\t", "").equals(templateVarText.replaceAll("\r|\n|\t", "")))){
//	        		//不一样，进行保存
//	        		com.xnx3.wangmarket.admin.entity.TemplateVar tv = sqlService.findAloneBySqlQuery("SELECT * FROM template_var WHERE template_name = '"+site.getTemplateName()+"' AND var_name = '"+templateVarName+"'", com.xnx3.wangmarket.admin.entity.TemplateVar.class);
//	        		tv.setUpdatetime(DateUtil.timeForUnix10());
//	        		sqlService.save(tv);
//	        		
//	        		TemplateVarData templateVarData = (TemplateVarData) sqlService.findById(TemplateVarData.class, tv.getId());
//	        		templateVarData.setText(templateVarText);
//	        		sqlService.save(templateVarData);
//	        	}
//	        	
////	            将用户保存的当前的模版页面，模版变量摘除出来，还原为模版调用的模式
//	            html = html.replaceAll("<!--templateVarStart--><!--templateVarName="+templateVarName+"-->([\\s|\\S]*?)<!--templateVarEnd-->", "{include="+templateVarName+"}");
//	        }
//		}
//		
//		//将自定义首页的源代码保存
//		OSSUtil.putStringFile("site/"+getSiteId()+"/"+fileName+".html", html);
//		
//		AliyunLog.addActionLog(getSiteId(), "通用电脑网站，编辑保存指定html页面："+fileName+".html");
//		
//		JSONObject json = new JSONObject();
//		json.put("result", "1");
//		response.setCharacterEncoding("UTF-8");  
//	    response.setContentType("application/json; charset=utf-8");  
//	    PrintWriter out = null;  
//	    try { 
//	        out = response.getWriter();  
//	        out.append(json.toString());
//	    } catch (IOException e) {  
//	        e.printStackTrace();  
//	    } finally {
//	        if (out != null) {  
//	            out.close();  
//	        }  
//	    }  
//	}
	

	/**
	 * 首页，点击左侧菜单的高级设置生效
	 */
	@RequestMapping("popupGaoJiSet${url.suffix}")
	public String popupGaoJiSet(Model model){
		AliyunLog.addActionLog(getSiteId(), "通用电脑网站模式下，点击左侧菜单打开高级模式页面");
		return "sitePc/popup_gaoJiSet";
	}
	

	/**
	 * 首页，点击左侧菜单的SEO优化调出
	 */
	@RequestMapping("seo${url.suffix}")
	public String seo(Model model){
		AliyunLog.addActionLog(getSiteId(), "通用电脑网站模式下，点击左侧菜单打开SEO优化页面");
		return "sitePc/popup_seo";
	}
	
	/**
	 * v3.11 增加，适应v3.10及以前版本
	 */
	@RequestMapping("*.html")
	public String html(Model model, HttpServletRequest request){
		String htmlFiles = request.getServletPath();
		String[] hfs = htmlFiles.split("/");
		String fileName = hfs[hfs.length-1];	//得到文件名，如 1234.html
		
		String[] fns = fileName.split("\\.");
		String htmlFile = fns[0];
		
		return redirect("sites/html.do?htmlFile="+htmlFile);
	}
	

}