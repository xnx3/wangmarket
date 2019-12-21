package com.xnx3.j2ee.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.entity.Collect;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.CollectService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.LanguageUtil;
import com.xnx3.j2ee.vo.BaseVO;

@Service
public class CollectServiceImpl implements CollectService {
	@Resource
	private SqlDAO sqlDAO;

	public SqlDAO getSqlDAO() {
		return sqlDAO;
	}

	public void setSqlDAO(SqlDAO sqlDAO) {
		this.sqlDAO = sqlDAO;
	}

	
	/**
	 * 增加关注
	 * @param userid 要关注的用户的id
	 * @return {@link BaseVO} 若成功，info返回关注成功的 collect.id
	 */
	public BaseVO addCollect(int userid){
		BaseVO baseVO = new BaseVO();
		
		if(ShiroFunc.getUser().getId() == userid){
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("collect_notCollectOneself"));
			return baseVO;
		}
		
		User user = sqlDAO.findById(User.class, userid);
		if(user == null){
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("collect_null"));
			return baseVO;
		}
		
		Collect collect = sqlDAO.findAloneBySqlQuery("SELECT * FROM collect WHERE othersid = " + userid + " AND userid = " + ShiroFunc.getUser().getId(), Collect.class);
		if(collect != null){
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("collect_already"));
			return baseVO;
		}
		
		collect = new Collect();
		collect.setOthersid(userid);
		collect.setUserid(ShiroFunc.getUser().getId());
		collect.setAddtime(DateUtil.timeForUnix10());
		sqlDAO.save(collect);
		if(collect.getId()>0){
			baseVO.setBaseVO(BaseVO.SUCCESS, collect.getId()+"");
			return baseVO;
		}else{
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("collect_saveFailure"));
			return baseVO;
		}
	}

	public BaseVO cancelCollect(int userid) {
		BaseVO baseVO = new BaseVO();
		
//		Collect collect = new Collect();
//		collect.setOthersid(userid);
//		collect.setUserid(ShiroFunc.getUser().getId());
		Collect collect = sqlDAO.findAloneBySqlQuery("SELECT * FROM collect WHERE othersid = " + userid + " AND userid = " + ShiroFunc.getUser().getId(), Collect.class);
//		List<Collect> list = sqlDAO.findByExample(collect);
		if(collect == null){
			baseVO.setBaseVO(BaseVO.FAILURE, LanguageUtil.show("collect_notCollectSoNotCancel"));
			return baseVO;
		}
		sqlDAO.delete(collect);
		
		return baseVO;
	}
	
	/**
	 * 检索我是否已经关注过此人了。
	 * <br/>只能是登陆状态使用，会自动加入我的userid进行搜索。若未登录，会返回null
	 * @param othersid 检索我是否关注过的人的userid
	 * @return
	 */
	public Collect findMyByOthersid(int othersid){
		User user = ShiroFunc.getUser();
		if(user == null){
			return null;
		}
		
		
		Collect collect = sqlDAO.findAloneBySqlQuery("SELECT * FROM collect WHERE othersid = " + othersid + " AND userid = " + user.getId(), Collect.class);
		return collect;
	}

}
