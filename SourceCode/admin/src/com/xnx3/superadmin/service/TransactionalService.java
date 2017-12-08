package com.xnx3.superadmin.service;

import javax.servlet.http.HttpServletRequest;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 有关事务的
 * @author 管雷鸣
 */
public interface TransactionalService {
	

	/**
	 * 转移站币到字代理账户，向自己的下级代理转账（站币），为其充值站币
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
}
