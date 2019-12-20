package com.xnx3.wangmarket.admin.cache;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.wangmarket.admin.G;
import com.xnx3.wangmarket.admin.entity.Carousel;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 站点相关信息进行js缓存
 * @author 管雷鸣
 */
public class Site extends BaseCache{
	
	/**
	 * 创建站点的栏目导航数据缓存
	 * @param siteColumnlist
	 */
	public void siteColumn(List<com.xnx3.wangmarket.admin.entity.SiteColumn> siteColumnlist, com.xnx3.wangmarket.admin.entity.Site site) {
		createCacheObject("siteColumn");
		int siteid = 0;
		String content = "";
		for (int i = 0; i < siteColumnlist.size(); i++) {
			SiteColumn column = siteColumnlist.get(i);
			if(siteid == 0){
				siteid = column.getSiteid();
			}

			//根据type，来判断url的值
			if(column.getType() == column.TYPE_NEWS){
				column.setUrl("/newsList.do?cid="+column.getId());
			}else if (column.getType() == SiteColumn.TYPE_IMAGENEWS) {
				column.setUrl("/imageNewsList.do?cid="+column.getId());
			}else if (column.getType() == SiteColumn.TYPE_PAGE) {
				column.setUrl(AttachmentUtil.netUrl()+"site/"+site.getId()+"/html/"+column.getId()+".html");
			}else if (column.getType() == SiteColumn.TYPE_LEAVEWORD) {
				column.setUrl("/leaveword.do?siteid="+siteid);
			}else if (column.getType() == SiteColumn.TYPE_HREF) {
				//5是超链接，忽略过
			}else if (column.getType() == SiteColumn.TYPE_HREF) {
				column.setUrl("#");
			}
			
			if(column.getIcon() == null || column.getIcon().length() == 0){
				column.setIcon(AttachmentUtil.netUrl()+G.DEFAULT_SITE_COLUMN_ICON_URL);
			}
			String icon = column.getIcon().indexOf("://")==-1? AttachmentUtil.netUrl()+"site/"+site.getId()+"/column_icon/"+column.getIcon():column.getIcon();
			content = content+" siteColumn["+i+"] = new Array();"
							+ " siteColumn["+i+"]['id'] = '"+column.getId()+"'; "
							+ " siteColumn["+i+"]['name'] = '"+StringUtil.Utf8ToString(column.getName())+"'; "
							+ " siteColumn["+i+"]['url'] = '"+column.getUrl()+"'; "
							+ " siteColumn["+i+"]['type'] = '"+column.getType()+"'; "
							+ " siteColumn["+i+"]['icon'] = '"+icon+"'; ";
		}
		
		if(siteid > 0){
			appendContent(content);
			generateCacheFile(site);
		}
	}
	
	/**
	 * 创建导航栏目的排序
	 * @param siteid 要排序的导航栏目所在的站点
	 * @param rank 排序，传入如：1,3,2,4,5  每个导航栏目id之间用,分割。这里在前面的数字排序越靠前
	 */
	public void siteColumnRank(com.xnx3.wangmarket.admin.entity.Site site,String rank){
		createCacheObject("siteColumnRank");
		if(rank != null && rank.length()>0){
			String[] ranks = rank.split(",");
			String content = " siteColumnRank = [";
			boolean haveData = false;
			for (int i = 0; i < ranks.length; i++) {
				if(ranks[i] != null && ranks[i].length() > 0){
					if(haveData){
						content+=","+ranks[i]+"";
					}else{
						content+=""+ranks[i]+"";
						haveData = true;
					}
				}
			}
			content+="]; ";
			appendContent(content);
			generateCacheFile(site);
		}
	}
	
	/**
	 * 创建导航栏目的排序,新建一个SiteColumn后，忘排序规则里面追加
	 * @param siteid
	 * @param rank
	 */
	public void siteColumnRankAppend(com.xnx3.wangmarket.admin.entity.Site site,int siteColumnId){
		String rankUrl = AttachmentUtil.netUrl()+"site/"+site.getId()+"/data/siteColumnRank.js";
		HttpResponse res = new HttpUtil().get(rankUrl);
		if(res.getCode() == 404){	//若没有，创建一个新的
			siteColumnRank(site, siteColumnId+"");
		}else{	//若有了，将新增加的放到最后一个
			String rankSource = new HttpUtil().get(rankUrl).getContent();
			rankSource = rankSource.replace("];", ","+siteColumnId+"];");
			try {
				AttachmentUtil.put("site/"+site.getId()+"/data/siteColumnRank.js", new ByteArrayInputStream(rankSource.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * banner轮播图缓存
	 * @param site
	 */
	public void carousel(List<Carousel> carouselList,com.xnx3.wangmarket.admin.entity.Site site){
		createCacheObject("carouselList");
		
		String content = "";
		int siteid = 0;
		for (int i = 0; i < carouselList.size(); i++) {
			Carousel carousel = carouselList.get(i);
			if(siteid == 0){
				siteid = carousel.getSiteid();
			}
			if(carousel.getIsshow() == Carousel.ISSHOW_SHOW){
				String image = carousel.getImage().indexOf("://")==-1? AttachmentUtil.netUrl()+"site/"+site.getId()+"/carousel/"+carousel.getImage():carousel.getImage();
				content = content+" carouselList["+i+"] = new Array();"
						+ " carouselList["+i+"]['id'] = '"+carousel.getId()+"'; "
						+ " carouselList["+i+"]['type'] = '"+carousel.getType()+"'; "
						+ " carouselList["+i+"]['url'] = '"+carousel.getUrl()+"'; "
						+ " carouselList["+i+"]['image'] = '"+image+"'; ";
			}
		}
		
		if(siteid > 0){
			appendContent(content);
			generateCacheFile(site);
		}
	}
}
