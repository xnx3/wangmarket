package com.xnx3.j2ee;

/**
 * 常用函数
 * @author 管雷鸣
 */
public class Func {

	/**
	 * 根据user表的authority字段的值，用户是否具有某个指定的角色(Role)
	 * @param authority User表的authority
	 * @param roleId role.id判断用户是否具有某个指定的角色，这里便是那个指定的角色，判断用户是否是授权了这个角色。多个用,分割，如传入  2,3,4
	 * 				<br/>
	 * @return true：是,用户拥有此角色
	 */
	public static boolean isAuthorityBySpecific(String authority, String roleId){
		String[] authArray = authority.split(",");
		for (int i = 0; i < authArray.length; i++) {
			String auth = authArray[i];
			if(auth != null && auth.length() > 0){
				if(auth.equals(roleId)){
					return true;
				}
			}
		}
		return false;
	}
	
}
