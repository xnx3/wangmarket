package com.xnx3.j2ee.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xnx3.j2ee.Global;

/**
 * 系统管理-系统变量的参数，key-value的形态，tomcat在启动时，系统会首先自动加载这个数据表中的数据到Java的Map中进行长久缓存。取这个数据表中的数值时，会从map中取
 * @author 管雷鸣
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

	private Integer id;			//自动编号
	private String description;	//说明描述，备注的作用
	private String name;		//参数名,程序内调用，相当于map的key
	private String value;		//参数值,程序内调用，相当于map的value
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
	@Override
	public String toString() {
		return "System [id=" + id + ", description=" + description + ", name=" + name + ", value=" + value
				+ ", lasttime=" + lasttime + "]";
	}
	
}