<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="历史聊天记录列表"/>
</jsp:include>

<script type="text/javascript">
var sendUserName = '';	//发送人昵称
</script>
<table class="layui-table">
  <thead>
    <tr>
    	<th>对方昵称</th>
    	<th>对方消息条数</th>
		<th style="width:80px; min-width:120px;">最后咨询时间</th>
    </tr> 
  </thead>
  <tbody>
	<c:forEach items="${list}" var="log">
			<tr onclick="openChatRecord(${log.sendIds });" style="cursor: pointer;">
				<td>
					<script>
					//写这么多，其实就是兼容上一周的
					var name = ${log.sendUserName }.array;
					if(typeof(name) == 'underfind' || name == null || name == 'null'){
						document.write('访客007');
					}else{
						document.write(name);
					}
					</script>
				</td>
				<td>${log.count }</td>
				<td><x:time linuxTime="${log.time }" format="MM-dd HH:mm:ss"></x:time></td>
			</tr>
	</c:forEach>	
  </tbody>
</table>
<div style="padding:10px; color:gray;">
	提示，通过此通道所查看到的信息：<br/>
	1.只保留30天时间<br/>
	2.只显示前100位用户会话记录<br/>
	3.缓存时间1分钟，即用户会话沟通后，并不会立马在这里显示，要过1分钟之后才会在这里显示出来
</div>
<script>
/* 
 * 打开聊天记录列表
 * id 对方的id
 * name 对方的昵称
 */
function openChatRecord(id,name){
	layer.open({
        type: 2
        ,maxmin: true
        ,title: '查看聊天记录'
        ,area: ['450px', '100%']
        ,shade: false
        ,offset: 'rb'
        ,skin: 'layui-box'
        ,anim: 2
        ,id: 'layui-layim-chatlog'
        ,content: 'chatRecord.do?id='+id
      });
}
</script>
<jsp:include page="../iw/common/foot.jsp"></jsp:include>  