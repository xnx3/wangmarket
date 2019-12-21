package com.xnx3.wangmarket.admin.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.DateUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.SafetyUtil;
import com.xnx3.wangmarket.admin.Func;
import com.xnx3.wangmarket.admin.cache.GenerateHTML;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.NewsData;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.service.SiteColumnService;
import com.xnx3.wangmarket.admin.util.SessionUtil;
import com.xnx3.wangmarket.admin.vo.SiteColumnTreeVO;

@Service("columnService")
public class SiteColumnServiceImpl implements SiteColumnService {
	@Resource
	private SqlDAO sqlDAO;

	public List<SiteColumn> findBySiteid(int siteid) {
		return sqlDAO.findBySqlQuery("SELECT * FROM site_column WHRER siteid = "+siteid +" ORDER BY rank ASC", SiteColumn.class);
	}

	public void resetColumnRankAndJs(Site site) {
		List<SiteColumn> list = sqlDAO.findBySqlQuery("SELECT * FROM site_column WHERE siteid = "+site.getId() +" AND used = "+SiteColumn.USED_ENABLE+" ORDER BY rank ASC", SiteColumn.class);
		String rankString = "";
		for (int i = 0; i < list.size(); i++) {
			String id = list.get(i).getId()+"";
			if(rankString.length() == 0){
				rankString = id;
			}else{
				rankString = rankString + "," + id;
			}
		}
		new com.xnx3.wangmarket.admin.cache.Site().siteColumnRank(site, rankString);
		new com.xnx3.wangmarket.admin.cache.Site().siteColumn(list,site);
	}

	/**
	 * 从当前Shiro Session缓存中，拿当前网站的栏目。如果当前没有缓存，那么读数据库缓存栏目数据
	 */
	public List<SiteColumnTreeVO> getSiteColumnTreeVOByCache() {
		Map<Integer, SiteColumn> siteColumnMap = getSiteColumnMapByCache();
		
		List<SiteColumnTreeVO> siteColumnTreeVOList = new ArrayList<SiteColumnTreeVO>(); 

		//首先，先列出一级栏目
		for (Map.Entry<Integer, SiteColumn> entry : siteColumnMap.entrySet()) {
			SiteColumn sc = entry.getValue();
			if((sc.getParentid() == null || sc.getParentid() == 0) && (sc.getParentCodeName() == null || sc.getParentCodeName().length() == 0)){
				SiteColumnTreeVO vo = new SiteColumnTreeVO();
				vo.setLevel(1);
				vo.setList(new ArrayList<SiteColumnTreeVO>());
				vo.setSiteColumn(sc);
				siteColumnTreeVOList.add(vo);
			}
		}
		//根据rank排序
		Collections.sort(siteColumnTreeVOList,new Comparator<SiteColumnTreeVO>(){
            public int compare(SiteColumnTreeVO arg0, SiteColumnTreeVO arg1) {
//            	if(arg0.getSiteColumn().getRank() == null){
//            		arg0.getSiteColumn().setRank(0);
//            	}
//            	if(arg1.getSiteColumn().getRank() == null){
//            		arg1.getSiteColumn().setRank(0);
//            	}
                return arg0.getSiteColumn().getRank().compareTo(arg1.getSiteColumn().getRank());
            }
        });
		
		//然后，列出二级栏目
		for (Map.Entry<Integer, SiteColumn> entry : siteColumnMap.entrySet()) {
			SiteColumn sc = entry.getValue();
			//如果不是一级栏目，那么全部认为都是二级栏目，至于三级或者四级栏目，以后再说
			if((sc.getParentid() != null && sc.getParentid() > 0) || (sc.getParentCodeName() != null && sc.getParentCodeName().length() > 0)){
				SiteColumnTreeVO vo = new SiteColumnTreeVO();
				vo.setLevel(1);
				vo.setList(new ArrayList<SiteColumnTreeVO>());
				vo.setSiteColumn(sc);
				
				//将之前得到的一级栏目逐个遍历出来，向是这个二级栏目父类的一级栏目下，添加二级栏目
				for (int j = 0; j < siteColumnTreeVOList.size(); j++) {
					if(siteColumnTreeVOList.get(j).getSiteColumn().getCodeName().equals(sc.getParentCodeName())){
						siteColumnTreeVOList.get(j).getList().add(vo);
						break;
					}
				}
			}
		}
		//将所有二级栏目，根据rank排序
		for (int j = 0; j < siteColumnTreeVOList.size(); j++) {
			SiteColumnTreeVO vo = siteColumnTreeVOList.get(j);
			if(vo.getList() != null && vo.getList().size() > 0){
				List<SiteColumnTreeVO> list = vo.getList();
				//将某个父栏目内的子栏目进行排序
				Collections.sort(list,new Comparator<SiteColumnTreeVO>(){
		            public int compare(SiteColumnTreeVO arg0, SiteColumnTreeVO arg1) {
		                return arg0.getSiteColumn().getRank().compareTo(arg1.getSiteColumn().getRank());
		            }
		        });
			}
		}
		
		
		return siteColumnTreeVOList;
	}

