package com.xnx3.wangmarket.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模版页面
 * @author 管雷鸣
 */
@Entity
@Table(name = "template_page")
public class TemplatePage implements java.io.Serializable {
	/**
	 * 模版页面类型，0:其他
	 */
	public final static Short TYPE_ELSE = 0;
	/**
	 * 模版页面类型，1:首页
	 */
	public final static Short TYPE_INDEX = 1;
	/**
	 * 模版页面类型，2:文章列表,文字列表
	 */
	public final static Short TYPE_NEWS_LIST = 2;
	/**
	 * 模版页面类型，3:文章详情
	 */
	public final static Short TYPE_NEWS_VIEW = 3;
//	/**
//	 * 模版页面类型，4:图片列表，图文列表，统一归属于文章列表、文章详情
//	 */
//	public final static Short TYPE_NEWSIMAGE_LIST = 4;
//	/**
//	 * 模版页面类型，5:图片详情，图文详情
//	 */
//	public final static Short TYPE_NEWSIMAGE_VIEW = 5;
	/**
	 * 模版页面类型，6:单页面如关于我们，废弃，并入详情页模版
	 */
	public final static Short TYPE_ALONEPAGE = 6;
	
	/**
	 * 当前模版页面的编辑模式，1:智能模式， 这里，判断只要不是2，那都是智能模式，以兼容以前的版本
	 */
	public final static Short EDIT_MODE_VISUAL = 1;
	/**
	 * 当前模版页面的编辑模式，2:代码模式
	 */
	public final static Short EDIT_MODE_CODE = 2;

	private Integer id;			//自动编号
	private String name;		//当前模版页面的名字，（还原模板时会使用到）
	private Short editMode;		//当前模版页面的编辑模式，1:智能模式； 2:代码模式。  这里，判断只要不是2，那都是智能模式，以兼容以前的版本
	private Integer userid;		//所属用户的id
	private Short type;			//当前模版页的类型，类型；0其他；1首页；2新闻列表,文字列表；3新闻详情；6单页面如关于我们
	private String templateName;//所属的模版名字
	private int siteid;			//当前模版页面，是那个站点在使用。一个站点拥有一个自己的模版，克隆而来
	private String remark;		//备注
	
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
		if(name.length() > 20){
			name = name.substring(0, 20);
		}
		this.name = name;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public int getSiteid() {
		return siteid;
	}
	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Short getEditMode() {
		return editMode;
	}
	public void setEditMode(Short editMode) {
		this.editMode = editMode;
	}
	
}