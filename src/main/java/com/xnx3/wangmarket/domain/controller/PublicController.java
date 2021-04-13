package com.xnx3.wangmarket.domain.controller;

import java.lang.reflect.InvocationTargetException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.func.ApplicationProperties;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.util.TerminalDetection;
import com.xnx3.net.OSSUtil;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.domain.G;
import com.xnx3.wangmarket.domain.Log;
import com.xnx3.wangmarket.domain.bean.RequestInfo;
import com.xnx3.wangmarket.domain.bean.SimpleSite;
import com.xnx3.wangmarket.domain.bean.TextBean;
import com.xnx3.wangmarket.domain.pluginManage.interfaces.manage.DomainPluginManage;
import com.xnx3.wangmarket.domain.util.GainSource;
import com.xnx3.wangmarket.domain.vo.SImpleSiteVO;

/**
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class PublicController extends BaseController {
	@Resource
	private SqlService sqlService;
	
	//主站url，即使用泛解析的主域名访问时，直接跳转到的url
	@Value("masterSiteUrl")
	private String masterSiteUrl;
	
	/**
	 * 域名捕获转发
	 * @param htmlFile 访问的html文件，如访问c202.html ，则传入c202，会自动拼接上.html。 如果有传递这个参数，优先使用这个参数。如果这个参数没有转入值，那么取实际访问的路径文件
	 * @param domain 访问的域名，传入如 help.wang.market 如果这个参数有值，则优先使用这个参数。如果这里没有传入值，那么取用户当前访问所使用的的域名
	 * @param request 可get传入 domain 模拟访问的域名。可传入自己绑定的域名，也可传入二级域名。如domain=leiwen.wang.market
	 */
	@RequestMapping("*.html")
	public String dns(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(value = "htmlFile", required = false , defaultValue="") String htmlFile){
		if(htmlFile.length() == 0){
			htmlFile = request.getServletPath();
			htmlFile = htmlFile.replace("/", "");	//将开头的 /去掉
		}else{
			htmlFile = htmlFile + ".html";
		}
		
		SImpleSiteVO simpleSiteVO = getCurrentSimpleSite(request);
		
		/*
		 * 判断，当访问 index.html 有这么几种情况
		 * 1. 访问建好的某个网站首页   -- /index.html   有可能用户顶级域名解析过来了，但是还没有在网站后台绑定
		 * 2. 访问主站(总管理后台)		/index.html
		 * 3. 网站管理后台中，预览网站		/index.html + session|get参数domain,不过既然获取了get传来的参数，那么 simpleSiteVO.result 是 success 的
		 * 
		 */
		if(htmlFile.equals("index.html")){
			if(simpleSiteVO != null && simpleSiteVO.getResult() - SImpleSiteVO.SUCCESS == 0){
				//成功，用户直接访问的某个网站首页，肯定不进入登陆页面的
			}else{
				/*
				 * 不正常，因为没有找到对应的网站，那就应该是有两种情况： 
				 * 1. 访问顶级域名，但是域名解析过来了，但是还没有在网站后台绑定，所以找不到网站
				 * 2. 访问主站（管理后台）
				 * 判断一下，到底是1，还是2
				 */
				//如果访问的是首页，且访问的是masterSiteUrl，那么直接到 login.do
				if(SystemUtil.get("MASTER_SITE_URL").indexOf("://"+request.getServerName()) > 0){
					// 访问的是 直接跳转到登陆页面
					return redirect("login.do");
				}
			}
		}
		
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setHtmlFile(htmlFile);
		requestInfo.setIp(IpUtil.getIpAddress(request));
		requestInfo.setReferer(request.getHeader("referer"));
		requestInfo.setServerName(request.getServerName());
		requestInfo.setTime(DateUtil.timeForUnix10());
		requestInfo.setUserAgent(request.getHeader("User-Agent"));
		
//		htmlFile = htmlFile + ".html";
		//访问日志记录
//		requestLog(request, requestInfo);
		Log.requestLog(request, requestInfo, simpleSiteVO);
		
		if(simpleSiteVO.getResult() - SImpleSiteVO.FAILURE == 0){
			//未发现这个域名对应的网站
			
			//有可能是系统还未安装，判断，如果是，则进行指引安装
			if(!isUnInstallRequest()){
				return "domain/welcome";
			}
			
			//v4.12增加
			model.addAttribute("adminUrl", getAdminLoginUrl());
			return "domain/notFindDomain";
		}
		
		//判断网站的状态，冻结的网站将无法访问
		SimpleSite simpleSite = simpleSiteVO.getSimpleSite();
		if(simpleSite.getState() != null){
			if(simpleSite.getState() - Site.STATE_FREEZE == 0){
				//2为冻结，暂停，此时访问网站会直接到冻结的提示页面
				model.addAttribute("url", simpleSiteVO.getServerName()+"/"+htmlFile);
				return "domain/pause";
			}
		}
		
		String html = GainSource.get(simpleSite, htmlFile).getText();
		if(html == null){
			//判断一下是否是使用的OSS，并且配置了，如果没有配置，那么控制台给出提示
			if(AttachmentUtil.isMode(AttachmentUtil.MODE_ALIYUN_OSS) && OSSUtil.getOSSClient() == null){
				System.out.println("您未开启OSS对象存储服务！网站访问是必须通过读OSS数据才能展现出来的。开启可参考：http://www.guanleiming.com/2327.html");
			}
			if(htmlFile.equals("index.html")){
				//如果是首页，但是没有获取到这个页面的数据
				
				//有可能是系统还未安装，判断，如果是，则进行指引安装
				if(!isUnInstallRequest()){
					return "domain/welcome";
				}
				
				//已安装过了，正常访问，那肯定是CMS模式，没有生成整站的缘故，应在404页面中给用户提示
				//v4.12增加
				model.addAttribute("adminUrl", getAdminLoginUrl());
				return "domain/notFindIndexHtml";
			}
			return "domain/404";
		}
		
		//如果用的第六套模版，需要进行手机电脑自适应
		if(simpleSite.getTemplateId() - 6 == 0){
			//判断是否增加了 viewport，若没有，补上
			if(html.indexOf("<meta name=\"viewport\"") == -1){
				String equiv = "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">";
				int ins = -1;
				int equivInt = html.indexOf(equiv);
				if(equivInt > -1){
					ins = equivInt + equiv.length();
				}
				html = StringUtil.insert(html, "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=no\">", ins);
			}
		}
		
		html = replaceHtmlTag(simpleSite, html);
		//过滤掉空行
//			html = html.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", ""); 
		
		
		/**** 针对html源码处理插件 ****/
		try {
			html = DomainPluginManage.manage(html, simpleSite, requestInfo);
		} catch (InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		
		model.addAttribute("html", html);
		
		//判断此网站的类型，是PC端还是手机端
		if(simpleSite.getClient() - Site.CLIENT_WAP == 0){
			//手机端
			//如果是电脑浏览的，会显示经过处理的电脑页面，有个手机模拟器
			//如果是手机访问的，也是使用二级域名进行访问
			boolean isMobile = TerminalDetection.checkMobileOrPc(request);
			if(!isMobile){
				model.addAttribute("url", AttachmentUtil.netUrl()+"site/"+simpleSite.getId()+"/"+htmlFile);
				return "domain/pcPreview";
			}
		}
		return "domain/display";
	}
	
	/**
	 * 返回当前网市场云建站系统登陆的url网址 (不带 login.do)
	 * @return 登陆url地址，如  http://wang.market/ 
	 */
	private String getAdminLoginUrl(){
		String rabbitmqHost = ApplicationProperties.getProperty("spring.rabbitmq.host");
		if(rabbitmqHost == null || rabbitmqHost.length() == 0){
			//单服务器部署，用相对路径就可以
			return "/";
		}else{
			//多服务器分布式部署，用绝对路径
			return SystemUtil.get("MASTER_SITE_URL");
		}
	}
	
	/**
	 * 判断是否是第一次安装好后访问,判断一下是不是安装之前 install/index.do 访问的，安装入口还是开启的
	 * @return
	 * 		<ul>
	 * 			<li>false： 系统尚未进行安装，会转到 domain/welcome 进行安装指引</li>
	 * 			<li>true： 系统已进行安装了，不需要安装指引</li>
	 * 		</ul>
	 */
	private boolean isUnInstallRequest(){
		//可能是第一次安装好后访问,判断一下是不是安装install/index.do 还是开启的， true：开启的，允许进行安装
		if(SystemUtil.get("IW_AUTO_INSTALL_USE") != null && SystemUtil.get("IW_AUTO_INSTALL_USE").equals("true")){
			//install/index.do 还是开启的,那几乎可以肯定，还未安装过，这也就是刚运行来后访问的
			return false;
		}
		
		return true;
	}
	
	/**
	 * sitemap.xml展示
	 * @param request {@link HttpServletRequest}
	 * @param model {@link Model}
	 */
	@RequestMapping("sitemap.xml")
	public String sitemap(HttpServletRequest request, Model model){
		SImpleSiteVO simpleSiteVO = getCurrentSimpleSite(request);
		
		if(simpleSiteVO.getResult() - SImpleSiteVO.FAILURE == 0){
			return error404();
		}else{
			//访问日志记录
			alonePageRequestLog(request, "sitemap.xml", simpleSiteVO);
			TextBean textBean = GainSource.get(simpleSiteVO.getSimpleSite(), "sitemap.xml");
			String sitemapXml = textBean.getText();
			if(sitemapXml == null || sitemapXml.length() == 0){
				return error404();
			}else{
				model.addAttribute("html", sitemapXml);
				return "domain/display";
			}
		}
	}
	
	/**
	 * robots.txt展示
	 * @param request {@link HttpServletRequest}
	 * @param model {@link Model}
	 */
	@RequestMapping("robots.txt")
	public String robots(HttpServletRequest request, HttpServletResponse response, Model model){
		SImpleSiteVO simpleSiteVO = getCurrentSimpleSite(request);
		
		if(simpleSiteVO.getResult() - SImpleSiteVO.FAILURE == 0){
			return error404();
		}else{
			//访问日志记录
			alonePageRequestLog(request, "robots.txt", simpleSiteVO);
			TextBean textBean = GainSource.get(simpleSiteVO.getSimpleSite(), "robots.txt");
			String content = textBean.getText();
			if(content == null || content.length() == 0){
				return error404();
			}else{
				model.addAttribute("html", content);
				return "domain/robots";
			}
		}
	}
	
	/**
	 * 单独请求的页面，如 robots.txt 、 sitemap.xml 进行日志记录
	 * @param requestFileName 传入如 robots.txt、 sitemap.xml
	 * @param simpleSiteVO
	 */
	private void alonePageRequestLog(HttpServletRequest request, String requestFileName, SImpleSiteVO simpleSiteVO){
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setHtmlFile(requestFileName);
		requestInfo.setIp(IpUtil.getIpAddress(request));
		requestInfo.setReferer(request.getHeader("referer"));
		requestInfo.setServerName(request.getServerName());
		requestInfo.setTime(DateUtil.timeForUnix10());
		requestInfo.setUserAgent(request.getHeader("User-Agent"));
		
		Log.requestLog(request, requestInfo, simpleSiteVO);
	}
	
	/**
	 * 获取当前用户访问的域名对应的站点
	 * @param request 可get传入 domain 模拟访问的域名。可传入自己绑定的域名，也可传入二级域名。如传入leiwen.wang.market
	 * @return
	 */
	private SImpleSiteVO getCurrentSimpleSite(HttpServletRequest request){
		//取得要访问的域名
		String serverName = request.getParameter("domain");	//get传入的域名，如 pc.wang.market
		if(serverName == null || serverName.length() == 0){
			//get没传入，那么取当前访问的url的域名
			serverName = request.getServerName();	//访问域名，如 pc.wang.market
		}
		
		//先从session缓存中取
		SImpleSiteVO vo = (SImpleSiteVO) request.getSession().getAttribute("SImpleSiteVO");
//		if(serverName != null && vo != null && !vo.getServerName().equalsIgnoreCase(serverName)){
//			//当get传入的domain有值，且值跟之前缓存的simpleSiteVO的值不对应，那么应该是代理商在用，在编辑多个网站。之前的网站退出了，又上了一个网站，正在预览当前的网站。那么清空之前的网站再session的缓存，重新进行缓存
//			vo = null;
//		}
		//如果session缓存中有，直接将session的返回
		if(vo != null) {
			vo.setSourceBySession(true);
			return vo;
		}
		
		/****** session中没有，那么从map中读取 ******/
		vo = new SImpleSiteVO();
		
		if(serverName == null || serverName.length() == 0){
			serverName = request.getServerName();	//访问域名，如 pc.wang.market
		}
		vo.setServerName(serverName);
		SimpleSite simpleSite = null;
		
		//内部调试使用，本地
		if(serverName.equals("localhost") || serverName.equals("127.0.0.1")){
			//模拟一个站点提供访问
//			simpleSite = new SimpleSite();
//			simpleSite.setBindDomain(serverName);
//			simpleSite.setClient(Site.CLIENT_CMS);
//			simpleSite.setDomain(serverName);
//			simpleSite.setSiteid(219);
//			simpleSite.setId(simpleSite.getSiteid());
//			simpleSite.setState(Site.STATE_NORMAL);
//			simpleSite.setTemplateId(1);
		}else{
			//正常使用，从域名缓存中找到对应的网站
			
			//判断当前访问域名是否是使用的二级域名
			String twoDomain = null;	
			for (int i = 0; i < G.getAutoAssignDomain().length; i++) {
				if(serverName.indexOf("."+G.getAutoAssignDomain()[i]) > -1){
					twoDomain = serverName.replace("."+G.getAutoAssignDomain()[i], "");
				}
			}
			
			if(twoDomain != null){
				//用的二级域名
				simpleSite = G.getDomain(twoDomain);
			}
			if(simpleSite == null){
				//如果没有使用二级域名、或者是二级域名，但在二级域名里面，没找到，那么就从自己绑定的域名里面找
				simpleSite = G.getBindDomain(serverName);
			}
		}
		
		if(simpleSite == null){
			vo.setBaseVO(SImpleSiteVO.FAILURE, "网站没发现，过会在来看看吧");
			return vo;
		}
		vo.setSimpleSite(simpleSite);
		
		//将获取到的加入Session
		request.getSession().setAttribute("SImpleSiteVO", vo);
		return vo;
	}
	
	/**
	 * 替换HTML标签
	 * @param simpleSite
	 * @param html
	 */
	public String replaceHtmlTag(SimpleSite simpleSite, String html){
		//替换掉 data目录下的缓存js文件
		html = html.replaceAll("src=\"data/", "src=\""+AttachmentUtil.netUrl()+"site/"+simpleSite.getId()+"/data/");	
		//替换图片文件
		html = html.replaceAll("src=\"news/", "src=\""+AttachmentUtil.netUrl()+"site/"+simpleSite.getId()+"/news/");
		//替换掉HTML的注释 <!-- -->
		//html = html.replaceAll("<!--(.*?)-->", "");
		//替换掉JS的注释 /**/
		html = html.replaceAll("/\\*(.*?)\\*/", "");
		return html;
	}
	
}
