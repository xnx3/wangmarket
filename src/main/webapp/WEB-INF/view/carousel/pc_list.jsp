<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="轮播图管理"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Carousel_isshow.js"></script>

<div class="weui_panel">
      <div class="weui_panel_bd">
        <div class="weui_media_box weui_media_small_appmsg">
          <div class="weui_cells weui_cells_access">
          		<c:forEach items="${list}" var="carousel">
          			<% //String prefixUrl = OSSUtil.url+G.getCarouselPathS(request.getParameter("siteid")); 
          				String prefixUrl = "";
          			%>
               		<a class="weui_cell" href="/carousel/carousel.do?id=${carousel['id'] }&client=wap">
		              <div class="weui_cell_hd"><img src="<x:imgUrl img="${carousel.image }" prefixUrl="<%=prefixUrl %>"></x:imgUrl>" alt="" style="width:40px;margin-right:5px;display:block"></div>
		              <div class="weui_cell_bd weui_cell_primary">
		                <p>
		                	<span style="padding-left:20px;">
		                		<script type="text/javascript">document.write(isshow[${carousel['isshow']}]);</script>
		                	</span>
		                	<span style="padding-left:20px;">
		                		排序:${carousel['rank'] }
		                	</span>
		                </p>
		                
		              </div>
		              <span class="weui_cell_ft"></span>
		            </a>
                </tr>
               </c:forEach>
               
               <div class="demos-content-padded" style="margin:5px;">
			      <a href="/carousel/carousel.do?siteid=<%=request.getParameter("siteid") %>&client=wap" class="weui_btn weui_btn_primary">添加轮播图</a>
			    </div>
          </div>
        </div>
      </div>
    </div>

</body>
</html>

