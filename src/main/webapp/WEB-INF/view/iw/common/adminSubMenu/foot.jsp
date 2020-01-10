<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
		</ul>
	</div>
	
	<div style="width: 100%;height:100%;position: absolute;left: 170px;word-wrap: break-word;border-right: 170px;box-sizing: border-box; padding-right: 10px; overflow-y: auto;overflow-x: hidden; border-right: 170px solid transparent;">
		<iframe src="" id="iframe" frameborder="0" style="width:100%; height:100%;"></iframe>
	</div>
</div>

<script>
layui.use('element', function(){
  var element = layui.element;
});

//右侧主区域，加载url
function loadUrl(url){
	document.getElementById("iframe").src=url;
}
</script>

</body>
</html>