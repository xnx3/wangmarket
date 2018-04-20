package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import com.xnx3.j2ee.util.Sql;

/**
 * site entitiy
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
	
	private Integer id;
	private String name;
	private Integer userid;
	private Integer addtime;
	private Short mShowBanner;
	private String phone;
	private String qq;
	private Integer templateId;
	private String domain;
	private Integer aboutUsCid;
	private String logo;
	private Short client;
	private String keywords;
	private String address;
	
	private String username;	//联系人姓名
	private String companyName;		//公司名、工作室名字、团体名字
	private String bindDomain;	//用户自己绑定的域名
	private String columnId;	//栏目id，这里暂时只记录栏目类型的ID，方便生成页面时，生成Nav标签的填充，方便搜索引擎抓取
	private Short state;	//站点状态，1正常；2冻结
	private String templateName;	//自定义模版的模版名字，位于 /template/模版名字，这里的模版单纯修改HTML，没有动态后台
	private Integer expiretime;
	
	private Short useKefu;	//是否开启在线客服功能。0不开启，默认；  1开启
	
	public Site() {
		this.state = STATE_NORMAL;
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

	public Short getUseKefu() {
		return useKefu;
	}

	public void setUseKefu(Short useKefu) {
		this.useKefu = useKefu;
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