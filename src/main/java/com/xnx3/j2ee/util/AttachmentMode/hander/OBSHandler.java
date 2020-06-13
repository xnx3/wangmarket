package com.xnx3.j2ee.util.AttachmentMode.hander;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.AccessControlList;
import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.ListObjectsRequest;
import com.obs.services.model.ObjectListing;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ObsBucket;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import com.obs.services.model.S3Bucket;
import com.obs.services.model.StorageClassEnum;
import com.xnx3.StringUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.vo.UploadFileVO;

/**
 * 华为云OBS对象存储系统操作类
 * @author 李鑫
 */
@SuppressWarnings("deprecation")
public class OBSHandler {
	
	private String accessKeyId;// 华为云的 Access Key Id
	private String accessKeySecret;// 华为云的 Access Key Secret
	public String endpoint; // 华为云连接的地址节点
	
	private String obsBucketName; // 创建的桶的名称
	private String url; // 访问OBS文件的url
	
	private static ObsClient obsClient; // 进行操作的华为云的客户端组件
	
	/**
	 * 创建华为云OBS的本地控制器
	 * <br/>
	 * <br/>
	 * 使用示例 obsHandler = new OBSHandler("jasygaas327i6ewliasfi", "navugaiuaio1231isdhvsdtyquys", "https://obs.cn-	north-1.myhuaweicloud.com");
	 * 
	 * @author 李鑫
	 * @param accessKeyId 华为云的 Access Key Id
	 * @param accessKeySecret 华为云的 Access Key Secret
	 * @param endpoint 华为云连接的地址节点
	 */
	public OBSHandler(String accessKeyId, String accessKeySecret, String endpoint) {
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.endpoint = endpoint;
	}
	
	/**
	 * 创建华为云OBS的本地控制器
	 * <br/>
	 * <br/>
	 * 使用示例 obsHandler = new OBSHandler("jasygaas327i6ewliasfi", "navugaiuaio1231isdhvsdtyquys", "https://obs.cn-	north-1.myhuaweicloud.com", "wangmarket156987656");
	 * 
	 * @author 李鑫
	 * @param accessKeyId 华为云的 Access Key Id
	 * @param accessKeySecret 华为云的 Access Key Secret
	 * @param endpoint 华为云连接的地址节点
	 * @param obsBucketName 进行操作的桶名称
	 */
	public OBSHandler(String accessKeyId, String accessKeySecret, String endpoint, String obsBucketName) {
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.endpoint = endpoint;
		this.obsBucketName = obsBucketName;
	}
	
	/**
	 * 设置OBS访问的CDN路径
	 * @author 李鑫
	 * @param url 需要配置的访问OBS的CDN路径 传入的格式如 http://cdn.leimingyun.com/  
	 */
	public void setUrlForCDN(String url) {
		if(url == null){
			return;
		}
		url = url.trim();	//去前后空字符
		if(url.length() < 2){
			this.url = url;
			return;
		}
		
		if(!url.substring(url.length() - 1, url.length()).equals("/")){
			//如果url末尾没有 / ，那么加上
			url = url + "/";
		}
		this.url = url;
	}
	
	/**
	 * 设置OBS操作的同桶名称
	 * @author 李鑫
	 * @param obsBucketName 需要操作的桶名称 例：“wangmarket156921738”
	 */
	public void setObsBucketName(String obsBucketName) {
		this.obsBucketName = obsBucketName;
	}
	
	/**
	 * 获取华为云提供的操作客户端实体类
	 * @author 李鑫
	 * @return {@link com.obs.services.ObsClient} 华为云客户端
	 */
	public ObsClient getObsClient() {
		if(obsClient == null) {
			obsClient = new ObsClient(accessKeyId, accessKeySecret, endpoint);
		}
		return obsClient;
	}
	
