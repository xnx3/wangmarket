package com.xnx3.im;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.net.AliyunLogUtil;
import com.xnx3.net.MNSUtil;

/**
 * 全局控制
 * @author 管雷鸣
 */
public class Global {

	/**
	 * 管理后台有更改后，通过MNS来更新IM应用
	 */
	public static MNSUtil kefuMNSUtil;
	public static String kefuMNSUtil_queueName = "";
	/**
	 * 日志服务，记录客服对话
	 */
	public static AliyunLogUtil aliyunLogUtil;
	static{
		ConfigManagerUtil c = ConfigManagerUtil.getSingleton("imConfig.xml");
		//消息服务
		kefuMNSUtil = new MNSUtil(c.getValue("aliyunMNS_kefu.accessKeyId"), c.getValue("aliyunMNS_kefu.accessKeySecret"), c.getValue("aliyunMNS_kefu.endpoint"));
		kefuMNSUtil_queueName = c.getValue("aliyunMNS_kefu.queueName");
		//日志服务
		aliyunLogUtil = new AliyunLogUtil(c.getValue("aliyunLogKefu.endpoint"), c.getValue("aliyunLogKefu.accessKeyId"), c.getValue("aliyunLogKefu.accessKeySecret"), c.getValue("aliyunLogKefu.project"), c.getValue("aliyunLogKefu.logstore"));	
	}
	
}
