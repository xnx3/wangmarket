<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="绑定域名"/>
</jsp:include>


<style>
ul{
	padding:10px;
}
li{
	list-style-type: decimal;
	padding-bottom: 10px;
}
.shuoming{
	font-size: 12px; 
	color: gray;
}
</style>
<div style="padding-left: 25px;">
	<ul>
		<li>
			设置你想绑定的域名
			<div style="margin-top: 10px; ">
				<input type="text" style="width:250px; float: left;" name="domain" id="domain" lay-verify="domain" autocomplete="off" placeholder="输入您的域名" class="layui-input" value="${site.bindDomain }">
				<button onclick="submitButton();" class="layui-btn">立即绑定</button>
			</div>
			<div class="shuoming">填写格式如： www.guanleiming.com</div>
		</li>
		<li>
			将上面绑定的域名，做 CNAME 记录解析至：
			<br/> <div style="padding-left:10px;">domain.<script>document.write(parent.autoAssignDomain);</script></div>
			
		</li>
		<li>
			等待十分钟，访问域名，便可见效果
		</li>
	</ul>
	
	<div style="color:gray;  padding-bottom: 5px; font-size:14px;">
		提示：
		<br/>&nbsp;1. 中文域名无法绑定
		<br/>&nbsp;2. 如果您想解除域名绑定，可<a href="javascript:parent.bindDomain('');" style="padding:2px; cursor:pointer;">点击此处取消绑定</a>
	</div>
</div>


<script>
try{
	//自适应弹出层大小
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.iframeAuto(index);
}catch(err){}


//点击保存的按钮
function submitButton(){
	if(document.getElementById('domain').value.length == 0){
		parent.iw.msgFailure("请输入要绑定的域名");
		return;
	}
	//调用 commonedit.js的函数
	parent.bindDomain(document.getElementById('domain').value);
}
</script>

</body>
</html>