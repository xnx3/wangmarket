package com.xnx3.wangmarket.domain;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.wangmarket.domain.bean.RequestLog;

public class SessionListener implements HttpSessionListener {
	public void sessionCreated(HttpSessionEvent event) {
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		
//		SImpleSiteVO vo = (SImpleSiteVO) session.getAttribute("SImpleSiteVO");	//当前用户访问的站点信息
		RequestLog requestLog = (RequestLog) session.getAttribute("requestLog");

		try {
			//如果为空，应该是没走domain应用的dns.do，那么为空的不予理会
			if(requestLog != null && G.aliyunLogUtil != null){
				G.aliyunLogUtil.saveByGroup(requestLog.getServerName(), requestLog.getIp(), requestLog.getLogGroup());
			}
		} catch (LogException e) {
			e.printStackTrace();
		}
	}

}
