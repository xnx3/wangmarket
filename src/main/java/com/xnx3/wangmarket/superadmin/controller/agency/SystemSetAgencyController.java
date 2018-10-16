package com.xnx3.wangmarket.superadmin.controller.agency;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.superadmin.entity.Agency;
import com.xnx3.wangmarket.superadmin.entity.AgencyData;

/**
 * 代理商
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/agency")
public class SystemSetAgencyController extends BaseController {
	@Resource
	private SqlService sqlService;

	/**
	 * 系统设置，设置当前代理的一些属性等
	 */
	@RequiresPermissions("agencyIndex")
	@RequestMapping("systemSet${url.suffix}")
	public String systemSet(HttpServletRequest request, Model model){
		Agency agency = getMyAgency();
		
		AgencyData agencyData = getMyAgencyData();
		if(agencyData == null){
			//兼容v4.4以前的版本， agency_data 数据表是 v4.4 才增加的，如果没有 agency_data 信息，则默认填充一个
			agencyData = new AgencyData();
			agencyData.setId(agency.getId());
			agencyData.setNotice("");
		}
		
		model.addAttribute("user", getUser());
		model.addAttribute("agency", agency);
		model.addAttribute("agencyData", agencyData);
		return "agency/systemSet";
	}
	

	/**
	 * 保存 agency 相关信息
	 * @param name agency的列名，如 name 、 phone等
	 * @param value 修改后的值
	 */
	@RequiresPermissions("agencyIndex")
	@RequestMapping("saveAgency${url.suffix}")
	@ResponseBody
	public BaseVO saveAgency(HttpServletRequest request,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "value", required = true) String value){
		Agency agency = getMyAgency();
		if(agency == null){
			return error("您不是代理，无权操作");
		}
		value = filter(value);
		
		agency = sqlService.findById(Agency.class, agency.getId());
		if(name.equals("name")){
			agency.setName(value);
			AliyunLog.addActionLog(agency.getId(), "更改自己代理信息的公司名字", agency.getName());	//记录操作日志
		}else if (name.equals("phone")) {
			agency.setPhone(value);
			AliyunLog.addActionLog(agency.getId(), "更改自己代理信息的电话", agency.getPhone());	//记录操作日志
		}else if (name.equals("address")) {
			agency.setAddress(value);
			AliyunLog.addActionLog(agency.getId(), "更改自己代理信息的地址", agency.getAddress());	//记录操作日志
		}else if (name.equals("qq")) {
			agency.setQq(value);
			AliyunLog.addActionLog(agency.getId(), "更改自己代理信息的QQ", agency.getName());	//记录操作日志
		}else{
			return error("name无效");
		}
		sqlService.save(agency);
		
		//更新session缓存
		com.xnx3.wangmarket.admin.Func.getUserBeanForShiroSession().setMyAgency(agency);
		
		return success();
	}
	

	/**
	 * 保存公告
	 * @param value 要更改的公告的信息，
	 */
	@RequiresPermissions("agencyIndex")
	@RequestMapping("saveNotice${url.suffix}")
	@ResponseBody
	public BaseVO saveNotice(HttpServletRequest request,
			@RequestParam(value = "value", required = true) String value){
		Agency agency = getMyAgency();
		if(agency == null){
			return error("您不是代理，无权操作");
		}
		value = StringUtil.filterXss(value);
		
		AgencyData agencyData = sqlService.findAloneBySqlQuery("SELECT * FROM agency_data WHERE id = "+getMyAgency().getId(), AgencyData.class);
		if(agencyData == null){
			//兼容4.4版本以前的。这个功能是4.4版本才增加的
			agencyData = new AgencyData();
			agencyData.setId(agency.getId());
		}
		agencyData.setNotice(value);
		sqlService.save(agencyData);
		
		//更新session缓存
		com.xnx3.wangmarket.admin.Func.getUserBeanForShiroSession().setMyAgencyData(agencyData);
				
		//记录操作日志
		AliyunLog.addActionLog(agencyData.getId(), "代理更改公告");
		
		return success();
	}
}
