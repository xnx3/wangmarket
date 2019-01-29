<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.shiro.ShiroFunc"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="IM设置首页"/>
</jsp:include>
<script src="/<%=Global.CACHE_FILE %>Role_role.js"></script>
<script src="<%=Global.get("ATTACHMENT_FILE_URL") %>js/fun.js"></script>
<script src="<%=Global.get("ATTACHMENT_FILE_URL") %>js/admin/commonedit.js?v=<%=G.VERSION %>" type="text/javascript"></script>

<table class="layui-table layui-form" lay-even lay-skin="nob" style="margin:0px; padding:0px;">
	<tbody>
		<tr>
			<td style="min-width:110px; width:110px;" id="useKefu_td">在线客服</td>
			<td style="width:200px;">
				<input type="checkbox" id="switchInputId" name="use" value="1" lay-filter="useKefu" lay-skin="switch" lay-text="开启|关闭" <c:if test="${im.useKefu == 1}">checked</c:if>>
				<span id="switch_span" style="margin-left:-50px; width:1px; height:1px;">&nbsp;</span>
			</td>
		</tr>
		<tr class="kefuSetInfo">
			<td id="nickname_td">客服名字</td>
			<td onclick="updateNickname();" style="cursor: pointer;">
				${user.nickname }
				<i class="layui-icon" style="padding-left:8px; font-size:21px;">&#xe642;</i> 
			</td>
		</tr>
		<tr class="kefuSetInfo">
			<td id="head_td">客服头像</td>
			<td>
				<a id="headAId" href="${head }" title="点击预览原图" target="_black"><img id="kefuHead" src="${head }?x-oss-process=image/resize,h_25" /></a>
				<button type="button" class="layui-btn layui-btn-primary layui-btn-sm" id="uploadKefuHeadButton" style="margin-left:20px;">
					<i class="layui-icon">&#xe67c;</i>上传图片
				</button>
			</td>
		</tr>
		<tr class="kefuSetInfo">
			<td id="autoReply_td">自动回复</td>
			<td onclick="updateAutoReply();" style="cursor: pointer;">
				${im.autoReply }
				<i class="layui-icon" style="padding-left:8px; font-size:21px;">&#xe642;</i> 
			</td>
		</tr>
		<tr class="kefuSetInfo">
			<td id="useEmail_td">邮件通知</td>
			<td style="width:200px;">
				<input type="checkbox" id="switchInputId_email" name="useEmail" value="1" lay-filter="useEmail" lay-skin="switch" lay-text="开启|关闭" <c:if test="${im.useOffLineEmail == 1}">checked</c:if>>
				<span id="switch_span_email" style="margin-left:-50px; width:1px; height:1px;">&nbsp;</span>
			</td>
		</tr>
		<tr class="kefuSetInfo emailSetInfo">
			<td id="email_td">邮箱地址</td>
			<td onclick="updateEmail();" style="cursor: pointer;">
				${im.email }
				<i class="layui-icon" style="padding-left:8px; font-size:21px;">&#xe642;</i> 
			</td>
		</tr>
		<tr class="kefuSetInfo emailSetInfo">
			<td id="email_shengyunul">
				剩余条数
				<script> var email_shengyu_explain='亲，现在内测阶段，发送邮件条数无限制！放心随便用就行了！<br/>若您有使用此项服务，在内测免费阶段结束前半月，我们工作人员会主动联系通知您，确认是否继续使用，绝无其他另收、乱收费！<br/>内测结束后的费用大致是每发送一条邮件收费0.03到0.01元之间，具体多少带内测结束时公布。也就是通常几块钱就够您使用一年的！价格低廉公道，按量收取，用多少付多少！用不了会一直给您存着！'; </script>
			</td>
			<td onclick="layer.msg(email_shengyu_explain);" style="cursor: pointer;">
				无限！
				<i class="layui-icon" style="padding-left:8px; font-size:21px;">&#xe642;</i> 
			</td>
		</tr>
		
	</tbody>
</table>

<div style="color:#a2a2a2; text-align:left; padding-top:10px; padding-bottom: 10px; padding-left:20px;">
		提示：<br/>
		1.鼠标放到左侧文字描述,可显示当前说明<br/>
		2.改动后，非立即生效，会在3分钟内生效<a id="tishi_shengxiao_more" style="padding-left:10px;">更多</a>
