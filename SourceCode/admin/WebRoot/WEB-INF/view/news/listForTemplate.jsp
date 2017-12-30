<%@page import="com.xnx3.net.OSSUtil"%>
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
	<jsp:param name="title" value="文章列表"/>
</jsp:include>

<style>
.body{
	margin: 0;padding: 0px;height: 100%;overflow: hidden;
}
.menu{
	width:160px;
	height:100%;
	background-color: #EAEDF1;
	position: absolute;
}

/* 左侧栏目列表的文字 */
.layui-nav-tree .layui-nav-item a{
	color:#333;
}

/*鼠标移动到某项后的样式*/
.layui-nav-tree .layui-nav-item a:hover:HOVER{
	background-color: #f4f6f8;
	color:#222;
}

/*子栏目*/
.layui-nav-tree .layui-nav-item dl dd a{
	padding-left:35px;
}

.dltitle{
	background-color: #EAEDF1;
}

.layui-nav-itemed>a, .layui-nav-tree .layui-nav-title a, .layui-nav-tree .layui-nav-title a:hover {
    background-color: #EAEDF1!important;
    color: #222!important;
}

.table>thead>tr>th, .table>tbody>tr>th, .table>tfoot>tr>th{
	line-height: 2.2;
	text-align:center;
}

.layui-nav-child dd{
	background-color: #EAEDF1;
}

.layui-nav .layui-nav-item .layui-nav-more{
	border-top-color: rgba(64, 34, 34, 0.7);
}
/*有二级栏目的，二级栏目伸缩的时候，右侧的小尖头的颜色*/
.layui-nav .layui-nav-itemed .layui-nav-more{
	border-top-color: rgba(64, 34, 34, 0);
	border-color: transparent transparent #a42828;
}
    
</style>

<div style="width:100%;height:100%; background-color: #fff; overflow-x: hidden;">
	
		
	<div class="layui-nav layui-nav-tree layui-nav-side menu">
		<div style="height: 65px;text-align: left;line-height: 65px;font-size: 16px;font-weight: 700;color: black;padding-left: 18px;">内容管理</div>
		<ul class="">
		  ${columnTreeNav }
		</ul>
	</div>
	
	
	<div style="width: 100%;height:100%;position: absolute;left: 170px;word-wrap: break-word;border-right: 170px;box-sizing: border-box; padding-right: 10px; overflow-y: auto;overflow-x: hidden; border-right: 170px solid transparent;">
		
		<jsp:include page="../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
			<jsp:include page="../iw/common/list/formSearch_input.jsp">
				<jsp:param name="iw_label" value="标题"/>
				<jsp:param name="iw_name" value="title"/>
			</jsp:include>
			
			<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
			
		    <c:choose>
			    <c:when test="${siteColumn.type == 1 || siteColumn.type == 2}">
			    	<a href="news.do?cid=${siteColumn['id'] }" class="layui-btn layui-btn-normal" style="float: right; margin-right:10px;">添加信息</a>
			    </c:when>
			    <c:otherwise>
			    </c:otherwise>
			</c:choose>
		</form>	
		    
		<table class="layui-table iw_table">
			<thead>
				<tr>
					<th>编号</th>
		            <th>文章标题</th>
		            <th>标题图片</th>
		            <th>发布时间</th>
		            <th>操作</th>
				</tr> 
			</thead>
			<tbody>
				<c:forEach items="${list}" var="news">
		            <tr>
		                <td style="width:55px;">${news['id'] }</td>
		                <td><a href="redirectByNews.do?newsId=${news['id'] }&cid=${news['cid'] }&type=${news['type'] }" target="_black">${news['title'] }</a></td>
		                <td style="width:60px;">
		                	<c:if test="${not empty news.titlepic }">
		               			<c:choose>
								    <c:when test="${fn:contains(news.titlepic,'http://')}">
								    	<img src="${news.titlepic }?x-oss-process=image/resize,h_25" height="25" />
								    </c:when>
								    <c:otherwise>
										<a href="<%=OSSUtil.url %>site/${news.siteid }/news/${news.titlepic }" target="_black"><img src="<%=OSSUtil.url %>site/${news.siteid }/news/${news.titlepic }?x-oss-process=image/resize,h_25" height="25" /></a>
								    </c:otherwise>
								</c:choose>
							</c:if>
		                </td>
		                <td style="width:100px;"><x:time linuxTime="${news['addtime'] }" format="yy-MM-dd hh:mm"></x:time></td>
		                <td style="text-align: center; width:140px;">
		                	<c:choose>
							    <c:when test="${siteColumn.type == 3 && siteColumn.editMode == 1}">
									<botton class="layui-btn layui-btn-small" onclick="editText('${siteColumn.templatePageViewName}');" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
							    </c:when>
							    <c:otherwise>
							    	<a class="layui-btn layui-btn-small" href="news.do?id=${news['id'] }" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></a>
							    </c:otherwise>
							</c:choose>
		                	
		                	<c:choose>
							    <c:when test="${siteColumn.type == 1 || siteColumn.type == 2}">
							    	<botton class="layui-btn layui-btn-small" onclick="changeColumn('${news['id'] }', '${news['cid'] }');" style="margin-left: 3px;"><i class="layui-icon">&#xe609;</i></botton>
							    	<botton class="layui-btn layui-btn-small" onclick="deleteNews('${news['id'] }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
							    </c:when>
							    <c:otherwise>
							    </c:otherwise>
							</c:choose>
		                	
		                </td>
		            </tr>
		        </c:forEach>
			</tbody>
		</table>
		<!-- 通用分页跳转 -->
		<jsp:include page="../iw/common/page.jsp" ></jsp:include>
		
		<div style="padding: 20px;color: gray;">
			<div>操作按钮提示:</div>
			<div><i class="layui-icon">&#xe642;</i> &nbsp;：对某篇文章进行编辑操作</div>
			<div><i class="layui-icon">&#xe609;</i> &nbsp;：对某篇文章进行转移操作，可转移到其他栏目下</div>
			<div><i class="layui-icon">&#xe640;</i> &nbsp;：删除某篇文章，栏目类型为新闻信息、图文信息的栏目才会有此按钮</div>
			<div>添加信息 &nbsp;：顶部的“添加信息”按钮，可以添加多条文章信息，栏目类型为新闻信息、图文信息的栏目才会有此按钮</div>
		</div>
		
	</div>
