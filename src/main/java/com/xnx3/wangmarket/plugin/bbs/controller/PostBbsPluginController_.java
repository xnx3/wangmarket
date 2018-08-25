package com.xnx3.wangmarket.plugin.bbs.controller;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xnx3.Lang;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.func.ActionLogCache;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.controller.BaseController;
import com.xnx3.wangmarket.plugin.bbs.entity.Post;
import com.xnx3.wangmarket.plugin.bbs.entity.PostClass;
import com.xnx3.wangmarket.plugin.bbs.service.PostService;
import com.xnx3.wangmarket.plugin.bbs.vo.PostVO;

/**
 * 论坛，帖子处理
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/plugin/bbs/")
public class PostBbsPluginController_ extends BaseController {
	@Resource
	private PostService postService;
	@Resource
	private SqlService sqlService;
	@Resource
	private UserService userService;
	
	/**
	 * 发帖
	 * @param classid 要发表到哪个分类下(论坛板块)
	 */
//	@RequiresPermissions("bbsAddPost")
	@RequestMapping("addPost${url.suffix}")
	public String addPost(
			@RequestParam(value = "classid", required = false, defaultValue="0") int classid,
			Model model, HttpServletRequest request){
		if(classid==0){
			classid=Global.getInt("BBS_DEFAULT_PUBLISH_CLASSID");
		}
		
		//缓存优化
		List<PostClass> postClassList = sqlService.findAll(PostClass.class); 
		
		ActionLogCache.insert(request, classid, "进入发帖页面");
		
		model.addAttribute("postClassList", postClassList);
		model.addAttribute("classid", classid);
		return "plugin/bbs/post/addPost";
	}
	
	/**
	 * 发帖提交页面
	 * @param text 帖子内容
	 */
//	@RequiresPermissions("bbsAddPost")
	@RequestMapping("addPostSubmit${url.suffix}")
	public String addPostSubmit(HttpServletRequest request, Model model){
		BaseVO baseVO = postService.savePost(request);
		if(baseVO.getResult() == BaseVO.SUCCESS){
			ActionLogCache.insert(request, Lang.stringToInt(baseVO.getInfo(), 0), "帖子发布成功");
			return success(model, "操作成功！", "bbs/view.do?id="+baseVO.getInfo());
		}else{
			ActionLogCache.insert(request, "帖子发布失败", baseVO.getInfo());
			return error(model, baseVO.getInfo());
		}
	}
	
	/**
	 * 帖子列表
	 * @param post {@link Post}，其中classid: 
	 * 				<ul>
	 * 					<li>0:所有
	 * 					<li>其余数字就是搜索的classid
	 * 				</ul>
	 */
//	@RequiresPermissions("bbsList")
	@RequestMapping("list${url.suffix}")
	public String list(Post post,HttpServletRequest request,Model model){
		Sql sql = new Sql(request);
		sql.setSearchColumn(new String[]{"classid=","title","view>","info","addtime","userid="});
		sql.appendWhere("post.isdelete = "+Post.ISDELETE_NORMAL);
		int count = sqlService.count("post", sql.getWhere());
		Page page = new Page(count, Global.getInt("LIST_EVERYPAGE_NUMBER"), request);
		sql.setSelectFromAndPage("SELECT post.*, user.nickname, user.head FROM post LEFT JOIN user ON user.id = post.userid ", page);
		sql.setDefaultOrderBy("post.id DESC");
		List<Map<String, Object>> list = sqlService.findMapBySql(sql);
		
		ActionLogCache.insert(request, "查看帖子列表");
		model.addAttribute("page", page);
		model.addAttribute("list", list);
		return "iw/bbs/list";
	}
	
	/**
	 * 查看帖子详情
	 * @param post {@link Post}
	 */
	@RequiresPermissions("bbsView")
	@RequestMapping("view${url.suffix}")
	public String view(@RequestParam(value = "id", required = true) int id,Model model, HttpServletRequest request){
		PostVO postVO = postService.read(id);
		if(postVO.getResult() == PostVO.SUCCESS){
			//查询回帖
			List commentList = postService.commentAndUser(postVO.getPost().getId(),10);
			model.addAttribute("postVO", postVO);
			model.addAttribute("commentList", commentList);
			
			ActionLogCache.insert(request, id, "查看帖子详情", postVO.getPost().getTitle());
			return "iw/bbs/view";
		}else{
			ActionLogCache.insert(request, id, "查看帖子详情", "出错："+postVO.getInfo());
			return error(model, postVO.getInfo());
		}
	}
	
	/**
	 * 回帖处理
	 * @param post {@link Post}
	 * @param text 回帖内容
	 */
	@RequiresPermissions("bbsAddComment")
	@RequestMapping("addCommentSubmit${url.suffix}")
	public String commentSubmit(HttpServletRequest request,Model model){
		BaseVO baseVO = postService.addComment(request);
		if(baseVO.getResult() == BaseVO.SUCCESS){
			ActionLogCache.insert(request, "回帖");
			return success(model, "回复成功！","bbs/view.do?id="+request.getParameter("postid"));
		}else{
			ActionLogCache.insert(request, "回帖", "出错："+baseVO.getInfo());
			return error(model, baseVO.getInfo());
		}
	}
}
