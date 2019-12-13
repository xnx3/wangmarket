package com.xnx3.wangmarket.superadmin.vo;

import java.util.List;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.superadmin.bean.Application;

/**
 * 应用插件列表，网市场总管理后台获取所有可用插件时，获取到的插件列表
 * @author 管雷鸣
 *
 */
public class ApplicationListVO extends BaseVO{
	//应用插件列表
	private List<Application> list;
	//分页信息
	private Page page;
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public List<Application> getList() {
		return list;
	}
	public void setList(List<Application> list) {
		this.list = list;
	}
	
}
