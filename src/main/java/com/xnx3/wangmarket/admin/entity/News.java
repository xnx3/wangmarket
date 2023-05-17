package com.xnx3.wangmarket.admin.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 网站的文章
 * @author 管雷鸣
 */
@Entity
@Table(name = "news", indexes={@Index(name="suoyin_index",columnList="userid,addtime,type,status,cid,siteid,html_name")})
public class News implements java.io.Serializable {
	
	/**
	 * 正常显示
	 */
	public final static Short STATUS_NORMAL = 1;
	/**
	 * 隐藏不显示
	 */
	public final static Short STATUS_HIDDEN = 2;
	/**
	 * 类型：新闻
	 */
	public final static Short TYPE_NEWS = 1;
	/**
	 * 类型：图文
	 * @deprecated 已废弃，这种归入 TYPE_NEWS
	 */
	public final static Short TYPE_IMAGENEWS = 2;
	/**
	 * 类型：独立页面
	 */
	public final static Short TYPE_PAGE = 3;

	/**
	 * 是否是合法的，合法
	 */
	public final static short LEGITIMATE_OK = 1;
	/**
	 * 是否是合法的，不合法或者涉嫌
	 */
	public final static short LEGITIMATE_NO = 0;
	/**
	 * 文章置顶：是 ， 为1
	 */
	public final static short TOP_YES = 1;
	/**
	 * 文章置顶：否 ， 为0
	 */
	public final static short TOP_NO = 0;
	
	// Fields
	private Integer id;
	private Integer userid;		//对应user.id，是哪个用户发表的
	private Integer addtime;	//发布时间
	private String title;
	private String titlepic;	//头图
	private String intro;		//简介,从内容正文里自动剪切出开始的160个汉字
	private Integer opposenum;	//反对的总数量
	private Integer readnum;	//阅读的总数量
	private Short type;			//1新闻；2图文；3独立页面
	private Short status;		//1：正常显示；2：隐藏不显示
	private Integer commentnum;	//评论的总数量
	private Integer cid;			//所属栏目id，对应site_column.id
	private Integer siteid;		//所属站点，对应site.id
	private Short legitimate;	//是否是合法的，1是，0不是，涉嫌
	private String htmlName;			//要生成的html文件的名字。比如这里的值为 abc ，那么生成整站时，该页面的url访问路径便是 abc.html  
	
	//以下两个为预留字段，可以通过输入模型进行扩展
	private String reserve1;
	private String reserve2;
	
	//v6.2增加
	private Short top;	//文章置顶，0不置顶，1置顶。默认为0
	
	public News() {
		this.legitimate = LEGITIMATE_OK;
		this.reserve1 = "";
		this.reserve2 = "";
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "userid", columnDefinition="int(11) COMMENT '对应user.id，是哪个用户发表的' default '0'")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "addtime", columnDefinition="int(11) COMMENT '发布时间'")
	public Integer getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "title", columnDefinition="char(60) COMMENT '' default ''")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "titlepic", columnDefinition="char(200) COMMENT '头图' default ''")
	public String getTitlepic() {
		return this.titlepic;
	}

	public void setTitlepic(String titlepic) {
		this.titlepic = titlepic;
	}

	@Column(name = "intro", columnDefinition="char(160) COMMENT '简介,从内容正文里自动剪切出开始的160个汉字' default ''")
	public String getIntro() {
		return this.intro;
	}

	public void setIntro(String intro) {
		if(intro == null){
			intro = "";
		}else{
			if(intro.length()>160){
				intro = intro.substring(0, 160);
			}
			//过滤特殊字符
			if(intro.indexOf("{") > -1){
				intro = intro.replaceAll("\\{", "");
			}
			if(intro.indexOf("}") > -1){
				intro = intro.replaceAll("\\}", "");
			}
		}
		this.intro = intro;
	}

	@Column(name = "opposenum", columnDefinition="int(11) COMMENT '反对的总数量' default '0'")
	public Integer getOpposenum() {
		return this.opposenum;
	}

	public void setOpposenum(Integer opposenum) {
		this.opposenum = opposenum;
	}

	@Column(name = "readnum", columnDefinition="int(11) COMMENT '阅读的总数量' default '0'")
	public Integer getReadnum() {
		return this.readnum;
	}

	public void setReadnum(Integer readnum) {
		this.readnum = readnum;
	}

	@Column(name = "type", columnDefinition="tinyint(2) COMMENT '1新闻；2图文；3独立页面' default '0'")
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Column(name = "status", columnDefinition="tinyint(2) COMMENT '1：正常显示；2：隐藏不显示' default '0'")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "commentnum", columnDefinition="int(11) COMMENT '评论的总数量' default '0'")
	public Integer getCommentnum() {
		return this.commentnum;
	}

	public void setCommentnum(Integer commentnum) {
		this.commentnum = commentnum;
	}
	
	@Column(name = "cid", columnDefinition="int(11) COMMENT '所属栏目id，对应site_column.id' default '0'")
	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}
	
	@Column(name = "siteid", columnDefinition="int(11) COMMENT '所属站点，对应site.id' default '0'")
	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}
	
	@Column(name = "legitimate", columnDefinition="tinyint(2) COMMENT '是否是合法的，1是，0不是，涉嫌' default '0'")
	public Short getLegitimate() {
		return legitimate;
	}

	public void setLegitimate(Short legitimate) {
		this.legitimate = legitimate;
	}

	@Column(name = "reserve1", columnDefinition="char(10) COMMENT '预留字段，用户可使用输入模型来进行扩展' default ''")
	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		if(reserve1 != null && reserve1.length()>10){
			reserve1 = reserve1.substring(0, 10);
		}
		this.reserve1 = reserve1;
	}

	@Column(name = "reserve2", columnDefinition="char(10) COMMENT '预留字段，用户可使用输入模型来进行扩展' default ''")
	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		if(reserve2 != null && reserve2.length()>10){
			reserve2 = reserve2.substring(0, 10);
		}
		this.reserve2 = reserve2;
	}
	
	@Column(name = "html_name", columnDefinition="char(50) COMMENT '要生成的html文件的名字。比如这里的值为 abc ，那么生成整站时，该页面的url访问路径便是 abc.html  '")
	public String getHtmlName() {
		return htmlName;
	}

	public void setHtmlName(String htmlName) {
		this.htmlName = htmlName;
	}
	
	@Column(name = "top", columnDefinition="tinyint(2) COMMENT '文章置顶，0不置顶，1置顶。默认为0' default '0'")
	public Short getTop() {
		if(this.top == null) {
			this.top = News.TOP_NO;
		}
		return top;
	}

	public void setTop(Short top) {
		this.top = top;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", userid=" + userid + ", addtime=" + addtime
				+ ", title=" + title + ", titlepic=" + titlepic + ", intro="
				+ intro + ", opposenum=" + opposenum + ", readnum=" + readnum
				+ ", type=" + type + ", status=" + status + ", commentnum="
				+ commentnum + ", cid=" + cid + ", siteid=" + siteid
				+ ", legitimate=" + legitimate + "]";
	}
	
}