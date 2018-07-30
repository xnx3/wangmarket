package com.xnx3.wangmarket.weixin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.xnx3.wangmarket.weixin.autoReplyPluginManage.Reply;
import com.xnx3.wangmarket.weixin.interfaces.AutoReply;
import com.xnx3.weixin.bean.MessageReceive;

/**
 * 这是演示的自定义回复规则
 * @author 管雷鸣
 */
public class YanShiPlugin implements AutoReply{
	public void reply(HttpServletRequest request, HttpServletResponse response, MessageReceive message, Reply reply) {
		System.out.println("微信用户发给公众号的内容为："+message.getContent());
		
		//这里会将此段话自动通过微信公众号回复给微信用户
//		reply.replyText("欢迎关注“网市场”订阅号，你在使用网市场建站的过程中，遇到什么问题，可随时向我们公众号发送信息进行求助");
	}
}
