package com.xnx3.wangmarket.admin.vo;

import java.util.List;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.vo.bean.OSSFile;


/**
 * 站点文件列表
 * @author 管雷鸣
 */
public class SiteFileListVO extends BaseVO {
	private List<OSSFile> list;
	private Site site;

	public List<OSSFile> getList() {
		return list;
	}

	public void setList(List<OSSFile> list) {
		this.list = list;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
	
}
