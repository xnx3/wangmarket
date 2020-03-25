package com.xnx3.wangmarket.admin.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 网站栏目
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
	 * 所属类型，1新闻信息 (CMS 已废弃此状态)
	 * @deprecated
	 */
	public static final Short TYPE_NEWS = 1;
	/**
	 * 图文信息 (CMS 已废弃此状态)
	 * @deprecated
	 */
	public static final Short TYPE_IMAGENEWS = 2;
	/**
	 * 3独立页面
	 * @deprecated
	 */
	public static final Short TYPE_PAGE = 3;
	/**
	 * 4留言板 (CMS 已废弃此状态)
	 * @deprecated
	 */
	public static final Short TYPE_LEAVEWORD= 4;
	/**
	 * 5超链接 (CMS 已废弃此状态)
	 * @deprecated
	 */
	public static final Short TYPE_HREF= 5;
	/**
	 * 6纯文字 (CMS 已废弃此状态)
	 * @deprecated
	 */
	public static final Short TYPE_TEXT= 6;
	/**
	 * 所属类型，7信息列表 (CMS 模式使用，v4.6版本增加，代替新闻信息、图文信息两种状态)
	 */
	public static final Short TYPE_LIST= 7;
	/**
	 * 所属类型，8独立页面 (CMS 模式使用，v4.6版本增加，代替之前的3独立页面的状态。以实现4.6之前版本向4.6版本的过度)
	 */
	public static final Short TYPE_ALONEPAGE= 8;
	
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
	
	/**
	 * 栏目内信息的列表排序规则，按照发布时间倒序，发布时间越晚，排序越靠前
	 * 默认便是此种的，v4.4版本增加，4.4版本以前只有这一种排序
	 */
	public static final Short LIST_RANK_ADDTIME_DESC = 1;
	
	/**
	 * 栏目内信息的列表排序规则，按照发布时间正序，发布时间越早，排序越靠前
	 */
	public static final Short LIST_RANK_ADDTIME_ASC = 2;
	
	private Integer id;		//id
	private String name;	//栏目名字
	private String url;		//已废弃
	private String icon;	//本栏目的图片、图标，可在模版中使用{siteColumn.icon}进行调用此图以显示
	private Integer rank;	//栏目间的排序
	private Short used;		//已废弃
	private Integer siteid;	//栏目所属的站点id，对应 site.id
	private Integer userid;	//栏目所属的用户id，对应 user.id，这个已逐渐废弃，由siteid代替。
	private Integer parentid;	//已废弃，用 parentCodeName 取代
	private Short type;		//栏目类型
//	private Short client;
	private String templatePageListName;	//列表页模板对应的模板页面的名字
	private String templatePageViewName;	//详情页模板对应的模板页面的名字
	private String codeName;		//栏目diamante
	private String parentCodeName;	//父栏目diamante
	private Integer listNum;		//列表中每页显示多少条
	private String inputModelCodeName;	//绑定的输入模型的代码
	private Short editMode;	//若是独立页面，内容的编辑方式，是使用富文本编辑框呢，还是直接编辑模板
	private Short listRank;	//列表排序，当前栏目若是信息列表，信息列表的排序规则
	
	private Short editUseTitlepic;		//内容管理中，添加内容时，封面图片的输入。 0隐藏，1显示，若是null，则是兼容v4.6以前的版本，需要根据栏目类型type进行判断
	private Short editUseIntro;			//内容管理中，添加内容时，文章简介的输入 0隐藏，1显示，若是null，则是兼容v4.6以前的版本，需要根据栏目类型type进行判断
	private Short editUseText;			//内容管理中，添加内容时，文章详情的输入 0隐藏，1显示，若是null，则是兼容v4.6以前的版本，需要根据栏目类型type进行判断
	private Short editUseExtendPhotos;	//内容管理中，添加内容时，图集的输入 0隐藏，1显示，若是null，则是兼容v4.6以前的版本，需要根据栏目类型type进行判断
	
	//v4.7
	private Short useGenerateView;		//是否生成内容页面。取值1生成；0不生成，如果为null则默认为1，默认是生成。set时使用 SiteColumn.USED_ENABLE 赋值
	
	//v4.10
	private Short templateCodeColumnUsed;		//是否在模版调用中显示（调取子栏目列表）。在模板中，使用动态栏目调用代码调取栏目列表时，是否会调取到此栏目。例如顶级栏目名为 手机 ，其下有三个子栏目，分别为小米、魅族、中兴，如果这个栏目是“魅族”，那么设置此处为隐藏后，调取“手机”这个栏目下的所有子栏目列表时，就只有小米、中兴。默认1显示，0为不显示。如果为null也是显示