	public void updateSiteColumnByCache(SiteColumn siteColumn) {
		Map<Integer, SiteColumn> siteColumnMap = getSiteColumnMapByCache();
		siteColumnMap.put(siteColumn.getId(), siteColumn);
		SessionUtil.setSiteColumnMap(siteColumnMap);
	}
	
	public SiteColumn getSiteColumnByCache(int siteColumnId){
		return getSiteColumnMapByCache().get(siteColumnId);
	}
	
	public Map<Integer, SiteColumn> getSiteColumnMapByCache(){
		Map<Integer, SiteColumn> siteColumnMap = SessionUtil.getSiteColumnMap();
		//若缓存中没有，那么重新加入
		if(siteColumnMap == null){
			siteColumnMap = new HashMap<Integer, SiteColumn>();
			
			Site site = SessionUtil.getSite();
			List<SiteColumn> siteColumnList = sqlDAO.findBySqlQuery("SELECT * FROM site_column WHERE siteid = "+site.getId()+" ORDER BY rank ASC", SiteColumn.class);
			for (int i = 0; i < siteColumnList.size(); i++) {
				SiteColumn sc = siteColumnList.get(i);
				siteColumnMap.put(sc.getId(), sc);
			}
		}
		
		return siteColumnMap;
	}

	public void deleteSiteColumnByCache(int siteColumnId) {
		Map<Integer, SiteColumn> siteColumnMap = getSiteColumnMapByCache();
		siteColumnMap.remove(siteColumnId);
		SessionUtil.setSiteColumnMap(siteColumnMap);
	}

	public List<SiteColumn> getSiteColumnListByCache() {
		Map<Integer, SiteColumn> map = getSiteColumnMapByCache();
		List<SiteColumn> list = new ArrayList<SiteColumn>();
		
		for (Map.Entry<Integer, SiteColumn> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		return list;
	}
	
	public void createNonePage(SiteColumn siteColumn,Site site,boolean updateName) {
		News news = sqlDAO.findAloneBySqlQuery("SELECT * FROM news WHERE cid = "+siteColumn.getId(), News.class);
		if(news == null){
			//需要创建一个新页面
			news = new News();
			news.setAddtime(DateUtil.timeForUnix10());
			news.setCid(siteColumn.getId());
			news.setCommentnum(0);
			news.setIntro(SafetyUtil.filter(siteColumn.getName()));
			news.setOpposenum(0);
			news.setReadnum(0);
			news.setSiteid(siteColumn.getSiteid());
			news.setStatus(News.STATUS_NORMAL);
			news.setTitle(news.getIntro());
			news.setTitlepic(StringUtil.filterXss(siteColumn.getIcon()));
			news.setType(News.TYPE_PAGE);
			news.setUserid(ShiroFunc.getUser().getId());
			sqlDAO.save(news);
			if(news.getId() > 0){
				NewsData newsData = new NewsData();
				newsData.setId(news.getId());
				newsData.setText(news.getTitle());
				sqlDAO.save(newsData);
				
				GenerateHTML gh = new GenerateHTML(site);
				gh.generateViewHtml(site, news, siteColumn, news.getTitle());
			}
		}else{
			if(updateName){
				news.setTitle(SafetyUtil.filter(siteColumn.getName()));
				sqlDAO.save(news);
				
				NewsData newsData = sqlDAO.findById(NewsData.class, news.getId());
				GenerateHTML gh = new GenerateHTML(site);
				gh.generateViewHtml(site, news, siteColumn, newsData.getText());
			}
		}
	}
	
	/**
	 * 刷新 Session 中存储的栏目缓存。清空掉原本的缓存，重新从数据库中读最新的栏目数据并缓存入Session
	 */
	public void refreshCache(){
		//清空掉原本的
		SessionUtil.setSiteColumnMap(null);
		
		//重新获取一次
		getSiteColumnMapByCache();
	}
	
}
