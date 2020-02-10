package com.xnx3.wangmarket.domain.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * {@link Site} 的简化
 * @author 管雷鸣
 */
public class SimpleSite implements Serializable{
	private int id;	//site.id ，废弃，用 siteid 代替
	private String domain;
	private String bindDomain;
	private int client;
	private int templateId;
	private Short state;
	private int siteid;	//site.id 
	
	/*
	 * 插件
	 * plugin.get(plugin id)   value：1  判断pluginid是否不为null，若不为null，则是启用了该插件。 value则是自定义存储的一些数据
	 * 
	 * 示例：  在线客服插件 kefu
	 * key："kefu"
	 * value： 
	 * 			key:"templateName"	value:"blackdaqi1"
	 * 			key:"use"			value:1
	 * 
	 */
	private Map<String, Map<String,Object>> plugin;
	
	//读数据库的方式载入 SimpleSite ，在系统刚启动时，会自动读数据库中的站点属性，将其载入，分配好域名
	public SimpleSite(Site site) {
		plugin = new HashMap<String, Map<String,Object>>();
		if(site != null){
			id = site.getId();
			siteid = id;
			domain = site.getDomain();
			client = site.getClient();
			bindDomain = site.getBindDomain();
			if(site.getTemplateId() != null){
				templateId = site.getTemplateId();
			}else{
				templateId = 0;
			}
			state = site.getState();
		}
	}
	
	public SimpleSite() {
		plugin = new HashMap<String, Map<String,Object>>();
	}
	
	/**
	 * 克隆一个 {@link SimpleSite} 对象，将其内容导入到这里面
	 * @param originalSimpleSite 要克隆的对象
	 */
	public void clone(SimpleSite originalSimpleSite){
		this.bindDomain = originalSimpleSite.getBindDomain();
		this.client = originalSimpleSite.getClient();
		this.domain = originalSimpleSite.getDomain();
		this.id = originalSimpleSite.getId();
		this.plugin = originalSimpleSite.getPlugin();
		this.state = originalSimpleSite.getState();
		this.templateId = originalSimpleSite.getTemplateId();
		this.siteid = this.id;
		
		if(this.plugin == null){
			this.plugin = new HashMap<String, Map<String,Object>>();
		}
	}
	
	/**
	 * 废弃，请使用 getSiteid()
	 * @deprecated
	 */
	public int getId() {
		return id;
	}
	/**
	 * 废弃，请使用 setSiteid()
	 * @deprecated
	 */
	public void setId(int id) {
		this.id = id;
		this.siteid = id;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		if(domain.equals("null")){
			this.domain = "";
		}
		this.domain = domain;
	}
	public int getClient() {
		return client;
	}
	public void setClient(int client) {
		this.client = client;
	}

	public String getBindDomain() {
		return bindDomain;
	}

	public void setBindDomain(String bindDomain) {
		if(bindDomain.equals("null")){
			this.bindDomain = "";
		}
		this.bindDomain = bindDomain;
	}
	
	@Column(name = "template_id")
	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}


	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	/**
	 * 已废弃，使用 PluginCache
	 * 根据 key = plugin.id 来判断是否启用了该插件
	 * @deprecated
	 */
	public Map<String, Map<String,Object>> getPlugin() {
		if(plugin == null){
			return new HashMap<String, Map<String,Object>>();
		}
		return plugin;
	}

	/**
	 * 已废弃，使用 PluginCache
	 * @deprecated
	 */
	public void setPlugin(Map<String, Map<String,Object>> plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * 根据插件id，得到当前网站是否开启了此插件
	 * @param pluginId 插件id，插件的唯一标示。比如 baidushare
	 * @return true: 使用了该插件；  false:未使用此插件
	 */
	public boolean isUsePlugin(String pluginId){
		if(plugin == null){
			return false;
		}
		if(plugin.get(pluginId) == null){
			return false;
		}
		return true;
	}
	
	/**
	 * 获取 site.id
	 * @return
	 */
	public int getSiteid() {
		return siteid;
	}

	public void setSiteid(int siteid) {
		this.id = siteid;
		this.siteid = siteid;
	}

	@Override
	public String toString() {
		return "SimpleSite [id=" + id + ", domain=" + domain + ", bindDomain=" + bindDomain + ", client=" + client
				+ ", templateId=" + templateId + ", state=" + state + ", plugin=" + plugin + "]";
	}
	
}
