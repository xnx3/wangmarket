package com.xnx3.wangmarket.im;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import com.aliyun.mns.model.Message;
import com.xnx3.IntegerUtil;
import com.xnx3.wangmarket.im.bean.Im;

/**
 * 消息服务轮训更新管理后台自定义的客服配置信息
 * 应用启动后自动运行
 * @author 管雷鸣
 */
@Component
public class MNSUpdateThread {
	//主要用户消息服务取到消息后，将消息内容更新过来。但是一个消息可能会取多次，如果连续改动几次，那此处接收到消息后可能会频繁改来改去。所以用此map，来判断是否此消息已经取过了，若取过了，则忽略，若没有取过，才能拿来使用。同时缓存到此map中进行记录
	public static Map<String,String> messageUpdateMap = new HashMap<String, String>();
	
	/**
	 * 消息服务，获取域名的变动，1分钟更新一次
	 */
	public MNSUpdateThread() {
		//MNS每次轮训最新域名列表时，中间延迟时间使用随机数，以绕过MNS的消费后1秒的隐藏间隔
		final int intervalTime = IntegerUtil.random(25, 50)*1000;
		
		new Thread(new Runnable() {
			public void run() {
				try {
					//延迟，。。。秒后更新域名改动
					Thread.sleep(intervalTime);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				if(Global.kefuMNSUtil == null){
					return;
				}
				
				while(true){
					try {
						//延迟，。。。使用此应用独有的延迟时间＋随机增加时间，进行轮训拉取
						int s = intervalTime+(IntegerUtil.random(1, 35)*1000);
						Thread.sleep(s);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					try {
						List<Message> ml = Global.kefuMNSUtil.listMessage(Global.kefuMNSUtil_queueName);
						for (int i = 0; i < ml.size(); i++) {
							Message message = ml.get(i);
							//判断此是否已经更新过了
							if(messageUpdateMap.get(message.getMessageId()) == null){
								//若没有更新过，才会进行更新信息的操作
								JSONObject json = JSONObject.fromObject(message.getMessageBody());
								
								Im im = new Im();
								im.setAutoReply(json.getString("autoReply"));
								im.setEmail(json.getString("email"));
								im.setHaveImSet(json.getBoolean("haveImSet"));
								im.setUseOffLineEmail(json.getString("useOffLineEmail").equals("1"));
								im.setUserid(json.getLong("userid"));
								//将IM存入内存
								Global.imMap.put(im.getUserid(), im);
//								System.out.println("更新值－－－－>"+im.toString());
								
								//将此计入messageUpdateMap，此消息已经更新过了
								messageUpdateMap.put(message.getMessageId(), "1");
							}
						}
					} catch (Exception e) {
						if(e != null && e.getMessage() != null && e.getMessage().indexOf("Connection reset by peer") > -1){
							//忽略，不影响使用。服务端是VIP网络环境，会主动掐掉空闲连接。
						}else{
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

}
