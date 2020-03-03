<%@page import="com.xnx3.wangmarket.admin.entity.Site"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
    <jsp:param name="title" value="自定义输入模型"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_type.js"></script>

<table class="layui-table" style="margin:0px;">
  <colgroup>
    <col>
    <col width="160">
    <col width="80">
    <col width="58">
  </colgroup>
  <thead>
    <tr>
      <th>模型代码</th>
      <th>备注说明</th>
      <th>操作</th>
    </tr> 
  </thead>
  <tbody class="tile__listedit" id="columnList">
  	<!-- display 显示或者隐藏，是否在导航中显示。若为0，则不加入排序 -->
  	<c:forEach items="${list}" var="model">
        <tr>
        	<td>${model.codeName }</td>
        	<td>${model.remark }</td>
            <td  style="width:110px;">
            	<botton class="layui-btn layui-btn-sm" onclick="window.location.href='edit.do?id=${model.id}';"><i class="layui-icon">&#xe642;</i></botton>
            	<botton class="layui-btn layui-btn-sm" onclick="deleteInputModel('${model.id }', '${model.remark }');"><i class="layui-icon">&#xe640;</i></botton>
			</td>
        </tr>
    </c:forEach>
  </tbody>
</table>

<div style="padding:15px;">
	<button class="layui-btn" onclick="window.location.href='edit.do';" style="margin-left: 10px;margin-bottom: 20px;">添加模型</button>
</div>
<div style="padding-right:15px; text-align: right;margin-top: -66px;">
	提示：&nbsp;&nbsp;&nbsp;
	<botton class=""><i class="layui-icon">&#xe642;</i></botton><span style="padding-left:12px;padding-right: 30px;">编辑</span>
	<botton class=""><i class="layui-icon">&#xe640;</i></botton><span style="padding-left:12px;padding-right: 30px;">删除</span>
</div>
 
<script type="text/javascript">
/**
 * 删除栏目
 * id 要编辑的输入模型的id
 * remark 要删除的输入模型的remark
 */
function deleteInputModel(id, remark){
	var dtv_confirm = layer.confirm('确定要删除“'+remark+'”吗? ', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtv_confirm);
		
		parent.msg.loading('删除中');
		$.post('/inputModel/delete.do?id='+id, function(data){
		    parent.msg.close();
		    if(data.result == '1'){
		        parent.msg.success("删除成功");
				window.location.reload();	//刷新当前页
		     }else if(data.result == '0'){
				msg.failure(data.info);
		     }else{
				msg.failure('操作失败');
		     }
		});
		
	}, function(){
	});
}

</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>