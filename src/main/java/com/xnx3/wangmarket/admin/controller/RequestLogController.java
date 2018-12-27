package com.xnx3.wangmarket.admin.controller;

import java.util.ArrayList;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.net.AliyunLogPageUtil;
import com.xnx3.wangmarket.admin.service.SiteService;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.admin.vo.RequestLogDayLineVO;
import com.xnx3.wangmarket.admin.vo.RequestLogItemListVO;
import com.xnx3.wangmarket.domain.G;
import com.xnx3.wangmarket.domain.Log;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 访问统计
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/requestLog/")
public class RequestLogController extends BaseController {
	
	//爬虫的useragent
	public static String[] spiderNameArray = {		"Baiduspider",	"Sogou web spider",	"Googlebot",			"YisouSpider"	,	"bingbot",		"spiderman",			"AhrefsBot",		"360Spider"};
	//爬虫的描述说明
	public static String[] spiderExplainArray = {	"百度搜索爬虫",	"搜狗搜索爬虫",		"Google搜索爬虫",		"神马搜索爬虫",	"必应搜索爬虫",	"Java爬虫 Spiderman","AhrefsBot爬虫","360搜索(so.com)爬虫"}; 
	
	
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;
	
	/**
	 * 访问统计，折线图
	 */
	@RequestMapping("fangwentongji${url.suffix}")
	public String fangwentongji(HttpServletRequest request, Model model){
		if(Log.aliyunLogUtil == null){
			return error(model, "未开启网站访问日志统计");
		}
		AliyunLog.addActionLog(getSiteId(), "进入日志访问-访问统计页面");
		return "requestLog/fangwentongji";
	}
	
	
	/**
	 * 爬虫统计，饼状图，SEO优化相关
	 */
	@RequestMapping("pachongtongji${url.suffix}")
	public String pachongtongji(HttpServletRequest request, Model model){
		if(Log.aliyunLogUtil == null){
			return error(model, "未开启网站访问日志统计");
		}
		AliyunLog.addActionLog(getSiteId(), "进入日志访问-爬虫统计页面");
		return "requestLog/pachongtongji";
	}
	
	/**
	 * 折线图，当天、昨天，24小时，每小时的访问情况
	 */
	@RequestMapping("dayLineForCurrentDay${url.suffix}")
	@ResponseBody
	public RequestLogDayLineVO dayLineForCurrentDay(HttpServletRequest request) throws LogException{
		RequestLogDayLineVO vo = new RequestLogDayLineVO();
		
		//当前10位时间戳
		int currentTime = DateUtil.timeForUnix10();
		String query = "siteid="+getSiteId()+" | timeslice 1h | count as c";
		
		//今日访问量统计
		ArrayList<QueriedLog> jinriQlList = Log.aliyunLogUtil.queryList(query, "", DateUtil.getDateZeroTime(currentTime), currentTime, 0, 100, true);
		
		JSONArray jsonArrayFangWen = new JSONArray();	//今日访问量，pv
		String countString = null;
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
		
		AliyunLog.addActionLog(getSiteId(), "获取当天、昨天的按小时访问数据统计记录");
		
		return vo;
	}
	
	/**
	 * 折线图，当月(最近30天)，每天的访问情况
	 */
	@RequestMapping("dayLineForCurrentMonth${url.suffix}")
	@ResponseBody
	public RequestLogDayLineVO dayLineForCurrentMonth(HttpServletRequest request) throws LogException{
		RequestLogDayLineVO vo = new RequestLogDayLineVO();
		
		//当前10位时间戳
		int currentTime = DateUtil.timeForUnix10();
		String query = "siteid="+getSiteId()+" | timeslice 24h | count as c";
		
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
		
		AliyunLog.addActionLog(getSiteId(), "获取最近30天的访问数据统计记录");
		
		return vo;
	}
	

