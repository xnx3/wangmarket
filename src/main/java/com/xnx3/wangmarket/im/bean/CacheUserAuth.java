package com.xnx3.wangmarket.im.bean;

/**
 * 账户安全，授权，检测传入的id是否是真实的
 * 此会在用户建立socket连接后，若是非访客，则都会进行授权判断，判断这个id的真实性。
 * 判断时，先从map中，根据id(String)拉取auth对象，看是否有此对象，若没有，则读取数据库，从数据库中读取出此id用户的信息，
 * 将数据库中获取到的用户信息，创建一个 此对象，若数据库中查询到对应的user.id了，则将此user.id相关的信息赋予此对象，
 * 若数据库中没有查询到对应的id数据，也将其创建一个此对象，不过haveUser为false，意为数据库中没有这个userid，下次直接能从map中取到，就不用再多次找数据库了
 * @author 管雷鸣
 */
public class CacheUserAuth {
//	private boolean haveUser;	//数据库中是否有此id的用户。若有，则为true，若根据id查询数据库时没有此用户，则此为false，同时下面的userid、password则不会给其赋值。其判断主要是根据权限来判断的，若权限>0，则肯定是有用户了
	private long userid;		//授权的用户id，对应user.id
	private String password;		//授权的用户password，对应user.password
	private int authority;	//当前用户拥有的权限，1:普通用户；9:超级管理员； 10:代理
	private long referrerid;	//当前用户的上级用户id

	public static final int AUTH_USER = 1;	//普通用户
	public static final int AUTH_SUPER_ADMIN = 9;	//超级管理员
	public static final int AUTH_AGENT = 10;	//代理
	
	/**
	 * 当前用户如果是代理商
	 * @return true：是
	 */
	public boolean isAgent(){
		return getAuthority() == CacheUserAuth.AUTH_AGENT;
	}
	
	/**
	 * 当前用户如果是超级管理员
	 * @return true：是
	 */
	public boolean isSuperAdmin(){
		return getAuthority() == CacheUserAuth.AUTH_SUPER_ADMIN;
	}

	/**
	 * 当前用户如果是游客
	 * @return true：是
	 */
	public boolean isVisit(){
		return getAuthority() == 0;
	}
	
	/**
	 * 当前用户如果是普通用户
	 * @return true：是
	 */
	public boolean isUser(){
		return getAuthority() == CacheUserAuth.AUTH_USER;
	}
	
	public boolean isHaveUser() {
		return authority > 0;
	}
	
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAuthority() {
		return authority;
	}
	public void setAuthority(int authority) {
		this.authority = authority;
	}
	public long getReferrerid() {
		return referrerid;
	}
	public void setReferrerid(long referrerid) {
		this.referrerid = referrerid;
	}

	@Override
	public String toString() {
		return "CacheUserAuth [userid=" + userid
				+ ", password=" + password + ", authority=" + authority
				+ ", referrerid=" + referrerid + "]";
	}
	
}
