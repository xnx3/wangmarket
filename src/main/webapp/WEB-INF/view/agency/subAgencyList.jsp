<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="我的下级代理列表"/>
</jsp:include>

<jsp:include page="../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
	<jsp:include page="../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="公司名"/>
		<jsp:param name="iw_name" value="name"/>
	</jsp:include>
	<jsp:include page="../iw/common/list/formSearch_input.jsp">
		<jsp:param name="iw_label" value="手机号"/>
		<jsp:param name="iw_name" value="phone"/>
	</jsp:include>
	
	<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
	
	<div style="float: right;">
    	<script type="text/javascript"> orderBy('id_DESC=代理编号,expiretime_ASC=到期时间,addtime_DESC=开通时间'); </script>
    </div>
    <a href="/agency/addAgency.do" class="layui-btn layui-btn-normal" style="float: right; margin-right:10px;">开通下级代理</a>
</form>	
             
<table class="layui-table iw_table">
	<thead>
		<tr>
			<th>编号ID</th>
			<th>用户名</th>
			<th>公司名</th>
			<th>手机号</th>
			<th>拥有站币</th>
			<th>开通时间</th>
			<th>到期时间</th>
			<th>操作</th>
		</tr> 
	</thead>
	<tbody>
		<c:forEach items="${list}" var="agency">
		   	<tr>
		        <td style="width:50px;">${agency['id'] }</td>
		        <td>${agency['username'] }</td>
		        <td><x:substring maxLength="6" text="${agency['name'] }" more=".."></x:substring></td>
		        <td style="width:90px;">${agency['phone'] }</td>
		        <td style="width:60px;">${agency['site_size'] }</td>
		        <td style="width:100px;"><x:time linuxTime="${agency['addtime'] }" format="yy-MM-dd HH:mm"></x:time></td>
		        <td style="width:100px;"><x:time linuxTime="${agency['expiretime'] }" format="yy-MM-dd HH:mm"></x:time></td>
		        <td style="width:200px;">
					<c:choose>
						<c:when test="${agency['state'] == 1 }">
							<botton class="layui-btn layui-btn-sm" onclick="freeze('${agency['id'] }','${agency['username'] }');" style="margin-left: 3px;">冻结</botton>
						</c:when>
						<c:when test="${agency['state'] == 2 }">
							<botton class="layui-btn layui-btn-sm" onclick="unFreeze('${agency['id'] }','${agency['username'] }');" style="margin-left: 3px;">解冻</botton>
						</c:when>
					</c:choose>
					<botton class="layui-btn layui-btn-sm" onclick="yanqi('${agency['id'] }','${agency['username'] }');" style="margin-left: 3px;">延期</botton>
					<botton class="layui-btn layui-btn-sm" onclick="chongzhi('${agency['id'] }','${agency['username'] }');" style="margin-left: 3px;">充值</botton>
					<botton class="layui-btn layui-btn-sm" onclick="updatePassword('${agency['userid'] }','${agency['username'] }');" style="margin-left: 3px;">改密</botton>
		        </td>
		    </tr>
		</c:forEach>
  </tbody>
</table>
<!-- 通用分页跳转 -->
<jsp:include page="../iw/common/page.jsp" ></jsp:include>             
                        
<div style="padding: 20px;color: gray;">
	<div>操作按钮提示:</div>
	<div>冻结：将网站冻结。冻结的网站无法登陆，无法访问。注意，冻结网站不会退返站币，会按照正常的1年/站币方式计费</div>
	<div>解冻：将冻结的网站解除冻结状态，解冻后网站恢复正常。</div>
	<div>延期：延长某个代理账户的使用期限</div>
	<div>充值：给下级代理充值站币。相当于将当前自己的站币转给下级代理</div>
	<div>改密：更改密码。当用户忘记密码时，对其进行更改密码</div>
</div>
                            
                 
<script type="text/javascript">
//给agency.id的下级代理充值站币。agencyName只是展示作用
function chongzhi(agencyId, agencyName){
	layer.prompt({
		formType: 0,
		value: '',
		title: '给'+agencyName+'充值的站币',
	}, function(value, index, elem){
		parent.msg.loading('充值中');
		$.post("/agency/transferSiteSizeToSubAgency.do?targetAgencyId="+agencyId+"&transferSiteSize="+value,function(result){
			parent.msg.close();
			if(result.result != '1'){
				parent.msg.failure(result.info);
			}else{
				parent.msg.success('充值成功');
				location.reload();
			}
		});
	});
}

/**
 * 给下级代理延长使用期限
 * @param agencyId 要延长使用期限的下级代理的id
 * @param name 对方代理的名字，这里仅仅只是提示的作用
 */
function yanqi(agencyId, name){
	layer.prompt({
		formType: 0,
		value: '1',
		title: '给'+name+'延期，单位：年',
	}, function(value, index, elem){
		parent.msg.loading('延长中');
		$.post("/agency/agencyYanQi.do?agencyId="+agencyId+"&year="+value,function(result){
			parent.msg.close();
			if(result.result != '1'){
				parent.msg.failure(result.info);
			}else{
				parent.msg.success('延长成功');
				location.reload();
			}
		});
	});
}


//给我下级代理修改密码
function updatePassword(userid, name){
	layer.prompt({
		formType: 0,
		value: '',
		title: '给'+name+'改密码，请输入新密码',
	}, function(value, index, elem){
		parent.msg.loading("更改中");    //显示“更改中”的等待提示
		$.post(
		    "/agency/siteUpdatePassword.do", 
		    { "newPassword": value, userid:userid }, 
		    function(data){
		        parent.msg.close();    //关闭“更改中”的等待提示
		        if(data.result != '1'){
		            parent.msg.failure(data.info);
		        }else{
		            parent.msg.success('修改成功');
					location.reload();
		        }
		    }, 
		"json");
	});
}


//冻结下级代理
function freeze(agencyId, name){
	layer.confirm('确定要冻结'+name+'吗?<br/>冻结后其将无法登录', {icon: 3, title:'确认冻结'}, function(index){
		parent.msg.loading('冻结中');
		$.post("/agency/agencyFreeze.do?agencyId="+agencyId,function(result){
			parent.msg.close();
			if(result.result != '1'){
				parent.msg.failure(result.info);
			}else{
				parent.msg.success('已冻结');
				location.reload();
			}
		});
		layer.close(index);
	});
}

//解除冻结下级代理，恢复正常
function unFreeze(agencyId, name){
	layer.confirm('确定要解冻'+name+'吗?<br/>解冻后其将会恢复正常使用', {icon: 3, title:'确认解冻'}, function(index){
		parent.msg.loading('解除中');
		$.post("/agency/agencyUnFreeze.do?agencyId="+agencyId,function(result){
			parent.msg.close();
			if(result.result != '1'){
				parent.msg.failure(result.info);
			}else{
				parent.msg.success('已解冻');
				location.reload();
			}
		});
		layer.close(index);
	});
}
</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>