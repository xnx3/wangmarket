<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
  	<meta charset="utf-8">
    <title>CMS模版列表</title>
    <meta name="keywords" content="网·市场,网市场" />
	<meta name="description" content="网·市场建站平台可用模版列表" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
	<link rel="stylesheet" href="http://res.weiunity.com/css/weui.min.css">

	<script src="http://res.weiunity.com/js/jquery-2.1.4.js"></script>
	
<!-- author:管雷鸣 -->
<style>
.list{
	padding-left: 0px;
}
.list>div{
	float:left;
	width:22%;
	padding:1.5%;
}
.list>div>div{
	padding-bottom: 65px;
}

/*预览图*/
.previewImg{
	width:100%;
	cursor: pointer;
}
/*预览按钮*/
.previewButton{
	margin-top: -30px;
	text-align: right;
	overflow-x: hidden;
}
.previewButton a{
    background-color: #FF5722;
    border-radius: 15px;
    padding: 3px;
    padding-left: 12px;
    padding-right: 22px;
    cursor: pointer;
    color: #eeeeee;
    font-size: 18px;
    margin-right: -12px;
}

/*模版名字，编号*/
.templateName{
	width:100%;
	text-align:center;
	color:#2F4056;
	padding-top: 8px;
	cursor: pointer;
}
/*模版简介说明*/
.info{
	color:#c2c2c2;
}

@media screen and (max-width:600px){
.list>div{
	float:left;
	width:45%;
	padding:2.5%;
}
.list>div>div{
	padding-bottom: 30px;
}

}
</style>

<body>

<div class="list" id="cloudList">
	加载中...

</div>

<script type="text/javascript">

//加载云端的模版列表
function loadCloudTemplateList(){
	$.getJSON('http://res.weiunity.com/cloudControl/cmsTemplate.json',function(obj){
		var html = '';	//云端模版的列表
			if(obj.result == '1'){
				var elseList = [
					{"intro":"标准手机站，黑色大气，手机网站","name":"1", "url":"w"}
					,{"intro":"标准手机站，蓝色风格，手机网站", "name":"2", "url":"saizhuo"}
					,{"intro":"标准企业站，黑色大气，电脑网站","name":"3","url":"sanmudi"}
					,{"intro":"滚屏风格网站，整屏滚动，手机电脑通用","name":"7","url":"weifangwangzhanjianshe"}
					,{"intro":"标准企业站，蓝色简约，电脑网站","name":"5","url":"cuihaomin"}
					,{"intro":"标准企业站，适合懒人、服务业，手机电脑通用","name":"6","url":"tongguang"}
					,{"intro":"标准手机站，可用于企业，也可用于个人名片，手机网站","name":"4","url":"guanleiming"}
				];
				
				var list = obj.list.concat(elseList);
				
				var divArray = new Array();	//共分四列，也就是下标为0～3
				for(var i=0; i<list.length; i++){
					var xiabiao = i%4;	//取余，得数组下表
					var to = list[i];
					var url = "";
					if(to.name == 1 || to.name == 2 || to.name == 3 || to.name == 4 || to.name == 5 || to.name == 6 || to.name == 7){
						url = to.url;
					}else{
						url = to.name;
					}
					var temp = '<div>'+
								'<img src="http://res.weiunity.com/template/'+to.name+'/preview.jpg" class="previewImg" onclick="window.open(\'http://'+url+'.wscso.com\');" />'+
								'<div class="previewButton"><a href="http://'+url+'.wscso.com" target="_black">点此预览</a></div>'+
								'<div class="templateName" onclick="useCloudTemplate(\''+to.name+'\');">模版编号：'+to.name+'</div>'+
								'<div class="info">'+to.intro+'</div>'+
						 		'</div>';
					if(divArray[xiabiao] == null){
						divArray[xiabiao] = '';
					}	 		
					divArray[xiabiao] = divArray[xiabiao] + temp;
				}
				
				//将四个div分别遍历，组合成显示的html
				for(var i=0; i<divArray.length; i++){
					html = html + '<div>' + divArray[i] +'</div>';
				}
				
				document.getElementById("cloudList").innerHTML = html;
	     	}else if(obj.result == '0'){
	     		 $.toast(obj.info, "cancel", function(toast) {});
	     	}else{
	     		alert(obj.result);
	     	}
		});
}


loadCloudTemplateList();
</script>
</body>
</html>