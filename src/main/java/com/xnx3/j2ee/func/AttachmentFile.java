package com.xnx3.j2ee.func;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.xnx3.BaseVO;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.file.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.media.ImageUtil;
import com.xnx3.net.OSSUtil;
import com.xnx3.net.ossbean.PutResult;

/**
 * 附件的操作，如OSS、或服务器本地文件
 * 如果时localFile ，则需要设置 AttachmentFile.netUrl
 * @author 管雷鸣
 */
public class AttachmentFile {
	
	private static String maxFileSize;	//application.properties 中配置的，比如3MB
	private static int maxFileSizeKB = -1;	//最大上传限制，单位：KB，在getMaxFileSizeKB()获取
	
	public static String mode;	//当前文件附件存储使用的模式，用的阿里云oss，还是服务器本身磁盘进行存储
	public static final String MODE_ALIYUN_OSS = "aliyunOSS";		//阿里云OSS模式存储
	public static final String MODE_LOCAL_FILE = "localFile";		//服务器本身磁盘进行附件存储
	
	//文件路径，文件所在。oss则为OSSUtil.url， localFile则是存储到磁盘，访问时自然就是主域名
	public static String netUrl = null;
	
	//如果附件保存在当前服务器上，则保存的路径是哪个
	public static String localFilePath = "";	
	
	static{
		mode = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("attachmentFile.mode");
		if(mode == null){
			mode = MODE_ALIYUN_OSS;
		}
		
		localFilePath = Global.getProjectPath();
	}
	
	/**
	 * 获取当前允许上传的文件的最大大小
	 * @return 如 3MB 、 400KB 等
	 */
	public static String getMaxFileSize(){
		if(maxFileSize == null){
			try {
				maxFileSize = ApplicationProperties.getProperty("spring.servlet.multipart.max-file-size");
				Log.debug("加载 application.properties 的文件上传限制："+maxFileSize);
			} catch (Exception e) {
				maxFileSize = "3MB";
				Log.error("出错---强制将大小设置为3MB");
				e.printStackTrace();
			}
		}
		return maxFileSize;
	}
	
	/**
	 * 获取当前限制的上传文件最大的大小限制。单位是KB
	 * @return 单位KB
	 */
	public static int getMaxFileSizeKB(){
		if(maxFileSizeKB == -1){
			//未初始化，那么进行初始化
			maxFileSize = getMaxFileSize();
			
			if(maxFileSize.indexOf("KB") > 0){
				//使用KB单位
				maxFileSizeKB = Lang.stringToInt(maxFileSize.replace("KB", "").trim(), 0);
				if(maxFileSizeKB == 0){
					Log.error("application.properties --> spring.servlet.multipart.max-file-size use KB， but !!! string to int failure !");
				}
			}else if (maxFileSize.indexOf("MB") > 0) {
				//使用MB
				maxFileSizeKB = Lang.stringToInt(maxFileSize.replace("MB", "").trim(), 0) * 1024;
				if(maxFileSizeKB == 0){
					Log.error("application.properties --> spring.servlet.multipart.max-file-size use MB， but !!! string to int failure !");
				}
			}else if (maxFileSize.indexOf("GB") > 0) {
				//使用 GB
				maxFileSizeKB = Lang.stringToInt(maxFileSize.replace("GB", "").trim(), 0) * 1024 * 1024;
				if(maxFileSizeKB == 0){
					Log.error("application.properties --> spring.servlet.multipart.max-file-size use GB， but !!! string to int failure !");
				}
			}else{
				//没有找到合适单位，报错
				Log.error("application.properties --> spring.servlet.multipart.max-file-size not find unit，your are KB ? MB ? GB ? Please use one of them");
			}
		}
		return maxFileSizeKB;
	}
	
