package com.xnx3.wangmarket.admin.cache.generateSite;

import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;

/**
 * 默认的 GenerateHtmlInterface 接口的实现
 * @author 管雷鸣
 *
 */
public class DefaultGenerateHtmlInterfaceImpl implements GenerateHtmlInterface{
	private Site site;
	
	public DefaultGenerateHtmlInterfaceImpl(Site site) {
		this.site = site;
	}
	
	@Override
	public BaseVO putStringFile(String text, String path) {
		AttachmentUtil.uploadStringFile("site/"+site.getId()+"/"+path, text);
		return BaseVO.success();
	}
}
