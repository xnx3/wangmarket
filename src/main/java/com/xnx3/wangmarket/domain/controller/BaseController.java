package com.xnx3.wangmarket.domain.controller;


public class BaseController extends com.xnx3.j2ee.controller.BaseController{
	
	/**
	 * 进入404错误页面
	 * @return
	 */
	public String error404(){
		return "domain/404";
	}
	
}
