package com.xnx3.j2ee.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xnx3.j2ee.bean.PermissionTree;
import com.xnx3.j2ee.entity.Permission;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.util.SessionUtil;

/**
 * 用户身份信息，存入session 由于tomcat将session会序列化在本地硬盘上，所以使用Serializable接口
 * @author 管雷鸣
 *
 */
public class ActiveUser implements Serializable{
	private Integer id;	//user.id
	
	private User user;	//用户信息
	private List<Permission> permissions;// 拥有的权限
	private List<PermissionTree> permissionTreeList;	//入口列表菜单树
	
	/**
	 * {@link SessionUtil#getPlugin(String)}
	 * 缓存 功能插件 的一些信息。比如关键词插件，用户登录成功后，当编辑过一篇文章时，将关键词缓存到这里，在编辑其他文章时直接从这里取数据即可 。
	 * key: 如插件的id，比如网站管理后台，用户登录成功后，吧自己管理的网站也会缓存到session中，那这个网站的key为：wangmarket_site   
	 * value：插件存储的数据
	 * 取时，使用 
	 * v5.0增加
	 */
	private HashMap<String, Object> pluginMap;
	
	/**
	 * 入口列表菜单树
	 * @return
	 */
	public List<PermissionTree> getPermissionTreeList() {
		return permissionTreeList;
	}
	public void setPermissionTreeList(List<PermissionTree> permissionTreeList) {
		this.permissionTreeList = permissionTreeList;
	}

	/**
	 * 拥有的权限
	 * @return
	 */
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * 用户信息
	 * @return
	 */
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
		if(user != null){
			this.id = user.getId();
			System.out.println("-------setid:"+this.id);
		}
	}
	
	public HashMap<String, Object> getPluginMap() {
		if(this.pluginMap == null){
			this.pluginMap = new HashMap<String, Object>();
		}
		return pluginMap;
	}
	public void setPluginMap(HashMap<String, Object> pluginMap) {
		this.pluginMap = pluginMap;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
