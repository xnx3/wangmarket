package com.xnx3.wangmarket.admin.util.TemplateAdminMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * 一级菜单，顶级菜单。网站用户登陆后，能看到哪些菜单，每个大菜单（一级菜单）都是这里生成的。每个能生成一个一级菜单
 * @author 管雷鸣
 *
 */
public class FirstMenu {
	/*
	 * 一级菜单，顶级菜单
	 */
	public String firstMenu_li_id;		//一级菜单中，li标签的id
	public String firstMenu_a_id;		//一级菜单中，a标签的id
	public String firstMenu_a_href;		//一级菜单中，a标签的href的值
	public String firstMenu_icon;		//一级菜单中，图标，如： &#xe609;
	public String firstMenu_font;		//一级菜单中，菜单显示的文字，如： 生成整站
	
	/*
	 * 二级菜单，子菜单。如果有值，那么就有二级菜单，没有的话就是没有二级菜单
	 */
	List<TwoMenu> twoMenuList;
	
	/**
	 * @param li_id 一级菜单中，li标签的id，没有可为null
	 * @param a_id 一级菜单中，a标签的id，没有可为null
	 * @param a_href 一级菜单中，a标签的href的值
	 * @param icon 一级菜单中，图标，如： &#xe609;
	 * @param font 一级菜单中，菜单显示的文字，如： 生成整站
	 */
	public FirstMenu(String li_id, String a_id, String a_href, String icon, String font) {
		twoMenuList = new ArrayList<TwoMenu>();
		
		firstMenu_li_id = li_id;
		firstMenu_a_id = a_id;
		firstMenu_a_href = a_href;
		firstMenu_icon = icon;
		firstMenu_font = font;
	}
	
	/**
	 * 向当前顶级菜单中，加入二级菜单
	 * @param dd_id dd的id，若没有，可为null
	 * @param a_id	a标签的id，若没有，可为null
	 * @param a_href	a标签的href的值，必填
	 * @param font		a标签的文字，也就是二级菜单的文字，必填
	 */
	public void addTwoMenu(String dd_id, String a_id, String a_href, String font){
		TwoMenu tmb = new TwoMenu();
		if(dd_id != null && dd_id.length() > 0){
			tmb.setTwoMenu_dd_id(dd_id);
		}
		if(a_id != null && a_id.length() > 0){
			tmb.setTwoMenu_a_id(a_id);
		}
		tmb.setTwoMenu_a_href(a_href);
		tmb.setTwoMenu_font(font);
		this.twoMenuList.add(tmb);
	}
	
	/**
	 * 生成当前菜单的li标签的html代码。
	 */
	public String gainMenuHTML(){
		StringBuffer sb = new StringBuffer();
		sb.append("<li class=\"layui-nav-item\""+((this.firstMenu_li_id != null && this.firstMenu_li_id.length() > 0)? " id=\""+this.firstMenu_li_id+"\" ":"")+">"
				+ "	<a "+((this.firstMenu_a_id != null || this.firstMenu_a_id.length() > 0)? "id=\""+this.firstMenu_a_id+"\"":"")+" href=\""+this.firstMenu_a_href+"\">"
				+ "		<i class=\"layui-icon firstMenuIcon\">"+this.firstMenu_icon+"</i>"
				+ "		<span class=\"firstMenuFont\">"+this.firstMenu_font+"</span>"
				+ "	</a>");
		//判断是否有子栏目，如果有，那么也加入进去
		if(this.twoMenuList.size() > 0){
			sb.append("<dl class=\"layui-nav-child\">");
			for (int i = 0; i < this.twoMenuList.size(); i++) {
				TwoMenu twoMenu = this.twoMenuList.get(i);
				sb.append("<dd "+((twoMenu.getTwoMenu_dd_id() != null && twoMenu.getTwoMenu_dd_id().length() > 0)? "id=\""+twoMenu.getTwoMenu_dd_id()+"\"":"")+" class=\"twoMenu\"><a "+((twoMenu.getTwoMenu_a_id() != null || twoMenu.getTwoMenu_a_id().length() > 0)? "id=\""+twoMenu.getTwoMenu_a_id()+"\"":"")+" class=\"subMenuItem\" href=\""+twoMenu.getTwoMenu_a_href()+"\">"+twoMenu.getTwoMenu_font()+"</a></dd>");
			}
			sb.append("</dl>");
		}
		
		sb.append("</li>");
		return sb.toString();
	}
	
}
