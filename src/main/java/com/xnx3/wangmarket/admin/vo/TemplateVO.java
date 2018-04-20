package com.xnx3.wangmarket.admin.vo;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.func.Safety;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.entity.TemplatePageData;
import com.xnx3.wangmarket.admin.entity.TemplateVarData;
import com.xnx3.wangmarket.admin.vo.bean.template.TemplatePage;
import com.xnx3.wangmarket.admin.vo.bean.template.TemplateVar;

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
	private String plugin;		//插件模式。如果此不为null，且长度大于1，则视为插件模式，无视templateName，可在已经有模版的CMS模式网站直接导入
	
	//v3.6增加，根据模版中的useUtf8Encode=true来识别。3.6之后的全部采用编码机制
	private boolean isUtf8Encode;	//当前是否使用utf8编码，将汉字转化为utf8字符，避免乱码
	
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
		
		//首先判断是否是使用UTF8将字符串进行编码了，一定在最前面，不然后面的提取字符串是要判断是否使用编码，要解码的
		if(jo.get("useUtf8Encode") != null){
			String utf8Encode = jo.getString("useUtf8Encode");
			if(utf8Encode.equals("true")){
				this.isUtf8Encode = true;
			}
		}
		
		//TemplatePage模版页
		templatePageList = new ArrayList<TemplatePage>();
		if(jo.get("templatePageList") != null){
			JSONArray templatePageArray = jo.getJSONArray("templatePageList");
			for (int i = 0; i < templatePageArray.size(); i++) {
				JSONObject j = templatePageArray.getJSONObject(i);
				com.xnx3.wangmarket.admin.entity.TemplatePage ntp = new com.xnx3.wangmarket.admin.entity.TemplatePage();
				ntp.setName(getJsonStringAndSafetyFilter(j.getString("name")));
				ntp.setSiteid(currentSite.getId());
				ntp.setTemplateName(getJsonStringAndSafetyFilter(jo.getString("templateName")));
				ntp.setType((short) j.getInt("type"));
				ntp.setUserid(currentSite.getUserid());
				if(j.get("remark") != null){
					//兼容之前没有remark导出的json数据
					ntp.setRemark(getJsonStringAndSafetyFilter(j.getString("remark")));
				}
				
				TemplatePage tp = new TemplatePage();
				tp.setTemplatePage(ntp);
				tp.setText(getJsonString(j.getString("text")));
				
				templatePageList.add(tp);
			}
		}
		
		

		//创建TemplateVar模版变量
		templateVarList = new ArrayList<TemplateVar>();
		if(jo.get("templateVarList") != null){
			JSONArray templateVarArray = jo.getJSONArray("templateVarList");
			for (int i = 0; i < templateVarArray.size(); i++) {
				JSONObject j = templateVarArray.getJSONObject(i);
				com.xnx3.wangmarket.admin.entity.TemplateVar tv = new com.xnx3.wangmarket.admin.entity.TemplateVar();
				tv.setAddtime(DateUtil.timeForUnix10());
				tv.setRemark(getJsonStringAndSafetyFilter(j.getString("remark")));
				tv.setTemplateName(getJsonStringAndSafetyFilter(jo.getString("templateName")));
				tv.setUpdatetime(tv.getAddtime());
				tv.setUserid(currentSite.getUserid());
				tv.setVarName(getJsonStringAndSafetyFilter(j.getString("var_name")));
				tv.setSiteid(currentSite.getId());
				
				TemplateVar t = new TemplateVar();
				t.setTemplateVar(tv);
				t.setText(getJsonString(j.getString("text")));
				
				templateVarList.add(t);
			}
		}
		
		

		//导入自定义输入模型
		inputModelList = new ArrayList<InputModel>();
		if(jo.get("inputModelList") != null){
			//有输入模型，那么要导入进去
			JSONArray inputModelArray = jo.getJSONArray("inputModelList");
			for (int i = 0; i < inputModelArray.size(); i++) {
				JSONObject j = inputModelArray.getJSONObject(i);
				InputModel im = new InputModel();
				im.setCodeName(getJsonStringAndSafetyFilter(j.getString("codeName")));
				im.setRemark(getJsonStringAndSafetyFilter(j.getString("remark")));
				im.setSiteid(currentSite.getId());
				im.setText(getJsonString(j.getString("text")));
				inputModelList.add(im);
			}
		}
		
		//拿到模版网站下所有可用的栏目
		siteColumnList = new ArrayList<SiteColumn>();
		if(jo.get("siteColumnList") != null){
			JSONArray siteColumnArray = jo.getJSONArray("siteColumnList");
			for (int i = 0; i < siteColumnArray.size(); i++) {
				JSONObject j = siteColumnArray.getJSONObject(i);	//要复制的目标栏目
				Short type = (short) j.getInt("type");
				
				//创建栏目，将栏目复制一份，再当前网站创建栏目
				SiteColumn nsc = new SiteColumn();
				nsc.setName(getJsonStringAndSafetyFilter(j.getString("name")));
				nsc.setRank(j.getInt("rank"));
				nsc.setUsed((short) j.getInt("used"));
				nsc.setSiteid(currentSite.getId());
				nsc.setUserid(currentSite.getUserid());
				nsc.setType(type);
				nsc.setTemplatePageListName(getJsonStringAndSafetyFilter(j.getString("templatePageListName")));
				nsc.setTemplatePageViewName(getJsonStringAndSafetyFilter(j.getString("templatePageViewName")));
				nsc.setCodeName(getJsonStringAndSafetyFilter(j.getString("codeName")));
				nsc.setParentCodeName(getJsonStringAndSafetyFilter(j.getString("parentCodeName")));
				nsc.setListNum(j.getInt("listNum"));
				nsc.setEditMode((short) (j.get("editMode") == null ? 0:j.getInt("editMode")));
				if(j.get("inputModelCodeName") != null){
					//兼容之前没有输入模型导出的模板
					nsc.setInputModelCodeName(getJsonStringAndSafetyFilter(j.getString("inputModelCodeName")));
				}
				
				siteColumnList.add(nsc);
			}
		}
		
		//是否是插件模式
		if(jo.get("plugin") != null){
			plugin = getJsonStringAndSafetyFilter(jo.getString("plugin"));
		}
		
		if(jo.get("templateName") != null){
			templateName = getJsonStringAndSafetyFilter(jo.getString("templateName"));
		}else{
			templateName = "";
		}
		
		if(jo.get("systemVersion") != null){
			systemVersion = getJsonStringAndSafetyFilter(jo.getString("systemVersion"));
		}else{
			systemVersion = "";
		}
		
		if(jo.get("time") != null){
			time = jo.getInt("time");
		}
		
		if(jo.get("sourceUrl") != null){
			sourceUrl = getJsonStringAndSafetyFilter(jo.getString("sourceUrl"));
		}else{
			sourceUrl = "";
		}
		
