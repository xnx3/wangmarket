package com.xnx3.j2ee.func;

import javax.servlet.http.HttpServletRequest;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.net.AliyunLogUtil;

/**
 * 会员动作日志的缓存及使用。
 * 有其他日志需要记录，可以参考这个类。可吧这个类复制出来，在此基础上进行修改
 * @author 管雷鸣
 */
public class ActionLogCache {
	public static AliyunLogUtil aliyunLogUtil = null;
	static{
		//判断是否使用日志服务进行日志记录，条件便是 accessKeyId 是否为空。若为空，则不使用
		String use = Global.get("ALIYUN_SLS_USE");
		if(use != null && use.equals("1")){
			//使用日志服务
			
			String keyId = Global.get("ALIYUN_ACCESSKEYID");
			String keySecret = Global.get("ALIYUN_ACCESSKEYSECRET");
			String endpoint = Global.get("ALIYUN_SLS_ENDPOINT");
			String project = Global.get("ALIYUN_SLS_PROJECT");
			String logstore = Global.get("ALIYUN_SLS_USERACTION_LOGSTORE");
			
			//最大超时时间
			int log_cache_max_time = Global.getInt("ALIYUN_SLS_CACHE_MAX_TIME");
			if(log_cache_max_time == 0){
				log_cache_max_time = 120;
			}
			//最大条数
			int log_cache_max_number = Global.getInt("ALIYUN_SLS_CACHE_MAX_NUMBER");
			if(log_cache_max_number == 0){
				log_cache_max_number = 100;
			}
			
			
			if(keyId.length() > 10){
				aliyunLogUtil = new AliyunLogUtil(endpoint,  keyId, keySecret, project, logstore);
				//开启触发日志的，其来源类及函数的记录
				aliyunLogUtil.setStackTraceDeep(4);
				aliyunLogUtil.setCacheAutoSubmit(log_cache_max_number, log_cache_max_time);
				Log.info("开启日志服务进行操作记录");
			}else{
				//此处可能是还没执行install安装
			}
			
		}
	}
	

	/**
	 * 插入一条日志
	 * @param logItem 传入要保存的logItem，若为空，则会创建一个新的。此项主要为扩展使用，可自行增加其他信息记录入日志
	 * @param request HttpServletRequest
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static synchronized void insert(LogItem logItem, HttpServletRequest request, int goalid, String action, String remark){
		if(aliyunLogUtil == null){
			//不使用日志服务，终止即可
			return;
		}
		if(logItem == null){
			logItem = aliyunLogUtil.newLogItem();
		}
		
		/*用户相关信息,只有用户登录后，才会记录用户信息*/
		User user = ShiroFunc.getUser();
		if(user != null){
			logItem.PushBack("userid", user.getId()+"");
			logItem.PushBack("username", user.getUsername());
		}
		
		/* 动作相关 */
		logItem.PushBack("goalid", goalid+"");
		logItem.PushBack("action", action);
		logItem.PushBack("remark", remark);
		
		/*浏览器自动获取的一些信息*/
		if(request != null){
			logItem.PushBack("ip", IpUtil.getIpAddress(request));
			logItem.PushBack("param", request.getQueryString());
			logItem.PushBack("url", request.getRequestURL().toString());
			logItem.PushBack("referer", request.getHeader("referer"));
			logItem.PushBack("userAgent", request.getHeader("User-Agent"));
		}
		try {
			aliyunLogUtil.cacheLog(logItem);
		} catch (LogException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 插入一条日志
	 * @param request HttpServletRequest
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static synchronized void insert(HttpServletRequest request, int goalid, String action, String remark){
		insert(null, request, goalid, action, remark);
	}
	
	/**
	 * 插入一条日志
	 * @param request HttpServletRequest
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static void insert(HttpServletRequest request, String action, String remark){
		insert(null, request, 0, action, remark);
	}
	
	/**
	 * 插入一条日志
	 * @param request HttpServletRequest
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 */
	public static void insert(HttpServletRequest request, int goalid, String action){
		insert(null, request, goalid, action, "");
	}
	
	/**
	 * 插入一条日志
	 * @param request HttpServletRequest
	 * @param action 动作的名字，如：用户登录、更改密码
	 */
	public static void insert(HttpServletRequest request, String action){
		insert(null, request, 0, action, "");
	}
	
}
