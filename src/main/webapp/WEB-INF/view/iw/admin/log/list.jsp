<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../common/head.jsp">
	<jsp:param name="title" value="动作日志列表"/>
</jsp:include>

<jsp:include page="../../common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="关键词"/>
		<jsp:param name="iw_name" value="queryString"/>
	</jsp:include>
	
	<i class="layui-icon" style="border: 1px solid #e2e2e2; padding:3px;border-radius: 30px; color: #626262;font-size: 12px; cursor: pointer; margin-left: 10px;" onclick="window.open('https://help.aliyun.com/document_detail/29060.html');">&#xe607;</i>
    <input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
    
</form>

<table class="layui-table iw_table">
  <thead>
    <tr>
        <th>动作</th>
        <th>备注</th>
        <th>目标id</th>
        <th>操作用户</th>
        <th>操作时间</th>
    </tr>
  </thead>
  <tbody>
  	<% int i = 0; %>
  	<c:forEach items="${list}" var="log">
  		<% i++; %>
  		<tr>
          <td style="cursor: pointer;" id="xnx3_<%=i %>" onclick="xnx3_<%=i %>_onclick();"><x:substring maxLength="18" text="${log['action'] }"></x:substring> </td>
          <td style="cursor: pointer;" onclick="xnx3_<%=i %>_onclick();"><x:substring maxLength="18" text="${log['remark'] }"></x:substring> </td>
          <td style="width:50px; cursor: pointer;" onclick="xnx3_<%=i %>_onclick();">${log['goalid'] }</td>
          <td style="width:100px; cursor: pointer;" onclick="userView('${log['userid'] }');"><x:substring maxLength="18" text="${log['username'] }"></x:substring></td>
          <td style="width:100px;"><x:time linuxTime="${log['logtime'] }" format="yy-MM-dd hh:mm"></x:time></td>
      </tr>
      <script>
      		//<table class="layui-table iw_table"> 这个湿table显示，但是不含<table>的table头，因为鼠标跟随提示跟点击弹出层的class是不一样的
      		var xnx3_<%=i %>_table_content = ''+
							'<tr><td style="width:80px;">动作</td><td>${log['action']}</td></tr>'+
							'<tr><td>描述</td><td>${log['remark']}</td></tr>'+
							'<tr><td>目标id</td><td>${log['goalid']}</td></tr>'+
							'<tr><td>操作用户</td><td>${log['username']}</td></tr>'+
							'<tr><td>操作人IP</td><td>${log['ip']}</td></tr>'+
							'<tr><td>时间</td><td><x:time linuxTime="${log['logtime'] }"></x:time></td></tr>'+
							'<tr><td>访问网址</td><td>${log['url']}</td></tr>'+
							'<tr><td>GET参数</td><td>${log['param']}</td></tr>'+
							'<tr><td>来源网址</td><td>${log['referer']}</td></tr>'+
							'<tr><td>UserAgent</td><td>${log['userAgent']}</td></tr>'+
							'<tr><td>触发类</td><td>${log['className']}</td></tr>'+
							'<tr><tr><td>触发函数</td><td>${log['methodName']}</td></tr>'+
							'</table>';
      
			//鼠标跟随提示
			$(function(){
				var xnx3_<%=i %>_index = 0;
				$("#xnx3_<%=i %>").hover(function(){
					xnx3_<%=i %>_index = layer.tips('<table>'+xnx3_<%=i %>_table_content, '#xnx3_<%=i %>', {
						tips: [1, '#0FA6A8'], //还可配置颜色
						time:0,
						tipsMore: true,
						area : ['480px' , 'auto']
					});
				},function(){
					layer.close(xnx3_<%=i %>_index);
				})
			});	
			function xnx3_<%=i %>_onclick(){
				view('<table class="layui-table iw_table">'+xnx3_<%=i %>_table_content);
			}
		</script>
    </c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../common/page.jsp"></jsp:include>

<script>
//查看某条动作详情
function view(content){
	layer.open({
		type: 1, 
		title:'查看动作详情', 
		area: ['490px', 'auto'],
		shadeClose: true, //开启遮罩关闭
		content: content
	});
}

//查看用户详情信息
function userView(id){
	if(id.length < 1){
		return;
	}
	layer.open({
		type: 2, 
		title:'查看用户信息', 
		area: ['460px', '630px'],
		shadeClose: true, //开启遮罩关闭
		content: '/admin/user/view.do?id='+id
	});
}
</script>

<jsp:include page="../../common/foot.jsp"></jsp:include>