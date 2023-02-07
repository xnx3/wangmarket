package com.xnx3.wangmarket.domain;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.wangmarket.domain.bean.RequestInfo;
import com.xnx3.wangmarket.domain.vo.SImpleSiteVO;

/**
 * 网站访问日志
 * @author 管雷鸣
 */
public class LogUtil{
	public static cn.zvo.log.framework.springboot.Log log;

    /**
     * 获取log对象
     */
    public static cn.zvo.log.framework.springboot.Log getLog() {
    	if(log == null) {
    		log = cn.zvo.log.framework.springboot.LogUtil.getLog().clone();
    		log.setTable("fangwen");
    		ConsoleUtil.debug("访问日志记录到log table : fangwen , log interface : "+log.getLogInterface().getClass().getName());
    	}
    	return log;
    }

	public static void requestLog(HttpServletRequest request, RequestInfo requestInfo, SImpleSiteVO simpleSiteVO){
		if(simpleSiteVO.getResult() - SImpleSiteVO.FAILURE == 0){
			//失败的，没有访问正规站点的，可能是ip直接访问的，或者访问的未绑定上的域名过来的。这样的不与记录。
			return;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("time", DateUtil.timeForUnix10());
//		LogItem logItem = new LogItem(DateUtil.timeForUnix10());
		map.put("ip", requestInfo.getIp());
		map.put("referer", requestInfo.getReferer());
		map.put("userAgent", requestInfo.getUserAgent());
		map.put("htmlFile", requestInfo.getHtmlFile());
		map.put("serverName", requestInfo.getServerName());
		if(simpleSiteVO != null && simpleSiteVO.getSimpleSite() != null) {
			map.put("siteid", simpleSiteVO.getSimpleSite().getId()+"");
		}
		getLog().add(map);
	}
}