	/**
	 * 爬虫访问列表，列出最近7天内，最近的100条爬虫记录
	 */
	@RequestMapping("spiderList${url.suffix}")
	@ResponseBody
	public RequestLogItemListVO spiderList(HttpServletRequest request) throws LogException{
		RequestLogItemListVO vo = new RequestLogItemListVO();
		
		//当前10位时间戳
		int currentTime = DateUtil.timeForUnix10();
		String query = "siteid="+getSiteId();
		String spider = null;
		for (int i = 0; i < spiderNameArray.length; i++) {
			if(spider == null){
				spider = spiderNameArray[i];
			}else{
				spider = spider + " or " + spiderNameArray[i];
			}
		}
		query = query + " and ("+spider+")";
		
		//当月访问量统计
		ArrayList<QueriedLog> jinriQlList = Log.aliyunLogUtil.queryList(query, "", DateUtil.getDateZeroTime(currentTime - 604800), currentTime, 0, 100, true);
		
		JSONArray jsonArray = new JSONArray();	//某天访问量，pv
		for (int i = 0; i < jinriQlList.size(); i++) {
			LogItem li = jinriQlList.get(i).GetLogItem();
			JSONObject json = JSONObject.fromObject(li.ToJsonString());
			try {
				json.put("logtimeString", DateUtil.dateFormat(json.getInt("logtime"), "MM-dd HH:mm"));
			} catch (NotReturnValueException e) {
				e.printStackTrace();
			}
			
			UserAgent ua = UserAgent.parseUserAgentString(json.getString("userAgent"));
			json.put("os", ua.getOperatingSystem());
			json.put("browser", ua.getBrowser());
			if(ua.getOperatingSystem().getName().equals("Unknown")){
				String userAgent = json.getString("userAgent");
				//没有发现是哪个浏览器，那可能是爬虫
				for (int j = 0; j < spiderNameArray.length; j++) {
					if(userAgent.indexOf(spiderNameArray[j]) > -1){
						json.put("os", spiderExplainArray[j]);
					}
				}
				
				if(json.get("os") == null){
					if(userAgent.equals("Mozilla")){
						//忽略
					}else{
						System.out.println("未发现的useragent ： "+json.toString());
					}
				}
			}
			
			jsonArray.add(json);
			
		}
		vo.setList(jsonArray);
		
		AliyunLog.addActionLog(getSiteId(), "获取最近7天内，最近的100条访问记录");
		
		return vo;
	}
	

	/**
	 * 获取当前搜索引擎爬虫的爬取情况
	 */
	@RequestMapping("spiderCount${url.suffix}")
	@ResponseBody
	public BaseVO spiderCount(HttpServletRequest request) throws LogException{
		BaseVO vo = new BaseVO();
		
		//当前10位时间戳
		int currentTime = DateUtil.timeForUnix10();
		
		//爬虫的具体访问统计
		long[] spiderRequestCountArray = new long[spiderNameArray.length];
		//最近7天的爬虫统计
		for (int i = 0; i < spiderNameArray.length; i++) {
			spiderRequestCountArray[i] = Log.aliyunLogUtil.queryCount("siteid="+getSiteId()+" AND "+spiderNameArray[i], "", DateUtil.getDateZeroTime(currentTime - 604800), currentTime);
		}
		
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < spiderNameArray.length; i++) {
			if(spiderRequestCountArray[i] > 0){
				JSONObject json = new JSONObject();
				json.put("name", spiderExplainArray[i]);
				json.put("value", spiderRequestCountArray[i]);
				jsonArray.add(json);
			}
		}
		vo.setInfo(jsonArray.toString());
		
		AliyunLog.addActionLog(getSiteId(), "获取7天爬虫抓取记录");
		return vo;
	}
	

	/**
	 * 用户查看自己的操作日志列表
	 */
	@RequestMapping("actionLogList${url.suffix}")
	public String actionLogList(HttpServletRequest request, Model model) throws LogException{
		if(AliyunLog.aliyunLogUtil == null){
			return error(model, "未开启日志服务");
		}
		AliyunLogPageUtil log = new AliyunLogPageUtil(AliyunLog.aliyunLogUtil);
		JSONArray jsonArray = log.list("userid="+getUserId(), "", false, 15, request);
		Page page = log.getPage();
		AliyunLog.addActionLog(getSiteId(), "进入日志访问-操作日志页面第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("list", jsonArray);
		model.addAttribute("page", page);
		return "requestLog/actionLogList";
	}
	
}