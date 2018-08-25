package com.xnx3.wangmarket.plugin.bbs.controller.admin;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.controller.BaseController;
import com.xnx3.wangmarket.plugin.bbs.entity.PostClass;
import com.xnx3.wangmarket.plugin.bbs.service.PostService;

/**
 * 论坛，帖子
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/bbs/admin/postClass/")
public class PostClassBbsPluginAdminController extends BaseController {
	@Resource
	private PostService postService;
	@Resource
	private UserService userService;
	@Resource
	private SqlService sqlService;

	/**
	 * 板块列表
	 */
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"id=","name"});
		sql.appendWhere("siteid="+getSiteId());
		sql.appendWhere("isdelete = "+PostClass.ISDELETE_NORMAL);
		int count = sqlService.count("plugin_bbs_post_class", sql.getWhere());
		Page page = new Page(count, Global.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT * FROM plugin_bbs_post_class", page);
		sql.setDefaultOrderBy("id DESC");
		List<PostClass> list = sqlService.findBySql(sql, PostClass.class);
		
		ActionLogCache.insert(request, "论坛插件查看板块列表", "第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "/plugin/bbs/admin/postClass/list";
	}
	
	/**
	 * 添加／修改板块提交页面
	 * @param postClass {@link PostClass}
	 */
	@RequestMapping(value="saveClass${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO saveClass(HttpServletRequest request, Model model){
		BaseVO baseVO = postService.savePostClass(request, getSiteId());
		if(baseVO.getResult() == BaseVO.SUCCESS){
			ActionLogCache.insert(request, "论坛板块保存成功");
		}else{
			ActionLogCache.insert(request, "论坛板块保存失败", baseVO.getInfo());
		}
		
		return baseVO;
	}

	/**
	 * 编辑板块
	 * @param id 板块id，PostClass.id
	 */
	@RequestMapping("postClass${url.suffix}")
	public String postClass(@RequestParam(value = "id", required = false, defaultValue="0") int id,Model model, HttpServletRequest request){
		if(id>0){
			//修改
			PostClass postClass = sqlService.findById(PostClass.class, id);
			if(postClass!=null){
				model.addAttribute("postClass", postClass);
				ActionLogCache.insert(request, id, "编辑论坛板块页面");
			}else{
				ActionLogCache.insert(request, id, "编辑论坛板块页面", "出错：板块不存在");
				return "板块不存在";
			}
		}else{
			//新增
			ActionLogCache.insert(request, "添加论坛板块页面");
		}
		
		return "plugin/bbs/admin/postClass/postClass";
	}
	

	/**
	 * 删除板块
	 * @param id 板块id，PostClass.id
	 */
	@RequestMapping(value="deleteClass${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO deleteClass(@RequestParam(value = "id", required = true) int id, Model model, HttpServletRequest request){
		BaseVO baseVO = postService.deletePostClass(id);
		if(baseVO.getResult() == BaseVO.SUCCESS){
			ActionLogCache.insert(request, "删除论坛板块");
		}else{
			ActionLogCache.insert(request, "删除论坛板块出错", baseVO.getInfo());
		}
		
		return baseVO;
	}
	


	/**
	 * 生成postClass缓存
	 */
	private void generatePostClassCache(Model model, HttpServletRequest request){
		//new Bbs().postClass(sqlService.findAll(PostClass.class));
	}
	
	
}
