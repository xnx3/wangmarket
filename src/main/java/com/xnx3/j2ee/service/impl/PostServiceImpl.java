package com.xnx3.j2ee.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.xnx3.DateUtil;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.entity.BaseEntity;
import com.xnx3.j2ee.entity.Post;
import com.xnx3.j2ee.entity.PostClass;
import com.xnx3.j2ee.entity.PostComment;
import com.xnx3.j2ee.entity.PostData;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.func.Language;
import com.xnx3.j2ee.generateCache.Bbs;
import com.xnx3.j2ee.service.PostService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.PostVO;

@Service
public class PostServiceImpl implements PostService {
	
	@Resource
	private SqlDAO sqlDAO;

	public SqlDAO getSqlDAO() {
		return sqlDAO;
	}

	public void setSqlDAO(SqlDAO sqlDAO) {
		this.sqlDAO = sqlDAO;
	}

	public BaseVO savePost(HttpServletRequest request) {
		BaseVO baseVO = new BaseVO();
		int id = Lang.stringToInt(request.getParameter("id"), 0);
		int classid = Lang.stringToInt(request.getParameter("classid"), 0);
		String title = StringUtil.filterXss(request.getParameter("title"));
		String text = StringUtil.filterXss(request.getParameter("text"));
		
		if(classid == 0){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_addPostPleaseSelectClass"));
			return baseVO;
		}
		if(title==null || title.length()<Global.bbs_titleMinLength || title.length()>Global.bbs_titleMaxLength){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_addPostTitleSizeFailure").replaceAll("\\$\\{min\\}", Global.bbs_titleMinLength+"").replaceAll("\\$\\{max\\}", Global.bbs_titleMaxLength+""));
			return baseVO;
		}
		if(text==null || text.length()<Global.bbs_textMinLength){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_addPostTextSizeFailure").replaceAll("\\$\\{min\\}",Global.bbs_textMinLength+""));
			return baseVO;
		}
		
		Post post = new com.xnx3.j2ee.entity.Post();
		PostData postData = new PostData();
		if(id != 0){
			post = sqlDAO.findById(Post.class, id);
			if(post == null){
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_updatePostNotFind"));
				return baseVO;
			}else{
				post.setId(id);
				postData = sqlDAO.findById(PostData.class, post.getId());
			}
		}else{
			post.setAddtime(com.xnx3.DateUtil.timeForUnix10());
			post.setState(Post.STATE_NORMAL);
			post.setUserid(ShiroFunc.getUser().getId());
			post.setView(0);
			post.setIsdelete(Post.ISDELETE_NORMAL);
		}
		
		String info="";	//截取简介文字,30字
		String filterText = StringUtil.filterHtmlTag(text);
		if(filterText.length()<60){
			info=filterText;
		}else{
			info=filterText.substring(0,60);
		}
		
		post.setTitle(title);
		post.setClassid(classid);
		post.setInfo(info);
		sqlDAO.save(post);
		
		if(postData.getPostid()==null){
			postData.setPostid(post.getId());
		}
		postData.setText(text);
		sqlDAO.save(postData);
		
		baseVO.setBaseVO(BaseVO.SUCCESS, post.getId()+"");
		if(id == 0){
//			logDAO.insert(post.getId(), "BBS_POST_ADD", post.getTitle());
		}else{
//			logDAO.insert(post.getId(), "BBS_POST_UPDATE", post.getTitle());
		}
		
		return baseVO;
	}

	public BaseVO deletePost(int id) {
		BaseVO baseVO = new BaseVO();
		if(id>0){
			Post p = sqlDAO.findById(Post.class, id);
			if(p!=null){
				p.setIsdelete(BaseEntity.ISDELETE_DELETE);
				sqlDAO.save(p);
//				logDAO.insert(p.getId(), "ADMIN_SYSTEM_BBS_POST_DELETE", p.getTitle());
			}else{
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_deletePostNotFind"));
			}
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_deletePostIdFailure"));
		}
		return baseVO;
	}

	public PostVO read(int id) {
		PostVO postVO = new PostVO();
		
		if(id>0){
			//查询帖子详情
			Post post=sqlDAO.findById(Post.class, id);
			if(post == null){
				postVO.setBaseVO(PostVO.FAILURE, Language.show("bbs_viewPostNotFind"));
				return postVO;
			}
			
			//查看帖子所属用户
			User user = sqlDAO.findById(User.class, post.getUserid());
			//检验此用户状态是否正常，是否被冻结
			if(user.getIsfreeze() == User.ISFREEZE_FREEZE){
				postVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_postCreateUserIsFreeze"));
				return postVO;
			}
			postVO.setUser(user);
			
			//查所属板块
			PostClass postClass = sqlDAO.findById(PostClass.class, post.getClassid());
			if(postClass == null || postClass.getIsdelete() == BaseEntity.ISDELETE_DELETE){
				postVO.setBaseVO(PostVO.FAILURE, Language.show("bbs_postViewPostClassIsNotFind"));
			}else{
				postVO.setPostClass(postClass);
				postVO.setPost(post);
				post.setView(post.getView()+1);
				sqlDAO.save(post);
				
				PostData postData = sqlDAO.findById(PostData.class, post.getId());
				postVO.setText(postData.getText());
				
				postVO.setCommentCount(count(post.getId()));
				
				if(Global.bbs_readPost_addLog){
//					logDAO.insert(post.getId(), "BBS_POST_VIEW", post.getTitle());
				}
			}
		}else{
			postVO.setBaseVO(PostVO.FAILURE, Language.show("bbs_postIdFailure"));
		}
		
		return postVO;
	}

