<%@page import="com.xnx3.admin.entity.Site"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="../iw/common/head.jsp">
    <jsp:param name="title" value="栏目导航"/>
</jsp:include>
<script src="<%=basePath+Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="<%=basePath+Global.CACHE_FILE %>SiteColumn_type.js"></script>

<table class="layui-table" style="margin:0px;">
  <thead>
    <tr>
      <th>栏目名称</th>
      <th>栏目代码</th>
      <th>类型</th>
      <th>是否显示</th>
      <th>操作</th>
    </tr> 
  </thead>
  <tbody class="tile__listedit" id="columnList">
  	<!-- display 显示或者隐藏，是否在导航中显示。若为0，则不加入排序 -->
  	<c:forEach items="${list}" var="siteColumnTreeVO">
        <tr id="${siteColumnTreeVO.siteColumn.id }">
        	<td width="140">${siteColumnTreeVO.siteColumn.name }</td>
        	<td width="150">${siteColumnTreeVO.siteColumn.codeName }</td>
            <td width="100"><script type="text/javascript">document.write(type['${siteColumnTreeVO.siteColumn.type}']);</script></td>
            <td width="60"><script type="text/javascript">document.write(used['${siteColumnTreeVO.siteColumn.used}']);</script></td>
            <td width="80">
            	<botton class="layui-btn layui-btn-small" onclick="editColumn('${siteColumnTreeVO.siteColumn.id }',true);" style="margin-left: 3px;"><i class="layui-icon">&#xe630;</i></botton>
            	<botton class="layui-btn layui-btn-small" onclick="editColumn('${siteColumnTreeVO.siteColumn.id }',false);" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
            	<botton class="layui-btn layui-btn-small" onclick="deleteColumn('${siteColumnTreeVO.siteColumn.id }', '${siteColumnTreeVO.siteColumn.name }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
			</td>
        </tr>
        
        <!-- 其下级栏目 -->
        <c:if test="${not empty siteColumnTreeVO.list }">
			<c:forEach items="${siteColumnTreeVO.list}" var="subSCT">
		        <tr id="${subSCT.siteColumn.id }">
		        	<td width="140"><span style="padding-left:20px;">${subSCT.siteColumn.name }</span></td>
		        	<td width="150"><span style="padding-left:20px;">${subSCT.siteColumn.codeName }</span></td>
		            <td width="100"><script type="text/javascript">document.write(type['${subSCT.siteColumn.type}']);</script></td>
		            <td width="60"><script type="text/javascript">document.write(used['${subSCT.siteColumn.used}']);</script></td>
		            <td width="80">
		            	<botton class="layui-btn layui-btn-small" onclick="editColumn('${subSCT.siteColumn.id }',true);" style="margin-left: 3px;"><i class="layui-icon">&#xe630;</i></botton>
		            	<botton class="layui-btn layui-btn-small" onclick="editColumn('${subSCT.siteColumn.id }',false);" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
		            	<botton class="layui-btn layui-btn-small" onclick="deleteColumn('${subSCT.siteColumn.id }', '${subSCT.siteColumn.name }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
					</td>
		        </tr>
		    </c:forEach>
		</c:if> 
    </c:forEach>
  </tbody>
</table>

<div style="padding:15px;">
	<button class="layui-btn" onclick="addColumn();" style="margin-left: 10px;margin-bottom: 20px;"><i class="layui-icon" style="padding-right:8px; font-size: 22px;">&#xe608;</i>添加栏目</button>
</div>
<div style="padding-right:35px; text-align: right;margin-top: -66px;">
	
	提示：&nbsp;&nbsp;&nbsp;
	<botton class=""><i class="layui-icon">&#xe642;</i></botton><span style="padding-left:12px;padding-right: 30px;">编辑</span>
	<botton class=""><i class="layui-icon">&#xe640;</i></botton><span style="padding-left:12px;padding-right: 30px;">删除</span>
	<botton class=""><i class="layui-icon">&#xe630;</i></botton><span style="padding-left:12px;padding-right: 20px;">复制一个现有的栏目快速创建新栏目</span>
</div>
 
<script type="text/javascript">
/**
 * 编辑栏目
 * siteColumnId 要编辑的栏目的id
 * isCopy 是否是复制一个新的栏目出来进入编辑状态，1是复制的，    0不是复制的，属于正常的添加或者修改
 */
function editColumn(siteColumnId, isCopy){
	var url = '<%=basePath %>column/popupColumnForTemplate.do';
	layer.open({
		type: 2, 
		title:(isCopy? '复制一个栏目快速创建':'修改栏目'), 
		area: ['460px', '700px'],
		shadeClose: true, //开启遮罩关闭
		content: url+'?id='+siteColumnId+(isCopy? '&isCopy=1':'')
	});
}

/**
 * 删除栏目
 * siteColumnId 要编辑的栏目的id
 * name 要删除的栏目的栏目名字
 */
function deleteColumn(siteColumnId, name){
	var dtv_confirm = layer.confirm('确定要删除“'+name+'”吗? ', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtv_confirm);
		$.showLoading('删除中...');
		$.getJSON('<%=basePath %>column/delete.do?id='+siteColumnId,function(obj){
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

/**
 * 添加栏目
 * siteColumnId 要编辑的栏目的id
 */
function addColumn(siteColumnId){
	var url = '<%=basePath %>column/popupColumnForTemplate.do';
	layer.open({
		type: 2, 
		title:'添加栏目', 
		area: ['460px', '600px'],
		shadeClose: true, //开启遮罩关闭
		content: url
	});
}

</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>