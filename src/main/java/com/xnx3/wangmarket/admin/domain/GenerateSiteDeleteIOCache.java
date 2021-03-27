package com.xnx3.wangmarket.admin.domain;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.bean.NewsDataBean;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.pluginManage.interfaces.GenerateSiteInterface;
import com.xnx3.wangmarket.domain.CleanIOCacheMQListener;
import com.xnx3.wangmarket.domain.bean.SimpleSite;
import com.xnx3.wangmarket.domain.mq.DomainMQ;

/**
 * 当生成整站时，通知domain项目刷新，删除 制定 站点的io缓存
 * @author 管雷鸣
 *
 */
public class GenerateSiteDeleteIOCache implements GenerateSiteInterface{

	@Override
	public BaseVO generateSiteBefore(HttpServletRequest request, Site site) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateSiteFinish(HttpServletRequest request, Site site, Map<String, SiteColumn> siteColumnMap,
			Map<String, List<News>> newsMap, Map<Integer, NewsDataBean> newsDataMap) {
		if(AttachmentUtil.isMode(AttachmentUtil.MODE_LOCAL_FILE)) {
			//如果使用的是本地存储，那么不需要走缓存，这层缓存是为云存储准备的，避免被ddos穿透到云存储
			return;
		}
		SimpleSite simpleSite = new SimpleSite(site);
		String content = new com.xnx3.wangmarket.domain.bean.PluginMQ(com.xnx3.wangmarket.admin.util.SessionUtil.getSite()).jsonAppend(net.sf.json.JSONObject.fromObject(com.xnx3.j2ee.util.EntityUtil.entityToMap(simpleSite))).toString();
		com.xnx3.wangmarket.domain.mq.DomainMQ.send(CleanIOCacheMQListener.CLEAN_IO_CACHE_MQ_NAME, content);
	}

}
