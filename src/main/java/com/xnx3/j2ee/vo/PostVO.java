package com.xnx3.j2ee.vo;

import com.xnx3.j2ee.entity.Post;
import com.xnx3.j2ee.entity.PostClass;
import com.xnx3.j2ee.entity.PostData;
import com.xnx3.j2ee.entity.User;

/**
 * 组合好的Post，包含 {@link Post}、 {@link PostData}
 * <br/>首先判断getResult()是否是 {@link BaseVO#SUCCESS}，若是，才可以调取其他的值。若不是，可通过getInfo()获取错误信息
 * @author 管雷鸣
 *
 */
public class PostVO extends BaseVO {
	private Post post;
	private User user;	//发帖用户
	private String text;	//帖子的内容
	private PostClass postClass;	//所属的栏目信息
	private int commentCount;	//回帖评论总数
	
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	/**
	 * 发帖用户
	 * @return
	 */
	public User getUser() {
		return user;
	}
	/**
	 * 发帖用户
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * 帖子的内容
	 * @return
	 */
	public String getText() {
		return text;
	}
	/**
	 * 帖子的内容
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * 所属的栏目信息
	 * @return
	 */
	public PostClass getPostClass() {
		return postClass;
	}
	/**
	 * 所属的栏目信息
	 * @param postClass
	 */
	public void setPostClass(PostClass postClass) {
		this.postClass = postClass;
	}
	/**
	 * 回帖评论总数
	 * @return
	 */
	public int getCommentCount() {
		return commentCount;
	}
	/**
	 * 回帖评论总数
	 * @param commentCount
	 */
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	
}
