package com.xnx3.admin.vo;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.func.Safety;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.admin.Func;
import com.xnx3.admin.G;
import com.xnx3.admin.entity.InputModel;
import com.xnx3.admin.entity.News;
import com.xnx3.admin.entity.NewsData;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.entity.SiteColumn;
import com.xnx3.admin.entity.TemplatePageData;
import com.xnx3.admin.entity.TemplateVarData;
import com.xnx3.admin.vo.bean.template.TemplatePage;
import com.xnx3.admin.vo.bean.template.TemplateVar;

/**
 * 模版页面，模版导入，将导入的字符串转化为json，然后将json转化为此对象
 * @author 管雷鸣
 */
public class TemplateVO extends BaseVO {
	private Site currentSite;	//当前用户的站点信息
	private String text;		//导入的模版的text内容，字符串
	private List<TemplatePage> templatePageList;		//模版页面
	private List<TemplateVar> templateVarList;		//模版变量
	private List<InputModel> inputModelList;	//输入模型
	private List<SiteColumn> siteColumnList;	//栏目
	
	private String systemVersion;	// 当前系统版本号
	private int time;	//导出的时间，10为时间戳
	private String templateName;	//当前模版的名字
	private String sourceUrl;	//模版来源的网站，从那个网站导出来的，可以作为预览网站
	
	
	public Site getCurrentSite() {
		return currentSite;
	}
	public void setCurrentSite(Site currentSite) {
		this.currentSite = currentSite;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<TemplatePage> getTemplatePageList() {
		return templatePageList;
	}
	public void setTemplatePageList(List<TemplatePage> templatePageList) {
		this.templatePageList = templatePageList;
	}
	public List<TemplateVar> getTemplateVarList() {
		return templateVarList;
	}
	public void setTemplateVarList(List<TemplateVar> templateVarList) {
		this.templateVarList = templateVarList;
	}
	public List<InputModel> getInputModelList() {
		return inputModelList;
	}
	public void setInputModelList(List<InputModel> inputModelList) {
		this.inputModelList = inputModelList;
	}
	public List<SiteColumn> getSiteColumnList() {
		return siteColumnList;
	}
	public void setSiteColumnList(List<SiteColumn> siteColumnList) {
		this.siteColumnList = siteColumnList;
	}
	
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	
	/**
	 * 导入模版内容。会自动赋予当前网站、来源、版本号、模版变量、模版页、栏目、输入模型等
	 * @param text 模版内容，字符串
	 * @return true：导入成功
	 */
	public boolean importText(String text) {
		//当前登录用户的站点信息
		currentSite = Func.getCurrentSite();	
		
		this.text = text;
		
		//将导入的模版转化为JSON对象
		JSONObject jo = JSONObject.fromObject(text);
		
		//TemplatePage模版页
		JSONArray templatePageArray = jo.getJSONArray("templatePageList");
		templatePageList = new ArrayList<TemplatePage>();
		for (int i = 0; i < templatePageArray.size(); i++) {
			JSONObject j = templatePageArray.getJSONObject(i);
			com.xnx3.admin.entity.TemplatePage ntp = new com.xnx3.admin.entity.TemplatePage();
			ntp.setName(Safety.filter(j.getString("name")));
			ntp.setSiteid(currentSite.getId());
			ntp.setTemplateName(Safety.filter(jo.getString("templateName")));
			ntp.setType((short) j.getInt("type"));
			ntp.setUserid(currentSite.getUserid());
			if(j.get("remark") != null){
				//兼容之前没有remark导出的json数据
				ntp.setRemark(Safety.filter(j.getString("remark")));
			}
			
			TemplatePage tp = new TemplatePage();
			tp.setTemplatePage(ntp);
			tp.setText(j.getString("text"));
			
			templatePageList.add(tp);
		}
		

		//创建TemplateVar模版变量
		JSONArray templateVarArray = jo.getJSONArray("templateVarList");
		templateVarList = new ArrayList<TemplateVar>();
		for (int i = 0; i < templateVarArray.size(); i++) {
			JSONObject j = templateVarArray.getJSONObject(i);
			com.xnx3.admin.entity.TemplateVar tv = new com.xnx3.admin.entity.TemplateVar();
			tv.setAddtime(DateUtil.timeForUnix10());
			tv.setRemark(Safety.filter(j.getString("remark")));
			tv.setTemplateName(Safety.filter(jo.getString("templateName")));
			tv.setUpdatetime(tv.getAddtime());
			tv.setUserid(currentSite.getUserid());
			tv.setVarName(Safety.filter(j.getString("var_name")));
			tv.setSiteid(currentSite.getId());
			
			TemplateVar t = new TemplateVar();
			t.setTemplateVar(tv);
			t.setText(j.getString("text"));
			
			templateVarList.add(t);
		}
		

		//导入自定义输入模型
		inputModelList = new ArrayList<InputModel>();
		if(jo.get("inputModelList") == null){
//			System.out.println("没有输入模型，忽略");
		}else{
			//有输入模型，那么要导入进去
			JSONArray inputModelArray = jo.getJSONArray("inputModelList");
			for (int i = 0; i < inputModelArray.size(); i++) {
				JSONObject j = inputModelArray.getJSONObject(i);
				InputModel im = new InputModel();
				im.setCodeName(Safety.filter(j.getString("codeName")));
				im.setRemark(Safety.filter(j.getString("remark")));
				im.setSiteid(currentSite.getId());
				im.setText(j.getString("text"));
				inputModelList.add(im);
			}
		}
		
		//拿到模版网站下所有可用的栏目
		JSONArray siteColumnArray = jo.getJSONArray("siteColumnList");
		siteColumnList = new ArrayList<SiteColumn>();
		for (int i = 0; i < siteColumnArray.size(); i++) {
			JSONObject j = siteColumnArray.getJSONObject(i);	//要复制的目标栏目
			Short type = (short) j.getInt("type");
			
			//创建栏目，将栏目复制一份，再当前网站创建栏目
			SiteColumn nsc = new SiteColumn();
			nsc.setName(Safety.filter(j.getString("name")));
			nsc.setRank(j.getInt("rank"));
			nsc.setUsed((short) j.getInt("used"));
			nsc.setSiteid(currentSite.getId());
			nsc.setUserid(currentSite.getUserid());
			nsc.setType(type);
			nsc.setTemplatePageListName(Safety.filter(j.getString("templatePageListName")));
			nsc.setTemplatePageViewName(Safety.filter(j.getString("templatePageViewName")));
			nsc.setCodeName(Safety.filter(j.getString("codeName")));
			nsc.setParentCodeName(Safety.filter(j.getString("parentCodeName")));
			nsc.setListNum(j.getInt("listNum"));
			nsc.setEditMode((short) (j.get("editMode") == null ? 0:j.getInt("editMode")));
			if(j.get("inputModelCodeName") != null){
				//兼容之前没有输入模型导出的模板
				nsc.setInputModelCodeName(Safety.filter(j.getString("inputModelCodeName")));
			}
			
			siteColumnList.add(nsc);
		}
		
		try {
			templateName = Safety.filter(jo.getString("templateName"));
			systemVersion = Safety.filter(jo.getString("systemVersion"));
			time = jo.getInt("time");
			sourceUrl = Safety.filter(jo.getString("sourceUrl"));
		} catch (Exception e) {
			e.printStackTrace();
			if(templateName == null){
				templateName = "";
			}
			if(systemVersion == null){
				systemVersion = "";
			}
			if(sourceUrl == null){
				sourceUrl = "";
			}
		}
		
		return true;
	}
}
