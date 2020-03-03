<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="我开通的网站列表"/>
</jsp:include>

<jsp:include page="../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="用户名"/>
		<jsp:param name="iw_name" value="username"/>
	</jsp:include>
	
	<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
	
	<div style="float: right;">
    	<script type="text/javascript"> orderBy('id_DESC=编号,lasttime_DESC=最后登陆时间'); </script>
    </div>
    <a href="/agency/add.do" class="layui-btn layui-btn-normal" style="float: right; margin-right:10px;">开通网站</a>
</form>	


<table class="layui-table iw_table">
	<thead>
		<tr>
			<th>编号ID</th>
			<th>网站名</th>
			<th>用户名</th>
			<th>域名</th>
			<th>最后上线时间</th>
			<th>到期时间</th>
			<th>操作</th>
		</tr> 
	</thead>
	<tbody>
		<c:forEach items="${list}" var="obj">
			<tr>
				<td style="width:55px;"><a href="">${obj['id'] }</a></td>
				<td onclick="window.open('http://${obj['domain']}.<%=com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() %>');" style="cursor: pointer; width:55px;">
					<x:substring maxLength="10" text="${obj['name'] }"></x:substring>
				</td>
				<td style="width:100px;">${obj['userusername'] }</td>
				<c:choose>
				    <c:when test="${!obj['bind_domain'].isEmpty() && fn:length(obj['bind_domain']) > 0}">
				        <td onclick="window.open('http://${obj['bind_domain'] }'); " style="cursor: pointer; width: 160px;">${obj['bind_domain'] }</td>
				    </c:when>
				    <c:otherwise>
				        <td onclick="window.open('http://${obj['domain'] }.<%=com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() %>'); " style="cursor: pointer; width: 160px;">${obj['domain'] }.<%=com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() %></td>
				    </c:otherwise>
				</c:choose>
				<td style="width:100px;"><x:time linuxTime="${obj['lasttime'] }" format="yy-MM-dd hh:mm"></x:time></td>
				<td style="width:100px;"><x:time linuxTime="${obj['expiretime'] }" format="yy-MM-dd hh:mm"></x:time></td>
				<td style="width:150px;">
					<c:choose>
					<c:when test="${obj['state'] == 1 }">
						<botton class="layui-btn layui-btn-sm" onclick="freeze('${obj['id'] }','${obj['userusername'] }');" style="margin-left: 3px;">冻结</botton>
					</c:when>
					<c:when test="${obj['state'] == 2 }">
						<botton class="layui-btn layui-btn-sm" onclick="unFreeze('${obj['id'] }','${obj['userusername'] }');" style="margin-left: 3px;">解冻</botton>
					</c:when>
					</c:choose>
	            	
	            	<botton class="layui-btn layui-btn-sm" onclick="xufei('${obj['id'] }','${obj['userusername'] }');" style="margin-left: 3px;">续费</botton>
	            	<botton class="layui-btn layui-btn-sm" onclick="updatePassword('${obj['userid'] }','${obj['userusername'] }');" style="margin-left: 3px;">改密</botton>
	            </td>
			</tr>
		</c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../iw/common/page.jsp" ></jsp:include>

<div style="padding: 20px;color: gray;">
	<div>操作按钮提示:</div>
	<div>冻结：将网站冻结。冻结的网站无法登陆，无法访问。&nbsp;&nbsp;注意：
		<ul style="padding-left:70px; font-size:12px;">
			<li style="list-style-type: decimal;">冻结网站不会退返站币，会按照正常的1年/站币方式计费</li>
			<li style="list-style-type: decimal;">冻结后，会在一分钟之后生效。你可以新打开一个其他浏览器，输入域名即可看到结果。当前浏览器因缓存，不会立即出现冻结提示，短时间内可以正常浏览，无需在意，等过一个小时缓存过期后自然就可以变过来了。（下面的 解冻 同此）</li>
		</ul>
	</div>
	<div>解冻：将冻结的网站解除冻结状态，解冻后网站恢复正常。</div>
	<div>改密：更改密码。当用户忘记密码时，对其进行更改密码</div>
	<div>续费：对网站进行续费操作。网站到期时，用户登录，会提前一个月向用户提示续费。<!-- 若网站到期，将无法访问！(数据会保留，直至续费继续开通) --></div>
</div>

<script type="text/javascript">
//给agency.id的下级代理充值站币。agencyName只是展示作用
function xufei(siteid, name){
	layer.prompt({
		formType: 0,
		value: '1',
		title: '给'+name+'延期续费，单位：年',
	}, function(value, index, elem){
		parent.msg.loading('续费中');
		$.post("/agency/siteXuFie.do?siteid="+siteid+"&year="+value,function(result){
			parent.msg.close();
			if(result.result != '1'){
				parent.msg.failure(result.info);
			}else{
				parent.msg.success('续费成功');
				location.reload();
			}
		});
	});
}

//冻结网站
function freeze(siteid, name){
	layer.confirm('确定要冻结'+name+'吗?<br/>冻结后其将无法登录', {icon: 3, title:'确认冻结'}, function(index){
		parent.msg.loading('冻结中');
		$.getJSON("/agency/siteFreeze.do?siteid="+siteid,function(result){
			parent.msg.close();
			if(result.result != '1'){
				msg.failure(result.info);
			}else{
				parent.msg.success('已冻结');
				location.reload();
			}
		});
		layer.close(index);
	});
}

//解除冻结网站，解冻
function unFreeze(siteid, name){
	layer.confirm('确定要解冻'+name+'吗?<br/>解冻后其将会恢复正常使用', {icon: 3, title:'确认解冻'}, function(index){
		parent.msg.loading('解除中');
		$.getJSON("/agency/siteUnFreeze.do?siteid="+siteid,function(result){
			parent.msg.close();
			if(result.result != '1'){
				msg.failure(result.info);
			}else{
				parent.msg.success('已解冻');
				location.reload();
			}
		});
		layer.close(index);
	});
}

//给我创建的站点修改站点密码
function updatePassword(userid, name){
	layer.prompt({
		formType: 0,
		value: '',
		title: '给'+name+'改密码，请输入新密码',
	}, function(value, index, elem){
		parent.msg.loading('更改中');
		$.post(
		    "/agency/siteUpdatePassword.do", 
		    { "newPassword": value, userid:userid }, 
		    function(data){
		        parent.msg.close();    //关闭“更改中”的等待提示
		        if(data.result != '1'){
		            parent.msg.failure(data.info);
		        }else{
		            parent.msg.success('更改成功');
					location.reload();
		        }
		    }, 
		"json");
		
	});
}
</script>
<jsp:include page="../iw/common/foot.jsp"></jsp:include>