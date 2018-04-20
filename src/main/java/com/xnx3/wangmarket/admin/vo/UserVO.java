package com.xnx3.wangmarket.admin.vo;

import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 用户
 * @author 管雷鸣
 */
public class UserVO extends BaseVO {
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
