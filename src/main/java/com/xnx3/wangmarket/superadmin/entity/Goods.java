package com.xnx3.wangmarket.superadmin.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 积分兑换（user.money）的商品
 * @deprecated
 */
@Entity
@Table(name = "goods")
public class Goods implements java.io.Serializable {
	
	private Integer id;
	private Integer money;
	private String explain;
	private String deadline;
	private String type;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "money", columnDefinition = "int(11) COMMENT '' default '0'")
	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	@Column(name = "explain", columnDefinition = "char(100) COMMENT '' default ''")
	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	@Column(name = "deadline", columnDefinition = "char(10) COMMENT '' default ''")
	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	@Column(name = "type", columnDefinition = "char(5) COMMENT '' default ''")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Goods [id=" + id + ", money=" + money + ", explain=" + explain + ", deadline=" + deadline + ", type="
				+ type + "]";
	}

}