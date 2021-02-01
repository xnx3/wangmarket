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
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.bean.SiteVarBean;
import com.xnx3.wangmarket.admin.cache.TemplateCMS;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteUser;
import com.xnx3.wangmarket.admin.entity.SiteVar;
import com.xnx3.wangmarket.admin.entity.Template;
import com.xnx3.wangmarket.admin.entity.TemplateVar;
import com.xnx3.wangmarket.admin.entity.TemplateVarData;
import com.xnx3.wangmarket.admin.service.SiteVarService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;
import com.xnx3.wangmarket.admin.util.SessionUtil;

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
		Site site = getSite();
		
		//列表中的值，替换公共标签使用
		Template templateEntity = sqlService.findAloneByProperty(Template.class, "name", site.getTemplateName());
		TemplateCMS template = new TemplateCMS(site, templateEntity);
		
		//将json转化为list形式
		List<SiteVarBean> list = new ArrayList<SiteVarBean>();
		Iterator<String> iter = json.keys();
        while (iter.hasNext()) {
        	String key = iter.next();
            SiteVarBean bean = new SiteVarBean();
            JSONObject item = json.getJSONObject(key);
            
            bean.setName(key);
            bean.setDescription(item.getString("description"));
            bean.setValue(template.replacePublicTag(item.getString("value").replaceAll("\r|\n", " ")));
            if(item.get("type") == null){
            	bean.setType(SiteVar.TYPE_TEXT);
			}else{
				bean.setType(item.getString("type"));
			}
            if(item.get("title") == null){
            	bean.setTitle(bean.getDescription());
			}else{
				bean.setTitle(item.getString("title"));
			}
            
            
            if(item.get("valueItems") == null){
            	bean.setValueItems("");
			}else{
				bean.setValueItems("");
				
				//如果当前变量类型是select的，那么还要生成select的js变量，以显示给用户具体的值的描述
				if(item.get("type").equals(SiteVar.TYPE_SELECT)){
					//将其转化为js变量
					String vi = item.getString("valueItems");
					String[] vis = vi.split("\r|\n");
					
					StringBuffer sb = new StringBuffer();
					sb.append("var site_var_"+key+" = new Array(); ");
					if(vis.length > 1 || vis[0].indexOf(":") > 0){
						for (int i = 0; i < vis.length; i++) {
							String[] items = vis[i].split(":");
							if(items.length == 2){
								sb.append("site_var_"+key+"['"+items[0]+"'] = '"+items[1]+"'; ");
							}
						}
					}
					bean.setValueItems(sb.toString());
				}
			}
            
            list.add(bean);
        }
		
        
        SiteUser siteUser = SessionUtil.getSiteUser();
		if(siteUser == null || siteUser.getSiteid() == null){
			//主账号
			model.addAttribute("isSubAccount", "0");	//是否是子账号，不是
		}else{
			//子客户，只能看到修改
			model.addAttribute("isSubAccount", "1");	//是否是子账号,是
		}
        
        ActionLogUtil.insert(request, "查看网站全局变量列表");
		model.addAttribute("list", list);
		return "siteVar/list";
	}
	

	/**
	 * 新增、编辑页面
	 * @param name 要编辑的模板变量的名字
	 * @param editType 用什么方式来编辑这个模板变量。可传入 property:属性设置 、 edit:编辑内容。默认不传则是 edit 编辑内容 
	 */
	@RequestMapping("/edit${url.suffix}")
	public String edit(HttpServletRequest request ,Model model,
			@RequestParam(value = "name", required = false , defaultValue="") String name,
			@RequestParam(value = "editType", required = false , defaultValue="edit") String editType){
		Site site = getSite();
		if(name.trim().length() > 0){
			//修改
			JSONObject json = siteVarService.getVar(site.getId(), name);
			model.addAttribute("siteVar", new SiteVarBean(name, json));
			ActionLogUtil.insert(request, "打开修改网站全局变量页面", StringUtil.filterXss(name));
		}else{
			//新增
			ActionLogUtil.insert(request, "打开增加网站全局变量页面");
		}
		
		//可上传的后缀列表
		model.addAttribute("ossFileUploadImageSuffixList", Global.ossFileUploadImageSuffixList);
		//可上传的文件最大大小(KB)
		model.addAttribute("maxFileSizeKB", AttachmentUtil.getMaxFileSizeKB());
		//设置上传后的图片、附件所在的个人路径
		SessionUtil.setUeUploadParam1(site.getId()+"");
		
		SiteUser siteUser = SessionUtil.getSiteUser();
		if(siteUser == null || siteUser.getSiteid() == null){
			//主账号
		}else{
			//子客户，只能看到修改，那么肯定就是只是编辑内容，不需要编辑属性的
			editType = "edit";
		}
		
		//列表中的值，替换公共标签使用
		Template templateEntity = sqlService.findAloneByProperty(Template.class, "name", site.getTemplateName());
		TemplateCMS template = new TemplateCMS(site, templateEntity);
		model.addAttribute("templatePath", template.getTemplatePath());
		return "siteVar/"+(editType.contentEquals("edit")? "edit":"property");
	}
	
	
	/**
	 * 保存某个模板变量
	 * @param updateName 如果当前是修改的某个变量，那么这里时修改前，变量的名字，以此来判断是否修改过变量的名字。如果这里没有任何东西，那就是新增了
	 * @param name 修改后的变量的名字
	 * @param description 修改后的变量的描述
	 * @param value 修改后的变量的值
	 * @param title 给客户修改时，客户看到的标题，2~6字的那种标题
	 * @param type 数据类型，包含 text（文本框、文本输入）、 image（图片上传，存的是图片url）、select（select下拉框）。  不传默认是 text
	 */
	@RequestMapping(value="save${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO save(
			@RequestParam(value = "updateName", required = false , defaultValue="") String updateName,
			@RequestParam(value = "name", required = false , defaultValue="") String name,
			@RequestParam(value = "description", required = false , defaultValue="") String description,
			@RequestParam(value = "value", required = false , defaultValue="") String value,
			@RequestParam(value = "type", required = false , defaultValue="") String type,
			@RequestParam(value = "title", required = false , defaultValue="") String title,
			@RequestParam(value = "valueItems", required = false , defaultValue="") String valueItems,
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
		if(type.equals(SiteVar.TYPE_IMAGE)){
			varJson.put("type", SiteVar.TYPE_IMAGE);
		}else if(type.equals(SiteVar.TYPE_SELECT)){
			varJson.put("type", SiteVar.TYPE_SELECT);
		}else if(type.equals(SiteVar.TYPE_NUMBER)){
			varJson.put("type", SiteVar.TYPE_NUMBER);
		}else if(type.equals(SiteVar.TYPE_IMAGE_GROUP)){	
			varJson.put("type", SiteVar.TYPE_IMAGE_GROUP);
		}else{
			//如果上面的情况都不是，那么默认就是text文本方式。也是对v5.1版本的兼容
			varJson.put("type", SiteVar.TYPE_TEXT);
		}
		varJson.put("valueItems", valueItems);
		varJson.put("title", title);
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
