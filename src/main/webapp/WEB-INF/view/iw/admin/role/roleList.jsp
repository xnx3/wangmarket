<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="角色列表"/>
</jsp:include>
<script type="text/javascript">
	//根据角色id删除指定角色
	function deleteRole(roleId){
		//要用ajax
		window.location="deleteRole.do?id="+roleId;
	}
</script>

<table class="layui-table iw_table">
  <thead>
    <tr>
		<th>ID</th>
        <th>角色名</th>
        <th>描述</th>
        <th>操作</th>
    </tr> 
  </thead>
  <tbody>
  	<c:forEach items="${list}" var="role">
	   	<tr>
	        <td style="width:55px;">${role.id }</td>
	        <td>${role.name }</td>
	        <td>${role.description }</td>
	        <td style="width:140px;">
	        	<botton class="layui-btn layui-btn-sm" onclick="property('${role.name }', '${role.id }');" style="margin-left: 3px;"><i class="layui-icon">&#xe614;</i></botton>
	        	<a class="layui-btn layui-btn-sm" href="editRolePermission.do?roleId=${role.id }" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></a>
	        	<botton class="layui-btn layui-btn-sm" onclick="deleteRole('${role.name }', '${role.id }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
	        </td>
	    </tr>
	</c:forEach>
  </tbody>
</table>
<div style="padding:15px;">
	<button class="layui-btn" onclick="property('', 0);" style="margin-left: 10px;margin-bottom: 20px;"><i class="layui-icon" style="padding-right:8px; font-size: 22px;">&#xe608;</i>添加角色</button>
</div>
<div style="padding-right:35px; text-align: right;margin-top: -66px;">
	提示：&nbsp;&nbsp;&nbsp;
	<botton class=""><i class="layui-icon">&#xe614;</i></botton><span style="padding-left:12px;padding-right: 30px;">属性设置</span>
	<botton class=""><i class="layui-icon">&#xe642;</i></botton><span style="padding-left:12px;padding-right: 30px;">编辑权限</span>
	<botton class=""><i class="layui-icon">&#xe640;</i></botton><span style="padding-left:12px;padding-right: 30px;">删除角色</span>
</div>

<script type="text/javascript">
/**
 * 编辑角色属性／新增角色
 * name 角色名，若是新增，传入空字符串
 * id 角色的id，若是新增，传入0
 */
function property(name, id){
	layer.open({
		type: 2, 
		title: id>0? '修改角色：'+name:'新增角色',
		area: ['460px', '330px'],
		shadeClose: true, //开启遮罩关闭
		content: 'role.do?id='+id
	});
}

/**
 * 删除角色
 * name 角色名
 * id 角色的id
 */
function deleteRole(name, id){
	$.confirm("您确定要删除\""+name+"\"吗?", "确认删除?", function() {
	
		parent.iw.loading("删除中");    //显示“操作中”的等待提示
		$.post('deleteRole.do?id='+id, function(data){
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
		
	}, function() {
		//取消操作
	});
}

</script>
<jsp:include page="../../common/foot.jsp"></jsp:include>