<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*,java.io.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单管理中心</title>
<script type="text/javascript" language="JavaScript">
	var xmlHttpRequest = null;
	//标记变量，结束时间只获取当前系统时间一次
	var flag_current_date = true;

	//创建xmlhttprequest对象函数
	function creatXmlHttpRequest() {
		try {
			xmlHttpRequest = new XMLHttpRequest();
		} catch (e) {
			try {
				//6.0版本一下
				xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				try {
					//ie6.0版本以上
					xmlHttpRequest = ActiveXObject("Msxml2.XMLHTTP");
				} catch (e) {
					alert("浏览器不支持此网页...");
				}
			}
		}
	}

	//发送页面请求函数
	function sendRequest() {
		var query = document.getElementById("query_type");
		var query_type = query[query.selectedIndex].value;
		if (query_type != -1) {
			//创建xmlhttprequest对象
			creatXmlHttpRequest();
			var url = "/wsd_erp/QueryDate";
			//var url = "<c:url value='/QueryDate'/>";

			//调用公司app数据接口，返回的是json数据
			//var url = "http://151h286a02.iok.la:12230/wsd_cementapp/hello/getxspool";
			//创建一个请求
			xmlHttpRequest.open("POST", url, true);
			//post方式需要增加的代码，设置请求头信息
			xmlHttpRequest.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded");
			//发送请求，需要带参数的话就封装成一个字符串，作为参数
			xmlHttpRequest.send(getParams());
			//指定响应的函数
			xmlHttpRequest.onreadystatechange = processResponse;
		} else {
			//为-1的话，代表没有选择查询类型，进行提示
			window.alert("请选择查询类型");
		}
	}

	// 请求状态改变时，处理返回信息函数
	function processResponse() {
		if (xmlHttpRequest.readyState == 4) { // 判断对象状态    
			if (xmlHttpRequest.status == 200) { // 信息已经成功返回，开始处理信息    
				//正确响应后被回调的函数
				DisplayOrderInfo();
			} else { //页面不正常    
				window.alert("获取数据失败......");
			}
		}
	}

	//请求成功响应后回调函数
	function DisplayOrderInfo() {
		//获取查询类型
		var status = xmlHttpRequest.responseXML.getElementsByTagName("status");
		var query_type_code = status[0].getElementsByTagName("query_type")[0].firstChild.nodeValue;
		var datas = "";
		switch (query_type_code) {
		case "0":
			//品种汇总，品种同，合并数据（名称，数量--，总价--）
			var NAMES_0 = xmlHttpRequest.responseXML
					.getElementsByTagName("name");
			var COUNTS_0 = xmlHttpRequest.responseXML
					.getElementsByTagName("count");
			var TOTAL_PRICE_0 = xmlHttpRequest.responseXML
					.getElementsByTagName("total_price");
			datas += "<tr align='left'>" + "<td width='1024px'>"
					+ "<table width='100%' bgcolor='#d3d3d3'>" + "<tr>"
					+ "<td width='33.33%' align='left'>" + "商品名称" + "</td>"
					+ "<td width='33.33%' align='left'>" + "商品数量" + "</td>"
					+ "<td width='33.33%' align='left'>" + "商品总价" + "</td>"
					+ "</tr>" + "</table>" + "</td>" + "</tr>";
			//封装字符串数据
			for ( var i = 0; i < TOTAL_PRICE_0.length; i++) {
				var total_price = TOTAL_PRICE_0[i].firstChild.nodeValue;
				var name = NAMES_0[i].firstChild.nodeValue;
				var count = COUNTS_0[i].firstChild.nodeValue;
				//奇数行为白色底，偶数行为蓝色底
				if ((i % 2) == 0) {
					datas += "<tr align='left'>"
							+ "<td width='1024px'>"
							+ "<table width='100%'>"
							+ "<tr>"
							+ "<td width='33.33%' align='left' style='word-break:break-all'>"
							+ name + "</td>"
							+ "<td width='33.33%' align='left'>" + count
							+ "</td>" + "<td width='33.33%' align='left'>"
							+ total_price + "</td>" + "</tr>" + "</table>"
							+ "</td>" + "</tr>";
				} else if ((i % 2) == 1) {
					datas += "<tr align='left'>"
							+ "<td width='1024px'>"
							+ "<table width='100%' bgcolor='#d3d3d3'>"
							+ "<tr>"
							+ "<td width='33.33%' align='left' style='word-break:break-all'>"
							+ name + "</td>"
							+ "<td width='33.33%' align='left'>" + count
							+ "</td>" + "<td width='33.33%' align='left'>"
							+ total_price + "</td>" + "</tr>" + "</table>"
							+ "</td>" + "</tr>";
				}

			}
			break;
		case "1":
			//日期汇总，日期同，合并数据（日期，数量--，总价--）
			var DATA_1 = xmlHttpRequest.responseXML
					.getElementsByTagName("data");
			var COUNTS_1 = xmlHttpRequest.responseXML
					.getElementsByTagName("count");
			var TOTAL_PRICE_1 = xmlHttpRequest.responseXML
					.getElementsByTagName("total_price");
			datas += "<tr align='left'>" + "<td width='1024px'>"
					+ "<table width='100%' bgcolor='#d3d3d3'>" + "<tr>"
					+ "<td width='33.33%' align='left'>" + "订单日期" + "</td>"
					+ "<td width='33.33%' align='left'>" + "商品数量" + "</td>"
					+ "<td width='33.33%' align='left'>" + "商品总价" + "</td>"
					+ "</tr>" + "</table>" + "</td>" + "</tr>";
			//封装字符串数据
			for ( var i = 0; i < TOTAL_PRICE_1.length; i++) {
				var data = DATA_1[i].firstChild.nodeValue;
				var count = COUNTS_1[i].firstChild.nodeValue;
				var total_price = TOTAL_PRICE_1[i].firstChild.nodeValue;
				if ((i % 2) == 0) {
					datas += "<tr align='left'>" + "<td width='1024px'>"
							+ "<table width='100%'>" + "<tr>"
							+ "<td width='33.33%' align='left'>" + data
							+ "</td>" + "<td width='33.33%' align='left'>"
							+ count + "</td>"
							+ "<td width='33.33%' align='left'>" + total_price
							+ "</td>" + "</tr>" + "</table>" + "</td>"
							+ "</tr>";
				} else if ((i % 2) == 1) {
					datas += "<tr align='left'>" + "<td width='1024px'>"
							+ "<table width='100%' bgcolor='#d3d3d3'>" + "<tr>"
							+ "<td width='33.33%' align='left'>" + data
							+ "</td>" + "<td width='33.33%' align='left'>"
							+ count + "</td>"
							+ "<td width='33.33%' align='left'>" + total_price
							+ "</td>" + "</tr>" + "</table>" + "</td>"
							+ "</tr>";
				}
			}

			break;
		case "2":
			//车牌汇总，车牌同，合并数据（车牌，数量--，总价--）
			var CAR_CODE_2 = xmlHttpRequest.responseXML
					.getElementsByTagName("car_code");
			var COUNTS_2 = xmlHttpRequest.responseXML
					.getElementsByTagName("count");
			var TOTAL_PRICE_2 = xmlHttpRequest.responseXML
					.getElementsByTagName("total_price");
			datas += "<tr align='left'>" + "<td width='1024px'>"
					+ "<table width='100%' bgcolor='#d3d3d3'>" + "<tr>"
					+ "<td width='33.33%' align='left'>" + "车牌号码" + "</td>"
					+ "<td width='33.33%' align='left'>" + "商品数量" + "</td>"
					+ "<td width='33.33%' align='left'>" + "商品总价" + "</td>"
					+ "</tr>" + "</table>" + "</td>" + "</tr>";
			//封装字符串数据
			for ( var i = 0; i < TOTAL_PRICE_2.length; i++) {
				var car_code = CAR_CODE_2[i].firstChild.nodeValue;
				var count = COUNTS_2[i].firstChild.nodeValue;
				var total_price = TOTAL_PRICE_2[i].firstChild.nodeValue;
				if ((i % 2) == 0) {
					datas += "<tr align='left'>" + "<td width='1024px'>"
							+ "<table width='100%'>" + "<tr>"
							+ "<td width='33.33%' align='left'>" + car_code
							+ "</td>" + "<td width='33.33%' align='left'>"
							+ count + "</td>"
							+ "<td width='33.33%' align='left'>" + total_price
							+ "</td>" + "</tr>" + "</table>" + "</td>"
							+ "</tr>";
				} else if ((i % 2) == 1) {
					datas += "<tr align='left'>" + "<td width='1024px'>"
							+ "<table width='100%' bgcolor='#d3d3d3'>" + "<tr>"
							+ "<td width='33.33%' align='left'>" + car_code
							+ "</td>" + "<td width='33.33%' align='left'>"
							+ count + "</td>"
							+ "<td width='33.33%' align='left'>" + total_price
							+ "</td>" + "</tr>" + "</table>" + "</td>"
							+ "</tr>";
				}
			}
			break;
		case "3":
			//品种+日期，品种和日期相同合并数据，其他因素忽略（品种，日期，数量--，总价--）
			var NAMES_3 = xmlHttpRequest.responseXML
					.getElementsByTagName("name");
			var DATA_3 = xmlHttpRequest.responseXML
					.getElementsByTagName("data");
			var COUNTS_3 = xmlHttpRequest.responseXML
					.getElementsByTagName("count");
			var TOTAL_PRICE_3 = xmlHttpRequest.responseXML
					.getElementsByTagName("total_price");
			datas += "<tr align='left'>" + "<td width='1024px'>"
					+ "<table width='100%' bgcolor='#d3d3d3'>" + "<tr>"
					+ "<td width='25%' align='left'>" + "订单日期" + "</td>"
					+ "<td width='25%' align='left'>" + "品种名称" + "</td>"
					+ "<td width='25%' align='left'>" + "商品数量" + "</td>"
					+ "<td width='25%' align='left'>" + "商品总价" + "</td>"
					+ "</tr>" + "</table>" + "</td>" + "</tr>";
			//封装字符串数据
			for ( var i = 0; i < TOTAL_PRICE_3.length; i++) {
				var data = DATA_3[i].firstChild.nodeValue;
				var name = NAMES_3[i].firstChild.nodeValue;
				var count = COUNTS_3[i].firstChild.nodeValue;
				var total_price = TOTAL_PRICE_3[i].firstChild.nodeValue;
				if ((i % 2) == 0) {
					datas += "<tr align='left'>"
							+ "<td width='1024px'>"
							+ "<table width='100%'>"
							+ "<tr>"
							+ "<td width='25%' align='left'>"
							+ data
							+ "</td>"
							+ "<td width='25%' align='left' style='word-break:break-all'>"
							+ name + "</td>" + "<td width='25%' align='left'>"
							+ count + "</td>" + "<td width='25%' align='left'>"
							+ total_price + "</td>" + "</tr>" + "</table>"
							+ "</td>" + "</tr>";
				} else if ((i % 2) == 1) {
					datas += "<tr align='left'>"
							+ "<td width='1024px'>"
							+ "<table width='100%' bgcolor='#d3d3d3'>"
							+ "<tr>"
							+ "<td width='25%' align='left'>"
							+ data
							+ "</td>"
							+ "<td width='25%' align='left' style='word-break:break-all'>"
							+ name + "</td>" + "<td width='25%' align='left'>"
							+ count + "</td>" + "<td width='25%' align='left'>"
							+ total_price + "</td>" + "</tr>" + "</table>"
							+ "</td>" + "</tr>";
				}
			}
			break;
		case "4":
			//日期+车牌，。。。（日期，车牌，数量--，总价--）
			var DATA_4 = xmlHttpRequest.responseXML
					.getElementsByTagName("data");
			var CAR_CODE_4 = xmlHttpRequest.responseXML
					.getElementsByTagName("car_code");
			var COUNTS_4 = xmlHttpRequest.responseXML
					.getElementsByTagName("count");
			var TOTAL_PRICE_4 = xmlHttpRequest.responseXML
					.getElementsByTagName("total_price");
			datas += "<tr align='left'>" + "<td width='1024px'>"
					+ "<table width='100%' bgcolor='#d3d3d3'>" + "<tr>"
					+ "<td width='25%' align='left'>" + "订单日期" + "</td>"
					+ "<td width='25%' align='left'>" + "车牌号码" + "</td>"
					+ "<td width='25%' align='left'>" + "商品数量" + "</td>"
					+ "<td width='25%' align='left'>" + "商品总价" + "</td>"
					+ "</tr>" + "</table>" + "</td>" + "</tr>";
			//封装字符串数据
			for ( var i = 0; i < TOTAL_PRICE_4.length; i++) {
				var data = DATA_4[i].firstChild.nodeValue;
				var car_code = CAR_CODE_4[i].firstChild.nodeValue;
				var count = COUNTS_4[i].firstChild.nodeValue;
				var total_price = TOTAL_PRICE_4[i].firstChild.nodeValue;
				if ((i % 2) == 0) {
					datas += "<tr align='left'>" + "<td width='1024px'>"
							+ "<table width='100%'>" + "<tr>"
							+ "<td width='25%' align='left'>" + data + "</td>"
							+ "<td width='25%' align='left'>" + car_code
							+ "</td>" + "<td width='25%' align='left'>" + count
							+ "</td>" + "<td width='25%' align='left'>"
							+ total_price + "</td>" + "</tr>" + "</table>"
							+ "</td>" + "</tr>";
				} else if ((i % 2) == 1) {
					datas += "<tr align='left'>" + "<td width='1024px'>"
							+ "<table width='100%' bgcolor='#d3d3d3'>" + "<tr>"
							+ "<td width='25%' align='left'>" + data + "</td>"
							+ "<td width='25%' align='left'>" + car_code
							+ "</td>" + "<td width='25%' align='left'>" + count
							+ "</td>" + "<td width='25%' align='left'>"
							+ total_price + "</td>" + "</tr>" + "</table>"
							+ "</td>" + "</tr>";
				}
			}
			break;
		}
		document.getElementById("values").innerHTML = datas;
	}

	//封装http请求参数
	function getParams() {
		var params = null;
		//参数：pageNum(第几页：从0开始)
		//参数：pageSize(单页数量50)

		//获取各个控件的值
		var start_time = document.getElementById("start_time").value;

		var end_time = document.getElementById("end_time").value;
		var query = document.getElementById("query_type");

		//正确的值为：-1,0,1，2，3，4
		var query_type = query[query.selectedIndex].value;
		var order = document.getElementById("order_status");
		//正确的值：0，1，2，3，4，5，9
		var order_status = order[order.selectedIndex].value;
		params = "start_time=" + start_time + "&end_time=" + end_time
				+ "&query_type=" + query_type + "&order_status=" + order_status;
		return params;
	}
	//预下单数据方法
	function ready_data() {
		sendRequest();
	}
	//测试数据方法
	function test_data() {
		document.getElementById("values").innerHTML = "<tr width='100%'>"
				+ "<td align='center' width='100%'>" + "暂无数据" + "</td>"
				+ "</tr>";
	}
	//进行鼠标监听
	var rows = document.getElementsByTagName("tr");
	for ( var i = 1; i < rows.length; i++) {
		rows[i].onmouseover = function() {//鼠标移上去,添加一个类'hilite'
			this.className += 'hilite';
		};
		rows[i].onmouseout = function() {//鼠标移开,改变该类的名称
			this.className = '';
		};
	}
