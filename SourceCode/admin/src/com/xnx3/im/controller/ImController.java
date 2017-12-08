package com.xnx3.im.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.DateUtil;
import com.xnx3.im.Global;
import com.xnx3.im.entity.Im;
import com.xnx3.im.service.ImService;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.net.AliyunLogPageUtil;
import com.xnx3.admin.cache.KeFu;
import com.xnx3.admin.controller.BaseController;
import com.xnx3.admin.util.AliyunLog;
import com.xnx3.domain.G;

/**
 * IM相关。这里为网站游客与建站客户的沟通设置。网站管理员设置自己网站是否开通IM，以及如何使用等
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/im")
public class ImController extends BaseController{
	@Resource
	private SqlService sqlService;
	@Resource
	private UserService userService;
	@Resource
	private ImService imService;
	
	//默认客服头像
	public static String defaultHead = "http://res.weiunity.com/image/imqq.jpg";

	/**
	 * 设置首页
	 */
	@RequestMapping("set")
	public String set(Model model){
		User user = getUser(); 
		
		model.addAttribute("head", userService.getHead(defaultHead));
		model.addAttribute("user", user);
		model.addAttribute("im", imService.getImByCache());
		AliyunLog.addActionLog(getSiteId(), "打开IM设置页面");
		return "im/set";
	}
	

	/**
	 * 更改当前网站，是否使用在线客服功能
	 * @param use 是否使用，1使用，0不使用
	 */
	@RequestMapping("useKefu")
	@ResponseBody
	public BaseVO useKefu(@RequestParam(value = "use", required = false , defaultValue="0") short use){
		Im si = imService.getImByDB();
		
		si.setUseKefu(use - Im.USE_TRUE == 0 ? Im.USE_TRUE:Im.USE_FALSE);
		sqlService.save(si);
		//缓存当前最新的im
		imService.updateImForCache(si);
		
		//更新site.js缓存。如果是网站，有site，则会更新。如果不是网站，是代理或者其他，那么没有site，自然就不会更新
		new com.xnx3.admin.cache.Site().site(getSite(), si);
		
		//创建新的kefu.js缓存文件
		User user = getUser();
		new KeFu().kefuInfo(getSite(), user.getNickname(), userService.getHead(defaultHead), imService.getImByCache());
		//通知IM应用
		noticeImServer(user, si);
		
		AliyunLog.addActionLog(getUserId(), (use - Im.USE_TRUE == 0 ? "开启":"关闭")+"在线客服功能");
		return success();
	}
	

	/**
	 * 客服名字修改
	 * @param request 需post/get传入nickname 客服的名字，即user.nickname，不可为空。
	 */
	@RequestMapping("propertySave")
	@ResponseBody
	public BaseVO propertySave(HttpServletRequest request){
		BaseVO vo = userService.updateNickname(request);
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			//创建新的kefu.js缓存文件
			User user = getUser();
			new KeFu().kefuInfo(getSite(), user.getNickname(), userService.getHead(defaultHead), imService.getImByCache());
			
			AliyunLog.addActionLog(getSiteId(), "更新当前客服名字成功", getUser().getNickname());
		}
		return vo;
	}
	
	/**
	 * 当前客服的属性保存，更新客服的头像
	 * @param head 上传的头像文件，可为空
	 */
	@RequestMapping("headSave")
	@ResponseBody
	public UploadFileVO headSave(@RequestParam("head") MultipartFile head){
		UploadFileVO vo = userService.updateHeadByOSS(head);
		if(vo.getResult() - UploadFileVO.SUCCESS == 0){
			User user = getUser();
			//创建新的kefu.js缓存文件
			new KeFu().kefuInfo(getSite(), user.getNickname(), userService.getHead(defaultHead), imService.getImByCache());
			
			AliyunLog.addActionLog(user.getId(), "更新客服头像成功", vo.getUrl());
		}
		return vo;
	}
	

	/**
	 * 客服自动回复的内容修改
	 * @param request 需post/get传入nickname 客服的名字，即user.nickname，不可为空。
	 */
	@RequestMapping("autoReplySave")
	@ResponseBody
	public BaseVO autoReplySave(@RequestParam(value = "text", required = false , defaultValue="") String text){
		User user = getUser();
		Im si = imService.getImByDB();
		si.setAutoReply(filter(text));
		sqlService.save(si);
		//进行缓存
		imService.updateImForCache(si);
		
		//创建新的kefu.js缓存文件
		new KeFu().kefuInfo(getSite(), user.getNickname(), userService.getHead(defaultHead), si);
		
		//通知IM应用
		noticeImServer(user, si);
				
		AliyunLog.addActionLog(user.getId(), "更新当前客服自动回复内容", si.getAutoReply());
		
		return success();
	}

	/**
	 * 更改当前网站，是否启用，当不在线时，自动将客户咨询内容发送到指定邮箱
	 * @param use 是否使用，1使用，0不使用
	 */
	@RequestMapping("useOffLineEmail")
	@ResponseBody
	public BaseVO useOffLineEmail(@RequestParam(value = "use", required = false , defaultValue="0") short use){
		User user = getUser();
		Im im = imService.getImByDB();
		im.setUseOffLineEmail((use - Im.USE_TRUE == 0) ? Im.USE_TRUE : Im.USE_FALSE);
		sqlService.save(im);
		//进行缓存
		imService.updateImForCache(im);
		//通知IM应用
		noticeImServer(user, im);
		
		AliyunLog.addActionLog(getSiteId(), (use - Im.USE_TRUE == 0 ? "开启":"关闭")+"离线邮件通知");
		return success();
	}
	
	//通知IM应用
	private void noticeImServer(User user, Im si){
		if(user == null){
			return;
		}
		boolean useKefu = false;
		if(si.getUseKefu() != null && si.getUseKefu() - Im.USE_TRUE == 0){
			useKefu = true;
		}
		
		imService.updateIMServer(useKefu, si);
	}

	/**
	 * 更改当前网站，当不在线时，自动将客户咨询内容发送到指定邮箱，这里便是要改动的邮箱
	 * @param use 是否使用，1使用，0不使用
	 */
	@RequestMapping("emailSave")
	@ResponseBody
	public BaseVO emailSave(@RequestParam(value = "email", required = false, defaultValue="") String email){
		User user = getUser();
		Im si = imService.getImByDB();
		si.setEmail(filter(email));
		sqlService.save(si);
		//进行缓存
		imService.updateImForCache(si);
		//通知IM应用
		noticeImServer(user, si);
		
		AliyunLog.addActionLog(user.getId(), "修改离线时游客信息的邮件通知邮箱", si.getEmail());
		return success();
	}
	
	@RequestMapping("previewByToken")
	public String previewByToken(HttpServletRequest request,Model model,
			@RequestParam(value = "token", required = false , defaultValue="") String token){
		//进行安全过滤
		token = token.replaceAll("\\s*", "");
		token = filter(token);
		if(token.length() != 64){
			return error(model, "授权码错误");
		}else{
			int currentTime = DateUtil.timeForUnix10();
			int startTime = currentTime - 86400*30;	//30天内有效
			try {
				AliyunLogPageUtil log = new AliyunLogPageUtil(Global.aliyunLogUtil);
				JSONArray jsonArray = log.listForJSONArray("receiveSocketUuid="+token, "", false, startTime, currentTime, 100, request);
				model.addAttribute("list", jsonArray);
			} catch (LogException e) {
				e.printStackTrace();
			}
		}
		
		return "im/perviewByToken";
	}
	
	/**
	 * 历史聊天人列表，会话列表，每一项都是一个人
	 */
	@RequestMapping("hostoryChatList")
	public String hostoryChatList(HttpServletRequest request,Model model){
		if(Global.aliyunLogUtil == null){
			return error(model, "您未开启IM客服访问相关的日志服务！");
		}
		User user = getUser();
		int currentTime = DateUtil.timeForUnix10();
		int startTime = currentTime - 86400*30;	//30天内有效
		try {
			AliyunLogPageUtil log = new AliyunLogPageUtil(Global.aliyunLogUtil);
			JSONArray jsonArray = log.listForJSONArray("receiveId = "+user.getId()+" | select max(sendId) , map_agg('array',sendUserName) as sendUserName,max(sendId) as sendIds, count(*) as count, max(__time__) as time group by sendId order by time desc limit 100", "", false, startTime, currentTime, 100, request);
			model.addAttribute("list", jsonArray);
		} catch (LogException e) {
			e.printStackTrace();
		}
		
		return "im/hostoryChatList";
	}
	

	/**
	 * 聊天记录，跟某个人之间，一个会话的聊天记录，两个人之间的聊天记录
	 * @param id 跟自己会话的那个人的id，对方的id
	 */
	@RequestMapping("chatRecord")
	public String chatRecord(HttpServletRequest request,Model model,
			@RequestParam(value = "id", required = false , defaultValue="0") long id){
		if(id == 0){
			return error(model, "请输入要查看得对方得id编号");
		}
		User user = getUser();
		int currentTime = DateUtil.timeForUnix10();
		int startTime = currentTime - 86400*30;	//30天内有效
		try {
			AliyunLogPageUtil log = new AliyunLogPageUtil(Global.aliyunLogUtil);
			JSONArray jsonArray = log.listForJSONArray("(receiveId = "+user.getId()+" and sendId = "+id+" ) or (receiveId = "+id+" and sendId = "+user.getId()+" )", "", false, startTime, currentTime, 100, request);
			JSONArray ja = new JSONArray();	//将其倒序
			for (int i = jsonArray.size()-1; i >-1 ; --i) {
				ja.add(jsonArray.get(i));
			}
			model.addAttribute("list", ja);
		} catch (LogException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("user", user);
		return "im/chatRecord";
	}
	
	
}
