package com.xnx3.j2ee.func;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse.Credentials;
import com.xnx3.Lang;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.media.ImageUtil;
import com.xnx3.net.OSSUtil;
import com.xnx3.net.ossbean.CredentialsVO;
import com.xnx3.net.ossbean.PutResult;

/**
 * 阿里云OSS相关操作，如上传图片。建议上传图片、附件、文件等，用 {@link AttachmentFile} 进行上传。可自由配置使用哦OSS、或服务器磁盘等
 * @author 管雷鸣
 */
public class OSS {

	/**
	 * SpringMVC 带的文件上传
	 * <br/>推荐使用 uploadImageByMultipartFile
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
	 * @param multipartFile SpringMVC接收的 {@link MultipartFile}
	 * @return {@link UploadFileVO}
	 * @deprecated
	 */
	public static UploadFileVO upload(String filePath, MultipartFile multipartFile) {
		return uploadImageByMultipartFile(filePath, multipartFile);
	}

	/**
	 * SpringMVC 上传图片文件，配置允许上传的文件后缀再 systemConfig.xml 的OSS节点
	 * <br/>推荐使用 uploadImageByMultipartFile
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
	 * @param multipartFile SpringMVC接收的 {@link MultipartFile}
	 * @return {@link UploadFileVO}
	 * @deprecated
	 */
	public static UploadFileVO uploadImage(String filePath, MultipartFile multipartFile) {
		return uploadImageByMultipartFile(filePath, multipartFile);
	}

	/**
	 * 判断某个后缀名是否在可上传图片的后缀列表中(systemConfig.xml的attachmentFile.allowUploadSuffix节点配置)，该图片是否允许上传
	 * @param fileSuffix 要判断的上传的文件的后缀名
	 * @return true：可上传，允许上传，后缀在指定的后缀列表中
	 */
	public static boolean imageAllowUpload(String fileSuffix){
		String[] ia = Global.ossFileUploadImageSuffixList.split("\\|");
		for (int j = 0; j < ia.length; j++) {
			if(ia[j].length()>0){
				if(ia[j].equalsIgnoreCase(fileSuffix)){
					return true;
				}
			}
		}
		return false;
	}
	

