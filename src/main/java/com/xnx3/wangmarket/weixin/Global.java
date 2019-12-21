package com.xnx3.wangmarket.weixin;

import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.weixin.WeiXinUtil;

public class Global {
	
	/**
	 * weixin工具类
	 */
	private static WeiXinUtil wx = null;
	public static WeiXinUtil getWeiXinUtil(){
		if(wx == null){
			wx = new WeiXinUtil(SystemUtil.get("WEIXIN_APPID"), SystemUtil.get("WEIXIN_APPSECRET"), SystemUtil.get("WEIXIN_TOKEN"));
		}
		return wx;
	}

	
	
	
	
}
