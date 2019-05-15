<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="编辑角色对应资源的授权"/>
</jsp:include>
<style>
.iw_table tbody tr td div{
	margin-top:3px; 
	margin-bottom: 3px;
} 
</style>

<form action="saveRolePermission.do" method="post" class="layui-form">
	<input type="hidden" value="${role.id }" name="roleId" />
	
	<div style="padding:16px; padding-left:130px; font-size:19px;">
		编辑&nbsp; <b>${role.name }</b> &nbsp;所拥有的权限
	</div>
	
	<table class="layui-table iw_table">
	  <thead>
	    <tr>
			<th>一级权限(功能模块)</th>
	        <th>二级权限(具体功能点)</th>
	    </tr> 
	  </thead>
	  <tbody>
	  	<c:forEach items="${list}" var="permissionTree">
	  		<tr>
				<td style="width:150px;">
					<span id="pid${permissionTree.permissionMark.permission.id }">
						<input type="checkbox" lay-skin="primary" name="permission" value="${permissionTree.permissionMark.permission.id }" title="${permissionTree.permissionMark.permission.name }" <c:if test="${permissionTree.permissionMark.selected==true }"> checked</c:if>>
		        	</span>
		        	<script>
						//鼠标跟随提示
						$(function(){
							var pid${permissionTree.permissionMark.permission.id }_index = 0;
							$("#pid${permissionTree.permissionMark.permission.id }").hover(function(){
								pid${permissionTree.permissionMark.permission.id }_index = layer.tips('percode&nbsp;:&nbsp;${permissionTree.permissionMark.permission.percode }<br/>${permissionTree.permissionMark.permission.description }', '#pid${permissionTree.permissionMark.permission.id }', {
									tips: [2, '#0FA6A8'], //还可配置颜色
									time:0,
									tipsMore: true,
									area : ['280px' , 'auto']
								});
							},function(){
								layer.close(pid${permissionTree.permissionMark.permission.id }_index);
							})
						});	
					</script>
				</td>
		        <td>
		        	<c:forEach items="${permissionTree.list }" var="permissionMark">
		        		<span id="pid${permissionMark.permission.id }">
							<input type="checkbox" name="permission" title="${permissionMark.permission.name }" <c:if test="${permissionMark.selected==true }"> checked</c:if> value="${permissionMark.permission.id }">
						</span>
						<script>
							//鼠标跟随提示
							$(function(){
								//栏目名称
								var pid${permissionMark.permission.id }_index = 0;
								$("#pid${permissionMark.permission.id }").hover(function(){
									pid${permissionMark.permission.id }_index = layer.tips('percode&nbsp;:&nbsp;${permissionMark.permission.percode }<br/>${permissionMark.permission.description }', '#pid${permissionMark.permission.id }', {
										tips: [2, '#0FA6A8'], //还可配置颜色
										time:0,
										tipsMore: true,
										area : ['280px' , 'auto']
									});
								},function(){
									layer.close(pid${permissionMark.permission.id }_index);
								})
							});	
						</script>
					</c:forEach>
		        </td>
		    </tr>
		</c:forEach>
	  </tbody>
	</table>
	<br/>
	<div class="layui-form-item">
	    <div class="layui-input-block" style="padding-left:100px;">
	      <button class="layui-btn" lay-submit lay-filter="formDemo" style="margin-right:50px;">保存</button>
	      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
	    </div>
	</div>
</form>
<script>
//Demo
layui.use('form', function(){
  var form = layui.form;
  //监听提交
  form.on('submit(formDemo)', function(data){
    return true;
  });
});
</script>

<jsp:include page="../../common/foot.jsp"></jsp:include>