package com.xnx3.j2ee.shiro;

import java.util.List;
import com.xnx3.j2ee.bean.PermissionTree;
import com.xnx3.j2ee.entity.Permission;
import com.xnx3.j2ee.entity.User;

/**
 * 用户身份信息，存入session 由于tomcat将session会序列化在本地硬盘上，所以使用Serializable接口
 * @author 管雷鸣
 * @deprecated 已废弃，请使用 {@link com.xnx3.j2ee.bean.ActiveUser} 。此保留着，只是位列适配v5.0版本之前的一些插件而已
 */
public class ActiveUser {
	
	private User user;	//用户信息
	private Object obj;	//登陆成功后，用户可自定义放入其存储的信息
	private String ueUploadParam1;	//UEditor上传文件相关的参数，可用{uploadParam1}来调用。每个人的上传参数都会不同
	private boolean allowUploadForUEditor;	//百度UEditor编辑器允许上传文件
	private List<Permission> permissions;// 拥有的权限
	private List<PermissionTree> permissionTreeList;	//入口列表菜单树
	private String languagePackageName;	//当前使用哪个语言包
	
	
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
	}
	
	public boolean isAllowUploadForUEditor() {
		return allowUploadForUEditor;
	}
	public void setAllowUploadForUEditor(boolean allowUploadForUEditor) {
		this.allowUploadForUEditor = allowUploadForUEditor;
	}
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String getUeUploadParam1() {
		return ueUploadParam1;
	}
	/**
	 * UEditor上传文件相关的路径替换参数，可用{uploadParam1}来调用。每个人的上传参数都会不同
	 * @param ueUploadParam1 参数
	 */
	public void setUeUploadParam1(String ueUploadParam1) {
		this.ueUploadParam1 = ueUploadParam1;
	}
	
	public String getLanguagePackageName() {
		return languagePackageName;
	}
	public void setLanguagePackageName(String languagePackageName) {
		this.languagePackageName = languagePackageName;
	}
	
	
}
