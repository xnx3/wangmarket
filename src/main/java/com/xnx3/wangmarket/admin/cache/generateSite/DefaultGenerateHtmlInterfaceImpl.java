package com.xnx3.wangmarket.admin.cache.generateSite;

import org.springframework.beans.BeanUtils;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;
import cn.zvo.fileupload.framework.springboot.FileUpload;
import cn.zvo.fileupload.framework.springboot.FileUploadUtil;

/**
 * 默认的 GenerateHtmlInterface 接口的实现
 * @author 管雷鸣
 *
 */
public class DefaultGenerateHtmlInterfaceImpl implements GenerateHtmlInterface{
	private Site site;
	private FileUpload fileUpload;
	
	public DefaultGenerateHtmlInterfaceImpl(Site site) {
		this.site = site;
		this.fileUpload = new FileUpload();
		BeanUtils.copyProperties(FileUploadUtil.fileupload, this.fileUpload);
		this.fileUpload.setAllowUploadSuffix("html|txt|xml"); //自动生成整站允许上传的文件后缀
	}
	
	@Override
	public BaseVO putStringFile(String text, String path) {
		AttachmentUtil.uploadStringFile("site/"+site.getId()+"/"+path, text);
		this.fileUpload.uploadString("site/"+site.getId()+"/"+path, text);
		return BaseVO.success();
	}
}
