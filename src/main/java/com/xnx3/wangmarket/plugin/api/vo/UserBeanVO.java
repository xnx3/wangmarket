package com.xnx3.wangmarket.plugin.api.vo;

import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.agencyadmin.entity.Agency;

/**
 * key验证成功后，拿到此用户信息。同样，缓存也是根据key，缓存此
 * @author 管雷鸣
 *
 */
public class UserBeanVO extends BaseVO{
	private User user;
	private Agency agency;
	private Site site;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Agency getAgency() {
		return agency;
	}
	public void setAgency(Agency agency) {
		this.agency = agency;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	
	@Override
	public String toString() {
		return "UserBeanVO [user=" + user + ", agency=" + agency + ", site="
				+ site + ", getResult()=" + getResult() + ", getInfo()="
				+ getInfo() + "]";
	}
	
}
