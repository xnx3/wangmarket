<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.xnx3.wangmarket.admin.entity.Site"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="还原模版"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Site_mShowBanner.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_type.js"></script>
<script>
	var siteColumnType = type;
	var siteColumnUsed = used;
</script>

<!-- 还原网站，当前使用模板与备份的模板对比，有哪些地方做了修改的预览 -->

<style>
/*弹出提示，要是有修改，这里是修改的表格信息*/
.tip_table{
	width:100%;
}
.tip_table tr td, .tip_table tr th{
	border: 1px solid #e2e2e2;
	padding-left:10px;
	padding-right:10px;
}

/*弹出提示的标题*/
.tip_title{
	padding:5px;
	font-size:15px;
}
</style>


<body style="text-align:left; min-width:10px; padding:25px;">


<div class="layui-form-item">
	<label class="layui-form-label">生成时间</label>
	<div class="layui-input-block">
		<div style="padding-top: 7px;">
			<x:time linuxTime="${tcv.buckupsTemplateVO.time }"></x:time>
			<i class="layui-icon" style="font-size: 14px;color: #777777;border: 1px solid #5FB878;border-radius: 10px;padding: 1px;cursor: pointer;" onclick="layer.msg('生成的时间，此是什么时间生成的，什么时间导出的。', {shade: 2});">&#xe607;</i>  
		</div>
		
	</div>
</div>


<script>
//弹出的提示，背景颜色
var tipColor = "#0FA6A8";
//弹出的提示，宽度，如 auto 、 310px
var tipWidth = 'auto';

//鼠标放上后会弹出修改项的提示，这里是修改项表格的数据填充
var tableBefore = '<table class="tip_table"><thead><tr><th>修改项</th><th>当前值</th><th>还原为</th></tr></thead><tbody>';
var tableAfter = '</tbody></table>';
/**
 * 生成Table表格的tr标签以及其内容
 * @param name 修改项的名字，文字名称
 * @param currentValue 修改项当前的值
 * @param backupsValue 修改项还原后的值
 * @return tr标签以及其内容
 */
function generateTableTrTag(name, currentValue, backupsValue){
	return '<tr><td>' + name + '</td><td>' + currentValue + '</td><td>' + backupsValue + '</td></tr>';
}
</script>

