package com.xnx3.domain.vo;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.domain.bean.SimpleSite;

public class SImpleSiteVO extends BaseVO {
	private SimpleSite simpleSite;
	private String serverName;	//获取用户当前访问的域名，如 leiwen.wang.market
	
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
	
}
