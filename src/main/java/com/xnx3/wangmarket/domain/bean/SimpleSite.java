package com.xnx3.wangmarket.domain.bean;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * {@link Site} 的简化
 * @author 管雷鸣
 */
public class SimpleSite {
	private int id;
	private String domain;
	private String bindDomain;
	private int client;
	private int templateId;
	private Short state;
	/*
	 * 插件
	 * plugin.get(plugin id)   value：1  判断pluginid是否不为null，若不为null，则是启用了该插件。 至于value则无所谓，没用到
	 */
	private Map<String, String> plugin;
	
	//读数据库的方式载入 SimpleSite ，在系统刚启动时，会自动读数据库中的站点属性，将其载入，分配好域名
	public SimpleSite(Site site) {
		if(site != null){
			id = site.getId();
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
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
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
	 * 根据 key = plugin.id 来判断是否启用了该插件
	 * @return
	 */
	public Map<String, String> getPlugin() {
		if(plugin == null){
			return new HashMap<String, String>();
		}
		return plugin;
	}

	public void setPlugin(Map<String, String> plugin) {
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
	
	@Override
	public String toString() {
		return "SimpleSite [id=" + id + ", domain=" + domain + ", bindDomain=" + bindDomain + ", client=" + client
				+ ", templateId=" + templateId + ", state=" + state + ", plugin=" + plugin + "]";
	}
	
}
