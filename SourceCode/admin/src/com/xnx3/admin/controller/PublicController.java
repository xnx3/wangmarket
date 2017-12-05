package com.xnx3.admin.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.admin.G;
import com.xnx3.admin.service.NewsService;
import com.xnx3.admin.service.SiteColumnService;
import com.xnx3.admin.service.SiteService;
import com.xnx3.admin.service.TemplateService;
import com.xnx3.admin.util.AliyunLog;
import com.xnx3.admin.vo.CloudTemplateListVO;
import com.xnx3.j2ee.service.SqlService;

@Controller
@RequestMapping("/")
public class PublicController extends BaseController{
	@Resource
	private SqlService sqlService;
	@Resource
	private NewsService newsService;
	@Resource
	private SiteService siteService;
	@Resource
	private SiteColumnService siteColumnService;
	@Resource
	private TemplateService templateService;
	
	/**
	 * 云端模版，模版列表，返回JSON，将现有的云端模版返回
	 * @return
	 */
	@RequestMapping("cloudTemplateList")
	@ResponseBody
	public CloudTemplateListVO cloudTemplateList(){
		AliyunLog.addActionLog(getSiteId(), "获取云端模版，模版列表数据");
		return G.cloudTemplateListVO;
	}
	
}
