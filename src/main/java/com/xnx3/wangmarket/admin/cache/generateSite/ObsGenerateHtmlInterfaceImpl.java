package com.xnx3.wangmarket.admin.cache.generateSite;

import java.io.UnsupportedEncodingException;
import com.obs.services.exception.ObsException;
import com.xnx3.j2ee.util.AttachmentMode.hander.OBSHandler;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.net.HttpUtil;

/**
 * 默认的 GenerateHtmlInterface 接口的实现
 * @author 管雷鸣
 */
public class ObsGenerateHtmlInterfaceImpl implements GenerateHtmlInterface{
	private String accessKeyId = "";
	private String accessKeySecret = "";
	private String endpoint = "";
	private String bucketName = "";
	
	private static OBSHandler obsHandler;	//禁用，通过getObsUtil() 获取
	private static String obsBucketName; // 当前进行操作桶的名称
	
	
	public ObsGenerateHtmlInterfaceImpl(String accessKeyId, String accessKeySecret, String endpoint, String bucketName) {
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.endpoint = endpoint;
		this.bucketName = bucketName;
		
		if((this.accessKeyId == null || this.accessKeyId.length() == 0) && (this.accessKeySecret == null || this.accessKeySecret.length() == 0)) {
			//使用系统本身的obs
//			this.accessKeyId = SystemUtil.get("HUAWEIYUN_ACCESSKEYID");
//			this.accessKeySecret = SystemUtil.get("HUAWEIYUN_ACCESSKEYSECRET");
		}
	}
	
	/**
	 * 获取华为云OBS的操作类
	 * @return 当前华为云OBS的操作类型
	 */
	public OBSHandler getObsHander() {
		if(obsHandler == null) {
			obsHandler = new OBSHandler(accessKeyId,accessKeySecret,endpoint);
			// 如果设置过CDN的路径测设置为CDN路径，没有设置则为桶原生的访问路径
//			obsHandler.setUrlForCDN(SystemUtil.get("ATTACHMENT_FILE_URL"));
			// 在数据库中读取进行操作的桶的明恒
			obsHandler.setObsBucketName(bucketName);
			// 对桶名称进行当前类内缓存
			obsBucketName = obsHandler.getObsBucketName();
		}
		return obsHandler;
	}
	
	
	@Override
	public BaseVO putStringFile(String text, String path) {
		try {
			getObsHander().putStringFile(obsBucketName, path, text, HttpUtil.UTF8);
		} catch (ObsException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return BaseVO.success();
	}
}
