package com.xnx3.wangmarket.domain;

import javax.servlet.http.HttpServletRequest;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.DateUtil;
import com.xnx3.net.AliyunLogUtil;
import com.xnx3.wangmarket.domain.bean.RequestInfo;
import com.xnx3.wangmarket.domain.vo.SImpleSiteVO;

/**
 * 访问日志
 * @author 管雷鸣
 *
 */
public class Log {
	static{
		if(G.aliyunLogUtil != null){
			G.aliyunLogUtil.setStackTraceDeep(0);
			//设置提交日志触发点为 1000条，或者 60秒
			G.aliyunLogUtil.setCacheAutoSubmit(1000, 60);
		}
	}
	
	
	public static AliyunLogUtil getAliyunLogUtil(){
		return G.aliyunLogUtil;
	}

	
	public static void requestLog(HttpServletRequest request, RequestInfo requestInfo, SImpleSiteVO simpleSiteVO){
		//未开启日志记录
		if(G.aliyunLogUtil == null){
			return;
		}
		if(simpleSiteVO.getResult() - SImpleSiteVO.FAILURE == 0){
			//失败的，没有访问正规站点的，可能是ip直接访问的，或者访问的未绑定上的域名过来的。这样的不与记录。
			return;
		}
		
		LogItem logItem = new LogItem(DateUtil.timeForUnix10());
		logItem.PushBack("ip", requestInfo.getIp());
		logItem.PushBack("referer", requestInfo.getReferer());
		logItem.PushBack("userAgent", requestInfo.getUserAgent());
		logItem.PushBack("htmlFile", requestInfo.getHtmlFile());
		logItem.PushBack("siteid", simpleSiteVO.getSimpleSite().getId()+"");
		logItem.PushBack("serverName", requestInfo.getServerName());
		
		try {
			G.aliyunLogUtil.cacheLog(logItem);
		} catch (LogException e1) {
			e1.printStackTrace();
		}
	}
}
