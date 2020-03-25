<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.wangmarket.admin.entity.Site"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
    <jsp:param name="title" value="栏目导航"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_used.js"></script>
<script src="/<%=Global.CACHE_FILE %>SiteColumn_type.js"></script>

<table class="layui-table" style="margin:0px;">
  <thead>
    <tr>
      <th>栏目名称</th>
      <th>栏目代码</th>
      <th>类型</th>
      <th title="是否在模版中，使用动态栏目代码调取子栏目列表时，显示此栏目">子栏目显示</th>
	  <th>排序</th>
      <th>操作</th>
    </tr> 
  </thead>
  <tbody class="tile__listedit" id="columnList">
  	<!-- display 显示或者隐藏，是否在导航中显示。若为0，则不加入排序 -->
  	<c:forEach items="${list}" var="siteColumnTreeVO">
        <tr id="${siteColumnTreeVO.siteColumn.id }">
        	<td width="140" onclick="javascript:window.open('/${siteColumnTreeVO.siteColumn.codeName }.html?domain=${site.domain }.<%=G.getFirstAutoAssignDomain() %>');" style="cursor: pointer;">${siteColumnTreeVO.siteColumn.name }</td>
        	<td>${siteColumnTreeVO.siteColumn.codeName }</td>
            <td width="100"><script type="text/javascript">document.write(type['${siteColumnTreeVO.siteColumn.type}']);</script></td>
            <td width="80"><script type="text/javascript">document.write(used['${siteColumnTreeVO.siteColumn.templateCodeColumnUsed}']);</script></td>
            <td width="50" onclick="updateRank('${siteColumnTreeVO.siteColumn.id }', '${siteColumnTreeVO.siteColumn.rank }', '${siteColumnTreeVO.siteColumn.name }');" style="cursor:pointer;">${siteColumnTreeVO.siteColumn.rank }</td>
            <td width="160">
            	<botton class="layui-btn layui-btn-sm" onclick="editColumn('${siteColumnTreeVO.siteColumn.id }',true);" style="margin-left: 3px;"><i class="layui-icon">&#xe630;</i></botton>
            	<botton class="layui-btn layui-btn-sm" onclick="editColumn('${siteColumnTreeVO.siteColumn.id }',false);" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
            	<botton class="layui-btn layui-btn-sm" onclick="deleteColumn('${siteColumnTreeVO.siteColumn.id }', '${siteColumnTreeVO.siteColumn.name }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
			</td>
        </tr>
        
        <!-- 其下级栏目 -->
        <c:if test="${not empty siteColumnTreeVO.list }">
			<c:forEach items="${siteColumnTreeVO.list}" var="subSCT">
		        <tr id="${subSCT.siteColumn.id }">
		        	<td width="140" onclick="javascript:window.open('/${subSCT.siteColumn.codeName }.html?domain=${site.domain }.<%=G.getFirstAutoAssignDomain() %>');" style="cursor: pointer;"><span style="padding-left:20px;">${subSCT.siteColumn.name }</span></td>
		        	<td><span style="padding-left:20px;">${subSCT.siteColumn.codeName }</span></td>
		            <td width="100"><script type="text/javascript">document.write(type['${subSCT.siteColumn.type}']);</script></td>
		            <td width="80"><script type="text/javascript">document.write(used['${subSCT.siteColumn.templateCodeColumnUsed}']);</script></td>
		            <td width="50" onclick="updateRank('${subSCT.siteColumn.id }', '${subSCT.siteColumn.rank }', '${subSCT.siteColumn.name }');" style="cursor:pointer;">&nbsp;&nbsp;&nbsp;&nbsp;${subSCT.siteColumn.rank }</td>
		            <td width="160">
		            	<botton class="layui-btn layui-btn-sm" onclick="editColumn('${subSCT.siteColumn.id }',true);" style="margin-left: 3px;"><i class="layui-icon">&#xe630;</i></botton>
		            	<botton class="layui-btn layui-btn-sm" onclick="editColumn('${subSCT.siteColumn.id }',false);" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
		            	<botton class="layui-btn layui-btn-sm" onclick="deleteColumn('${subSCT.siteColumn.id }', '${subSCT.siteColumn.name }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
					</td>
		        </tr>
		    </c:forEach>
		</c:if> 
    </c:forEach>
  </tbody>
