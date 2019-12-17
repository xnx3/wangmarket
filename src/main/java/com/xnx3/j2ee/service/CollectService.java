package com.xnx3.j2ee.service;

import com.xnx3.j2ee.entity.Collect;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 关注
 * @author 管雷鸣
 *
 */
public interface CollectService {

	/**
	 * 增加关注
	 * @param userid 要关注的用户的id
	 * @return {@link BaseVO} 若成功，info返回关注成功的 collect.id
	 */
	public BaseVO addCollect(int userid);
	
	/**
	 * 取消关注
	 * @param userid 要撤销关注的用户的id
	 * @return {@link BaseVO}
	 */
	public BaseVO cancelCollect(int userid);
	
	/**
	 * 检索我是否已经关注过此人了。
	 * <br/>只能是登陆状态使用，会自动加入我的userid进行搜索。若未登录，会返回null
	 * @param othersid 检索我是否关注过的人的userid
	 * @return
	 */
	public Collect findMyByOthersid(int othersid);
}