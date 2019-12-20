package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模版。
 * 只要是网站中使用过的模版，都会进入此处进行记录。无论是云端模版、用户自己上传的模版、还是私有模版
 * @author 管雷鸣
 */
@Entity
@Table(name = "template")
public class Template implements java.io.Serializable {
	//是公共的
	public static final Short ISCOMMON_YES = 1;
	public static final Short ISCOMMON_NO = 0;
	
	//资源引用，使用云端模版库的资源，如css、js
	public static final String RESOURCE_IMPORT_CLOUD = "cloud";
	//资源引用，使用本地的资源文件
	public static final String RESOURCE_IMPORT_PRIVATE = "private";
	
	private Integer id;			//自动编号
	private String name;			//模版的名字，编码，唯一，限制50个字符以内
	private Integer addtime;		//模版添加时间
	private Integer userid;		//此模版所属的用户，user.id。如果此模版是用户的私有模版，也就是 iscommon=0 时，这里存储导入此模版的用户的id
	private String remark;		//模版的简介，备注说明，限制200字以内
	private String previewUrl;	//模版预览网址，示例网站网址，绝对路径，
	private String previewPic;	//模版预览图的网址，preview.jpg 图片的网址
	private int type;			//模版所属分类，如广告、科技、生物、医疗等
	private String companyname;	//模版开发者公司名字。如果没有公司，则填写个人姓名。限制50字符以内
	private String username;	//模版开发人员的名字，姓名，限制10个字符以内
	private String siteurl;		//模版开发者官方网站、企业官网。如果是企业，这里是企业官网的网址，格式如： http://www.leimingyun.com  ，如果是个人，则填写个人网站即可
	private Short terminalMobile;	//网站模版是否支持手机端, 1支持，0不支持
	private Short terminalPc;		//网站模版是否支持PC端, 1支持，0不支持
	private Short terminalIpad;		//网站模版是否支持平板电脑, 1支持，0不支持
	private Short terminalDisplay;	//网站模版是否支持展示机, 1支持，0不支持
	private Short iscommon;		//是否是公共的模版 1是公共的模版， 0不是公共的，私有的，是用户自己开通网站导入的
	private int rank;			//公共模版的排序，数字越小越靠前。
	private String wscsoDownUrl;	//wscso模版文件下载的url地址
	private String zipDownUrl;		//zip模版素材包文件下载的url地址
	
	/**
	 * js、css等资源引用方式。 cloud：使用云端模版库； private:使用私有模版库，也就是本地的
	 * v4.8更改，已废弃
	 * 再预留两个版本，之后删除
	 * @deprecated
	 */
	private String resourceImport;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getAddtime() {
		return addtime;
	}
	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "preview_url")
	public String getPreviewUrl() {
		return previewUrl;
	}
	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSiteurl() {
		return siteurl;
	}
	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}
	@Column(name = "terminal_mobile")
	public Short getTerminalMobile() {
		return terminalMobile;
	}
	public void setTerminalMobile(Short terminalMobile) {
		this.terminalMobile = terminalMobile;
	}
	@Column(name = "terminal_ipad")
	public Short getTerminalIpad() {
		return terminalIpad;
	}
	public void setTerminalIpad(Short terminalIpad) {
		this.terminalIpad = terminalIpad;
	}
	public Short getIscommon() {
		return iscommon;
	}
	public void setIscommon(Short iscommon) {
		this.iscommon = iscommon;
	}
	@Column(name = "terminal_pc")
	public Short getTerminalPc() {
		return terminalPc;
	}
	public void setTerminalPc(Short terminalPc) {
		this.terminalPc = terminalPc;
	}
	@Column(name = "terminal_display")
	public Short getTerminalDisplay() {
		return terminalDisplay;
	}
	public void setTerminalDisplay(Short terminalDisplay) {
		this.terminalDisplay = terminalDisplay;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	@Column(name = "wscso_down_url")
	public String getWscsoDownUrl() {
		return wscsoDownUrl;
	}
	public void setWscsoDownUrl(String wscsoDownUrl) {
		this.wscsoDownUrl = wscsoDownUrl;
	}
	@Column(name = "zip_down_url")
	public String getZipDownUrl() {
		return zipDownUrl;
	}
	public void setZipDownUrl(String zipDownUrl) {
		this.zipDownUrl = zipDownUrl;
	}
	@Column(name = "preview_pic")
	public String getPreviewPic() {
		return previewPic;
	}
	public void setPreviewPic(String previewPic) {
		this.previewPic = previewPic;
	}
	
	@Column(name = "resource_import")
	public String getResourceImport() {
		return resourceImport;
	}
	public void setResourceImport(String resourceImport) {
		this.resourceImport = resourceImport;
	}
	
	@Override
	public String toString() {
		return "Template [id=" + id + ", name=" + name + ", addtime=" + addtime + ", userid=" + userid + ", remark="
				+ remark + ", previewUrl=" + previewUrl + ", previewPic=" + previewPic + ", type=" + type
				+ ", companyname=" + companyname + ", username=" + username + ", siteurl=" + siteurl
				+ ", terminalMobile=" + terminalMobile + ", terminalPc=" + terminalPc + ", terminalIpad=" + terminalIpad
				+ ", terminalDisplay=" + terminalDisplay + ", iscommon=" + iscommon + ", rank=" + rank
				+ ", wscsoDownUrl=" + wscsoDownUrl + ", zipDownUrl=" + zipDownUrl + ", resourceImport=" + resourceImport
				+ "]";
	}
	
}