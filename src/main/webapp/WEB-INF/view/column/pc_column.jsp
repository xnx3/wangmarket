<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
${headHtml}
<script src="/<%=Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_type.js"></script>
<script type="text/javascript">
	function selectTypeChange(){
		if(document.getElementById("type").options[4].selected){
			document.getElementById("xnx3_url").style.display="";
		}else{
			document.getElementById("xnx3_url").style.display="none";
		}
	}
</script>

<body>
${topHtml}
							
                        <form action="saveColumn.do" id="form" class="weui_cells weui_cells_form" enctype="multipart/form-data" method="post">
                        	<input type="hidden" value="${siteColumn.id }" name="id">
                        	<input type="hidden" value="${site.id }" name="siteid">
                        	<input type="hidden" value="pc" name="client">
                        	<!-- <div class="weui_cell">
								<div class="weui_cell_hd"><label class="weui_label">所属站点：</label></div>
								<div class="weui_cell_bd weui_cell_primary">
									${site.name }
								</div>
							</div> -->
							<div class="weui_cell">
								<div class="weui_cell_hd"><label class="weui_label">名称：</label></div>
								<div class="weui_cell_bd weui_cell_primary">
									<input class="weui_input" type="text" name="name" placeholder="(20个汉字之内，必填)" value="${siteColumn.name }">
								</div>
							</div>
							
							<div class="weui_cell">
						        <div class="weui_cell_hd"><label class="weui_label">图标：</label></div>
						        <div class="weui_cell_bd weui_cell_primary">
						        	<input type="file" class="weui_input" value="" name="iconFile" />
						        </div>
						        <div class="weui_cell_ft">
						        	${iconImage }
						        </div>
						     </div>
						     
							<div class="weui_cell">
						        <div class="weui_cell_hd"><label for="name" class="weui_label">类型：</label></div>
						        <div class="weui_cell_bd weui_cell_primary">
						        	<select name="type" id="type" onchange="selectTypeChange()" class="weui_input">
					                	<script type="text/javascript">writeSelectAllOptionFortype_('${siteColumn['type'] }','请选择');</script>
					                </select> 
						        </div>
						    </div>
                            <div class="weui_cell" id="xnx3_url">
								<div class="weui_cell_hd"><label class="weui_label">网址：</label></div>
								<div class="weui_cell_bd weui_cell_primary">
									<input class="weui_input" type="text" name="url" placeholder="请输入目标网页链接地址" value="${siteColumn.url }">
								</div>
							</div>
                            <div class="weui_cell">
						        <div class="weui_cell_hd"><label for="name" class="weui_label">显示：</label></div>
						        <div class="weui_cell_bd weui_cell_primary">
						        	<select name="used" class="weui_input">
			                        	<script type="text/javascript">writeSelectAllOptionForused_('${siteColumn.used }','请选择');</script>
			                        </select>
						        </div>
						      </div>
                            <div>    
                            
                            <div class="weui_btn_area">
                            	<button class="weui_btn weui_btn_primary" onclick="return true;">保存</button>
						    </div>
                        </form>
                        
${footHtml}
</body>
<script type="text/javascript">
selectTypeChange();

function submitForm(){
	
}
</script>
</html>
