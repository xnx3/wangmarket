//总管理后台、代理后台使用

//当前跟自己聊天的人的信息，再切换聊天窗口时会动态复制，将当前显示的聊天窗口的用户信息赋予此处。
var otherId = 1;
var otherUsername = '在线客服';
var otherAvatar = 'http://res.weiunity.com/image/imqq.jpg';

//当前我的好友列表，主要用来进行当前在线用户的防重。加入的都是 friendArray['id123']=friend对象
var friendArray = [];
layui.use('layim', function(layim){
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
			,friend: [
			          {"groupname": "可求助客服","id": 1000000001,"online": 0,"list": []},
			          {"groupname": "我的上级","id": 2000000001,"online": 0,"list": []},
			          {"groupname": "我的客户","id": 3000000001,"online": 0,"list": []}
					]
			,group: []
		}
	   	,isgroup: false
		,brief: false //是否简约模式（若开启则不显示主面板）
		,min:true
		,title:'在线求助'
		,initSkin: '2.jpg' //1-5 设置初始背景
		,notice: true //是否开启桌面消息提醒，默认false
		,copyright:true
		,chatLog: '../../im/chatRecord.do'
	});

	var socket = new WebSocket('ws://'+socketUrl+'/websocket?id='+id+'&password='+password+'&username='+username);
	//建立链接成功
	socket.onopen = function(){
		
	};
	
	//监听收到的聊天消息，假设你服务端emit的事件名为：chatMessage
	socket.onmessage = function(res){
		var json = JSON.parse(res.data);
		if(json.type == 'onlineList'){
			//先移除所有
			console.log('size:'+friendArray.length);
			for(var i=0; i<friendArray.length; i++){
				console.log('移除');
				console.log(friendArray[i]);
				layim.removeList(friendArray[i]);
			}
			
			//再加入
			var jsonArray = json.list;
			for(var i=0; i<jsonArray.length; i++){
				jsonArray[i].type = 'friend';
				try{
					layim.addList(jsonArray[i]);
				}catch(e){
					console.log(e);
				}
				
				//将其加入当前页面缓存
				friendArray.push(jsonArray[i]);
			}
		}else{
			//收到消息
			if(typeof(json.system) != 'undefined' && json.system != null && json.system){
				//系统消息
			}else{
				//用户正常对话聊天消息
				otherId = json.id;
				otherUsername = json.username;
				otherAvatar = json.avatar;
			}
			layim.getMessage(json);
		}
	};
	//监听发送消息
	layim.on('sendMessage', function(data){
		data.to.id = otherId;
		data.to.name = otherUsername;
		data.to.avatar = otherAvatar;
		socket.send(JSON.stringify(data));
	});
	
	//监听聊天窗口的切换
	layim.on('chatChange', function(res){
		otherId = res.data.id;
		otherUsername = res.data.name;
		otherAvatar = res.data.avatar;
	});
});
