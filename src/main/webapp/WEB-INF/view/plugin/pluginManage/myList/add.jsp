<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="用户列表"/>
</jsp:include>

<script type="text/javascript">
	//是否显示下拉列表
	function selectShow(name,option){
		var selectHtml = '';
		
		selectHtml += '<select name="' + name + '" id="'+ name + '">';
		if(option == 0){
			selectHtml += '	<option selected value="0">不显示</option>';
			selectHtml += '	<option value="1">显示</option>';
		}else{
			selectHtml += '	<option value="0">不显示</option>';
			selectHtml += '	<option selected value="1">显示</option>';
		}
		selectHtml += '</select>';
		document.write(selectHtml);
		
	}
	
	//是否支持下拉列表
	function selectSupport(name,option){
		var selectHtml = '';
		
		selectHtml += '<select name="' + name + '" id="'+ name + '">';
		
		if(option == 0){
			selectHtml += '	<option selected value="0">不支持</option>';
			selectHtml += '	<option value="1">支持</option>';
		}else{
			selectHtml += '	<option value="0">不支持</option>';
			selectHtml += '	<option selected value="1">支持</option>';
		}
		selectHtml += '</select>';
		document.write(selectHtml);
	}
</script>

<form id="form" class="layui-form" onsubmit="javascript:;" style="padding:20px; padding-top:35px; margin-bottom: 10px;">
	<div class="layui-form-item">
		<label class="layui-form-label">插件id</label>
		<div class="layui-input-block">
			<c:choose>
				<c:when test="${plugin != null }">
					<input type="text"  readonly="readonly" name="id" id="pluginId"  class="layui-input" value="${plugin.id }">
				</c:when>
				<c:otherwise>
					<input type="text" name="id" id="pluginId"  class="layui-input" value="${plugin.id }">
				</c:otherwise>
			</c:choose>
			
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">插件名称</label>
		<div class="layui-input-block">
			<input type="text" name="menuTitle" id="menuTitle" class="layui-input" value="${plugin.menuTitle }">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">CMS后台</label>
		<div class="layui-input-block">
 			<script type="text/javascript">
 				selectShow('applyToCMS',${plugin.applyToCMS });
 			</script>
		</div>
	</div>	
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">电脑模式网站后台</label>
		<div class="layui-input-block">
			<script type="text/javascript">
 				selectShow('applyToPC',${plugin.applyToPC });
 			</script>
		</div>
	</div>
	
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">手机模式网站后台</label>
		<div class="layui-input-block">
			<script type="text/javascript">
 				selectShow('applyToWAP',${plugin.applyToWAP });
 			</script>
		</div>
	</div>
	
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">代理后台</label>
		<div class="layui-input-block">
			<script type="text/javascript">
 				selectShow('applyToAgency',${plugin.applyToAgency });
 			</script>
		</div>
	</div>
	
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">总管理后台</label>
		<div class="layui-input-block">
			<script type="text/javascript">
 				selectShow('applyToSuperAdmin',${plugin.applyToSuperAdmin });
 			</script>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="label_columnName">插件简介</label>
		<div class="layui-input-block">
			<textarea rows="5" cols="20" placeholder="右下角可调整大小" class="layui-input" name="intro" id="intro">${plugin.intro }</textarea>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">插件版本号</label>
		<div class="layui-input-block">
			<input type="text" name="version" placeholder="如 1.0 则是 100000000; 1.2.1 则是 100200100; 2.13.3则是 200130300" id="version" class="layui-input" value="${plugin.version }">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">支持最低版本</label>
		<div class="layui-input-block">
			<input type="text" name="wangmarketVersionMin" placeholder="同插件版本号" id="wangmarketVersionMin" class="layui-input" value="${plugin.wangmarketVersionMin }">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">作者名称</label>
		<div class="layui-input-block">
			<input type="text" name="authorName" id="authorName" class="layui-input" value="${plugin.authorName }">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">OSS存储是否支持</label>
		<div class="layui-input-block">
			<script type="text/javascript">
				selectSupport('supportOssStorage' , ${plugin.supportOssStorage });
			</script>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">本地存储是否支持</label>
		<div class="layui-input-block">
			<script type="text/javascript">
				selectSupport('supportLocalStorage' , ${plugin.supportLocalStorage });
			</script>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">SLS是否支持</label>
		<div class="layui-input-block">
			<script type="text/javascript">
				selectSupport('supportSls' , ${plugin.supportSls });
			</script>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">MySQL是否支持</label>
		<div class="layui-input-block">
			<script type="text/javascript">
				selectSupport('supportMysql' , ${plugin.supportMysql });
			</script>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">Sqlite是否支持</label>
		<div class="layui-input-block">
			<script type="text/javascript">
				selectSupport('supportSqlite' , ${plugin.supportSqlite });
			</script>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">网市场免费版</label>
		<div class="layui-input-block">
			<script type="text/javascript">
				selectSupport('supportFreeVersion' , ${plugin.supportFreeVersion });
			</script>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label" id="columnEditMode">网市场授权版本</label>
		<div class="layui-input-block">
			<script type="text/javascript">
				selectSupport('supportAuthorizeVersion' , ${plugin.supportAuthorizeVersion });
			</script>
		</div>
	</div>
	
	<div class="layui-form-item">
	    <div class="layui-input-block">
			<button class="layui-btn" lay-submit lay-filter = "formSubmit">立即保存</button>
			<button type="reset" class="layui-btn layui-btn-primary">重置</button>
	    </div>
	</div>
</form>

<script>
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use(['form', 'layedit', 'laydate'], function(){
	var form = layui.form;
	//监听提交
	form.on('submit(formSubmit)', function(data){
		//表单信息验证d
		//如果是删除操作的话不需要验证消息
		
		//检查进行的操作是不是删除操作，删除的话没必要进行信息校验，直接修改学校删除状态即可
		if($("#pluginId").val() == ''){
			iw.msgFailure("请输入插件id");
			return false;
		}
		if($("#menuTitle").val() == ''){
			iw.msgFailure("请输入插件名称");
			return false;
		}
		if($("#intro").val() == ''){
			iw.msgFailure("请输入插件简介");
			return false;
		}
		if($("#version").val() == ''){
			iw.msgFailure("请输入插件版本号");
			return false;
		}
		if($("#wangmarketVersionMin").val() == ''){
			iw.msgFailure("请输入最低网市场的支持版本");
			return false;
		}
		//表单序列化
		var d = $("#form").serialize();
		parent.iw.loading("保存中");
        $.post("/plugin/pluginManage/addSubmit.do", d, function (result) {
        	parent.iw.loadClose();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.parent.iw.msgSuccess("操作成功");
        		parent.layer.close(index);	//关闭当前窗口
        		parent.location.reload();	//刷新父窗口列表
        	}else if(obj.result == '0'){
        		parent.iw.msgFailure(obj.info);
        	}else{
        		parent.iw.msgFailure(result);
        	}
         }, "text");
        
		return false;
	});
});
</script>
<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>