package com.xnx3.wangmarket.agencyadmin.service;

import javax.servlet.http.HttpServletRequest;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.vo.UserVO;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;

/**
 * 有关事务的
 * @author 管雷鸣
 */
public interface TransactionalService {
	

	/**
	 * 转移站币到子代理账户，向自己的下级代理转账（站币），为其充值站币
	 * @param request 主要拿操作者ip地址
	 * @param targetAgencyId 要充值站币的代理的id，下级代理的id，agency.id
	 * @param transferSiteSize 要充值的站币数量，必须大于0
	 */
	public BaseVO transferSiteSizeToSubAgency(HttpServletRequest request, int targetAgencyId, int transferSiteSize);
	
	
	/**
	 * 站点续费，给自己开通的站点续费时长
	 * @param siteid 要续费的站点id，site.id
	 * @param year 要续费的年数，支持1～10，最大续费10年
	 * @return
	 */
	public BaseVO siteXuFei(HttpServletRequest request, int siteid, int year);
	
	/**
	 * 代理账户创建网站接口。  代理后台中创建网站、API接口通过代理key来创建网站，都是用的这个
	 * @param request
	 * @param agency 当前登录用户的agency对象。需要从数据库中新查询的，保存会直接保存此对象 sql.save(agency)
	 * @param user	要创建得用户信息
	 * @param site 要创建得网站
	 * @param email	邮箱，存入用户表，user.email
	 * @return
	 */
	public BaseVO agencyCreateSite(HttpServletRequest request, Agency agency, User user, Site site, String email);
	
	/**
	 * 注册User，代理商开通用户网站
	 * @param user
	 * @param request
	 * @param isAgency 是否是开通的普通代理，true，是开通普通代理
	 * @return 生成的用户User对象
	 */
	public UserVO regUser(User user, HttpServletRequest request, boolean isAgency);
}
