<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../../../../iw/common/head.jsp">
	<jsp:param name="title" value="分类板块"/>
</jsp:include>

<jsp:include page="../../../../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="名称"/>
		<jsp:param name="iw_name" value="name"/>
	</jsp:include>
	
    <input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
    <a href="javascript:class_xnx3(0,'');" class="layui-btn layui-btn-normal" style="float: right; margin-right:10px;">添加板块</a>
</form>

<table class="layui-table iw_table">
  <thead>
    <tr>
    	<th>编号</th>
		<th>版块名</th>
        <th>操作</th>
    </tr> 
  </thead>
  <tbody>
  	<c:forEach items="${list}" var="postClass">
	   	<tr>
	        <td>${postClass.id }</td>
	        <td>${postClass.name }</td>
	        <td style="width:150px;">
	        	<a class="layui-btn layui-btn-sm" href="<%=basePath %>admin/bbs/postList.do?classid=${postClass.id }" style="margin-left: 3px;">贴子</a>
	        	<botton class="layui-btn layui-btn-sm" onclick="class_xnx3(${postClass.id }, '${postClass.name }');" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
	        	<botton class="layui-btn layui-btn-sm" onclick="deleteClass(${postClass.id }, '${postClass.name }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
	        </td>
	    </tr>
	</c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../../../iw/common/page.jsp"></jsp:include>
<div style="padding: 20px;color: gray;">
	<div>操作按钮提示:</div>
	<div><i class="layui-icon">&#xe642;</i> &nbsp;：编辑操作，进行修改</div>
	<div><i class="layui-icon">&#xe640;</i> &nbsp;：删除操作，删除某项</div>
</div>


<script type="text/javascript">
//根据id删除板块
function deleteClass(id,name){
	var dtp_confirm = layer.confirm('确定要删除分类板块“'+name+'”？', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtp_confirm);
		
		parent.iw.loading("删除中");    //显示“操作中”的等待提示
		$.post('deleteClass.do?id='+id, function(data){
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
 * 修改／新增板块
 * id 要修改的变量的id，若是为0，则是新增
 * name 若是新增，这里可传入个空字符串
 */
function class_xnx3(id, name){
	layer.open({
		type: 2, 
		title: id==0? '新增分类板块':'修改分类板块：&nbsp;&nbsp;'+name+'&nbsp;', 
		area: ['380px', '180px'],
		shadeClose: true, //开启遮罩关闭
		content: 'postClass.do?id='+id
	});
}
</script>

<jsp:include page="../../../../iw/common/foot.jsp"></jsp:include> 