</div>

<script type="text/javascript">
//自适应弹出层大小
var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
parent.layer.iframeAuto(index);

layui.use('form', function(){
	var form = layui.form;
	
	form.on('switch(useKefu)', function(data){
		useKefuChange(data.elem.checked);
		updateUseKefu(data.elem.checked? '1':'0');	//将改动同步到服务器，进行保存
	});
	
	form.on('switch(useEmail)', function(data){
		useEmailChange(data.elem.checked);
		updateUseEmail(data.elem.checked? '1':'0');	//将改动同步到服务器，进行保存
	});
	
	
	//美化是否启用的开关控件
	$(".layui-form-switch").css("marginTop","-2px");
	
	//判断是否要显示启用在线客服的提示
	if('${im.useKefu}' != '1'){
		//是否启用的开关，自动弹出提示，引导使用
		layer.tips('要想在网站中使用在线客服功能，请先点此启用', '#switch_span', {
			tips: [3, '#0FA6A8'], //还可配置颜色
			time:3000,
			tipsMore: true,
			area : ['178px' , 'auto']
		});
	}
});

layui.use('upload', function(){
	var upload = layui.upload;
	upload.render({
    	elem: '#uploadKefuHeadButton' //绑定元素
		,url: '/im/headSave.do' //上传接口
		,field: 'head'
		,before: function(obj){
			loading('上传中...');
		}
		,done: function(res){
			loadClose();
			console.log(res);
			msgSuccess('上传成功');
			document.getElementById('kefuHead').src = res.url+'?x-oss-process=image/resize,h_25';
			document.getElementById('headAId').href  = res.url;
		}
		,error: function(){
			//请求异常回调
			loadClose();
			msgFailure('上传出错');
		}
	});
});

//是否使用客服的开关发生改变触发  use  true:开启使用状态
function useKefuChange(use){
	if(use){
		//使用
		$(".kefuSetInfo").css("opacity","1.0");
	}else{
		//不使用
		$(".kefuSetInfo").css("opacity","0.3");
	}
}
useKefuChange('${im.useKefu}' == 1);

//是否使用当前离现实自动发送邮件提醒的开关发生改变触发  use  true:开启使用状态
function useEmailChange(use){
	if(use){
		//使用
		$(".emailSetInfo").css("opacity","1.0");
	}else{
		//不使用
		$(".emailSetInfo").css("opacity","0.3");
	}
}
useEmailChange('${im.useOffLineEmail}' == 1);

//修改客服名字，修改昵称
function updateNickname(){
	layer.prompt({
			title: '请输入客服名字',
			value: '${user.nickname}',
		},
		function(value, index, elem){
			loading('保存中...');
			$.getJSON("/im/propertySave.do?nickname="+value,function(result){
				loadClose();
				if(result.result != '1'){
					alert(result.info);
				}else{
					layer.close(index);
					location.reload();
					parent.msgSuccess();
				}
			});
		});
}

//修改离线时，接受的邮箱地址
function updateEmail(){
	layer.prompt({
			title: '请输入您的邮箱地址',
			value: '${im.email}',
		},
		function(value, index, elem){
			loading('保存中...');
			$.getJSON("/im/emailSave.do?email="+value,function(result){
				loadClose();
						//$.hideLoading();
				if(result.result != '1'){
					alert(result.info);
				}else{
					layer.close(index);
					location.reload();
					parent.msgSuccess();
				}
			});
		});
}

//修改客服不在线时自动回复的内容
function updateAutoReply(){
	layer.prompt({
			title: '请输入当客服不在线时，自动回复给用户的文字',
			formType: 2,
			maxlength:200,
			value: '${im.autoReply}',
		},
		function(value, index, elem){
			loading('保存中...');
			
			$.post("/im/autoReplySave.do", { "text":''+value },
				function(data){
					if(data.result != '1'){
						msgFailure(data.info);
					}else{
						layer.close(index);
						location.reload();
						parent.msgSuccess();
					}
				}, "json");
		
		});
}

