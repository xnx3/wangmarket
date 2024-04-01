package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SiteCarousel entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "carousel")
public class Carousel implements java.io.Serializable {
	
	/**
	 * 显示
	 */
	public final static short ISSHOW_SHOW = 1;
	/**
	 * 隐藏
	 */
	public final static short ISSHOW_HIDDEN = 2;

	/**
	 * 默认的，头部通用的图，比如模版3、5、6的首页跟所有内页、模版7的内页，都是用的这个
	 */
	public final static short TYPE_DEFAULT_PAGEBANNER = 1;
	/**
	 * 只有首页的Banner才会使用的，比如模版7的首页第一屏的大Banner图。
	 */
	public final static short TYPE_INDEXBANNER = 2;
	
	// Fields
	private Integer id;
	private String url;			//点击跳转的目标url
	private Integer addtime;	//添加时间
	private Short isshow;		//是否显示，1为显示，0为不显示
	private Integer rank;		//排序，数小越靠前
	private Integer siteid;		//轮播图属于哪个站点，对应site.id
	private Integer userid;		//轮播图属于哪个用户建立的，对应user.id
	private String image;		//轮播图的url，分两种，一种只是文件名，如asd.png  另一种为绝对路径
	private Short type;			//类型，默认1:内页通用的头部图(有的模版首页也用)；2:只有首页顶部才会使用的图

	// Constructors
	/** default constructor */
	public Carousel() {
		this.type = TYPE_DEFAULT_PAGEBANNER;
	}

	/** full constructor */
	public Carousel(String url, Integer addtime, Short isshow, Integer rank) {
		this.url = url;
		this.addtime = addtime;
		this.isshow = isshow;
		this.rank = rank;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id",columnDefinition="int(11) COMMENT '自动编号'", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "url", columnDefinition="char(120) COMMENT '点击跳转的目标url' default ''")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "addtime", columnDefinition="int(11) COMMENT '添加时间'")
	public Integer getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "isshow", columnDefinition="tinyint(2) COMMENT '是否显示，1为显示，0为不显示' default '0'")
	public Short getIsshow() {
		return this.isshow;
	}

	public void setIsshow(Short isshow) {
		this.isshow = isshow;
	}

	@Column(name = "rank", columnDefinition="int(11) COMMENT '排序，数小越靠前' default '0'")
	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Column(name = "siteid", columnDefinition="int(11) COMMENT '轮播图属于哪个站点，对应site.id' default '0'")
	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	@Column(name = "userid", columnDefinition="int(11) COMMENT '轮播图属于哪个用户建立的，对应user.id' default '0'")
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "image", columnDefinition="char(120) COMMENT '轮播图的url，分两种，一种只是文件名，如asd.png  另一种为绝对路径' default ''")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "type", columnDefinition="tinyint(2) COMMENT '类型，默认1:内页通用的头部图(有的模版首页也用)；2:只有首页顶部才会使用的图' default '0'")
	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Carousel [id=" + id + ", url=" + url + ", addtime=" + addtime + ", isshow=" + isshow + ", rank=" + rank
				+ ", siteid=" + siteid + ", userid=" + userid + ", image=" + image + ", type=" + type + "]";
	}

}