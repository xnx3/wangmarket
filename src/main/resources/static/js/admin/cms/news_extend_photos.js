try{
	var photosHtml = document.getElementById('photosDefaultValue').innerHTML.trim();	//得到extend.photos的上一次保存的数据
	var itemTemplate = document.getElementById('photoInputList').innerHTML.trim();	//得到每一项item的模版
	document.getElementById('photoInputList').innerHTML = '';	//清空里面原本存在的item模版
	
	/**
	 * 判断图集是否有值，v4.7增加
	 * true：有值；
	 * false：无值
	 */
	function photosHaveValue(){
		if(photosHtml.length > 3){
			return true;
		}else{
			return false;
		}
	}
	
	if(photosHaveValue()){
		//有值，那么判断是有一个值，还是有多个值
		
		if(photosHtml.indexOf('[') > -1){
			//如果是有[] 存在，那么就是数组，就是有多个值
			var photos = JSON.parse(photosHtml);	//得到默认的数据值
			
			var inputListHTML = '';	//输出出来的，默认填写好的信息
			for(var i=0,l=photos.length;i<l;i++){
				appendPhotosInput(photos[i]);
			}
		}else{
			//如果没有[]存在，那么就是只有一个值，只有一个值的情况下是不输出数组，只输出某个具体值
			appendPhotosInput(photosHtml);
		}
	}else{
		//没有值，那么估计就是新增，或者上次编辑时没有保存入值。那么就什么都不让他显示好了
	}

	/**
	 * 向 photoInputList 里面的最后追加 input
	 * @param photoUrl 要显示的图片的url
	 */
	function appendPhotosInput(photoUrl){
		//先把添加input之前，那几个input输入框的数据拿出来
		var titlePicInput_i = 0;
		var titlePicInput_array = [];
		for(titlePicInput_i=0; titlePicInput_i < 500; titlePicInput_i++){
			var titlePicInput_name = 'titlePicInput'+titlePicInput_i;
			if(document.getElementById(titlePicInput_name) == null){
				titlePicInput_i = 500;
				break;
			}
			titlePicInput_array[titlePicInput_i] = document.getElementById(titlePicInput_name).value;
		}
		console.log(titlePicInput_array);
		
		var photos_i = document.getElementById('photos_i').value;
		var inputListHTML = itemTemplate.replace(/{i}/g, photos_i).replace(/{value}/g, photoUrl);
		document.getElementById('photos_i').value = parseInt(photos_i)+1;
		document.getElementById('photoInputList').innerHTML = document.getElementById('photoInputList').innerHTML + inputListHTML;
		
		//input添加后，在吧之前那些input输入框的数据补上
		for(var old_i = 0; old_i < titlePicInput_array.length; old_i++){
			try{
				document.getElementById('titlePicInput'+old_i).value = titlePicInput_array[old_i];
			}catch(e){ console.log(e); }
		}
		
		//当手动点击添加输入框按钮后，刷新上传按钮绑定
		if(typeof(upload) != "undefined"){
			upload.render(uploadExtendPhotos);
		}

	}

	/**
	 * 根据 图集 input 的输入编号（数组下标）来删除input输入框
	 * @param input_photo_i 要删除的数组下标
	 */
	function deletePhotosInput(input_photo_i){
		var parent=document.getElementById("photoInputList");
		var child=document.getElementById("photos_input_item_"+input_photo_i);
		parent.removeChild(child);
	}
	
	
}catch(e){
	console.log(e);
}