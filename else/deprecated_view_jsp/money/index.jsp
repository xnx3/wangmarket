<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="兑换商城"/>
</jsp:include>
<body>
<!-- author:管雷鸣 -->
<fieldset class="layui-elem-field" style="margin: 15px;border-radius: 10px;">
  <legend>邀请注册快速赚<%=Global.get("CURRENCY_NAME") %></legend>
  <div class="layui-field-box">
    邀请他人注册，可以获得丰厚的积分奖励！<button class="layui-btn layui-btn-normal layui-btn-mini" onclick="window.parent.openMyInviteList();">查看我的下线</button>并且后期用户付费，也会得到相应的提成！比如，我推荐了B用户注册后，我单纯的推荐注册收益如下：
	<br/>我推荐B注册，<span style="color: #285292;">我获得<%=Global.get("INVITEREG_AWARD_ONE") %><%=Global.get("CURRENCY_NAME") %></span>
	<br/>B又推荐C注册，B用户获得<%=Global.get("INVITEREG_AWARD_ONE") %><%=Global.get("CURRENCY_NAME") %>，<span style="color: #285292;">我获得<%=Global.get("INVITEREG_AWARD_TWO") %><%=Global.get("CURRENCY_NAME") %></span>
	<br/>C又推荐D注册，C用户获得<%=Global.get("INVITEREG_AWARD_ONE") %><%=Global.get("CURRENCY_NAME") %>，B用户获得<%=Global.get("INVITEREG_AWARD_TWO") %><%=Global.get("CURRENCY_NAME") %>，<span style="color: #285292;">我获得<%=Global.get("INVITEREG_AWARD_THREE") %><%=Global.get("CURRENCY_NAME") %></span>
	<br/>D又推荐E注册，D用户获得<%=Global.get("INVITEREG_AWARD_ONE") %><%=Global.get("CURRENCY_NAME") %>，C用户获得<%=Global.get("INVITEREG_AWARD_TWO") %><%=Global.get("CURRENCY_NAME") %>，B用户获得<%=Global.get("INVITEREG_AWARD_THREE") %><%=Global.get("CURRENCY_NAME") %>，<span style="color: #285292;">我获得<%=Global.get("INVITEREG_AWARD_FOUR") %><%=Global.get("CURRENCY_NAME") %></span>
	<div class="weui_cells_title">将下面任意一个地址发送他人，只要通过此地址进入的，注册成功后都是你的下线，都会给你奉献长久的收入</div>
	<c:choose>
	    <c:when test="${user.id > 0}">
	        <div class="weui_cells weui_cells_form">
			  <div class="weui_cell">
			    <div class="weui_cell_bd weui_cell_primary">
			      <textarea class="weui_textarea"rows="2">
			      		<%=Global.get("MASTER_SITE_URL") %>regByPhone.do?inviteid=${user.id }
			      		<%=Global.get("MASTER_SITE_URL") %>currency/index.do?inviteid=${user.id }
			      </textarea>
			    </div>
			  </div>
			</div>
	    </c:when>
	    <c:otherwise>
			<div style="text-align:center; font-size:18px; padding-top:12px;">
				此处注册后才可以查看，
				&nbsp;&nbsp;<a href="../regByPhone.do?inviteid=<%=request.getParameter("inviteid") %>" class="layui-btn layui-btn-normal ">立即注册</a>
			</div>
	    </c:otherwise>
	</c:choose>
	<br/>
  </div>
</fieldset>

