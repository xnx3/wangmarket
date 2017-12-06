package com.xnx3.domain.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.aliyun.common.utils.IOUtils;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.oss.OSSException;
import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.j2ee.util.TerminalDetection;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.net.OSSUtil;
import com.xnx3.admin.entity.Site;
import com.xnx3.domain.G;
import com.xnx3.domain.bean.RequestLog;
import com.xnx3.domain.bean.SimpleSite;
import com.xnx3.domain.vo.SImpleSiteVO;

/**
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class PublicController_ extends BaseController {
	private HttpUtil http = new HttpUtil(HttpUtil.UTF8);
	
	@Resource
	private SqlService sqlService;

	/**
	 * 域名捕获转发
	 * @param htmlFile 访问的html文件，如访问c202.html ，则传入c202，会自动拼接上.html
	 */
	@RequestMapping("dns.do")
	public String dns(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(value = "htmlFile", required = false , defaultValue="") String htmlFile){
		SImpleSiteVO simpleSiteVO = getCurrentSimpleSite(request);
		
		//判断访问的站点的哪个文件
		if(htmlFile.length() == 0){
			htmlFile = "index";
		}
		htmlFile = htmlFile + ".html";
		
		//访问日志记录
		requestLog(request, htmlFile);
		
		if(simpleSiteVO.getResult() - SImpleSiteVO.FAILURE == 0){
			return error404();
		}else{
			//判断网站的状态，冻结的网站将无法访问
			SimpleSite simpleSite = simpleSiteVO.getSimpleSite();
			if(simpleSite.getState() != null){
				if(simpleSite.getState() - Site.STATE_FREEZE == 0){
					//2为冻结，暂停，此时访问网站会直接到冻结的提示页面
					model.addAttribute("url", simpleSiteVO.getServerName()+"/"+htmlFile);
					return "domain/pause";
				}
			}
			
			com.aliyun.oss.model.OSSObject ossObj = null;
			try{
				ossObj = OSSUtil.getOSSClient().getObject(OSSUtil.bucketName, "site/"+simpleSite.getId()+"/"+htmlFile);
			}  catch (OSSException e) {
				return "domain/404";
			}
			
			InputStream in = ossObj.getObjectContent();
			String html = null;
			try {
				html = IOUtils.readStreamAsString(in, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			html = html.replaceAll("(?m)^\\s*$"+System.lineSeparator(), "");
			
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
			html = html.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", ""); 
			//加载在线客服
			int time = DateUtil.timeForUnix10();
			html = html + ""
					+ "<script src=\"http://res.weiunity.com/js/fun.js\"></script>"
					+ "<script src=\""+OSSUtil.url+"/site/"+simpleSite.getId()+"/data/site.js?v="+time+"\"></script>"
					+ "<script src=\"http://res.weiunity.com/js/im/site_kefu.js\"></script>"
					+ "";
			model.addAttribute("html", html);
			
			//判断此网站的类型，是PC端还是手机端
			if(simpleSite.getClient() - Site.CLIENT_WAP == 0){
				//手机端
				//如果是电脑浏览的，会显示经过处理的电脑页面，有个手机模拟器
				//如果是手机访问的，也是使用二级域名进行访问
				boolean isMobile = TerminalDetection.checkMobileOrPc(request);
				if(!isMobile){
					model.addAttribute("url", OSSUtil.url+"site/"+simpleSite.getId()+"/"+htmlFile);
					return "domain/pcPreview";
				}
			}
			return "domain/pc";
		}
	}
	
	private void requestLog(HttpServletRequest request, String htmlFile){
		//进行记录访问日志，记录入Session
		RequestLog requestLog = (RequestLog) request.getSession().getAttribute("requestLog");
		if(requestLog == null){
			requestLog = new RequestLog();
			requestLog.setIp(IpUtil.getIpAddress(request));
			requestLog.setServerName(request.getServerName());
			
			SImpleSiteVO vo = (SImpleSiteVO) request.getSession().getAttribute("SImpleSiteVO");
			if(vo != null){
				requestLog.setSiteid(vo.getSimpleSite().getId());
			}
			
			request.getSession().setAttribute("requestLog", requestLog);
		}
		
		LogItem logItem = new LogItem(DateUtil.timeForUnix10());
		logItem.PushBack("ip", requestLog.getIp());
		logItem.PushBack("referer", request.getHeader("referer"));
		logItem.PushBack("userAgent", request.getHeader("User-Agent"));
		logItem.PushBack("htmlFile", htmlFile);
		logItem.PushBack("siteid", requestLog.getSiteid()+"");
		requestLog.getLogGroup().add(logItem);
		
		if(requestLog.getLogGroup().size() > 1000){
			try {
				G.aliyunLogUtil.saveByGroup(requestLog.getServerName(), requestLog.getIp(), requestLog.getLogGroup());
				requestLog.setLogGroup(new Vector<LogItem>());
			} catch (LogException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * sitemap.xml展示
	 * @param request {@link HttpServletRequest}
	 * @param model {@link Model}
	 */
	@RequestMapping("sitemap.do")
	public String sitemap(HttpServletRequest request, Model model){
		SImpleSiteVO simpleSiteVO = getCurrentSimpleSite(request);
		if(simpleSiteVO.getResult() - SImpleSiteVO.FAILURE == 0){
			return error404();
		}else{
			//访问日志记录
			requestLog(request, "sitemap.xml");
			
			HttpResponse hr = http.get(OSSUtil.url+"site/"+simpleSiteVO.getSimpleSite().getId()+"/sitemap.xml");
			if(hr.getCode() - 404 == 0){
				return error404();
			}else{
				model.addAttribute("html", hr.getContent());
				return "domain/sitemap";
			}
		}
	}
	
	/**
	 * 获取当前用户访问的域名对应的站点
	 * @return
	 */
	private SImpleSiteVO getCurrentSimpleSite(HttpServletRequest request){
		//当前访问域名的对应站点，先从Session中拿
		SImpleSiteVO vo = (SImpleSiteVO) request.getSession().getAttribute("SImpleSiteVO");
		
		if(vo == null){
			//Session中没有SImpleSiteVO ，第一次用，那就找内存中的吧
			vo = new SImpleSiteVO();
			
			String serverName = request.getServerName();	//访问域名，如 pc.wang.market
			//内部调试使用
			if(serverName.equals("localhost")){
				serverName = "cs."+G.twoDomainArray[0];
			}
			vo.setServerName(serverName);
			
			//判断当前访问域名是否是使用的二级域名
			String twoDomain = null;		
			for (int i = 0; i < G.twoDomainArray.length; i++) {
				if(serverName.indexOf("."+G.twoDomainArray[i]) > -1){
					twoDomain = serverName.replace("."+G.twoDomainArray[i], "");
				}
			}
			
			SimpleSite simpleSite = null;
			if(twoDomain != null){
				//用的二级域名
				simpleSite = G.getDomain(twoDomain);
			}else{
				//自己绑定的域名
				simpleSite = G.getBindDomain(serverName);
			}
			
			if(simpleSite == null){
				vo.setBaseVO(SImpleSiteVO.FAILURE, "网站没发现，过会在来看看吧");
				return vo;
			}
			
			vo.setSimpleSite(simpleSite);
			//计算获取OSS的路径域名(外网下的)
//			vo.setOssUrl(G.ossUrl+"site/"+simpleSite.getId()+"/");
			//将获取到的加入Session
			request.getSession().setAttribute("SImpleSiteVO", vo);
		}
		
		return vo;
	}
	
	/**
	 * 替换HTML标签
	 * @param simpleSite
	 * @param html
	 */
	public String replaceHtmlTag(SimpleSite simpleSite, String html){
		//替换掉 data目录下的缓存js文件
		html = html.replaceAll("src=\"data/", "src=\""+OSSUtil.url+"site/"+simpleSite.getId()+"/data/");	
		//替换图片文件
		html = html.replaceAll("src=\"news/", "src=\""+OSSUtil.url+"site/"+simpleSite.getId()+"/news/");
		//替换掉HTML的注释 <!-- -->
		html = html.replaceAll("<!--(.*?)-->", "");
		//替换掉JS的注释 /**/
		html = html.replaceAll("/\\*(.*?)\\*/", "");
		return html;
	}
	
}
