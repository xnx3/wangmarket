package com.xnx3.wangmarket.plugin.dataDictionary.controller;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xnx3.j2ee.pluginManage.controller.BasePluginController;
import com.xnx3.j2ee.service.SqlService;

/**
 * 数据字典
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/dataDictionary/")
public class DataDictionaryPluginController extends BasePluginController {
	
	//数据库名字
    @Value("${database.name}")
    private String databaseName;
    
    //数据库驱动
    @Value("${spring.datasource.driver-class-name}")
    private String dataDriver;
    
	@Resource
	private SqlService sqlService;
	
	/**
	 * 列出当前数据库的数据表
	 * @param model
	 * @return
	 */
	@RequestMapping("tableList${url.suffix}")
	public String tableList(Model model){
		
		//因为用于总管理后台，判断当前用户是否有总管理后台的权限
		if(!haveSuperAdminAuth()){
			return error(model, "无权使用！");
		}
		
		if(dataDriver.indexOf("mysql") > 0){
			//mysql才会有，非mysql没有这个数据字典功能
		}else{
			return error(model, "您当前使用的是 sqlite 数据库，无此功能！只有已授权用户的 mysql 数据库才有此功能，以便二次开发。");
		}
		
		String sql = "SELECT * "
				+ "FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '"+databaseName+"'";
		List<Map<String,Object>> list = sqlService.findMapBySqlQuery(sql);
		
		
		model.addAttribute("list", list);
		model.addAttribute("tableNum", list.size());
		return "plugin/dataDictionary/tableList";
	}
	
	
	
	/**
	 * 列出指定数据表的属性
	 * @param tableName 要查看的数据表的名字
	 * @return
	 */
	@RequestMapping("tableView${url.suffix}")
	public String tableView(Model model,
			@RequestParam(value = "tableName", required = true) String tableName){
		
		//因为用于总管理后台，判断当前用户是否有总管理后台的权限
		if(!haveSuperAdminAuth()){
			return error(model, "无权使用！");
		}
		
		String sql = "SELECT COLUMN_NAME, COLUMN_TYPE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT "
				+ "FROM INFORMATION_SCHEMA.COLUMNS "
				+ "WHERE table_schema ='"+databaseName+"' "
				+ "AND table_name  = '"+tableName+"'";
		List<Map<String,Object>> list = sqlService.findMapBySqlQuery(sql);
		model.addAttribute("list", list);
		return "plugin/dataDictionary/tableView";
	}
}