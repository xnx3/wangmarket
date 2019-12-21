package com.xnx3.j2ee.util;

import javax.servlet.http.HttpServletRequest;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.net.AliyunLogUtil;

/**
 * 会员动作日志的缓存及使用。
 * 有其他日志需要记录，可以参考这个类。可吧这个类复制出来，在此基础上进行修改
 * @author 管雷鸣
 */
public class ActionLogUtil {
	/**
	 * 日志类型：普通日志
	 * <br/>比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。
	 * <br/>默认不传入此参数时，也是使用此种方式
	 */
	public static final String TYPE_NORMAL = "NORMAL";
	/**
	 * 日志类型：错误日志
	 * <br/>记录理论上不会出现的错误，但实际用户使用时，真的出现了。出现这种记录，技术人员看到这种类型记录后，一定是程序中数据、逻辑出现问题了，需要排查的，这种记录的日志不能看完就忽略，一定是要经过技术排查。
	 * <br/>比如有一个订单，根据订单中的用户编号(userid)取用户表(User)的记录时，用户表中没有这个人，那这个就是程序在哪个地方出现问题了，就要技术人员进行排查了。这种信息就可以用 TYPE_ERROR 来进行记录
	 */
	public static final String TYPE_ERROR = "ERROR";
	/**
	 * 日志类型：数据库有变动的。
	 * <br/>凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此种类型。
	 */
	public static final String TYPE_UPDATE_DATABASE = "UPDATE_DATABASE";
	
	
	public static AliyunLogUtil aliyunLogUtil = null;
	static{
		//判断是否使用日志服务进行日志记录，条件便是 accessKeyId 是否为空。若为空，则不使用
		String use = SystemUtil.get("ALIYUN_SLS_USE");
		if(use != null && use.equals("1")){
			//使用日志服务
			
			String keyId = SystemUtil.get("ALIYUN_ACCESSKEYID");
			String keySecret = SystemUtil.get("ALIYUN_ACCESSKEYSECRET");
			String endpoint = SystemUtil.get("ALIYUN_SLS_ENDPOINT");
			String project = SystemUtil.get("ALIYUN_SLS_PROJECT");
			String logstore = SystemUtil.get("ALIYUN_SLS_USERACTION_LOGSTORE");
			
			//最大超时时间
			int log_cache_max_time = SystemUtil.getInt("ALIYUN_SLS_CACHE_MAX_TIME");
			if(log_cache_max_time == 0){
				log_cache_max_time = 120;
			}
			//最大条数
			int log_cache_max_number = SystemUtil.getInt("ALIYUN_SLS_CACHE_MAX_NUMBER");
			if(log_cache_max_number == 0){
				log_cache_max_number = 100;
			}
			
			
			if(keyId.length() > 10){
				aliyunLogUtil = new AliyunLogUtil(endpoint,  keyId, keySecret, project, logstore);
				//开启触发日志的，其来源类及函数的记录
				aliyunLogUtil.setStackTraceDeep(4);
				aliyunLogUtil.setCacheAutoSubmit(log_cache_max_number, log_cache_max_time);
				ConsoleUtil.info("开启日志服务进行操作记录");
			}else{
				//此处可能是还没执行install安装
			}
			
		}
	}
	
