package com.xnx3.wangmarket.weixin;

import com.xnx3.weixin.WeiXinUtil;

public class Global {
	
	/**
	 * weixin工具类
	 */
	private static WeiXinUtil wx = null;
	public static WeiXinUtil getWeiXinUtil(){
		if(wx == null){
			wx = new WeiXinUtil(com.xnx3.j2ee.Global.get("WEIXIN_APPID"), com.xnx3.j2ee.Global.get("WEIXIN_APPSECRET"), com.xnx3.j2ee.Global.get("WEIXIN_TOKEN"));
		}
		return wx;
	}

	
	
	
	
}