	/**
	 * 上传图片文件
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
	 * @param inputStream 图片的数据流
	 * @param fileSuffix 图片的后缀名
	 * @param maxWidth 上传图片的最大宽度，若超过这个宽度，会对图片进行等比缩放为当前宽度
	 * @return {@link UploadFileVO}
	 */
	public static UploadFileVO uploadImageByInputStream(String filePath, InputStream inputStream, String fileSuffix, int maxWidth) {
		UploadFileVO vo = new UploadFileVO();
		
		if(!imageAllowUpload(fileSuffix)){
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
		
		PutResult pr = null;
		pr = OSSUtil.put(filePath, "."+Safety.filter(fileSuffix), inputStream);
		
		vo.setPath(pr.getPath());
		vo.setFileName(pr.getFileName());
		vo.setUrl(pr.getUrl());
		
		return vo;
	}

	/**
	 * SpringMVC 带的文件上传
	 * <br/>推荐使用 uploadImageByMultipartFile
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
	 * @param multipartFile SpringMVC接收的 {@link MultipartFile}
	 * @return {@link UploadFileVO}
	 */
	public static UploadFileVO uploadImageByMultipartFile(String filePath, MultipartFile multipartFile) {
		return uploadImageByMultipartFile(filePath, multipartFile, 0);
	}
	
	/**
	 * SpringMVC 上传图片文件，配置允许上传的文件后缀再 systemConfig.xml 的OSS节点
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
	 * @param multipartFile SpringMVC接收的 {@link MultipartFile},若是有上传图片文件，会自动转化为{@link MultipartFile}保存
	 * @param maxWidth 上传图片的最大宽度，若超过这个宽度，会对图片进行等比缩放为当前宽度。
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
		fileSuffix = Lang.findFileSuffix(Safety.filter(multipartFile.getOriginalFilename()));
		
		if(!imageAllowUpload(fileSuffix)){
			vo.setBaseVO(UploadFileVO.FAILURE, Language.show("oss_uploadFileNotInSuffixList"));
			return vo;
		}
		
		vo = uploadImageByInputStream(filePath, inputStream, fileSuffix, maxWidth);
		return vo;
	}
	
	/**
	 * 创建 授权于OSS GetObject、PutObject 权限的临时账户（此只是针对 {@link OSSUtil#createSTS(String, String)}的简化 ）
	 * @return {@link Credentials}
	 */
	public static CredentialsVO createGetAndPutObjectSTS() {
		String policy = "{\n" +
				"    \"Version\": \"1\", \n" +
				"    \"Statement\": [\n" +
				"        {\n" +
				"            \"Action\": [\n" +
				"                \"oss:PutObject\", \n" +
				"                \"oss:GetObject\" \n" +
				"            ], \n" +
				"            \"Resource\": [\n" +
				"                \"acs:oss:*:*:*\"\n" +
				"            ], \n" +
				"            \"Effect\": \"Allow\"\n" +
				"        }\n" +
				"    ]\n" +
				"}";
		String id = "";
		if(ShiroFunc.getUser() == null){
			id = Lang.uuid();
		}else{
			id = "user"+ShiroFunc.getUser().getId();
		}
		
		Credentials credentials = OSSUtil.createSTS(id, policy);
		CredentialsVO cVO = new CredentialsVO();
		if(credentials == null){
			cVO.setBaseVO(com.xnx3.BaseVO.FAILURE, "创建失败");
		}else{
			cVO.setAccessKeyId(credentials.getAccessKeyId());
			cVO.setAccessKeySecret(credentials.getAccessKeySecret());
			cVO.setExpiration(credentials.getExpiration());
			cVO.setSecurityToken(credentials.getSecurityToken());
		}
		
		return cVO;
	}

	/**
	 * SpringMVC 上传图片文件，配置允许上传的文件后缀再 systemConfig.xml 的OSS节点
	 * <br/>推荐使用  uploadImageByMultipartFile
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
	 * @param multipartFile SpringMVC接收的 {@link MultipartFile}
	 * @param maxWidth 上传图片的最大宽度，若超过这个宽度，会对图片进行等比缩放为当前宽度。
	 * @return {@link UploadFileVO}
	 * @deprecated
	 */
	public static UploadFileVO uploadImage(String filePath, MultipartFile multipartFile, int maxWidth) {
		return uploadImageByMultipartFile(filePath, multipartFile, maxWidth);
	}
	
	/**
	 * SpringMVC 上传图片文件，配置允许上传的文件后缀再 systemConfig.xml 的OSS节点
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
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
	 * SpringMVC 上传单个图片文件，配置允许上传的文件后缀再 systemConfig.xml 的OSS节点
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
	 * @param request SpringMVC接收的 {@link MultipartFile},若是有上传图片文件，会自动转化为{@link MultipartFile}保存
	 * @param formFileName form表单上传的单个图片文件，表单里上传文件的文件名
	 * @return {@link UploadFileVO} 若成功，则上传了文件并且上传成功
	 */
	public static UploadFileVO uploadImageByRequest(String filePath, HttpServletRequest request, String formFileName) {
		UploadFileVO uploadFileVO = new UploadFileVO();
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
			List<MultipartFile> imageList = multipartRequest.getFiles(formFileName);  
			
			if(imageList.size()>0 && !imageList.get(0).isEmpty()){
				uploadFileVO = uploadImageByMultipartFile(filePath, imageList.get(0));
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
	 * SpringMVC 上传单个图片文件，配置允许上传的文件后缀再 systemConfig.xml 的OSS节点
	 * <br/> 建议使用 uploadImageByRequest
	 * @param filePath 上传后的文件所在OSS的目录、路径，如 "jar/file/"
	 * @param request SpringMVC接收的 {@link MultipartFile},若是有上传图片文件，会自动转化为{@link MultipartFile}保存
	 * @param formFileName form表单上传的单个图片文件，表单里上传文件的文件名
	 * @return {@link UploadFileVO} 若成功，则上传了文件并且上传成功
	 * @deprecated
	 */
	public static UploadFileVO uploadImage(String filePath, HttpServletRequest request, String formFileName) {
		UploadFileVO uploadFileVO = new UploadFileVO();
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
			List<MultipartFile> imageList = multipartRequest.getFiles(formFileName);  
			
			if(imageList.size()>0 && !imageList.get(0).isEmpty()){
				uploadFileVO = uploadImageByMultipartFile(filePath, imageList.get(0));
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
	
}
