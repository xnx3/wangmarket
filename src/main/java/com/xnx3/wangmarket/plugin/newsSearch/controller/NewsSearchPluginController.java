package com.xnx3.wangmarket.plugin.newsSearch.controller;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.BaseVO;
import com.xnx3.j2ee.pluginManage.controller.BasePluginController;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.plugin.newsSearch.vo.SearchResultVO;

/**
 * 站内文章搜索
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/newsSearch/")
public class NewsSearchPluginController extends BasePluginController {
	@Resource
	private SqlService sqlService;

	/**
	 * 进行搜索，网站 ajax 方式请求,搜索当前网站中，栏目类型为新闻、图文列表下的信息
	 * @param siteid 站点编号，id
	 * @param title 要从标题中搜索的关键词
	 * @param everyPageNumber 每页显示多少条信息。取值范围限制 2 ～ 30 之间
	 */
	@RequestMapping("search${url.suffix}")
	@ResponseBody
	public SearchResultVO search(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "siteid", required = false , defaultValue="0") int siteid,
			@RequestParam(value = "title", required = false , defaultValue="") String title,
			@RequestParam(value = "everyPageNumber", required = false , defaultValue="10") int everyPageNumber){
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		SearchResultVO vo = new SearchResultVO();
		if(siteid == 0){
			vo.setBaseVO(BaseVO.FAILURE, "请传入站点编号{site.id}");
			return vo;
		}
		if(title.length() == 0){
			vo.setBaseVO(BaseVO.FAILURE, "请传入要搜索的关键词");
			return vo;
		}
		if(everyPageNumber > 30 || everyPageNumber < 2){
			vo.setBaseVO(BaseVO.FAILURE, "everyPageNumber 每页显示多少条信息，范围必须在 2～30 之间");
			return vo;
		}
		
		Sql sql = new Sql(request);
		sql.setSearchTable("news");
		sql.appendWhere("siteid = "+siteid+" AND status = "+News.STATUS_NORMAL);
	    sql.appendWhere("( type = "+News.TYPE_NEWS+" OR type = "+News.TYPE_IMAGENEWS+" OR type = "+SiteColumn.TYPE_LIST+")");
		sql.setSearchColumn(new String[]{"title"});
	    int count = sqlService.count("news", sql.getWhere());
	    Page page = new Page(count, everyPageNumber, request);
	    //创建查询语句，只有SELECT、FROM，原生sql查询。其他的where、limit等会自动拼接
	    sql.setSelectFromAndPage("SELECT id,addtime,title,titlepic,intro,cid FROM news", page);
	    //v4.4版本以前，没有自定义内容排序功能，只有按时间倒序排列
    	sql.setDefaultOrderBy("addtime DESC");
	    //因联合查询，结果集是没有实体类与其对应，故而用List<Map>接收
    	List<Map<String, Object>> list = sqlService.findMapBySql(sql);
	    
	    vo.setList(list);
		vo.setPage(page);
		return vo;
	}
	
}