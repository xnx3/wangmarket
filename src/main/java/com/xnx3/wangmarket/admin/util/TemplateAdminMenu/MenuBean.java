package com.xnx3.wangmarket.admin.util.TemplateAdminMenu;

import java.util.ArrayList;
import java.util.List;

import com.xnx3.wangmarket.admin.util.TemplateAdminMenu.TemplateMenuEnum;

/**
 * 网站管理后台，右侧菜单相关
 * @author 管雷鸣
 *
 */
public class MenuBean {
	
	public String id;	//id，如 jibenxinxi 一级menu是直接就是id，但是二级不是直接用，加tag前缀，如 dd_jibenxinxi 、 a_jibenxinxi
	public String name;	//菜单所显示的文字，如 基本信息
	public String href;	//a标签的href的值
	public String icon;	//一级菜单才有，也就是顶级菜单，前面会有个图标。这个就是。值如：&#xe620;
	public String parentid;	//父菜单，上级菜单的id，如果已经是顶级菜单，这里没有值，为“”空字符串
	
	List<MenuBean> subList;	//子菜单
	
	private int isUse;	//是否已使用，若是已经使用，则是1，没有使用，则是0
	
	public MenuBean(TemplateMenuEnum menuEnum) {
		this.id = menuEnum.id;
		this.name = menuEnum.name;
		this.href = menuEnum.href;
		this.icon = menuEnum.icon;
		this.parentid = menuEnum.parentid;
		
		//默认是0
		isUse = 0;
		this.subList = new ArrayList<MenuBean>();
	}

	public int getIsUse() {
		return isUse;
	}

	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getHref() {
		return href;
	}

	public String getIcon() {
		return icon;
	}

	public String getParentid() {
		return parentid;
	}

	public List<MenuBean> getSubList() {
		return subList;
	}

	public void setSubList(List<MenuBean> subList) {
		this.subList = subList;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	
}
