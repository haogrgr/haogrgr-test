<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setAttribute("path", request.getContextPath());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<title>promise</title>

<script type="text/javascript" src="${path}/js/jquery-1.11.0.js"></script>

<script type="text/javascript">
	
</script>
</head>
<body>
	<div id="content"></div>
	<br>
	<div id="spinner">正在加载中...</div>
	<script type="text/javascript" src="${path}/app/promise.js"></script>
</body>
</html>