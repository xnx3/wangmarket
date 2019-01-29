package com.xnx3.wangmarket.weixin.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.weixin.autoReplyPluginManage.AutoReplyPluginManage;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.weixin.bean.MessageReceive;

/**
 * 网市场微信公众号使用的
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class WeiXinController extends BaseController{

	/**
	 * 微信服务器推送消息，推送到此接口
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	@RequestMapping("weixin${url.suffix}")
	public void weixin(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException{
		MessageReceive message = com.xnx3.wangmarket.weixin.Global.getWeiXinUtil().receiveMessage(request);
		
		AliyunLog.addActionLog("weixinMessage", message.getReceiveBody());
		
		if(request.getQueryString() == null){
			//return "<html><head><meta charset=\"utf-8\"></head><body>浏览器中直接输入网址访问是不行的！这个接口是接收微信服务器返回数据的，得通过微信服务器返回得数据测试才行</body></html>";
		}
		
		/**** 针对微信自动回复插件 ****/
		try {
			AutoReplyPluginManage.autoReply(request, response, message);
		} catch (InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 微信服务器配置，过验证
	 */
//	@RequestMapping("weixin")
//	public void verify(HttpServletRequest request, HttpServletResponse response){
//		getWeiXinUtil().joinVerify(request, response);
//	}
	

}
