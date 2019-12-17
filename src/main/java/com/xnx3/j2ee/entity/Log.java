package com.xnx3.j2ee.entity;

import static javax.persistence.GenerationType.IDENTITY;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 日志
 * @author 管雷鸣
 * @deprecated 已使用阿里云日志服务
 */
@Entity
@Table(name = "log")
public class Log implements java.io.Serializable {
	/**
	 * 取type的值，根据name获取数字
	 */
	public static Map<String, Integer> typeMap = new HashMap<String, Integer>();	
	/**
	 * 根据type的值，取type的说明描述
	 */
	public static Map<Integer, String> typeDescriptionMap = new HashMap<Integer, String>();
	
	private Integer id;
	private Integer userid;
	private Integer type;
	private Integer goalid;	//目标id，如操作的帖子，则为帖子id，如果为消息，则为消息id
	private String value;
	private Integer addtime;	//时间戳
	private short isdelete;	//此条纪录是否已删除，1删除，0未删除正常状态
	
	public Log() {
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
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getGoalid() {
		return goalid;
	}
	public void setGoalid(Integer goalid) {
		this.goalid = goalid;
	}
	public String getValue() {
		return value;
	}
	
	/**
	 * 自动截取前20个字符
	 * @param value 值
	 */
	public void setValue(String value) {
		if(value!=null){
			if(value.length()>15){
				value = value.substring(0, 15);
			}
		}
		
		this.value = value;
	}
	
	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	public short getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(short isdelete) {
		this.isdelete = isdelete;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", userid=" + userid + ", type=" + type + ", goalid=" + goalid + ", value=" + value
				+ ", addtime=" + addtime + ", isdelete=" + isdelete + "]";
	}

}
