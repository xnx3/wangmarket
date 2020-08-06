<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="模版列表"/>
</jsp:include>
<script type="text/javascript">
var currentSelect = '${site.templateId}';
</script>
<body style="min-width:10px;">
<div>&nbsp;</div>
<div id="info" style="display:none; padding: 35px 0;">
	<h1 style="text-align: center; font-size: 34px;color: #3cc51f;font-weight: 400;margin: 0 15%;">设置网站信息</h1>
</div>

<style>
.templateList{
}
.templateList div{
	float:left;
	width:48%;
	padding:1%;
}
.templateList div img{
	width:96%;
	padding:2%;
}
.templateList div div{
	width:96%;
	padding:2%;
	text-align: center;
}
</style>
<div class="templateList" id="pc" style="display:none;">
	<div onclick="selectTemp('3');">
		<img src="<%=G.RES_CDN_DOMAIN %>template/3/preview.jpg" />
		<div>
			<i class="weui_icon_success" style="padding-right:10%; display:none;" id="tempSelect3"></i>
			编号3：黑色大气
		</div>
	</div>
	
	<div onclick="selectTemp('5');">
		<img src="<%=G.RES_CDN_DOMAIN %>template/5/preview.jpg" />
		<div>
			<i class="weui_icon_success" style="padding-right:10%; display:none;" id="tempSelect5"></i>
			编号5：蓝白简约
		</div>
	</div>
	
	<div onclick="selectTemp('6');">
		<img src="<%=G.RES_CDN_DOMAIN %>template/6/preview.jpg" />
		<div>
			<i class="weui_icon_success" style="padding-right:10%; display:none;" id="tempSelect6"></i>
			编号6：适合懒人、服务业，手机电脑通用
		</div>
	</div>
	<div onclick="selectTemp('7');">
		<img src="<%=G.RES_CDN_DOMAIN %>template/7/preview.jpg" />
		<div>
			<i class="weui_icon_success" style="padding-right:10%; display:none;" id="tempSelect7"></i>
			编号7：滚屏风格，手机电脑通用（<b>已不推荐使用！</b>）
		</div>
	</div>

</div>


<div class="templateList" id="wap" style="display:none;">
	<div onclick="selectTemp('1');">
		<img src="<%=G.RES_CDN_DOMAIN %>template/1/preview.jpg" />
		<div>
			<i class="weui_icon_success" style="padding-right:10%; display:none;" id="tempSelect1"></i>
			编号1：黑色大气
		</div>
	</div>
	
	<div onclick="selectTemp('2');">
		<img src="<%=G.RES_CDN_DOMAIN %>template/2/preview.jpg" />
		<div>
			<i class="weui_icon_success" style="padding-right:10%; display:none;" id="tempSelect2"></i>
			编号2：蓝色风格
		</div>
	</div>
	
	<div onclick="selectTemp('4');">
		<img src="<%=G.RES_CDN_DOMAIN %>template/4/preview.jpg" />
		<div>
			<i class="weui_icon_success" style="padding-right:10%; display:none;" id="tempSelect4"></i>
			编号4：背景全图
		</div>
	</div>
</div>

<button onclick="updateTemplateSubmit();" class="weui_btn weui_btn_primary" style="margin:2%; width: 96%;">用此模版</button>

<script type="text/javascript">
//提取当前的模版，赋值
document.getElementById('tempSelect${site.templateId}').style.display = 'inline';

//若当前client状态是pc模式
var clientPc = '${client}' == '1';

//设置当前显示pc、还是wap模版
if(clientPc){
	document.getElementById('pc').style.display = 'inline';
}else{
	document.getElementById('wap').style.display = 'inline';
}

//更改当前模版，修改全局化模版参数
function selectTemp(tempId){
	currentSelect = tempId;
	//隐藏所有组件的选中效果
	document.getElementById('tempSelect1').style.display = 'none';
	document.getElementById('tempSelect2').style.display = 'none';
	document.getElementById('tempSelect4').style.display = 'none';
	
	document.getElementById('tempSelect3').style.display = 'none';
	document.getElementById('tempSelect5').style.display = 'none';
	document.getElementById('tempSelect6').style.display = 'none';
	document.getElementById('tempSelect7').style.display = 'none';
	
	//为指定的选中的设置显示选中状态
	document.getElementById('tempSelect'+tempId).style.display = 'inline';
	
	//询问框
	var layer_confirm_tip = layer.confirm('确定要使用此套模版？', {
		btn: ['立即使用','再看看']
	}, function(){
		layer.close(layer_confirm_tip);
		parent.msg.loading("修改中");
		$.post("/sites/templateSave.do?templateId="+tempId, function(data){
			parent.msg.close();
			if(data.result == '1'){
				parent.msg.success("操作成功");
				//设置cookie，首页强制刷新开启。再打开首页时，会判断这个Cookie，若是1，则变为0，并且执行js强制刷新本页面
				parent.openIndexRefreshCache();
		 	}else if(data.result == '0'){
		 		parent.msg.failure(data.info);
		 	}else{
		 		parent.msg.failure();
		 	}
		});
		
	}, function(){
		//再看看，不做任何处理
	});
}

</script>

</body>
</html>