package com.xnx3.wangmarket.plugin.huaWeiYunServiceCreate.controller;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.DateUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.System;
import com.xnx3.j2ee.func.AttachmentFileMode.hander.OBSHandler;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.SystemService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.plugin.base.controller.BasePluginController;

/**
 * OBS的自动创建
 * @author 李鑫
 */
@Controller
@RequestMapping("/plugin/huaWeiYunServiceCreate/obs/")
public class ObsHuaWeiYunServiceCreatePluginController extends BasePluginController {
	
	@Resource
	private SqlService sqlService;
	
	@Resource
	private SystemService systemService;
	
	// 华为云OBS对象存储系统的本地操作类
	private OBSHandler obsHandler; 
	
	/**
	 * 获取当前的进行操作的华为云操作类对象
	 * @author 李鑫
	 * @return {@link com.xnx3.j2ee.func.AttachmentFileMode.hander.OBSHandler} 当前使用的华为云操作类对象
	 */
	private OBSHandler getObsHander() {
		if(obsHandler == null) {
			obsHandler = new OBSHandler(Global.get("HUAWEIYUN_ACCESSKEYID"), Global.get("HUAWEIYUN_ACCESSKEYSECRET"), "https://" + Global.get("HUAWEIYUN_OBS_ENDPOINT"));
		}
		return obsHandler;
	}

	/**
	 * 自动创建OBS以及更新数据库参数
	 * @author 李鑫
	 */
	@RequestMapping("create${url.suffix}")
	@ResponseBody
	public BaseVO createOBS(){
		if(!haveSuperAdminAuth()){
			return error("无权使用");
		}
		
		/*
		 * 初始化判断 system 的数据
		 */
		if(Global.get("HUAWEIYUN_ACCESSKEYID").length() < 5){
			return error("请先设置 Access Key Id");
		}
		
		//从通用区域( HUAWEIYUN_COMMON_ENDPOINT )中，取得所属区域，设置当前obs的区域
		String area = Global.get("HUAWEIYUN_COMMON_ENDPOINT");
		if(area == null || area.length() < 3){
			return error("通用区域未设置值！请先访问功能插件下的华为云配置菜单，选择云服务所在区域。");
		}
		
		//保存了通用区域，那么进行 OBS自动匹配相应区域进行设置
		System obsSys = sqlService.findAloneByProperty(System.class, "name", "HUAWEIYUN_OBS_ENDPOINT");
		if(obsSys == null){
			obsSys = new System();
			obsSys.setDescription("华为云OBS的Endpoint设置。如香港，则此处的值为 obs.ap-southeast-1.myhuaweicloud.com ；上海一，则是 obs.cn-east-3.myhuaweicloud.com");
			obsSys.setName("HUAWEIYUN_OBS_ENDPOINT");
		}
		obsSys.setLasttime(DateUtil.timeForUnix10());
		obsSys.setValue("obs." + area + ".myhuaweicloud.com");
		sqlService.save(obsSys);
		//刷新系统缓存
		systemService.refreshSystemCache();
		
		//检测OBS是否能成功连接上
		try {
			getObsHander().getBuckets().size();
		} catch (Exception e) {
			return error("连接华为云OBS服务失败" + e.getMessage());
		}
		
		// 如果表中没有进行设置的话，创建一个新的
		if(Global.get("HUAWEIYUN_OBS_BUCKETNAME") == null || Global.get("HUAWEIYUN_OBS_BUCKETNAME").equals("auto") || Global.get("HUAWEIYUN_OBS_BUCKETNAME").trim().equals("")) {
			// 如果不存在，则创建一个
			getObsHander().createOBSBucket("wangmarket" + DateUtil.timeForUnix10());
			/*
			 *  修改表数据
			 */
			// 设置当前使用 OBS进行存储，而非本地存储
			sqlService.executeSql("UPDATE system SET value = 'huaWeiYunOBS' WHERE name = 'ATTACHMENT_FILE_MODE'");
			// 更新当前桶的名称
			sqlService.executeSql("UPDATE system SET value = '" +  getObsHander().getObsBucketName() + "' WHERE name = 'HUAWEIYUN_OBS_BUCKETNAME'");
			// 更新当前桶的访问路径
			sqlService.executeSql("UPDATE system SET value = '" + getObsHander().getOriginalUrlForOBS() + "' WHERE name = 'ATTACHMENT_FILE_URL'");
			// 更新当地system表缓存
			systemService.refreshSystemCache();
		}
		//检测这个当前数据库中保存的bucket是否已经存在，是否已经创建了
		boolean have = getObsHander().getObsClient().headBucket(getObsHander().getObsBucketName());
		if(!have){
			// 如果不存在，则创建一个
			getObsHander().createOBSBucket("wangmarket" + DateUtil.timeForUnix10());
			// 更新当地system表缓存
			systemService.refreshSystemCache();
			//创建完毕后再请求看看，是不是真的创建成功了
			if(getObsHander().getObsClient().headBucket(Global.get("HUAWEIYUN_OBS_BUCKETNAME"))){
				/*
				 * 修改表数据
				 */
				// 设置当前使用 OBS进行存储，而非本地存储
				sqlService.executeSql("UPDATE system SET value = 'huaWeiYunOBS' WHERE name = 'ATTACHMENT_FILE_MODE'");
				// 更新当前桶的名称
				sqlService.executeSql("UPDATE system SET value = '" +  getObsHander().getObsBucketName() + "' WHERE name = 'HUAWEIYUN_OBS_BUCKETNAME'");
				// 更新当前桶的访问路径
				sqlService.executeSql("UPDATE system SET value = '" + getObsHander().getOriginalUrlForOBS() + "' WHERE name = 'ATTACHMENT_FILE_URL'");
				// 更新当地system表缓存
				systemService.refreshSystemCache();
				return success("OBS创建成功");
			}else{
				return error("检测到OBS的BucketName不存在，系统进行自动创建时，失败!如有需要，请联系我们。");
			}
		}else{
			// 更新当地system表缓存
			systemService.refreshSystemCache();
			return success("OBS创建成功");
		}
	}
}