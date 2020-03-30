package com.xnx3.wangmarket.admin.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.bean.SiteVarBean;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteVar;
import com.xnx3.wangmarket.admin.entity.TemplateVar;
import com.xnx3.wangmarket.admin.entity.TemplateVarData;
import com.xnx3.wangmarket.admin.service.SiteVarService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;

import net.sf.json.JSONObject;

/**
 * 网站的全局变量
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/siteVar/")
public class SiteVarController extends com.xnx3.wangmarket.admin.controller.BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteVarService siteVarService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list${url.suffix}")
	public String list(HttpServletRequest request ,Model model){
		ActionLogUtil.insert(request, "查看网站全局变量列表");
		JSONObject json = siteVarService.getVar(getSiteId());
		
		//将json转化为list形式
		List<SiteVarBean> list = new ArrayList<SiteVarBean>();
		Iterator<String> iter = json.keys();
        while (iter.hasNext()) {
        	String key = iter.next();
            SiteVarBean bean = new SiteVarBean();
            bean.setName(key);
            bean.setDescription(json.getJSONObject(key).getString("description"));
            bean.setValue(json.getJSONObject(key).getString("value"));
            list.add(bean);
        }
		
		model.addAttribute("list", list);
		return "siteVar/list";
	}
	

	/**
	 * 新增、编辑页面
	 */
	@RequestMapping("/edit${url.suffix}")
	public String edit(HttpServletRequest request ,Model model,
			@RequestParam(value = "name", required = false , defaultValue="") String name){
		if(name.trim().length() > 0){
			//修改
			JSONObject json = siteVarService.getVar(getSiteId());
			model.addAttribute("json", json);
			ActionLogUtil.insert(request, "打开修改网站全局变量页面", StringUtil.filterXss(name));
		}else{
			//新增
			ActionLogUtil.insert(request, "打开增加网站全局变量页面");
		}
		
		return "siteVar/edit";
	}
	
	
	/**
	 * 保存某个模板变量
	 */
	@RequestMapping(value="save${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO save(TemplateVar templateVarInput, 
			@RequestParam(value = "name", required = false , defaultValue="") String name,
			@RequestParam(value = "description", required = false , defaultValue="") String description,
			@RequestParam(value = "value", required = false , defaultValue="") String value,
			HttpServletRequest request,Model model){
		Site site = getSite();
		SiteVar siteVar = sqlService.findById(SiteVar.class, site.getId());
		if(siteVar == null){
			siteVar = new SiteVar();
			siteVar.setId(site.getId());
		}
		JSONObject json = JSONObject.fromObject(siteVar.getText());
		
		JSONObject varJson = new JSONObject();
		varJson.put("description", description);
		varJson.put("value", value);
		json.put(name, varJson);
		
		//保存到数据库
		siteVar.setText(json.toString());
		sqlService.save(siteVar);
		
		//更新缓存
		siteVarService.setVar(site.getId(), siteVar);
		
		return success();
	}
	
	
}
