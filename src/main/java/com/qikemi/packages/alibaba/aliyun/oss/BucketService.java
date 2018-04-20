package com.qikemi.packages.alibaba.aliyun.oss;

import java.util.List;
import com.aliyun.openservices.oss.OSSClient;
//import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.Bucket;
import com.aliyun.openservices.oss.model.CannedAccessControlList;

/**
 * Bucket是OSS上的命名空间，也是计费、权限控制、日志记录等高级功能的管理实体；Bucket名称在整个OSS服务中具有全局唯一性，且不能修改；
 * 存储在OSS上的每个Object必须都包含在某个Bucket中
 * 。一个应用，例如图片分享网站，可以对应一个或多个Bucket。一个用户最多可创建10个Bucket
 * ，但每个Bucket中存放的Object的数量和大小总和没有限制，用户不需要考虑数据的可扩展性。
 * 
 * @create date : 2014年10月28日 上午10:52:00
 * @Author XieXianbin<a.b@hotmail.com>
 * @Source Repositories Address: <https://github.com/qikemi/UEditor-for-aliyun-OSS>
 */
public class BucketService {

	/**
	 * 创建一个Bucket，如果创建成功，则返回新的Bucket <br>
	 * 
	 * Bucket命名规则：<br>
	 * 只能包括小写字母，数字，短横线（-） <br>
	 * 必须以小写字母或者数字开头 <br>
	 * 长度必须在3-63字节之间
	 * 
	 * @return
	 */
	public static Bucket create(OSSClient client, String bucketName) {
		Bucket bucket = client.createBucket(bucketName);
		return bucket;
	}
//
//	/**
//	 * 列出用户所有的Bucket<br>
//	 * 
//	 * @return
//	 */
//	public static List<Bucket> list(OSSClient client) {
//		List<Bucket> buckets = client.listBuckets();
//		return buckets;
//	}
//
//	/**
//	 * 判断Bucket是否存在<br>
//	 * 
//	 * @param client
//	 * @param bucketName
//	 * @return
//	 */
//	public static boolean doesBucketExist(OSSClient client, String bucketName) {
//		// 获取Bucket的存在信息
//		boolean exists = client.doesBucketExist(bucketName);
//		// 输出结果
//		if (exists) {
//			logger.debug("Bucket: " + bucketName + " is exists");
//		} else {
//			logger.debug("Bucket: " + bucketName + " is not exists");
//		}
//		return exists;
//	}
//
//	/**
//	 * 删除Bucket<br>
//	 * 如果Bucket不为空（Bucket中有Object），则Bucket无法删除，必须清空Bucket后才能成功删除
//	 * 
//	 * @param client
//	 * @param bucketName
//	 */
//	public static void delete(OSSClient client, String bucketName) {
//		client.deleteBucket(bucketName);
//	}
//
//	/**
//	 * Bucket权限控制 
//	 * 
//	 * public-read-write:
//	 * 任何人（包括匿名访问）都可以对该bucket中的object进行上传、下载和删除操作；所有这些操作产生的费用由该bucket的创建者承担
//	 * ，请慎用该权限。<br>
//	 * public-read: 只有该bucket的创建者可以对该bucket内的Object进行写操作（包括上传和删除）；任何人（包括匿名访问）
//	 * 可以对该bucket中的object进行读操作。<br>
//	 * private: 只有该bucket的创建者才可以访问此Bukcet。其他人禁止对此Bucket做任何操作。<br>
//	 * 
//	 * @param client
//	 * @param bucketName
//	 * @param acl<br>
//	 * 			CannedAccessControlList是枚举类型，包含三个值： Private 、 PublicRead 、 PublicReadWrite ，它们分别对应相关权限
//	 */
//	public static void setBucketAcl(OSSClient client, String bucketName, CannedAccessControlList acl) {
//		client.setBucketAcl(bucketName, acl);
//	}

}
