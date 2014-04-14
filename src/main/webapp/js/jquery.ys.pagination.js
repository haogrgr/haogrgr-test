/**
 * jquery插件,用来扩展jquery分页插件,
 * 该插件做以下事情:
 * 1.$.ajax.post异步请求数据,数据格式{rows:[]};
 * 2.将获取到的数据根据配置生成tbody内容
 */
 (function($){
	 
	$.PageProcesser = function(tableId, url, pageSize, opts){
		var that = this;
		this.tableId = tableId;
		this.url = url;
		this.pageSize = pageSize ? pageSize : 10;
		
		if(!tableId || !url){
			throw "PageProcesser : the param tableId and url are required";
		}
		
		this.opts = jQuery.extend({
			param:{},
			rowHandler:function(index, data, tr){return true;},},
			opts||{}
		);
		
		this.tdOpts = [];
		$("#" + tableId + " th[name]").each(function(index, element){
			var fieldName = $(this).attr("name");
			var fieldOpts = $(this).attr("opts");
			fieldOpts = fieldOpts ? eval("fieldOpts = " + fieldOpts) : {};
			fieldOpts = jQuery.extend({handler:function(index, data, td){return true;}}, fieldOpts);
			that.tdOpts.push({name:fieldName, index:index, opts:fieldOpts});
		});
	};
	
	$.extend($.PageProcesser.prototype, {
		
		callback:function(page_index, pagination){//pagination的回调函数
			this.requester(page_index);
			return false;
		},
		
		requester:function(page){//ajax请求数据
			var requestParam = jQuery.extend({page:(page+1), rows:this.pageSize}, this.opts.param);
			$.ajax({ type : "POST", url : this.url, data : requestParam, context:this,
				success : function(json) {
					this.handlerJson(json);
				}
			});
		},
		
		handlerJson:function(json){
			var datas = json ? (json.rows ? json.rows : []) : [] ;
			var tbody = $("#" + this.tableId + " tbody[data]");
			tbody.html("");
			
			for(var i=0 ; i<datas.length ; i++){
				var tr = $("<tr></tr>");
				for(var j=0 ; j<this.tdOpts.length ; j++){
					var val = datas[i][this.tdOpts[j].name];
					var td = $("<td></td>").text(val);
					this.tdOpts[j].opts.handler(i, datas[i], td);
					td.appendTo(tr);
				}
				this.opts.rowHandler(i, datas[i], tr);
				tr.appendTo(tbody);
			}
			
		},
	});
	
})(jQuery);
