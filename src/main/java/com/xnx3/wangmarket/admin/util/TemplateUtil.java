package com.xnx3.wangmarket.admin.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xnx3.Lang;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.func.Log;
import com.xnx3.wangmarket.admin.entity.Template;

/**
 * 模版相关
 * @author 管雷鸣
 *
 */
public class TemplateUtil {
	/*
	 * 用户建立网站后，给用户使用的模版列表，这里仅只是同步的云端模版列表
	 * map -  key: 
	 */
	public static Map<Integer, List<Template>> cloudTemplateMap = new HashMap<Integer, List<Template>>();
	
	/**
	 * 本地数据库中存储的模版名字
	 * map -  key:template.name
	 */
	public static Map<String, Template> databaseTemplateMap = new HashMap<String, Template>();
	
	/**
	 * 获取用户建立网站后，给用户使用的模版列表
	 * @param templateType 传入具体的值，若调取全部，则传入 -1
	 * @return
	 */
	public static List<Template> getTemplateList(int templateType){
		List<Template> list = null;
		if(templateType > -1){
			//调取某个分类的
			list = cloudTemplateMap.get(templateType);
			if(list == null){
				list = new ArrayList<Template>();
			}
		}else{
			//调取所有的,便利map
			list = new ArrayList<Template>();
			
			for (Map.Entry<Integer, List<Template>> entry : cloudTemplateMap.entrySet()) {
				if(entry.getValue() != null && entry.getValue().size() > 0){
					//如果这个分类有，那么将其拿出来加入输出的list列表
					List<Template> oriList = entry.getValue();
					for (int i = 0; i < oriList.size(); i++) {
						list.add(oriList.get(i));
					}
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 通过 模版编码来取模版
	 * @param name 要取的模版的名字， template.name ，这里只是取云端同步过来的模版
	 * @return 如果不存在，返回 null
	 */
	public static Template getTemplateByName(String name){
		for (Map.Entry<Integer, List<Template>> entry : TemplateUtil.cloudTemplateMap.entrySet()) {
			if(entry.getValue() != null && entry.getValue().size() > 0){
				//如果这个分类有，那么将其拿出来加入输出的list列表
				List<Template> oriList = entry.getValue();
				for (int i = 0; i < oriList.size(); i++) {
					Template temp = oriList.get(i);
					if(name.equals(temp.getName())){
						return temp;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 模版中允许存在的后缀，安全的后缀文件
	 */
	public static final String[] allowSuffixArray = {"js","xml","swf", "css","png","jpg","bmp","jpeg","gif","eot","svg","ttf","woff","woff2","otf","wscso"};
	
	private String name;	//当前操作的模版的名字
	
	public TemplateUtil(String name) {
		this.name = name;
	}
	
	/**
	 * 过滤用户自己上传的模版资源文件，将不合法不安全的后缀全部删除掉
	 * @param file 要过滤检查的文件夹的文件
	 */
	public void filterTemplateFile(File file){
		File[] subFileList = file.listFiles();
		for (int i = 0; i < subFileList.length; i++) {
			File subFile = subFileList[i];
			if(subFile.isDirectory()){
				//是目录，那么继续进入
				filterTemplateFile(subFile);
			}else{
				//是文件，那么进行后缀判断
				String subFileSuffix = null;
				subFileSuffix = Lang.findFileSuffix(subFile.getName());
				boolean hefa = false;	//默认不合法
				if(subFileSuffix == null){
					//不合法
				}else{
					subFileSuffix = subFileSuffix.toLowerCase();
					for (int j = 0; j < allowSuffixArray.length; j++) {
						if(allowSuffixArray[j].equals(subFileSuffix)){
							hefa = true;
							break;
						}
					}
				}
				if(hefa){
					//合法，转移过去，转移到oss、附件等。
					String subFilePath = subFile.getPath(); 
					int index = subFilePath.indexOf("/classes/templateTemporaryFile/");
					String temp = subFilePath.substring(index+31, subFilePath.length());	//得到模版文件名+具体文件目录，如  qiye2/preview.jpg
					String jutiFile = temp.substring(temp.indexOf("/")+1, temp.length());	//得到模版文件夹内的具体文件目录，如 css/style.css
					
					int jutiIndex = jutiFile.lastIndexOf("/");
					String mulu = "";	//得到目录结构，相对与模版本身的
					if(jutiIndex>-1){
						//有文件结构，格式如  res/css/
						mulu = jutiFile.substring(0, jutiIndex+1);
					}
					Log.info("mulu----"+mulu);
					AttachmentFile.put("websiteTemplate/"+name+"/"+mulu, subFile);
				}else{
					//违规
					//System.out.println("违规！受攻击了----"+subFile.getName()+"    --"+subFileSuffix);
					subFile.delete();
				}
			}
		}
		
		
	}
	
}
