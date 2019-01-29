<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="模版列表"/>
</jsp:include>

<style>
.list{}
.list>div{
	width:23%;
	padding:1%;
	float:left;
	padding-bottom: 35px;
}
.list>div .imgA img{
	width:100%;
}
.list>div div{
	text-align:center;
}
.list>div .code{
	padding-top:10px;
	color:gray;
}
.list>div .name{
	width:100%;
	text-overflow:ellipsis;
	overflow:hidden;
	white-space:nowrap;
}

/*自动分词的搜索右侧的热词*/
.hotWordList{
}
.hotWordList a{
	padding-left: 5px;
    font-size: 14px;
}

@media screen and (max-width:800px){
	.list>div{
		width:48%;
		padding:1%;
		float:left;
		padding-bottom: 35px;
	}
}
@media screen and (max-width:380px){
	.list>div{
		width:98%;
		padding:1%;
		float:left;
		padding-bottom: 35px;
	}
}
</style>
<div>
	<jsp:include page="../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
		<jsp:include page="../iw/common/list/formSearch_input.jsp">
			<jsp:param name="iw_label" value="模版"/>
			<jsp:param name="iw_name" value="name"/>
		</jsp:include>
		<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
		
		<span class="hotWordList">
			<c:forEach items="${hotWordList}" var="hotWord">
				<a href="?name=${hotWord.word }">${hotWord.word }(${hotWord.num })</a>
			</c:forEach>
		</span>
		
	</form>	
</div>

<div class="list">
	<c:forEach items="${list}" var="template">
		<div>
			<a class="imgA" href="//res.weiunity.com/template_external/${template.code }/index.html" target="_black">
				<img alt="" src="//res.weiunity.com/template_external/${template.code }/preview.jpg">
			</a>
			<div class="code">编号：${template.code }</div>
			<div class="name">${template.name }</div>
		</div>
	</c:forEach>
</div>

<!-- 通用分页跳转 -->
<div style="clear:both;"></div>
<jsp:include page="../iw/common/page.jsp" ></jsp:include>

</body>
</html>