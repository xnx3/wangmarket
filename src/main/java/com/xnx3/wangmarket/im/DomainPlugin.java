package com.xnx3.wangmarket.im;

import com.xnx3.ConfigManagerUtil;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.wangmarket.domain.bean.RequestInfo;
import com.xnx3.wangmarket.domain.bean.SimpleSite;
import com.xnx3.wangmarket.domain.pluginManage.interfaces.DomainVisitInterface;

/**
 * 针对网站访问
 * @author 管雷鸣
 *
 */
public class DomainPlugin implements DomainVisitInterface {
	//webSocket IM的对接url， 在 imConfig.xml 中配置
	public static String websocketUrl = "";
	public static boolean isUse = false;	//是否使用，启用。若启用，则为true
	
	static{
		ConfigManagerUtil c = ConfigManagerUtil.getSingleton("imConfig.xml");
		String useMNS = c.getValue("aliyunMNS_kefu.use");
		if(useMNS != null && useMNS.equals("true")){
			//使用IM功能，那么增加html文件追加功能
			isUse = true;
			websocketUrl = c.getValue("websocketUrl");
		}
	}
	
	@Override
	public String htmlManage(String html, SimpleSite simpleSite, RequestInfo requestInfo) {
		if(isUse){
			return html + ""
					+ "<script> "
					+ "		var im_kefu_socketUrl = '"+ websocketUrl +"'; "
					+ "		var attachmentFileUrl = '"+AttachmentFile.netUrl()+"';"
					+ "</script>"
					+ "<script src=\"http://res.weiunity.com/js/fun.js\"></script>"
					+ "<script src=\""+AttachmentFile.netUrl()+"/site/"+simpleSite.getId()+"/data/site.js?v="+requestInfo.getTime()+"\"></script>"
					+ "<script src=\""+AttachmentFile.netUrl()+"/js/im/site_kefu.js\"></script>"
					+ "";
		}else{
			//不做处理追加，那么将原本的返回。
			return html;
		}
	}

}
