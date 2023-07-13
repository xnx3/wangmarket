<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="欢迎使用网市场云建站系统"/>
</jsp:include>

	<style>
		html,body, .content{
		
		}
		.content{
			width: 600px;
			min-height:700px;
			margin: 0 auto;
			box-shadow: rgba(0, 0, 0, 0.06) 0px 1px 10px 2px;
			padding: 30px;
			margin-top: 50px;
		}
		.title{
			border-bottom: 1px solid #eee;
			padding-top: 20px;
			padding-left: 10px;
			padding-bottom: 20px;
			font-size: 28px;
			margin-bottom: 20px;
		}
		.content ul{
			padding-left: 20px;
		}
		.content ul li{
			list-style-type: decimal;
			padding-left:10px;
			padding-bottom:4px;
		}
		.content ul li img{
			max-width:250px;
			padding:4px;
			padding-left:40px;
		}
		.info{
			font-size:14px;
			line-height: 22px;
		}
		.info h2,h3,h4,h5{
			border-bottom: 1px solid #eee;
			padding-top: 23px;
			margin-bottom: 10px;
			padding-bottom: 5px;
		}
		
		@media only screen and (max-width: 700px) {
			.content{
				width:auto;
				margin-top: 0px;
				box-shadow: rgba(0, 0, 0, 0.06) 0px 0px 0px 0px;
			}
			
		}
		
		a{
			color: blue;
		}
	</style>

	<div class="content">
		<div class="title">
			欢迎使用 网市场云建站系统 v<%=G.VERSION %>
		</div>
		
		
		<div style="padding-top:100px;; padding-bottom:110px; text-align:center;">
			<a href="/install/selectAttachment.do" class="layui-btn layui-btn-primary" style="line-height: 0px;padding: 30px;font-size: 20px;">点击此处开始安装本系统</a>
			<br/>
			<div style="font-size:12px; padding-top:8px; ">
				<span style="color: gray;">交流QQ群22群：740332119</span> &nbsp;&nbsp; <span style="color: gray;">作者微信：<span class="ignore">xnx3com</span></span>
				<!-- <a href="javascript:updateSystemConfig();" style="color: gray;">安装好后再次修改配置方式</a> -->
			</div>
			
		</div>
		
		
		<div class="info" onclick="huodong();" style="display:none; cursor: pointer; padding-top:20px;">
			如果你是用做网站来拉客户或者盈利，做网站的数量比较多，比如50个以上，考虑到系统的可维护性、升级方便、以及网站数量持续增长之后的可扩展性、稳定性，我们建议你参与我们的活动，我们帮你安装、调试好、有入门文档教你怎么更好的使用，让这套建站系统真正给你创造利益。[点此查看更多]
		</div>
		
		
		<div class="info" style="    position: initial; bottom: 25px;padding: 20px; color: gray; padding-top: 170px;">
			私有化部署自己的 SAAS 云建站系统，可通过后台任意开通多个网站，每个网站使用自己的账号进行独立管理。延续了织梦、帝国 CMS 的模版方式，性能高度优化，一台 1 核 1G 服务器可建立几万个独立网站。
			详细说明参见：<a href="http://cms.zvo.cn" target="_black" style="color: gray;" class="ignore">http://cms.zvo.cn</a>
		</div>
		
	 	<div style="width: 100%;text-align: center; padding-top: 30px;">
			<a href="http://cms.zvo.cn" target="_black" style="color: gray;font-size: 8px;">潍坊雷鸣云网络科技有限公司</a>
		</div>
	</div>
	
	

	
	<div style="display:none;" id="huodong">
		<div style="display: inline; line-height: 1.5rem;">
			<div>为扩大网市场云建站系统市场占有率，决定在人力、资金方面加大投入：</div>
			<ul>
				<li>1. 华为云通过我们链接 <a style=" background-color: coral;" href="http://huawei.leimingyun.com" target="_black">huawei.leimingyun.com</a> 开通的华为云账户，进行完实名认证，将你华为云账号密码发我们。</li>
				<li>2. 我们技术人员登录此账户，帮您结合活动购买服务器（1核2G服务器一年33元，具体价格看当月活动。购买时技术会询问你是否确认购买）免费帮你安装好、调试好，你可以不用管服务器所有操作，只管用！</li>
				<li>2. 注意，此活动需要购买一年服务器，是因为我们想把有限的精力放在真正想用的人身上。买了服务器，那证明你确实想用它。</li>
				<li>4. 会教授在合法合规的前提下，如何使用未备案的域名解析到国内服务器</li>
				<li>5. 技术指导你如何开通网站、选择模板、修改、填充网站信息、网站上线，让你真正用它来发展业务赚钱。</li>
			</ul>
			<div style="margin-top: 3rem;"><b>重点：我们支持白嫖，但我们不想被无意义的白嫖！开源不易，还要赚钱糊口，精力实在有限，我们只能将时间放在真正想用它的朋友那里。如果你能套买服务器的百八十块钱，不管后面能不能用不用起来，起码是有想法，舍得去投入那么一点点去做的，我们也愿意帮助您。</b></div>	
		</div>
	</div>
	
	<script>
	//修改系统相关配置，也就是安装的系统参数
	function updateSystemConfig(){
		layer.open({
			title: '修改方式'
			,content: '当您安装成功后，可使用账号 admin  密码 admin 进行登录，登录成功后，在 系统设置 - 系统变量 中，可进行修改相关配置参数。<br/>本次安装，便是针对系统变量的一些重要参数进行简单的引导设定。<br/>注意：看不懂的建议别随便改'
		});
	}
	
	function huodong(){
		msg.popups({
			text:document.getElementById('huodong').innerHTML,
			width:'50rem'
		});
	}
	
	</script>
	
<jsp:include page="/wm/common/foot.jsp"></jsp:include> 
<style> /* 显示多语种切换 */ .translateSelectLanguage{ display:block; } </style>