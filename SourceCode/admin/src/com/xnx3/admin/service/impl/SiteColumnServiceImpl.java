package com.xnx3.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.admin.Func;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.entity.SiteColumn;
import com.xnx3.admin.service.SiteColumnService;
import com.xnx3.admin.vo.SiteColumnTreeVO;

@Service("columnService")
public class SiteColumnServiceImpl implements SiteColumnService {
	@Resource
	private SqlDAO sqlDAO;

	public List<SiteColumn> findBySiteid(int siteid) {
		// TODO Auto-generated method stub
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
		new com.xnx3.admin.cache.Site().siteColumnRank(site, rankString);
		new com.xnx3.admin.cache.Site().siteColumn(list,site);
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
		
		return siteColumnTreeVOList;
	}

	public void updateSiteColumnByCache(SiteColumn siteColumn) {
		Map<Integer, SiteColumn> siteColumnMap = getSiteColumnMapByCache();
		siteColumnMap.put(siteColumn.getId(), siteColumn);
		Func.getUserBeanForShiroSession().setSiteColumnMap(siteColumnMap);
	}
	
	public Map<Integer, SiteColumn> getSiteColumnMapByCache(){
		Map<Integer, SiteColumn> siteColumnMap = Func.getUserBeanForShiroSession().getSiteColumnMap();
		//若缓存中没有，那么重新加入
		if(siteColumnMap == null){
			siteColumnMap = new HashMap<Integer, SiteColumn>();
			
			Site site = Func.getUserBeanForShiroSession().getSite();
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
		Func.getUserBeanForShiroSession().setSiteColumnMap(siteColumnMap);
	}

	public List<SiteColumn> getSiteColumnListByCache() {
		Map<Integer, SiteColumn> map = getSiteColumnMapByCache();
		List<SiteColumn> list = new ArrayList<SiteColumn>();
		
		for (Map.Entry<Integer, SiteColumn> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		return list;
	}

}