//修改当前客服是否使用
function updateUseKefu(value){
	loading('修改中...');
	$.getJSON("/im/useKefu.do?use="+value,function(result){
		loadClose();
				//$.hideLoading();
		if(result.result != '1'){
			alert(result.info);
		}else{
			msgSuccess();
		}
	});
}
//修改当前离线邮箱接受消息是否使用
function updateUseEmail(value){
	loading('修改中...');
	$.getJSON("/im/useOffLineEmail.do?use="+value,function(result){
		loadClose();
		if(result.result != '1'){
			alert(result.info);
		}else{
			msgSuccess();
		}
	});
}


//鼠标跟随提示
$(function(){
	//是否启用在线客服
	var td_useKefu_index = 0;
	$("#useKefu_td").hover(function(){
		td_useKefu_index = layer.tips('您是否想在您的网站中启用在线客服呢？<br/>启用在线客服后，会在网站底部中间位置出现在线客服功能，访客可通过此跟您进行实时沟通；同时您也可以看到当前网站有多少访客在浏览，还可以向访客主动发起会话进行沟通！', '#useKefu_td', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['250px' , 'auto']
		});
	},function(){
		layer.close(td_useKefu_index);
	})
	
	//客服名字
	var nickname_td_index = 0;
	$("#nickname_td").hover(function(){
		nickname_td_index = layer.tips('网站访客所看到的客服的名字', '#nickname_td', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['250px' , 'auto']
		});
	},function(){
		layer.close(nickname_td_index);
	})
	
	//客服头像
	var head_td_index = 0;
	$("#head_td").hover(function(){
		head_td_index = layer.tips('网站访客所看到的客服的头像。请使用正方形的图片', '#head_td', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['250px' , 'auto']
		});
	},function(){
		layer.close(head_td_index);
	})
	
	//当客服不在时，访客问问题时，会自动回复给访客的内容
	var autoReply_td_index = 0;
	$("#autoReply_td").hover(function(){
		autoReply_td_index = layer.tips('当客服不在时，访客问问题时，会自动回复给访客的内容。请输入100字以内', '#autoReply_td', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['250px' , 'auto']
		});
	},function(){
		layer.close(autoReply_td_index);
	})
	
	//是否启用离线时邮件通知
	var useEmail_td_index = 0;
	$("#useEmail_td").hover(function(){
		useEmail_td_index = layer.tips('当您不在线时，网站访客通过在线客服想您提问问题、对话时，是否将访客的对话发送到您指定的邮箱进行实时提醒？<hr/>提示：手机上可使用QQ、微信实时接收邮件，或者可以下载个邮箱客户端，随时都能在第一时间接收到访客的咨询。', '#useEmail_td', {
			tips: [3, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['350px' , 'auto']
		});
	},function(){
		layer.close(useEmail_td_index);
	})
	
	//离线邮件通知的邮箱地址
	var email_td_index = 0;
	$("#email_td").hover(function(){
		email_td_index = layer.tips('请输入您离线时，接收访客信息的邮箱。若没有邮箱，可以填写上您的QQ邮箱', '#email_td', {
			tips: [2, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['250px' , 'auto']
		});
	},function(){
		layer.close(email_td_index);
	})
	
	//离线邮件通知的邮件剩余条数
	var email_shengyunul_index = 0;
	$("#email_shengyunul").hover(function(){
		email_shengyunul_index = layer.tips(email_shengyu_explain, '#email_shengyunul', {
			tips: [3, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['380px' , 'auto']
		});
	},function(){
		layer.close(email_shengyunul_index);
	})
	
	
	//最低不提示的更多
	var tishi_shengxiao_more_index = 0;
	$("#tishi_shengxiao_more").hover(function(){
		tishi_shengxiao_more_index = layer.tips('<b>自动回复的文字</b>、<b>是否启用离线邮件通知</b>、<b>以及离线邮件通知的邮箱地址</b>，这三项改完后，需要等一到两分钟之后才会生效，并不是改完后就立马生效的。所以，如果要测试一下效果，要在改完之后的，三分钟之后再进行测试<br/>目前是内测优惠阶段，邮件通知免费！内测结束后，将转至收费，费用为按量计费，每条邮件', '#tishi_shengxiao_more', {
			tips: [1, '#0FA6A8'], //还可配置颜色
			time:0,
			tipsMore: true,
			area : ['320px' , 'auto']
		});
	},function(){
		layer.close(tishi_shengxiao_more_index);
	})
	
});	
</script>
<jsp:include page="../iw/common/foot.jsp"></jsp:include>  