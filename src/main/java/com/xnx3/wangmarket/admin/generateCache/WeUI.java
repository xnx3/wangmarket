package com.xnx3.wangmarket.admin.generateCache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xnx3.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SystemUtil;

/**
 * WeUI的选择使用，创建js，方便一件调用
 * @author 管雷鸣
 */
public class WeUI {
	/**
	 * 生成的js存储数据的对象名，保存的文件名也是使用此有关联
	 */
	private String objName;	
	
	//select :  map.get("title")  map.get("value")
	private List<Map<String,String>> dataList;
	
	public WeUI() {
		this.dataList = new ArrayList<Map<String,String>>();
	}
	
	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}
	
	/**
	 * 向数据列表中追加数据
	 * select :  map.get("title")  map.get("value")
	 * @param dataMap
	 */
	public void appendDataList(Map<String, String> dataMap) {
		this.dataList.add(dataMap);
	}

	/**
	 * 同 {@link #appendDataList(Map)}
	 * @param title
	 * @param value
	 */
	public void appendDataList(String title,String value) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("title", title);
		map.put("value", value);
		this.dataList.add(map);
		
	}
	
	/**
	 * 生成js缓存文件保存
	 */
	public void generateCacheFile(){
		String fileName = getClass().getSimpleName()+"_"+objName;
		String filePath = SystemUtil.getProjectPath()+Global.CACHE_FILE+fileName+".js"; 
		String data = "";
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> map = dataList.get(i);
			if(data.length() > 0){
				data = data + ",";
			}
			data = data + "{title: \""+map.get("title")+"\",value: \""+map.get("value")+"\"}";
		}
		String content = "var "+fileName+" = ["+data+"];";
		try {
			ConsoleUtil.info("create cache js file success ! file path : "+filePath);
			FileUtil.write(filePath, content,FileUtil.UTF8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
