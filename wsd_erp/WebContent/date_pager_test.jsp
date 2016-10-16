<%@page import="java.sql.*,java.io.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>处理请求页面</title>
</head>
<body>
	<!-- 定义系列方法 -->
	<%
	Connection conn = null;
	Statement statement = null;
	ResultSet resultset = null;

		//加载sql数据库jdbs操作对象
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
				.newInstance();
		String userName = "sa";
		String passWord = "654321";
		String url = "jdbc:sqlserver://localhost:1433;DatebaseName=QuJinKunGang";
		//初始化connection对象
		conn = DriverManager.getConnection(url, userName, passWord);
		//初始化会话对象
		statement = conn.createStatement();
		//sql查询语句,测试：查询所有订单信息
		String sql = "select * from QuJinKunGang..XS_Order_ABase";
		resultset = statement.executeQuery(sql);
	%>
	
	
	<h3 align="center">欢迎进入订单管理中心</h3>
	<table width="1024px" background="#d3d3d3" align="center">
		<tr>
			<!--  开始时间-->
			<td width="10%" valign="middle" bgcolor="#00A0E9" align="center">开始时间</td>
			<td width="20%" valign="middle" align="left"><input type="text"
				name="start_time"></td>

			<!-- 结束时间 -->
			<td width="10%" valign="middle" bgcolor="#00A0E9" align="center">结束时间</td>
			<td width="20%" valign="middle" align="left"><input type="text"
				name="end_time"></td>

			<!-- 查询类别 -->
			<td width="20%" valign="middle" align="left"><input type="text"
				value="查询类别" name="query_type"></td>

			<!-- 查询按钮 -->
			<td width="20%" valign="middle" align="left"><input
				type="button" value="查询" onclick="sendRequest()" width="100%"></td>
		</tr>
	</table>
	<br />
	<table align="center" width="1024px" background="#d3d3d3"> 
		
		<%
		//迭代输出查询的数据
		while (resultset.next()) {
			//订单编号
			String lade_id = resultset.getString("XSO_LadeID");
			//订单日期
			String set_data = resultset.getString("XSO_SetDate");
			//品种名称
			String cement_name = resultset.getString("XSO_CementName");
			//产品数量
			String product_count = resultset.getString("XSO_Number");
			//下单人联系方式
			String order_phone = resultset.getString("XSO_OrderPhone");
		%>
			<tr>
			<td>
			<%=set_data %>
			</td>
			<td>
			<%=cement_name %>
			</td>
			<td>
			<%=product_count %>
			</td>
			<td>
			<%=order_phone %>
			</td>
			</tr>
			
		<%
		}
		out.close();
		resultset.close();
		statement.close();
		conn.close();
		%>
		
	</table>
</body>
</html>