<form class="layui-form" action="">

	<div class="layui-form-item">
		<label class="layui-form-label">模板页面</label>
		<div class="layui-input-block">
			<c:if test="${fn:length(tcv.templatePageList) == 0 }">
				<div style="padding-top: 7px;">无数据</div>
			</c:if> 
			<c:forEach items="${tcv.templatePageList}" var="v">
				<span id="templatePage_${v.backupsTemplatePage.name }">
					<input type="checkbox"  value="1" name="templatePage_${v.backupsTemplatePage.name }" title="${v.backupsTemplatePage.remark }(${v.backupsTemplatePage.name })" <c:if test="${v.result == 0}">disabled</c:if>>
				</span>
				<script>
				$(function(){
					<c:choose>
					    <c:when test="${v.result == 0}">
					        var text = '当前网站内未有改动，<b>无需导入</b>';
					    </c:when>
					    <c:when test="${v.result == 1}">
					        var text = '<div class="tip_title">当前网站内有修改，<b>可还原</b></div>';
					        var tableTr = '';
					        <c:forEach items="${v.updateListInfo}" var="updateInfo">
					        	console.log('${updateInfo}');
					        	if('${updateInfo}' == 'type'){
					        		tableTr = tableTr + generateTableTrTag('页面类型', '${v.currentTemplatePage.type }', '${v.backupsTemplatePage.type }');
					        	}else if ('${updateInfo}' == 'text') {
					        		tableTr = tableTr + generateTableTrTag('页面内容', '${fn:length(v.currentTemplatePageDataText)}字', '${fn:length(v.backupsTemplatePageDataText)}字');
								}
					        </c:forEach>
					        if(tableTr.length > 0){
					        	text = text + tableBefore + tableTr + tableAfter;
					        }
					    </c:when>
					    <c:when test="${v.result == 2}">
					        var text = '当前网站内未发现其存在，<b>可导入</b>';
					    </c:when>
					</c:choose>
					
					var templatePage_${v.backupsTemplatePage.name }_tipindex = 0;
					$("#templatePage_${v.backupsTemplatePage.name }").hover(function(){
						templatePage_${v.backupsTemplatePage.name }_tipindex = layer.tips(text, '#templatePage_${v.backupsTemplatePage.name }', {
							tips: [2, tipColor], //还可配置颜色
							time:0,
							tipsMore: true,
							area : [tipWidth , 'auto']
						});
					},function(){
						layer.close(templatePage_${v.backupsTemplatePage.name }_tipindex);
					})
				})
				</script>
			</c:forEach>
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">模板变量</label>
		<div class="layui-input-block">
			<c:if test="${fn:length(tcv.templateVarList) == 0 }">
				<div style="padding-top: 7px;">无数据</div>
			</c:if> 
			<c:forEach items="${tcv.templateVarList}" var="v">
				<span id="templateVar_${v.backupsTemplateVar.varName }">
					<input type="checkbox" value="1" name="templateVar_${v.backupsTemplateVar.varName }" title="${v.backupsTemplateVar.remark }(${v.backupsTemplateVar.varName })" <c:if test="${v.result == 0}">disabled</c:if>>
				</span>
				<script>
				$(function(){
					<c:choose>
					    <c:when test="${v.result == 0}">
					        var text = '当前网站内未有改动，<b>无需导入</b>';
					    </c:when>
					    <c:when test="${v.result == 1}">
					        var text = '<div class="tip_title">当前网站内有修改，<b>可还原</b></div>';
					        var tableTr = '';
					        <c:forEach items="${v.updateListInfo}" var="updateInfo">
					        	if ('${updateInfo}' == 'text') {
					        		tableTr = tableTr + generateTableTrTag('变量内容', '${fn:length(v.currentTemplateVarDataText)}字', '${fn:length(v.backupsTemplateVarDataText)}字');
								}
					        </c:forEach>
					        if(tableTr.length > 0){
					        	text = text + tableBefore + tableTr + tableAfter;
					        }
					    </c:when>
					    <c:when test="${v.result == 2}">
					        var text = '当前网站内未发现其存在，<b>可导入</b>';
					    </c:when>
					</c:choose>
					
					var templateVar_${v.backupsTemplateVar.varName }_tipindex = 0;
					$("#templateVar_${v.backupsTemplateVar.varName }").hover(function(){
						templateVar_${v.backupsTemplateVar.varName }_tipindex = layer.tips(text, '#templateVar_${v.backupsTemplateVar.varName }', {
							tips: [2, tipColor], //还可配置颜色
							time:0,
							tipsMore: true,
							area : [tipWidth , 'auto']
						});
					},function(){
						layer.close(templateVar_${v.backupsTemplateVar.varName }_tipindex);
					})
				})
				</script>
			</c:forEach>	
			
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">网站栏目</label>
		<div class="layui-input-block">
			<c:if test="${fn:length(tcv.siteColumnList) == 0 }">
				<div style="padding-top: 7px;">无数据</div>
			</c:if> 
			<c:forEach items="${tcv.siteColumnList}" var="v">
				<span id="siteColumn_${v.backupsSiteColumn.codeName }">
					<input type="checkbox" value="1" name="siteColumn_${v.backupsSiteColumn.codeName }" title="${v.backupsSiteColumn.name }(${v.backupsSiteColumn.codeName })" <c:if test="${v.result == 0}">disabled</c:if>>
				</span>
				<script>
				$(function(){
					<c:choose>
					    <c:when test="${v.result == 0}">
					        var text = '当前网站内未有改动，<b>无需导入</b>';
					    </c:when>
					    <c:when test="${v.result == 1}">
					        var text = '<div class="tip_title">当前网站内有修改，<b>可还原</b></div>';
					        var tableTr = '';
					        <c:forEach items="${v.updateListInfo}" var="updateInfo">
					        	switch ('${updateInfo}') {
								case 'icon':
									tableTr = tableTr + generateTableTrTag('栏目图标', '${v.currentSiteColumn.icon }', '${v.backupsSiteColumn.icon }');
									break;
								case 'inputModelCodeName':
									tableTr = tableTr + generateTableTrTag('输入模型', '${v.currentSiteColumn.inputModelCodeName }', '${v.backupsSiteColumn.inputModelCodeName }');
									break;
								case 'icon':
									tableTr = tableTr + generateTableTrTag('父栏目码', '${v.currentSiteColumn.parentCodeName }', '${v.backupsSiteColumn.parentCodeName }');
									break;
								case 'listNum':
									tableTr = tableTr + generateTableTrTag('列表条数', '${v.currentSiteColumn.listNum }', '${v.backupsSiteColumn.listNum }');
									break;
								case 'type':
									tableTr = tableTr + generateTableTrTag('栏目类型', siteColumnType['${v.currentSiteColumn.type }'], siteColumnType['${v.backupsSiteColumn.type }']);
									break;
								case 'used':
									tableTr = tableTr + generateTableTrTag('是否启用', siteColumnUsed['${v.currentSiteColumn.used }'], siteColumnUsed['${v.backupsSiteColumn.used }']);
									break;
								case 'templatePageListName':
									tableTr = tableTr + generateTableTrTag('列表模板', '${v.currentSiteColumn.templatePageListName }', '${v.backupsSiteColumn.templatePageListName }');
									break;
								case 'templatePageViewName':
									tableTr = tableTr + generateTableTrTag('详情模板', '${v.currentSiteColumn.templatePageViewName }', '${v.backupsSiteColumn.templatePageViewName }');
									break;
								case 'editMode':
									tableTr = tableTr + generateTableTrTag('编辑模式', '${v.currentSiteColumn.templatePageViewName }', '${v.backupsSiteColumn.templatePageViewName }');
									break;	
								default:
									break;
								}
					        	
					        </c:forEach>
					        if(tableTr.length > 0){
					        	text = text + tableBefore + tableTr + tableAfter;
					        }
					    </c:when>
					    <c:when test="${v.result == 2}">
					        var text = '当前网站内未发现其存在，<b>可导入</b>';
					    </c:when>
					</c:choose>
					
					var siteColumn_${v.backupsSiteColumn.codeName }_tipindex = 0;
					$("#siteColumn_${v.backupsSiteColumn.codeName }").hover(function(){
						siteColumn_${v.backupsSiteColumn.codeName }_tipindex = layer.tips(text, '#siteColumn_${v.backupsSiteColumn.codeName }', {
							tips: [2, tipColor], //还可配置颜色
							time:0,
							tipsMore: true,
							area : [tipWidth , 'auto']
						});
					},function(){
						layer.close(siteColumn_${v.backupsSiteColumn.codeName }_tipindex);
					})
				})
				</script>
			</c:forEach>	
		</div>
	</div>
	
	
	<div class="layui-form-item">
		<label class="layui-form-label">输入模型</label>
		<div class="layui-input-block">
			<c:if test="${fn:length(tcv.inputModelList) == 0 }">
				<div style="padding-top: 7px;">无数据</div>
			</c:if> 
			<c:forEach items="${tcv.inputModelList}" var="v">
				<span id="inputModel_${v.backupsInputModel.codeName }">
					<input type="checkbox" value="1" name="inputModel_${v.backupsInputModel.codeName }" title="${v.backupsInputModel.remark }(${v.backupsInputModel.codeName })" <c:if test="${v.result == 0}">disabled</c:if>>
				</span>
				<script>
				$(function(){
					<c:choose>
					    <c:when test="${v.result == 0}">
					        var text = '当前网站内未有改动，<b>无需导入</b>';
					    </c:when>
					    <c:when test="${v.result == 1}">
					        var text = '<div class="tip_title">当前网站内有修改，<b>可还原</b></div>';
					        var tableTr = '';
					        <c:forEach items="${v.updateListInfo}" var="updateInfo">
					        	if ('${updateInfo}' == 'text') {
					        		tableTr = tableTr + generateTableTrTag('模型内容', '${fn:length(v.currentInputModel.text)}字', '${fn:length(v.backupsInputModel.text)}字');
								}
					        </c:forEach>
					        if(tableTr.length > 0){
					        	text = text + tableBefore + tableTr + tableAfter;
					        }
					    </c:when>
					    <c:when test="${v.result == 2}">
					        var text = '当前网站内未发现其存在，<b>可导入</b>';
					    </c:when>
					</c:choose>
					
					var inputModel_${v.backupsInputModel.codeName }_tipindex = 0;
					$("#inputModel_${v.backupsInputModel.codeName }").hover(function(){
						inputModel_${v.backupsInputModel.codeName }_tipindex = layer.tips(text, '#inputModel_${v.backupsInputModel.codeName }', {
							tips: [2, tipColor], //还可配置颜色
							time:0,
							tipsMore: true,
							area : [tipWidth , 'auto']
						});
					},function(){
						layer.close(inputModel_${v.backupsInputModel.codeName }_tipindex);
					})
				})
				</script>
			</c:forEach>	
		</div>
	</div>
	
	<div class="layui-form-item" style="padding-bottom: 20px;">
		<div class="layui-input-block">
			<button class="layui-btn" lay-submit lay-filter="formSubmit">立即导入</button>
			<button type="reset" class="layui-btn layui-btn-primary">重置</button>
		</div>
	</div>
	
	<div class="layui-form-item" style="padding-bottom: 20px; padding-left:35px; color: gray;">
		提示：&nbsp;&nbsp;&nbsp;
		<div style="padding-left:60px;padding-right: 10px;">
			鼠标选中想要还原的项，即可立即还原
			<br/>另外，您可通过备份，先讲此模版进行备份，别还原后，丢失资料
		</div>
	</div>
	