	/**
	 * 判断当前文件附件存储使用的是哪种模式，存储到什么位置
	 * @param mode 存储的代码，可直接传入如 {@link #MODE_ALIYUN_OSS}
	 * @return 是否使用
	 * 			<ul>
	 * 				<li>true ： 是此种模式</li>
	 * 				<li>false ： 不是此种模式</li>
	 * 			</ul>
	 */
	public static boolean isMode(String mode){
		if(mode == null || AttachmentFile.mode == null){
			return false;
		}
		return AttachmentFile.mode.equals(mode);
	}
	
	
	/**
	 * 获取附件访问的url地址
	 * @return 返回如 http://res.weiunity.com/   若找不到，则返回null
	 */
	public static String netUrl(){
		if(netUrl == null){
			if(isMode(MODE_ALIYUN_OSS)){
				netUrl = OSSUtil.url;
			}else if(isMode(MODE_LOCAL_FILE)){
				//如果有当前网站的域名，那么返回域名，格式如"http://www.xnx3.com/" 。如果没有，则返回"/"
				netUrl = Global.get("ATTACHMENT_FILE_URL");
				if(netUrl != null && netUrl.length() == 0){
					netUrl = null;
				}
			}else{
				//未发现什么类型。此种情况是不应该存在的
				Log.error("当前系统未发现附件网址，请执行install/index.do进行安装，选择附件存储方式");
			}
		}
		return netUrl;
	}
	
	/**
	 * 设置当前的netUrl
	 * @param url
	 */
	public static void setNetUrl(String url){
		url = netUrl;
	}
	
	/**
	 * 给出文本内容，写出文件。写出UTF－8编码
	 * @param path 写出的路径,上传后的文件所在的目录＋文件名，如 "jar/file/xnx3.html"
	 * @param text 文本内容
	 */
	public static void putStringFile(String path, String text){
		putStringFile(path, text, FileUtil.UTF8);
	}
	
