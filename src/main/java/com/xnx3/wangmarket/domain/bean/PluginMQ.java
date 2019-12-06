package com.xnx3.wangmarket.domain.bean;

import com.xnx3.wangmarket.admin.entity.Site;

import net.sf.json.JSONObject;

/**
 * 网市场插件开发过程中，后台设置了某个参数后，需要用MQ传递到domain的，便是这个
 * @author 管雷鸣
 *
 */
public class PluginMQ {
	private int siteid;	//站点id
	private String domain;	//站点自动分配的二级域名,无论什么插件，MQ的必须有这个参数
	private String bindDomain;	//站点绑定的域名,无论什么插件，MQ的必须有这个参数
	
	JSONObject mqContentJson;	//mqContent MQ传递来的消息体的JSON格式数据

	/**
	 * 创建 MQBean对象，并吧 MQ 传递来的消息体转化为 MQBean对象
	 * @param mqContent MQ传递来的消息体
	 */
	public PluginMQ(String mqContent) {
		mqContentJson = JSONObject.fromObject(mqContent);
		if(mqContentJson.get("siteid") != null){
			this.siteid = mqContentJson.getInt("siteid");
		}
		if(mqContentJson.get("domain") != null){
			this.domain = mqContentJson.getString("domain");
		}else{
			this.domain = "";
		}
		if(mqContentJson.get("bindDomain") != null){
			this.bindDomain = mqContentJson.getString("bindDomain");
		}
	}
	
	/**
	 * 创建 MQBean对象，并吧当前站点信息传入
	 * @param site {@link Site}对象， 当前登陆的网站站点信息
	 */
	public PluginMQ(Site site) {
		if(site == null){
			return;
		}
		this.siteid = site.getId();
		this.domain = site.getDomain();
		this.bindDomain = site.getBindDomain();
	}
	
	public int getSiteid() {
		return siteid;
	}
	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getBindDomain() {
		return bindDomain;
	}
	public void setBindDomain(String bindDomain) {
		this.bindDomain = bindDomain;
	}
	
	/**
	 * 获取 mqContent MQ传递来的消息体 转换为的 JSON格式数据。
	 * <br/>必须先执行了 {@link #MQBeanPluginSuperClass(String)} 传入数据之后，这里才有值
	 * @return JSON数据
	 */
	public JSONObject getMqContentJson() {
		return mqContentJson;
	}
	
	/**
	 * 将传入的JSON对象，增加 siteid、doamin、bindDomain
	 * @return 增加了这三项的json
	 */
	public JSONObject jsonAppend(JSONObject json){
		json.put("siteid", this.siteid);
		json.put("domain", this.domain);
		json.put("bindDomain", this.bindDomain);
		return json;
	}
	
}
