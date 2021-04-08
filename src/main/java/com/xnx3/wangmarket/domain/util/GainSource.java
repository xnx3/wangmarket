package com.xnx3.wangmarket.domain.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.xnx3.ClassUtil;
import com.xnx3.FileUtil;
import com.xnx3.Lang;
import com.xnx3.cache.JavaUtil;
import com.xnx3.j2ee.util.ApplicationPropertiesUtil;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.AttachmentMode.LocalServerMode;
import com.xnx3.net.HttpUtil;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.util.WangmarketDataUtil;
import com.xnx3.wangmarket.domain.bean.SimpleSite;
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
	public static LocalServerMode localServerMode;	//本地文件操作的工具类
	public static int cacheToMemorySize = 10000;	//将文本文件缓存到内存中条件，当文件字符数量不超过多大，才会缓存到内存中。超过这个大小将缓存到磁盘中。这里10000，约等于10KB， 单位是字符的数量
	
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
		localServerMode = new LocalServerMode();
		
		String sizeValue = ApplicationPropertiesUtil.getProperty("wangmarket.domain.cacheToMemorySize");
		if(sizeValue != null && sizeValue.length() > 0) {
			cacheToMemorySize = Lang.stringToInt(sizeValue, 10);
			ConsoleUtil.info("load wangmarket.domain.cacheToMemorySize : "+cacheToMemorySize);
		}
		
	}
	
	/**
	 * 获取文件的文本格式
	 * @param simpleSite 要访问的站点
	 * @param path 传入要访问的文件名，如 index.html、 robots.txt、 sitemap.xml 等，这些文件都是在 site/123/ 下的。（5.3及以前是传入如 site/219/index.html）
	 * @return 源代码 。若返回null，则是文件不存在，404
	 */
	public static TextBean get(SimpleSite simpleSite, String fileName){
		if(simpleSite == null) {
			return null;
		}
		if(AttachmentUtil.isMode(AttachmentUtil.MODE_LOCAL_FILE)) {
			//使用的本地存储，没有使用云存储，那么不需要走缓存
			TextBean tb = new TextBean();
			tb.setText(AttachmentUtil.getTextByPath("site/"+simpleSite.getSiteid()+"/"+fileName));
			return tb;
		}
		
		/****** 下面使用了云存储，比如oss、obs、或其他扩展存储等，那么会走缓存层 *******/
		/*
		 * 缓存层文件大于20KB的，缓存再服务器本地磁盘
		 * 小于20KB的，缓存在内存中。
		 */
		
		TextBean bean;
		Map<String, TextBean> map;
		
		Object obj = JavaUtil.get("site:"+simpleSite.getSiteid());
		if(obj != null) {
			//这个站点已经有过缓存了
			
			map = (Map<String, TextBean>) obj;
			bean = map.get(fileName);
			if(bean != null) {
				//已经有过缓存了，直接将缓存中的返回
				//判断是在内存缓存中，还是再服务器文件缓存中
				if(bean.isExistMenory()) {
					//内存中，直接拿出来返回
					ConsoleUtil.debug("read by java map cache : "+fileName+", siteid:"+simpleSite.getSiteid());
					return bean;
				}else {
					//再文件缓存中，读出来
					//检测缓存文件夹是否存在，如果不存在，则创建
					directoryInit(simpleSite.getSiteid());	
					//从缓存文件夹中取缓存的这个文件
					String text = FileUtil.read(WangmarketDataUtil.getWangmarketDataRootPath()+"domain_io_cache/site/"+simpleSite.getSiteid()+"/"+fileName, FileUtil.UTF8);
					if(text != null && text.length() == 0){
						text = null;
					}
					
					if(text != null) {
						//克隆一个，返回
						TextBean returnTextBena = new TextBean();
						returnTextBena.setText(text);
						ConsoleUtil.debug("read by local server cache : "+fileName+", siteid:"+simpleSite.getSiteid());
						return returnTextBena;
					}
					
					//因为是超过多少KB不能放入内存，才会放入到服务器本地磁盘缓存，如果为空，肯定是缓存文件被删掉了，那么重新从云存储中拉最新的文件下来
					ConsoleUtil.log("缓存文件被删掉了，那么重新从云存储中拉最新的文件下来。 文件: "+"domain_io_cache/site/"+simpleSite.getSiteid()+"/"+fileName);
				}
			}
		}else {
			//这个网站目前没在缓存中，直接通过io取
			map = new HashMap<String, TextBean>();
		}
		
		ConsoleUtil.debug("read by 云存储 : "+fileName+", siteid:"+simpleSite.getSiteid());
		String text = AttachmentUtil.getTextByPath("site/"+simpleSite.getSiteid()+"/"+fileName);
		
		bean = new TextBean();
		
		//加入缓存
		//判断是加入内存缓存，还是加入磁盘缓存
		if(text != null && text.length() > cacheToMemorySize) {
			System.out.println(fileName+" size: "+text.length());
			//超过10KB，那么存到磁盘中
			
			//检测缓存文件夹是否存在，如果不存在，则创建
			directoryInit(simpleSite.getSiteid());	
			//写入txt缓存文件
			try {
				FileUtil.write(WangmarketDataUtil.getWangmarketDataRootPath()+"domain_io_cache/site/"+simpleSite.getSiteid()+"/"+fileName, text, FileUtil.UTF8);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			bean.setExistMenory(false);
		}else {
			//不超过10KB，存到内存中
			bean.setExistMenory(true);
			bean.setText(text);
		}
		
		map.put(fileName, bean);
		if(obj == null) {
			JavaUtil.set("site:"+simpleSite.getSiteid(), map);
		}
		
		if(bean.isExistMenory()) {
			//是加入到了内存中，那么text文本就在bean里面，直接返回
			return bean;
		}else {
			//加入到了本地磁盘的文件中，那么text文本不在缓存的bean里面，克隆一个
			TextBean returnTextBean = new TextBean();
			returnTextBean.setText(text);
			return returnTextBean;
		}
	}
	
	
	/**
	 * 自动判断这个缓存文件夹是否存在，如果不存在，那么创建一个
	 * @param siteid {@link Site}.id
	 */
	public static void directoryInit(int siteid) {
		//domain_io_cache 文件夹
		String cachePath = WangmarketDataUtil.getWangmarketDataRootPath()+"domain_io_cache/";
		if(!FileUtil.exists(cachePath)) {
			new File(cachePath).mkdir();
		}
		
		//site 文件夹
		cachePath = cachePath+"site/";
		if(!FileUtil.exists(cachePath)) {
			new File(cachePath).mkdir();
		}
		
		//siteid 文件夹
		cachePath = cachePath+siteid+"/";
		if(!FileUtil.exists(cachePath)) {
			new File(cachePath).mkdir();
		}
	}
}
