package com.xnx3.j2ee.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.xnx3.j2ee.entity.Post;
import com.xnx3.j2ee.entity.PostClass;
import com.xnx3.j2ee.entity.PostComment;
import com.xnx3.j2ee.entity.PostData;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.PostVO;
/**
 * 论坛帖子
 * @author 管雷鸣
 *
 */
public interface PostService {

	/**
	 * 发表帖子
	 * @param request {@link HttpServletRequest}
	 * 			<br/>form表单提交项：
	 * 			<ul>
	 * 				<li>id(帖子id)，若有id，则是修改，若无id，则是新增发表帖子</li>
	 * 				<li>title(标题)，发表帖子的标题</li>
	 * 				<li>text(帖子内容)，发表帖子的内容</li>
	 * 				<li>classid(postClass.id)，发表的帖子属于哪个分类</li>
	 * 			</ul>
	 * @return {@link BaseVO} 若成功，info传回帖子的id
	 */
	public BaseVO savePost(HttpServletRequest request);
	
	/**
	 * 删除帖子，逻辑删除，改状态isdelete=1
	 * @param id 要删除的帖子的id，post.id
	 * @return {@link BaseVO}
	 */
	public BaseVO deletePost(int id);
	
	/**
	 * 查看，阅读帖子，通过 {@link Post}.id获取此条帖子的具体信息，包含 {@link Post}、 {@link PostData}
	 * @param id {@link Post}.id
	 * @return {@link PostVO}
	 * 			<br/>首先判断getResult()是否是 {@link BaseVO#SUCCESS}，若是，才可以调取其他的值。若不是，可通过getInfo()获取错误信息
	 */
	public PostVO read(int id);
	

	/**
	 * 根据帖子id查回帖的数量
	 * @param postid 帖子id
	 * @return 回帖数量
	 */
	public int count(int postid);
	

	/**
	 * 添加回复，回帖
	 * @param request {@link HttpServletRequest}
	 * 			<br/>form表单提交项：
	 * 			<ul>
	 * 				<li>postid(帖子的id)  {@link Post}.id)</li>
	 * 				<li>text(回复内容)，发表回帖的内容</li>
	 * 			</ul>
	 * @return {@link BaseVO}
	 */
	public BaseVO addComment(HttpServletRequest request);
	
	/**
	 * 删除帖子评论，逻辑删除，改状态isdelete=1
	 * @param id 要删除的评论的id， {@link PostComment}.id
	 * @return {@link BaseVO}
	 */
	public BaseVO deleteComment(int id);
	
	/**
	 * 根据帖子id查回帖，排序为 id DESC
	 * @param postid 帖子id
	 * @param limit 条数，若为0则显示所有
	 * @return List<comment.addtime,comment.userid,comment.text,user.head,user.nickname,user.id>
	 */
	public List commentAndUser(int postid,int limit);
	
	/**
	 * 根据帖子id查所有回帖，排序为 id DESC
	 * @param postid 帖子id
	 * @return List<comment.addtime,comment.userid,comment.text,user.head,user.nickname,user.id>
	 */
	public List commentAndUser(int postid);
	

	/**
	 * 添加、修改板块
	 * @param request {@link HttpServletRequest}
	 * 			<br/>form表单提交项：
	 * 			<ul>
	 * 				<li>id(板块id， {@link PostClass}.id)，若有id，则是修改，若无id，则是新增板块</li>
	 * 				<li>name(板块名)，新增的板块的名字</li>
	 * 			</ul>
	 * @return {@link BaseVO} 若成功，info传回板块的id
	 */
	public BaseVO savePostClass(HttpServletRequest request);
	
	/**
	 * 删除板块，逻辑删除，改状态isdelete=1
	 * @param id 要删除的板块的id，PostClass.id
	 * @return {@link BaseVO}
	 */
	public BaseVO deletePostClass(int id);
}