	/**
	 * 给出文本内容，写出文件。写出UTF－8编码
	 * @param path 写出的路径,上传后的文件所在的目录＋文件名，如 "jar/file/xnx3.html"
	 * @param text 文本内容
	 * @param encode 编码格式，可传入 {@link FileUtil#GBK}、{@link FileUtil#UTF8}
	 */
	public static void putStringFile(String path, String text, String encode){
		if(isMode(MODE_ALIYUN_OSS)){
			OSSUtil.putStringFile(path, text, encode);
		}else if(isMode(MODE_LOCAL_FILE)){
			directoryInit(path);
			try {
				FileUtil.write(localFilePath+path, text, encode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 判断要上传的文件是否超出大小限制，若超出大小限制，返回出错原因
	 * @param file 要上传的文件，判断其大小是否超过系统指定的最大限制
	 * @return 若超出大小，则返回result:Failure ，info为出错原因
	 */
	public static UploadFileVO verifyFileMaxLength(File file){
		UploadFileVO vo = new UploadFileVO();
		if(file != null){
			//文件的KB长度
			int lengthKB = (int) Math.ceil(file.length()/1024);
			vo = verifyFileMaxLength(lengthKB);
		}
		return vo;
	}
	
	/**
	 * 判断要上传的文件是否超出大小限制，若超出大小限制，返回出错原因
	 * @param lengthKB 要上传的文件的大小，判断其大小是否超过系统指定的最大限制，单位是KB
	 * @return 若超出大小，则返回result:Failure ，info为出错原因
	 */
	public static UploadFileVO verifyFileMaxLength(int lengthKB){
		UploadFileVO vo = new UploadFileVO();
		if(getMaxFileSizeKB() > 0 && lengthKB > getMaxFileSizeKB()){
			vo.setBaseVO(BaseVO.FAILURE, "文件大小超出限制！上传大小在 "+maxFileSize+" 以内");
			return vo;
		}
		return vo;
	}
	
	/**
	 * 上传本地文件
	 * @param filePath 上传后的文件所在的目录、路径，如 "jar/file/"
	 * @param localPath 本地要上传的文件的绝对路径，如 "/jar_file/iw.jar"
	 * @return {@link PutResult} 若失败，返回null
	 */
	public static UploadFileVO put(String filePath, String localPath){
		UploadFileVO vo = new UploadFileVO();
		
		File localFile = new File(localPath);
		vo = verifyFileMaxLength(localFile);
		if(vo.getResult() - UploadFileVO.FAILURE == 0){
			return vo;
		}
		
		if(isMode(MODE_ALIYUN_OSS)){
			PutResult pr = OSSUtil.put(filePath, localPath);
			vo = PutResultToUploadFileVO(pr);
		}else if(isMode(MODE_LOCAL_FILE)){
			directoryInit(filePath);
			try {
				InputStream localInput = new FileInputStream(localFile);
				//将其保存到服务器磁盘
				vo = put(filePath, localInput);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				vo.setBaseVO(UploadFileVO.FAILURE, "上传出错，要上传的文件不存在！");
			}
		}
		
		return vo;
	}
	
	/**
	 * 上传文件。上传后的文件名固定
	 * @param path 上传到哪里，包含上传后的文件名，如"image/head/123.jpg"
	 * @param inputStream 文件
	 * @return {@link UploadFileVO}
	 */
	public static UploadFileVO put(String path,InputStream inputStream){
		UploadFileVO vo = new UploadFileVO();
		
		//判断文件大小是否超出最大限制的大小
		int lengthKB = 0;
		try {
			lengthKB = (int) Math.ceil(inputStream.available()/1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
		vo = verifyFileMaxLength(lengthKB);
		if(vo.getResult() - UploadFileVO.FAILURE == 0){
			return vo;
		}
		vo.setSize(lengthKB);
		
		if(isMode(MODE_ALIYUN_OSS)){
			PutResult pr = OSSUtil.put(path, inputStream);
			vo = PutResultToUploadFileVO(pr);
		}else if(isMode(MODE_LOCAL_FILE)){
			directoryInit(path);
			File file = new File(localFilePath+path);
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
				e.printStackTrace();
			}
		}
		
		return vo;
	}
	
	/**
	 * 传入一个路径，得到其源代码(文本)
	 * @param path 要获取的文本内容的路径，如  site/123/index.html
	 * @return 返回其文本内容。若找不到，或出错，则返回 null
	 */
	public static String getTextByPath(String path){
		if(isMode(MODE_ALIYUN_OSS)){
			OSSObject ossObject = null; 
			try {
				ossObject = OSSUtil.getOSSClient().getObject(OSSUtil.bucketName, path);
			} catch (OSSException e) {
			} catch (ClientException e) {
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
		}else if(isMode(MODE_LOCAL_FILE)){
			String text = FileUtil.read(localFilePath+path, FileUtil.UTF8);
			if(text != null && text.length() == 0){
				text = null;
			}
			return text;
		}
		
		return null;
	}
	
	/**
	 * 删除文件
	 * @param filePath 文件所在的路径，如 "jar/file/xnx3.jpg"
	 */
	public static void deleteObject(String filePath){
		if(isMode(MODE_ALIYUN_OSS)){
			OSSUtil.getOSSClient().deleteObject(OSSUtil.bucketName, filePath);
		}else if(isMode(MODE_LOCAL_FILE)){
			FileUtil.deleteFile(localFilePath+filePath);
		}
		
	}
	
	/**
	 * 复制文件
	 * @param originalFilePath 原本文件所在的路径(相对路径，非绝对路径，操作的是当前附件文件目录下)
	 * @param newFilePath 复制的文件所在的路径，所放的路径。(相对路径，非绝对路径，操作的是当前附件文件目录下)
	 */
	public static void copyObject(String originalFilePath, String newFilePath){
		if(isMode(MODE_ALIYUN_OSS)){
			OSSUtil.getOSSClient().copyObject(OSSUtil.bucketName, originalFilePath, OSSUtil.bucketName, newFilePath);
		}else if(isMode(MODE_LOCAL_FILE)){
			directoryInit(newFilePath);
			FileUtil.copyFile(localFilePath + originalFilePath, localFilePath + newFilePath);
		}
	}
	
	/**
	 * 上传文件。UEditor使用
	 * @param filePath 上传到哪里，包含上传后的文件名，如"image/head/123.jpg"
	 * @param content 文件
	 * @param meta {@link com.aliyun.oss.model.ObjectMetadata}其他属性、说明
	 */
	public static void putForUEditor(String filePath, InputStream input, ObjectMetadata meta){
		if(isMode(MODE_ALIYUN_OSS)){
			com.aliyun.oss.model.ObjectMetadata m = new com.aliyun.oss.model.ObjectMetadata();
			m.setContentLength(meta.getContentLength());
			m.setUserMetadata(meta.getUserMetadata());
			OSSUtil.getOSSClient().putObject(OSSUtil.bucketName, filePath, input, m);
		}else if(isMode(MODE_LOCAL_FILE)){
			put(filePath, input);
		}
	}
	
	/**
	 * SpringMVC 上传文件，配置允许上传的文件后缀再 systemConfig.xml 的attachmentFile.allowUploadSuffix.suffix节点
	 * @param filePath 上传后的文件所在目录、路径，如 "jar/file/"
	 * @param multipartFile SpringMVC接收的 {@link MultipartFile},若是有上传文件，会自动转化为{@link MultipartFile}保存
	 * @return {@link UploadFileVO} 若成功，则上传了文件并且上传成功
	 */
	public static UploadFileVO uploadFileByMultipartFile(String filePath, MultipartFile multipartFile) {
		UploadFileVO vo = new UploadFileVO();
		
		if(multipartFile == null){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_pleaseSelectUploadFile"));
			return vo;
		}
		
		InputStream inputStream = null;
		try {
			inputStream = multipartFile.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(inputStream == null){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_pleaseSelectUploadFile"));
			return vo;
		}
		
		//获取上传的文件的后缀
		String fileSuffix = null;
		fileSuffix = Lang.findFileSuffix(multipartFile.getOriginalFilename());
		
		if(!allowUploadSuffix(fileSuffix)){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_uploadFileNotInSuffixList"));
			return vo;
		}
		
		vo = uploadFileByInputStream(filePath, inputStream, fileSuffix);
		return vo;
	}
	
	/**
	 * SpringMVC 上传图片文件，配置允许上传的文件后缀再 systemConfig.xml 的attachmentFile.allowUploadSuffix.suffix节点
	 * @param filePath 上传后的文件所在目录、路径，如 "jar/file/"
	 * @param multipartFile SpringMVC接收的 {@link MultipartFile},若是有上传图片文件，会自动转化为{@link MultipartFile}保存
	 * @param maxWidth 上传图片的最大宽度，若超过这个宽度，会对图片进行等比缩放为当前宽度。若传入0.则不启用此功能
	 * @return {@link UploadFileVO} 若成功，则上传了文件并且上传成功
	 */
	public static UploadFileVO uploadImageByMultipartFile(String filePath, MultipartFile multipartFile, int maxWidth) {
		UploadFileVO vo = new UploadFileVO();
		
		if(multipartFile == null){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_pleaseSelectUploadFile"));
			return vo;
		}
		
		InputStream inputStream = null;
		try {
			inputStream = multipartFile.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(inputStream == null){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_pleaseSelectUploadFile"));
			return vo;
		}
		
		//获取上传的文件的后缀
		String fileSuffix = null;
		fileSuffix = Lang.findFileSuffix(multipartFile.getOriginalFilename());
		
		if(!allowUploadSuffix(fileSuffix)){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_uploadFileNotInSuffixList"));
			return vo;
		}
		
		vo = uploadImageByInputStream(filePath, inputStream, fileSuffix, maxWidth);
		return vo;
	}
	
	/**
	 * SpringMVC 上传图片文件，配置允许上传的文件后缀再 systemConfig.xml 的attachmentFile.allowUploadSuffix.suffix节点
	 * @param filePath 上传后的文件所在目录、路径，如 "jar/file/"
	 * @param multipartFile SpringMVC接收的 {@link MultipartFile},若是有上传图片文件，会自动转化为{@link MultipartFile}保存
	 * @return {@link UploadFileVO} 若成功，则上传了文件并且上传成功
	 */
	public static UploadFileVO uploadImageByMultipartFile(String filePath, MultipartFile multipartFile) {
		return uploadImageByMultipartFile(filePath, multipartFile, 0);
	}
	
	/**
	 * 上传文件
	 * @param filePath 上传后的文件所在的目录、路径，如 "jar/file/"
	 * @param inputStream 要上传的文件的数据流
	 * @param fileSuffix 上传的文件的后缀名
	 * @return {@link UploadFileVO}
	 */
	public static UploadFileVO uploadFileByInputStream(String filePath, InputStream inputStream, String fileSuffix) {
		UploadFileVO vo = new UploadFileVO();
		
		if(!allowUploadSuffix(fileSuffix)){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_uploadFileNotInSuffixList"));
			return vo;
		}
		
		if(inputStream == null){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_pleaseSelectUploadFile"));
			return vo;
		}
		
		return put(filePath, "."+fileSuffix, inputStream);
	}
	
	/**
	 * 上传图片文件
	 * @param filePath 上传后的文件所在的目录、路径，如 "jar/file/"
	 * @param inputStream 图片的数据流
	 * @param fileSuffix 图片的后缀名
	 * @param maxWidth 上传图片的最大宽度，若超过这个宽度，会对图片进行等比缩放为当前宽度
	 * @return {@link UploadFileVO}
	 */
	public static UploadFileVO uploadImageByInputStream(String filePath, InputStream inputStream, String fileSuffix, int maxWidth) {
		UploadFileVO vo = new UploadFileVO();

		if(!allowUploadSuffix(fileSuffix)){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_uploadFileNotInSuffixList"));
			return vo;
		}
		
		if(inputStream == null){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_pleaseSelectUploadFile"));
			return vo;
		}
		
		//判断其是否进行图像压缩
		if(maxWidth > 0){
			inputStream = ImageUtil.proportionZoom(inputStream, maxWidth, fileSuffix);
		}
		
		return put(filePath, "."+fileSuffix, inputStream);
	}
	
	
	//允许上传的后缀名数组，存储如 jpg 、 gif、zip
	private static String[] allowUploadSuffixs;
	/**
	 * 判断当前后缀名是否在可允许上传的后缀中(systemConfig.xml的attachmentFile.allowUploadSuffix节点配置)，该图片是否允许上传
	 * @param fileSuffix 要判断的上传的文件的后缀名
	 * @return true：可上传，允许上传，后缀在指定的后缀列表中
	 */
	public static boolean allowUploadSuffix(String fileSuffix){
		if(allowUploadSuffixs == null){
			
			String ss[] = Global.ossFileUploadImageSuffixList.split("\\|");
			//过滤一遍，空跟无效的
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < ss.length; i++) {
				String s = ss[i].trim();
				if(s != null && s.length() > 0){
					list.add(s);
				}
			}
			
			//初始化创建数组
			allowUploadSuffixs = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				allowUploadSuffixs[i] = list.get(i);
			}
		}
		
		
		//进行判断，判断传入的suffix是否在允许上传的后缀里面
		for (int j = 0; j < allowUploadSuffixs.length; j++) {
			if(allowUploadSuffixs[j].equalsIgnoreCase(fileSuffix)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 上传文件
	 * @param filePath 上传后的文件所在的目录、路径，如 "jar/file/"
	 * @param fileName 上传的文件名，如“xnx3.jar”；主要拿里面的后缀名。也可以直接传入文件的后缀名如“.jar”
	 * @param inputStream {@link InputStream}
	 * @return {@link PutResult} 若失败，返回null
	 */
	public static UploadFileVO put(String filePath,String fileName,InputStream inputStream){
		UploadFileVO vo = new UploadFileVO();
		
		//进行文件后缀校验
		if(fileName == null || fileName.indexOf(".") == -1){
			vo.setBaseVO(UploadFileVO.FAILURE, "上传的文件名(后缀)校验失败！传入的为："+fileName+"，允许传入的值如：a.jpg或.jpg");
			return vo;
		}
		String fileSuffix=com.xnx3.Lang.subString(fileName, ".", null, 3);	//获得文件后缀，以便重命名
        String name=Lang.uuid()+"."+fileSuffix;
        String path = filePath+name;
        return put(path, inputStream);
	}
	
	/**
	 * 将阿里云OSS的上传结果 {@link PutResult}转化为 {@link UploadFileVO}结果
	 * @param pr 阿里云OSS的上传结果 {@link PutResult}
	 * @return {@link UploadFileVO}
	 */
	public static UploadFileVO PutResultToUploadFileVO(PutResult pr){
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
	
	/**
	 * SpringMVC 上传图片文件，配置允许上传的文件后缀再 systemConfig.xml 的AttachmentFile节点
	 * @param filePath 上传后的文件所在的目录、路径，如 "jar/file/"
	 * @param request SpringMVC接收的 {@link MultipartFile},若是有上传图片文件，会自动转化为{@link MultipartFile}保存
	 * @param formFileName form表单上传的单个图片文件，表单里上传文件的文件名
	 * @param maxWidth 上传图片的最大宽度，若超过这个宽度，会对图片进行等比缩放为当前宽度。
	 * @return {@link UploadFileVO} 若成功，则上传了文件并且上传成功
	 */
	public static UploadFileVO uploadImage(String filePath,HttpServletRequest request,String formFileName, int maxWidth) {
		UploadFileVO uploadFileVO = new UploadFileVO();
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
			List<MultipartFile> imageList = multipartRequest.getFiles(formFileName);  
			if(imageList.size()>0 && !imageList.get(0).isEmpty()){
				MultipartFile multi = imageList.get(0);
				uploadFileVO = uploadImageByMultipartFile(filePath, multi, maxWidth);
			}else{
				uploadFileVO.setResult(UploadFileVO.NOTFILE);
				uploadFileVO.setInfo(Language.show("oss_uploadNotFile"));
			}
	    }else{
	    	uploadFileVO.setResult(UploadFileVO.NOTFILE);
			uploadFileVO.setInfo(Language.show("oss_uploadNotFile"));
	    }
		return uploadFileVO;
	}
	
	/**
	 * SpringMVC 上传文件，配置允许上传的文件后缀再 systemConfig.xml 的AttachmentFile节点
	 * @param filePath 上传后的文件所在的目录、路径，如 "jar/file/"
	 * @param request SpringMVC接收的 {@link MultipartFile},若是有上传文件，会自动转化为{@link MultipartFile}保存
	 * @param formFileName form表单上传的单个文件，表单里上传文件的文件名
	 * @return {@link UploadFileVO} 若成功，则上传了文件并且上传成功
	 */
	public static UploadFileVO uploadFile(String filePath,HttpServletRequest request,String formFileName) {
		UploadFileVO uploadFileVO = new UploadFileVO();
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
			List<MultipartFile> list = multipartRequest.getFiles(formFileName);  
			if(list.size()>0 && !list.get(0).isEmpty()){
				MultipartFile multi = list.get(0);
				uploadFileVO = uploadFileByMultipartFile(filePath, multi);
			}else{
				uploadFileVO.setResult(UploadFileVO.NOTFILE);
				uploadFileVO.setInfo(Language.show("oss_uploadNotFile"));
			}
	    }else{
	    	uploadFileVO.setResult(UploadFileVO.NOTFILE);
			uploadFileVO.setInfo(Language.show("oss_uploadNotFile"));
	    }
		return uploadFileVO;
	}
	
	/**
	 * 获取某个目录（文件夹）占用空间的大小
	 * @param path 要计算的目录(文件夹)，如 jar/file/
	 * @return 计算出来的大小。单位：字节，B。  千分之一KB
	 */
	public static long getDirectorySize(String path){
		if(isMode(MODE_ALIYUN_OSS)){
			return OSSUtil.getFolderSize(path);
		}else if(isMode(MODE_LOCAL_FILE)){
			directoryInit(path);
			return FileUtils.sizeOfDirectory(new File(localFilePath+path));
		}
		
		return 0;
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
					if(!FileUtil.exists(localFilePath+xiangdui)){
						File file = new File(localFilePath+xiangdui);
						file.mkdir();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		directoryInit("sitse/1/new/a.jpg");
	}
}
