package com.xnx3.im;

import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.im.bean.Message;

/**
 * 客服对话日志记录，将内容记录入日志服务
 */
public class KefuLog {
	
	static{
		//定时提交。被动触发，当有人发送信息时，会自动触发使用本类进行保存，从而触发此线程的开启。每隔1分钟会自动提交一次
		new Thread(new Runnable() {
			public void run() {
				if(Global.aliyunLogUtil == null){
					return;
				}
				System.out.println("KefuLog 已开启自动提交线程，每间隔一分钟会自动提交一次");
				while(true){
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					try {
						//如果其中有值，则自动提交
						if(Global.aliyunLogUtil.logGroupCache.size() > 0){
							Global.aliyunLogUtil.cacheCommit();
						}
					} catch (LogException e) {
						System.out.println("日志用多线程自动提交出现问题");
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public static void insert(Message msg){
		if(Global.aliyunLogUtil == null){
			return;
		}
		LogItem item = Global.aliyunLogUtil.newLogItem();
		
		//发送方
		item.PushBack("sendId", msg.getSendId()+"");
		item.PushBack("sendUserName", msg.getSendUserName());
		//接收方
		item.PushBack("receiveId", msg.getReceiveId()+"");
		item.PushBack("receiveUserName", msg.getReceiveUserName());
		//发送的消息内容
		item.PushBack("content", msg.getContent());
		//接收方的socket标示
		item.PushBack("receiveSocketId", msg.getSocketId()+"");
		item.PushBack("receiveSocketUuid", msg.getSocketUuid());
		
		try {
			Global.aliyunLogUtil.cacheLog(item);
		} catch (LogException e) {
			e.printStackTrace();
		}
	}
	
}
