package com.xnx3.wangmarket.domain;

import java.util.Map;
import org.springframework.stereotype.Component;

import com.xnx3.cache.JavaUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.wangmarket.domain.G;
import com.xnx3.wangmarket.domain.bean.MQBean;
import com.xnx3.wangmarket.domain.bean.SimpleSite;
import com.xnx3.wangmarket.domain.mq.DomainMQ;
import com.xnx3.wangmarket.domain.mq.ReceiveDomainMQ;
import net.sf.json.JSONObject;

/**
 * MQ消息监听，用于清空缓存的html页面等io的缓存用
 * @author 管雷鸣
 *
 */
@Component
public class CleanIOCacheMQListener {
	//清空缓存的数据再mq中传递时的名字
	public static final String CLEAN_IO_CACHE_MQ_NAME = "domain_request_io_cache";
	
	public CleanIOCacheMQListener() {
		ConsoleUtil.info("domain load CleanIOCacheMQListener");
		DomainMQ.receive(CLEAN_IO_CACHE_MQ_NAME, new ReceiveDomainMQ() {
			public void receive(String content) {
				JSONObject json = JSONObject.fromObject(content);
				int siteid = json.getInt("siteid");
				JavaUtil.delete("site:"+siteid);
				ConsoleUtil.debug("删除网站 "+siteid+" 的io缓存");
			}
		});
		
	}
	
}

