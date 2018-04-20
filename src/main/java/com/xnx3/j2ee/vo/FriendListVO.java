package com.xnx3.j2ee.vo;

import java.util.List;
import com.xnx3.j2ee.entity.Friend;

/**
 * 好友列表
 * @author 管雷鸣
 */
public class FriendListVO extends BaseVO {
	private List<Friend> list;	//好友列表
	private int size;	//好友数量
	public List<Friend> getList() {
		return list;
	}
	public void setList(List<Friend> list) {
		this.list = list;
		this.size = list.size();
	}
	public int getSize() {
		return size;
	}
	
}
