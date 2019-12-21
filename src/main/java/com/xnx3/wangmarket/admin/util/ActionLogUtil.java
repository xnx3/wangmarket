package com.xnx3.wangmarket.admin.util;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import com.aliyun.openservices.log.common.LogItem;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * 用户操作日志统计
 * @author 管雷鸣
 * @since 5.0
 */
public class ActionLogUtil extends com.xnx3.j2ee.util.ActionLogUtil {
	
	/**
	 * 生成带有站点信息的logItem
	 */
	private static LogItem generateLogItem(){
		Site site = SessionUtil.getSite();
		
		String siteName = "";
		String siteDomain = "";
		int siteid = 0;
		if(site != null){
			siteName = site.getName();
			siteDomain = Func.getDomain(site);
			siteid = site.getId();
		}
		
		LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
		logItem.PushBack("siteName", siteName);
		logItem.PushBack("siteDomain", siteDomain);
		logItem.PushBack("siteid", siteid+"");
		return logItem;
	}
	
	/**
	 * 插入一条日志。
	 * <br/>这里插入的日志类型是 {@link ActionLogCache#TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param goalid 操作的目标id,若没有，则为0
	 * @param action 动作
	 * @param remark 备注，说明
	 * @deprecated 请使用有 HttpServletRequest 传入的，还能记录请求信息
	 */
	public static synchronized void insert(Integer goalid, String action, String remark){
		logExtend(generateLogItem(), null, goalid, action, remark, TYPE_NORMAL);
	}
	/**
	 * 插入一条日志。
	 * <br/>这里插入的日志类型是 {@link ActionLogCache#TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标id,若没有，则为0
	 * @param action 动作
	 * @param remark 备注，说明
	 */
	public static synchronized void insert(HttpServletRequest request, Integer goalid, String action, String remark){
		logExtend(generateLogItem(), request, goalid, action, remark, TYPE_NORMAL);
	}
	

	/**
	 * 插入一条日志。
	 * <br/>这里插入的日志类型是 {@link ActionLogCache#TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param goalid 操作的目标id,若没有，则为0
	 * @param action 动作
	 * @deprecated 请使用有 HttpServletRequest 传入的，还能记录请求信息
	 */
	public static synchronized void insert(Integer goalid, String action){
		logExtend(generateLogItem(), null, goalid, action, "", TYPE_NORMAL);
	}
	/**
	 * 插入一条日志。
	 * <br/>这里插入的日志类型是 {@link ActionLogCache#TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标id,若没有，则为0
	 * @param action 动作
	 */
	public static synchronized void insert(HttpServletRequest request, Integer goalid, String action){
		logExtend(generateLogItem(), request, goalid, action, "", TYPE_NORMAL);
	}
	
	/**
	 * 插入一条日志。
	 * <br/>这里插入的日志类型是 {@link ActionLogCache#TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param action 动作
	 * @param remark 备注，说明
	 * @deprecated 请使用有 HttpServletRequest 传入的，还能记录请求信息
	 */
	public static synchronized void insert(String action, String remark){
		logExtend(generateLogItem(), null, 0, action, remark, TYPE_NORMAL);
	}
	/**
	 * 插入一条日志。
	 * <br/>这里插入的日志类型是 {@link ActionLogCache#TYPE_NORMAL} 正常类型，比如用户进入某个页面、查看什么详情、查看什么列表等，只是记录用户普通的动作。如果是对数据库有更改、新增、删除操作的，需要使用 insertUpdateDatabase(...)
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param action 动作
	 * @param remark 备注，说明
	 */
	public static synchronized void insert(HttpServletRequest request, String action, String remark){
		logExtend(generateLogItem(), request, 0, action, remark, TYPE_NORMAL);
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
		logExtend(generateLogItem(), request, goalid, action, remark, TYPE_UPDATE_DATABASE);
	}

	/**
	 * 插入一条数据库变动日志。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此方法记录日志。
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static synchronized void insertUpdateDatabase(HttpServletRequest request, Integer goalid, String action, String remark){
		logExtend(generateLogItem(), request, goalid, action, remark, TYPE_UPDATE_DATABASE);
	}
	

	/**
	 * 插入一条数据库变动日志。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此方法记录日志。
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param action 动作的名字，如：用户登录、更改密码
	 * @param remark 动作的描述，如用户将名字张三改为李四
	 */
	public static void insertUpdateDatabase(HttpServletRequest request, String action, String remark){
		logExtend(generateLogItem(), request, 0, action, remark, TYPE_UPDATE_DATABASE);
	}
	
	/**
	 * 插入一条数据库变动日志。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此方法记录日志。
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param goalid 操作的目标的id，若无，可为0，也可为空
	 * @param action 动作的名字，如：用户登录、更改密码
	 */
	public static void insertUpdateDatabase(HttpServletRequest request, Integer goalid, String action){
		logExtend(generateLogItem(), request, goalid, action, "", TYPE_UPDATE_DATABASE);
	}
	
	/**
	 * 插入一条数据库变动日志。凡是数据库有插入、修改、删除记录的，让数据库数据有变动的，都使用此方法记录日志。
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param action 动作的名字，如：用户登录、更改密码
	 */
	public static void insertUpdateDatabase(HttpServletRequest request, String action){
		logExtend(generateLogItem(), request, 0, action, "", TYPE_UPDATE_DATABASE);
	}
	
	/**
	 * 插入一条错误日志。
	 * <br/>记录理论上不会出现的错误，但实际用户使用时，真的出现了。出现这种记录，技术人员看到这种类型记录后，一定是程序中数据、逻辑出现问题了，需要排查的，这种记录的日志不能看完就忽略，一定是要经过技术排查。
	 * <br/>比如有一个订单，根据订单中的用户编号(userid)取用户表(User)的记录时，用户表中没有这个人，那这个就是程序在哪个地方出现问题了，就要技术人员进行排查了。这种信息就可以用 TYPE_ERROR 来进行记录
	 * @param request HttpServletRequest 若为空，则不日志中不记录请求的信息，比如浏览器信息、请求网址等
	 * @param remark 详细描述，如： 有一个订单，订单号是xxx,根据订单中的用户编号userid:xxxx取用户表(User)的记录时，用户表中没有这个人，
	 */
	public static void insertError(HttpServletRequest request, String remark){
		logExtend(generateLogItem(), request, 0, "ERROR LOG", remark, TYPE_ERROR);
	}
	

}
