<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/wm/common/head.jsp">
	<jsp:param name="title" value="选择使用方式"/>
</jsp:include>

  	<style>
  		
  		.content{
  			width: 600px;
  			min-height:80%;
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
		    text-align:center;
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
  		}
  		.info h2,h3,h4,h5{
 			border-bottom: 1px solid #eee;
		    padding-top: 15px;
		    margin-bottom: 5px;
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
    	<div class="select">
			<!-- 这里的 onmouseenter 是为了避免下面的收费鼠标移开后，不变回来，所以这里又加了一个，如果下面的收费的霉变过来，鼠标移动过来后，收费的恢复回来 -->
			<!-- setAttachmentMode.do?mode=${AttachmentFile_MODE_LOCAL_FILE} -->
			<div class="selectItem" onclick="mianfeiban();" onmouseenter="qiehuanshouquanyuanshi();">
				<div class="title">免费开源版</div>
				<div class="intro">
					<br/>服务器存储
					<ul class="lilist"> 
						<li>安装方便，一键运行</b></li>
						<li>可用于商业用途</li>
						<li>可开通数千网站</li>
						<li>网站基本功能全包含</li>
					</ul>
					使用运行程序的服务器本身作为存储对象，会在程序根目录下创建site文件夹存储文件，传统文件存储方式。将生成的html页面、上传的图片，都存储在代码所在的服务器上。
					<br/>优点：简单，免费！可快速使用，无需再掌握其他知识。
					<br/>缺点：网站个数建议在几千个以内。使用中有不懂的地方可看帮助说明 <a href="http://help.wscso.com" target="_black">help.wscso.com</a> 或者加入QQ群472328584讨论交流。
				</div>
			</div>
			
			<br/>
			
			<div class="selectItem" id="yun" onmouseenter="qiehuanshouquan();" onmouseleave="qiehuanshouquanyuanshi();" onclick="window.open('http://www.wang.market/price.html')">
				<div class="title">免费开源版</div>
				<!-- setAttachmentMode.do?mode=${AttachmentFile_MODE_ALIYUN_OSS} -->
			</div>
			<div style="display:none;" id="weishouquanyun">
				<div class="title">收费企业版</div>
				<div class="intro">
					云端存储，无限扩展
						<ul class="lilist"> 
							<li>我们帮你安装调试好，你只管用</li>
							<li>我方可帮你选择华为云、阿里云服务器规格及购买</li>
							<li>创立专门的微信讨论组进行技术指导，随时咨询，帮你快速掌握使用技巧。</li>
							<li>正版授权，去管理后台左下角我方版权标识</li>
							<li>未来可支持分布式部署，无网站个数限制</li>
							<li>使用CDN加速，网站极速访问</li>
							<li>有文件管理、网站转移等锦上添花的功能</li>
						</ul>
				</div>
			</div>
			<div style="display:none;" id="shouquanxinxi">
				<div class="title">收费企业版</div>
				<div style="font-size: 18px;color: red;">
					免费赠送3个月的企业版授权。帮你安装好、调试好，你可以不用管服务器所有操作，只管用！三个月之后可以选择续费延期(500/年,或者5000元永久) ，也可以不再购买授权，依旧可以正常免费使用。只不过我方不再提供人工方面的咨询及帮助。详情点击查看
				</div>
			</div>
			
		</div>
    	
    </div>
  </body>
  
  
<style>
.select{
	padding:20px;
}
.select>div{
	padding: 30px;
    border: 1px;
    border-style: solid;
    border-color: #d2d2d236;
}
.select div .title{
	font-size: 20px;
}
.select div .intro{
	font-size: 14px;
}


.lilist{
	padding-left: 40px;
}
.lilist>li{
	list-style: decimal;
}

.selectItem{
	cursor: pointer;
}
.selectItem:HOVER{
	border-color: #000000;
	background-color: #FAFAFA;
}
</style>

<script>
function qiehuanshouquan(){
	document.getElementById('yun').innerHTML = document.getElementById('shouquanxinxi').innerHTML; 
}
//用户鼠标移开，显示云版本的功能
function qiehuanshouquanyuanshi(){
	document.getElementById('yun').innerHTML = document.getElementById('weishouquanyun').innerHTML;
}
qiehuanshouquanyuanshi();

function mianfeiban(){
	if(window.location.hostname.indexOf('localhost') > -1 || window.location.hostname.indexOf('127.0.0.1') > -1){
		
		layer.open({
			type: 1
			,title: false
			,closeBtn: false
			,area: '590px;'
			,shade: 0.8
			,id: 'versionUpdateTipsss'
			,resize: false
			,btn: ['本地快速安装','完整安装']
			,btnAlign: 'c'
			,moveType: 1
			,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300; text-align:left;">'
				+'<div style="width:100%; text-align:center; font-size:22px;"> <span style="font-size:28px; padding-right:15px;"></span>友情提示</div><br>'
				+'<style> .xnx3_gxc li{list-style-type: decimal;} </style>'
				+'<ul style="list-style-type: decimal;" class="xnx3_gxc">'
				+'<div>监测到您访问域名是本地的，如果是本地体验功能、或者做模版，您可以点下面的 本地快速安装 。 </div>'
				+'</ul></div>'
				
			,yes: function(index, layero){
				window.location.href="setLocalDomain.do";
			}
			,btn2: function(index, layero){
				window.location.href='domainSet.do';
			}
		});
		
		
	}else{
		window.location.href='domainSet.do';
	}
}
</script>
  
</html>
