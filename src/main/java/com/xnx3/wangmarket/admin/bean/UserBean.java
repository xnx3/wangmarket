package com.xnx3.wangmarket.admin.bean;

import java.util.HashMap;
import java.util.Map;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.util.SessionUtil;
import com.xnx3.wangmarket.admin.vo.TemplateVarVO;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;
import com.xnx3.wangmarket.agencyadmin.entity.AgencyData;

/**
 * 用户登录后，即跟随用户Session一块的缓存，其在Shiro中
 * @author 管雷鸣
 * @deprecated 已废弃。可以直接使用 {@link SessionUtil} 来获取响应信息。此保留着只是为了适配之前的一些插件
 */
public class UserBean {
	private Agency myAgency;		//我的代理信息，如果我是代理的话，才有内容
	private AgencyData myAgencyData;	//我的代理信息-变长表的信息
	private Site site;		//当前用户的站点信息，当前用户所能管理的网站信息。如果当前用户是网站用户的话。
	private Agency parentAgency;	//我的上级代理信息，当前用户的上级代理信息
	private AgencyData parentAgencyData;	//我的上级代理信息-变长表的信息
	private Map<String, String> templateVarCompileDataMap;	//我当前高级模式使用的模版变量，可能是已被编译(替换)过标签的内容了。key:template.name	value:模版变量的内容
	private Map<String, TemplateVarVO> templateVarMapForOriginal;	//原始的模版变量，其内包含模版变量的数据库中的原始内容. key:templateVar.name 
	private Map<Integer, SiteColumn> siteColumnMap;		//缓存的当前用户的栏目信息 key:siteColumn.id（CMS模式才会使用此缓存）登录时不会缓存此处，在使用时才会缓存
	private Map<Integer, InputModel> inputModelMap;	//当前CMS网站的输入模型，由 inputModelService 初始化赋予值。在用户进入内容管理，编辑时才会判断，如果此为null，才会从数据库加载数据
	private Map<String, String> siteMenuRole;	//网站管理后台的左侧菜单使用权限，只限网站用户有效，v4.9版本增加。 key: id，也就是左侧菜单的唯一id标示，比如模版管理是template，生成整站是 shengchengzhengzhan， 至于value，无意义，1即可 
	private Map<String, Object> pluginDataMap;	// 缓存 功能插件 的一些信息。比如关键词插件，用户登录成功后，当编辑过一篇文章时，将关键词缓存到这里，在编辑其他文章时直接从这里取数据即可 。 key: 插件的id,如 keyword   value：插件存储的数据
	
	public Agency getMyAgency() {
		return myAgency;
	}
	public void setMyAgency(Agency myAgency) {
		this.myAgency = myAgency;
	}
	public Agency getParentAgency() {
		return parentAgency;
	}
	public void setParentAgency(Agency parentAgency) {
		this.parentAgency = parentAgency;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}

	public Map<Integer, SiteColumn> getSiteColumnMap() {
		return siteColumnMap;
	}
	public void setSiteColumnMap(Map<Integer, SiteColumn> siteColumnMap) {
		this.siteColumnMap = siteColumnMap;
	}
	public Map<Integer, InputModel> getInputModelMap() {
		return inputModelMap;
	}
	public void setInputModelMap(Map<Integer, InputModel> inputModelMap) {
		this.inputModelMap = inputModelMap;
	}
	public Map<String, TemplateVarVO> getTemplateVarMapForOriginal() {
		return templateVarMapForOriginal;
	}
	public void setTemplateVarMapForOriginal(
			Map<String, TemplateVarVO> templateVarMapForOriginal) {
		this.templateVarMapForOriginal = templateVarMapForOriginal;
	}
	public Map<String, String> getTemplateVarCompileDataMap() {
		return templateVarCompileDataMap;
	}
	public void setTemplateVarCompileDataMap(
			Map<String, String> templateVarCompileDataMap) {
		this.templateVarCompileDataMap = templateVarCompileDataMap;
	}
	
	public AgencyData getMyAgencyData() {
		return myAgencyData;
	}
	public void setMyAgencyData(AgencyData myAgencyData) {
		this.myAgencyData = myAgencyData;
	}
	public AgencyData getParentAgencyData() {
		return parentAgencyData;
	}
	public void setParentAgencyData(AgencyData parentAgencyData) {
		this.parentAgencyData = parentAgencyData;
	}
	
	public Map<String, String> getSiteMenuRole() {
		if(siteMenuRole == null){
			siteMenuRole = new HashMap<String, String>();
		}
		return siteMenuRole;
	}
	public void setSiteMenuRole(Map<String, String> siteMenuRole) {
		this.siteMenuRole = siteMenuRole;
	}
	public Map<String, Object> getPluginDataMap() {
		if(pluginDataMap == null){
			pluginDataMap = new HashMap<String, Object>();
		}
		return pluginDataMap;
	}
	public void setPluginDataMap(Map<String, Object> pluginDataMap) {
		this.pluginDataMap = pluginDataMap;
	}
	
}