<!-- 可兑换列表 -->
<table class="layui-table" lay-even style="margin-top:0px; margin-bottom: -1px;">
  <colgroup>
    <col width="80">
    <col width="100">
    <col>
    <col width="80">
  </colgroup>
  <thead>
    <tr>
      <th>类型</th>
      <th>价格(<%=Global.get("CURRENCY_NAME") %>)</th>
      <th>说明</th>
      <th>有效期</th>
    </tr> 
  </thead>
  <tbody style="cursor:pointer;">
    <tr onclick="apply_domain(1);">
      <td rowspan="2">域名</td>
      <td>400</td>
      <td>.top&nbsp;&nbsp;.pw&nbsp;&nbsp;.bid&nbsp;后缀的顶级域名任选一个</td>
      <td>1年</td>
    </tr>
    <tr onclick="apply_domain(2);">
      <td>6000</td>
      <td>.com&nbsp;&nbsp;.cn&nbsp;后缀的顶级域名任选一个</td>
      <td>1年</td>
    </tr>
    <tr onclick="confirm(3,'100MB空间');">
      <td rowspan="2">空间</td>
      <td>2000</td>
      <td>在您原有的附件存储空间基础上，增加100MB</td>
      <td>永久</td>
    </tr>
    <tr onclick="confirm(4,'1000MB空间');">
      <td>20000</td>
      <td>在您原有的附件存储空间基础上，增加1000MB</td>
      <td>永久</td>
    </tr>
    <tr onclick="confirm(5,'普通代理');">
      <td rowspan="2">代理<br/>资格</td>
      <td>1000</td>
      <td>普通代理。拥有代理后台，可以在代理后台开通任意数量的网站。其建立的网站可以送人，但不可售卖，不可用于商业用途</td>
      <td>1年</td>
    </tr>
    <tr onclick="confirm(6,'商用代理');">
      <td>200000</td>
      <td>商用代理，同普通代理，其建立的网站允许对外出售，允许其用于商业用途</td>
      <td>1年</td>
    </tr>
    <tr onclick="confirm(7,'网站迁移');">
      <td rowspan="1">网站<br/>迁移</td>
      <td>1000</td>
      <td>如果您想将网站独立出去，放到自己的服务器或者FTP上，我们可以吧您网站的源代码(html文件)、图片、附件等打包给你，直接上传就可以开通访问</td>
      <td>1次</td>
    </tr>
    <tr onclick="confirm(8,'手机');">
      <td rowspan="1">手机</td>
      <td>500000</td>
      <td>苹果 Apple iPhone7 4G手机 全网通(32G)</td>
      <td>1台</td>
    </tr>
  </tbody>
</table>
<blockquote class="layui-elem-quote layui-quote-nm" style="margin-bottom: 0px;">	
	您当前有<b>${user.currency }<%=Global.get("CURRENCY_NAME") %></b>，可以在以上商品中，选中您喜欢的商品，进行兑换。兑换提交后我们会有专人在2个工作日内为您审核发放。
</blockquote>
<br/>


<script>
/**
 * 弹出输入框提示，请输入您要兑换的域名
 * @param domain 输入com或top，两个价位的域名
 */
function apply_domain(goodsid){
	layer.prompt({title: '请输入您想兑换的域名，比如您想兑换：nindemingzi.'+(goodsid=='1' ? 'top':'com'), formType: 3}, function(pass, index){
		if(pass.length > 0){
			layer.close(index);
			apply(goodsid,pass);
		}
	});
}

/**
 * 点击后弹出确认框，点击确认后直接进行兑换操作
 * @param goodsid 要兑换的商品id，对应Goods.id
 * @param title 要兑换的商品标题，如域名、100MB空间、1000MB空间
 */
function confirm(goodsid,title){
	layer.confirm('确定要进行兑换'+title+'？', {
	  btn: ['确定兑换','取消'] //按钮
	}, function(){
	  apply(goodsid,'');
	}, function(){
	  
	});
}

/**
 * 提交申请的后台数据传输
 * @param goodsid 商品编号
 * @param userRemark 用户的备注信息，若为空则不会传递
 */
function apply(goodsid,userRemark){
	if('${user.id}' == ''){
		layer.confirm('登陆后才可兑换，是否立即注册？', {
		  btn: ['立即注册','取消'] //按钮
		}, function(){
		  window.location.href='../regByPhone.do?inviteid=<%=request.getParameter("inviteid") %>';
		}, function(){
		  
		});
		return;
	}
	var data = 'goodsid='+goodsid;
	if(userRemark != null  && userRemark.length > 0){
		data = data + '&userRemark='+userRemark;
	}
	var loadVar = layer.msg('兑换中', {
		icon: 16
		,shade: 0.01
		,time:0
	});
    $.post("/currency/apply.do", data, function (result) { 
    	layer.close(loadVar);
    	var obj = JSON.parse(result);
    	if(obj.result == '1'){
    		layer.msg('兑换申请提交成功<br/>工作人员将在2个工作日内为您处理', {shade: 0.3,time:5000});
    	}else if(obj.result == '0'){
    		layer.msg(obj.info, {shade: 0.3})
    	}else{
    		layer.msg(result, {shade: 0.3})
    	}
     }, "text");
}

</script>


</body>
</html>