<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="系统参数"/>
</jsp:include>

<table class="layui-table iw_table">
  <tbody>
	<tr>
		<td style="width:200px;"> 服务所在区域 ( End Point )  ：</td>
		<td style="width:160px;"><%=Global.get("HUAWEIYUN_COMMON_ENDPOINT") %></td>
		<td>
		设置：
		<select id="selectId" onchange="selectChange();">
			<option value="" >请选择服务区域</option>
			<option value="cn-north-4" >华北-北京四</option>
			<option value="cn-north-1" >华北-北京一</option>
			<option value="cn-east-2" >华东-上海二</option>
			<option value="cn-east-3" >华东-上海三</option>
			<option value="cn-south-1" >华南-广州</option>
			<option value="cn-southwest-2" >西南-贵阳一</option>
			<option value="ap-southeast-1" >亚太-香港</option>
			<option value="ap-southeast-3" >亚太-新加坡</option>
			<option value="ap-southeast-2" >亚太-曼谷</option>
			</select>
		</td>
	</tr>
  </tbody>
</table>

<script type="text/javascript">
	//当选择区域之后，触发
	function selectChange(){
		var objS = document.getElementById("selectId");
		if(objS.value.length < 2){
			return;
		}
		
		parent.iw.loading("更改中");	//显示“更改中”的等待提示
		$.post(
			"/plugin/huaWeiYunServiceCreate/set/setAreaSave.do", 
			{ "area": objS.value}, 
				function(data){
				parent.iw.loadClose();	//关闭“更改中”的等待提示
				if(data.result != '1'){
					parent.iw.msgFailure(data.info);
				}else{
					parent.iw.msgSuccess("操作成功");
					window.location.href="../index.do";
				}
			}, 
		"json");
	}
</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include> 