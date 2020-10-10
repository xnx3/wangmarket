<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="网站全局变量"/>
</jsp:include>
<style>
.var_name_class_0{
	width:155px;
}
.var_name_class_1{
	display:none;
}
.des_name_class_1, .des_name_class_0{
	display:none;
}
.add_var_1{
	display:none;
}
.del_var_1{
	display:none;
}
.tishi_1{
	margin-top:-10px;
}
.tishi_0{
	margin-top: -86px;
}
.property_button_1{
	display:none;
}
</style>

<div style="width:100%;height:100%; background-color: #fff; ">
	<table class="layui-table iw_table">
		<thead>
			<tr>
				<th class="var_name_class_${isSubAccount}">变量名</th>
				<th style="min-width:100px;">名称</th>
	            <th class="des_name_class_${isSubAccount}">说明</th>
	            <th>值</th>
	            <th>操作</th>
			</tr> 
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item">
	            <tr>
	                <td style="" class="var_name_class_${isSubAccount}">${item['name'] }</td>
	                <td style="min-width:100px;">${item['title'] }</td>
	                <td class="des_name_class_${isSubAccount}">${item['description'] }</td>
	                <td>
	                	<script>
	                		try{ ${item.valueItems} }catch(e){console.log(e);}
	                		var type = '${item['type']}';
	                		if(type == 'text' || type == 'number'){
								document.write('${item['value']}');
							}else if(type == 'image'){
								document.write('<a href="${item['value']}" target="_black"><img src="${item['value']}?x-oss-process=image/resize,h_29" style="height:29px; max-width:100px;" /></a>');
							}else if(type == 'imagegroup'){
								document.write('图片列表,点修改查看');
							}else if(type == 'select'){
								document.write(site_var_${item['name']}['${item['value']}']);
							}
	                	</script>
	                </td>
	                <td style="text-align:center; width:140px;">
	                	<button onclick="property('${item['name'] }');" class="layui-btn layui-btn-sm property_button_${isSubAccount}"><i class="layui-icon">&#xe614;</i></button>
	                	<botton class="layui-btn layui-btn-sm" onclick="edit('${item['name'] }');" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
	                	<botton class="layui-btn layui-btn-sm del_var_${isSubAccount}" onclick="deleteVar('${item['name'] }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
	                </td>
	            </tr>
	        </c:forEach>
		</tbody>
	</table>

	<div style="padding:15px;">
		<button class="layui-btn add_var_${isSubAccount}" onclick="property('');" style="margin-left: 10px;margin-bottom: 35px;"><i class="layui-icon" style="padding-right:8px; font-size: 22px;">&#xe608;</i>添加全局变量</button>
	</div>
	<div style="padding-right:15px; text-align: right;" class="tishi_${isSubAccount}">
		提示：&nbsp;&nbsp;&nbsp;
		<botton class=""><i class="layui-icon">&#xe614;</i></botton><span style="padding-left:12px;padding-right: 30px;">设置编辑属性</span>
		<botton class=""><i class="layui-icon">&#xe642;</i></botton><span style="padding-left:12px;padding-right: 30px;">编辑</span>
		<botton class=""><i class="layui-icon">&#xe640;</i></botton><span style="padding-left:12px;padding-right: 30px;">删除</span>
	</div>
	
</div>

<script>
layui.use('element', function(){
  var element = layui.element;
});
var laydate;
layui.use('laydate', function(){
  laydate = layui.laydate;
});


/**
 * 删除变量，传入要删除的变量的name
 */
function deleteVar(name){
	var dtv_confirm = layer.confirm('删除后不可恢复，您确定要删除此变量吗？', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtv_confirm);
		parent.msg.loading("删除中");
		$.post("/siteVar/deleteVar.do?name="+name, function(data){
			parent.msg.close();
			if(data.result == '1'){
				parent.msg.success("删除成功");
				location.reload();
	     	}else if(data.result == '0'){
	     		parent.msg.failure(data.info);
	     	}else{
	     		parent.msg.failure('操作失败');
	     	}
		});
    }, function(){
	});
}

/**
 * 新增，编辑变量
 * name 修改的变量，如果是新增，传入 '' 空字符串
 */
function edit(name){
	var url = '/siteVar/edit.do?editType=edit&name='+name;
	layer.open({
		type: 2, 
		title:'编辑变量内容', 
		area: ['490px', '620px'],
		shadeClose: true, //开启遮罩关闭
		content: url,
		closeBtn: 1
	});
}

/**
 * 编辑模板变量属性
 */
function property(name){
	var url = '/siteVar/edit.do?editType=property&name='+name;
	layer.open({
		type: 2, 
		title:'编辑变量属性', 
		area: ['490px', '620px'],
		shadeClose: true, //开启遮罩关闭
		content: url,
		closeBtn: 1
	});
}
</script>


</body>
</html>