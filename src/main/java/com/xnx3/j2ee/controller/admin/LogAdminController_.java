package com.xnx3.j2ee.controller.admin;

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
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.LogLineGraphVO;
import com.xnx3.net.AliyunLogPageUtil;

/**
 * 日志管理，使用的是阿里云日志服务
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/log")
public class LogAdminController_ extends BaseController{
	@Resource
	private SqlService sqlService;
	
	/**
	 * 日志列表
	 * @throws LogException 
	 */
	@RequiresPermissions("adminLogList")
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request,Model model) throws LogException{
		if(ActionLogUtil.aliyunLogUtil == null){
			return error(model, "您未开启日志服务！无法查看操作日志");
		}
		
		AliyunLogPageUtil log = new AliyunLogPageUtil(ActionLogUtil.aliyunLogUtil);
		
		//得到当前页面的列表数据
		JSONArray jsonArray = log.list("", "", true, SystemUtil.getInt("LIST_EVERYPAGE_NUMBER"), request);
		
		//得到当前页面的分页相关数据（必须在执行了list方法获取列表数据之后，才能调用此处获取到分页）
		Page page = log.getPage();
		//设置分页，出现得上几页、下几页跳转按钮的个数
		page.setListNumber(2);
		
		ActionLogUtil.insert(request, "查看总管理后台日志列表", "第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("list", jsonArray);
		model.addAttribute("page", page);
		return "/iw/admin/log/list";
	}
	
	/**
	 * 访问统计，折线图
	 */
	@RequiresPermissions("adminLogCartogram")
	@RequestMapping("cartogram${url.suffix}")
	public String cartogram(HttpServletRequest request, Model model){
		if(ActionLogUtil.aliyunLogUtil == null){
			return error(model, "您未开启日志服务！无法查看操作日志");
		}
		ActionLogUtil.insert(request, "查看总管理后台操作的统计图表");
		return "/iw/admin/log/cartogram";
	}
	

	/**
	 * 折线图，当天、昨天，24小时，每小时的访问情况
	 */
	@RequiresPermissions("adminLogCartogram")
	@RequestMapping("dayLineForCurrentDay${url.suffix}")
	@ResponseBody
	public LogLineGraphVO dayLineForCurrentDay(HttpServletRequest request) throws LogException{
		LogLineGraphVO vo = new LogLineGraphVO();
		
		ActionLogUtil.insert(request, "管理后台操作日志-折线图，当天、昨天，24小时，每小时的访问情况");
		
		//当前10位时间戳
		int currentTime = DateUtil.timeForUnix10();
		String query = "Mozilla | timeslice 1h | count as c";
		
		//今日访问量统计
		ArrayList<QueriedLog> jinriQlList = ActionLogUtil.aliyunLogUtil.queryList(query, "", DateUtil.getDateZeroTime(currentTime), currentTime, 0, 100, true);
		
		JSONArray jsonArrayFangWen = new JSONArray();	//今日访问量，pv
		String countString = null;
		for (int i = 0; i < jinriQlList.size(); i++) {
			LogItem li = jinriQlList.get(i).GetLogItem();
			JSONObject json = JSONObject.fromObject(li.ToJsonString());
			jsonArrayFangWen.add(json.getInt("c"));
		}
		vo.setDataArray(jsonArrayFangWen);
		
		
		//昨日
		//1天前的时间戳
		int startTime = DateUtil.getDateZeroTime(currentTime - 86400);
		
		ArrayList<QueriedLog> zuoriQlList = ActionLogUtil.aliyunLogUtil.queryList(query, "", startTime, DateUtil.getDateZeroTime(currentTime), 0, 100, true);
		JSONArray jsonArrayFangWenZuoRi = new JSONArray();	//昨日访问量，pv
		for (int i = 0; i < zuoriQlList.size(); i++) {
			LogItem li = zuoriQlList.get(i).GetLogItem();
			JSONObject json = JSONObject.fromObject(li.ToJsonString());
			jsonArrayFangWenZuoRi.add(json.getInt("c"));
		}
		vo.setDataArray2(jsonArrayFangWenZuoRi);
		
		JSONArray jsonArrayDate = JSONArray.fromObject("[\"0\",\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\",\"16\",\"17\",\"18\",\"19\",\"20\",\"21\",\"22\",\"23\",\"24\"]");	//小时，比如1、2、3、4h等
		vo.setNameArray(jsonArrayDate);
		
		return vo;
	}
	
	/**
	 * 折线图，当月(最近30天)，每天的访问情况
	 */
	@RequiresPermissions("adminLogCartogram")
	@RequestMapping("dayLineForCurrentMonth${url.suffix}")
	@ResponseBody
	public LogLineGraphVO dayLineForCurrentMonth(HttpServletRequest request) throws LogException{
		LogLineGraphVO vo = new LogLineGraphVO();
		ActionLogUtil.insert(request, "管理后台操作日志-折线图，当月(最近30天)，每天的访问情况");
		
		//当前10位时间戳
		int currentTime = DateUtil.timeForUnix10();
		String query = "Mozilla | timeslice 24h | count as c";
		
		//当月访问量统计
		ArrayList<QueriedLog> jinriQlList = ActionLogUtil.aliyunLogUtil.queryList(query, "", DateUtil.getDateZeroTime(currentTime - 2592000), currentTime, 0, 100, true);
		
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
		vo.setDataArray(jsonArrayFangWen);
		vo.setNameArray(jsonArrayDate);
		
		return vo;
	}
}
