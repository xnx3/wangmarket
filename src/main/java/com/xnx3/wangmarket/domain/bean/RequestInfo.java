package com.xnx3.wangmarket.domain.bean;

/**
 * 用户访问网站页面，用户及本次访问的信息
 * @author 管雷鸣
 *
 */
public class RequestInfo {
	
	private String htmlFile;	//访问的网站哪个页面。值如：  about.html
	private String ip;			//访客的ip地址	
	private int time;			//当前请求访问的时间，也就是当前时间。
	private String serverName;	//当前访问的域名,即 request.getServerName()
	private String referer;		//当前访问的来源，即访问的 referer
	private String userAgent;	//当前访问网站的用户的浏览器 User-Agnet 信息
	
	public String getHtmlFile() {
		return htmlFile;
	}
	public void setHtmlFile(String htmlFile) {
		this.htmlFile = htmlFile;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	@Override
	public String toString() {
		return "RequestInfo [htmlFile=" + htmlFile + ", ip=" + ip + ", time="
				+ time + ", serverName=" + serverName + ", referer=" + referer
				+ ", userAgent=" + userAgent + "]";
	}
	
	
	
}
