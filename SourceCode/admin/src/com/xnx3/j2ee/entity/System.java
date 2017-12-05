package com.xnx3.j2ee.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xnx3.j2ee.Global;

/**
 * Friend entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "system")
public class System implements java.io.Serializable {
	/**
	 * 此项会再后台系统管理列表显示
	 */
	public final static Short LISTSHOW_SHOW=1;
	/**
	 * 此项不会再后台系统管理列表显示
	 */
	public final static Short LISTSHOW_UNSHOW=0;

	private Integer id;
	private String description;
	private String name;
	private String value;
	private Integer lasttime;	//最后修改时间
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 参数名,程序内调用
	 *  {@link Global#get(String)}
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * 参数名,程序内调用
	 *  {@link Global#get(String)}
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 值
	 * @return
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 值
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 说明描述,给后台操作人员看的
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 说明描述,给后台操作人员看的
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getLasttime() {
		return lasttime;
	}
	public void setLasttime(Integer lasttime) {
		this.lasttime = lasttime;
	}
	
}