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
<script type="text/javascript">
	$(function() {
		
		$("#page_bar").pagination(10, {
			callback : function(page){
				$.ajax({ type : "POST", url : "${path}/json",
					async : true, data : {page:(current_page+1), rows:"10"},
					success : function(json) {
						alert(json);
					}
				});
			},
			items_per_page : 10, //每页显示的条目数
			num_display_entries : 10, //默认值10可以不修改
			num_edge_entries : 1, //两侧显示的首尾分页的条目数
			prev_text : "上一页",
			next_text : "下一页",
			current_page : 0 //当前页索引 
		});
	});
</script>
</head>
<body>
	<table id="table">
		<thead>
			<tr>
				<th name="id">主键</th>
				<th name="name">姓名</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<div id="page_bar"></div>
</body>
</html>