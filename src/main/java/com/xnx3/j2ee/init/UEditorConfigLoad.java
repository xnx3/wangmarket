package com.xnx3.j2ee.init;

import com.qikemi.packages.alibaba.aliyun.oss.properties.OSSClientProperties;
import com.xnx3.ConfigManagerUtil;
import com.xnx3.net.OSSUtil;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.func.Log;

/**
 * 初始化UEditor编辑器的一些配置
 * 初始化数据在 com.qikemi.packages.alibaba.aliyun.oss.properties.OSSClientProperties
 * @author 管雷鸣
 */
public class UEditorConfigLoad {
	
	public UEditorConfigLoad() {
		//首先判断一下当前使用的是那种附件存储模式
		if(AttachmentFile.isMode(AttachmentFile.MODE_ALIYUN_OSS)){
			//使用的阿里云oss进行存储
			new OSSUtil();
			OSSClientProperties.useStatus = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("UEditor.aliyunOSS.useOSS").equals("true");
			Log.log("UEditor used OSS : "+OSSClientProperties.useStatus);
			OSSClientProperties.bucketName = OSSUtil.bucketName;
			OSSClientProperties.key = OSSUtil.accessKeyId;
			OSSClientProperties.secret = OSSUtil.accessKeySecret;
			OSSClientProperties.autoCreateBucket = false;			//不自动创建Bucket，需手动创建
			
			String url = OSSUtil.url;
			int lastUrlx = url.lastIndexOf("/");
			if((lastUrlx+1) == url.length()){
				url = url.substring(0, lastUrlx);
			}
			
			//如果用的阿里云自带的外网域名，自动截取 endpoint
			if(url.indexOf(".aliyuncs.com") > -1){
				int start = url.indexOf("://")+3;
				int end = url.indexOf(".");
				if(url.substring(start,end).equals(OSSUtil.bucketName)){
					OSSClientProperties.ossCliendEndPoint = url.substring(0,start)+url.substring(end+1,url.length());
				}
			}else{
				OSSClientProperties.ossCliendEndPoint = url;
			}
			OSSClientProperties.ossEndPoint = url;
			OSSClientProperties.useCDN = false;		//默认为false，反正这几个point都一样，只要xnx3Config.xml 的 aliyunOSS.url 配置上CDN的请求域名，便是用cdn
			OSSClientProperties.cdnEndPoint = url;
			
			OSSClientProperties.useLocalStorager = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("UEditor.aliyunOSS.useLocalStorager").equals("true");
//			OSSClientProperties.uploadBasePath = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("UEditor.aliyunOSS.uploadBasePath");
			OSSClientProperties.astrictUpload = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("UEditor.aliyunOSS.astrictUpload").equals("true");
			OSSClientProperties.useAsynUploader = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("UEditor.aliyunOSS.useAsynUploader").equals("true");
			OSSClientProperties.astrictUploadMessage = ConfigManagerUtil.getSingleton("systemConfig.xml").getValue("UEditor.aliyunOSS.astrictUploadMessage");
			
			Log.info("UEditor 使用阿里云OSS作为文件、附件存储");
			Log.info("load ueditor config , OSSClientProperties.astrictUpload : "+OSSClientProperties.astrictUpload);
		}else if (AttachmentFile.isMode(AttachmentFile.MODE_LOCAL_FILE)) {
			//使用本服务器进行存储
			
			OSSClientProperties.useStatus = false;		//使用服务器存储，那此处设置为false，不使用阿里云
			Log.info("UEditor 使用服务器本身磁盘作为文件、附件存储");
		}
	}
	
	public static void main(String[] args) {
		new UEditorConfigLoad();
	}
}