</table>

<div style="padding:15px;">
	<button class="layui-btn" onclick="addColumn();" style="margin-left: 10px;margin-bottom: 20px;"><i class="layui-icon" style="padding-right:8px; font-size: 22px;">&#xe608;</i>添加栏目</button>
</div>
<div style="padding-right:35px; text-align: right;margin-top: -66px;">
	
	提示：&nbsp;&nbsp;&nbsp;
	<botton class=""><i class="layui-icon">&#xe642;</i></botton><span style="padding-left:12px;padding-right: 30px;">编辑</span>
	<botton class=""><i class="layui-icon">&#xe640;</i></botton><span style="padding-left:12px;padding-right: 30px;">删除</span>
	<botton class=""><i class="layui-icon">&#xe630;</i></botton><span style="padding-left:12px;padding-right: 20px;">复制一个现有的栏目快速创建新栏目</span>
</div>
 
<script type="text/javascript">
/**
 * 编辑栏目
 * siteColumnId 要编辑的栏目的id
 * isCopy 是否是复制一个新的栏目出来进入编辑状态，1是复制的，    0不是复制的，属于正常的添加或者修改
 */
function editColumn(siteColumnId, isCopy){
	var url = '/column/popupColumnForTemplate.do';
	var index = layer.open({
		type: 2, 
		//title: false,
		closeBtn: 1,
		//title:(isCopy? '复制一个栏目快速创建':'修改栏目'),
		area: ['490px', '600px'],
		shadeClose: true, //开启遮罩关闭
		content: url+'?id='+siteColumnId+(isCopy? '&isCopy=1':''),
		title:false, 
		closeBtn: 1
	});
	layer.style(index, {
	  overflow: 'hidden'
	}); 
}

/**
 * 删除栏目
 * siteColumnId 要编辑的栏目的id
 * name 要删除的栏目的栏目名字
 */
function deleteColumn(siteColumnId, name){
	var dtv_confirm = layer.confirm('确定要删除“'+name+'”吗? ', {
	  btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtv_confirm);
		parent.msg.loading('删除中');
		$.post('/column/delete.do?id='+siteColumnId,function(obj){
			parent.msg.close();
			if(obj.result == '1'){
				parent.msg.success("删除成功");
				window.location.reload();	//刷新当前页
	     	}else if(obj.result == '0'){
	     		parent.msg.success(obj.info);
	     	}else{
	     		parent.msg.success('操作失败');
	     	}
		});
	}, function(){
	});
}

/**
 * 添加栏目
 * siteColumnId 要编辑的栏目的id
 */
function addColumn(siteColumnId){
	var url = '/column/popupColumnForTemplate.do';
	layer.open({
		type: 2, 
		//title:'添加栏目', 
		area: ['490px', '600px'],
		shadeClose: true, //开启遮罩关闭
		content: url,
		title:false, 
		closeBtn: 1
	});
	
}

/**
 * 更改某项的排序
 * @param id 要修改的 某项的id编号，要改那一项
 * @param rank 当前的排序序号，默认值
 * @param name 栏目名字，仅仅只是提示作用
 */
function updateRank(id,rank,name){
	layer.prompt({title: '请输入排序数字，数字越小越靠前', formType: 3, value: ''+rank}, function(text, index){
		parent.msg.loading("保存中");    //显示“操作中”的等待提示
		$.post('updateRank.do?id='+id+'&rank='+text, function(data){
		    parent.msg.close();    //关闭“操作中”的等待提示
		    if(data.result == '1'){
		        //由最外层发起提示框
				parent.msg.success('操作成功');
				//刷新当前页
				window.location.reload();	
		     }else if(data.result == '0'){
		         parent.msg.failure(data.info);
		     }else{
		         parent.msg.failure('操作失败');
		     }
		});
		
	});
}
</script>

<jsp:include page="../iw/common/foot.jsp"></jsp:include>