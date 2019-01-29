<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="系统参数"/>
</jsp:include>

<jsp:include page="../../common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="变量名"/>
		<jsp:param name="iw_name" value="name"/>
	</jsp:include>
	<jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="描述"/>
		<jsp:param name="iw_name" value="description"/>
	</jsp:include>
	
	<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
	
	<div style="float: right;">
    	<script type="text/javascript"> orderBy('id_ASC=编号,name_ASC=变量名,lasttime_DESC=最后修改'); </script>
    </div>
    <a href="javascript:variable(0,'');" class="layui-btn layui-btn-normal" style="float: right; margin-right:10px;">添加变量</a>
</form>	

<table class="layui-table iw_table">
  <thead>
    <tr>
		<th>变量名</th>
        <th>值</th>
        <th>描述</th>
        <th>操作</th>
    </tr> 
  </thead>
  <tbody>
	<c:forEach items="${systemList}" var="system">
	   	<tr>
	   		<td>${system.name }</td>
	   		<td id="xnx3_${system.name }"><x:substring maxLength="10" text="${system.value }"></x:substring></td>
	        <td><x:substring maxLength="40" text="${system.description }"></x:substring></td>
	        <td style="width:110px;">
	        	<botton class="layui-btn layui-btn-sm" onclick="variable('${system.name }');" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
	        	<botton class="layui-btn layui-btn-sm" onclick="deleteVariable(${system.id }, '${system.name }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
	        </td>
	    </tr>
	    <script>
			//鼠标跟随提示
			$(function(){
				var xnx3_${system.name }_index = 0;
				$("#xnx3_${system.name }").hover(function(){
					xnx3_${system.name }_index = layer.tips('值&nbsp;:&nbsp;${system.value }<br/><hr class="layui-bg-gray">${system.description }', '#xnx3_${system.name }', {
						tips: [2, '#0FA6A8'], //还可配置颜色
						time:0,
						tipsMore: true,
						area : ['480px' , 'auto']
					});
				},function(){
					layer.close(xnx3_${system.name }_index);
				})
			});	
		</script>
	</c:forEach>
  </tbody>
</table>

<div style="padding: 20px;color: gray;">
	<div>操作按钮提示:</div>
	<div><i class="layui-icon">&#xe642;</i> &nbsp;：编辑操作，进行修改</div>
	<div><i class="layui-icon">&#xe640;</i> &nbsp;：删除操作，删除某项</div>
</div>
<div style="padding: 10px;color: gray;">
	<div>程序中调用，可以用此来调用这里的所有变量的值，返回String类型。如，要调取网站名称，其变量名为&nbsp; SITE_NAME &nbsp;，那么调取的Java代码为：</div>
	<div style="padding-left:30px;">com.xnx3.j2ee.Global.get("SITE_NAME");</div>
	<div>如果已经知道要取的值一定是int类型，那么可以用以下方式来直接取上面的，为int的值。当然，返回类型是Integer</div>
	<div style="padding-left:30px;">com.xnx3.j2ee.Global.getInt("USER_REG_ROLE");</div>
</div>

<script type="text/javascript">
//根据id删除系统变量
function deleteVariable(id,name){
	var dtp_confirm = layer.confirm('确定要删除系统变量“'+name+'”？若程序中有此变量的使用，删除后，当操作到用到此变量的页面时，会报错！', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtp_confirm);
		
		
		parent.iw.loading("删除中");    //显示“操作中”的等待提示
		$.post('/admin/system/deleteVariable.do?id='+id, function(data){
		    parent.iw.loadClose();    //关闭“操作中”的等待提示
		    if(data.result == '1'){
		        parent.iw.msgSuccess('删除成功');
		        window.location.reload();	//刷新当前页
		     }else if(data.result == '0'){
		         parent.iw.msgFailure(data.info);
		     }else{
		         parent.iw.msgFailure();
		     }
		});
		
	}, function(){
	});
}

/* 
 * 修改／新增变量
 * id 要修改的变量的id，若是为0，则是新增
 */
function variable(name){
	layer.open({
		type: 2, 
		title: name.length==0? '新增系统变量':'修改系统变量：&nbsp;&nbsp;'+name+'&nbsp;', 
		area: ['380px', '370px'],
		shadeClose: true, //开启遮罩关闭
		content: '/admin/system/variable.do?name='+name
	});
}
</script>

<jsp:include page="../../common/foot.jsp"></jsp:include> 