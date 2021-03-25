package com.xnx3.wangmarket.domain.util;

import com.xnx3.ClassUtil;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.CacheUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.wangmarket.domain.bean.TextBean;

/**
 * 获取html源代码的工具类
 * @author 管雷鸣
 *
 */
public class GainSource {
	public static HttpUtil httpUtil;
	public static boolean isDomain;	//当前是否是单独运行的domain项目
	public static final String jiasuDomain = "http://cdn.weiunity.com/";	//单独部署domain项目，获取html源代码的全球加速域名
	
	static{
		httpUtil = new HttpUtil();
		if(ClassUtil.classExist("com.xnx3.wangmarket.admin.controller.TemplateController")){
			//存在 TemplateController ，那肯定就是主项目，在管理后台的
			isDomain = false;
			ConsoleUtil.log("isDomain = false");
		}else{
			isDomain = true;
			ConsoleUtil.log("isDomain = true");
		}
	}
	
	/**
	 * 获取文件的文本格式
	 * @param path 传入如 site/219/index.html
	 * @return 源代码 。若返回null，则是文件不存在，404
	 */
	public static TextBean get(String path){
		TextBean bean;
		//先从缓存中取
		Object obj = CacheUtil.get("path:"+path);
		if(obj == null) {
			System.out.println("read oss : "+path);
			String text = AttachmentUtil.getTextByPath(path);
			bean = new TextBean();
			bean.setText(text);
			
			//加入缓存，缓存时间是3秒
			CacheUtil.set("path:"+path, bean, 3);
		}else {
			System.out.println("read cache : "+path);
			bean = (TextBean) obj;
		}
		
		return bean;
		
//		if(isDomain){
//			//网站管理后台
//			return AttachmentUtil.getTextByPath(path);
//		}else{
//			//单独部署的domain项目
//			HttpResponse hr = httpUtil.get(jiasuDomain+path);
//			if(hr.getCode() - 200 == 0){
//				return hr.getContent();
//			}else{
//				return null;
//			}
//		}
	}
	
}
