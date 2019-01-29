<%@page import="com.xnx3.j2ee.entity.User"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="文章详情"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>News_type.js"></script>
<script src="/<%=Global.CACHE_FILE %>News_status.js"></script>
<script src="/<%=Global.CACHE_FILE %>News_legitimate.js"></script>

<table class="layui-table iw_table">
	<tbody>
		<tr>
			<td class="iw_table_td_view_name">ID编号</td>
			<td>${news.id }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">标题</td>
			<td>${news.title }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">简介</td>
			<td>${news.intro }</td>
		</tr>
	<!-- 	
		<tr>
			<td class="iw_table_td_view_name">标题图片</td>
			<td>
				<c:choose>
					<c:when test="${!news.titlepic.isEmpty() && fn:length(news.titlepic) > 1}">
						<c:choose>
						    <c:when test="${fn:contains(news.titlepic,'http://')}">
						    	<img src="${news.titlepic }?x-oss-process=image/resize,h_100" height="50" />
						    </c:when>
						    <c:otherwise>
								<a href="${AttachmentFileUrl }site/${news.siteid }/news/${news.titlepic }" target="_black"><img src="${AttachmentFileUrl }site/${news.siteid }/news/${news.titlepic }?x-oss-process=image/resize,h_100" height="50" /></a>
						    </c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						无
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	-->
		
		<tr>
			<td class="iw_table_td_view_name">类型</td>
			<td><script type="text/javascript">document.write(type[${news['type']}]);</script></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">合法性</td>
			<td>
				<script type="text/javascript">document.write(legitimate[${news['legitimate']}]);</script>
				<c:choose>
					<c:when test="${news['legitimate'] == 0}">
		            	<script type="text/javascript">
		            		document.getElementById("legitimate").style.background="red";
		            	</script>
						<botton class="layui-btn layui-btn-sm" onclick="cancelLegitimate('${news['id'] }');" style="margin-left: 3px;">标记为合法</botton>
						<botton class="layui-btn layui-btn-sm" onclick="deleteNews('${news['id'] }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">创建时间</td>
			<td><x:time linuxTime="${news['addtime'] }"></x:time></td>
		</tr>
		
  </tbody>
</table>

<script type="text/javascript">
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

function deleteNews(id){
	if(confirm("确定要删除吗？删除后不可恢复！")){
		window.location.href="delete.do?id="+id;
	}
}
function cancelLegitimate(id){
	window.location.href="cancelLegitimate.do?id="+id;
}
</script>
<jsp:include page="../../iw/common/foot.jsp"></jsp:include>  