<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="用户列表"/>
</jsp:include>
<script src="<%=basePath+Global.CACHE_FILE %>Bbs_classid.js"></script>

<jsp:include page="../../common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="标题"/>
		<jsp:param name="iw_name" value="title"/>
	</jsp:include>
	
    <jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="板块"/>
		<jsp:param name="iw_name" value="classid"/>
		<jsp:param name="iw_type" value="select"/>
	</jsp:include>
    
    <jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="邮箱"/>
		<jsp:param name="iw_name" value="email"/>
	</jsp:include>
    
    <input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
    
    <div style="float: right; " class="layui-form">
		<script type="text/javascript"> orderBy('id_DESC=编号,view_DESC=浏览量'); </script>
	</div>
</form>
	                       

<table class="layui-table iw_table">
  <thead>
    <tr>
		<th>ID</th>
        <th>所属版块</th>
        <th>帖子名称</th>
        <th>发布时间</th>
        <th>操作</th>
    </tr> 
  </thead>
  <tbody>
  	<c:forEach items="${list}" var="post">
	   	<tr>
	        <td style="width:55px;">${post.id }</td>
	        <td style="width:150px;"><script type="text/javascript">document.write(classid[${post.classid }]);</script></td>
	        <td style="cursor: pointer;" onclick="javascript:alert('此处可自行扩展');">
	            ${post.title }
	        </td>
	        <td style="width:100px;"><x:time linuxTime="${post.addtime }" format="yy-MM-dd HH:mm"></x:time></td>
	        <td style="width:105px;">
	        	<a class="layui-btn layui-btn-small" href="<%=basePath %>admin/bbs/commentList.do?postid=${post.id }" style="margin-left: 3px;">回贴</a>
	        	<botton class="layui-btn layui-btn-small" onclick="deletePost(${post.id }, '${post.title }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
	        </td>
	    </tr>
   </c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../common/page.jsp"></jsp:include>

<script type="text/javascript">
//根据id删除系统变量
function deletePost(id,name){
	var dtp_confirm = layer.confirm('确定要删除帖子“'+name+'”？', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtp_confirm);
		$.showLoading('正在删除');
		$.getJSON('<%=basePath %>admin/bbs/deletePost.do?id='+id,function(obj){
			$.hideLoading();
			if(obj.result == '1'){
				$.toast("删除成功", function() {
					window.location.reload();	//刷新当前页
				});
	     	}else if(obj.result == '0'){
	     		 $.toast(obj.info, "cancel", function(toast) {});
	     	}else{
	     		alert(obj.result);
	     	}
		});
	}, function(){
	});
}


</script>

<jsp:include page="../../common/foot.jsp"></jsp:include> 