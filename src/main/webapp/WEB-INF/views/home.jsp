<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    request.setAttribute("path", request.getContextPath());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="${path}">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<title>index</title>
<link href="${path}/css/pagination.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${path}/js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="${path}/js/jquery.pagination.js"></script>
<script type="text/javascript" src="${path}/js/jquery.ys.pagination.js"></script>
<script type="text/javascript">
	$(function() {
		var pageHaper = new $.PageProcesser("table", "${path}/json", 10, {
			param:{type:"1"}, 
			rowHandler:function(index, data, tr){//双行变红
				index % 2 == 0 ? tr.css("color", "red") : "";
			}
		});
		$("#page_bar").pagination(100,{callback:function(pageIndex, pagination){
				return pageHaper.callback(pageIndex, pagination);
			}
		});
	});
</script>
</head>
<body>
	<table id="table" style="border: 1px; width: 50%; text-align: center;">
		<thead>
			<tr head>
				<th name="id">主键</th>
				<th name="name">姓名</th>
				<th name="link" opts="{handler:function(index, data, td){
					var a = $('<a>');
					a.attr('href','http://www.baidu.com/');
					a.text('删除:' + data.id);
					a.appendTo(td);
				}}">操作</th>
			</tr>
		</thead>
		<tbody data></tbody>
	</table>
	<div id="page_bar" style="margin-top: 50px;"></div>
	
</body>
</html>