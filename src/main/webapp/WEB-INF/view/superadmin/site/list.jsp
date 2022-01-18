<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="网站列表"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Site_client.js"></script>
<script src="/<%=Global.CACHE_FILE %>Site_state.js"></script>


<jsp:include page="../../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="站名"/>
		<jsp:param name="iw_name" value="name"/>
	</jsp:include>
	<jsp:include page="../../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="状态"/>
		<jsp:param name="iw_name" value="state"/>
		<jsp:param name="iw_type" value="select"/>
	</jsp:include>
	
	<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
	
	<div style="float: right;">
    	<script type="text/javascript"> orderBy('id_DESC=编号,addtime_DESC=创建时间,expiretime_DESC=过期时间'); </script>
    </div>
</form>	
                   
<table class="layui-table iw_table">
	<thead>
		<tr>
			<th>编号ID</th>
			<th>用户ID</th>
			<th>网站名</th>
			<th>域名</th>
			<th>备注</th>
			<th>创建时间</th>
			<th>到期时间</th>
			<th style="width: 90px;">状态</th>
		</tr> 
	</thead>
	<tbody>
		<c:forEach items="${list}" var="site">
			<tr>
				<td style="width:55px;"><a href="view.do?id=${site['id'] }">${site['id'] }</a></td>
				<td onclick="userView(${site['userid'] });" style="cursor: pointer; width:55px;">${site['userid'] }</td>
				<td><a href="javascript:siteView('${site['id'] }');" title="${site['name'] }"><x:substring maxLength="20" text="${site['name'] }"></x:substring> </a></td>
				<c:choose>
				    <c:when test="${!site.bindDomain.isEmpty() && fn:length(site.bindDomain) > 0}">
				        <td onclick="window.open('http://${site['bindDomain'] }'); " style="cursor: pointer; width: 180px;">${site['bindDomain'] }</td>
				    </c:when>
				    <c:otherwise>
				        <td onclick="window.open('http://${site['domain'] }.<%=com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() %>'); " style="cursor: pointer; width: 180px;">${site['domain'] }.<%=com.xnx3.wangmarket.admin.G.getFirstAutoAssignDomain() %></td>
				    </c:otherwise>
				</c:choose>
				<td><x:substring maxLength="15" text="${site['remark'] }"></x:substring><botton class="layui-btn layui-btn-sm" onclick="updateRemark('${site['id'] }','${site['name'] }','${site['remark'] }');" style="margin-left: 3px;">修改</botton></td>
				<td style="width:100px;"><x:time linuxTime="${site['addtime'] }" format="yy-MM-dd hh:mm"></x:time></td>
				<td style="width:100px;"><x:time linuxTime="${site['expiretime'] }" format="yy-MM-dd hh:mm"></x:time></td>
				<td>
					<script type="text/javascript">document.write(state[${site['state']}]);</script>
					<c:choose>
						<c:when test="${site['state'] == 1 }">
							<botton class="layui-btn layui-btn-sm" onclick="freeze('${site['id'] }','${site['name'] }');" style="margin-left: 3px;">冻结</botton>
						</c:when>
						<c:when test="${site['state'] == 2 }">
							<botton class="layui-btn layui-btn-sm" onclick="unFreeze('${site['id'] }','${site['name'] }');" style="margin-left: 3px;">解冻</botton>
						</c:when>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../../iw/common/page.jsp" ></jsp:include>

<script>
//查看站点详情信息
function siteView(id){
	layer.open({
		type: 2, 
		title:'查看站点信息', 
		area: ['460px', '470px'],
		shadeClose: true, //开启遮罩关闭
		content: '/admin/site/view.do?id='+id
	});
}

//查看用户信息
function userView(id){
	layer.open({
		type: 2, 
		title:'查看用户信息', 
		area: ['460px', '630px'],
		shadeClose: true, //开启遮罩关闭
		content: '/admin/user/view.do?id='+id
	});
}


//冻结网站
function freeze(siteid, name){
	msg.confirm('<div style="padding-bottom: 0.4rem;">确定要冻结['+name+']吗?</div>1. 冻结后其将无法登录<br/>2. 冻结后网站将无法访问',function(){
		parent.msg.loading('冻结中');    //接口请求前，显示加载中的友好提示，避免用户感觉系统卡了
		wm.post("siteFreeze.json", {siteid:siteid},function(data){
			parent.msg.close();    //接口请求完毕拿到返回结果后，关闭加载中的提示。
		    if(data.result != '1'){
		        //接口返回执行失败
		        msg.failure(data.info);
		        return;
		    }

		    parent.msg.success('已冻结');
			location.reload();
		});
	});
}

//解除冻结网站，解冻
function unFreeze(siteid, name){
	msg.confirm('<div style="padding-bottom: 0.4rem;">确定要解冻['+name+']吗?</div>1. 解除后网站可正常登录<br/>2. 解除后网站将正常访问',function(){
		parent.msg.loading('解冻中');    //接口请求前，显示加载中的友好提示，避免用户感觉系统卡了
		wm.post("siteUnFreeze.json", {siteid:siteid},function(data){
			parent.msg.close();    //接口请求完毕拿到返回结果后，关闭加载中的提示。
		    if(data.result != '1'){
		        //接口返回执行失败
		        msg.failure(data.info);
		        return;
		    }

		    parent.msg.success('已解冻');
			location.reload();
		});
	});
}
//给某个站点修改站点备注
function updateRemark(siteid, name, remark){
	layer.prompt({
		formType: 2,
		value: remark.replace(/\[br\]/g,"\n"),
		title: '给'+name+'改备注，请输入备注',
	}, function(value, index, elem){
		parent.msg.loading('更改中');
		$.post(
		    "/admin/site/siteUpdateRemark.json", 
		    { 'remark' : value.replace(/\n/g,"[br]"), siteid:siteid }, 
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
<jsp:include page="../../iw/common/foot.jsp"></jsp:include>                  