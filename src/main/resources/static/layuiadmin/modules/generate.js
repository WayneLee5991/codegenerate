layui.define(['form','table','layer','laytpl'],function(exports){
  var $ = layui.$
  ,form = layui.form
  ,laytpl = layui.laytpl
  ,layer = layui.layer
  ,setter = layui.setter
  ,admin = layui.admin
  
  var formSelects = layui.formSelects;
  
  admin.req({
  	type:'get'
  	,url:setter.serverUrl + 'tables'
  	,done:function(res){
  		var data = res.data;
  		
  		var arr = new Array();
  		for(var i = 0; i < data.length; i++){
  			var o = new Object();
  			o["name"] = data[i];
  			o["value"] = data[i];
  			arr[i] = o;
  		}
  		//console.log(arr);
  		formSelects.data('selectId', 'local', {
			    arr: arr
			});
		  
  	}
  })
  
  form.on('submit(LAY_app_table_submit)', function(data){
  	
	  var field = data.field;
	  var tableArray = field.tablelist.split(',');
	  var array = JSON.stringify(tableArray);
	  delete field.tablelist;
	  field.tablelist = array;
	  
	  admin.req({
	  	type:'post'
	  	,url:setter.serverUrl + 'generate'
	  	,data:field
	  	,done:function(res){
	  		var panName = $(".panName").val();
	  		var content = "代码生成成功 <br />请打开 【" + panName + "盘】 下的codeGenerate文件夹查看";
				layer.msg(content, {
				    offset: '15px'
				    ,icon: 1
				    ,time: 5000
				})
	  	}
	  })
	  return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});
  
  //对外暴露的接口
  exports('generate', {});
});