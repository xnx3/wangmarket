<%@page import="com.xnx3.wangmarket.admin.entity.Carousel"%>
<%@page import="com.xnx3.wangmarket.admin.entity.Site"%>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%
Site site = (Site)request.getAttribute("site");
%>
${headHtml}
<script src="/<%=Global.CACHE_FILE %>Carousel_isshow.js"></script>

<body>
${topHtml}
<script type="text/javascript">
//form提交验证
function validate(){
	msg.loading('正在上传中');
	return true;
}
</script>
<form action="carouselSubmit.do" class="weui_cells weui_cells_form" enctype="multipart/form-data" method="post"  onsubmit="return validate();">
                        	<input type="hidden" value="${carousel.id }" name="id" />
                        	<input type="hidden" value="${site.id }" name="siteid" />
                        	<div class="weui_cell">
						        <div class="weui_cell_hd"><label class="weui_label">图片：</label></div>
						        <div class="weui_cell_bd weui_cell_primary">
						        	<input type="file" class="weui_input" value="" name="imageFile" />
						        </div>
						        <div class="weui_cell_ft">
						        	${imageImage }
						        </div>
						     </div>
                        	
                        	<div class="weui_cell">
								<div class="weui_cell_hd"><label class="weui_label">跳转链接：</label></div>
								<div class="weui_cell_bd weui_cell_primary">
									<input class="weui_input" type="text" name="url" placeholder="可不填写" value="${carousel.url}" />
								</div>
							</div>
							
							<div class="weui_cell" style="display:none;">
						        <div class="weui_cell_hd"><label for="name" class="weui_label">首页显示：</label></div>
						        <div class="weui_cell_bd weui_cell_primary">
						        	<select name="isshow" class="weui_input">
		                        		<script type="text/javascript">writeSelectAllOptionForisshow_('${carousel.isshow}','请选择');</script>
		                        	</select>
						        </div>
						    </div>
						    <div class="weui_cell" style="display:none">
								<div class="weui_cell_hd"><label class="weui_label">排序：</label></div>
								<div class="weui_cell_bd weui_cell_primary">
									<input class="weui_input" type="text" name="rank" placeholder="数字越小越靠前" value="${carousel.rank}">
								</div>
							</div>
							
						    <div class="weui_btn_area">
						        <input class="weui_btn weui_btn_primary" type="submit" value="保存">
						    </div>
                        </form>

</body>
</html>
