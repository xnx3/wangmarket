package com.xnx3.wangmarket.admin.cache.pc;

import com.xnx3.StringUtil;
import com.xnx3.wangmarket.admin.cache.GenerateHTML;
import com.xnx3.wangmarket.admin.cache.Template;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.vo.IndexVO;

/**
 * 首页的关于我们
 * @author 管雷鸣
 * @deprecated 已在v4.0版本就已废弃PC模式
 */
public class IndexAboutUs {
	//获取替换标签的TAG
	public final static String Tag = "Index_Content_AboutUs";

	/**
	 * 刷新PC端网站首页的关于我们
	 * @param site
	 * @param siteColumn
	 * @param news 关于我们的信息内容
	 * @param text 关于我们的正文内容
	 */
	public static void refreshIndexData(Site site, SiteColumn siteColumn, News news, String text){
		if(siteColumn == null){
			//栏目不存在
			return;
		}
		
		GenerateHTML gh = new GenerateHTML(site);
		//获取当前用户正常访问的首页的源代码
		String index = gh.getGeneratePcIndexHtml();
		
		IndexVO vo = refreshIndexData(site, siteColumn, news, text, index);
		if(vo.getResult() - IndexVO.SUCCESS == 0){
			//只有当有过修改时，才会进行重写首页
			gh.generateIndexHtml(vo.getText());
		}
	}
	
	/**
	 * 刷新PC端网站首页的关于我们
	 * @param site
	 * @param siteColumn
	 * @param news 关于我们的信息内容
	 * @param text 关于我们的正文内容
	 * @param sourceIndexHtml 原首页的html字符串
	 * @return {@link IndexVO}
	 * 			<ul>
	 * 				<li>result:SUCCESS 替换了，首页有更新
	 * 				<li>result:FAILURE 没有替换，首页没有更新，此时无须更新首页的数据
	 * 			</ul>
	 * 			<br/>备注：此两种状态下，text均有值
	 */
	public static IndexVO refreshIndexData(Site site, SiteColumn siteColumn, News news, String text, String sourceIndexHtml){
		IndexVO vo = new IndexVO();
		
		//判断首页是否有跟此栏目相关的列表模版，若是没有的话，直接退出，不修改首页
		if(!Template.isAnnoCenterStringById_Have(sourceIndexHtml, Tag, siteColumn.getId()+"")){
			vo.setText(sourceIndexHtml);
			vo.setBaseVO(IndexVO.FAILURE, "没有更改");
			return vo;
		}
		Template t = new Template(site, true);
		//获得网站所用模版编号的，仅仅首页的模版
		String indexTemplateHtml = t.getIndexTemplateHtml_Only();
		//关于我们模块的内容块
		String aboutUsContent = Template.getAnnoCenterString(indexTemplateHtml, Tag);
		int sizeNumber = Template.getConfigValue(aboutUsContent, "sizeNumber", 100);	//显示条数
//				int tempSiteColumnId = Template.getConfigValue(aboutUsContent, "id", -1);	//该模块调用数据的 SiteColumn.id
				
//				//如果没有绑定栏目id的话，应该就是新增的了
//				if(tempSiteColumnId == 0){
					//若是新增，将当前的栏目ID加进去（将模版的模版快拿过去，当然得更改栏目id）
					aboutUsContent = Template.setConfigValue(aboutUsContent, "id", siteColumn.getId()+"");
//				}else{
//					//不是新增的，那么判断当前更改的信息是否是该模版的
//					if(tempSiteColumnId != siteColumn.getId()){
//						//跟新闻模块调用数据不匹配，不需要刷新
//						return;
//					}
//				}
				
		GenerateHTML gh = new GenerateHTML(site);
		aboutUsContent = gh.replaceSiteColumnTag(aboutUsContent, siteColumn);		//替换SiteColumn相关数据引用
		aboutUsContent = gh.replaceNewsListItem(aboutUsContent, news);					//替换News相关数据引用
		
		//内容截取
		String introContent = StringUtil.filterHtmlTag(text);
		if(introContent.length() > sizeNumber){
			introContent = introContent.substring(0, sizeNumber);
		}
		aboutUsContent = Template.replaceHtmlAnnoTag(aboutUsContent, "AboutUs_Text", introContent);	//替换关于我们的内容
		
		//将新生成的关于我们模块加入到原本已经生成好的网页中
		String newIndex = Template.replaceHtmlAnnoTag(sourceIndexHtml, Tag, aboutUsContent);
		vo.setText(newIndex);
		return vo;
	}
}
