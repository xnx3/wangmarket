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
	// Fields

	/**
	 * 默认的，头部通用的图，比如模版3、5、6的首页跟所有内页、模版7的内页，都是用的这个
	 */
	public final static short TYPE_DEFAULT_PAGEBANNER = 1;
	/**
	 * 只有首页的Banner才会使用的，比如模版7的首页第一屏的大Banner图。
	 */
	public final static short TYPE_INDEXBANNER = 2;
	
	private Integer id;
	private String url;
	private Integer addtime;
	private Short isshow;
	private Integer rank;
	private Integer siteid;
	private Integer userid;
	private String image;
	private Short type;

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
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "url", length = 120)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "addtime")
	public Integer getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "isshow")
	public Short getIsshow() {
		return this.isshow;
	}

	public void setIsshow(Short isshow) {
		this.isshow = isshow;
	}

	@Column(name = "rank")
	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

}