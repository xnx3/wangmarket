<%@page import="com.xnx3.j2ee.entity.User"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<jsp:include page="../../../iw/common/head.jsp">
	<jsp:param name="title" value="插件信息"/>
</jsp:include>

<script type="text/javascript">
//判断是否显示的函数
function yesOrNoShow(msg){
	if(msg == 1){
		document.write('显示');
	}else{
		document.write('不显示');;
	}
}
//判断是否支持的函数
function yesOrNoSupport(msg){
	if(msg == 1){
		document.write('支持');
	}else{
		document.write('不支持');
	}
}
//格式化版本号将10010010转换为1.1.1格式
function versionFormat(version){
	version = version + '';
	var add = 9 - version.length;
	for(var i = 0 ; i < add; i ++) {
		version = "0" + version;
	}
	var one = version.substring(0,3).replace(/\b(0+)/gi, "");
	var two = version.substring(3,6).replace(/\b(0+)/gi, "");
	var three = version.substring(6,9).replace(/\b(0+)/gi, "");
	if(one == ''){
		one = '0';
	}
	if(two == ''){
		two = '0';
	}
	if(three == ''){
		document.write(one + '.' + two);
	}else {
		document.write(one + '.' + two + '.' + three);
	}
}

</script>

<table class="layui-table iw_table">
	<tbody>
		<tr>
			<td class="iw_table_td_view_name">插件ID</td>
			<td>${plugin.id }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">插件名称</td>
			<td>${plugin.menuTitle }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">插件作者</td>
			<td>${plugin.authorName }</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">CMS网站后台</td>
			<td>
				<script type="text/javascript">
					yesOrNoShow(${plugin.applyToCMS});
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">手机管理后台</td>
			<td>
				<script type="text/javascript">
					yesOrNoShow(${plugin.applyToWAP});
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">代理管理后台</td>
			<td>
			<script type="text/javascript">
					yesOrNoShow(${plugin.applyToAgency});
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">总管理后台</td>
			<td>
				<script type="text/javascript">
					yesOrNoShow(${plugin.applyToSuperAdmin});
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">OOS存储文件</td>
			<td>
				<script type="text/javascript">
					yesOrNoSupport(${plugin.supportOssStorage });
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">本地存储文件</td>
			<td>
				<script type="text/javascript">
					yesOrNoSupport(${plugin.supportLocalStorage });
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">SSL日志服务</td>
			<td>
				<script type="text/javascript">
					yesOrNoSupport(${plugin.supportSls });
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">MySQL数据库</td>
			<td>
				<script type="text/javascript">
					yesOrNoSupport(${plugin.supportMysql });
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">Sqlite数据库</td>
			<td>
				<script type="text/javascript">
					yesOrNoSupport(${plugin.supportSqlite });
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">免费版本</td>
			<td>
				<script type="text/javascript">
					yesOrNoSupport(${plugin.supportFreeVersion });
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">授权版本</td>
			<td>
				<script type="text/javascript">
					yesOrNoSupport(${plugin.supportAuthorizeVersion });
				</script>
			</td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">添加时间</td>
			<td><x:time linuxTime="${plugin.addtime }"></x:time></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">最后修改时间</td>
			<td><x:time linuxTime="${plugin.updatetime }"></x:time></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">当前插件版本</td>
			<td><script>versionFormat(${plugin.version })</script></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">支持网市场最低版本</td>
			<td><script>versionFormat(${plugin.wangmarketVersionMin })</script></td>
		</tr>
		<tr>
			<td class="iw_table_td_view_name">插件简介</td>
			<td>${plugin.intro }</td>
		</tr>
	</tbody>
</table>

<script type="text/javascript">
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

</script>
<jsp:include page="../../../iw/common/foot.jsp"></jsp:include>