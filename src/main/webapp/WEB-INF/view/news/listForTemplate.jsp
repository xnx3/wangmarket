<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="文章列表"/>
</jsp:include>
<link href="/css/site_two_subMenu.css" rel="stylesheet">
<script src="/<%=Global.CACHE_FILE %>News_newsTop.js"></script>

<div style="width:100%;height:100%; background-color: #fff; overflow-x: hidden;">
		
	<div class="layui-nav layui-nav-tree layui-nav-side menu">
		<div style="
		    text-align: left;
		    line-height: 1.2rem;
		    font-size: 16px;
		    padding-top: 30px;
		    padding-bottom: 20px;
		    font-weight: 700;
		    color: black;
		    padding-left: 18px;">内容管理</div>
		<ul class="ignore" id="columnTreeNav">
			${columnTreeNav }
		</ul>
	</div>
	
	<div id="pleaseSelectColumn" style="width: 100%;height:100%;position: fixed;left: 170px;word-wrap: break-word;border-right: 170px;box-sizing: border-box; padding-right: 10px; overflow-y: auto;overflow-x: hidden; border-right: 170px solid transparent;">
		<!-- 点击内容管理进来后，如果没选择栏目，那么显示这个请选择要修改的栏目的提示标识 -->
		<div style="font-size: 2rem; text-align: center; padding-top: 28%;">
			请在左侧选择您要进行操作的栏目 <span onclick="yindaoSelectColumn();" style="cursor: pointer; font-size: 0.5rem; color: blue; text-decoration: underline;">没看到?</span>
		</div>
	</div>
	<div id="newsList" style="width: 100%;height:100%;position: fixed;left: 170px;word-wrap: break-word;border-right: 170px;box-sizing: border-box; padding-right: 10px; overflow-y: auto;overflow-x: hidden; border-right: 170px solid transparent; display:none;">
		
		<jsp:include page="../iw/common/list/formSearch_formStart.jsp" ></jsp:include>
			<jsp:include page="../iw/common/list/formSearch_input.jsp">
				<jsp:param name="iw_label" value="标题"/>
				<jsp:param name="iw_name" value="title"/>
			</jsp:include>
			
			<input class="layui-btn iw_list_search_submit" type="submit" value="搜索" />
			
			<c:choose>
				<c:when test="${siteColumn.type == 1 || siteColumn.type == 2 || siteColumn.type == 7}">
					<a href="/news/news.do?cid=${siteColumn['id'] }" class="layui-btn layui-btn-normal" style="float: right; margin-right:10px;">添加信息</a>
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
					<th>置顶</th>
					<th>操作</th>
				</tr> 
			</thead>
			<tbody>
				<c:forEach items="${list}" var="news">
					<tr>
						<td style="width:55px;">${news['id'] }</td>
						<td><a href="/news/redirectByNews.do?newsId=${news['id'] }&cid=${news['cid'] }&type=${news['type'] }" target="_black"><x:xss text="${news['title'] }"></x:xss> </a></td>
						<td style="width:60px;">
							<c:if test="${not empty news.titlepic }">
								<c:choose>
									<c:when test="${fn:contains(news.titlepic,'//')}">
										<img src="${news.titlepic }?x-oss-process=image/resize,h_25" height="25" />
									</c:when>
									<c:otherwise>
										<a href="${AttachmentFileUrl }site/${news.siteid }/news/${news.titlepic }" target="_black"><img src="${AttachmentFileUrl }site/${news.siteid }/news/${news.titlepic }?x-oss-process=image/resize,h_25" height="25" /></a>
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>
						<td style="width:140px; cursor: pointer;" id="addtime_${news['id'] }" onclick="updateAddtime('${news['id'] }', '<x:time linuxTime="${news['addtime'] }" format="yyyy-MM-dd hh:mm:ss"></x:time>');">
							<x:time linuxTime="${news['addtime'] }" format="yy-MM-dd HH:mm"></x:time>
							<input style="width:0px; height:0px; overflow: hidden; float: left;" type="text" id="addtime_${news['id'] }_input" value="<x:time linuxTime="${news['addtime'] }" format="yyyy-MM-dd hh:mm:ss"></x:time>" />	
						</td>
						
						<td style="width:40px; cursor: pointer;" onclick="updateTop('${news['id'] }', '<x:xss text="${news['title'] }"></x:xss>', '${news['top'] }');">
							<script type="text/javascript">document.write(newsTop['${news['top'] }']);</script>
						</td>
						<td style="text-align: center; width:140px;">
							<c:choose>
								<c:when test="${siteColumn.type == 3 && siteColumn.editMode == 1}">
									<botton class="layui-btn layui-btn-sm" onclick="editText('${siteColumn.templatePageViewName}');" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></botton>
								</c:when>
								<c:otherwise>
									<a class="layui-btn layui-btn-sm" href="/news/news.do?id=${news['id'] }" style="margin-left: 3px;"><i class="layui-icon">&#xe642;</i></a>
								</c:otherwise>
							</c:choose>
							
							<c:choose>
								<c:when test="${siteColumn.type == 3 || siteColumn.type == 5 || siteColumn.type == 8}">
									<!-- 独立页面、超链接是不显示删除按钮、转移栏目的 -->
								</c:when>
								<c:otherwise>
									<!-- 新闻列表、图文列表, 又或者这里是列出所有信息，siteColumn 本身为空 -->
									<botton class="layui-btn layui-btn-sm" onclick="changeColumn('${news['id'] }', '${news['cid'] }');" style="margin-left: 3px;"><i class="layui-icon">&#xe609;</i></botton>
									<botton class="layui-btn layui-btn-sm" onclick="deleteNews('${news['id'] }');" style="margin-left: 3px;"><i class="layui-icon">&#xe640;</i></botton>
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
			<div>点击文章的 发布时间 位置，可以修改文章的发布时间</div>
			<div>点击文章的 置顶 位置，可以修改文章是否置顶。置顶后的文章将会在后台以及网站中进行置顶显示。（动态栏目代码调用时也会按照此规则来）</div>
		</div>
	</div>
