package com.xnx3.wangmarket.domain.vo;

import java.io.Serializable;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.domain.bean.SimpleSite;

public class SImpleSiteVO extends BaseVO implements Serializable{
	private SimpleSite simpleSite;
	private String serverName;	//获取用户当前访问的域名，如 leiwen.wang.market
	private boolean sourceBySession;	//获取到的此对象的来源，默认为false，不是从session获取的
	
	public SImpleSiteVO(){
		sourceBySession = false;
	}
	
	public SimpleSite getSimpleSite() {
		return simpleSite;
	}

	public void setSimpleSite(SimpleSite simpleSite) {
		this.simpleSite = simpleSite;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public boolean isSourceBySession() {
		return sourceBySession;
	}

	public void setSourceBySession(boolean sourceBySession) {
		this.sourceBySession = sourceBySession;
	}
	
}
