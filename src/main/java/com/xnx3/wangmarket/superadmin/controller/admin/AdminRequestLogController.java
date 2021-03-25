package com.xnx3.wangmarket.superadmin.controller.admin;

import java.util.ArrayList;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.DateUtil;
import com.xnx3.exception.NotReturnValueException;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.net.AliyunLogPageUtil;
import com.xnx3.wangmarket.admin.controller.BaseController;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.vo.RequestLogDayLineVO;
import com.xnx3.wangmarket.domain.Log;

/**
 * 访问统计
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/requestLog/")
public class AdminRequestLogController extends BaseController {
	
	//爬虫的useragent
	public static String[] spiderNameArray = {		"Baiduspider",	"Sogou web spider",	"Googlebot",			"YisouSpider"	,	"bingbot",		"spiderman",			"AhrefsBot"}; 
	//爬虫的描述说明
	public static String[] spiderExplainArray = {	"百度搜索爬虫",	"搜狗搜索爬虫",		"Google搜索爬虫",		"神马搜索爬虫",	"必应搜索爬虫",	"Java爬虫 Spiderman","AhrefsBot爬虫"}; 
	
	
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	
	/**
	 * 访问统计，折线图
	 */
	@RequiresPermissions("adminRequestLogFangWen")
	@RequestMapping("fangwentongji${url.suffix}")
	public String fangwentongji(HttpServletRequest request, Model model){
		if(Log.aliyunLogUtil == null){
			return error(model, "您未开启网站访问相关的日志服务！无法查看网站访问日志");
		}
		ActionLogUtil.insert(request, "进入总管理后台－日志访问-访问统计页面");
		return "/superadmin/requestLog/fangwentongji";
	}
	
	/**
	 * 折线图，当天、昨天，24小时，每小时的访问情况
	 */
	@RequiresPermissions("adminRequestLogFangWen")
	@RequestMapping("dayLineForCurrentDay${url.suffix}")
	@ResponseBody
	public RequestLogDayLineVO dayLineForCurrentDay(HttpServletRequest request) throws LogException{
		RequestLogDayLineVO vo = new RequestLogDayLineVO();
		
		//当前10位时间戳
		int currentTime = DateUtil.timeForUnix10();
		String query = "Mozilla | timeslice 1h | count as c";
		
		//今日访问量统计
		ArrayList<QueriedLog> jinriQlList = Log.aliyunLogUtil.queryList(query, "", DateUtil.getDateZeroTime(currentTime), currentTime, 0, 100, true);
		
		JSONArray jsonArrayFangWen = new JSONArray();	//今日访问量，pv
		for (int i = 0; i < jinriQlList.size(); i++) {
			LogItem li = jinriQlList.get(i).GetLogItem();
			JSONObject json = JSONObject.fromObject(li.ToJsonString());
			jsonArrayFangWen.add(json.getInt("c"));
		}
		vo.setJsonArrayFangWen(jsonArrayFangWen);
		
		
		//昨日
		//1天前的时间戳
		int startTime = DateUtil.getDateZeroTime(currentTime - 86400);
		ArrayList<QueriedLog> zuoriQlList = Log.aliyunLogUtil.queryList(query, "", startTime, DateUtil.getDateZeroTime(currentTime), 0, 100, true);
		JSONArray jsonArrayFangWenZuoRi = new JSONArray();	//昨日访问量，pv
		for (int i = 0; i < zuoriQlList.size(); i++) {
			LogItem li = zuoriQlList.get(i).GetLogItem();
			JSONObject json = JSONObject.fromObject(li.ToJsonString());
			jsonArrayFangWenZuoRi.add(json.getInt("c"));
		}
		vo.setJsonArrayFangWenZuoRi(jsonArrayFangWenZuoRi);
		
		JSONArray jsonArrayDate = JSONArray.fromObject("[\"0\",\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\",\"16\",\"17\",\"18\",\"19\",\"20\",\"21\",\"22\",\"23\",\"24\"]");	//小时，比如1、2、3、4h等
		vo.setJsonArrayDate(jsonArrayDate);
		
		ActionLogUtil.insert(request, "总管理后台，获取当天、昨天的按小时访问数据统计记录");
		
		return vo;
	}
	
	/**
	 * 折线图，当月(最近30天)，每天的访问情况
	 */
	@RequiresPermissions("adminRequestLogFangWen")
	@RequestMapping("dayLineForCurrentMonth${url.suffix}")
	@ResponseBody
	public RequestLogDayLineVO dayLineForCurrentMonth(HttpServletRequest request) throws LogException{
		RequestLogDayLineVO vo = new RequestLogDayLineVO();
		
		//当前10位时间戳
		int currentTime = DateUtil.timeForUnix10();
		String query = "Mozilla | timeslice 24h | count as c";
		
		//当月访问量统计
		ArrayList<QueriedLog> jinriQlList = Log.aliyunLogUtil.queryList(query, "", DateUtil.getDateZeroTime(currentTime - 2592000), currentTime, 0, 100, true);
		
		JSONArray jsonArrayDate = new JSONArray();	//天数
		JSONArray jsonArrayFangWen = new JSONArray();	//某天访问量，pv
		for (int i = 0; i < jinriQlList.size(); i++) {
			LogItem li = jinriQlList.get(i).GetLogItem();
			JSONObject json = JSONObject.fromObject(li.ToJsonString());
			try {
				jsonArrayDate.add(DateUtil.dateFormat(json.getInt("logtime"), "MM-dd"));
			} catch (NotReturnValueException e) {
				e.printStackTrace();
			}
			jsonArrayFangWen.add(json.getInt("c"));
		}
		vo.setJsonArrayFangWen(jsonArrayFangWen);
		vo.setJsonArrayDate(jsonArrayDate);
		
		ActionLogUtil.insert(request, "总管理后台，获取最近30天的访问数据统计记录");
		
		return vo;
	}
	

	/**
	 * 网站访问记录日志列表
	 * @throws LogException 
	 */
	@RequiresPermissions("adminLogList")
	@RequestMapping("fangwenList${url.suffix}")
	public String fangwenList(HttpServletRequest request,Model model) throws LogException{
		if(Log.aliyunLogUtil == null){
			return error(model, "您未开启网站访问相关的日志服务！无法查看网站访问日志");
		}
		AliyunLogPageUtil log = new AliyunLogPageUtil(Log.aliyunLogUtil);
		
		//得到当前页面的列表数据
		JSONArray jsonArray = log.list("", "", true, 100, request);
		
		//得到当前页面的分页相关数据（必须在执行了list方法获取列表数据之后，才能调用此处获取到分页）
		Page page = log.getPage();
		//设置分页，出现得上几页、下几页跳转按钮的个数
		page.setListNumber(3);
		
		ActionLogUtil.insert(request, "查看总管理后台网站访问日志列表", "第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("list", jsonArray);
		model.addAttribute("page", page);
		return "/superadmin/requestLog/fangwenList";
	}
	
}