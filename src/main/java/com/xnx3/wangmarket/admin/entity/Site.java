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
	
	private Integer id;			//自动编号
	private String name;		//站点名字
	private Integer userid;		//站点所属用户，是哪个用户创建的，对应 User.id
	private Integer addtime;	//站点添加时间，10位linux时间戳
	private Short mShowBanner;	//pc、wap模式使用的，已废弃！
	private String phone;		//联系手机，可以在模版中，通过 {site.phone} 调用
	private String qq;			//联系QQ，可以在模版中，通过 {site.qq} 调用
	private String domain;		//站点自动分配的二级域名
	private Short client;		//客户端类型，现在都是 CMS模式，之前的pc、wap已经没有了
	private String keywords;	//站点的关键字，可以在模版中，通过 {site.keywords} 调用
	private String address;		//站点公司地址，可以在模版中，通过 {site.address} 调用
	private String username;	//联系人姓名
	private String companyName;	//公司名、工作室名字、团体名字, 可以在模版中，通过 {site.companyName} 调用
	private String bindDomain;	//用户自己绑定的域名
	private String columnId;	//栏目id，这里暂时只记录栏目类型的ID，方便生成页面时，生成Nav标签的填充，方便搜索引擎抓取
	private Short state;		//站点状态，1正常；2冻结
	private String templateName;	//自定义模版的模版名字，位于 /template/模版名字，这里的模版单纯修改HTML，没有动态后台
	private Integer expiretime;		//网站过期时间，linux时间戳
	private String attachmentUpdateDate;	//当前附件占用空间大小，最后一次计算的日期，存储格式如 20191224 ，每天登录时都会计算一次 
	private Integer attachmentSize;			//当前附件占用的空间大小，服务器空间，或云存储空间。计算的是 site/$siteid/ 下的空间占用大小，单位是KB  
	private Integer attachmentSizeHave;		//当前用户网站所拥有的空间大小，单位是MB	
	
	/**
	 * @deprecated
	 */
	private Integer templateId;	//pc、wap模式使用的，已废弃！
	/**
	 * @deprecated
	 */
	private Integer aboutUsCid;	//pc、wap模式使用的，已废弃！
	/**
	 * @deprecated
	 */
	private String logo;		//pc、wap模式使用的，已废弃！
	
	public Site() {
		this.state = STATE_NORMAL;
		this.attachmentSizeHave = G.REG_GENERAL_OSS_HAVE;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "m_show_banner")
	public Short getmShowBanner() {
		return mShowBanner;
	}

	public void setmShowBanner(Short mShowBanner) {
		this.mShowBanner = mShowBanner;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	
	@Column(name = "template_id")
	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	@Column(name = "domain")
	public String getDomain() {
		if(domain == null || domain.equals("")){
			domain = id+"";
		}
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@Column(name = "about_us_cid")
	public Integer getAboutUsCid() {
		return aboutUsCid;
	}

	public void setAboutUsCid(Integer aboutUsCid) {
		this.aboutUsCid = aboutUsCid;
	}
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Short getClient() {
		return client;
	}

	public void setClient(Short client) {
		this.client = client;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name = "company_name")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@Column(name = "bind_domain")
	public String getBindDomain() {
		return bindDomain;
	}

	public void setBindDomain(String bindDomain) {
		this.bindDomain = bindDomain;
	}
	@Column(name = "column_id")
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
	
	@Column(name = "state")
	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = Sql.filter(templateName);
	}


	public Integer getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(Integer expiretime) {
		this.expiretime = expiretime;
	}
	
	
	@Column(name = "attachment_update_date", columnDefinition="int(11) comment '当前附件占用空间大小，最后一次计算的日期，存储格式如 20191224 ，每天登录时都会计算一次'")
	public String getAttachmentUpdateDate() {
		return attachmentUpdateDate;
	}

	public void setAttachmentUpdateDate(String attachmentUpdateDate) {
		this.attachmentUpdateDate = attachmentUpdateDate;
	}

	@Column(name = "attachment_size", columnDefinition="int(11) comment '当前附件占用的空间大小，服务器空间，或云存储空间。计算的是 site/siteid/ 下的空间占用大小，单位是KB'")
	public Integer getAttachmentSize() {
		return attachmentSize;
	}

	public void setAttachmentSize(Integer attachmentSize) {
		this.attachmentSize = attachmentSize;
	}
	
	@Column(name = "attachment_size_have", columnDefinition="int(11) comment '当前用户网站所拥有的空间大小，单位是MB'")
	public Integer getAttachmentSizeHave() {
		if(attachmentSizeHave == null){
			this.attachmentSizeHave = 0;
		}
		return attachmentSizeHave;
	}

	public void setAttachmentSizeHave(Integer attachmentSizeHave) {
		this.attachmentSizeHave = attachmentSizeHave;
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