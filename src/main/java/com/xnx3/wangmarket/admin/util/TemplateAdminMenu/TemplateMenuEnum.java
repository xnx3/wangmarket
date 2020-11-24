package com.xnx3.wangmarket.admin.util.TemplateAdminMenu;
/**
 * 网站管理后台的左侧菜单项的id唯一标示
 * @author 管雷鸣
 *
 */
public enum TemplateMenuEnum {
	SYSTEM("system", "系统设置", "javascript:;", "&#xe620;", ""),
	SYSTEM_JiBenXinXi("jibenxinxi", "基本信息", "javascript:;", "", "system"),
	SYSTEM_DomainBind("domainBind", "绑定域名", "javascript:updateBindDomain();", "", "system"),
	SYSTEM_WangZhanSheZhi("wangzhanshuxing", "网站设置", "javascript:;", "", "system"),
	SYSTEM_XiuGaiMiMa("xiugaimima", "修改密码", "javascript:updatePassword();", "", "system"),
	SYSTEM_YuLanWangZhan("chakanwangzhan", "预览网站", "javascript:window.open('/sites/sitePreview.do');", "", "system"),
	
	TEMPLATE("template", "模版管理", "javascript:;", "&#xe61b;", ""),
	TEMPLATE_QuanJuBianLiang("quanjubianliang", "全局变量", "javascript:loadIframeByUrl('/siteVar/list.do'), notUseTopTools();", "", "template"),
	TEMPLATE_MoBanBianLiang("mobanbianliang", "模版变量", "javascript:loadIframeByUrl('/template/templateVarList.do'), notUseTopTools();", "", "template"),
	TEMPLATE_MoBanYeMian("mobanyemian", "模版页面", "javascript:loadIframeByUrl('/template/templatePageList.do'), notUseTopTools();", "", "template"),
	TEMPLATE_ShuRuMoXing("shurumoxing", "输入模型", "javascript:loadIframeByUrl('/inputModel/list.do'), notUseTopTools();", "", "template"),
	TEMPLATE_DaoChuBeiFen("daochutemplate", "导出/备份", "javascript:exportTemplate();", "", "template"),
	TEMPLATE_DaoRuHuanYuan("daorutemplate", "导入/还原", "javascript:loadIframeByUrl('/template/selectTemplate.do'), notUseTopTools();", "", "template"),
	TEMPLATE_MoBanChaJian("templateplugin", "模版插件", "javascript:loadIframeByUrl('/template/templatePlugin.do'), notUseTopTools();", "", "template"),
	
	HELP("help", "帮助说明", "javascript:;", "&#xe60b;", ""),
	HELP_ShiYongRuMen("shiyongrumen", "使用入门", "javascript:loadIframeByUrl('/help/shiyongrumen.do'), notUseTopTools();", "", "help"),
	HELP_MoBanKaiFa("mobankaifa", "模版开发", "javascript:loadIframeByUrl('/help/mobankaifa.do'), notUseTopTools();", "", "help"),
	
	PLUGIN("plugin", "功能插件", "javascript:;", "&#xe857;", ""),
	
	COLUMN("column", "栏目管理", "javascript:loadIframeByUrl('/column/popupListForTemplate.do'), notUseTopTools();", "&#xe638;", ""),
	
	NEWS("news", "内容管理", "javascript:loadIframeByUrl('/news/listForTemplate.do'), notUseTopTools();", "&#xe647;", ""),
	
	SUPPORT("support", "技术支持", "javascript:jumpParentAgency();", "&#xe612;", ""),
	
	SHENGCHENGZHENGZHAN("generatehtml", "生成整站", "javascript:generatehtml();", "&#xe609;", "");
	
	
	
	
	public final String id;	//id，如 jibenxinxi 一级menu是直接就是id，但是二级不是直接用，加tag前缀，如 dd_jibenxinxi 、 a_jibenxinxi
	public final String name;	//菜单所显示的文字，如 基本信息
	public final String href;	//a标签的href的值
	public final String icon;	//一级菜单才有，也就是顶级菜单，前面会有个图标。这个就是。值如：&#xe620;
	public final String parentid;	//父菜单，上级菜单的id，如果已经是顶级菜单，这里没有值，为“”空字符串
	
	private TemplateMenuEnum(String id, String name, String href, String icon, String parentid) { 
		this.id = id; 
		this.name = name;
		this.href = href;
		this.icon = icon;
		this.parentid = parentid;
	}
}
