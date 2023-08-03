<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="编辑模版页面"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>TemplatePage_type.js"></script>
<script src="/<%=Global.CACHE_FILE %>TemplatePage_editMode.js"></script>

<style>
.title{
	width:170px;
	text-align:center;
}
.value{
}
</style>
<div style="margin:30px;">
<table class="layui-table">
<tbody>
	<tr id="td_name">
		<td class="title">公司名称</td>
		<td class="value" onclick="popAgency('name','公司名称', '${agency.name }');" style="cursor:pointer;">
			<span class="ignore">${agency.name }</span>
			<button class="layui-btn layui-btn-primary layui-btn-xs" style="margin-left:15px;"><i class="layui-icon layui-icon-edit"></i></button>
		</td>
	</tr>
	<tr id="td_phone">
		<td class="title">联系电话</td>
		<td class="value" onclick="popAgency('phone','联系电话', '${agency.phone }');" style="cursor:pointer;">
			<span class="ignore">${agency.phone }</span>
			<button class="layui-btn layui-btn-primary layui-btn-xs" style="margin-left:15px;"><i class="layui-icon layui-icon-edit"></i></button>
		</td>
	</tr>
	<tr id="td_phone">
		<td class="title">联系QQ</td>
		<td class="value" onclick="popAgency('qq','联系QQ', '${agency.qq }');" style="cursor:pointer;">
			<span class="ignore">${agency.qq }</span>
			<button class="layui-btn layui-btn-primary layui-btn-xs" style="margin-left:15px;"><i class="layui-icon layui-icon-edit"></i></button>
		</td>
	</tr>
	<tr id="td_address">
		<td class="title">办公地点</td>
		<td class="value" onclick="popAgency('address','办公地点', '${agency.address }');" style="cursor:pointer;">
			<span class="ignore">${agency.address }</span>
			<button class="layui-btn layui-btn-primary layui-btn-xs" style="margin-left:15px;"><i class="layui-icon layui-icon-edit"></i></button>
		</td>
	</tr>
	<tr id="td_notice">
		<td class="title" >公告信息</td>
		<td class="value" onclick="popNotice();" style="cursor:pointer;" id="notice">${agencyData.notice }</td>
		<script>
			try{
				document.getElementById('notice').innerHTML = document.getElementById('notice').innerHTML.replace(/\n/g,"<br/>");
			}catch(e){}
		</script>
	</tr>
</tbody>
</table>
</div>

<div style="padding: 30px;color: gray;">
	<div>提示:</div>
	<div>鼠标放到某项上，即可看到该项的提示说明。</div>
</div>

<script>
//鼠标跟随提示
$(function(){
	//name
	// msg.tip({id:'',width:'',text:''})
	msg.tip({id:'td_name',width:'auto',direction:'bottom',text:'您的公司名。若没有，可输入您的姓名、或者您工作室的名字'})
	
	//phone
	msg.tip({id:'td_phone',width:'auto',direction:'bottom',text:'您的联系电话，会显示在您的客户的网站管理后台。'})
	
	//address
	msg.tip({id:'td_address',width:'auto',direction:'bottom',text:'您的办公地点，会显示在客户网站管理后台'})
	
	//notice
	msg.tip({id:'td_notice',width:'350px',direction:'bottom',text:'客户网站管理后台登陆成功后，欢迎页面显示的公告。若是公告内容少于两个字符，则不会在客户的后台显示公告。'})
	
});	

/**
 * 弹出修改窗口，修改 agency 的某个信息
 * @param name agency的数据表某列名字，如name、qq、address
 * @param description 描述，弹出窗口的说明信息
 * @param oldValue 旧的值，当前的值
 */
function popAgency(name, description, oldValue){
	layer.prompt({
		formType: 2,
		value: oldValue,
		title: description,
		area: ['400px', '100px'] //自定义文本域宽高
	}, function(value, index, elem){
		layer.close(index);
		parent.msg.loading('修改中');
	$.post(
		"saveAgency.do", 
		{ "name": name, "value":value }, 
		function(data){
			parent.msg.close();	//关闭“更改中”的等待提示
			if(data.result != '1'){
				parent.msg.failure(data.info);
			}else{
				parent.msg.success("操作成功");
				location.reload();
			}
		}, 
	"json");
	
	});
}


/**
 * 弹出修改窗口，修改notice公告信息
 * @param oldValue 旧的值，当前的值
 */
function popNotice(){
	layer.prompt({
		formType: 2,
		value: document.getElementById('notice').innerHTML.replace(/<br\/>/g, "\n").replace(/<br>/g, "\n"),
		title: '公告信息',
		area: ['500px', '200px'] //自定义文本域宽高
	}, function(value, index, elem){
		layer.close(index);
		parent.msg.loading('修改中');
	$.post(
		"saveNotice.do", 
		{ "value":value }, 
		function(data){
			console.log(data);
			parent.msg.close();	//关闭“更改中”的等待提示
			if(data.result != '1'){
				parent.msg.failure(data.info);
			}else{
				parent.msg.success("操作成功");
				location.reload();
			}
		}, 
	"json");
	
	});
}
</script>

<jsp:include page="/wm/common/foot.jsp"></jsp:include> 
<style> /* 显示多语种切换 */ .translateSelectLanguage{ display:block; } </style>