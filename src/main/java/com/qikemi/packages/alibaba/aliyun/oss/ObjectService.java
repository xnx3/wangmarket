package com.qikemi.packages.alibaba.aliyun.oss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ListObjectsRequest;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.qikemi.packages.utils.SystemUtil;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.ConsoleUtil;

/**
 * Object是OSS中最基本的数据单元，你可以把它简单地理解为文件<br>
 * 
 * 在OSS中，用户操作的基本数据单元是Object。单个Object最大允许存储5TB的数据。<br>
 * Object包含key、meta和data。其中，<br>
 * key是Object的名字；<br>
 * meta是用户对该object的描述，由一系列name-value对组成；<br>
 * data是Object的数据。
 * 
 * @create date : 2014年10月28日 下午2:03:24
 * @Author XieXianbin<a.b@hotmail.com>
 * @Source Repositories Address: <https://github.com/qikemi/UEditor-for-aliyun-OSS>
 */
public class ObjectService {
	
	/**
	 * 上传Object
	 * 
	 * @param client
	 * @param bucketName
	 * @param key
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 */
//	public static PutObjectResult putObject(OSSClient client,
//			String bucketName, String key, String filePath)
//			throws FileNotFoundException {
//
//		// 获取指定文件的输入流
//		File file = new File(filePath);
//		InputStream content = new FileInputStream(file);
//
//		// 创建上传Object的Metadata
//		ObjectMetadata meta = new ObjectMetadata();
//
//		// 必须设置ContentLength
//		meta.setContentLength(file.length());
//		// 用户自定义文件名称
//		meta.addUserMetadata("filename", key);
//
//		// 上传Object.
//		PutObjectResult result = client.putObject(bucketName, key, content,
//				meta);
//
//		return result;
//	}
	public boolean upload(JSONObject stateJson, HttpServletRequest request) {
//		Bucket bucket = BucketService.create(client,
//				OSSClientProperties.bucketName);
		// get the key, which the upload file path
		String key = stateJson.getString("url").replaceFirst("/", "");
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(SystemUtil.getProjectRootPath() + key));
//			PutObjectResult result = ObjectService.putObject(client, OSSClientProperties.bucketName, key, fileInputStream);
			ObjectService.putObject(key, fileInputStream);
			return true;
		} catch (FileNotFoundException e) {
			ConsoleUtil.error("upload file to aliyun OSS object server occur FileNotFoundException.");
		} catch (NumberFormatException e) {
			ConsoleUtil.error("upload file to aliyun OSS object server occur NumberFormatException.");
		} catch (IOException e) {
			ConsoleUtil.error("upload file to aliyun OSS object server occur IOException.");
		}
		return false;
	}


	/**
	 * 上传Object
	 * 
	 * @param client
	 * @param bucketName
	 * @param key
	 * @param content
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
//	public static PutObjectResult putObject(OSSClient client,
//			String bucketName, String key, InputStream content)
//			throws NumberFormatException, IOException {
//
//		// 创建上传Object的Metadata
//		ObjectMetadata meta = new ObjectMetadata();
//
//		// 必须设置ContentLength
//		meta.setContentLength(Integer.parseInt(String.valueOf(content
//				.available())));
//		// 用户自定义文件名称
//		meta.addUserMetadata("filename", key);
//
//		// 上传Object.
//		PutObjectResult result = client.putObject(bucketName, key, content,
//				meta);
//
//		return result;
//	}

	public static void putObject(String key, InputStream content)
			throws NumberFormatException, IOException {

		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();

		// 必须设置ContentLength
		meta.setContentLength(Integer.parseInt(String.valueOf(content .available())));
		// 用户自定义文件名称
		meta.addUserMetadata("filename", key);
		ConsoleUtil.debug("putObject--filename : "+key);
		
		// 上传Object.
//		PutObjectResult result = client.putObject(bucketName, key, content, meta);
		AttachmentUtil.putForUEditor(key, content, meta);
	}
	
	/**
	 * 列出Object<br>
	 * 
	 * @param client
	 * @param bucketName
	 * @param delimiter
	 *            Delimiter 设置为 “/” 时，返回值就只罗列该文件夹下的文件，可以null
	 * @param prefix
	 *            Prefix 设为某个文件夹名，就可以罗列以此 Prefix 开头的文件，可以null
	 * @return
	 */
	public static List<String> listObject(OSSClient client, String bucketName,
			String delimiter, String prefix) {

		// 是否循环的标识
		boolean hasNext = false;
		// 设定结果从Marker之后按字母排序的第一个开始返回
		String marker = "";
		//
		// ObjectListing listing = new ObjectListing();
		List<String> filePathList = new ArrayList<String>();
		// 构造ListObjectsRequest请求
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
				bucketName);

		// 是一个用于对Object名字进行分组的字符。所有名字包含指定的前缀且第一次出现Delimiter字符之间的object作为一组元素:
		// CommonPrefixes
		listObjectsRequest.setDelimiter(delimiter);
		// 限定此次返回object的最大数，如果不设定，默认为100，MaxKeys取值不能大于1000
		listObjectsRequest.setMaxKeys(20);
		// 限定返回的object key必须以Prefix作为前缀。注意使用prefix查询时，返回的key中仍会包含Prefix
		listObjectsRequest.setPrefix(prefix);

		do {
			// 设定结果从Marker之后按字母排序的第一个开始返回
			listObjectsRequest.setMarker(marker);
			// 获取指定bucket下的所有Object信息
			ObjectListing sublisting = client.listObjects(listObjectsRequest);
			// 如果Bucket中的Object数量大于100，则只会返回100个Object， 且返回结果中 IsTruncated
			// 为false
			if (sublisting.isTruncated()) {
				hasNext = true;
				marker = sublisting.getNextMarker();
			} else {
				hasNext = false;
				marker = "";
			}
			// // 遍历所有Object
			for (OSSObjectSummary objectSummary : sublisting.getObjectSummaries()) {
				// System.out.println(objectSummary.getKey());
				filePathList.add("/"+objectSummary.getKey());
			}
		} while (hasNext);

		return filePathList;
	}
	
	
