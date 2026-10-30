package com.xnx3.wangmarket.admin.cache.generateSite;

import java.io.IOException;
import org.springframework.beans.BeanUtils;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.util.SitePageUtil;
import cn.zvo.fileupload.framework.springboot.FileUpload;
import cn.zvo.fileupload.framework.springboot.FileUploadUtil;
import cn.zvo.fileupload.storage.local.LocalStorage;
import cn.zvo.fileupload.vo.UploadFileVO;

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
		if(site == null || site.getId() == null || site.getId() <= 0){
			return BaseVO.failure("页面写出失败：当前站点不存在或站点编号无效");
		}
		String pathError = SitePageUtil.relativePathError(path);
		if(pathError != null){
			return BaseVO.failure(pathError);
		}
		String storagePath = "site/" + site.getId() + "/" + path;
		try {
			if(fileUpload.getStorage() instanceof LocalStorage){
				SitePageUtil.checkLocalPath(((LocalStorage) fileUpload.getStorage()).getLocalFilePath(), storagePath);
			}
		} catch (IOException e) {
			return BaseVO.failure(e.getMessage());
		}
		UploadFileVO uploaded = fileUpload.uploadString(storagePath, text);
		if(uploaded == null){
			return BaseVO.failure("上传接口未返回执行结果（null）");
		}
		if(uploaded.getResult() != UploadFileVO.SUCCESS){
			String reason = uploaded.getInfo();
			if(reason == null || reason.trim().length() == 0){
				reason = "上传接口返回状态 " + uploaded.getResult() + "，但未提供失败原因";
			}
			return BaseVO.failure(reason);
		}
		return BaseVO.success();
	}
}
