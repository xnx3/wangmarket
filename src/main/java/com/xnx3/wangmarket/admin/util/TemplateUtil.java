package com.xnx3.wangmarket.admin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import com.xnx3.Lang;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.net.HttpResponse;
import com.xnx3.net.HttpUtil;
import com.xnx3.wangmarket.admin.entity.Template;
import com.xnx3.wangmarket.admin.util.interfaces.TemplateUtilFileMove;

/**
 * 模版相关
 * @author 管雷鸣
 *
 */
public class TemplateUtil {
	/*
	 * 用户建立网站后，给用户使用的模版列表，这里仅只是同步的云端模版列表
	 * 在云端模版库同步完成后，会更新这两个存储
	 */
	public static Map<Integer, Map<String, Template>> cloudTemplateMapForType = new HashMap<Integer, Map<String, Template>>();	//key: template.type
	public static Map<String, Template> cloudTemplateMapForName = new HashMap<String, Template>();		//key: template.name
	
	/**
	 * 本地数据库中存储的模版名字。当 template 数据有更改时，会同时更新此处
	 * 
	 * 注意！！！！！！
	 * 此处应用 Redis ，不然分布式情况下，用不同的服务器，生成整站时，因找不到，会使用云端资源库。当然，使用不会有任何影响。但是私有云、局域网不联网的情况下，网站因使用云端资源，无法联网，将会导致无法加载css、js资源等问题。
	 * 
	 */
	public static Map<String, Template> databaseTemplateMapForName = new HashMap<String, Template>();	//key:template.name
	public static Map<Integer, Map<String, Template>> databaseTemplateMapForType = new HashMap<Integer, Map<String, Template>>();	//key:template.type
	
	/**
	 * 用户建立网站时，选择模版的时候，可用的模版。是集云端模版+本地存储的模版总和。
	 * 如果用户不用云端模版，那就只是使用本地模版
	 * 如果用户同时使用云端模版+本地模版，且模版编码同时在云端模版跟本地模版中都有，会优先使用本地模版。本地模版的优先级大于云端模版。
	 * map  key: template.name
	 */
	//public static Map<String, Template> useTemplateMap = new HashMap<String, Template>();
	//public static Map<Integer, Template>
	
	/**
	 * 更新内存中存储的数据库自定义模版缓存
	 * @param template 要更新的模版
	 */
	public static void updateDatabaseTemplateMap(Template template){
		if(template == null){
			return;
		}
		
		//判断是否是将共享的模版改为私有。也就是相当于从内存中，删除掉某个数据库模版
		if(template.getIscommon() - Template.ISCOMMON_NO == 0){
			//确实是改为私有，要从本地模版库中删除
			
			//因为是新导入，默认也是私有，先判断一下map中是否为null
			if(databaseTemplateMapForName.get(template.getName()) != null){
				databaseTemplateMapForName.remove(template.getName());
			}
			if(databaseTemplateMapForType.get(template.getType()) != null && databaseTemplateMapForType.get(template.getType()).get(template.getName()) != null){
				databaseTemplateMapForType.get(template.getType()).remove(template.getName());
			}

			return;
		}
		
		//判断 databaseTemplateMap 中是否有存储这个模版了，也就是判断当前是更新，还是添加模版
		Template temp = databaseTemplateMapForName.get(template.getName());
		if(temp == null){
			//不存在，那就是添加模版。直接加入进 databaseTemplateMap
			databaseTemplateMapForName.put(template.getName(), template);
			
			//加入进 databaseTemplateMapForType
			//判断一下是否有这个一级的key存在，若不存在，择要创建
			if(databaseTemplateMapForType.get(template.getType()) == null){
				databaseTemplateMapForType.put(template.getType(), new HashMap<String, Template>());
			}
			//将之加入进去
			databaseTemplateMapForType.get(template.getType()).put(template.getName(), template);
		}else{
			//已存在，那就是更新模版数据
			//直接更新 databaseTemplateMap
			databaseTemplateMapForName.put(template.getName(), template);
			
			//更新 databaseTemplateMapForType
			//先删除掉原先的template.name
			databaseTemplateMapForType.get(template.getType()).remove(template.getName());
			//再将新的加入
			databaseTemplateMapForType.get(template.getType()).put(template.getName(), template);
		}
	}
	
	/**
	 * 获取用户建立网站后，给用户使用的模版列表
	 * @param templateType 传入具体的值，若调取全部，则传入 -1
	 * @return
	 */
	public static Map<String,Template> getTemplateList(int templateType){
		Map<String,Template> map = new HashMap<String, Template>();
		//List<Template> list = null;
		
		//判断是否使用云端模版库。默认没有的话是使用。如果值0，则是不使用，否则都是使用
		String useCloudTemplate = SystemUtil.get("PLUGIN_TEMPLATECENTER_USE_ClOUDTEMPLATE");
		if(useCloudTemplate != null && useCloudTemplate.equals("0")){
			//不使用云端模版库
		}else{
			//先从云端模版库加载
			if(templateType > -1){
				//调取某个分类的
				mapClone(map, cloudTemplateMapForType.get(templateType));
			}else{
				//调取所有的
				mapClone(map, cloudTemplateMapForName);
			}
		}
		
		
		//再从本地模版库加载，本地跟云端有重复，将会覆盖掉云端模版库
		//判断一下是按分类取还是取所有模版
		if(templateType > -1){
			//调取某个分类的
			mapClone(map, databaseTemplateMapForType.get(templateType));
		}else{
			//调取所有的
			mapClone(map, databaseTemplateMapForName);
		}
		
		
		return map;
	}
	
