package com.xnx3.wangmarket.admin.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.version.VersionUtil;
import com.xnx3.version.VersionVO;

/**
 * 版本相关
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class VersionController_ extends BaseController {
	/**
	 * 获取当前系统版本，此需要开发者自行修改，根据当前版本的记录方式返回版本号，如返回 3.7
	 * @return
	 */
	private String getCurrentVersion(){
		return Global.VERSION;
	}
	
	/**
	 * 检查当前系统是否有最新版本
	 * @return 若有最新版本，VersionVO.getResult == VersionVO.SUCCESS
	 */
	@RequestMapping("getNewVersion${url.suffix}")
	@ResponseBody
	public VersionVO getNewVersion(HttpServletRequest request){
		ActionLogUtil.insert(request, "检查当前系统是否有最新版本", "当前版本v"+getCurrentVersion());
		return VersionUtil.cloudContrast("http://version.xnx3.com/wangmarket.html", getCurrentVersion());
	}
	
}
