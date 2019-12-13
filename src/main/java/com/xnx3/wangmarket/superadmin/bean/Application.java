package com.xnx3.wangmarket.superadmin.bean;


/**
 * 应用插件
 * @author 管雷鸣
 *
 */
public class Application {
	/**
	 * 该插件的唯一标识。如自定义表单插件，唯一标识便是 formManage 。注意不能与其他插件重名
	 */
	private String id;
	/**
	 * 在网站管理后台中，功能插件下，显示的菜单项的标题文字，也就是插件的名字
	 */
	private String menuTitle;
	/**
	 * 是否在CMS模式网站管理后台的功能插件中显示， 1：是, 不填、0则是不显示
	 */
	private Short applyToCMS;
	/**
	 * 是否在电脑(pc)模式网站管理后台的功能插件中显示， 1：是, 不填、0则是不显示
	 */
	private Short applyToPC;
	/**
	 * 是否在手机(wap)模式网站管理后台的功能插件中显示， 1：是, 不填、0则是不显示
	 */
	private Short applyToWAP;
	/**
	 * 是否在代理后台的功能插件中显示， 1：是, 不填、0则是不显示
	 */
	private Short applyToAgency;
	/**
	 * 是否在总管理后台的功能插件中显示， 1：是, 不填、0则是不显示
	 */
	private Short applyToSuperAdmin;
	/**
	 * 该插件的简介说明,char(200)
	 */
	private String intro;
	/**
	 * 当前插件的版本号 ， 如  1.0  则是 100000000; 1.2.1 则是 100200100; 2.13.3则是 200130300
	 */
	private Integer version;
	/**
	 * 支持的网市场最低版本，规则也是同上，如4.7.1则是 400700100
	 */
	private Integer wangmarketVersionMin;
	/**
	 * 应用添加时间
	 */
	private Integer addtime;
	/**
	 * 应用最后改动时间
	 */
	private Integer updatetime;
	/**
	 * 作者名字
	 */
	private String authorName;
	/**
	 * 若wangmarket使用的OSS，是否支持该插件运行。 1支持，0或者其他是不支持
	 */
	private Short supportOssStorage;
	/**
	 * 若wangmarket使用的服务器本身进行的文件存储，是否支持该插件运行。 1支持，0或者其他是不支持
	 */
	private Short supportLocalStorage;
	/**
	 * 若wangmarket使用的SLS，是否支持该插件运行。 1支持，0或者其他是不支持
	 */
	private Short supportSls;
	/**
	 * 若wangmarket使用的Mysql数据库，是否支持该插件运行。 1支持，0或者其他是不支持
	 */
	private Short supportMysql;
	/**
	 * 若wangmarket使用的Sqlite数据库，是否支持该插件运行。 1支持，0或者其他是不支持
	 */
	private Short supportSqlite;
	/**
	 * 若wangmarket使用的是免费开源版本，是否支持该插件运行。 1支持，0或者其他是不支持
	 */
	private Short supportFreeVersion;
	/**
	 * 若wangmarket使用的是授权版本，是否支持该插件运行。 1支持，0或者其他是不支持
	 */
	private Short supportAuthorizeVersion;
	/**
	 * 插件下载的url
	 */
	private String downUrl;
	/**
	 * 插件是否被安装 0：未安装  1：已安装
	 */
	private Short installState;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMenuTitle() {
		return menuTitle;
	}
	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}
	public Short getApplyToCMS() {
		return applyToCMS;
	}
	public void setApplyToCMS(Short applyToCMS) {
		this.applyToCMS = applyToCMS;
	}
	public Short getApplyToPC() {
		return applyToPC;
	}
	public void setApplyToPC(Short applyToPC) {
		this.applyToPC = applyToPC;
	}
	public Short getApplyToWAP() {
		return applyToWAP;
	}
	public void setApplyToWAP(Short applyToWAP) {
		this.applyToWAP = applyToWAP;
	}
	public Short getApplyToAgency() {
		return applyToAgency;
	}
	public void setApplyToAgency(Short applyToAgency) {
		this.applyToAgency = applyToAgency;
	}
	public Short getApplyToSuperAdmin() {
		return applyToSuperAdmin;
	}
	public void setApplyToSuperAdmin(Short applyToSuperAdmin) {
		this.applyToSuperAdmin = applyToSuperAdmin;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getWangmarketVersionMin() {
		return wangmarketVersionMin;
	}
	public void setWangmarketVersionMin(Integer wangmarketVersionMin) {
		this.wangmarketVersionMin = wangmarketVersionMin;
	}
	public Integer getAddtime() {
		return addtime;
	}
	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}
	public Integer getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Integer updatetime) {
		this.updatetime = updatetime;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public Short getSupportOssStorage() {
		return supportOssStorage;
	}
	public void setSupportOssStorage(Short supportOssStorage) {
		this.supportOssStorage = supportOssStorage;
	}
	public Short getSupportLocalStorage() {
		return supportLocalStorage;
	}
	public void setSupportLocalStorage(Short supportLocalStorage) {
		this.supportLocalStorage = supportLocalStorage;
	}
	public Short getSupportSls() {
		return supportSls;
	}
	public void setSupportSls(Short supportSls) {
		this.supportSls = supportSls;
	}
	public Short getSupportMysql() {
		return supportMysql;
	}
	public void setSupportMysql(Short supportMysql) {
		this.supportMysql = supportMysql;
	}
	public Short getSupportSqlite() {
		return supportSqlite;
	}
	public void setSupportSqlite(Short supportSqlite) {
		this.supportSqlite = supportSqlite;
	}
	public Short getSupportFreeVersion() {
		return supportFreeVersion;
	}
	public void setSupportFreeVersion(Short supportFreeVersion) {
		this.supportFreeVersion = supportFreeVersion;
	}
	public Short getSupportAuthorizeVersion() {
		return supportAuthorizeVersion;
	}
	public void setSupportAuthorizeVersion(Short supportAuthorizeVersion) {
		this.supportAuthorizeVersion = supportAuthorizeVersion;
	}
	public String getDownUrl() {
		return downUrl;
	}
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}
	public Short getInstallState() {
		return installState;
	}
	public void setInstallState(Short installState) {
		this.installState = installState;
	}
	
	
}