	/**
	 * 通过 模版编码来取模版
	 * @param name 要取的模版的名字， template.name ，这里只是取云端同步过来的模版
	 * @return 如果不存在，返回 null
	 */
	public static Template getTemplateByName(String name){
		//先判断数据库本地是否有这个模版
		Template template = databaseTemplateMapForName.get(name);
		if(template == null){
			//本地模版库不存在，那么从云端获取
			template = cloudTemplateMapForName.get(name);
		}
		
		return template;
	}
	
	/**
	 * 获取template.wscso文件的内容，通过模版名
	 * @param name 模版名
	 * @return 
	 * 		<ul>
	 * 			<li>result:{@link BaseVO}.SUCCESS,则是成功，info返回template.wscso 文件的内容。</li>
	 * 			<li>result:{@link BaseVO}.FAILURE,则是失败，info返回失败原因</li>
	 * 		</ul>
	 */
	public static BaseVO getTemplateWscso(Template template){
		BaseVO vo = new BaseVO();
		
		if(template.getWscsoDownUrl() != null && template.getWscsoDownUrl().length() > 3){
			//有 wscso 下载地址，优先通过这个进行下载
			
			HttpUtil http = new HttpUtil(HttpUtil.UTF8);
			
			String wscsoUrl = template.getWscsoDownUrl();
			//判断是否加协议了，如果没有加，需要补齐协议
			if(wscsoUrl != null && wscsoUrl.indexOf("//") == 0){
				//需要补齐协议
				wscsoUrl = "http:"+wscsoUrl;
			}
			HttpResponse hr = http.get(wscsoUrl);
			if(hr.getCode() - 404 == 0){
				vo.setBaseVO(BaseVO.FAILURE, "模版不存在");
				return vo;
			}
			vo.setInfo(hr.getContent());
		}else{
			//未指定 wscso 下载地址，则是通过本地模版库进行加载 template.wscsos
			
			String text = AttachmentUtil.getTextByPath("websiteTemplate/"+template.getName()+"/template.wscso");
			vo.setInfo(text);
		}
		
		return vo;
	}
	
	
	
	/**
	 * 模版中允许存在的后缀，安全的后缀文件
	 */
	public static final String[] allowSuffixArray = {"js","xml","swf", "css","png","jpg","bmp","jpeg","gif","eot","svg","ttf","woff","woff2","otf","wscso"};
	
	private String name;	//当前操作的模版的名字
	private TemplateUtilFileMove templateUtilFileMove;	//执行 filterTemplateFile() 方法进行文件转移，文件转移执行的命令。
	
	/**
	 * 若不使用 filterTemplateFile() 可用此创建对象
	 * @param name 模版名字
	 */
	public TemplateUtil(String name) {
		this.name = name;
	}
	
	/**
	 * 若要使用 filterTemplateFile() 。必须用此创建对象
	 * @param name 模版名字
	 * @param templateUtilFileMove
	 */
	public TemplateUtil(String name, TemplateUtilFileMove templateUtilFileMove) {
		this.name = name;
		this.templateUtilFileMove = templateUtilFileMove;
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
					
					try {
						templateUtilFileMove.move("websiteTemplate/"+name+"/"+jutiFile, new FileInputStream(subFile));
						//AttachmentUtil.put("websiteTemplate/"+name+"/"+jutiFile, new FileInputStream(subFile));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					//AttachmentUtil.put("websiteTemplate/"+name+"/"+mulu, subFile);
				}else{
					//违规
					//System.out.println("违规！受攻击了----"+subFile.getName()+"    --"+subFileSuffix);
					subFile.delete();
				}
			}
		}
		
		
	}
	
	
	/**
	 * Map 克隆
	 * @param mapOriginal 原始的map，最终组合而成的结果map
	 * @param mapAdd 要增加进入map主体的map，这是要增加合并的map
	 * @return 确实不用返回的。执行完 mapOriginal 就增加了
	 */
	public static Map<String, Template> mapClone(Map<String, Template> mapOriginal, Map<String, Template> mapAdd){
		if(mapAdd == null){
			return mapOriginal;
		}
		for (Map.Entry<String, Template> entry : mapAdd.entrySet()) { 
			mapOriginal.put(entry.getKey(), entry.getValue());
		}
		
		return mapOriginal;
	}
}