</div>

<script>
layui.use('element', function(){
  var element = layui.element;
});

/**
 * 删除新闻，传入要删除的新闻id
 */
function deleteNews(newsid){
	var dtv_confirm = layer.confirm('删除后不可恢复，您确定要删除此条信息吗？', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtv_confirm);
		$.showLoading('删除中');
		$.getJSON("<%=basePath %>news/deleteNewsForAjax.do?id="+newsid,function(obj){
			$.hideLoading();
			if(obj.result == '1'){
				$.toast("删除成功", function() {
					location.reload();
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
 * 编辑页面的内容代码， 复制于模板页面列表
 * 页面类型为独立页面
 * @param name TemplatePage.name要编辑的模版页面名字
 */
function editText(name){
	if(parent.currentMode == 2){
		//要将其切换回智能模式
		parent.window.htmledit_mode();
	}

	parent.document.getElementById("currentTemplatePageName").value = name;
	parent.loadIframe();
	
	try{
		parent.document.getElementById("currentTemplateType").innerHTML = '详情页模版';
		parent.document.getElementById("tongyong").style.display = '';
		parent.document.getElementById("lanmu").style.display = '';
		parent.document.getElementById("fenye").style.display = 'none';
		parent.document.getElementById("wenzhang").style.display = '';
		parent.document.getElementById("dongtailanmu").style.display = '';
		parent.document.getElementById("xiangqingduyou").style.display = '';
		parent.document.getElementById("liebiaoduyou").style.display = 'none';
	}catch(err){}
	
	parent.layer.close(index);
}

/**
 * 转移栏目
 * newsid 要转移的文章的id
 * columnid 要转移的文章所在的栏目id
 */
function changeColumn(newsid, columnid){
	var url = '<%=basePath %>news/newsChangeColumnForSelectColumn.do?newsid='+newsid+'&columnid='+columnid;
	layer.open({
		type: 2, 
		title:'转移到其他栏目', 
		area: ['250px', '500px'],
		shadeClose: true, //开启遮罩关闭
		content: url
	});
}
</script>

</body>
</html>