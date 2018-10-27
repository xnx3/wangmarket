package com.xnx3.wangmarket.domain.bean;

/**
 * 域名、网站更新相关的mq
 * @author 管雷鸣
 *
 */
public class MQBean {
	public final static int TYPE_DOMAIN = 1;		//更新二级域名，系统分配的域名，更新 site.domain
	public final static int TYPE_BIND_DOMAIN = 2;	//更新自己绑定的顶级域名，更新 site.bindDomain
	public final static int TYPE_STATE = 3;			//更新网站的状态，比如暂停网站、正常访问等网站状态
	public final static int TYPE_NEW_SITE = 4;		//创建新的网站，会自动分配一个二级域名
	
	private int type;	//类型；
	private String oldValue;	//旧的值，比如旧的二级域名、或者旧的绑定顶级域名
	private SimpleSite simpleSite;	//新的值
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		if(oldValue == null){
			this.oldValue = "";
		}
		this.oldValue = oldValue;
	}
	public SimpleSite getSimpleSite() {
		return simpleSite;
	}
	public void setSimpleSite(SimpleSite simpleSite) {
		this.simpleSite = simpleSite;
	}
	public String toString() {
		return "MQBean [type=" + type + ", oldValue=" + oldValue + ", simpleSite=" + simpleSite + "]";
	}
	
}
