package com.xnx3.wangmarket.plugin.pluginManage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 应用插件
 * @author 管雷鸣
 *
 */
@Entity
@Table(name = "application")
public class Application {
	private String id;	//该插件的唯一标识。如自定义表单插件，唯一标识便是 formManage 。注意不能与其他插件重名
	private String menuTitle;	//在网站管理后台中，功能插件下，显示的菜单项的标题文字，也就是插件的名字
	private Short applyToCMS;	//是否在CMS模式网站管理后台的功能插件中显示， 1：是, 不填、0则是不显示
	private Short applyToPC;	//是否在电脑(pc)模式网站管理后台的功能插件中显示， 1：是, 不填、0则是不显示
	private Short applyToWAP;	//是否在手机(wap)模式网站管理后台的功能插件中显示， 1：是, 不填、0则是不显示
	private Short applyToAgency;	//是否在代理后台的功能插件中显示， 1：是, 不填、0则是不显示
	private Short applyToSuperAdmin;	//是否在总管理后台的功能插件中显示， 1：是, 不填、0则是不显示
	private String intro;	//该插件的简介说明,char(200)
	private Integer version;	//当前插件的版本号 ， 如  1.0  则是 100000000; 1.2.1 则是 100200100; 2.13.3则是 200130300
	private Integer wangmarketVersionMin;	//支持的网市场最低版本，规则也是同上，如4.7.1则是 400700100
	private Integer addtime;	//应用添加时间
	private Integer updatetime;	//应用最后改动时间
	private String authorName;	//作者名字
	private Short supportOssStorage;	//若wangmarket使用的OSS，是否支持该插件运行。 1支持，0或者其他是不支持
	private Short supportLocalStorage;	//若wangmarket使用的服务器本身进行的文件存储，是否支持该插件运行。 1支持，0或者其他是不支持
	private Short supportSls;	//若wangmarket使用的SLS，是否支持该插件运行。 1支持，0或者其他是不支持
	private Short supportMysql;	//若wangmarket使用的Mysql数据库，是否支持该插件运行。 1支持，0或者其他是不支持
	private Short supportSqlite;	//若wangmarket使用的Sqlite数据库，是否支持该插件运行。 1支持，0或者其他是不支持
	private Short supportFreeVersion;	//若wangmarket使用的是免费开源版本，是否支持该插件运行。 1支持，0或者其他是不支持
	private Short supportAuthorizeVersion;	//若wangmarket使用的是授权版本，是否支持该插件运行。 1支持，0或者其他是不支持
	private String downUrl; //插件下载的url
	private Short installState;	//插件是否被安装 0：未安装  1：已安装
	
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "menu_title")
	public String getMenuTitle() {
		return menuTitle;
	}
	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}
	
	@Column(name = "apply_to_cms")
	public Short getApplyToCMS() {
		return applyToCMS;
	}
	public void setApplyToCMS(Short applyToCMS) {
		this.applyToCMS = applyToCMS;
	}
	
	@Column(name = "apply_to_pc")
	public Short getApplyToPC() {
		return applyToPC;
	}
	public void setApplyToPC(Short applyToPC) {
		this.applyToPC = applyToPC;
	}
	
	@Column(name = "apply_to_wap")
	public Short getApplyToWAP() {
		return applyToWAP;
	}
	public void setApplyToWAP(Short applyToWAP) {
		this.applyToWAP = applyToWAP;
	}
	
	@Column(name = "apply_to_agency")
	public Short getApplyToAgency() {
		return applyToAgency;
	}
	public void setApplyToAgency(Short applyToAgency) {
		this.applyToAgency = applyToAgency;
	}
	
	@Column(name = "apply_to_superadmin")
	public Short getApplyToSuperAdmin() {
		return applyToSuperAdmin;
	}
	public void setApplyToSuperAdmin(Short applyToSuperAdmin) {
		this.applyToSuperAdmin = applyToSuperAdmin;
	}
	
	@Column(name = "intro")
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	@Column(name = "version")
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Column(name = "wangmarket_version_min")
	public Integer getWangmarketVersionMin() {
		return wangmarketVersionMin;
	}
	public void setWangmarketVersionMin(Integer wangmarketVersionMin) {
		this.wangmarketVersionMin = wangmarketVersionMin;
	}
	
	@Column(name = "addtime")
	public Integer getAddtime() {
		return addtime;
	}
	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}
	
	@Column(name = "updatetime")
	public Integer getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Integer updatetime) {
		this.updatetime = updatetime;
	}
	
	@Column(name = "author_name")
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	@Column(name = "support_oss_storage")
	public Short getSupportOssStorage() {
		return supportOssStorage;
	}
	public void setSupportOssStorage(Short supportOssStorage) {
		this.supportOssStorage = supportOssStorage;
	}
	
	@Column(name = "support_local_storage")
	public Short getSupportLocalStorage() {
		return supportLocalStorage;
	}
	public void setSupportLocalStorage(Short supportLocalStorage) {
		this.supportLocalStorage = supportLocalStorage;
	}
	
	@Column(name = "support_sls")
	public Short getSupportSls() {
		return supportSls;
	}
	public void setSupportSls(Short supportSls) {
		this.supportSls = supportSls;
	}
	
	@Column(name = "support_mysql")
	public Short getSupportMysql() {
		return supportMysql;
	}
	public void setSupportMysql(Short supportMysql) {
		this.supportMysql = supportMysql;
	}
	
	@Column(name = "support_sqlite")
	public Short getSupportSqlite() {
		return supportSqlite;
	}
	public void setSupportSqlite(Short supportSqlite) {
		this.supportSqlite = supportSqlite;
	}
	
	@Column(name = "support_free_version")
	public Short getSupportFreeVersion() {
		return supportFreeVersion;
	}
	public void setSupportFreeVersion(Short supportFreeVersion) {
		this.supportFreeVersion = supportFreeVersion;
	}
	
	@Column(name = "support_authorize_version")
	public Short getSupportAuthorizeVersion() {
		return supportAuthorizeVersion;
	}
	public void setSupportAuthorizeVersion(Short supportAuthorizeVersion) {
		this.supportAuthorizeVersion = supportAuthorizeVersion;
	}
	
	@Column(name = "down_url")
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

	@Override
	public String toString() {
		return "Application [id=" + id + ", menuTitle=" + menuTitle + ", applyToCMS=" + applyToCMS + ", applyToPC="
				+ applyToPC + ", applyToWAP=" + applyToWAP + ", applyToAgency=" + applyToAgency + ", applyToSuperAdmin="
				+ applyToSuperAdmin + ", intro=" + intro + ", version=" + version + ", wangmarketVersionMin="
				+ wangmarketVersionMin + ", addtime=" + addtime + ", updatetime=" + updatetime + ", authorName="
				+ authorName + ", supportOssStorage=" + supportOssStorage + ", supportLocalStorage="
				+ supportLocalStorage + ", supportSls=" + supportSls + ", supportMysql=" + supportMysql
				+ ", supportSqlite=" + supportSqlite + ", supportFreeVersion=" + supportFreeVersion
				+ ", supportAuthorizeVersion=" + supportAuthorizeVersion + ", downUrl=" + downUrl
				+ "]";
	}
	
	
	
}
