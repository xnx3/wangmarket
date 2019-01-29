<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ taglib uri="http://www.xnx3.com/java_xnx3/xnx3_tld" prefix="x" %>
<%@page import="com.xnx3.wangmarket.admin.G"%>
<jsp:include page="../iw/common/head.jsp">
	<jsp:param name="title" value="爬虫统计"/>
</jsp:include>
<script src="//res.weiunity.com/js/jquery-2.1.4.js"></script>

<!-- echarts -->
<script src="//res.weiunity.com/js/echarts.min.js" type="text/javascript"></script>
</head>
<body>

<div style="padding:20px;">
	
	<!-- 当日搜索引擎爬虫情况 -->
	<div id="spiderDay" style="width: 100%;height:400px;"></div>
	
	<div>
		<br/>
		<blockquote class="layui-elem-quote">显示最近7天内的100条访问记录(10分钟缓存时间，比如现在访问网站后，会在10分钟内访问数据才会进入统计，并不是立马就会统计上)</blockquote>
		<table class="layui-table">
		  <colgroup>
		    <col width="60">
		    <col width="140">
		    <col width="135">
		    <col width="135">
		    <col width="150">
		    <col width="250">
		  </colgroup>
		  <thead>
		    <tr>
		   	  <th>记录</th>
		      <th>访问时间</th>
		      <th>访问页面</th>
		      <th>访问者IP</th>
		      <th>访客系统信息</th>
		    </tr> 
		  </thead>
		  <tbody id="logList">
		  		<tr>
		  			<td colspan="5" style="text-align:center; padding:25px; font-size:20px;">日志记录加载中...</td>
		  		</tr>
		  </tbody>
		</table>
	</div>
	
</div>

<script type="text/javascript">
// 基于准备好的dom，初始化echarts实例
var spiderDay = echarts.init(document.getElementById('spiderDay'));
spiderDay.showLoading();
$.get('/requestLog/spiderCount.do').done(function (data) {
    spiderDay.hideLoading();
    var spiderName=[];
    var dl = eval(data.info);
    for(var i = 0; i < dl.length; i++){
    	spiderName[i] = dl[i].name;
    }
    
    spiderDay.setOption({
	    title : {
	        text: '最近七天，各搜索引擎爬虫抓取记录',
	        subtext: '抓取记录',
	        x:'center'
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        left: 'left',
	        data: spiderName
	    },
	    series : [
	        {
	            name: '爬虫抓取',
	            type: 'pie',
	            radius : '55%',
	            center: ['50%', '60%'],
	            /* data:[
	                {value:335, name:'直接访问'},
	                {value:310, name:'邮件营销'},
	                {value:234, name:'联盟广告'},
	                {value:135, name:'视频广告'},
	                {value:1548, name:'搜索引擎'}
	            ], */
	            data:dl,
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }
	    ]
	});
});


//加载logList日志列表
$.get('/requestLog/spiderList.do').done(function (data) {
	if(data.result == '1'){
		var table = '';
		for(var o = 0; o < data.list.length; o++){
			var json = data.list[o];
			table = table + 
					'<tr>'+
					'	<td>'+(o+1)+'</td>'+
					'	<td>'+json.logtimeString+'</td>'+
					'	<td><a href="http://'+json.__topic__+'/'+json.htmlFile+'" target="_black">'+json.htmlFile+'</a></td>'+
					'	<td>'+json.ip+'</td>'+
					'	<td><a href="javascript:;" title="'+json.userAgent+'">'+json.os+'</a></td>'+
					'</tr>';
		}
		document.getElementById('logList').innerHTML = table;
	}
});

//字符串截取
function subString(text){
	if(text.length > 40){
		text = text.substr(0,40)+'...';
	}
	return text;
}

//传入10位时间戳，返回描述文字
function timezhuanhuan(timestamp){
	var d = new Date(timestamp * 1000);    //根据时间戳生成的时间对象
	var date = (d.getFullYear()) + "-" + 
           (d.getMonth() + 1) + "-" +
           (d.getDate()) + " " + 
           (d.getHours()) + ":" + 
           (d.getMinutes()) + ":" + 
           (d.getSeconds());
	return date;
}
</script>

</body></html>