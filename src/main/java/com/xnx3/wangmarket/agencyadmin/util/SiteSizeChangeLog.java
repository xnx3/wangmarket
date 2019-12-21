package com.xnx3.wangmarket.agencyadmin.util;

import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.net.AliyunLogUtil;
import com.xnx3.net.MailUtil;

/**
 * 站币变动记录
 * @author 管雷鸣
 */
public class SiteSizeChangeLog {
	public static AliyunLogUtil aliyunLogUtil;
	
	static{
		//判断是否使用日志服务进行日志记录，条件便是 accessKeyId 是否为空。若为空，则不使用
		String use = SystemUtil.get("ALIYUN_SLS_USE");
		if(use != null && use.equals("1")){
			//使用日志服务
			
			String keyId = SystemUtil.get("ALIYUN_ACCESSKEYID");
			String keySecret = SystemUtil.get("ALIYUN_ACCESSKEYSECRET");
			String endpoint = SystemUtil.get("ALIYUN_SLS_ENDPOINT");
			String project = SystemUtil.get("ALIYUN_SLS_PROJECT");
			String logstore = SystemUtil.get("ALIYUN_LOG_SITESIZECHANGE");
			
			
			if(keyId.length() > 10){
				aliyunLogUtil = new AliyunLogUtil(endpoint,  keyId, keySecret, project, logstore);
				aliyunLogUtil.setCacheAutoSubmit(0, 60);
				ConsoleUtil.info("开启日志服务进行操作记录");
			}else{
				//此处可能是还没执行install安装
			}
			
		}
	}
	
	
	/**
	 * 增加动作日志。此方法不可直接调用，需间接
	 * @param userid 当前日志属于哪个登录用户的，对应其user.id
	 * @param username 当前日志属于哪个登录用户的，对应其user.username
	 * @param agencyName 当前登录的代理的 {@link Agency}.name 若是不是代理，那就是管理员了，管理员可传入空字符串
	 * @param remark 备注说明
	 * @param agencySiteSizeChange 代理变动的“站”余额的多少，消耗为负数，增加为正数  {@link Agency}.siteSize
	 * @param changeBefore 变动前，站点的站余额是多少， {@link Agency}.siteSize
	 * @param changeAfter 变化之后的站点的站余额是多少。{@link Agency}.siteSize
	 * @param goalid 其余额变动，是开通的哪个站点引起的，记录站点的id，或者是哪个人给他增加的，记录给他增加的人的userid
	 * @param ip 操做人的ip地址
	 * @param topic 主题，分类。减去站币，消费，传入"xiaofei"， 增加站币，充值，传入"chongzhi"
	 */
	private static void addChangeLog(int userid, String username, String agencyName, String remark, int agencySiteSizeChange, int changeBefore, int changeAfter, int goalid, String ip, String topic){
		if(aliyunLogUtil == null){
			return;
		}
		StackTraceElement st = Thread.currentThread().getStackTrace()[3];
		
		LogItem logItem = aliyunLogUtil.newLogItem();
		/*用户相关信息*/
		logItem.PushBack("userid", userid+"");
		logItem.PushBack("username", username);
		/*日志信息*/
		logItem.PushBack("goalid", goalid+"");
		logItem.PushBack("remark", remark);
		/*代理信息，如果是代理操作的话*/
		logItem.PushBack("agencyName", agencyName);
		logItem.PushBack("changeBefore", changeBefore+"");
		logItem.PushBack("changeAfter", changeAfter+"");
		logItem.PushBack("agencySiteSizeChange", agencySiteSizeChange+"");
		/*使用的类的信息，来源位置*/
		logItem.PushBack("class", st.getClassName());
		logItem.PushBack("method", st.getMethodName());
		
		try {
			aliyunLogUtil.save(topic, ip, logItem);
		} catch (LogException e) {
			MailUtil.sendMail(SystemUtil.get("SERVICE_MAIL"), "站币消费出错", e.getMessage()+"，"+logItem.ToJsonString());
			e.printStackTrace();
		}
	}
	
	/**
	 * 消费日志，那肯定就是有代理操作的。管理员操作只是充值
	 * @see #addChangeLog(int, String, String, String, int, int, int, int, String, String)
	 */
	public static void xiaofei(String agencyName, String remark, int agencySiteSizeChange, int changeBefore, int changeAfter, int goalid, String ip){
		if(aliyunLogUtil == null){
			return;
		}
		//当前登录用户信息
		User user = ShiroFunc.getUser();
		int userid = 0;
		String username = "";
		if(user != null){
			userid = user.getId();
			username = user.getUsername();
		}
		
		addChangeLog(userid, username, agencyName, remark, agencySiteSizeChange, changeBefore, changeAfter, goalid, ip, "xiaofei");
	}
	
	/**
	 * 充值日志，那肯定就是有代理操作的。（管理员操作只能是消费，给代理充值后是消费日志）充值大多都是我(当前登录用户)给对方充值，所以对方是未登录的，手动传入其用户信息
	 * @param agencyName
	 * @param remark
	 * @param agencySiteSizeChange
	 * @param changeBefore
	 * @param changeAgter
	 * @param goalid
	 * @param ip
	 */
	public static void chongzhi(int userid, String username, String agencyName, String remark, int agencySiteSizeChange, int changeBefore, int changeAgter, int goalid, String ip){
		if(aliyunLogUtil == null){
			return;
		}
		addChangeLog(userid, username, agencyName, remark, agencySiteSizeChange, changeBefore, changeAgter, goalid, ip, "chongzhi");
	}
}
