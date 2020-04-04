package com.xnx3.j2ee.util.AttachmentMode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.AttachmentMode.bean.SubFileBean;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.net.OSSUtil;
import com.xnx3.net.ossbean.PutResult;

/**
 * 附件上传之 阿里云 OSS
 * @author 管雷鸣
 *
 */
public class AliyunOSSMode implements StorageModeInterface{

	@Override
	public void putStringFile(String path, String text, String encode) {
		OSSUtil.putStringFile(path, text, encode);
	}

	@Override
	public UploadFileVO put(String filePath, File localFile) {
		return PutResultToUploadFileVO(OSSUtil.put(filePath, localFile.getPath()));
	}

	@Override
	public UploadFileVO put(String path, InputStream inputStream) {
		return PutResultToUploadFileVO(OSSUtil.put(path, inputStream));
	}

	@Override
	public String getTextByPath(String path) {
		OSSObject ossObject = null;
		try {
			ossObject = OSSUtil.getOSSClient().getObject(OSSUtil.bucketName, path);
		} catch (com.aliyun.oss.OSSException e) {
			//path不存在
		}
		
		if(ossObject == null){
			return null;
		}else{
			try {
				return IOUtils.toString(ossObject.getObjectContent(), "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public void deleteObject(String filePath) {
		OSSUtil.getOSSClient().deleteObject(OSSUtil.bucketName, filePath);
	}

	@Override
	public void putForUEditor(String filePath, InputStream input, ObjectMetadata meta) {
		com.aliyun.oss.model.ObjectMetadata m = new com.aliyun.oss.model.ObjectMetadata();
		m.setContentLength(meta.getContentLength());
		m.setUserMetadata(meta.getUserMetadata());
		OSSUtil.getOSSClient().putObject(OSSUtil.bucketName, filePath, input, m);
	}

	@Override
	public long getDirectorySize(String path) {
		return OSSUtil.getFolderSize(path);
	}

	@Override
	public void copyObject(String originalFilePath, String newFilePath) {
		OSSUtil.getOSSClient().copyObject(OSSUtil.bucketName, originalFilePath, OSSUtil.bucketName, newFilePath);
	}

	
	/**
	 * 将阿里云OSS的上传结果 {@link PutResult}转化为 {@link UploadFileVO}结果
	 * @param pr 阿里云OSS的上传结果 {@link PutResult}
	 * @return {@link UploadFileVO}
	 */
	private static UploadFileVO PutResultToUploadFileVO(PutResult pr){
		UploadFileVO vo = new UploadFileVO();
		if(pr == null || pr.getFileName() == null || pr.getUrl() == null){
			vo.setBaseVO(UploadFileVO.FAILURE, "上传失败！");
		}else{
			vo.setFileName(pr.getFileName());
			vo.setInfo("success");
			vo.setPath(pr.getPath());
			vo.setUrl(pr.getUrl());
		}
		return vo;
	}

	@Override
	public List<SubFileBean> getSubFileList(String path) {
		List<SubFileBean> list = new ArrayList<SubFileBean>();
		if(path == null || path.length() == 0){
			return list;
		}
		
		List<OSSObjectSummary> subList = OSSUtil.getFolderObjectList(path);
		for (int i = 0; i < subList.size(); i++) {
			OSSObjectSummary sum = subList.get(i);
			SubFileBean bean = new SubFileBean();
			bean.setSize(sum.getSize());
			bean.setPath(sum.getKey());
			bean.setLastModified(sum.getLastModified().getTime());
			list.add(bean);
		}
		
		return list;
	}
}
