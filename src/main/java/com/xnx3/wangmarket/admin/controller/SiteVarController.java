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
		
        ActionLogUtil.insert(request, "查看网站全局变量列表");
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
			JSONObject json = siteVarService.getVar(getSiteId(), name);
			model.addAttribute("siteVar", new SiteVarBean(name, json));
			ActionLogUtil.insert(request, "打开修改网站全局变量页面", StringUtil.filterXss(name));
		}else{
			//新增
			ActionLogUtil.insert(request, "打开增加网站全局变量页面");
		}
		
		return "siteVar/edit";
	}
	
	
	/**
	 * 保存某个模板变量
	 * @param updateName 如果当前是修改的某个变量，那么这里时修改前，变量的名字，以此来判断是否修改过变量的名字。如果这里没有任何东西，那就是新增了
	 * @param name 修改后的变量的名字
	 * @param description 修改后的变量的描述
	 * @param value 修改后的变量的值
	 */
	@RequestMapping(value="save${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO save(
			@RequestParam(value = "updateName", required = false , defaultValue="") String updateName,
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
		
		JSONObject varJson = null;
		//判断是修改还是新增
		if(updateName.length() == 0){
			//新增
			varJson = new JSONObject();
		}else{
			//修改
			if(!updateName.equals(name.trim())){
				//修改了变量名字了，那么要把之前的变量删掉，不然会变成两个变量了
				json.remove(updateName);
				varJson = new JSONObject();
			}else{
				//updateName == name ，那么只是修改了value、或者描述
				varJson = json.getJSONObject(updateName);
			}
		}
		
		varJson.put("description", description);
		varJson.put("value", value);
		json.put(name, varJson);
		
		//保存到数据库
		siteVar.setText(json.toString());
		sqlService.save(siteVar);
		
		//更新缓存
		siteVarService.setVar(site.getId(), siteVar);
		
		ActionLogUtil.insertUpdateDatabase(request, "保存全局变量", StringUtil.filterXss(name+", "+value));
		return success();
	}
	
	/**
	 * 删除变量
	 * @param name 要删除的全局变量的name
	 */
	@RequestMapping(value="deleteVar${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO deleteVar(HttpServletRequest request,
			@RequestParam(value = "name", required = true) String name){
		Site site = getSite();
		SiteVar siteVar = sqlService.findById(SiteVar.class, site.getId());
		if(siteVar == null){
			siteVar = new SiteVar();
			siteVar.setId(site.getId());
		}
		JSONObject json = JSONObject.fromObject(siteVar.getText());
		json.remove(name);
		
		//保存到数据库
		siteVar.setText(json.toString());
		sqlService.save(siteVar);
		
		//更新缓存
		siteVarService.setVar(site.getId(), siteVar);
		
		ActionLogUtil.insertUpdateDatabase(request, "删除全局变量", StringUtil.filterXss(name));
		return success();
	}
	
}
