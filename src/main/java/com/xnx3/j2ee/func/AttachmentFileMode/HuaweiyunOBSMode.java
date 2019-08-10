package com.xnx3.j2ee.func.AttachmentFileMode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.oss.model.OSSObject;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.net.OSSUtil;
import com.xnx3.net.ossbean.PutResult;

/**
 * 附件上传之 华为云 OBS 
 * @author 管雷鸣
 *
 */
public class HuaweiyunOBSMode implements StorageModeInterface{

	@Override
	public void putStringFile(String path, String text, String encode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UploadFileVO put(String filePath, File localFile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UploadFileVO put(String path, InputStream inputStream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTextByPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteObject(String filePath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putForUEditor(String filePath, InputStream input, ObjectMetadata meta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getDirectorySize(String path) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void copyObject(String originalFilePath, String newFilePath) {
		// TODO Auto-generated method stub
		
	}

}
