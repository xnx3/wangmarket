package com.xnx3.wangmarket.plugin.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.j2ee.service.ApiService;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.service.TemplateService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;
import com.xnx3.wangmarket.agencyadmin.util.SessionUtil;
import com.xnx3.wangmarket.plugin.api.service.KeyManageService;
import com.xnx3.wangmarket.plugin.api.vo.UserBeanVO;

/**
 * 站点相关
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/api/")
public class SiteApiPluginController extends com.xnx3.wangmarket.admin.controller.BaseController {
	@Resource
	private ApiService apiService;
	@Resource
	private SqlService sqlService;
	@Resource
	private TemplateService templateService;
	@Resource
	private KeyManageService keyManageService;


	/**
	 * 通过res.weiunity.com的CDN获取制定的模版，远程获取模版文件，将当前网站应用此模版。
	 * <br/>模版文件包含模版页面，模版变量、栏目
	 */
	@RequestMapping(value="remoteImportTemplate${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO remoteImportTemplate(HttpServletRequest request, Model model,
			@RequestParam(value = "key", required = false , defaultValue="") String key,
			@RequestParam(value = "templateName", required = false , defaultValue="") String templateName){
		//key安全校验
		UserBeanVO vo = keyManageService.verify(key);
		if(vo.getResult() - UserBeanVO.FAILURE == 0){
			return error(vo.getInfo());
		}
		
		if(templateName.length() == 0){
			return error("请选择要远程获取的模版");
		}
		
		SessionUtil.setUser(vo.getUser());
		SessionUtil.setSite(vo.getSite());
		SessionUtil.setAgency(vo.getAgency());
		
		HttpUtil http = new HttpUtil(HttpUtil.UTF8);
		HttpResponse hr = http.get(G.RES_CDN_DOMAIN+"template/"+templateName+"/template.wscso");
		if(hr.getCode() - 404 == 0){
			return error("模版不存在");
		}
		
		BaseVO beanVO = templateService.importTemplate(hr.getContent(), true, request);
		if(beanVO.getResult() - BaseVO.SUCCESS == 0){
			//导入完毕后，还要刷新当前的模版页面、模版变量缓存。这里清空缓存，下次使用时从新从数据库加载最新的
			request.getSession().setAttribute("templatePageListVO", null);
			SessionUtil.setTemplateVarCompileDataMap(null);
			SessionUtil.setTemplateVarMapForOriginal(null);
			
			ActionLogUtil.insertUpdateDatabase(request, "云端导入模版文件成功！");
		}
		return beanVO;
	}

}
