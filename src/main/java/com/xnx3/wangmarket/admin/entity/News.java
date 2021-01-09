package com.xnx3.wangmarket.admin.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * News entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "news", indexes={@Index(name="suoyin_index",columnList="userid,addtime,type,status,cid,siteid")})
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
	
	
	// Fields

	private Integer id;
	private Integer userid;
	private Integer addtime;
	private String title;
	private String titlepic;
	private String intro;
	private Integer opposenum;
	private Integer readnum;
	private Short type;
	private Short status;
	private Integer commentnum;
	private Integer cid;
	private Integer siteid;
	private Short legitimate;
	
	//以下两个为预留字段，可以通过输入模型进行扩展
	private String reserve1;
	private String reserve2;
	
	public News() {
		this.legitimate = LEGITIMATE_OK;
		this.reserve1 = "";
		this.reserve2 = "";
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(name = "userid")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "addtime")
	public Integer getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "titlepic")
	public String getTitlepic() {
		return this.titlepic;
	}

	public void setTitlepic(String titlepic) {
		this.titlepic = titlepic;
	}

	@Column(name = "intro")
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

	@Column(name = "opposenum")
	public Integer getOpposenum() {
		return this.opposenum;
	}

	public void setOpposenum(Integer opposenum) {
		this.opposenum = opposenum;
	}

	@Column(name = "readnum")
	public Integer getReadnum() {
		return this.readnum;
	}

	public void setReadnum(Integer readnum) {
		this.readnum = readnum;
	}

	@Column(name = "type")
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Column(name = "status")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "commentnum")
	public Integer getCommentnum() {
		return this.commentnum;
	}

	public void setCommentnum(Integer commentnum) {
		this.commentnum = commentnum;
	}
	@Column(name = "cid")
	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}
	
	@Column(name = "siteid")
	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}
	@Column(name = "legitimate")
	public Short getLegitimate() {
		return legitimate;
	}

	public void setLegitimate(Short legitimate) {
		this.legitimate = legitimate;
	}

	@Column(name = "reserve1")
	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		if(reserve1 != null && reserve1.length()>10){
			reserve1 = reserve1.substring(0, 10);
		}
		this.reserve1 = reserve1;
	}

	@Column(name = "reserve2")
	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		if(reserve2 != null && reserve2.length()>10){
			reserve2 = reserve2.substring(0, 10);
		}
		this.reserve2 = reserve2;
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