<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="编辑用户[${currentUser.nickname }]权限"/>
</jsp:include>

<form class="layui-form">
	<input type="hidden" id="userid" value="${currentUser.id }" name="userid" />
	
	<div class="layui-form-item">
		<div class="layui-input-block">
			<c:forEach items="${list}" var="roleMark">
				<input type="radio" name="role" value="${roleMark.role.id }" title="${roleMark.role.name }" <c:if test="${roleMark.selected==true }"> checked</c:if>>
				<br/>
			</c:forEach>
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit="" lay-filter="demo1">保存</button>
		</div>
	</div>
	<br/>
</form>

<script type="text/javascript">
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use(['form', 'layedit', 'laydate'], function(){
	var form = layui.form;
	
	//监听提交
  	form.on('submit(demo1)', function(data){
		$.showLoading('保存中');
		var userid = document.getElementById("userid").value;
		
		var roleObj;
	    roleObj=document.getElementsByName('role');
	    var roleValue = '';	//权限的值，如 1,2,3
	    if(roleObj!=null){
	        var i;
	        for(i=0;i<roleObj.length;i++){
	            if(roleObj[i].checked){
	                if(roleValue == ''){
	                	roleValue = roleObj[i].value;
	                }else{
	                	roleValue = roleValue + ',' + roleObj[i].value;
	                }    
	            }
	        }
	    }
    
		$.getJSON('saveUserRole.do?userid='+userid+'&role='+roleValue,function(obj){
			$.hideLoading();
			if(obj.result == '1'){
				parent.location.reload();	//刷新父窗口
				parent.layer.msg('操作成功', {time:1000});
				parent.layer.close(index);
	     	}else if(obj.result == '0'){
	     		 $.toast(obj.info, "cancel", function(toast) {});
	     	}else{
	     		alert(obj.result);
	     	}
		});
		
		return false;
	});	
});
</script>

<jsp:include page="../../common/foot.jsp"></jsp:include>