//	private Short templateCodeNewsUsed;			//是否在模版调用中显示（调取文章列表）。在模板中，使用动态栏目调用代码调取某个父栏目下，所有子栏目的内容列表时，是否也将此栏目的内容一并调取出来。默认1显示，0为不显示。如果为null也是显示
	private Short adminNewsUsed;				//是否在内容管理中显示这个栏目。默认1显示，0为不显示。如果为null也是显示
	//v5.1
	private String keywords;	//SEO关键字，限制50字以内
	private String description;		//SEO描述，限制200字以内
	
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

	@Column(name = "list_rank")
	public Short getListRank() {
		return listRank;
	}

	public void setListRank(Short listRank) {
		this.listRank = listRank;
	}

	@Column(name = "edit_use_titlepic")
	public Short getEditUseTitlepic() {
		return editUseTitlepic;
	}

	public void setEditUseTitlepic(Short editUseTitlepic) {
		this.editUseTitlepic = editUseTitlepic;
	}

	@Column(name = "edit_use_intro")
	public Short getEditUseIntro() {
		return editUseIntro;
	}

	public void setEditUseIntro(Short editUseIntro) {
		this.editUseIntro = editUseIntro;
	}

	@Column(name = "edit_use_text")
	public Short getEditUseText() {
		return editUseText;
	}

	public void setEditUseText(Short editUseText) {
		this.editUseText = editUseText;
	}

	@Column(name = "edit_use_extend_photos")
	public Short getEditUseExtendPhotos() {
		return editUseExtendPhotos;
	}

	public void setEditUseExtendPhotos(Short editUseExtendPhotos) {
		this.editUseExtendPhotos = editUseExtendPhotos;
	}

	@Column(name = "use_generate_view")
	public Short getUseGenerateView() {
		return useGenerateView;
	}

	public void setUseGenerateView(Short useGenerateView) {
		this.useGenerateView = useGenerateView;
	}

	@Column(name = "template_code_column_used", columnDefinition="int(2) comment '是否在模版调用中显示（调取子栏目列表）。1使用，0不使用'")
	public Short getTemplateCodeColumnUsed() {
		if(templateCodeColumnUsed == null){
			return 1;
		}
		return templateCodeColumnUsed;
	}

	public void setTemplateCodeColumnUsed(Short templateCodeColumnUsed) {
		this.templateCodeColumnUsed = templateCodeColumnUsed;
	}
//	
//	@Column(name = "template_code_news_used", columnDefinition="int(2) comment '是否在模版调用中显示（调取文章列表）。1使用，0不使用'")
//	public Short getTemplateCodeNewsUsed() {
//		return templateCodeNewsUsed;
//	}
//
//	public void setTemplateCodeNewsUsed(Short templateCodeNewsUsed) {
//		this.templateCodeNewsUsed = templateCodeNewsUsed;
//	}
	
	@Column(name = "admin_news_used", columnDefinition="int(2) comment '是否在内容管理中显示这个栏目。'")
	public Short getAdminNewsUsed() {
		if(adminNewsUsed == null){
			return 1;
		}
		return adminNewsUsed;
	}

	public void setAdminNewsUsed(Short adminNewsUsed) {
		this.adminNewsUsed = adminNewsUsed;
	}
	
	@Column(name = "keywords", columnDefinition="char(50) comment 'SEO keywords'")
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	@Column(name = "description", columnDefinition="char(200) comment 'SEO description'")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "SiteColumn [id=" + id + ", name=" + name + ", url=" + url + ", icon=" + icon + ", rank=" + rank
				+ ", used=" + used + ", siteid=" + siteid + ", userid=" + userid + ", parentid=" + parentid + ", type="
				+ type + ", templatePageListName=" + templatePageListName + ", templatePageViewName="
				+ templatePageViewName + ", codeName=" + codeName + ", parentCodeName=" + parentCodeName + ", listNum="
				+ listNum + ", inputModelCodeName=" + inputModelCodeName + ", editMode=" + editMode + ", listRank="
				+ listRank + ", editUseTitlepic=" + editUseTitlepic + ", editUseIntro=" + editUseIntro
				+ ", editUseText=" + editUseText + ", editUseExtendPhotos=" + editUseExtendPhotos + ", useGenerateView="
				+ useGenerateView + ", templateCodeColumnUsed=" + templateCodeColumnUsed + ", adminNewsUsed="
				+ adminNewsUsed + ", keywords=" + keywords + ", description=" + description + "]";
	}

	
}