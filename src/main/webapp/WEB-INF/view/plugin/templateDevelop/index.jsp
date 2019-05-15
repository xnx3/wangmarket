<%@page import="com.xnx3.wangmarket.admin.G"%>
<%@page import="com.xnx3.j2ee.entity.User"%>
<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="../../iw/common/head.jsp">
	<jsp:param name="title" value="管理后台"/>
</jsp:include>
<script src="${STATIC_RESOURCE_PATH}js/fun.js"></script>
<script>
var masterSiteUrl = '<%=Global.get("MASTER_SITE_URL") %>'; 
</script>
<script src="/js/admin/commonedit.js?v=<%=G.VERSION %>"></script>


<style>
.explain{
	font-size:14px;
	color:gray;
	padding-left: 50px;
	padding-top:15px;
}
#dev_cont .explain ul li{
	list-style: decimal;
}
#dev_cont .buzhoutitle{
	padding-top:20px;
	font-size: 18px;
    font-weight: 500;
}
</style>
<div style="margin:0 auto;">
	
	<div id="dev_cont" style="padding: 20px;">
		<div>
			<div class="buzhoutitle">前言</div>
			<div class="explain">
				注意：
				<ul>
					<li>此插件仅限于模版开发人员使用</li>
					<li>如果你是Windows系统，请不要将项目此放到C盘，否则可能会由于权限等问题导致无法创建相关文件夹。</li>
					<li>不要将本项目放到有空格、中文字符的文件夹（路径）中</li>
					<li>如有疑问或是在解决不了的，可到 <a href="http://bbs.leimingyun.com" target="_black">社区论坛 bbs.leimingyun.com</a> 发帖求助，我们有专人负责答疑</li>
				</ul>
			</div>
		</div>
		
		
		<div>
			<div class="buzhoutitle">第一步：给要新建立的模版起一个编码</div>
			<div class="layui-form-item" style=" padding-left: 40px;padding-top: 15px; margin-bottom: 0px;">
			    <div class="layui-input-inline">
			      <input type="text" name="templateName" id="templateName" placeholder="请输入模版编码" value="${site.templateName}"  class="layui-input">
			    </div>
			    <button class="layui-btn layui-btn-primary" onclick="saveTemplateName();">保存</button>
			</div>
			<div class="explain">
				注意：
				<ul>
					<li>模版编码，也就是模版的唯一标示，不可跟网市场中的其他模版的编码重名。请使用你公司名字的首字母为前缀进行命名。如公司名字为“潍坊雷鸣云网络科技有限公司”，则模版编码前缀为“wflmy”，如模版编码叫"wflmy001"。如果你是个人，比如名字叫“管雷鸣”，那么模版编码的前缀为名字的首字母“glm”</li>
					<li>只限使用英文、数字</li>
					<li>长度在20个字符以内</li>
					<li>设置后不可更改！如果想改，你还是重新开通个网站，重新开始吧</li>
					<li>请不要将项目此放到C盘，否则可能会由于权限等问题导致无法创建相关文件夹。</li>
				</ul>
			</div>
		</div>
		
		<div>
			<div class="buzhoutitle">第二步：打开存放模版资源的文件夹</div>
			<div class="" style=" padding-left: 40px;padding-top: 15px;">
			    <div class="layui-input-inline" >
			      <div id="step2_path" style="display:none;">文件夹所在路径：${projectPath}${exportPath}${site.templateName}/</div>
			      <div id="step2_weihuoqu">文件夹所在路径：未获取，请先操作完第一步！</div>
			    </div>
			</div>
			<div class="explain">
				注意：
				<ul>
					<li>只有第一步中设置了模版编码后，这里才有这个资源文件路径。</li>
					<li>您需要将您设计的模版中，所使用的js、css、以及图片都放到这个文件夹中</li>
					<li>您设计的模版中，不允许使用任何来自外站的资源，只可使用这个文件夹中的资源</li>
					<li>比如，这个文件夹中有个 css/style.css 文件，那么在模版中，可以通过 {templatePath}css/style.css 这个路径引入</li>
					<li>css、js、images 等各种资源分好文件夹存放。禁止杂乱丢进去</li>
					<li>资源文件所允许的后缀名为：js、xml、swf、css、png、jpg、bmp、jpeg、gif、eot、svg、ttf、woff、woff2、otf ，不在此内的后缀名将会被系统自动删除掉！</li>
				</ul>
			</div>
		</div>
		
		
		<div>
			<div class="buzhoutitle">第三步：开发模版</div>
			<div class="" style=" padding-left: 40px;padding-top: 15px;">
			    <div class="layui-input-inline" >
			    	<a href="javascript:parent.templateDevHelp();" class="layui-btn layui-btn-primary">点此查看模版开发入门示例</a>
			    </div>
			</div>
			<div class="explain">
				注意：
				<ul>
					<li>只有第一步中设置了模版编码后，这里才有这个资源文件路径。</li>
					<li>您可以将您设计的模版中，所使用的js、css、以及图片都放到这个文件夹中</li>
					<li>所有引入的绝对路径资源，必须放入第二步中所创建出来的文件夹中。在模版中引入时，必须以 {templatePath} 引入。如引入一个图片head.jpg，该图片在 第二步所创建的文件夹的images文件夹中，则这样写 &lt;img src="{templatePath}images/head.jpg" /&gt;</li>
					<li>html（模版）中请勿存在js、css代码。有关css、js方面的代码，请新建相应的js、css资源文件进行存放。（如果你不想共享出模版，只是单纯自己用，可以忽略这条）</li>
				</ul>
			</div>
		</div>
		
		
		<div>
			<div class="buzhoutitle">第四步：设置模版首页预览图</div>
			<div class="" style=" padding-left: 40px;padding-top: 15px;">
			    <div class="layui-input-inline" >
			    	<button type="button" class="layui-btn layui-btn-primary" id="preview_index_button">
						点此上传模版首页图片
					</button>
			    </div>
			    <span  id="stop_4_tishi" style="padding-left: 50px;">
			    	<a href="/websiteTemplate/${site.templateName}/preview.jpg"  target="_black">已设置：<img id="stop4_img" src="/websiteTemplate/${site.templateName}/preview.jpg" class="layui-btn layui-btn-primary" style="border:0px;" onerror="document.getElementById('stop_4_tishi').innerHTML = '尚未上传！请上传首页预览图片';" /></a>
			    </span>
			     
			    
			</div>
			<div class="explain">
				注意：
				<ul>
					<li>需要模版首页的整张图片，也就是全图。你可以使用ps自行将首页整张图做出来、也可以使用一些浏览器三方插件自动将首页截图保存出来。<a href="/plugin/templateDevelop/images/template_preview_demo.jpg" target="_black">点此查看图片示例</a></li>
					<li>图片必须是jpg格式</li>
					<li>图片大小限制在1MB以内。建议大小在 200KB ～ 500KB 之间</li>
					<li>如果你要将此模版共享出来，这一步是必须的。如果这个模版只是你自己用，你可以不上传图片</li>
				</ul>
			</div>
		</div>
		
		
		<div>
			<div class="buzhoutitle">第五步：设置模版所属分类</div>
			<div class="" style=" padding-left: 40px;padding-top: 15px;">
			    <div class="layui-input-inline layui-form" id="stop_5_checkbox_item">
					加载中...
			    </div>
			</div>
		</div>
		
		
		<div>
			<div class="buzhoutitle">第六步：设置模版支持的终端设备</div>
			<div class="layui-form" style=" padding-left: 40px;padding-top: 15px;">
			      <div class="layui-form-item">
				    <label class="layui-form-label">手机端</label>
				    <div class="layui-input-block" id="stop6switch_mobile">
				      <input type="checkbox" name="switch" lay-skin="switch" lay-filter="client_pc" lay-text="支持|不支持">
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">电脑端</label>
				    <div class="layui-input-block" id="stop6switch_pc">
				      <input type="checkbox" name="switch" lay-skin="switch" lay-text="支持|不支持">
				    </div>
				  </div>
				  <div class="layui-form-item">
				    <label class="layui-form-label">ipad</label>
				    <div class="layui-input-block" id="stop6switch_ipad">
				      <input type="checkbox" name="switch" lay-skin="switch" lay-text="支持|不支持">
				    </div>
				  </div>
			    <div class="layui-form-item">
				    <label class="layui-form-label">广告展示机</label>
				    <div class="layui-input-block" id="stop6switch_display">
				      <input type="checkbox" name="switch" lay-skin="switch" lay-text="支持|不支持">
				    </div>
				  </div>
			</div>
			<div class="explain">
				注意：
				<ul>
					<li>选择你的模版是电脑pc网站的、还是手机网站的，哪种终端用的</li>
					<li>如果你想让模版支持多种终端，请使用响应式，用css对模版做自适应！不支持手机端单独做一个模版、电脑端再单独做一个模版。</li>
				</ul>
			</div>
		</div>
		
		
		
		<div>
			<div class="buzhoutitle">第七步：设置您的信息</div>
			<div class="" style=" padding-left: 40px;padding-top: 15px;">
			    <div class="layui-form-item">
				    <label class="layui-form-label">公司名字</label>
				    <div class="layui-input-inline" style="width: 300px;">
				      <input type="text" name="companyname" id="companyname" placeholder="请输入您公司名全称" autocomplete="off" class="layui-input" onchange="stop7InputOnchange('companyname', document.getElementById('companyname').value);">
				    </div>
				    <div class="layui-form-mid layui-word-aux">如果没有可留空</div>
				</div>
				<div class="layui-form-item">
				    <label class="layui-form-label">您的姓名</label>
				    <div class="layui-input-inline" style="width: 300px;">
				      <input type="text" name="username" id="username" placeholder="请输入您名字" autocomplete="off" class="layui-input" onchange="stop7InputOnchange('username', document.getElementById('username').value);">
				    </div>
				    <div class="layui-form-mid layui-word-aux">当前模版的开发者姓名</div>
				</div>
				<div class="layui-form-item">
				    <label class="layui-form-label">您的官网</label>
				    <div class="layui-input-inline" style="width: 300px;">
				      <input type="text" name="siteurl" id="siteurl" placeholder="请输入您公司或您的官方网站网址" autocomplete="off" class="layui-input" onchange="stop7InputOnchange('siteurl', document.getElementById('siteurl').value);">
				    </div>
				    <div class="layui-form-mid layui-word-aux">格式如： http://www.leimingyun.com</div>
				</div>
				<div style="font-size: 12px;color: gray;margin-top: -10px;padding-left: 110px;">如果是公司，请输入公司官网的网址。如果是个人，可填入个人网站或者博客的网址</div>
			    <div class="layui-form-item">
				    <label class="layui-form-label">模版简介</label>
				    <div class="layui-input-inline" style="width: 300px;">
				      <textarea name="remark" placeholder="请输入模版简介，可不填写" class="layui-textarea" id="remark" onchange="stop7InputOnchange('remark', document.getElementById('remark').value);"></textarea>
				    </div>
				    <div class="layui-form-mid layui-word-aux">
				    </div>
				</div>
				<div style="font-size: 12px;color: gray;margin-top: -10px;padding-left: 110px;">
					模版的简介，如模版是适用于展示机，可在此备注上，展示机显示的尺寸是1920*1680的
				    	<br/>注意，长度最大限制200个字符以内！
				</div>
			    
			</div>
			<div class="explain">
				注意：
				<ul>
					<li>填入的信息，会在模版列表中展示给用户看到。</li>
				</ul>
			</div>
		</div>
		
		<div>
			<div class="buzhoutitle">第八步：导出模版</div>
			<div class="" style=" padding-left: 40px;padding-top: 15px;">
			    <div class="layui-input-inline" >
			    	<button type="button" class="layui-btn layui-btn-primary" id="loadLocalTemplateFile" onclick="exportTemplate();">
						点此导出模版
					</button>
			    </div>
			</div>
			<div class="explain">
				注意：
				<ul>
					<li>导出的模版会包含你的所有资源文件（如js、css、图片等）</li>
					<li>导出的模版，可以在开通网站后，一键导入，即可快速创建一个网站！</li>
				</ul>
			</div>
		</div>
		
		<div>
			<div class="buzhoutitle">后记</div>
			<div class="" style=" padding-left: 40px;padding-top: 15px;">
			    <div class="layui-input-inline" >
			    	我们建议您将做好的模版共享给我们，来共同创建拓展网站模版！同时，您可以在模版中加入一些贵公司的信息，别人在浏览、使用的同时，也是在曝光您公司，也会为贵公司引流客户。
			    </div>
			</div>
			<div class="explain">
				共享模版条件：
				<ul>
					<li>模版中不可出现抢客户拉客户的字眼，如 某某公司XX元建站</li>
					<li>您可在网站最底部通栏，加入 技术支持：贵公司 ，同时加上自己公司主页的超链接</li>
					<li>模版中若有友情链接，可默认带着贵公司的友情链接</li>
				</ul>
			</div>
		</div>
		
		
	</div>
	

	
</div>
<script type="text/javascript">

//判断当前是否已经设置完第一步了，也就是是否已经有了模版编码了
if('${site.templateName}' != '' && '${site.templateName}' != 'null'){
	//更改步骤二中的模版显示
	document.getElementById('step2_path').style.display = '';
	document.getElementById('step2_weihuoqu').style.display = 'none';
	
	//设置第一步中的input为不可编辑
	//$('#templateName').attr("readonly",true);
}

</script>


<script src="/plugin/templateDevelop/js/js.js?v=<%=G.VERSION %>"></script>

<jsp:include page="../../iw/common/foot.jsp"></jsp:include> 