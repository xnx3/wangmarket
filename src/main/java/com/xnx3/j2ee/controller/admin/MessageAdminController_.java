package com.xnx3.j2ee.controller.admin;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.Message;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.MessageService;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 信息管理
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/message")
public class MessageAdminController_ extends BaseController {

	@Resource
	private MessageService messageService;
	@Resource
	private SqlService sqlService;
	
	/**
	 * 信息列表
	 * @param request {@link HttpServletRequest}
	 */
	@RequiresPermissions("adminMessageList")
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchTable("message");
		sql.setSearchColumn(new String[]{"id=","senderid=","recipientid="});
		sql.appendWhere("message.isdelete = "+Message.ISDELETE_NORMAL);
		int count = sqlService.count("message", sql.getWhere());
		Page page = new Page(count, SystemUtil.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT message.*,message_data.content, (SELECT user.nickname FROM user WHERE user.id=message.recipientid) AS other_nickname ,(SELECT user.nickname FROM user WHERE user.id=message.senderid) AS self_nickname FROM message ,message_data ,user ", page);
		sql.appendWhere("message.id=message_data.id");
		sql.setGroupBy("message.id");
		sql.setDefaultOrderBy("message.id DESC");
		List<Map<String, Object>> list = sqlService.findMapBySql(sql);
		
		ActionLogUtil.insert(request, "总管理后台站内信息列表", "第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "/iw/admin/message/list";
	}
	
	/**
	 * 删除信息
	 * @param id 信息的id，Message.id
	 */
	@RequiresPermissions("adminMessageDelete")
	@RequestMapping(value="delete${url.suffix}", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "id", required = true) int id, Model model, HttpServletRequest request){
		BaseVO baseVO = messageService.delete(id);
		if(baseVO.getResult() == BaseVO.SUCCESS){
			ActionLogUtil.insertUpdateDatabase(request, "总管理后台删除站内信息");
			return success(model, "删除成功！");
		}else{
			ActionLogUtil.insertUpdateDatabase(request, "总管理后台删除站内信息", "失败："+baseVO.getInfo());
			return error(model, baseVO.getInfo());
		}
	}
}
