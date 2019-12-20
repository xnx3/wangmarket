package com.xnx3.wangmarket.admin.vo;

import java.util.ArrayList;
import java.util.List;

import com.xnx3.j2ee.vo.BaseVO;

/**
 * 模板还原，预览表单选好要还原好的项提交，进行还原操作
 * 这里便是将还原提交的form的check参数进行分析，取出具体要还原的那些项
 * @author 管雷鸣
 *
 */
public class RestoreTemplateSubmitCheckDataVO extends BaseVO {

	private List<String> templatePageNameList, templateVarNameList, siteColumnCodeNameList, inputModelCodeNameList;
	
	/**
	 * 格式化get传入的数据
	 * @param paramList post传入的集合，list《某个要还选的项的名字，如 templatePage_index》
	 */
	public RestoreTemplateSubmitCheckDataVO(List<String> paramList) {
		templatePageNameList = new ArrayList<String>();
		templateVarNameList = new ArrayList<String>();
		siteColumnCodeNameList = new ArrayList<String>();
		inputModelCodeNameList = new ArrayList<String>();
		
//		if(queryString != null && queryString.length() > 0){
//			String[] pArray = queryString.split("&");
//			for (int i = 0; i < pArray.length; i++) {
//				if(pArray[i].indexOf("=") > 0){
//					String[] paramArray = pArray[i].split("=");
//					String key = paramArray[0];
//					String value = paramArray[1];
//					
//					//值为选中状态，value为1
//					if(value.equals("1")){
//						if(key.indexOf("templatePage_") == 0){
//							templatePageNameList.add(key.replace("templatePage_", ""));
//						}else if (key.indexOf("templateVar_") == 0) {
//							templateVarNameList.add(key.replace("templateVar_", ""));
//						}else if (key.indexOf("siteColumn_") == 0) {
//							siteColumnCodeNameList.add(key.replace("siteColumn_", ""));
//						}else if (key.indexOf("inputModel_") == 0) {
//							inputModelCodeNameList.add(key.replace("inputModel_", ""));
//						}
//					}
//					
//				}
//			}
//			
//			
//		}
		
		for (int i = 0; i < paramList.size(); i++) {
			String key = paramList.get(i);
			if(key.indexOf("templatePage_") == 0){
				templatePageNameList.add(key.replace("templatePage_", ""));
			}else if (key.indexOf("templateVar_") == 0) {
				templateVarNameList.add(key.replace("templateVar_", ""));
			}else if (key.indexOf("siteColumn_") == 0) {
				siteColumnCodeNameList.add(key.replace("siteColumn_", ""));
			}else if (key.indexOf("inputModel_") == 0) {
				inputModelCodeNameList.add(key.replace("inputModel_", ""));
			}
		}
	}
	
	public List<String> getTemplatePageNameList() {
		return templatePageNameList;
	}
	public List<String> getTemplateVarNameList() {
		return templateVarNameList;
	}

	public List<String> getSiteColumnCodeNameList() {
		return siteColumnCodeNameList;
	}

	public List<String> getInputModelCodeNameList() {
		return inputModelCodeNameList;
	}

}
