<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	登录失败，请重新登陆
	<br>
	<span>参数i的值：<%=request.getParameter("count") %>></span>
	<span>集合总数据：<%=request.getParameter("totalcount") %></span>
	<a href="login.jsp">重新登录</a>
</body>
</html>