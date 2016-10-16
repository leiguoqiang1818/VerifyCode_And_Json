<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*,java.util.*,java.io.*, java.net.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录验证页面</title>

</head>

<body>
	<%	
		//设置请求编码格式，防止中文乱码
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
	//获取登录页面传递过来的参数
		String verify_code = request.getParameter("verify_code");
		System.out.println(verify_code);
		//获取session域中的保存参数
		String verifyCode = (String)request.getSession().getAttribute("verify_code");
		System.out.println(verifyCode);
		//验证码正确的情况
		if(verifyCode.equalsIgnoreCase(verify_code)){
			//进行用户名和密码判断
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance(); 
			 String url="jdbc:sqlserver://localhost:1433;DatabaseName=QuJinKunGang"; 
			 //pubs为数据库名字
			 String Username="sa"; 
		     String Password="654321"; 
			 Connection conn= DriverManager.getConnection(url,Username,Password); 
			 Statement stmt=conn.createStatement(); 
		     ResultSet rst=stmt.executeQuery("select * from YW_ClientInfo");
		  	 String user_name=request.getParameter("ID");
		  	 String pass_word=request.getParameter("PWD");
		  	 List<Map<String,String>> list_user_info = new ArrayList<Map<String,String>>();
		  	//取出所有用户信息
		 while(rst.next())
		 	{
			 	//得到数据表中的用户代码
		  		String ID=rst.getString("YWC_ClientCode");
			 	//得到数据表中的密码
			 	String PWD=rst.getString("YWC_PassWord");
			 	Map<String,String> map = new HashMap<String,String>();
			 	map.put(ID, PWD);
			 	list_user_info.add(map);
		 	}
		 //遍历所有用户信息，进行校验
		 int i = 0;
		 for(Map<String,String> map:list_user_info){
			 for(Map.Entry<String, String> param : map.entrySet()){
			 if((param.getKey()).equals(user_name)&&(param.getValue()).equals(pass_word))
			 	{
				 //用户名和密码都正确，跳转到order_manager.jsp页面
				 response.sendRedirect("order_manager.jsp");
				 return;
	
		}else{
		if(i==list_user_info.size()-1){
			//用户名和密码不全部正确,跳转到login.jsp页面,参数0代表用户名或密码错误
			//response.sendRedirect("login.jsp?usename_or_password_eorror=0");
			}
			}
		%>
	<jsp:forward page="login.jsp">
		<jsp:param value="用户名或密码错误" name="usename_or_password_eorror" />
	</jsp:forward>
	<% 
			}
				i++;}
			 	rst.close();
			 	stmt.close();
			 	conn.close();
		//验证码不正确的情况
		}else{  
			
			
			
			//进行验证码错误提示，并转发login.jsp
			%>
	<jsp:forward page="login.jsp">
		<jsp:param value="验证码错误" name="verify_code_eorror" />
	</jsp:forward>
	<%
		}
		%>
</body>
</html>