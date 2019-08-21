package com.xnx3.wangmarket.plugin.huaWeiYunServiceCreate.controller;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.DateUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.System;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.SystemService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.plugin.base.controller.BasePluginController;

/**
 * 华为云环境设置基本变量信息
 * @author 李鑫
 */
@Controller
@RequestMapping("/plugin/huaWeiYunServiceCreate/set/")
public class SetHuaWeiYunServiceCreatePluginController extends BasePluginController {
	
	@Resource
	private SqlService sqlService;
	@Resource
	private SystemService systemService;
	
	/**
	 * 初始化-设置accesskey
	 */
	@RequestMapping("setAccessKey${url.suffix}")
	public String setAccessKey(Model model){
		if(!haveSuperAdminAuth()){
			return error(model, "无权使用");
		}
		/*
		 * 初始化数据库需要字段信息
		 */
		//检查HUAWEIYUN_ACCESSKEYID字段
		initializationSystem("HUAWEIYUN_ACCESSKEYID", "华为云平台的accessKeyId", null);
		// 检查HUAWEIYUN_ACCESSKEYSECRET字段
		initializationSystem("HUAWEIYUN_ACCESSKEYSECRET", "华为云平台的accessKeySecret", null);
		// 检查桶名称字段是否存在，默认值为 "auto"
		initializationSystem("HUAWEIYUN_OBS_BUCKETNAME", "华为云对象存储桶的名字。若值为auto，则会自动创建。建议值不必修改，默认即可。它可自动给你赋值。", "auto");
		
		//判断是否设置了accesskey等，如果没有设置，需要先进性设置
		if((Global.get("HUAWEIYUN_ACCESSKEYID") == null || Global.get("HUAWEIYUN_ACCESSKEYID").length() < 5) || Global.get("HUAWEIYUN_ACCESSKEYSECRET") == null || Global.get("HUAWEIYUN_ACCESSKEYSECRET").length() < 5){
			return "plugin/huaWeiYunServiceCreate/set/setAccessKey"; 
		}else{
			//如果已经设置了，那么就重定向，跳转到下一个设置项
			return redirect("plugin/huaWeiYunServiceCreate/set/setArea.do");
		}
	}
	
	/**
	 * 初始化-选择区域,比如香港、上海、北京、深圳
	 */
	@RequestMapping("setArea${url.suffix}")
	public String setArea(Model model){
		if(!haveSuperAdminAuth()){
			return error(model, "无权使用");
		}
		
		//判断是否设置了 endpoint ，如果没有设置，需要先进性设置
		if(Global.get("HUAWEIYUN_COMMON_ENDPOINT") == null || Global.get("HUAWEIYUN_COMMON_ENDPOINT").length() < 5){
			return "plugin/huaWeiYunServiceCreate/set/setArea";
		}else{
			//如果已经设置了，那么就重定向，跳转到下一个设置项
			return redirect("plugin/huaWeiYunServiceCreate/index.do");
		}
	}
	
	/**
	 * 设置区域后的保存
	 */
	@RequestMapping("setAreaSave${url.suffix}")
	@ResponseBody
	public BaseVO setAreaSave(Model model,
			@RequestParam(value = "area", required = false, defaultValue="") String area){
		if(!haveSuperAdminAuth()){
			return error("无权使用");
		}
		area = area.trim();
		// 保存及检查地域信息
		if(area.length() == 0){
			return error("请选择区域");
		}else{
			System areaSys = sqlService.findAloneByProperty(System.class, "name", "HUAWEIYUN_COMMON_ENDPOINT");
			if(areaSys == null){
				areaSys = new System();
				areaSys.setDescription("华为云OBS的Endpoint设置。如香港，则此处的值为 ap-southeast-1；上海一，则是 cn-east-3");
				areaSys.setName("HUAWEIYUN_COMMON_ENDPOINT");
			}
			areaSys.setLasttime(DateUtil.timeForUnix10());
			areaSys.setValue(area);
			sqlService.save(areaSys);
			//刷新缓存
			systemService.refreshSystemCache();
		}
		return success();
	}
	
	/**
	 * 初始化数据库中与华为云相关的信息,如果没有该字段将会进行创建
	 * @author 李鑫
	 * @param name 需要初始化的信息的名称 例 “HUAWEIYUN_ACCESSKEYSECRET”
	 * @param desc 该信息的描述信息
	 * @param defaultValue 字段的默认值，如果没有默认值请传入 对象 null
	 */
	private void initializationSystem(String name, String desc, String defaultValue) {
		// 检查HUAWEIYUN_ACCESSKEYSECRET字段
		System system = sqlService.findAloneByProperty(System.class, "name", name);
		if(system == null) {
			system = new System();
			// 设置字段描述
			system.setDescription(desc);
			// 设置字段名称
			system.setName(name);
			// 如果有默认值，设为默认值
			if(defaultValue != null) {
				system.setValue(defaultValue);
			}
			// 设置字段的最后修改的时间
			system.setLasttime(DateUtil.timeForUnix10());
			sqlService.save(system);
		}
	}
}