<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="资源列表"/>
</jsp:include>
<style>
.iw_table tbody tr td .titleIcon{
	padding-right:10px;
}
</style>
<jsp:include page="../../common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="名字"/>
		<jsp:param name="iw_name" value="name"/>
	</jsp:include>
	
    <jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="percode"/>
		<jsp:param name="iw_name" value="percode"/>
	</jsp:include>
    
    <jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="描述"/>
		<jsp:param name="iw_name" value="description"/>
	</jsp:include>
    
    <input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
	<a href="javascript:permission(0,0);" id="add_first_permission_aTag" class="layui-btn layui-btn-normal" style="float: right; margin-right:10px;"><i class="layui-icon">&#xe654;</i>&nbsp;顶级资源</a>
</form>

<table class="layui-table iw_table">
  <thead>
    <tr>
        <th>菜单名称</th>
        <th>描述</th>
        <th>percode</th>
        <th>操作</th>    
    </tr> 
  </thead>
  <tbody>
  	<c:forEach items="${list}" var="permissionTree">
       	<tr style="font-weight: bold; color:#123252; background-color:#fbfbfb;">
            <td style="width:180px;"><i class="layui-icon titleIcon">&#xe625;</i>${permissionTree.permissionMark.permission.name }</td>
            <td><x:substring maxLength="20" text="${permissionTree.permissionMark.permission.description }"></x:substring> </td>
            <!-- <td class="numeric">${permissionTree.permissionMark.permission.url }</td> -->
            <td>${permissionTree.permissionMark.permission.percode }</td>
            <td style="width:138px;">
            	<botton class="layui-btn layui-btn-sm" onclick="permission(${permissionTree.permissionMark.permission.id },0);" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
            	<botton class="layui-btn layui-btn-sm" onclick="deletePermission('${permissionTree.permissionMark.permission.name }', ${permissionTree.permissionMark.permission.id });" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
            	<botton class="layui-btn layui-btn-sm" onclick="permission(0,${permissionTree.permissionMark.permission.id });" style="margin-left: 3px;"><i class="layui-icon">&#xe654;</i></botton>
            </td>
        </tr>
        
        <!--二级菜单权限 -->
        <c:forEach items="${permissionTree.list }" var="permissionMark">
        	<tr>
				<td style="padding-left:41px;">${permissionMark.permission.name }</td>
				<td>${permissionMark.permission.description }</td>
				<!-- <td class="numeric">${permissionMark.permission.url }</td> -->
				<td>${permissionMark.permission.percode }</td>
				<td>
					<botton class="layui-btn layui-btn-sm" onclick="permission(${permissionMark.permission.id },0);" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
					<botton class="layui-btn layui-btn-sm" onclick="deletePermission('${permissionMark.permission.name }', ${permissionMark.permission.id });" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
				</td>
			</tr>
		</c:forEach>
     </c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../common/page.jsp"></jsp:include>

<div style="padding: 20px;color: gray;">
	<div>操作按钮提示:</div>
	<div><i class="layui-icon">&#xe642;</i> &nbsp;：编辑操作，进行修改</div>
	<div><i class="layui-icon">&#xe640;</i> &nbsp;：删除操作，删除某项</div>
	<div><i class="layui-icon">&#xe654;</i> &nbsp;：新增操作，可以在某个顶级资源分类下进行新增其下级的资源</div>
</div>

<script type="text/javascript">
//鼠标跟随提示
$(function(){
	//最右上方的添加顶级资源
	var add_first_permission_aTag_index = 0;
	$("#add_first_permission_aTag").hover(function(){
		add_first_permission_aTag_index = layer.tips('添加顶级资源，而下方的&nbsp;<i class="layui-icon">&#xe654;</i>&nbsp;只是添加某个顶级资源下的子资源 ', '#add_first_permission_aTag', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['310px' , 'auto']
		});
	},function(){
		layer.close(add_first_permission_aTag_index);
	})
});

//根据资源的id删除
function deletePermission(name, permissionId){
	layer.confirm("您确定要删除\""+name+"\"吗?", {icon: 3, title:'提示'}, function(index){
		parent.iw.loading("删除中");    //显示“操作中”的等待提示
		$.post('deletePermission.do?id='+permissionId, function(data){
		    parent.iw.loadClose();    //关闭“操作中”的等待提示
		    if(data.result == '1'){
				parent.parent.iw.msgSuccess('删除成功');
		        window.location.reload();	//刷新当前页
		     }else if(data.result == '0'){
		         parent.iw.msgFailure(data.info);
		     }else{
		         parent.iw.msgFailure();
		     }
		});
		layer.close(index);
	});
	
}

/**
 * 添加/修改资源
 * id 要修改的资源id， permission.id ，若是<1 则是添加
 * parentId 要添加的资源的父id，修改时此参数无效。若是0则是添加顶级资源
 */
function permission(id, parentId){
	var url = id<1 ? 'addPermission.do?parentId='+parentId : 'editPermission.do?id='+id;
	console.log(url);
	layer.open({
		type: 2, 
		title: id<1 ? '添加资源':'修改资源', 
		area: ['460px', '469px'],
		shadeClose: true, //开启遮罩关闭
		content: url
	});
}
</script>
<jsp:include page="../../common/foot.jsp"></jsp:include>