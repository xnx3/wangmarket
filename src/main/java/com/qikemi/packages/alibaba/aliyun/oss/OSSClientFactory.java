package com.qikemi.packages.alibaba.aliyun.oss;

import com.aliyun.openservices.oss.OSSClient;
import com.qikemi.packages.alibaba.aliyun.oss.properties.OSSClientProperties;

/**
 * OSSClient是OSS服务的Java客户端，它为调用者提供了一系列的方法，用于和OSS服务进行交互<br>
 * 
 * @create date : 2014年10月28日 上午11:20:56
 * @Author XieXianbin<a.b@hotmail.com>
 * @Source Repositories Address: <https://github.com/qikemi/UEditor-for-aliyun-OSS>
 */
public class OSSClientFactory {

	private static OSSClient client = null;
	
	/**
	 * 新建OSSClient 
	 * 
	 * @return
	 */
	public static OSSClient createOSSClient(){
		if ( null == client){
			client = new OSSClient(OSSClientProperties.ossCliendEndPoint, OSSClientProperties.key, OSSClientProperties.secret);
//			logger.info("First CreateOSSClient success.");
		}
		return client;
	}

}