	/**
	 * 下载ObsObject
	 * @author 李鑫
	 * @param bucketName 操作的桶的名称 例："wangmarket1232311"
	 * @param filePath 需要下载的文件路径。 例："site/a.txt"
	 * @return 下载文件的字节数组
	 * @throws IOException
	 */
	public byte[] getFileByteArray(String bucketName, String filePath) throws IOException {
		ObsObject obsObject = getObsClient().getObject(bucketName, filePath);
		InputStream input = obsObject.getObjectContent();
		byte[] b = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		int len;
		while ((len = input.read(b)) != -1){
			bos.write(b, 0, len);
		}
		bos.close();
		input.close();
		return bos.toByteArray();
	}
	
	/**
	 * 获取指定路径下的ObsObject数量
	 * @author 李鑫
	 * @param bucketName 操作的桶的名称 例："wangmarket1232311"
	 * @param filePath 需要检索的文件夹路径 例："site/"
	 * @return 检索搜文件下的ObsObject的数量
	 */
	public Integer getFolderObjectsSize(String bucketName, String filePath) {
		ListObjectsRequest request = new ListObjectsRequest(bucketName);
		if(filePath != null && (!filePath.trim().equals(""))){
			request.setPrefix(filePath);
		}
		ObjectListing result = getObsClient().listObjects(request);
		return new Integer(result.getObjects().size());
	}
	
	/**
	 * 获取指定路径下的ObsObject
	 * @author 李鑫
	 * @param bucketName 操作的桶的名称 例："wangmarket1232311"
	 * @param filePath 需要检索的文件夹路径
	 * @return 路径下的所有的ObsObject，包括子文件夹下的ObsObject
	 */
	public List<ObsObject> getFolderObjects(String bucketName, String filePath) {
		List<ObsObject> list = new ArrayList<ObsObject>();
		ListObjectsRequest request = new ListObjectsRequest(bucketName);
		if(filePath != null && (!filePath.trim().equals(""))){
			request.setPrefix(filePath);
		}
		request.setMaxKeys(100);
		ObjectListing result;
		do{
			result = getObsClient().listObjects(request);
			for(ObsObject obsObject : result.getObjects()){
				list.add(obsObject);
			}
			request.setMarker(result.getNextMarker());
		}while(result.isTruncated());
		return list;
	}
	
	/**
	 * 删除对象
	 * @author 李鑫
	 * @param bucketName 操作的桶的名称 例："wangmarket1232311"
	 * @param fileName 需要删除的对象全名 例："site/20190817/localFile.sh"
	 * @return {@link com.obs.services.model.DeleteObjectResult} 删除对象的响应结果
	 */
	public DeleteObjectResult deleteObject(String bucketName, String fileName) {
		return getObsClient().deleteObject(bucketName, fileName);
	}
	
	/**
	 * 创建文件夹
	 * @author 李鑫
	 * @param bucketName 操作的桶的名称 例："wangmarket1232311"
	 * @param fileName 新建文件夹的路径，总根路径开始，请务必以"/"结尾。例："2019/0817/"
	 * @return {@link com.obs.services.model.PutObjectResult} 返回创建的结果
	 */
	public PutObjectResult mkdirFolder(String bucketName, String fileName) {
		return getObsClient().putObject(bucketName, fileName, new ByteArrayInputStream(new byte[0]));
	}
	
	/**
	 * 通过流上传字符串为文件
	 * @author 李鑫
	 * @param bucketName 操作的桶的名称 例："wangmarket1232311"
	 * @param fileName 上传的路径和文件名 例："site/2010/example.txt"
	 * @param content 上传的String字符
	 * @param encode 进行转换byte时使用的编码格式 例："UTF-8"
	 * @return {@link UploadFileVO} 返回上传的结果
	 * @throws UnsupportedEncodingException 
	 * @throws ObsException 
	 */
	public UploadFileVO putStringFile(String bucketName, String fileName, String content, String encode) throws ObsException, UnsupportedEncodingException {
		PutObjectResult result = getObsClient().putObject(bucketName, fileName, new ByteArrayInputStream(content.getBytes(encode)));
		return getUploadFileVO(result);
	}
	
