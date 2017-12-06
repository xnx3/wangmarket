package com.xnx3.domain.bean;

import javax.persistence.Column;
import com.xnx3.admin.entity.Site;

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

	public String toString() {
		return "SimpleSite [id=" + id + ", domain=" + domain + ", bindDomain="
				+ bindDomain + ", client=" + client + ", templateId="
				+ templateId + ", state=" + state + "]";
	}
	
}
