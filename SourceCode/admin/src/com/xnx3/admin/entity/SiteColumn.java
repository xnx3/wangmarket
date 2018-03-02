package com.xnx3.admin.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SiteColumn entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "site_column")
public class SiteColumn implements java.io.Serializable, Cloneable {
	/**
	 * 启用
	 */
	public static final Short USED_ENABLE = 1;
	/**
	 * 不启用
	 */
	public static final Short USED_UNABLE = 0;
	
	/**
	 * 所属类型，1新闻信息
	 */
	public static final Short TYPE_NEWS = 1;
	/**
	 * 图文信息
	 */
	public static final Short TYPE_IMAGENEWS = 2;
	/**
	 * 独立页面
	 */
	public static final Short TYPE_PAGE = 3;
	/**
	 * 4留言板
	 */
	public static final Short TYPE_LEAVEWORD= 4;
	/**
	 * 5超链接
	 */
	public static final Short TYPE_HREF= 5;
	/**
	 * 6纯文字
	 */
	public static final Short TYPE_TEXT= 6;
	
	/**
	 * 客户端类型：PC电脑端
	 */
	public static final Short CLIENT_PC = 1;
	
	/**
	 * 客户端类型：WAP手机端
	 */
	public static final Short CLIENT_WAP = 2;
	
	/**
	 * 若是独立页面，内容的编辑方式：UEDITOR富文本编辑框，使用的是输入模型，默认的便是这个
	 */
	public static final Short EDIT_MODE_INPUT_MODEL = 0;
	
	/**
	 * 若是独立页面，内容的编辑方式：直接编辑模板
	 */
	public static final Short EDIT_MODE_TEMPLATE = 1;
	
	
	// Fields
	private Integer id;
	private String name;
	private String url;
	private String icon;
	private Integer rank;
	private Short used;
	private Integer siteid;
	private Integer userid;
	private Integer parentid;
	private Short type;
//	private Short client;
	private String templatePageListName;
	private String templatePageViewName;
	private String codeName;
	private String parentCodeName;
	private Integer listNum;
	private String inputModelCodeName;
	private Short editMode;	//若是独立页面，内容的编辑方式，是使用富文本编辑框呢，还是直接编辑模板
	
	
	// Constructors

	/** default constructor */
	public SiteColumn() {
//		client = CLIENT_WAP;	//默认手机端
	}

	/** minimal constructor */
	public SiteColumn(Integer id) {
		this.id = id;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "icon")
	public String getIcon() {
		if(this.icon == null){
			return "http://cdn.weiunity.com/res/glyph-icons/world.png";
		}
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "rank")
	public Integer getRank() {
		if(this.rank == null){
			return 0;
		}else{
			return this.rank;
		}
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Column(name = "used")
	public Short getUsed() {
		return this.used;
	}

	public void setUsed(Short used) {
		this.used = used;
	}

	@Column(name = "siteid")
	public Integer getSiteid() {
		return this.siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}
	
	@Column(name = "userid")
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Column(name = "template_page_list_name")
	public String getTemplatePageListName() {
		return templatePageListName;
	}

	public void setTemplatePageListName(String templatePageListName) {
		this.templatePageListName = templatePageListName;
	}
	
	@Column(name = "template_page_view_name")
	public String getTemplatePageViewName() {
		return templatePageViewName;
	}

	public void setTemplatePageViewName(String templatePageViewName) {
		this.templatePageViewName = templatePageViewName;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getParentCodeName() {
		return parentCodeName;
	}

	public void setParentCodeName(String parentCodeName) {
		this.parentCodeName = parentCodeName;
	}

	@Column(name = "list_num")
	public Integer getListNum() {
		return listNum;
	}

	public void setListNum(Integer listNum) {
		this.listNum = listNum;
	}

	@Column(name = "input_model_code_name")
	public String getInputModelCodeName() {
		return inputModelCodeName;
	}

	public void setInputModelCodeName(String inputModelCodeName) {
		this.inputModelCodeName = inputModelCodeName;
	}
	@Column(name = "edit_mode")
	public Short getEditMode() {
		return editMode;
	}

	public void setEditMode(Short editMode) {
		this.editMode = editMode;
	}

	public SiteColumn clone() throws CloneNotSupportedException {
		Object obj = super.clone();
		if(obj != null){
			return (SiteColumn) obj;
		}else{
			return new SiteColumn();
		}
	}

	@Override
	public String toString() {
		return "SiteColumn [id=" + id + ", name=" + name + ", url=" + url
				+ ", icon=" + icon + ", rank=" + rank + ", used=" + used
				+ ", siteid=" + siteid + ", userid=" + userid + ", parentid="
				+ parentid + ", type=" + type + ", templatePageListName="
				+ templatePageListName + ", templatePageViewName="
				+ templatePageViewName + ", codeName=" + codeName
				+ ", parentCodeName=" + parentCodeName + ", listNum=" + listNum
				+ ", inputModelCodeName=" + inputModelCodeName + ", editMode="
				+ editMode + "]";
	}
	
	
}