package com.xnx3.j2ee.generateCache;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import com.xnx3.DateUtil;
import com.xnx3.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SystemUtil;

/**
 * 所有js缓存生成的父类
 * @author 管雷鸣
 */
public class BaseGenerate {
	
	/**
	 * 生成js缓存文件的内容
	 */
	public String content;
	
	/**
	 * 生成的js存储数据的对象名，保存的文件名也是使用此有关联
	 */
	public String objName;	
	
	/**
	 * 创建js对象
	 * @param objName js对象名（保存的js文件名、使用时引用的js对象名）
	 */
	public void createCacheObject(String objName){
		this.objName=objName;
		content="var "+objName+" = new Array(); ";
	}
	
	/**
	 * 往缓存js对象中增加键值对
	 * @param key 键
	 * @param value 值
	 */
	public void cacheAdd(Object key,Object value){
		content += objName+"['"+key+"']='"+value+"'; ";
	}
	
	/**
	 * 生成js缓存文件保存
	 */
	public void generateCacheFile(){
		addCommonJsFunction();
		initCacheFolder();
		String filePath = SystemUtil.getProjectPath()+Global.CACHE_FILE+getClass().getSimpleName()+"_"+objName+".js"; 
		try {
			content = content + " var xnx3_r"+DateUtil.timeForUnix10()+" = '"+getRandomValue()+"';";
			FileUtil.write(filePath, content,FileUtil.UTF8);
			ConsoleUtil.info("create cache js file success ! file path : "+filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.content=null;
	}
	
	/**
	 * 每个js生成时，会加入一个长度随即的，100个以内字符的随机变量，防止浏览器因字节数一样而缓存数据
	 * @return 长度不固定的String，100个字符以内
	 */
	private String getRandomValue(){
		Random r = new Random();
		int ri = r.nextInt(100);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ri; i++) {
			sb.append("1");
		}
		return sb.toString();
	}
	
	/**
	 * 初始化缓存文件夹，若根目录下没有缓存文件夹，自动创建
	 */
	private void initCacheFolder(){
		if(!FileUtil.exists(SystemUtil.getProjectPath()+Global.CACHE_FILE)){
			ConsoleUtil.info("create cache folder : "+ SystemUtil.getProjectPath()+Global.CACHE_FILE);
			String[] folders = Global.CACHE_FILE.split("/");
			String path = SystemUtil.getProjectPath();
			for (int i = 0; i < folders.length; i++) {
				if(folders[i].length()>0&&!FileUtil.exists(path+folders[i])){
					File file = new File(path+folders[i]);
					file.mkdir();
				}
				path = path+folders[i]+"/";
			}
		}
	}
	
	/**
	 * 增加一些常用的js函数
	 */
	public void addCommonJsFunction(){
		this.content+= "/*页面上输出选择框的所有option，显示到页面上*/ function writeSelectAllOptionFor"+this.objName+"(selectValue){ writeSelectAllOptionFor"+this.objName+"_(selectValue,'所有', false); } function writeSelectAllOptionFor"+this.objName+"_(selectValue,firstTitle,required){ var content = \"\"; if(selectValue==''){ content = content + '<option value=\"\" selected=\"selected\">'+firstTitle+'</option>'; }else{ content = content + '<option value=\"\">'+firstTitle+'</option>'; } for(var p in "+this.objName+"){ if(p == selectValue){ content = content+'<option value=\"'+p+'\" selected=\"selected\">'+"+this.objName+"[p]+'</option>'; }else{ content = content+'<option value=\"'+p+'\">'+"+this.objName+"[p]+'</option>'; } } document.write('<select name="+this.objName+" '+(required? 'required':'')+' lay-verify=\""+this.objName+"\" lay-filter=\""+this.objName+"\" id=\""+this.objName+"\">'+content+'</select>'); }";
	}
	
	/**
	 * 向写出的js文件里增加内容
	 */
	public void appendContent(String content){
		this.content = this.content+" "+content;
	}
}