//		
//		try {
////			templateName = getJsonStringAndSafetyFilter(jo.getString("templateName"));
//			systemVersion = getJsonStringAndSafetyFilter(jo.getString("systemVersion"));
//			time = jo.getInt("time");
//			sourceUrl = getJsonStringAndSafetyFilter(jo.getString("sourceUrl"));
//		} catch (Exception e) {
//			e.printStackTrace();
//			if(templateName == null){
//				templateName = "";
//			}
//			if(systemVersion == null){
//				systemVersion = "";
//			}
//			if(sourceUrl == null){
//				sourceUrl = "";
//			}
//		}
		
		return true;
	}
	
	/**
	 * 将json获取的字符串进行UTF8编码判断，拿到原始字符串。
	 * @param text json中拿到的字符串
	 * @return 将字符串判断是否编码，若编码了，将其解码后输出
	 */
	public String getJsonString(String text){
		if(text == null){
			return "";
		}
		if(this.isUtf8Encode){
			//使用了UTF8编码，那么进行解码
			text = StringUtil.utf8ToString(text);
		}
		
		return text;
	}
	
	/**
	 * 将json取得的字符串进行UTF8编码的判断及解码、并进行安全校验
	 * @param text json取得的字符串
	 * @return
	 */
	public String getJsonStringAndSafetyFilter(String text){
		if(text == null){
			return "";
		}
		
		return Safety.filter(getJsonString(text));
	}
	public String getPlugin() {
		return plugin;
	}
	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}
	
}
