package com;

/**
 * 自动扫描指定的包下所有controller的json接口，根据其标准的JAVADOC注释，生成接口文档。  
 * 使用参考 https://gitee.com/leimingyun/dashboard/wikis/leimingyun/wm/preview?sort_id=4518712&doc_id=1101390
 * @author 管雷鸣
 */
public class ApiDoc {
	public static void main(String[] args) {
		new com.xnx3.doc.JavaDoc("com.xnx3.wangmarket.admin.controller").generateHtmlDoc();
	}
}
