package com.xnx3.wangmarket.plugin.formManage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.controller.BaseController;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.plugin.formManage.bean.Frequency;
import com.xnx3.wangmarket.plugin.formManage.entity.Form;
import com.xnx3.wangmarket.plugin.formManage.entity.FormData;

/**
 * CMS模式下，输入模型相关操作
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class FormManagePluginController extends BaseController {

	@Resource
	private SqlService sqlService;

	/**
	 * 反馈频率控制，避免被人利用而已提交
	 */
	public static Map<String, Frequency> frequencyMap = new HashMap<String, Frequency>();
	static{
		//开启一个线程，每天都清理一次 frequencyMap的数据
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1*24*60*60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				frequencyMap.clear();
			}
		}).start();
	}
	//两次提交表单的时间间隔。控制在一小时。1 * 60 * 60;
	public final static int FeedbackTimeInterval = 5;
	public final static int textMaxLength = 10000;	//表单提交的formData.text信息最大字数允许在1万以内
	
	
	/**
	 * 当前表单反馈信息的列表
	 */
	@RequestMapping("/form/list${url.suffix}")
	public String list(HttpServletRequest request, Model model){
		AliyunLog.addActionLog(getSiteId(), "查看反馈信息列表");
		
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"state="});
		sql.appendWhere("siteid = "+getSiteId());
	    //查询user数据表的记录总条数。 传入的user：数据表的名字为user
	    int count = sqlService.count("form", sql.getWhere());
	    //创建分页，并设定每页显示15条
	    Page page = new Page(count, 15, request);
	    //创建查询语句，只有SELECT、FROM，原生sql查询。其他的where、limit等会自动拼接
	    sql.setSelectFromAndPage("SELECT * FROM form", page);
	    sql.setDefaultOrderBy("id DESC");
	    //因只查询的一个表，所以可以将查询结果转化为实体类，用List接收。
	    List<Form> list = sqlService.findBySql(sql, Form.class);
	    //将展示的列表数据记录传到页面以供显示
	    model.addAttribute("list", list);
	    //将分页信息传到页面以供显示底部分页
	    model.addAttribute("page", page);
		return "plugin/formManage/list";
	}
	

	/**
	 * 后台查看回馈信息详情
	 */
	@RequestMapping("/form/view${url.suffix}")
	public String view(HttpServletRequest request, Model model,
			@RequestParam(value = "id", required = false , defaultValue="0") int id){
		AliyunLog.addActionLog(getSiteId(), "查看反馈信息详情");
		
		Form form = sqlService.findById(Form.class, id);
		if(form == null){
			return error(model,"查看的信息不存在");
		}
		if(form.getSiteid() - getSiteId() != 0){
			return error(model, "信息不属于你，无法查看");
		}
		
		//若是信息为未读状态，设置为已读
		if(form.getState() == null || form.getState() - Form.STATE_UNREAD == 0){
			form.setState(Form.STATE_READ);
			sqlService.save(form);
		}
		
		//取得分表的 data
		FormData formData = sqlService.findById(FormData.class, id);
		if(formData == null){
			return error(model,"异常，该信息的内容不存在");
		}
		
		JSONArray jsonArray = JSONArray.fromObject(formData.getText());
		
		model.addAttribute("form", form);
	    model.addAttribute("jsonArray", jsonArray);
		return "plugin/formManage/view";
	}
	
	
	
	
	/**
	 * 提交反馈信息，只限 post 提交
	 * @param id 要删除的输入模型的id，对应 {@link InputModel}.id
	 */
	@RequestMapping(value="formAdd${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO formAdd(HttpServletRequest request, Model model,
			@RequestParam(value = "siteid", required = false , defaultValue="0") int siteid,
			@RequestParam(value = "title", required = false , defaultValue="") String title){
		String ip = IpUtil.getIpAddress(request);
		Frequency frequency = frequencyMap.get(ip);
		int currentTime = DateUtil.timeForUnix10();	//当前10位时间戳
		//今天尚未提交过，那么创建一个记录
		if(frequency == null){
			frequency = new Frequency();
			frequency.setIp(ip);
		}
		
		//判断当前是否允许提交反馈信息，如果不允许，需要记录，并返回不允许的提示。
		if(frequency.getForbidtime() > currentTime){
			//间隔时间太短，不允许提交反馈信息
			frequency.setErrorNumber(frequency.getErrorNumber()+1);
			frequencyMap.put(ip, frequency);	//临时存储，这个存储时间是一天,每天清除一次
			return error("距离上次提交时间太短，等会再试试吧");
		}
		
		/** 下面就是允许提交的逻辑处理了 **/
		frequency.setLasttime(currentTime);	//设置当前为最后一次提交的时间
		frequency.setForbidtime(currentTime + FeedbackTimeInterval);	//设置下次允许提交反馈的时间节点，这个时间节点之前是不允许在此提交的
		frequencyMap.put(ip, frequency);	//临时存储，这个存储时间是一天,每天清除一次
		
		
		title = StringUtil.filterXss(title);
		if(siteid <= 0){
			return error("请传入您的站点id（siteid），不然，怎么知道此反馈表单是属于哪个网站的呢？");
		}
		
		Form form = new Form();
		form.setAddtime(DateUtil.timeForUnix10());
		form.setSiteid(siteid);
		form.setState(Form.STATE_UNREAD);
		form.setTitle(title);
		sqlService.save(form);
		if(form.getId() != null && form.getId() > 0){
			//成功，进而存储具体内容。存储内容时，首先要从提交的数据中，便利出所有表单数据.这里是原始提交的结果，需要进行xss过滤
			Map<String, String[]> params = new HashMap<String, String[]>();
			params.putAll(request.getParameterMap());
			//删除掉siteid、title的参数
			params.remove("siteid");
			if(params.get("title") != null){
				params.remove("title");
			}
			
			JSONArray jsonArray = new JSONArray();	//text文本框所存储的内容
			for (Map.Entry<String, String[]> entry : params.entrySet()) { 
				JSONObject json = new JSONObject();
				JSONArray valueJsonArray = new JSONArray();
				
				for (int i = 0; i < entry.getValue().length; i++) {
					valueJsonArray.add(StringUtil.filterXss(entry.getValue()[i]));
				}
				json.put(StringUtil.filterXss(entry.getKey()), valueJsonArray);
				jsonArray.add(json);
			}
			String text = jsonArray.toString();
			if(text.length() > textMaxLength){
				return error("信息太长，非法提交！");
			}
			
			FormData formData = new FormData();
			formData.setId(form.getId());
			formData.setText(text);
			sqlService.save(formData);
			
			//记录日志
			AliyunLog.addActionLog(form.getId(), "提交表单反馈", form.getTitle());
			
			return success();
		}else{
			return error("保存失败");
		}
		
	}
	
}