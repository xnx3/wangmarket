<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="栏目导航"/>
</jsp:include>

<body style="text-align:left; min-width:10px;">

<table class="layui-table" style="margin-top:0px;">
  <colgroup>
    <col width="120">
    <col width="120">
    <col>
    <col>
  </colgroup>
  <thead>
    <tr>
      <th>页面</th>
      <th>大小</th>
      <th>最后更改时间</th>
      <th>操作</th>
    </tr>
  </thead>
  <tbody>
  	<c:forEach items="${htmlList}" var="html">
		<tr id="${html.key }html">
			<td>${html.key }</td>
			<td><x:fileSizeToInfo size="${html.size }"></x:fileSizeToInfo></td>
			<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${html.lastModified}" /></td>
			<td>
				<button class="layui-btn layui-btn-sm layui-btn-radius" onclick="edit('${html.key }');">编辑</button>
				<button class="layui-btn layui-btn-sm layui-btn-radius" onclick="perview('${html.key }');">查看</button>
				<button class="layui-btn layui-btn-sm layui-btn-radius layui-btn-danger" onclick="deleteHtml('${html.key }');">删除</button>
			</td>
		</tr>
  	</c:forEach>
  </tbody>
</table>
<button class="layui-btn" onclick="newHtml();" style="margin-left: 10px;margin-bottom: 30px;">
  添加新页面
</button>
<button class="layui-btn" onclick="location.reload();" style="margin-left: 10px;margin-bottom: 30px;">
	刷新当前页面
</button>


<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
//parent.layer.iframeAuto(index);

//编辑页面
function edit(fileName){
	window.parent.updateHtmlSource(fileName);
}

//预览页面
function perview(fileName){
	window.open('http://'+window.parent.getSiteUrl()+'/'+fileName+'.html');
}

//新建页面
function newHtml(){
	window.parent.htmlSource('1','');
}

//删除某个HTML页面
function deleteHtml(fileName){
	layer.confirm('确定要删除 '+fileName+'.html ？', {
		btn: ['删除','取消'] //按钮
	}, function(){
        msg.loading("删除中");
		$.post("/sites/deleteOssData.do?fileName="+fileName+".html", function(data){
            msg.close();
			if(data.result == '1'){
                msg.success("删除成功");
				$("#"+fileName+"html").remove();
		 	}else if(data.result == '0'){
                msg.failure(data.info);
		 	}else{
                msg.failure();
		 	}
		});
		
	}, function(){
		
	});
}

</script>

</body>
</html>