</div>

<script>
if('${siteColumn.id}'.length > 0){
	//默认是直接点击内容管理过来的，列表区域显示请选择栏目的提示。如果点击某个栏目了，那就显示具体栏目的列表信息
	document.getElementById('newsList').style.display='';
	document.getElementById('pleaseSelectColumn').style.display='none';
}

layui.use('element', function(){
	var element = layui.element;
});
var laydate;
layui.use('laydate', function(){
	laydate = layui.laydate;
});

/**
 *	更改 addtime 发布时间
 *	id 绑定元素的id，在哪个元素上显示
 */
function updateAddtime(id, value){
	laydate.render({
		elem: '#addtime_'+id+'_input' //指定元素
		,type: 'datetime'
		,format: 'yyyy-MM-dd HH:mm:ss' //可任意组合
		,value: value //必须遵循format参数设定的格式
		,show: true //直接显示
		,closeStop: '#addtime_'+id //这里代表的意思是：点击 test1 所在元素阻止关闭事件冒泡。如果不设定，则无法弹出控件
		,done: function(value, date, endDate){
			//判断是否有过修改
			
			if(typeof(value) == 'undefined' || value == null){
				return;
			}
			
			//旧的，原始值
			var oldValue = document.getElementById('addtime_'+id+'_input').value;
			if(typeof(oldValue) == 'undefined' || oldValue == null){
				oldValue = '';
			}
			
			if(oldValue != value){
				//确实修改了，那么保存
				
				msg.loading("修改中");
				$.post("updateAddtime.do?id="+id+"&addtime="+value, function(data){
					msg.close();
					if(data.result == '1'){
						parent.msg.success("修改成功");
						location.reload();
				 	}else if(data.result == '0'){
				 		parent.msg.failure(data.info);
				 	}else{
				 		parent.msg.failure('修改失败');
				 	}
				});
				
			}
		}
	});
}

/**
 * 删除新闻，传入要删除的新闻id
 */
function deleteNews(newsid){
	var dtv_confirm = layer.confirm('删除后不可恢复，您确定要删除此条信息吗？', {
		btn: ['删除','取消'] //按钮
	}, function(){
		layer.close(dtv_confirm);
		parent.msg.loading("删除中");
		$.post("/news/deleteNewsForAjax.do?id="+newsid, function(data){
			parent.msg.close();
			if(data.result == '1'){
				parent.msg.success("删除成功");
				location.reload();
		 	}else if(data.result == '0'){
		 		parent.msg.failure(data.info);
		 	}else{
		 		parent.msg.failure('操作失败');
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
	parent.openTemplatePageList(name);
}

//更改文章是否置顶
function updateTop(id, title, top){
	msg.confirm('确定要将文章《'+title+'》进行 '+(top==0? '置顶':'取消置顶')+' 操作吗？',function(){
		parent.msg.loading("修改中");
		wm.post('/news/updateTop.json', {id:id, top:top==1? 0:1}, function(data){
			parent.msg.close();
			if(data.result == '1'){
				parent.msg.success("操作成功");
				location.reload();
		 	}else if(data.result == '0'){
		 		parent.msg.failure(data.info);
		 	}else{
		 		parent.msg.failure('操作失败');
		 	}
	    });
	});
}

/**
 * 转移栏目
 * newsid 要转移的文章的id
 * columnid 要转移的文章所在的栏目id
 */
function changeColumn(newsid, columnid){
	var url = '/news/newsChangeColumnForSelectColumn.do?newsid='+newsid+'&columnid='+columnid;
	layer.open({
		type: 2, 
		title:'转移到其他栏目', 
		area: ['250px', '500px'],
		shadeClose: true, //开启遮罩关闭
		content: url
	});
}
</script>

${autoJumpTemplateEdit}


<!-- 选择要编辑的栏目的引导 -->
<script src="${STATIC_RESOURCE_PATH}module/driver.js/driver.min.js"></script>
<link rel="stylesheet" href="${STATIC_RESOURCE_PATH}module/driver.js/driver.min.css">
<script>
/**
 * 运行引导。
 */
function yindaoSelectColumn(){
	const columnDriver = new Driver({
		//doneBtnText: '结束指引', // 最终按钮上的文本 Text on the final button
		closeBtnText: '关闭', // 当前步骤关闭按钮上的文本 Text on the close button for this step
		//nextBtnText: '下一步', //当前步骤下一步按钮上的文本 Next button text for this step
		//prevBtnText: '上一步', // 当前步骤上一步按钮上的文本 Previous button text for this step
		onReset: function(Element) {
			// 遮罩将要关闭时调用
		},
	});
	//Define the steps for introduction
	columnDriver.defineSteps([
		{
			element: '#columnTreeNav',
			popover: {
				title: '在这里选择要编辑的栏目',
				description: '您想编辑哪个栏目，就可以点击这里的哪个栏目，便可编辑其内容了。',
				position: 'right'
			}
		}
	]);
	
	//Start the introduction
	setTimeout(function(){
		columnDriver.start();
	}, 100);
}
</script>

<jsp:include page="/wm/common/foot.jsp"></jsp:include> 
