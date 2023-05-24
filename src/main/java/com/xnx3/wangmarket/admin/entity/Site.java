package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.wangmarket.admin.G;

/**
 * 网站站点信息表
 * @author 管雷鸣
 */
@Entity
@Table(name = "site")
public class Site implements java.io.Serializable {
	
	/**
	 * 隐藏手机端首页的Banner
	 */
	public final static Short MSHOWBANNER_HIDDEN = 0;
	/**
	 * 显示手机端首页的Banner
	 */
	public final static Short MSHOWBANNER_SHOW = 1;
	
	/**
	 * 客户端类型：PC电脑端
	 */
	public static final Short CLIENT_PC = 1;
	/**
	 * 客户端类型：WAP手机端
	 */
	public static final Short CLIENT_WAP = 2;
	/**
	 * 高级自定义模版CMS
	 */
	public static final Short CLIENT_CMS = 3;
	
	/**
	 * 站点状态，1:正常
	 */
	public static final Short STATE_NORMAL = 1;
	/**
	 * 站点状态，2:冻结、暂停
	 */
	public static final Short STATE_FREEZE = 2;
	
	/**
	 * 网站html存储方式：默认方式，采用AttachmentUtil存储
	 */
	public static final String GENERATE_HTML_STORAGE_TYPE_DEFAULT = "default";
	/**
	 * 网站html存储方式：obs
	 */
	public static final String GENERATE_HTML_STORAGE_TYPE_OBS = "obs";
	/**
	 * 网站html存储方式：ftp
	 */
	public static final String GENERATE_HTML_STORAGE_TYPE_FTP = "ftp";
	/**
	 * 网站html存储方式：sftp
	 */
	public static final String GENERATE_HTML_STORAGE_TYPE_SFTP = "sftp";
	/**
	 * 网站html存储方式：local，本地服务器存储，也就是不管是否AttachmentUtil 配置了OBS、OSS，这个网站存储的html都会使用LocalServerMode存储
	 */
	public static final String GENERATE_HTML_STORAGE_TYPE_LOCAL = "local";
	
	private Integer id;						//自动编号
	private String name;					//站点名字
	private Integer userid;					//站点所属用户，是哪个用户创建的，对应 User.id
	private Integer addtime;				//站点添加时间，10位linux时间戳
	private Short mShowBanner;				//pc、wap模式使用的，已废弃！
	private String phone;					//联系手机，可以在模版中，通过 {site.phone} 调用
	private String qq;						//联系QQ，可以在模版中，通过 {site.qq} 调用
	private String domain;					//站点自动分配的二级域名
	private Short client;					//客户端类型，现在都是 CMS模式，之前的pc、wap已经没有了
	private String keywords;				//站点的关键字，可以在模版中，通过 {site.keywords} 调用
	private String address;					//站点公司地址，可以在模版中，通过 {site.address} 调用
	private String username;				//联系人姓名
	private String companyName;				//公司名、工作室名字、团体名字, 可以在模版中，通过 {site.companyName} 调用
	private String bindDomain;				//用户自己绑定的域名
	private String columnId;				//栏目id，这里暂时只记录栏目类型的ID，方便生成页面时，生成Nav标签的填充，方便搜索引擎抓取
	private Short state;					//站点状态，1正常；2冻结
	private String templateName;			//自定义模版的模版名字，位于 /template/模版名字，这里的模版单纯修改HTML，没有动态后台
	private Integer expiretime;				//网站过期时间，linux时间戳
	private Integer attachmentUpdateDate;	//当前附件占用空间大小，最后一次计算的日期，存储格式如 20191224 ，每天登录时都会计算一次
	private Integer attachmentSize;			//当前附件占用的空间大小，服务器空间，或云存储空间。计算的是 site/$siteid/ 下的空间占用大小，单位是KB  
	private Integer attachmentSizeHave;		//当前用户网站所拥有的空间大小，单位是MB	
	
	private String generateHtmlStorageType;	//生成html页面的方式，存储方式， obs:obs buckname存储，  ftp:ftp方式存储，  空或者default或者其他则是默认的AttachmentUtil 方式存储
	
	//v6.1 增加
	private Integer newsSize;			//当前网站的文章条数
	private Integer newsSizeHave;				//当前网站的文章允许上传多少条。默认是1000（独立栏目也是一个文章）
	
