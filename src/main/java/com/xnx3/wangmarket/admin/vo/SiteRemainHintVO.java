package com.xnx3.wangmarket.admin.vo;

import com.xnx3.j2ee.vo.BaseVO;

/**
 * 站点过期提示，网站登陆后，即将过期时的提示
 * <br/>其中的result参数			
 * 			<ul>
 * 				<li>当result为成功( SiteRemainHintVO.SUCCESS )时，网站正常不需要过期提醒。</li>
 * 				<li>当result为失败( ( SiteRemainHintVO.FAILURE ) )时，就是快要过期了，使用过时间还不到两个月了，需要提示</li>
 * 				<li>当result为 3 时，网站已经到期</li>
 * 			</ul>
 * @author 管雷鸣
 */
public class SiteRemainHintVO extends BaseVO {
	private String remainTimeString;	//距离过期剩余时间文字提醒
	private String companyName;		//此网站上级代理的名字，公司名
	private String phone;		//此网站上级代理的联系电话
	private String qq;			//此网站上级代理的联系QQ
	
	public String getRemainTimeString() {
		return remainTimeString;
	}
	public void setRemainTimeString(String remainTimeString) {
		this.remainTimeString = remainTimeString;
		setInfo(remainTimeString);
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	
	
}