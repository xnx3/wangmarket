<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="回帖列表"/>
</jsp:include>

<jsp:include page="../../common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="帖子ID"/>
		<jsp:param name="iw_name" value="postid"/>
	</jsp:include>
	
    <jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="回贴用户id"/>
		<jsp:param name="iw_name" value="userid"/>
	</jsp:include>
    
    <input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
</form>

<table class="layui-table iw_table">
  <thead>
    <tr>
		<th>编号</th>
        <th>贴子ID</th>
        <th>回复者</th>
        <th>回复内容</th>
        <th>回复时间</th>
        <th>操作</th>
    </tr>
  </thead>
  <tbody>
  	<c:forEach items="${list}" var="comment">
    	<tr>
         <td style="width:55px;">${comment.id }</td>
         <td style="width:55px;">${comment.postid }</td>
         <td style="width:55px; cursor: pointer;" onclick="userView(${comment.userid });">${comment.userid }</td>
         <td>${comment.text }</td>
         <td style="width:100px;"><x:time linuxTime="${comment.addtime }" format="yy-MM-dd HH:mm"></x:time></td>
         <td style="width:55px;">
         	<botton class="layui-btn layui-btn-small" onclick="deleteComment(${comment.id });" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
         </td>
		</tr>
    </c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../common/page.jsp"></jsp:include>

<script type="text/javascript">
//根据id删除回帖
function deleteComment(id){
	var dtp_confirm = layer.confirm('确定要删除回帖？', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtp_confirm);
		$.showLoading('正在删除');
		$.getJSON('<%=basePath %>admin/bbs/deleteComment.do?id='+id,function(obj){
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

//查看用户详情信息
function userView(id){
	layer.open({
		type: 2, 
		title:'查看用户信息', 
		area: ['460px', '630px'],
		shadeClose: true, //开启遮罩关闭
		content: '<%=basePath %>admin/user/view.do?id='+id
	});
}
</script>

<jsp:include page="../../common/foot.jsp"></jsp:include>