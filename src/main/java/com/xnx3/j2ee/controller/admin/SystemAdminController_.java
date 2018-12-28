package com.xnx3.j2ee.controller.admin;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.System;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.generateCache.Message;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.SystemService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.controller.BaseController;

/**
 * 系统管理
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/system")
public class SystemAdminController_ extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private SystemService systemService;
	

	/**
	 * 系统变量列表
	 */
	@RequiresPermissions("adminSystemVariable")
	@RequestMapping("variableList${url.suffix}")
	public String variableList(HttpServletRequest request, Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"name","description"});
		int count = sqlService.count("system", sql.getWhere());
		Page page = new Page(count, 1000, request);
		sql.setSelectFromAndPage("SELECT * FROM system", page);
		sql.setDefaultOrderBy("id DESC");
		sql.setOrderByField(new String[]{"id","lasttime","name"});
		List<System> systemList = sqlService.findBySql(sql, System.class);
		
		ActionLogCache.insert(request, "系统变量列表");
		
		model.addAttribute("page", page);
		model.addAttribute("systemList", systemList);
		return "/iw/admin/system/variableList";
	}
	
	/**
	 * 新增/修改全局变量，修改system表的单个变量
	 * @param name 要修改的变量的name。 如果name为空字符串，则是新增； 如果name不存在这条记录，也是新增这条name的记录
	 */
	@RequiresPermissions("adminSystemVariable")
	@RequestMapping("variable${url.suffix}")
	public String variable(
			@RequestParam(value = "name", required = false, defaultValue="") String name,
			Model model, HttpServletRequest request){
		
		System system;
		if(name.length() == 0){
			//新增
			system = new System();
			ActionLogCache.insert(request, "进入新增系统变量页面");
		}else{
			//修改
			system = sqlService.findAloneByProperty(System.class, "name", name);
			if(system == null){
				system = new System();
				system.setName(name);
				ActionLogCache.insert(request, "进入修改系统变量页面,新增变量", system.getName());
			}else{
				ActionLogCache.insert(request, "进入修改系统变量页面,修改变量", system.getName());
			}
		}
		
		//编辑页面
		model.addAttribute("system", system);
		return "/iw/admin/system/variable";
	}
	

	/**
	 * 新增、修改全局变量后的保存。根据 name 来进行增、改
	 */
	@RequiresPermissions("adminSystemVariable")
	@RequestMapping(value="variableSave${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO variableSave(System sys, Model model, HttpServletRequest request){
		System system = sqlService.findAloneByProperty(System.class, "name", sys.getName());
		if(system == null){
			//新增
			system = new System();
			system.setName(sys.getName());
		}else{
			//编辑
		}
		system.setDescription(sys.getDescription());
		system.setLasttime(DateUtil.timeForUnix10());
		system.setValue(sys.getValue());
		sqlService.save(system);
		
		/***更新内存数据****/
		systemService.refreshSystemCache();
		
		ActionLogCache.insert(request, system.getId(), "保存系统变量", system.getName()+"="+system.getValue());
		return success();
	}
	
	/**
	 * 删除系统变量
	 */
	@RequiresPermissions("adminSystemDeleteVariable")
	@RequestMapping(value="deleteVariable${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO deleteVariable(@RequestParam(value = "id", required = false, defaultValue="0") int id, HttpServletRequest request){
		System system = sqlService.findById(System.class, id);
		if(system == null){
			return error("要删除的变量不存在");
		}
		sqlService.delete(system);
		
		/***更新内存数据****/
		systemService.refreshSystemCache();
		
		ActionLogCache.insert(request, system.getId(), "删除系统变量", system.getName()+"="+system.getValue());
		return success();
	}
}
