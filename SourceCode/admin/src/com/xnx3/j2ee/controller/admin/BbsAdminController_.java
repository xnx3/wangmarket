package com.xnx3.j2ee.controller.admin;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.BaseEntity;
import com.xnx3.j2ee.entity.Post;
import com.xnx3.j2ee.entity.PostClass;
import com.xnx3.j2ee.entity.PostComment;
import com.xnx3.j2ee.entity.PostData;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.PostService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 论坛，帖子
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/admin/bbs")
public class BbsAdminController_ extends BaseController {
	@Resource
	private PostService postService;
	@Resource
	private UserService userService;
	@Resource
	private SqlService sqlService;

	/**
	 * 帖子列表
	 */
	@RequiresPermissions("adminBbsPostList")
	@RequestMapping("postList")
	public String postList(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"classid=","title","view","info","addtime(date:yyyy-MM-dd hh:mm:ss)>"});
		sql.appendWhere("isdelete = "+BaseEntity.ISDELETE_NORMAL);
		int count = sqlService.count("post", sql.getWhere());
		Page page = new Page(count, Global.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT * FROM post", page);
		sql.setDefaultOrderBy("post.id DESC");
		sql.setOrderByField(new String[]{"id","view"});
		List<Post> list = sqlService.findBySql(sql, Post.class);
		
		ActionLogCache.insert(request, "管理后台帖子列表", "第"+ page.getCurrentPageNumber() +"页");
		
		model.addAttribute("page", page);
		model.addAttribute("list", list);
		return "/iw/admin/bbs/postList";
	}
	
	/**
	 * 新增、修改帖子
	 * @param id 帖子id，Post.id
	 */
	@RequiresPermissions("adminBbsPost")
	@RequestMapping("post")
	public String post(@RequestParam(value = "id", defaultValue = "0", required = false) int id,Model model, HttpServletRequest request){
		if(id > 0){
			Post post = sqlService.findById(Post.class, id);
			if(post != null){
				PostData postData = sqlService.findById(PostData.class, id);
				ActionLogCache.insert(request, "管理后台新增或修改帖子", post.getTitle());
				
				model.addAttribute("post", post);
				model.addAttribute("postData", postData);
			}else{
				ActionLogCache.insert(request, "管理后台新增或修改帖子页面", "失败：帖子不存在");
				return error(model, "帖子不存在！");
			}
		}
		return "iw/admin/bbs/post";
	}
	
	/**
	 * 添加、编辑时保存帖子
	 * @param id 帖子id，Post.id
	 * @param classid 分类id
	 * @param title 帖子标题
	 * @param text 帖子内容
	 */
	@RequiresPermissions("adminBbsPost")
	@RequestMapping("savePost")
	public String savePost(HttpServletRequest request,Model model){
		BaseVO baseVO = postService.savePost(request);
		if(baseVO.getResult() == BaseVO.SUCCESS){
			ActionLogCache.insert(request, "管理后台新增或修改帖子保存");
			return success(model, "操作成功！", "admin/bbs/postList.do?classid="+request.getParameter("classid"));
		}else{
			ActionLogCache.insert(request, "管理后台新增或修改帖子保存", "失败："+baseVO.getInfo());
			return error(model, baseVO.getInfo());
		}
	}

	/**
	 * 板块列表
	 */
	@RequiresPermissions("adminBbsClassList")
	@RequestMapping("classList")
	public String classList(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"id=","name"});
		sql.appendWhere("isdelete = "+PostClass.ISDELETE_NORMAL);
		int count = sqlService.count("post_class", sql.getWhere());
		Page page = new Page(count, Global.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT * FROM post_class", page);
		sql.setDefaultOrderBy("post_class.id DESC");
		List<PostClass> list = sqlService.findBySql(sql, PostClass.class);
		
		ActionLogCache.insert(request, "管理后台板块列表", "第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "/iw/admin/bbs/classList";
	}
	
	
	/**
	 * 删除帖子
	 * @param id 帖子id，Post.id
	 */
	@RequiresPermissions("adminBbsDeletePost")
	@RequestMapping("deletePost")
	@ResponseBody
	public BaseVO deletePost(@RequestParam(value = "id", required = true) int id, Model model, HttpServletRequest request){
		BaseVO baseVO = postService.deletePost(id);
		if(baseVO.getResult() == BaseVO.SUCCESS){
			ActionLogCache.insert(request, "管理后台删除帖子");
		}else{
			ActionLogCache.insert(request, "管理后台删除帖子", "失败："+baseVO.getInfo());
		}
		return baseVO;
	}
	
	/**
	 * 添加／修改板块提交页面
	 * @param postClass {@link PostClass}
	 */
	@RequiresPermissions("adminBbsClass")
	@RequestMapping("saveClass")
	@ResponseBody
	public BaseVO saveClass(HttpServletRequest request, Model model){
		BaseVO baseVO = postService.savePostClass(request);
		if(baseVO.getResult() == BaseVO.SUCCESS){
			ActionLogCache.insert(request, "管理后台添加论坛板块保存");
		}else{
			ActionLogCache.insert(request, "管理后台添加论坛板块保存", "失败："+baseVO.getInfo());
		}
		
		return baseVO;
	}

	/**
	 * 编辑板块
	 * @param id 板块id，PostClass.id
	 */
	@RequiresPermissions("adminBbsClass")
	@RequestMapping("class")
	public String editClass(@RequestParam(value = "id", required = false, defaultValue="0") int id,Model model, HttpServletRequest request){
		if(id>0){
			//修改
			PostClass postClass = sqlService.findById(PostClass.class, id);
			if(postClass!=null){
				model.addAttribute("postClass", postClass);
				ActionLogCache.insert(request, id, "管理后台编辑论坛板块页面");
			}else{
				ActionLogCache.insert(request, id, "管理后台编辑论坛板块页面", "出错：板块不存在");
				return "板块不存在";
			}
		}else{
			//新增
			ActionLogCache.insert(request, "管理后台添加论坛板块页面");
		}
		
		return "iw/admin/bbs/class";
	}
	

	/**
	 * 删除板块
	 * @param id 板块id，PostClass.id
	 */
	@RequiresPermissions("adminBbsDeleteClass")
	@RequestMapping("deleteClass")
	@ResponseBody
	public BaseVO deleteClass(@RequestParam(value = "id", required = true) int id, Model model, HttpServletRequest request){
		BaseVO baseVO = postService.deletePostClass(id);
		if(baseVO.getResult() == BaseVO.SUCCESS){
			ActionLogCache.insert(request, "管理后台删除论坛板块");
		}else{
			ActionLogCache.insert(request, "管理后台删除论坛板块", "出错："+baseVO.getInfo());
		}
		
		return baseVO;
	}
	

	/**
	 * 评论列表
	 * @param request {@link HttpServletRequest}
	 */
	@RequiresPermissions("adminBbsPostCommentList")
	@RequestMapping("commentList")
	public String commentList(HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"postid=","userid="});
		sql.appendWhere("isdelete = "+BaseEntity.ISDELETE_NORMAL);
		int count = sqlService.count("post_comment", sql.getWhere());
		Page page = new Page(count, Global.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT * FROM post_comment", page);
		sql.setDefaultOrderBy("post_comment.id DESC");
		List<PostComment> list = sqlService.findBySql(sql, PostComment.class);
		
		ActionLogCache.insert(request, "管理后台评论列表", "第"+page.getCurrentPageNumber()+"页");
		
		model.addAttribute("page", page);
		model.addAttribute("list", list);
		return "/iw/admin/bbs/commentList";
	}
	

	/**
	 * 删除帖子评论
	 * @param id 帖子评论的id，PostComment.id
	 */
	@RequiresPermissions("adminBbsDeletePostComment")
	@RequestMapping("deleteComment")
	@ResponseBody
	public BaseVO deleteComment(@RequestParam(value = "id", required = true) int id, Model model, HttpServletRequest request){
		BaseVO baseVO = postService.deleteComment(id);
		if(baseVO.getResult() == BaseVO.SUCCESS){
			ActionLogCache.insert(request, "管理后台删除帖子评论");
		}else{
			ActionLogCache.insert(request, "管理后台删除帖子评论", "失败："+baseVO.getInfo());
		}
		return baseVO;
	}
	
}
