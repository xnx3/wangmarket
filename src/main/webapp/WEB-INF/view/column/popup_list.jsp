<%@page import="com.xnx3.wangmarket.admin.entity.Site"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
//当前此页面仅仅只是用于PC端的通用模版，CMS模式有自己单独的
%>

<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="栏目导航"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_type.js"></script>

<script src="${STATIC_RESOURCE_PATH}js/jquery-2.1.4.js"></script>

<!-- DOM操作 -->
<script src="${STATIC_RESOURCE_PATH}js/HTML.min.js"></script>
<!-- 拖动操作 -->
<script src="${STATIC_RESOURCE_PATH}js/Sortable.js"></script>

<!-- Data -->
<script src="${AttachmentFileUrl }site/${site.id }/data/siteColumn.js"></script>
<script src="${AttachmentFileUrl }site/${site.id }/data/siteColumnRank.js"></script>

<body>
<table class="layui-table" id="xnx3_body" style="margin:0px;">
  <colgroup>
    <col>
    <col width="160">
    <col width="80">
    <col width="58">
    <col width="90">
    <col width="80">
    <col width="90">
  </colgroup>
  <thead>
    <tr>
      <th>栏目代码</th>
      <th>栏目名称</th>
      <th>图标</th>
      <th>类型</th>
      <th>是否显示</th>
      <th>操作</th>
    </tr> 
  </thead>
  <tbody class="tile__listedit" id="columnList">
  	<!-- display 显示或者隐藏，是否在导航中显示。若为0，则不加入排序 -->
  	<c:forEach items="${list}" var="column">
        <tr id="${column['id'] }" style="${cursorStyle }">
        	<!--display${column['used']}-->
        	<td width="150">${column['codeName'] }</td>
            <td width="140">${column['name'] }</td>
            <td width="40"><img src="<x:imgUrl img="${column['icon'] }" prefixUrl="${AttachmentFileUrl }site/${siteid }/column_icon/"></x:imgUrl>" alt="${column['icon'] }" height="20" /></td>
            <td width="100"><script type="text/javascript">document.write(type['${column['type']}']);</script></td>
            <td width="60"><script type="text/javascript">document.write(used['${column['used']}']);</script></td>
            <td width="80">
            	<botton class="layui-btn layui-btn-sm layui-btn-radius" onclick="editColumn('${column['id'] }');">编辑</botton>
            	<botton class="layui-btn layui-btn-sm layui-btn-radius" onclick="deleteColumn('${column['id'] }', '${column['name'] }');">删除</botton>
			</td>
        </tr>
    </c:forEach>
  </tbody>
</table>

<div style="padding:15px;">
	<button class="layui-btn" onclick="addColumn();" style="margin-left: 10px;margin-bottom: 20px;">添加栏目</button>
	<div style="float:right; margin-top:3px; color:#d2d2d2;">提示：拖动可进行排序</div>
</div>
 
<script type="text/javascript">
/* 保存栏目导航的排序 */
function saveRank() {
	var column = new Array();
	column = HTML.query("#columnList").tr;
	/* 比较获取到的导航栏目数量与初始的数量是否相同，若相同，才可以进入下一步保存 */
	//if (siteColumn.length == column.length) {
		var rankString = '';
		for (var i = 0; i < column.length; i++) {
			//只保存显示状态的
			if(column[i].innerHTML.indexOf('<!--display1-->') > -1){
				if (rankString.length == 0) {
					rankString = column[i].id;
				} else {
					rankString = rankString + ',' + column[i].id;
				}
			}
		}

		$.post("/column/saveRank.do", {
			siteid : '${site.id}',
			rankString : rankString
		}, function(data, status) {
			if (status != 'success') {
				alert("Data: " + data.info + "\nStatus: " + status);
				console.log(data);
			}
		});
	//} else {
	//	console.log("column:"+column.length);
	//	alert('排序后数据对比出错！需进行排序重置！');
	//	//window.location.href='/column/resetRank.do?siteid=${site.id}';
	//}
}

/**
 * 编辑栏目
 * siteColumnId 要编辑的栏目的id
 */
function editColumn(siteColumnId){
	var url = '/column/';
	if('${site.templateName}'.length > 0){
		//模版CMS建站
		url = url + 'popupColumnForTemplate.do';
	}else{
		//通用型模版
		url = url + 'popupColumnGaoJiUpdate.do';
	}
	layer.open({
		type: 2, 
		title:'修改栏目', 
		area: ['460px', '635px'],
		shadeClose: true, //开启遮罩关闭
		content: url+'?id='+siteColumnId
	});
}

/**
 * 删除栏目
 * siteColumnId 要编辑的栏目的id
 * name 要删除的栏目的栏目名字
 */
function deleteColumn(siteColumnId, name){
	$.confirm("您确定要删除\""+name+"\"吗?", "确认删除?", function() {
        msg.loading('正在删除');
		$.getJSON('/column/delete.do?id='+siteColumnId,function(obj){
            msg.close();
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
	}, function() {
		//取消操作
	});
}

/**
 * 添加栏目
 * siteColumnId 要编辑的栏目的id
 */
function addColumn(siteColumnId){
	var url = '/column/';
	if('${site.templateName}'.length > 0){
		//模版CMS建站
		url = url + 'popupColumnForTemplate.do';
	}else{
		//通用型模版
		url = url + 'popupColumnGaoJiUpdate.do';
	}
	layer.open({
		type: 2, 
		title:'添加栏目', 
		area: ['460px', '600px'],
		shadeClose: true, //开启遮罩关闭
		content: url
	});
}

//加载完毕后，初始化顺序
function initPaiXu(){
	var newTableText = '';	//重新组合得table得html内容
	var column = new Array();
	column = HTML.query("#columnList").tr;
	//计算，先显示出正常显示的栏目
	for (var i = 0; i < siteColumnRank.length; i++) {
		for (var j = 0; j < column.length; j++) {
			if(siteColumnRank[i]==column[j].id){
				newTableText = newTableText + "<tr id=\""+ column[j].id +"\" style=\"cursor: all-scroll;\">"+column[j].innerHTML + "</tr>";
			}
		}
	}
	//再将隐藏不显示的栏目放到最低下。不显示的栏目不参与排序
	for (var j = 0; j < column.length; j++) {
		var have = 0;	//默认栏目没有在排序中出现，也就是默认是隐藏的
		for (var i = 0; i < siteColumnRank.length; i++) {
			if(siteColumnRank[i]==column[j].id){
				have = 1;	//出现了
			}
		}
		//循环完毕后，如果栏目没有在排序中出现，那么是隐藏的栏目，要显示出来
		if(have == 0){
			newTableText = newTableText + "<tr id=\""+ column[j].id +"\" style=\"cursor: all-scroll;\">"+column[j].innerHTML + "</tr>";
		}
	}
	document.getElementById('columnList').innerHTML = newTableText;
}


//通用模版
initPaiXu();
      
(function() {
	/* 拖动布局 */
	[].forEach.call(xnx3_body.getElementsByClassName('tile__listedit'),
				function(el) {
					new Sortable(el, {
						group : 'site_column',
						onUpdate : function(evt) {
							var d = HTML.query("#columnList").tr[2];
							saveRank();
						}
					});
				});
})();
</script>


</body>
</html>