	public int count(int postid) {
		return sqlDAO.count("post_comment", "WHERE postid= "+postid);
	}


	public BaseVO deleteComment(int id) {
		BaseVO baseVO = new BaseVO();
		if(id>0){
			PostComment pc = sqlDAO.findById(PostComment.class, id);
			if(pc!=null){
				pc.setIsdelete(BaseEntity.ISDELETE_DELETE);
				sqlDAO.save(pc);
//				logDAO.insert(pc.getId(), "BBS_POST_DELETE_COMMENT", pc.getText());
			}else{
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_deleteCommentNotFind"));
			}
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_commentIdFailure"));
		}
		return baseVO;
	}

	public BaseVO addComment(HttpServletRequest request) {
		BaseVO baseVO = new BaseVO();
		int postid = Lang.stringToInt(request.getParameter("postid"), 0);
		String text = request.getParameter("text");
		if(postid == 0){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_unknowCommentPost"));
			return baseVO;
		}
		
		if(text==null || text.length()<Global.bbs_commentTextMinLength || text.length()>Global.bbs_commentTextMaxLength){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_commentSizeFailure").replaceAll("\\$\\{min\\}", Global.bbs_commentTextMinLength+"").replaceAll("\\$\\{max\\}", Global.bbs_commentTextMaxLength+""));
			return baseVO;
		}
		
		//先查询是不是有这个主贴
		Post p=sqlDAO.findById(Post.class, postid);
		if(p == null){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_commentPostNotFind"));
			return baseVO;
		}
		
		if(p.getState() != Post.STATE_NORMAL){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_commentPostIsNotNormal"));
			return baseVO;
		}
		
		if(p.getIsdelete() == Post.ISDELETE_DELETE){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_commentPostIsDelete"));
			return baseVO;
		}
		
		PostComment postComment=new PostComment();
		postComment.setPostid(p.getId());
		postComment.setUserid(ShiroFunc.getUser().getId());
		postComment.setAddtime(DateUtil.timeForUnix10());
		postComment.setText(text);
		postComment.setIsdelete(PostComment.ISDELETE_NORMAL);
		sqlDAO.save(postComment);
		
//		logDAO.insert(postComment.getId(), "BBS_POST_COMMENT_ADD", StringUtil.filterHtmlTag(postComment.getText()));
		return baseVO;
	}

	public List commentAndUser(int postid) {
		return commentAndUser(postid, 0);
	}

	public List commentAndUser(int postid, int limit) {
		String limitString="";
		if(limit > 0){
			limitString = " LIMIT 0,"+limit;
		}
		
		return sqlDAO.findMapBySqlQuery("SELECT comment.addtime,comment.userid,comment.text,user.head,user.nickname,user.id FROM post_comment comment,user WHERE comment.userid=user.id AND comment.postid= "+postid+" AND user.isfreeze="+User.ISFREEZE_NORMAL+" ORDER BY comment.id DESC "+limitString);
	}
	
	public BaseVO savePostClass(HttpServletRequest request) {
		BaseVO baseVO = new BaseVO();
		int id = Lang.stringToInt(request.getParameter("id"), 0);
		String name = request.getParameter("name");
		if(name == null || name.length()==0){
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_savePostClassNameNotNull"));
			return baseVO;
		}
		
		PostClass postClass = null;
		if(id==0){	//新增
			postClass = new PostClass();
		}else{
			//修改
			postClass = sqlDAO.findById(PostClass.class, id);
			if(postClass == null){
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_updatePostClassNotFind"));
				return baseVO;
			}
		}
		
		postClass.setName(name);
		postClass.setIsdelete(BaseEntity.ISDELETE_NORMAL);
		
		sqlDAO.save(postClass);
		if((id==0 && postClass.getId()>0) || id > 0){
			if(id>0){
//				logDAO.insert(postClass.getId(), "ADMIN_SYSTEM_BBS_CLASS_SAVE", postClass.getName());
			}else{
//				logDAO.insert(postClass.getId(), "ADMIN_SYSTEM_BBS_CLASS_ADD", postClass.getName());
			}
			gerenatePostClassCacheJsFile();
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_savePostClassFailure"));
		}
		return baseVO;
	}

	public BaseVO deletePostClass(int id) {
		BaseVO baseVO = new BaseVO();
		if(id>0){
			PostClass pc = sqlDAO.findById(PostClass.class, id);
			if(pc!=null){
				pc.setIsdelete(BaseEntity.ISDELETE_DELETE);
				sqlDAO.save(pc);
//				logDAO.insert(pc.getId(), "ADMIN_SYSTEM_BBS_POST_DELETE", pc.getName());
				gerenatePostClassCacheJsFile();
			}else{
				baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_deletePostClassNotFind"));
			}
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, Language.show("bbs_pleaseAddDeletePostClassID"));
		}
		return baseVO;
	}
	
	/**
	 * 生成正常未删除的PostClass的缓存的js文件
	 */
	private void gerenatePostClassCacheJsFile(){
		new Bbs().postClass(sqlDAO.findByProperty(PostClass.class, "isdelete", BaseEntity.ISDELETE_NORMAL));
	}
}
