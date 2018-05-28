//站点中使用的，网站在线客服
//加载此之前，已自动加载res...js/fun.js 、./data/kefu.js、./data/site.js

//初始化各项参数，同时也是进行各项参数的检测。若检测合格，可进行会话，则返回true
function initParam(){
	//首先检测各项参数，如有缺失，进行补齐。若有参数不合法，则终止下面的执行
	if(typeof(kefu['nickname']) == 'undefined' || kefu['nickname'].length == 0){
		kefu['nickname'] = '在线客服';
	}
	if(typeof(site['userid']) == 'undefined' || site['userid'].length == 0){
		//uid缺失，无法组建聊天
		return false;
	}
	
	if(typeof(kefu['autoReply']) == 'undefined' || kefu['autoReply'].length == 0){
		kefu['autoReply'] = '抱歉，客服当前不在线，您可以先留言留下您的联系方式，客服回来联系你';
	}
	return true;
}

//配置文件加载完毕、并判断IM需要的各项属性完整，可以开启IM会话，便通过这里进行初始化IM会话
function initIM(){
	//会话的id要从缓存中拉取的
	var id = getCookie('xnx3_kefu_id');
	var username = getCookie('xnx3_kefu_username');
	var sign = getCookie('xnx3_kefu_sign');
	if(typeof(id) == undefined || id.length == 0){
		//缓存中没有存，那么这个客户可能是第一次访问，自动生成一个id
		id = new Date().getTime();
		//将生成的id缓存进Cookie
		setCookie('xnx3_kefu_id', id);
		
		//生成username
		var myDate = new Date();
		username = '访客'+myDate.getSeconds()+''+myDate.getMilliseconds();
		setCookie('xnx3_kefu_username', username);
	}
	
	dynamicLoading.css('http://res.weiunity.com/layui222/css/layui.css');
	//加载layui(包含layim)
	loadJs('http://res.weiunity.com/layui222/layui.js', function(){
		layui.config({
			  base: 'http://res.weiunity.com/layui222/lay/modules/' //假设这是你存放拓展模块的根目录
		});
		//各项组件、js文件加载完毕后，开始此处初始化客服模块
		layui.use('layim', function(layim){
			var layim = layui.layim;
			var socket = new WebSocket('ws://'+im_kefu_socketUrl+'/websocket?id='+id+'&username='+username+'&sign='+sign+'&parentId='+site['userid']+'&siteFangKe=true');
			//建立链接成功
			socket.onopen = function(){
				//layui.layer.msg("IM 接入成功");
				
				//基础配置
				layim.config({
				    //初始化接口
					init: {
						mine: {
							"username": username //我的昵称
							,"id": id //我的ID
							,"status": "online" //在线状态 online：在线、hide：隐身
							,"remark": sign //我的签名
							,"avatar": "http://res.weiunity.com/image/imqq.jpg" //我的头像
						}
						,friend: []
						,group: []
					}
				   	,isgroup: false
					,brief: true //是否简约模式（若开启则不显示主面板）
					,min:true
					,title:kefu['nickname']
					,initSkin: '2.jpg' //1-5 设置初始背景
					,notice: true //是否开启桌面消息提醒，默认false
					,copyright:true
				}).chat({
					name: kefu['nickname']
				    ,type: 'friend'
				    ,avatar: kefu['head']
				    ,id: site['userid']
				});
				layim.setChatMin(); //收缩聊天面板
			};
			
			//监听收到的聊天消息，假设你服务端emit的事件名为：chatMessage
			socket.onmessage = function(res){
				console.log(res);
				var json = JSON.parse(res.data);
				if(json.type == 'onlineList'){
					//访客不给它收到这个消息权限，不会给访客推送
				}else{
					//收到消息
					if(typeof(json.system) != 'undefined' && json.system != null && json.system){
						//系统消息
						//如果对方已经离线，那么自动回复设定好的信息
						if(json.content.indexOf('对方已下线') > 0){
							json.content = kefu['autoReply'];
							layim.setChatStatus('<span style="color:#FF5722;">离线</span>');
						}
					}else{
						//用户正常对话聊天消息
					}
					layim.getMessage(json);
				}
			};

			//监听发送消息
			layim.on('sendMessage', function(data){
				socket.send(JSON.stringify(data));
			});
		});
		
		//IM 完毕
	});
}

if(typeof(site) != 'undefined' && typeof(site['useKefu']) != 'undefined' && site['useKefu'] == '1'){
	//开启，使用网站的在线客服功能
	//加载kefu.js缓存文件
	loadJs(attachmentFileUrl+'site/'+site['id']+'/data/kefu.js?v='+time, function(){
		//属性配置缓存加载完毕后，最后判断一下，是否能达到开启IM会话的要求。
		if(initParam()){
			//若能达到开启会话的使用要求，则加载相应的依赖js
			initIM();
		}
	});
}else{
	//不使用在线客服，忽略掉
}
