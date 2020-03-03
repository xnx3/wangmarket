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
		${agency.name }
		<button class="layui-btn layui-btn-primary layui-btn-xs" style="margin-left:15px;"><i class="layui-icon layui-icon-edit"></i></button>
      </td>
    </tr>
    <tr id="td_phone">
      <td class="title">联系电话</td>
      <td class="value" onclick="popAgency('phone','联系电话', '${agency.phone }');" style="cursor:pointer;">
		${agency.phone }
		<button class="layui-btn layui-btn-primary layui-btn-xs" style="margin-left:15px;"><i class="layui-icon layui-icon-edit"></i></button>
      </td>
    </tr>
    <tr id="td_phone">
      <td class="title">联系QQ</td>
      <td class="value" onclick="popAgency('qq','联系QQ', '${agency.qq }');" style="cursor:pointer;">
		${agency.qq }
		<button class="layui-btn layui-btn-primary layui-btn-xs" style="margin-left:15px;"><i class="layui-icon layui-icon-edit"></i></button>
      </td>
    </tr>
    <tr id="td_address">
      <td class="title">办公地点</td>
      <td class="value" onclick="popAgency('address','办公地点', '${agency.address }');" style="cursor:pointer;">
		${agency.address }
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
	var td_name_index = 0;
	$("#td_name").hover(function(){
		td_name_index = layer.tips('您的公司名。若没有，可输入您的姓名、或者您工作室的名字', '#td_name', {
			tips: [3, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['380px' , 'auto']
		});
	},function(){
		layer.close(td_name_index);
	})
	
	//phone
	var td_phone_index = 0;
	$("#td_phone").hover(function(){
		td_phone_index = layer.tips('您的联系电话，会显示在您的客户的网站管理后台。', '#td_phone', {
			tips: [3, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['350px' , 'auto']
		});
	},function(){
		layer.close(td_phone_index);
	})
	
	//address
	var td_address_index = 0;
	$("#td_address").hover(function(){
		td_address_index = layer.tips('您的办公地点，会显示在客户网站管理后台', '#td_address', {
			tips: [3, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['350px' , 'auto']
		});
	},function(){
		layer.close(td_address_index);
	})
	
	//notice
	var td_notice_index = 0;
	$("#td_notice").hover(function(){
		td_notice_index = layer.tips('客户网站管理后台登陆成功后，欢迎页面显示的公告。若是公告内容少于两个字符，则不会在客户的后台显示公告。', '#td_notice', {
			tips: [3, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['350px' , 'auto']
		});
	},function(){
		layer.close(td_notice_index);
	})
	
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
	        parent.msg.close();    //关闭“更改中”的等待提示
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
	        parent.msg.close();    //关闭“更改中”的等待提示
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

<jsp:include page="../iw/common/foot.jsp"></jsp:include>