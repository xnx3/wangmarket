package com.xnx3.wangmarket.plugin.form.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xnx3.j2ee.pluginManage.controller.BasePluginController;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;
import com.xnx3.wangmarket.admin.util.SessionUtil;
import com.xnx3.wangmarket.plugin.form.entity.Form;
import com.xnx3.wangmarket.plugin.form.entity.FormData;

/**
 * Form插件
 * @author 管雷鸣
 */
@Controller(value="FormPluginController")
@RequestMapping("/plugin/form/")
public class ManageController extends BasePluginController {

	@Resource
	private SqlService sqlService;

	
	/**
	 * 当前表单反馈信息的列表
	 */
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request, Model model){
		if(!haveSiteAuth()){
			return error(model, "请登录查看");
		}
		
		ActionLogUtil.insert(request, "查看 plugin formManage 反馈信息列表");
		Site site = SessionUtil.getSite();
		
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"state="});
		sql.appendWhere("siteid = "+site.getId());
	    //查询user数据表的记录总条数。 传入的user：数据表的名字为user
	    int count = sqlService.count("plugin_form", sql.getWhere());
	    //创建分页，并设定每页显示15条
	    Page page = new Page(count, 15, request);
	    //创建查询语句，只有SELECT、FROM，原生sql查询。其他的where、limit等会自动拼接
	    sql.setSelectFromAndPage("SELECT * FROM plugin_form", page);
	    sql.setDefaultOrderBy("id DESC");
	    //因只查询的一个表，所以可以将查询结果转化为实体类，用List接收。
	    List<Form> list = sqlService.findBySql(sql, Form.class);
	    //将展示的列表数据记录传到页面以供显示
	    model.addAttribute("list", list);
	    //将分页信息传到页面以供显示底部分页
	    model.addAttribute("page", page);
		return "plugin/form/list";
	}
	

	/**
	 * 后台查看回馈信息详情
	 */
	@RequestMapping("view${url.suffix}")
	public String view(HttpServletRequest request, Model model,
			@RequestParam(value = "id", required = false , defaultValue="0") int id){
		if(!haveSiteAuth()){
			return error(model, "请登录查看");
		}
		
		ActionLogUtil.insert(request, "查看 plugin formManage 反馈信息详情");
		Site site = SessionUtil.getSite();
		
		Form form = sqlService.findById(Form.class, id);
		if(form == null){
			return error(model,"查看的信息不存在");
		}
		if(form.getSiteid() - site.getId() != 0){
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
		return "plugin/form/view";
	}
	
}