//	public static List<String> listObject(String delimiter, String prefix) {
//		// 是否循环的标识
//		boolean hasNext = false;
//		// 设定结果从Marker之后按字母排序的第一个开始返回
//		String marker = "";
//		//
//		// ObjectListing listing = new ObjectListing();
//		List<String> filePathList = new ArrayList<String>();
//		// 构造ListObjectsRequest请求
//		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
//				bucketName);
//		
//		// 是一个用于对Object名字进行分组的字符。所有名字包含指定的前缀且第一次出现Delimiter字符之间的object作为一组元素:
//		// CommonPrefixes
//		listObjectsRequest.setDelimiter(delimiter);
//		// 限定此次返回object的最大数，如果不设定，默认为100，MaxKeys取值不能大于1000
//		listObjectsRequest.setMaxKeys(20);
//		// 限定返回的object key必须以Prefix作为前缀。注意使用prefix查询时，返回的key中仍会包含Prefix
//		listObjectsRequest.setPrefix(prefix);
//		
//		do {
//			// 设定结果从Marker之后按字母排序的第一个开始返回
//			listObjectsRequest.setMarker(marker);
//			// 获取指定bucket下的所有Object信息
//			ObjectListing sublisting = client.listObjects(listObjectsRequest);
//			// 如果Bucket中的Object数量大于100，则只会返回100个Object， 且返回结果中 IsTruncated
//			// 为false
//			if (sublisting.isTruncated()) {
//				hasNext = true;
//				marker = sublisting.getNextMarker();
//			} else {
//				hasNext = false;
//				marker = "";
//			}
//			// // 遍历所有Object
//			for (OSSObjectSummary objectSummary : sublisting.getObjectSummaries()) {
//				// System.out.println(objectSummary.getKey());
//				filePathList.add("/"+objectSummary.getKey());
//			}
//		} while (hasNext);
//		
//		return filePathList;
//	}

	
//	public static void getObject(OSSClient client, String bucketName, String key)
//			throws IOException {
//
//		// 获取Object，返回结果为OSSObject对象
//		OSSObject object = client.getObject(bucketName, key);
//
//		// 获取Object的输入流
//		InputStream objectContent = object.getObjectContent();
//
//		// 处理Object
//
//		// 关闭流
//		objectContent.close();
//
//	}
}
