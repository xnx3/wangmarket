<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<jsp:include page="../../iw/common/head.jsp">
		<jsp:param name="title" value="首页"/>
	</jsp:include>
</head>
<body>

<table class="layui-table iw_table">
  <tbody>
	   	<!-- 华为云日志服务预留
	   	 <tr>
	   		<td>SLS日志服务</td>
	   		<td id="sls"></td>
		</tr> -->
		<tr>
	   		<td>OBS对象存储</td>
	   		<td id="obs"></td>
		</tr>
  </tbody>
</table>
<!-- <div onclick="deleteThis" style="border:1px; width:100%; height:80px; font-size:50px;">创建完毕？彻底删除此插件</div> -->

<script>
	//华为云日志服务预留方法
	<%-- var slsObj = document.getElementById('sls');
	if('<%=Global.get("HUAWEIYUN_SLS_USE")  %>' == '1'){
		slsObj.innerHTML = "已使用";
	}else{
		//未使用
		slsObj.innerHTML = '未使用&nbsp;&nbsp; <button onclick="createSLS();">创建此组建云服务并启用</button>';
	} --%>
	
	//OBS检测
	var obsObj = document.getElementById('obs');
	if('<%=Global.get("ATTACHMENT_FILE_MODE") %>' == 'huaWeiYunOBS'){
		obsObj.innerHTML = "已使用";
	}else{
		//未使用
		obsObj.innerHTML = '未使用&nbsp;&nbsp; <button onclick="createOBS();">创建此组建云服务并启用</button>';
	}

	//创建OBS服务及自动配置
	function createOBS(){
		parent.iw.loading("创建中");	//显示“更改中”的等待提示
		$.post(
			"/plugin/huaWeiYunServiceCreate/obs/create.do",  function(data){
				parent.iw.loadClose();	//关闭“更改中”的等待提示
				if(data.result != '1'){
					parent.iw.msgFailure(data.info);
				}else{
					parent.iw.msgSuccess("操作成功!记得更改cdn附件路径。");
					window.location.reload(); // 刷新当前页
				}
			}, 
		"json");
	}
	
	
	//华为云日志服务预留方法
	/* function createSLS(){
		parent.iw.loading("创建中");	//显示“更改中”的等待提示
		$.post(
			"/plugin/huaWeiYunServiceCreate/sls/create.do",  function(data){
				parent.iw.loadClose();	//关闭“更改中”的等待提示
				if(data.result != '1'){
					parent.iw.msgFailure(data.info);
				}else{
					parent.iw.msgSuccess("已创建SLS日志服务！1分钟后生效");
					window.onload();	//刷新页面
				}
			}, 
		"json");
	} */

</script>


<jsp:include page="../../iw/common/foot.jsp"></jsp:include>  