</script>

<style type="text/css">
#all_div {
	width: 1024px;
	height: auto;
	margin-left: auto;
	margin-right: auto;
}

#left_div {
	width: 13.5%;
	height: auto;
	float: left;
	background-color: #00A0E9;
	margin-right: 0.5%;
}

#right_div {
	width: 86%;
	height: auto;
	float: left;
}

.data_table tr:hover,.data_table tr.hilite {
	background-color: yellow;
}

.classify_table tr:hover,.classify_table tr.hilite {
	background-color: white;
}
</style>

<%!
	//系统当前时间
	public String currentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String date_string = sdf.format(date);
		return date_string;
	}
	//系统当前时间往前推90天
	public String startTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		//利用大数据工具进行运算
		BigDecimal days = new BigDecimal(90);
		BigDecimal hours = new BigDecimal(24);
		BigDecimal Minute = new BigDecimal(60);
		BigDecimal second = new BigDecimal(60);
		BigDecimal millisecond = new BigDecimal(1000);
		BigDecimal currentmillis = new BigDecimal(date.getTime());
		BigDecimal threeMonthAgo = currentmillis.subtract(days.multiply(hours).multiply(Minute).multiply(second).multiply(millisecond));
		date.setTime(threeMonthAgo.longValue());
		String date_string = sdf.format(date);
		return date_string;
	}
	%>
