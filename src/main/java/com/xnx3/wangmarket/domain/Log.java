package com.xnx3.wangmarket.domain;

import javax.servlet.http.HttpServletRequest;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.net.AliyunLogUtil;
import com.xnx3.wangmarket.domain.bean.RequestInfo;
import com.xnx3.wangmarket.domain.vo.SImpleSiteVO;

/**
 * 访问日志
 * @author 管雷鸣
 *
 */
public class Log {
	
	//日志服务，用于统计访问日志。 topic:访问域名
	public static AliyunLogUtil aliyunLogUtil = null;
	
	static{
		//加载日志服务
		String useLog = SystemUtil.get("ALIYUN_SLS_USE");
		if(useLog != null && useLog.equals("1")){
			String log_accessKeyId = SystemUtil.get("ALIYUN_ACCESSKEYID");
			String log_accessKeySecret = SystemUtil.get("ALIYUN_ACCESSKEYSECRET");
			String endpoint = SystemUtil.get("ALIYUN_SLS_ENDPOINT");
			String project = SystemUtil.get("ALIYUN_SLS_PROJECT");
			String logstore = SystemUtil.get("ALIYUN_SLS_FANGWEN_LOGSTORE");
			
			if(log_accessKeyId.length() < 10){
				System.out.println("未开启网站访问日志记录。授权版本有此功能，详情可参考 http://www.wang.market/price.html");
			}else{
				aliyunLogUtil = new AliyunLogUtil(endpoint, log_accessKeyId, log_accessKeySecret, project, logstore);
				aliyunLogUtil.setStackTraceDeep(0);
				aliyunLogUtil.setCacheAutoSubmit(1000, 60);
			}
		}
	}
	
	
//	public static AliyunLogUtil getAliyunLogUtil(){
//		return aliyunLogUtil;
//	}

	
	public static void requestLog(HttpServletRequest request, RequestInfo requestInfo, SImpleSiteVO simpleSiteVO){
		//未开启日志记录
		if(aliyunLogUtil == null){
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
			aliyunLogUtil.cacheLog(logItem);
		} catch (LogException e1) {
			e1.printStackTrace();
		}
	}
}