</form>






<script>

layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form
  ,layer = layui.layer
  ,layedit = layui.layedit
  ,laydate = layui.laydate;
  
  //自适应弹出层大小
	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	parent.layer.iframeAuto(index);
  
  //创建一个编辑器
  var editIndex = layedit.build('LAY_demo_editor');
  /* 
  //监听指定开关
  form.on('switch(switchTest)', function(data){
    layer.msg('开关checked：'+ (this.checked ? 'true' : 'false'), {
      offset: '6px'
    });
    layer.tips('温馨提示：请注意开关状态的文字可以随意定义，而不仅仅是ON|OFF', data.othis)
  }); */
  
  
  //监听提交
  form.on('submit(formSubmit)', function(data){
		var d=$("form").serialize();
		if(d.length == 0){
			msg.failure('尚未选择');
			return false;
		}
		msg.loading('还原中');
        $.post("/template/restoreTemplateSubmit.do", d, function (result) { 
        	msg.close();
        	var obj = JSON.parse(result);
        	if(obj.result == '1'){
        		parent.msg.success('操作成功');
        		parent.layer.close(index);
        	}else if(obj.result == '0'){
        		parent.msg.failure(obj.info);
        	}else{
        		parent.msg.failure(result);
        	}
         }, "text");
		
    return false;
  });
});

</script>

</body>
</html>