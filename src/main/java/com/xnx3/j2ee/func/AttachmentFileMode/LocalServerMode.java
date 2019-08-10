package com.xnx3.j2ee.func.AttachmentFileMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.xnx3.BaseVO;
import com.xnx3.FileUtil;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.vo.UploadFileVO;

/**
 * 附件上传之 服务器本身存储，服务器本地存储，附件存储到服务器硬盘上
 * @author 管雷鸣
 *
 */
public class LocalServerMode implements StorageModeInterface{

	@Override
	public void putStringFile(String path, String text, String encode) {
		directoryInit(path);
		try {
			FileUtil.write(AttachmentFile.localFilePath+path, text, encode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public UploadFileVO put(String filePath, File localFile) {
		UploadFileVO vo = new UploadFileVO();
		
		directoryInit(filePath);
		try {
			InputStream localInput = new FileInputStream(localFile);
			//将其保存到服务器磁盘
			vo = put(filePath, localInput);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			vo.setBaseVO(UploadFileVO.FAILURE, "上传出错，要上传的文件不存在！");
		}
		return vo;
	}

	@Override
	public UploadFileVO put(String path, InputStream inputStream) {
		UploadFileVO vo = new UploadFileVO();
		
		directoryInit(path);
		File file = new File(AttachmentFile.localFilePath+path);
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			inputStream.close();
			
			vo.setFileName(file.getName());
			vo.setInfo("success");
			vo.setPath(path);
			vo.setUrl(AttachmentFile.netUrl()+path);
		} catch (IOException e) {
			vo.setBaseVO(BaseVO.FAILURE, e.getMessage());
			e.printStackTrace();
		}
		
		return vo;
	}

	@Override
	public String getTextByPath(String path) {
		String text = FileUtil.read(AttachmentFile.localFilePath+path, FileUtil.UTF8);
		if(text != null && text.length() == 0){
			text = null;
		}
		return text;
	}

	@Override
	public void deleteObject(String filePath) {
		FileUtil.deleteFile(AttachmentFile.localFilePath+filePath);
	}

	@Override
	public void putForUEditor(String filePath, InputStream input, ObjectMetadata meta) {
		put(filePath, input);
	}

	@Override
	public long getDirectorySize(String path) {
		directoryInit(path);
		return FileUtils.sizeOfDirectory(new File(AttachmentFile.localFilePath+path));
	}

	@Override
	public void copyObject(String originalFilePath, String newFilePath) {
		directoryInit(newFilePath);
		FileUtil.copyFile(AttachmentFile.localFilePath + originalFilePath, AttachmentFile.localFilePath + newFilePath);
	}
	
	
	/**
	 * 目录检测，检测是否存在。若不存在，则自动创建目录。适用于使用本地磁盘进行存储
	 * @param path 要检测的目录，相对路径，如 jar/file/  创建到file文件，末尾一定加/     或者jar/file/a.jar创建懂啊file文件
	 */
	public static void directoryInit(String path){
		if(path == null){
			return;
		}
		
		//windows取的路径是\，所以要将\替换为/
		if(path.indexOf("\\") > 1){
			path = StringUtil.replaceAll(path, "\\\\", "/");
		}
		
		if(path.length() - path.lastIndexOf("/") > 1){
			//path最后是带了具体文件名的，把具体文件名过滤掉，只留文件/结尾
			path = path.substring(0, path.lastIndexOf("/")+1);
		}
		
		//如果目录或文件不存在，再进行创建目录的判断
		if(!FileUtil.exists(path)){
			String[] ps = path.split("/");
			
			String xiangdui = "";
			//length-1，/最后面应该就是文件名了，所以要忽略最后一个
			for (int i = 0; i < ps.length; i++) {
				if(ps[i].length() > 0){
					xiangdui = xiangdui + ps[i]+"/";
					if(!FileUtil.exists(AttachmentFile.localFilePath+xiangdui)){
						File file = new File(AttachmentFile.localFilePath+xiangdui);
						file.mkdir();
					}
				}
			}
		}
	}
	
}
