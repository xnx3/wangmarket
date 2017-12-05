package com.xnx3.domain.vo;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.domain.bean.SimpleSite;

public class SImpleSiteVO extends BaseVO {
	private SimpleSite simpleSite;
	private String ossUrl;		//获取OSS数据所在URL网站路径，如 http://wangmarket.oss-cn-hongkong-internal.aliyuncs.com/site/130/
	private String serverName;	//获取用户当前访问的域名，如 leiwen.wang.market
	
	public SimpleSite getSimpleSite() {
		return simpleSite;
	}

	public void setSimpleSite(SimpleSite simpleSite) {
		this.simpleSite = simpleSite;
	}

	public String getOssUrl() {
		return ossUrl;
	}

	public void setOssUrl(String ossUrl) {
		this.ossUrl = ossUrl;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
}
