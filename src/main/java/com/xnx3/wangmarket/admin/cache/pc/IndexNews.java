package com.xnx3.wangmarket.admin.cache.pc;

import java.util.List;
import com.xnx3.wangmarket.admin.cache.GenerateHTML;
import com.xnx3.wangmarket.admin.cache.Template;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.vo.IndexVO;

/**
 * 首页的新闻
 * @author 管雷鸣
 * @deprecated 已在v4.0版本就已废弃PC模式
 */
public class IndexNews {
	
	/**
	 * 刷新PC端网站首页的新闻相关的内容数据 ,刷新新闻标题等的News的改变，
	 * 局部刷新，获取首页源代码后，再源代码的基础上，改了哪刷新哪
	 * @param site
	 * @param siteColumn
	 * @param newsList
	 */
	public static void refreshIndexData(Site site, SiteColumn siteColumn, List<News> newsList){
		GenerateHTML gh = new GenerateHTML(site);
		gh.setEditMode(false);
		//获取当前用户正常访问的首页的源代码
		String index = gh.getGeneratePcIndexHtml();
		
		IndexVO vo = refreshIndexData(site, siteColumn, newsList, index);
		if(vo.getResult() - IndexVO.SUCCESS == 0){
			//只有当有过修改时，才会进行重写首页
			gh.generateIndexHtml(vo.getText());
		}
	}
	

	/**
	 * 刷新PC端网站首页的新闻相关的内容数据 ,刷新新闻标题等的News的改变
	 * @param site
	 * @param siteColumn
	 * @param newsList
	 * @param sourceIndexHtml 原首页的html字符串
	 * @return {@link IndexVO}
	 * 			<ul>
	 * 				<li>result:SUCCESS 替换了，首页有更新
	 * 				<li>result:FAILURE 没有替换，首页没有更新，此时无须更新首页的数据
	 * 			</ul>
	 * 			<br/>备注：此两种状态下，text均有值
	 */
	public static IndexVO refreshIndexData(Site site, SiteColumn siteColumn, List<News> newsList,String sourceIndexHtml){
		IndexVO vo = new IndexVO();
		
		if(siteColumn == null){
			vo.setBaseVO(IndexVO.FAILURE, "栏目不存在");
			return vo;
		}
		
		//获取替换标签的TAG，是图片列表还是新闻列表
		String Tag = siteColumn.getType()==SiteColumn.TYPE_IMAGENEWS? "Index_Content_NewsImageList":"Index_Content_NewsList";
		
		GenerateHTML gh = new GenerateHTML(site);
		gh.setEditMode(false);
		//获取当前用户正常访问的首页的源代码
//		String index = gh.getGeneratePcIndexHtml();
		//判断首页是否有跟此栏目相关的列表模版，若是没有的话，直接退出，不修改首页
		if(!Template.isAnnoCenterStringById_Have(sourceIndexHtml, Tag, siteColumn.getId()+"")){
			vo.setText(sourceIndexHtml);
			vo.setBaseVO(IndexVO.FAILURE, "没有更改");
			return vo;
		}
		
		Template t = new Template(site, false);
		//获得网站所用模版编号的，仅仅首页的模版
		String indexTemplateHtml = t.getIndexTemplateHtml_Only();
		//新闻列表模块的内容块
		String newsListContent = Template.getAnnoCenterString(indexTemplateHtml, Tag);
		int number = Template.getConfigValue(newsListContent, "number", 6);	//显示条数
//		int tempSiteColumnId = Template.getConfigValue(newsListContent, "id", -1);	//该模块调用数据的 SiteColumn.id
		String itemTemp = Template.getAnnoCenterString(newsListContent, "List");	//list。item，循环遍历的html代码
		
//		//如果没有绑定栏目id的话，应该就是新增的了
//		if(tempSiteColumnId == 0){
//			若是新增，将当前的栏目ID加进去
			newsListContent = Template.setConfigValue(newsListContent, "id", siteColumn.getId()+"");
//		}else{
//			//不是新增的，那么判断当前更改的信息是否是该模版的
//			if(tempSiteColumnId != siteColumn.getId()){
//				//跟新闻模块调用数据不匹配，不需要刷新
//				return;
//			}
//		}
		
		String itemS = "";	//显示的新闻内容
		for (int i = 0; i < newsList.size() && i < number; i++) {
			itemS += gh.replaceNewsListItem(itemTemp, newsList.get(i));
		}
		
		newsListContent = gh.replaceSiteColumnTag(newsListContent, siteColumn);		//替换SiteColumn相关数据引用
		newsListContent = Template.replaceHtmlAnnoTag(newsListContent, "List", itemS);	//替换新闻内容列表
		
		//将新生成的首页新闻模块加入到原本已经生成好的网页中
		String newIndex = Template.replaceHtmlAnnoTag(sourceIndexHtml, Tag, newsListContent);
		
		vo.setText(newIndex);
		return vo;
	}
}