	/**
	 * 上传文件本地文件
	 * @author 李鑫
	 * @param bucketName 操作的桶的名称 例："wangmarket1232311"
	 * @param fileName 上传的路径和文件名 例："site/2010/example.txt"
	 * @param localFile 需要上传的文件
	 * @return {@link UploadFileVO} 返回上传的结果
	 */
	public UploadFileVO putLocalFile(String bucketName, String fileName, File localFile) {
		PutObjectResult result = getObsClient().putObject(bucketName, fileName, localFile);
		return getUploadFileVO(result);
	}
	
	/**
	 * 上传文件流
	 * @author 李鑫
	 * @param bucketName 操作的桶的名称 例："wangmarket1232311"
	 * @param fileName 上传的路径和文件名 例："site/2010/example.txt"
	 * @param inputStream 上传文件的输入流
	 * @return {@link UploadFileVO} 返回上传的结果
	 */
	public UploadFileVO putFileByStream(String bucketName, String fileName, InputStream inputStream) {
		PutObjectResult result = getObsClient().putObject(bucketName, fileName, inputStream);
		return getUploadFileVO(result);
	}
	
	/**
	 * 通过流上传文件并设置指定文件属性
	 * @author 李鑫
	 * @param bucketName 操作的桶的名称 例："wangmarket1232311"
	 * @param fileName 上传的路径和文件名 例："site/2010/example.txt"
	 * @param inputStream 上传文件的输入流
	 * @param metaData 上传文件的属性
	 * @return {@link UploadFileVO} 返回上传的结果
	 */
	public UploadFileVO putFilebyInstreamAndMeta(String bucketName, String fileName, InputStream inputStream, ObjectMetadata metaData) {
		PutObjectResult result = getObsClient().putObject(bucketName, fileName, inputStream, metaData);
		return getUploadFileVO(result);
	}
	
	/**
	 * OBS内对象复制
	 * @author 李鑫
	 * @param sourceBucketName 源文件的桶名称 例："wangmarket1232311"
	 * @param sourcePath 源文件的路径和文件名 例："site/2010/example.txt"
	 * @param destBucketName 目标文件的桶名称 例："swangmarket34578345"
	 * @param destPath 目标文件的路径和文件名 例："site/2010/example_bak.txt"
	 */
	public void copyObject(String sourceBucketName, String sourcePath,String destBucketName, String destPath) {
		getObsClient().copyObject(sourceBucketName, sourcePath, destBucketName, destPath);
	}
	
	/**
	 * 获得原生OBSBucket的访问前缀
	 * @author 李鑫
	 * @return 桶原生的访问前缀，即不经过CDN加速的访问路径
	 */
	public String getOriginalUrlForOBS() {
		String endpoint_ = "";
		if(this.endpoint.indexOf("https://") > -1){
			endpoint_ = this.endpoint.substring(8, this.endpoint.length());
		}else{
			endpoint_ = this.endpoint;
		}
		return "//" + obsBucketName + "." + endpoint_ + "/";
	}
	
	/**
	 * 通过bucket的名字和连接点信息获取bucket访问的url
	 * @author 李鑫
	 * @param bucketName 桶的名称 例："wangmarket21345665"
	 * @param endpoint 连接点的名称 例："obs.cn-north-1"
	 * @return 根据信息获得桶的访问路径 例："//wangmarket21345665.obs.cn-north-1.myhuaweicloud.com/"
	 */
	public String getUrlByBucketName(String bucketName, String endpoint) {
		String url = null;
		if (url == null || url.length() == 0) {
			url = "//" + bucketName + "." +  endpoint + ".myhuaweicloud.com" + "/";
		}
		return url;
	}
	
