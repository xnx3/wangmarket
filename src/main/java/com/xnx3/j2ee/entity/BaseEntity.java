package com.xnx3.j2ee.entity;

/**
 * Entity 实体类的父类
 * @author 管雷鸣
 *
 */
public class BaseEntity implements java.io.Serializable {
	/**
	 * 信息状态，是否已删除：未删除，正常状态
	 */
	public final static Short ISDELETE_NORMAL=0;
	
	/**
	 * 信息状态，是否已删除：已删除
	 */
	public final static Short ISDELETE_DELETE=1;
	
	/**
	 * 信息状态，是否已冻结，未冻结，正常
	 */
	public final static Short ISFREEZE_NORMAL = 0;
	
	/**
	 * 信息状态，是否已冻结，已冻结
	 */
	public final static Short ISFREEZE_FREEZE = 1;
}
