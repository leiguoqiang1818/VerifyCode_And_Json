<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登录页面</title>
<style type="text/css">
.main_table {
	width: 1024px;
	height: 768px;
	margin: 0px auto;
	background-image: url(img/main_background.jpg);
	background-repeat: repeat-x;
}
</style>
<script type="text/javascript">
	/*
		更换验证码图片方法
	 */
	function verify_code_change(){
		//得到img控件对象
		var img_ele = document.getElementById("img_verify_code");
		//改变该对象的src值，并且添加参数，为了避免浏览器的缓存
		img_ele.src = "/wsd_erp/VerifyCodeServlet?a="+new Date().getTime();
	}
</script>

</head>
<body>
	<%
		//设置请求编码格式，防止中文乱码
		request.setCharacterEncoding("utf-8");
		//获取验证错误信息
		String verify_code_eorror = "";
		String message = (String)request.getParameter("verify_code_eorror");
		System.out.println(message);
		//必须做判断，因为有可能message获取不到值
		if(message!=null){
			verify_code_eorror = message;
		}
		//获取用户名和密码错误信息
		String usename_password_eorror =  "";
		String usename_message = (String)request.getParameter("usename_or_password_eorror");
		if(usename_message!=null){
			usename_password_eorror = usename_message;
		}
		System.out.println(usename_password_eorror);
	%>


	<table class="main_table" width="100%" height="100%" border="0">

		<tr align="center">
			<td valign="middle">

				<form action="login_check.jsp" method="post">
					<table border="0">
						<tr>
							<Td colspan="2" align="center"><font size="3" face="微软雅黑">欢迎登录</font></Td>
						</tr>
						<tr>
						<td></td>
						<td>
						<font color="yellow" size="2" face="微软雅黑"><%=usename_password_eorror %></font>
						</td>
						</tr>
						<tr>
							<td style="height: 20px"><font size="3" face="微软雅黑">用户名：</font></td>
							<td><input style="width: 150px; height: 20px" type="text"
								name="ID"></td>
						</tr>
						<tr>
							<td style="height: 20px"><font size="3" face="微软雅黑">密        码：</font></td>
							<td><input style="width: 150px; height: 20px"
								type="password" name="PWD"></td>
						</tr>
						<tr>
						<td></td>
						<td><font color="yellow" size="2" face="微软雅黑"><%=verify_code_eorror %></font></td>
						</tr>
						<tr>
							<td style="height: 20px"><font size="3" face="微软雅黑">验证码：</font></td>
							<td><input style="width: 75px; height: 20px" type="text"
								name="verify_code"> <img id="img_verify_code"
								onclick="verify_code_change()"
								
								style="height: 25px; margin-bottom: -8px; width: 70px;"
								src="/wsd_erp/VerifyCodeServlet"></td>

						</tr>
						<tr>
							<td align="left"><input type="reset" value="重置"
								style="width: 100%; height: 25px"></td>
							<td align="left"><input type="submit" value="登录"
								style="width: 155px; height: 25px"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
</body>
</html>