	/**
	 * 创建华为云ObsBucket，默认设置为标准存储，桶访问权限为公共读私有写，同策略为所有用户可读桶内对象和桶内对象版本信息
	 * @author 李鑫
	 * @param obsBucketName 创建桶的名称
	 * @return 新创建的桶的名字
	 */
	public String createOBSBucket(String obsBucketName) {
		if(this.endpoint == null || this.endpoint.length() == 0){
			ConsoleUtil.error("error ! obs endpoint is null");
			return "endpoint is null";
		}
		String location = StringUtil.subString(this.endpoint, "obs.", ".myhuaweicloud.com");
		ConsoleUtil.log("setLocation : "+location);
				
		// 将桶的名字进行保存
		this.obsBucketName = obsBucketName;
		ObsBucket obsBucket = new ObsBucket();
		obsBucket.setBucketName(obsBucketName);
		// 设置桶访问权限为公共读，默认是私有读写
		obsBucket.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
		// 设置桶的存储类型为标准存储
		obsBucket.setBucketStorageClass(StorageClassEnum.STANDARD);
		obsBucket.setLocation(location);
		// 创建桶
		getObsClient().createBucket(obsBucket);
		//设置桶策略
		String json = "{"
				+ "\"Statement\":["
					+ "{"
						+ "\"Sid\":\"为授权用户创建OBS使用的桶策略\","
						+ "\"Principal\":{\"ID\" : \"*\"},"
						+ "\"Effect\":\"Allow\","
						+ "\"Action\":[\"GetObject\",\"GetObjectVersion\"],"
						+ "\"Resource\": [\"" + obsBucketName + "/*\"]"
					+ "}"
				+ "]}";
		getObsClient().setBucketPolicy(obsBucketName, json);
		return obsBucketName;
	}
	
	/**
	 * 获取当前的桶列表
	 * @author 李鑫
	 * @return 当前桶的列表信息
	 */
	public List<S3Bucket> getBuckets() {
		return getObsClient().listBuckets();
 	}
	
	/**
	 * 关闭当前的使用的OBSClient
	 * @author 李鑫
	 */
	public void closeOBSClient() {
		if(getObsClient() != null){
			try {
				getObsClient().close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 返回当前的创建桶的名称 例："wangmarket1232311" 
	 * @author 李鑫
	 * @return 如果有桶，那么返回桶的名称，如 "wangmarket1232311" ，如果没有，则返回 null
	 */
	public String getObsBucketName() {
		return this.obsBucketName;
	}
	
	/**
	 * 返回当前的桶的访问路径 例：“ http://cdn.leimingyun.com/”
	 * @author 李鑫
	 * @return 若已经手动设置CDN路径返回为CND路径，反之则为OBS原始的访问路径
	 */
	public String getUrl() {
		// 用户没有配置CDN，获的桶的原生访问路径
		if(url == null) {
			url = getOriginalUrlForOBS();
		}
		return url;
	}
	
	/**
	 * 将PutObjectResult封装为UploadFileVO但会返回
	 * @author 管雷鸣修改
	 * @param result 华为云文件上传返回的结果封装类
	 * @return {@link com.xnx3.j2ee.vo.UploadFileVO} 经过封装的UploadFileVO类
	 */
	private UploadFileVO getUploadFileVO(PutObjectResult result) {
		UploadFileVO vo = new UploadFileVO();
		// 上传成功
		if(result.getStatusCode() == 200) {
			vo.setResult(UploadFileVO.SUCCESS);
			vo.setInfo("success");
			vo.setPath(result.getObjectKey());
			if(getUrl() == null || getUrl().length() == 0){
				//未配置cdn，那么使用华为自带的域名
				vo.setUrl(result.getObjectUrl());
			}else{
				//配置了cdn，那么就用cdn域名
				vo.setUrl(getUrl()+result.getObjectKey());
			}
			
			return vo;
		}
		
		// 上传失败
		vo.setBaseVO(UploadFileVO.FAILURE, "上传失败!statusCode:"+result.getStatusCode()+",RequestId:"+result.getRequestId());
		return vo;
	}

}
