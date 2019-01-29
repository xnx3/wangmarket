<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="信息列表"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Message_state.js"></script>

<jsp:include page="../../common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="发信用户ID"/>
		<jsp:param name="iw_name" value="senderid"/>
	</jsp:include>
	
    <jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="收信用户ID"/>
		<jsp:param name="iw_name" value="recipientid"/>
	</jsp:include>
    
    <input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
</form>

<table class="layui-table iw_table">
  <thead>
    <tr>
        <th>编号</th>
        <th>发信者</th>
        <th>收信者</th>
        <th>信息内容</th>  
        <th>发信时间</th>  
        <th>状态</th>
        <th>操作</th>
    </tr> 
  </thead>
  <tbody>
  	<c:forEach items="${list}" var="message">
    	<tr>
         <td style="width:55px;">${message['id'] }</td>
         <td onclick="userView(${message['senderid'] });" style="cursor: pointer;">${message['self_nickname'] }(ID:${message['senderid'] })</td>
         <td onclick="userView(${message['recipientid'] });" style="cursor: pointer;">${message['other_nickname'] }(ID:${message['recipientid'] })</td>
         <td id="m${message['id'] }" onclick="messageTextView('${message['content'] }');" style="cursor: pointer;"><x:substring maxLength="15" text="${message['content'] }"></x:substring> </td>
         <td style="width:100px;"><x:time linuxTime="${message['time'] }" format="yy-MM-dd HH:mm"></x:time></td>
         <td style="width:50px;">
         	<script type="text/javascript">document.write(state['${message['state'] }']);</script>
         </td>
         <td style="width:50px;">
         	<botton class="layui-btn layui-btn-sm" onclick="deleteMessage(${message['id'] });" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
         </td>
		</tr>
		<script>
			//鼠标跟随提示
			$(function(){
				var m${message['id'] }_index = 0;
				$("#m${message['id'] }").hover(function(){
					m${message['id'] }_index = layer.tips('${message['content'] }', '#m${message['id'] }', {
						tips: [2, '#0FA6A8'], //还可配置颜色
						time:0,
						tipsMore: true,
						area : ['280px' , 'auto']
					});
				},function(){
					layer.close(m${message['id'] }_index);
				})
			});	
		</script>
    </c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../common/page.jsp"></jsp:include>

<script type="text/javascript">
/*
 * 根据站内信id删除站内信
 * id 删除的信息的id
 */
function deleteMessage(id){
	$.confirm("您确定要删除此条信息吗?", "确认删除?", function() {
		
		parent.iw.loading("删除中");    //显示“操作中”的等待提示
		$.post('/admin/message/delete.do?id='+id, function(data){
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

//查看信息内容详情
function messageTextView(text){
	layer.open({
		type: 1, 
		title:'查看站内信息内容详情', 
		area: ['460px', 'auto'],
		shadeClose: true, //开启遮罩关闭
		content: '<div style="padding: 20px;">'+text+'</div>'
	});
}
</script>

<jsp:include page="../../common/foot.jsp"></jsp:include>