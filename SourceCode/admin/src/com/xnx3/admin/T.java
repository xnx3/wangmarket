package com.xnx3.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xnx3.j2ee.Global;


public class T {
	public static void main(String[] args){
		JSONObject json = new JSONObject();
		
		JSONArray list = new JSONArray();
		
		JSONObject markdown = new JSONObject();
		markdown.put("name", "markdown");
		markdown.put("info", "markdown编辑器。在内容管理中，可使用此编辑器，快速，方便写出漂亮的文档、详情页面。");
		
		list.add(markdown);
		
		json.put("list", list);
		
		System.out.println(json.toString());
	}
}