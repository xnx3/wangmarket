package com.xnx3.wangmarket.admin.util.TemplateAdminMenu;

public class TwoMenu {
	private String twoMenu_dd_id;		//二级菜单中，dd标签的id
	private String twoMenu_a_id;			//二级菜单中，a标签的id
	private String twoMenu_a_href;		//二级菜单中，a标签的href
	private String twoMenu_font;			//二级菜单中，a标签的文字，如： 模版页面
	
	
	public static void main(String[] args) {
		
		for (TemplateMenuEnum e : TemplateMenuEnum.values()) { 
		    System.out.println(e.id); 
		} 
		
	}
	
	public String getTwoMenu_dd_id() {
		return twoMenu_dd_id;
	}
	public void setTwoMenu_dd_id(String twoMenu_dd_id) {
		this.twoMenu_dd_id = twoMenu_dd_id;
	}
	public String getTwoMenu_a_id() {
		return twoMenu_a_id;
	}
	public void setTwoMenu_a_id(String twoMenu_a_id) {
		this.twoMenu_a_id = twoMenu_a_id;
	}
	public String getTwoMenu_a_href() {
		return twoMenu_a_href;
	}
	public void setTwoMenu_a_href(String twoMenu_a_href) {
		this.twoMenu_a_href = twoMenu_a_href;
	}
	public String getTwoMenu_font() {
		return twoMenu_font;
	}
	public void setTwoMenu_font(String twoMenu_font) {
		this.twoMenu_font = twoMenu_font;
	}
}