	/**
	 * 插入一条日志。
	 * <b>注意，不能直接使用此方法此写日志</b>，是因为上面设置了 aliyunLogUtil.setStackTraceDeep(4); 如果直接使用此方法写日志，那么执行的类、方法 是记录不到的。这个方法是给 insert....方法 提供服务的
	 * @param logItem 传入要保存的logItem，若为空，则会创建一个新的。此项主要为扩展使用，可自行增加其他信息记录入日志
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 * @param type 当前日志记录的类型。 取值：
	 * 			<ul>
	 * 				<li> {@link #TYPE_NORMAL} : 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。 </li>
	 * 				<li> {@link #TYPE_ERROR} : 错误日志。记录理论上不会出现的错误，但实际用户使用时，真的出现了。出现这种记录，技术人员看到这种类型记录后，一定是程序中数据、逻辑出现问题了，需要排查的，这种记录的日志不能看完就忽略，一定是要经过技术排查。<br/>比如有一个订单，根据订单中的用户编号(userid)取用户表(User)的记录时，用户表中没有这个人，那这个就是程序在哪个地方出现问题了，就要技术人员进行排查了。这种信息就可以用 TYPE_ERROR 来进行记录<br/>这种类型一般不会用到。除非自己感觉哪里可能会有坑，留一条这种类型的日志</li>
	 * 				<li> {@link #TYPE_UPDATE_DATABASE} : 操作数据库相关的。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此种类型<br/>使用 insertUpdateDatabase(...) 这种方法名的，就是记录这种类型的日志 </li>
	 * 			</ul>
	 */
	public static synchronized void logExtend(LogItem logItem, HttpServletRequest request, Integer goalid, String action, String remark, String type){
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
		logItem.PushBack("goalid", goalid != null? goalid+"":"0");
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
		
		//类型
		if(type == null || type.length() == 0){
			type = TYPE_NORMAL;
		}
		logItem.PushBack("type",type);
		
		try {
			aliyunLogUtil.cacheLog(logItem);
		} catch (LogException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 插入一条日志。
	 * @param logItem 传入要保存的logItem，若为空，则会创建一个新的。此项主要为扩展使用，可自行增加其他信息记录入日志
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 * @param type 当前日志记录的类型。 取值：
	 * 			<ul>
	 * 				<li> {@link #TYPE_NORMAL} : 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。 </li>
	 * 				<li> {@link #TYPE_ERROR} : 错误日志。记录理论上不会出现的错误，但实际用户使用时，真的出现了。出现这种记录，技术人员看到这种类型记录后，一定是程序中数据、逻辑出现问题了，需要排查的，这种记录的日志不能看完就忽略，一定是要经过技术排查。<br/>比如有一个订单，根据订单中的用户编号(userid)取用户表(User)的记录时，用户表中没有这个人，那这个就是程序在哪个地方出现问题了，就要技术人员进行排查了。这种信息就可以用 TYPE_ERROR 来进行记录<br/>这种类型一般不会用到。除非自己感觉哪里可能会有坑，留一条这种类型的日志</li>
	 * 				<li> {@link #TYPE_UPDATE_DATABASE} : 操作数据库相关的。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此种类型<br/>使用 insertUpdateDatabase(...) 这种方法名的，就是记录这种类型的日志 </li>
	 * 			</ul>
	 */
	public static synchronized void insert(LogItem logItem, HttpServletRequest request, Integer goalid, String action, String remark, String type){
		logExtend(logItem, request, goalid, action, remark, type);
	}
	
	/**
	 * 插入一条日志。
	 * <br/>这里插入的日志类型是 {@link #TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param logItem 传入要保存的logItem，若为空，则会创建一个新的。此项主要为扩展使用，可自行增加其他信息记录入日志
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static synchronized void insert(LogItem logItem, HttpServletRequest request, Integer goalid, String action, String remark){
		logExtend(logItem, request, goalid, action, remark, TYPE_NORMAL);
	}
	

	
	/**
	 * 插入一条日志
	 * <br/>这里插入的日志类型是 {@link #TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static synchronized void insert(HttpServletRequest request, Integer goalid, String action, String remark){
		logExtend(null, request, goalid, action, remark, TYPE_NORMAL);
	}
	
	/**
	 * 插入一条日志。
	 * <br/>这里插入的日志类型是 {@link #TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static void insert(HttpServletRequest request, String action, String remark){
		logExtend(null, request, 0, action, remark, TYPE_NORMAL);
	}
	
	/**
	 * 插入一条日志
	 * <br/>这里插入的日志类型是 {@link #TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 */
	public static void insert(HttpServletRequest request, Integer goalid, String action){
		logExtend(null, request, goalid, action, "", TYPE_NORMAL);
	}
	
	/**
	 * 插入一条日志
	 * <br/>这里插入的日志类型是 {@link #TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param action 动作的名字，如：用户登录、更改密码
	 */
	public static void insert(HttpServletRequest request, String action){
		logExtend(null, request, 0, action, "", TYPE_NORMAL);
	}
	
	
	/**
	 * 插入一条数据库变动日志。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此方法记录日志。
	 * @param logItem 传入要保存的logItem，若为空，则会创建一个新的。此项主要为扩展使用，可自行增加其他信息记录入日志
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static synchronized void insertUpdateDatabase(LogItem logItem, HttpServletRequest request, Integer goalid, String action, String remark){
		logExtend(logItem, request, goalid, action, remark, TYPE_UPDATE_DATABASE);
	}

	/**
	 * 插入一条数据库变动日志。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此方法记录日志。
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static synchronized void insertUpdateDatabase(HttpServletRequest request, Integer goalid, String action, String remark){
		logExtend(null, request, goalid, action, remark, TYPE_UPDATE_DATABASE);
	}
	

	/**
	 * 插入一条数据库变动日志。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此方法记录日志。
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static void insertUpdateDatabase(HttpServletRequest request, String action, String remark){
		logExtend(null, request, 0, action, remark, TYPE_UPDATE_DATABASE);
	}
	
	/**
	 * 插入一条数据库变动日志。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此方法记录日志。
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 */
	public static void insertUpdateDatabase(HttpServletRequest request, Integer goalid, String action){
		logExtend(null, request, goalid, action, "", TYPE_UPDATE_DATABASE);
	}
	
	/**
	 * 插入一条数据库变动日志。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此方法记录日志。
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param action 动作的名字，如：用户登录、更改密码
	 */
	public static void insertUpdateDatabase(HttpServletRequest request, String action){
		logExtend(null, request, 0, action, "", TYPE_UPDATE_DATABASE);
	}
	
	/**
	 * 插入一条错误日志。
	 * <br/>记录理论上不会出现的错误，但实际用户使用时，真的出现了。出现这种记录，技术人员看到这种类型记录后，一定是程序中数据、逻辑出现问题了，需要排查的，这种记录的日志不能看完就忽略，一定是要经过技术排查。
	 * <br/>比如有一个订单，根据订单中的用户编号(userid)取用户表(User)的记录时，用户表中没有这个人，那这个就是程序在哪个地方出现问题了，就要技术人员进行排查了。这种信息就可以用 TYPE_ERROR 来进行记录
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param remark 详细描述，如： 有一个订单，订单号是xxx,根据订单中的用户编号userid:xxxx取用户表(User)的记录时，用户表中没有这个人，
	 */
	public static void insertError(HttpServletRequest request, String remark){
		logExtend(null, request, 0, "ERROR LOG", remark, TYPE_ERROR);
	}
	
}