</head>
<body onload="sendRequest()">
	<h2 align="center">
		<font size="5" face="微软雅黑">欢迎进入订单管理中心</font>
	</h2>

	<!-- 最外层div -->
	<div id="all_div">

		<!-- 左边div -->
		<div id="left_div">
			<table>
				<tr>
					<td style="padding: 5px;"><font size="3" face="微软雅黑">---数据分类---</font></td>
				</tr>
			</table>
			<table class="classify_table"
				style="width: 100%; height: auto; border: 0px;">
				<tr>
					<td onclick="ready_data()" style="padding: 5px;">预下单数据</td>
				</tr>
				<tr>
					<td onclick="test_data()" style="padding: 5px;">测试数据01</td>
				</tr>
				<tr>
					<td onclick="test_data()" style="padding: 5px;">测试数据02</td>
				</tr>
				<tr>
					<td onclick="test_data()" style="padding: 5px;">测试数据03</td>
				</tr>
				<tr>
					<td onclick="test_data()" style="padding: 5px;">测试数据04</td>
				</tr>
				<tr>
					<td onclick="test_data()" style="padding: 5px;">测试数据05</td>
				</tr>
			</table>
		</div>

		<!-- 右边div -->
		<div id="right_div">
			<table width="100%" bgcolor="#d3d3d3" align="center">
				<tr>
					<!--  开始时间-->
					<td width="10%" valign="middle" bgcolor="#00A0E9" align="center">开始时间</td>
					<td width="12%" valign="middle" align="left"><input
						id="start_time" type="date" value="<%=startTime()%>" style="width: 96%;" />
					</td>

					<!-- 结束时间 -->
					<td width="10%" valign="middle" bgcolor="#00A0E9" align="center">结束时间</td>
					<td width="12%" valign="middle" align="left"><input
						id="end_time" type="date" value="<%=currentDate()%>"
						style="width: 96%;" /></td>

					<td width="10%" valign="middle" align="center" bgcolor="#00A0E9">订单状态</td>

					<!-- 订单状态 -->
					<td width="12%" valign="middle" align="center"><select
						id="order_status"
						style="font-family: Verdana, Arial, Helvetica, sans-serif; width: 100%; height: 100%">
							<option selected="selected" value="-1">--订单状态--</option>
							<option value="0">等待审核</option>
							<option value="1">等待付款</option>
							<option value="2">已经付款</option>
							<option value="3">已经进厂</option>
							<option value="4">正在装车</option>
							<option value="5">装货完成</option>
							<option value="9">已经出厂</option>
					</select></td>

					<td width="10%" valign="middle" align="center" bgcolor="#00A0E9">查询类型</td>

					<!-- 查询类别 -->
					<td width="12%" valign="middle" align="center"><select
						id="query_type"
						style="font-family: Verdana, Arial, Helvetica, sans-serif; width: 100%; height: 100%">
							<option selected="selected" value="0">品种汇总</option>
							<option value="1">日期汇总</option>
							<option value="2">车牌汇总</option>
							<option value="3">品种+日期</option>
							<option value="4">车牌+日期</option>
					</select></td>

					<!-- 查询按钮 -->
					<td width="12%" valign="middle" align="left"><input
						type="button" value="查询" onclick="sendRequest()"
						style="width: 100%; background-color: #00A0E9;"></td>
				</tr>
			</table>
			<div style="height: 5px"></div>
			<table class="data_table" align="center" width="100%"
				bgcolor="#d3d3d3">
				<div id="values" align="left"
					style="height: auto; width: 100%; margin-left: auto; margin-right: auto;"></div>
			</table>
		</div>

		<!-- 最外层div结束 -->
	</div>

</body>
</html>