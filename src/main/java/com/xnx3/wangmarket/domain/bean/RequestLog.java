package com.xnx3.wangmarket.domain.bean;

import java.util.Vector;
import com.aliyun.openservices.log.common.LogItem;

/**
 * 访问统计
 * @author 管雷鸣
 */
public class RequestLog {
	private String serverName;	//访问域名，如 pc.wang.market
	private String ip;		//用户的ip地址
	private Vector<LogItem> logGroup;	//访问的日志信息
	private int siteid;	//站点的id，若是跟站点对应的话
	
	public RequestLog() {
		logGroup = new Vector<LogItem>();
		siteid = 0;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Vector<LogItem> getLogGroup() {
		return logGroup;
	}

	public void setLogGroup(Vector<LogItem> logGroup) {
		this.logGroup = logGroup;
	}

	public int getSiteid() {
		return siteid;
	}

	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}
	
	
}
