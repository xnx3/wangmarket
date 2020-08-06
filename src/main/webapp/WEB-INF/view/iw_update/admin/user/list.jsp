<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="用户列表"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Role_role.js"></script>

<jsp:include page="../../../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="用户名"/>
		<jsp:param name="iw_name" value="username"/>
	</jsp:include>
	
    <jsp:include page="../../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="手机号"/>
		<jsp:param name="iw_name" value="phone"/>
	</jsp:include>
    
    <jsp:include page="../../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="邮箱"/>
		<jsp:param name="iw_name" value="email"/>
	</jsp:include>
    
    <input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
    
    <div style="float: right; " class="layui-form">
		<script type="text/javascript"> orderBy('id_DESC=编号,lasttime_DESC=最后登陆时间,money_DESC=账户余额,currency=<%=Global.get("CURRENCY_NAME") %>'); </script>
	</div>
	
</form>


<table class="layui-table iw_table">
  <thead>
    <tr>
		<th>ID</th>
        <th>用户名</th>
        <th>昵称</th>
        <th>手机号</th>
        <th>最后上线时间</th>
        <th>权限</th>
        <!-- <th>操作</th>     -->
    </tr> 
  </thead>
  <tbody>
  	<c:forEach items="${list}" var="user">
  		<tr>
          <td style="width:28px; cursor: pointer;" onclick="userView(${user.id });">${user.id }</td>
          <td style="width:58px; cursor: pointer;" onclick="userView(${user.id });">${user.username }</td>
          <td style="cursor: pointer;" onclick="userView(${user.id });"><x:substring maxLength="15" text="${user.nickname }"></x:substring> </td>
          <td style="width: 90px;">${user.phone }</td>
          <td style="width:100px;"><x:time linuxTime="${user.lasttime }" format="yy-MM-dd hh:mm"></x:time></td>
          <td><script type="text/javascript">writeName('${user.authority }');</script></td>
          <!-- <td style="width: 100px;">
          		<botton class="layui-btn layui-btn-sm" onclick="deleteUser(${user.id }, '${user.username }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
          </td>
           -->
      </tr>
    </c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../../iw/common/page.jsp"></jsp:include>

<div style="padding:15px; color:gray;">
	提示：<br/>
	这里（总管理后台）只是提供查看，若您想开通网站、或者开通代理账户，请登陆 代理后台（也就是账号 agency ）进行操作。默认的代理后台账号密码都是 agency , 您可退出登陆后，直接使用账号 agency 登陆即可进入代理后台。
	<br/>
	代理后台相关介绍，点击此处查看 <a href="http://help.wscso.com/5717.html" target="_black">http://help.wscso.com/5717.html</a>
</div>

<script type="text/javascript">
//根据id删除用户
function deleteUser(id,name){
	var dtp_confirm = layer.confirm('确定要删除用户“'+name+'”？删除后不可恢复！', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtp_confirm);
		
		parent.msg.loading("删除中");    //显示“操作中”的等待提示
		$.post('/admin/user/deleteUser.do?id='+id, function(data){
		    parent.msg.close();    //关闭“操作中”的等待提示
		    if(data.result == '1'){
		        parent.msg.success('操作成功');
		        window.location.reload();	//刷新当前页
		     }else if(data.result == '0'){
		         parent.msg.failure(data.info);
		     }else{
		         parent.msg.failure();
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
		content: '/admin/user/view.do?id='+id
	});
}

//修改权限
function editRole(id, username){
	layer.open({
		type: 2, 
		title:'修改&nbsp;[&nbsp;'+username+'&nbsp;]&nbsp;权限', 
		area: ['auto', 'auto'],
		shadeClose: true, //开启遮罩关闭
		content: '/admin/role/editUserRole.do?userid='+id
	});
}
</script>

<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>