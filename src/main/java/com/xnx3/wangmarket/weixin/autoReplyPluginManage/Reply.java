package com.xnx3.wangmarket.weixin.autoReplyPluginManage;

import javax.servlet.http.HttpServletResponse;

import com.xnx3.weixin.bean.MessageReply;


/**
 * 回复，这个属于二次开发自定义开发时，利用接口  {@link AutoReply} 直接调用回复的内容
 * @author 管雷鸣
 *
 */
public class Reply {
	MessageReply messageReply;
	HttpServletResponse response;
	
	public Reply(HttpServletResponse response, String toUserName, String fromUserName) {
		messageReply = new MessageReply(toUserName, fromUserName);
		this.response = response;
	}
	
	/**
	 * 公众号回复给咨询者一段字符串，直接显示这段字符串给咨询者
	 * @param content 显示的字符串
	 */
	public void replyText(String content){
		messageReply.replyText(response, content);
	}
	
	/**
	 * 回复成功信息给微信服务器，避免咨询者在微信公众号回复信息时，提示错误信息。
	 */
	public void replySuccessForWeiXinServer(){
		messageReply.reply(response, "success");
	}
	
	/**
	 * 微信公众号回复给咨询者。 这里需要自己组合返回的 xml
	 * @param text 回复微信服务器的 xml 格式数据
	 */
	public void reply(String text){
		messageReply.reply(response, text);
	}
	
	
}
