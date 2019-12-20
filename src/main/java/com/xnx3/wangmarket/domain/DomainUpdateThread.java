package com.xnx3.wangmarket.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

import com.aliyun.mns.model.Message;
import com.xnx3.IntegerUtil;
import com.xnx3.j2ee.func.Log;
import com.xnx3.wangmarket.domain.bean.SimpleSite;

/**
 * 域名更新线程。使用MNS轮训。网站没有上万之前，此方式比较省钱
 * @author 管雷鸣
 */
@Component
public class DomainUpdateThread {
	//主要用户消息服务取到消息后，将消息内容更新过来。但是一个消息可能会取多次，如果连续改动几次，那此处接收到消息后可能会频繁改来改去。所以用此map，来判断是否此消息已经取过了，若取过了，则忽略，若没有取过，才能拿来使用。同时缓存到此map中进行记录
	public static Map<String,String> messageUpdateMap = new HashMap<String, String>();
	
	/**
	 * 消息服务，获取域名的变动，1分钟更新一次
	 */
//	public DomainUpdateThread() {
//		//MNS每次轮训最新域名列表时，中间延迟时间使用随机数，以绕过MNS的消费后1秒的隐藏间隔
//		final int intervalTime = IntegerUtil.random(25, 50)*1000;
//		
//		new Thread(new Runnable() {
//			public void run() {
//				System.out.println("监控域名更新线程－启动");
//				try {
//					//延迟，。。。秒后更新域名改动
//					Thread.sleep(intervalTime);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				
//				if(G.domainMNSUtil == null){
//					return;
//				}
//				
//				while(true){
//					try {
//						//延迟，。。。使用此应用独有的延迟时间＋随机增加时间，进行轮训拉取
//						int s = intervalTime+(IntegerUtil.random(1, 35)*1000);
//						Thread.sleep(s);
//					} catch (InterruptedException e1) {
//						e1.printStackTrace();
//					}
//					try {
//						List<Message> ml = G.domainMNSUtil.listMessage(G.mnsDomain_queueName);
//						for (int i = 0; i < ml.size(); i++) {
//							Message message = ml.get(i);
//							//判断此是否已经更新过了
//							if(messageUpdateMap.get(message.getMessageId()) == null){
//								//若没有更新过，才会进行更新信息的操作
//								JSONObject json = JSONObject.fromObject(message.getMessageBody());
//								SimpleSite ss = new SimpleSite();
//								ss.setId(json.getInt("id"));
//								ss.setDomain(json.getString("domain"));
//								ss.setBindDomain(json.getString("bindDomain"));
//								ss.setClient(json.getInt("client"));
//								ss.setState((short) json.getInt("state"));
//								if(json.get("templateId") != null){
//									ss.setTemplateId(json.getInt("templateId"));
//								}
//								
//								//更新域名缓存
//								//更新自动分配的二级域名
//								G.putDomain(ss.getDomain(), ss);
//								Log.debug("domain: "+ss.getDomain()+" --> "+ss.toString());
//								
//								//更新用户自己绑定的顶级域名
//								if(ss.getBindDomain() != null && ss.getBindDomain().length() > 0 && !ss.getBindDomain().equals("null")){
//									G.putBindDomain(ss.getBindDomain(), ss);
//									Log.debug("bindDomain: "+ss.getBindDomain()+" --> "+ss.toString());
//								}
//								
//								//将此计入messageUpdateMap，此消息已经更新过了
//								messageUpdateMap.put(message.getMessageId(), "1");
//							}
//						}
//					} catch (Exception e) {
//						if(e != null && e.getMessage() != null && e.getMessage().indexOf("Connection reset by peer") > -1){
//							//忽略，不影响使用。服务端是VIP网络环境，会主动掐掉空闲连接。
//						}else{
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}).start();
//		
//		
//		//每间隔一天，清空历史map
//		new Thread(new Runnable() {
//			public void run() {
//				messageUpdateMap.clear();
//				
//				try {
//					Thread.sleep(1000 * 60 * 60 * 24);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
	
}
