package com.baidu.ueditor.upload;

import java.io.File;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.qikemi.packages.alibaba.aliyun.oss.properties.OSSClientProperties;
import com.qikemi.packages.baidu.ueditor.upload.AsynUploaderThreader;
import com.qikemi.packages.baidu.ueditor.upload.SynUploader;
import com.qikemi.packages.utils.SystemUtil;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SessionUtil;

/**
 * 同步上传文件到阿里云OSS<br>
 * 
 * @create date : 2014年10月28日 上午22:15:00
 * @Author XieXianbin<a.b@hotmail.com>
 * @Source Repositories Address:
 *         <https://github.com/qikemi/UEditor-for-aliyun-OSS>
 */
public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
	}

	public State doExecss(){
		State state = null;
		state = BinaryUploader.save(this.request, this.conf);
		JSONObject stateJson = new JSONObject(state.toJSONString());
//		String bucketName = OSSClientProperties.bucketName;
//		OSSClient client = OSSClientFactory.createOSSClient();
		if (OSSClientProperties.useAsynUploader) {
			ConsoleUtil.debug(state.toJSONString());
			AsynUploaderThreader asynThreader = new AsynUploaderThreader();
			asynThreader.init(stateJson, this.request);
			Thread uploadThreader = new Thread(asynThreader);
			uploadThreader.start();
		} else {
			ConsoleUtil.debug(state.toJSONString());
			SynUploader synUploader = new SynUploader();
			synUploader.upload(stateJson, this.request);
		}
		if (false == OSSClientProperties.useLocalStorager) {
			String uploadFilePath = (String) this.conf.get("rootPath") + (String) stateJson.get("url");
			File uploadFile = new File(uploadFilePath);
			if (uploadFile.isFile() && uploadFile.exists()) {
				uploadFile.delete();
			}
		}
		state.putInfo("url", OSSClientProperties.ossEndPoint + stateJson.getString("url"));
		ConsoleUtil.debug("state.url : "+OSSClientProperties.ossEndPoint + stateJson.getString("url"));
		ConsoleUtil.debug("state.url : "+state.toJSONString());
		return state;
	}
	
	public final State doExec() {
		ConsoleUtil.debug("doExec--");
		State state = null;
		//是否有限制某用户上传
		if(OSSClientProperties.astrictUpload){
			if(!SessionUtil.isAllowUploadForUEditor()){
				return new BaseState(false, OSSClientProperties.astrictUploadMessage);
			}
		}
		
		String filedName = (String) this.conf.get("fieldName");
		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName),
					this.conf);
			ConsoleUtil.debug("doExec--isBase64--"+state.toJSONString());
		} else {
			ConsoleUtil.debug("doExec--not isBase64");
			state = BinaryUploader.save(this.request, this.conf);
			JSONObject stateJson = new JSONObject(state.toJSONString());
			
			ConsoleUtil.debug("doExec--OSSClientProperties.useStatus: "+OSSClientProperties.useStatus);
			// 判别云同步方式
			if (OSSClientProperties.useStatus) {
				// upload type
				ConsoleUtil.debug("doExec--OSSClientProperties.useAsynUploader: "+OSSClientProperties.useAsynUploader);
				if (OSSClientProperties.useAsynUploader) {
					AsynUploaderThreader asynThreader = new AsynUploaderThreader();
					asynThreader.init(stateJson, this.request);
					Thread uploadThreader = new Thread(asynThreader);
					uploadThreader.start();
				} else {
					SynUploader synUploader = new SynUploader();
					synUploader.upload(stateJson, this.request);
				}

				// storage type
				if (false == OSSClientProperties.useLocalStorager) {
					String uploadFilePath = (String) this.conf.get("rootPath") + (String) stateJson.get("url");
					File uploadFile = new File(uploadFilePath);
					if (uploadFile.isFile() && uploadFile.exists()) {
						uploadFile.delete();
					}
				}
				
				String uploadPath = stateJson.getString("url"); //上传的路径，如  /site/219/news/20191119/2234234.png
				if(uploadPath.indexOf("/") == 0){
					//如果最开始是 / ，那么判断一下 OSSClientProperties.ossEndPoint 中，域名最后是否加 / 了，如果加了，那么uploadPath 最开头的的这个/去掉
					if(OSSClientProperties.ossEndPoint.lastIndexOf("/")+1 == OSSClientProperties.ossEndPoint.length()){
						uploadPath = uploadPath.substring(1, uploadPath.length());
					}
				}
				state.putInfo("url", OSSClientProperties.ossEndPoint + uploadPath);
				ConsoleUtil.debug("doExec--上传到对象存储： "+OSSClientProperties.ossEndPoint + uploadPath);
			} else {
				//绝对路径，而非原本的相对路径
				String stateStr = stateJson.getString("state");
				if(stateStr.equals("SUCCESS")){
					state.putInfo("url",  AttachmentUtil.netUrl() + SystemUtil.getProjectName() + stateJson.getString("url"));
					ConsoleUtil.debug("doExec--上传服务器磁盘： "+AttachmentUtil.netUrl() + SystemUtil.getProjectName() + stateJson.getString("url"));
				}else{
					//不成功，忽略。自然会在客户端弹出提示
				}
			}
		}
		/*
		 * { "state": "SUCCESS", "title": "1415236747300087471.jpg", "original":
		 * "a.jpg", "type": ".jpg", "url":
		 * "/upload/image/20141106/1415236747300087471.jpg", "size": "18827" }
		 */
		ConsoleUtil.debug("doExec--最后返回："+state.toJSONString());
		return state;
	}
}
