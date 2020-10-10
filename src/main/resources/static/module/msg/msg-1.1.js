/**
 * 信息提醒，不依赖任何乱七八糟框架及其他文件，导入 msg.js ，msg.info('哈哈哈') 一句代码使用！
 * 作者：管雷鸣
 * 个人网站：www.guanleiming.com
 * 个人微信: xnx3com
 * 公司：潍坊雷鸣云网络科技有限公司
 * 公司官网：www.leimingyun.com
 */
var msg = {
	/*
	 * 错误的图
	 */
	errorIcon:'<svg style="width: 3rem;padding: 1.5rem; padding-bottom: 1.1rem; box-sizing: content-box;" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6977"><path d="M696.832 326.656c-12.8-12.8-33.28-12.8-46.08 0L512 465.92 373.248 327.168c-12.8-12.8-33.28-12.8-46.08 0s-12.8 33.28 0 46.08L466.432 512l-139.264 139.264c-12.8 12.8-12.8 33.28 0 46.08s33.28 12.8 46.08 0L512 558.08l138.752 139.264c12.288 12.8 32.768 12.8 45.568 0.512l0.512-0.512c12.8-12.8 12.8-33.28 0-45.568L557.568 512l139.264-139.264c12.8-12.8 12.8-33.28 0-46.08 0 0.512 0 0 0 0zM512 51.2c-254.464 0-460.8 206.336-460.8 460.8s206.336 460.8 460.8 460.8 460.8-206.336 460.8-460.8-206.336-460.8-460.8-460.8z m280.064 740.864c-74.24 74.24-175.104 116.224-280.064 115.712-104.96 0-205.824-41.472-280.064-115.712S115.712 616.96 115.712 512s41.472-205.824 116.224-280.064C306.176 157.696 407.04 115.712 512 116.224c104.96 0 205.824 41.472 280.064 116.224 74.24 74.24 116.224 175.104 115.712 280.064 0.512 104.448-41.472 205.312-115.712 279.552z" fill="#ffffff" p-id="6978"></path></svg>',
	/*
	 * 当前弹出窗口显示的id。每次弹出窗口都会生成一个随机id
	 */
	currentWindowsId:0,	
	
	/**
	 * 成功的提醒
	 * @param text 提示文字
	 * @param func 关闭提示后，要执行的方法
	 */
	success:function(text,func){
		this.show(text, '<svg style="width: 3rem;padding: 1.5rem; padding-bottom: 1.1rem; box-sizing: content-box;" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2351"><path d="M384 887.456L25.6 529.056 145.056 409.6 384 648.544 878.944 153.6 998.4 273.056z" p-id="2352" fill="#ffffff"></path></svg>');
		this.delayClose(1500, func);
	},
	/**
	 * 失败、错误的提醒
	 * @param text 提示文字
	 * @param func 关闭提示后，要执行的方法
	 */
	failure:function(text, func){
		this.show(text, this.errorIcon);
		this.delayClose(2500, func);
	},
	/**
	 * 提示信息
	 * @param text 提示文字
	 * @param func 关闭提示后，要执行的方法
	 */
	info:function(text, func){
		this.show(text, '<svg style="width: 3rem;padding: 1.5rem; padding-bottom: 1.1rem; box-sizing: content-box;" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7996"><path d="M509.979 959.316c-247.308 0-447.794-200.486-447.794-447.794S262.671 63.728 509.979 63.728s447.794 200.486 447.794 447.794-200.485 447.794-447.794 447.794z m0-814.171c-202.346 0-366.377 164.031-366.377 366.377s164.031 366.377 366.377 366.377c202.342 0 366.377-164.031 366.377-366.377S712.321 145.145 509.979 145.145z m-40.708 610.628c-40.709 0-40.709-40.708-40.709-40.708l40.709-203.543s0-40.709-40.709-40.709c0 0-40.709 0-40.709-40.709h122.126s40.709 0 40.709 40.709-40.709 162.834-40.709 203.543 40.709 40.709 40.709 40.709h40.709c-0.001 0-0.001 40.708-122.126 40.708z m81.417-407.085c-22.483 0-40.709-18.225-40.709-40.709s18.225-40.709 40.709-40.709 40.709 18.225 40.709 40.709-18.226 40.709-40.709 40.709z" p-id="7997" fill="#ffffff"></path></svg>');
		this.delayClose(2500, func);
	},
	/**
	 * 弹出询问选择框：确定、取消
	 */
	confirm:function(text){
		return confirm(text);
	},
	/**
	 * 加载中、等待中的动画效果
	 * @param text 提示文字
	 */
	loading:function(text){
		this.show(text, '<img src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAzMiAzMiIgd2lkdGg9IjY0IiBoZWlnaHQ9IjY0IiBmaWxsPSIjRjlGOUY5Ij4KICA8Y2lyY2xlIGN4PSIxNiIgY3k9IjMiIHI9IjAiPgogICAgPGFuaW1hdGUgYXR0cmlidXRlTmFtZT0iciIgdmFsdWVzPSIwOzM7MDswIiBkdXI9IjFzIiByZXBlYXRDb3VudD0iaW5kZWZpbml0ZSIgYmVnaW49IjAiIGtleVNwbGluZXM9IjAuMiAwLjIgMC40IDAuODswLjIgMC4yIDAuNCAwLjg7MC4yIDAuMiAwLjQgMC44IiBjYWxjTW9kZT0ic3BsaW5lIiAvPgogIDwvY2lyY2xlPgogIDxjaXJjbGUgdHJhbnNmb3JtPSJyb3RhdGUoNDUgMTYgMTYpIiBjeD0iMTYiIGN5PSIzIiByPSIwIj4KICAgIDxhbmltYXRlIGF0dHJpYnV0ZU5hbWU9InIiIHZhbHVlcz0iMDszOzA7MCIgZHVyPSIxcyIgcmVwZWF0Q291bnQ9ImluZGVmaW5pdGUiIGJlZ2luPSIwLjEyNXMiIGtleVNwbGluZXM9IjAuMiAwLjIgMC40IDAuODswLjIgMC4yIDAuNCAwLjg7MC4yIDAuMiAwLjQgMC44IiBjYWxjTW9kZT0ic3BsaW5lIiAvPgogIDwvY2lyY2xlPgogIDxjaXJjbGUgdHJhbnNmb3JtPSJyb3RhdGUoOTAgMTYgMTYpIiBjeD0iMTYiIGN5PSIzIiByPSIwIj4KICAgIDxhbmltYXRlIGF0dHJpYnV0ZU5hbWU9InIiIHZhbHVlcz0iMDszOzA7MCIgZHVyPSIxcyIgcmVwZWF0Q291bnQ9ImluZGVmaW5pdGUiIGJlZ2luPSIwLjI1cyIga2V5U3BsaW5lcz0iMC4yIDAuMiAwLjQgMC44OzAuMiAwLjIgMC40IDAuODswLjIgMC4yIDAuNCAwLjgiIGNhbGNNb2RlPSJzcGxpbmUiIC8+CiAgPC9jaXJjbGU+CiAgPGNpcmNsZSB0cmFuc2Zvcm09InJvdGF0ZSgxMzUgMTYgMTYpIiBjeD0iMTYiIGN5PSIzIiByPSIwIj4KICAgIDxhbmltYXRlIGF0dHJpYnV0ZU5hbWU9InIiIHZhbHVlcz0iMDszOzA7MCIgZHVyPSIxcyIgcmVwZWF0Q291bnQ9ImluZGVmaW5pdGUiIGJlZ2luPSIwLjM3NXMiIGtleVNwbGluZXM9IjAuMiAwLjIgMC40IDAuODswLjIgMC4yIDAuNCAwLjg7MC4yIDAuMiAwLjQgMC44IiBjYWxjTW9kZT0ic3BsaW5lIiAvPgogIDwvY2lyY2xlPgogIDxjaXJjbGUgdHJhbnNmb3JtPSJyb3RhdGUoMTgwIDE2IDE2KSIgY3g9IjE2IiBjeT0iMyIgcj0iMCI+CiAgICA8YW5pbWF0ZSBhdHRyaWJ1dGVOYW1lPSJyIiB2YWx1ZXM9IjA7MzswOzAiIGR1cj0iMXMiIHJlcGVhdENvdW50PSJpbmRlZmluaXRlIiBiZWdpbj0iMC41cyIga2V5U3BsaW5lcz0iMC4yIDAuMiAwLjQgMC44OzAuMiAwLjIgMC40IDAuODswLjIgMC4yIDAuNCAwLjgiIGNhbGNNb2RlPSJzcGxpbmUiIC8+CiAgPC9jaXJjbGU+CiAgPGNpcmNsZSB0cmFuc2Zvcm09InJvdGF0ZSgyMjUgMTYgMTYpIiBjeD0iMTYiIGN5PSIzIiByPSIwIj4KICAgIDxhbmltYXRlIGF0dHJpYnV0ZU5hbWU9InIiIHZhbHVlcz0iMDszOzA7MCIgZHVyPSIxcyIgcmVwZWF0Q291bnQ9ImluZGVmaW5pdGUiIGJlZ2luPSIwLjYyNXMiIGtleVNwbGluZXM9IjAuMiAwLjIgMC40IDAuODswLjIgMC4yIDAuNCAwLjg7MC4yIDAuMiAwLjQgMC44IiBjYWxjTW9kZT0ic3BsaW5lIiAvPgogIDwvY2lyY2xlPgogIDxjaXJjbGUgdHJhbnNmb3JtPSJyb3RhdGUoMjcwIDE2IDE2KSIgY3g9IjE2IiBjeT0iMyIgcj0iMCI+CiAgICA8YW5pbWF0ZSBhdHRyaWJ1dGVOYW1lPSJyIiB2YWx1ZXM9IjA7MzswOzAiIGR1cj0iMXMiIHJlcGVhdENvdW50PSJpbmRlZmluaXRlIiBiZWdpbj0iMC43NXMiIGtleVNwbGluZXM9IjAuMiAwLjIgMC40IDAuODswLjIgMC4yIDAuNCAwLjg7MC4yIDAuMiAwLjQgMC44IiBjYWxjTW9kZT0ic3BsaW5lIiAvPgogIDwvY2lyY2xlPgogIDxjaXJjbGUgdHJhbnNmb3JtPSJyb3RhdGUoMzE1IDE2IDE2KSIgY3g9IjE2IiBjeT0iMyIgcj0iMCI+CiAgICA8YW5pbWF0ZSBhdHRyaWJ1dGVOYW1lPSJyIiB2YWx1ZXM9IjA7MzswOzAiIGR1cj0iMXMiIHJlcGVhdENvdW50PSJpbmRlZmluaXRlIiBiZWdpbj0iMC44NzVzIiBrZXlTcGxpbmVzPSIwLjIgMC4yIDAuNCAwLjg7MC4yIDAuMiAwLjQgMC44OzAuMiAwLjIgMC40IDAuOCIgY2FsY01vZGU9InNwbGluZSIgLz4KICA8L2NpcmNsZT4KICA8Y2lyY2xlIHRyYW5zZm9ybT0icm90YXRlKDE4MCAxNiAxNikiIGN4PSIxNiIgY3k9IjMiIHI9IjAiPgogICAgPGFuaW1hdGUgYXR0cmlidXRlTmFtZT0iciIgdmFsdWVzPSIwOzM7MDswIiBkdXI9IjFzIiByZXBlYXRDb3VudD0iaW5kZWZpbml0ZSIgYmVnaW49IjAuNXMiIGtleVNwbGluZXM9IjAuMiAwLjIgMC40IDAuODswLjIgMC4yIDAuNCAwLjg7MC4yIDAuMiAwLjQgMC44IiBjYWxjTW9kZT0ic3BsaW5lIiAvPgogIDwvY2lyY2xlPgo8L3N2Zz4K" style="width: 3rem;padding: 1.5rem; padding-bottom: 1.1rem; box-sizing: content-box;" />');
	},
	/**
	 * 关闭各种提示，包括加载中、成功、失败、提示信息等，都可以用此强制关闭
	 */
	close:function(){
		this.currentWindowsId = 0;	//当前没有任何窗口
		var loadingDiv = document.getElementById('wangmarket_loading');
		if(loadingDiv != null){
			var loadingDivParent = loadingDiv.parentNode;
			if(loadingDivParent != null){
				loadingDivParent.removeChild(loadingDiv);
			}
		}
		
		//关闭pupups相关
		var popupsDiv = document.getElementById('wangmarket_popups')
		if(popupsDiv != null){
			var popupsDivParent = popupsDiv.parentNode;
			if(popupsDivParent != null){
				popupsDivParent.removeChild(popupsDiv);
			}
		}
	},
	/**
	 * 延迟几秒后关闭弹出提示
	 * @param time 延迟多长时间，单位是毫秒
	 * @param func 关闭提示后，要执行的方法
	 */
	delayClose(time, func){
		var cid = parseInt(Math.random()*100000);
		this.currentWindowsId = cid;
		setTimeout(function(){
			if(msg.currentWindowsId == cid){
				/* 能对应起来，才会关闭。避免关闭别的刚显示的窗口 */
				msg.close();
			}
			if(func != null){
				func();
			}
		},time);
	},
	/**
	 * 显示提示窗口，私有方法
	 * text 提示文字
	 * img 显示的图片或者svg
	 */
	show(text, img){
		/** 是否是横向显示 **/
		var wangmarket_loading_hengxiang = false;
		if(text != null && text.length > 10){
			wangmarket_loading_hengxiang = true;
		}
		
		/** 显示前，如果还有其他正在显示的，将其都关掉 **/
		this.close();
		if(document.getElementsByTagName("body") != null && document.getElementsByTagName("body").length > 0){
			var div=document.createElement("div");
			div.id = 'wangmarket_loading';
			div.style = 'position: fixed;z-index: 2147483647;margin: 0 auto;text-align: center;width: 100%;';
			div.innerHTML = ''
				+'<div id="loading" style="position: fixed;top: 30%;text-align: center;font-size: 1rem;color: #dedede;margin: 0px auto;left: 50%;margin-left: -'+(wangmarket_loading_hengxiang? '9':'3.5')+'rem;">'
				+'<div style="width: 7rem;background-color: #2e2d3c;border-radius: 0.3rem; filter: alpha(Opacity=80); -moz-opacity: 0.8; opacity: 0.8; min-height: 4.8rem;'+(wangmarket_loading_hengxiang? 'width: 18rem;':'')+'">'
				+'<div'+(wangmarket_loading_hengxiang? ' style="float:left;height: 20rem; margin-top: -0.6rem;"':'')+'>'+img+'</div>'
				+'<div style="width: 100%;padding-bottom: 1.4rem; font-size: 1.1rem; padding-left: 0.3rem;padding-right: 0.3rem; box-sizing: border-box;line-height: 1.2rem;color: white;'+(wangmarket_loading_hengxiang? 'padding: 1rem; text-align: left; padding-right: 0.3rem; line-height: 1.5rem;':'')+'">'+text+'</div>'
				+'</div>';
				+'</div>';
			document.getElementsByTagName("body")[0].appendChild(div);
		}
	},
	/**
	 * 弹出层，弹出窗口
	 * @param text 弹出层显示的内容
	 * @param attribute 弹出层的其他属性。传入如： 
	 * 		<pre>
	 * 			{
	 *				top:'30%',			//弹出层距离顶部的距离，不传默认是30%。 可以传入如 30%、 5rem、 10px 等
	 *				bottom:'1rem',		//弹出层距离底部的距离。不传默认是 auto
	 *				close:false			//是否显示右上角的关闭按钮，不传默认是true，显示关闭按钮
	 *				background:'#2e2d3c'	//背景颜色。十六进制颜色编码。不传默认是 '#2e2d3c'
	 *				opacity:92			//弹出层的透明度，默认是92, 取值0~100，0是不透明，100是全部透明
	 *			}
	 * 		</pre>
	 * @param showClose 是否显示关闭按钮，默认显示。可传入 false 不显示关闭按钮
	 * @param top 弹出层显示的区块，距离顶部的距离
	 */
	popups:function(text, attribute){
		if(attribute == null){
			attribute = {}
		}
		//赋予默认属性
		if(attribute.close == null){
			attribute.close = true;
		}
		if(attribute.top == null){
			attribute.top = '30%';
		}
		if(attribute.bottom == null || attribute.bottom.length < 1){
			attribute.bottom = 'auto';
		}
		if(attribute.background == null){
			attribute.background = '#2e2d3c';
		}
		if(attribute.opacity == null){
			attribute.opacity = 92;
		}
		
		
		console.log(attribute);
		
		var div=document.createElement("div");
		div.id = 'wangmarket_popups';
		div.style = 'position: fixed; z-index: 2147483647; margin: 0px auto; text-align: center; width: 100%; ';
		div.innerHTML = '<div style="position: fixed; top:'+attribute.top+'; bottom:'+attribute.bottom+'; text-align: center;font-size: 1rem;color: #dedede;margin: 0px auto;width: 90%;left: 5%; height: auto;"> <div style="width: 100%;background-color: '+attribute.background+';border-radius: 0.3rem;filter: alpha(Opacity='+attribute.opacity+');-moz-opacity: '+(attribute.opacity/100)+';opacity: '+(attribute.opacity/100)+';min-height: 4.8rem; height: 100%;"> <div style=" width: 100%; font-size: 1.1rem; box-sizing: border-box; line-height: 1.6rem; color: white; text-align: left; padding: 1rem; overflow-y: auto; height: 100%;">'+text+'</div>'+
						(attribute.close? '<div style="top: 0px;position: absolute;right: 0px;margin-top: -0.8rem;margin-right: -0.4rem;background-color: aliceblue;border-radius: 50%;height: 2rem;width: 2rem;" onclick="msg.close();"><svg style="width: 2rem; height:2rem; cursor: pointer;" t="1601801323865" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4482" width="48" height="48"><path d="M512.001 15.678C237.414 15.678 14.82 238.273 14.82 512.86S237.414 1010.04 512 1010.04s497.18-222.593 497.18-497.18S786.589 15.678 512.002 15.678z m213.211 645.937c17.798 17.803 17.798 46.657 0 64.456-17.798 17.797-46.658 17.797-64.456 0L512.001 577.315 363.241 726.07c-17.799 17.797-46.652 17.797-64.45 0-17.804-17.799-17.804-46.653 0-64.456L447.545 512.86 298.79 364.104c-17.803-17.798-17.803-46.657 0-64.455 17.799-17.798 46.652-17.798 64.45 0l148.761 148.755 148.755-148.755c17.798-17.798 46.658-17.798 64.456 0 17.798 17.798 17.798 46.657 0 64.455L576.456 512.86l148.756 148.755z m0 0" fill="'+attribute.background+'" p-id="4483"></path></svg></div>':'')+
						'</div></div>';
		
		//<div style="width: 100%;padding-bottom: 1rem;font-size: 1.1rem;padding-left: 0.3rem;padding-right: 2.0rem;box-sizing: border-box;line-height: 1.2rem;color: white;text-align: right;"> <button style=" border: aliceblue; padding: 0.4rem; padding-left: 1rem; padding-right: 1rem; font-size: 0.8rem; background-color: darkcyan; " onclick="close1();">确定</button> </div>
		
		document.getElementsByTagName("body")[0].appendChild(div);
	}
}