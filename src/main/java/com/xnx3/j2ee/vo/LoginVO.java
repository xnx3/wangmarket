package com.xnx3.j2ee.vo;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.vo.BaseVO;

/**
 * 登陆
 * @author 管雷鸣
 */
public class LoginVO extends BaseVO {
	private String token;	//sessionid
	private User user;		//登录的用户信息

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	/**
	 * 设置用户信息。这里会自动去除密码、sale等秘密信息
	 * @param user
	 */
	public void setUser(User user) {
		if(user == null){
			return;
		}
		this.user = new User();
		this.user.setHead(user.getHead());
		this.user.setId(user.getId());
		this.user.setLasttime(user.getLasttime());
		this.user.setLastip(user.getLastip());
		this.user.setMoney(user.getMoney());
		this.user.setNickname(user.getNickname());
		this.user.setPhone(user.getPhone());
		this.user.setSign(user.getSign());
		this.user.setUsername(user.getUsername());
	}

	@Override
	public String toString() {
		return "LoginVO [token=" + token + ", user=" + user + "]";
	}
	
}
