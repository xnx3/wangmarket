package com.xnx3.wangmarket.admin.cache;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import com.xnx3.DateUtil;
import com.xnx3.j2ee.util.AttachmentUtil;

/**
 * 所有js缓存生成的父类
 * @author 管雷鸣
 */
public class BaseCache {
	/**
	 * 生成js缓存文件的内容
	 */
	private String content;
	
	/**
	 * 生成的js存储数据的对象名，保存的文件名也是使用此有关联
	 */
	private String objName;	
	
	
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
	
	public void cacheAdd(int index,String value){
		content += objName+"["+index+"]='"+value+"'; ";
	}
	
	/**
	 * 生成js缓存文件保存
	 * @param siteid 站点id
	 * @param fileName 生成的js文件名字，只写文件名，不传入".js"后缀
	 */
	public void generateCacheFile(com.xnx3.wangmarket.admin.entity.Site site){
		try {
			content = content + " var xnx3_r"+DateUtil.timeForUnix10()+" = '"+getRandomValue()+"';";
			AttachmentUtil.put("site/"+site.getId()+"/data/"+objName+".js", new ByteArrayInputStream(content.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
	 * 增加一些常用的js函数
	 */
	public void addCommonJsFunction(){
		this.content+= "/*页面上输出选择框的所有option，显示到页面上*/ function writeSelectAllOptionFor"+this.objName+"(selectId){ var content = \"\"; if(selectId==''){ content = content + '<option value=\"\" selected=\"selected\">所有</option>'; }else{ content = content + '<option value=\"\">所有</option>'; } for(var p in "+this.objName+"){ if(p == selectId){ content = content+'<option value=\"'+p+'\" selected=\"selected\">'+"+this.objName+"[p]+'</option>'; }else{ content = content+'<option value=\"'+p+'\">'+"+this.objName+"[p]+'</option>'; } } document.write(content); }";
	}
	
	/**
	 * 向写出的js文件里增加内容
	 */
	public void appendContent(String content){
		this.content = this.content+" "+content;
	}
}