	/**
	 * @deprecated
	 */
	private Integer templateId;				//pc、wap模式使用的，已废弃！
	/**
	 * @deprecated
	 */
	private Integer aboutUsCid;				//pc、wap模式使用的，已废弃！
	/**
	 * @deprecated
	 */
	private String logo;					//pc、wap模式使用的，已废弃！
	
	private String remark;					//站点备注，代理商给网站的备注，方便代理商记录这个网站干嘛的
	
	public Site() {
		this.state = STATE_NORMAL;
		this.attachmentSizeHave = G.REG_GENERAL_OSS_HAVE;
		this.remark = "";
		this.generateHtmlStorageType = GENERATE_HTML_STORAGE_TYPE_DEFAULT;
		this.newsSizeHave = 1000;
		this.newsSize = 0;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		if(this.id == null){
			return 0;
		}
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", columnDefinition="char(40) COMMENT '站点名字' default ''")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "userid", columnDefinition="int(11) COMMENT '站点所属用户，是哪个用户创建的，对应 User.id' default '0'")
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "addtime", columnDefinition="int(11) COMMENT '站点添加时间，10位linux时间戳'")
	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "m_show_banner", columnDefinition="tinyint(1) COMMENT 'pc、wap模式使用的，已废弃！' default '0'")
	public Short getmShowBanner() {
		return mShowBanner;
	}

	public void setmShowBanner(Short mShowBanner) {
		this.mShowBanner = mShowBanner;
	}

	@Column(name = "phone", columnDefinition="char(12) COMMENT '联系手机，可以在模版中，通过 {site.phone} 调用' default ''")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "qq", columnDefinition="char(12) COMMENT '联系QQ，可以在模版中，通过 {site.qq} 调用' default ''")
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	
	@Column(name = "template_id", columnDefinition="int(11) COMMENT 'pc、wap模式使用的，已废弃！' default '0'")
	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	
	@Column(name = "domain", columnDefinition="char(30) COMMENT '站点自动分配的二级域名' default ''")
	public String getDomain() {
		if(domain == null || domain.equals("")){
			domain = id+"";
		}
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@Column(name = "about_us_cid", columnDefinition="int(11) COMMENT 'pc、wap模式使用的，已废弃！' default '0'")
	public Integer getAboutUsCid() {
		return aboutUsCid;
	}

	public void setAboutUsCid(Integer aboutUsCid) {
		this.aboutUsCid = aboutUsCid;
	}
	
	@Column(name = "logo", columnDefinition="char(80) COMMENT 'pc、wap模式使用的，已废弃！' default ''")
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Column(name = "client", columnDefinition="tinyint(2) COMMENT '客户端类型，现在都是 CMS模式，之前的pc、wap已经没有了' default '0'")
	public Short getClient() {
		return client;
	}

	public void setClient(Short client) {
		this.client = client;
	}

	@Column(name = "keywords", columnDefinition="char(38) COMMENT '站点的关键字，可以在模版中，通过 {site.keywords} 调用' default ''")
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Column(name = "address", columnDefinition="char(80) COMMENT '站点公司地址，可以在模版中，通过 {site.address} 调用' default ''")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "username", columnDefinition="char(10) COMMENT '联系人姓名' default ''")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "company_name", columnDefinition="char(30) COMMENT '公司名、工作室名字、团体名字, 可以在模版中，通过 {site.companyName} 调用' default ''")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Column(name = "bind_domain", columnDefinition="char(30) COMMENT '用户自己绑定的域名' default ''")
	public String getBindDomain() {
		return bindDomain;
	}

	public void setBindDomain(String bindDomain) {
		this.bindDomain = bindDomain;
	}
	
	@Column(name = "column_id", columnDefinition="char(80) COMMENT '栏目id，这里暂时只记录栏目类型的ID，方便生成页面时，生成Nav标签的填充，方便搜索引擎抓取' default ''")
	public String getColumnId() {
		return columnId;
	}

	/**
	 * 
	 * @param columnId 传入如 345,
	 */
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	
	@Column(name = "state", columnDefinition="tinyint(2) COMMENT '站点状态，1正常；2冻结' default '0'")
	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	@Column(name = "template_name", columnDefinition="char(20) COMMENT '自定义模版的模版名字，位于 /template/模版名字，这里的模版单纯修改HTML，没有动态后台' default ''")
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = Sql.filter(templateName);
	}

	@Column(name = "expiretime", columnDefinition="int(11) COMMENT '网站过期时间，linux时间戳'")
	public Integer getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(Integer expiretime) {
		this.expiretime = expiretime;
	}
	
	@Column(name = "attachment_update_date", columnDefinition="int(11) COMMENT '当前附件占用空间大小，最后一次计算的日期，存储格式如 20191224 ，每天登录时都会计算一次'")
	public Integer getAttachmentUpdateDate() {
		return attachmentUpdateDate;
	}

	public void setAttachmentUpdateDate(Integer attachmentUpdateDate) {
		this.attachmentUpdateDate = attachmentUpdateDate;
	}

	@Column(name = "attachment_size", columnDefinition="int(11) COMMENT '当前附件占用的空间大小，服务器空间，或云存储空间。计算的是 site/siteid/ 下的空间占用大小，单位是KB' default '0'")
	public Integer getAttachmentSize() {
		if(this.attachmentSize == null) {
			this.attachmentSize = 0;
		}
		return attachmentSize;
	}

	public void setAttachmentSize(Integer attachmentSize) {
		this.attachmentSize = attachmentSize;
	}
	
	@Column(name = "attachment_size_have", columnDefinition="int(11) COMMENT '当前用户网站所拥有的空间大小，单位是MB' default '0'")
	public Integer getAttachmentSizeHave() {
		if(attachmentSizeHave == null){
			this.attachmentSizeHave = 0;
		}
		return attachmentSizeHave;
	}

	public void setAttachmentSizeHave(Integer attachmentSizeHave) {
		this.attachmentSizeHave = attachmentSizeHave;
	}

	@Column(name = "remark", columnDefinition="char(255) COMMENT '站点备注，代理商给网站的备注，方便代理商记录这个网站干嘛的' default ''")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "generate_html_storage_type", columnDefinition="char(20) COMMENT '生成html页面的方式，存储方式， obs:obs buckname存储，  ftp:ftp方式存储，  空或者default或者其他则是默认的AttachmentUtil 方式存储' default ''")
	public String getGenerateHtmlStorageType() {
		if(generateHtmlStorageType == null || generateHtmlStorageType.length() == 0) {
			generateHtmlStorageType = GENERATE_HTML_STORAGE_TYPE_DEFAULT;
		}
		return generateHtmlStorageType;
	}

	public void setGenerateHtmlStorageType(String generateHtmlStorageType) {
		this.generateHtmlStorageType = generateHtmlStorageType;
	}
	
	@Column(name = "news_size_have", columnDefinition="int(11) COMMENT '当前网站的文章允许上传多少条。默认是1000（独立栏目也是一个文章）'")
	public Integer getNewsSizeHave() {
		if(this.newsSizeHave == null) {
			this.newsSizeHave = 1000;
		}
		return newsSizeHave;
	}

	public void setNewsSizeHave(Integer newsSizeHave) {
		this.newsSizeHave = newsSizeHave;
	}
	
	@Column(name = "news_size", columnDefinition="int(11) COMMENT '当前网站的文章条数'")
	public Integer getNewsSize() {
		if(newsSize == null) {
			newsSize = 0;
		}
		return newsSize;
	}

	public void setNewsSize(Integer newsSize) {
		this.newsSize = newsSize;
	}

	@Override
	public String toString() {
		return "Site [id=" + id + ", name=" + name + ", userid=" + userid
				+ ", addtime=" + addtime + ", mShowBanner=" + mShowBanner
				+ ", phone=" + phone + ", qq=" + qq + ", templateId="
				+ templateId + ", domain=" + domain + ", aboutUsCid="
				+ aboutUsCid + ", logo=" + logo + ", client=" + client
				+ ", keywords=" + keywords + ", address=" + address
				+ ", username=" + username + ", companyName=" + companyName
				+ ", bindDomain=" + bindDomain + ", columnId=" + columnId
				+ ", state=" + state + ", templateName=" + templateName
				+ ", expiretime=" + expiretime + "]";
	}
	
}