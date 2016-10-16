package com.wsd.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wsd.javaBean.QueryDateBean;

import net.sf.json.JSONObject;

/**
 * servlet:用于处理查询订单详细信息使用，放回json数据
 * @author leiguoqiang
 * QQ 274764936
 */
@WebServlet("/QueryDateJson")
public class QueryDateJson extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5076425661184945467L;
	/**
	 * 数据结合，封装查询结果数据
	 */
	private List<Map<String, String>> list_query_data = new ArrayList<Map<String,String>>();
	/**
	 * 服务器打印输出流对象
	 */
	private PrintWriter out_print = null;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryDateJson() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置响应体
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json;charset=UTF-8");
		response.setHeader("Cache-control", "no-cache");
		out_print = response.getWriter();
		String sql = "select * from QuJinKunGang..XS_Order_ABase";
		// 连接数据库,并且处理相关数据
		try {
			handleSqlserver(sql,request);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		out_print.flush();
		out_print.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置响应体
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json;charset=UTF-8");
		response.setHeader("Cache-control", "no-cache");
		out_print = response.getWriter();
		String sql = "select * from QuJinKunGang..XS_Order_ABase";
		// 连接数据库,并且处理相关数据
		try {
			handleSqlserver(sql,request);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		out_print.flush();
		out_print.close();
	}

	/**
	 * 自定义方法：加载数据库引擎，并且处理相关数据
	 * @param out:打印输出流对象
	 * @throws ParseException 
	 */
	private void handleSqlserver(String sql,HttpServletRequest request) throws ParseException{
		Connection conn = null;
		Statement statement = null;
		try {
			//加载数据库引擎
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			//获取数据库连接对象
			String url = "jdbc:sqlserver://localhost:1433;DatebaseName=QuJinKunGang";
			String userName = "sa";
			String passWord = "654321";
			//获取连接数据会话对象
			conn = DriverManager.getConnection(url, userName, passWord);
			//获取连接数据库会话对象
			statement = conn.createStatement();
			//进行数据处理
			handleDate(statement,sql,request);
			//关闭相关流和会话对象
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}finally{
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 自定义方法：对数据库进行查询操作
	 * @param out：打印输出流对象
	 * @param statement：连接数据会话对象
	 * @throws SQLException 
	 * @throws UnsupportedEncodingException 
	 * @throws ParseException 
	 */
	private void handleDate(Statement statement,String sql,HttpServletRequest request) throws SQLException, UnsupportedEncodingException, ParseException{
		//将所有数据清除
		list_query_data.clear();
		//根据请求体获取请求参数
		request.setCharacterEncoding("utf-8");
		//格式2016-8-15
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		//格式1，2，3，4，5
		//0 按品种汇总；1 按日期汇总；2 按车牌汇总；3 按品种 ＋日期；4 按日期＋车牌汇总
		String query_type = request.getParameter("query_type");
		//0:刚下单，等待审核；1 已审核、待付款；2 已付款；3 已进厂；4 正在装车； 5 装货完成； 9 已出厂 
		String order_status = request.getParameter("order_status");

		//开始时间
		long startTime = getMillis(start_time);
		//结束时间加上1天，把当天的数据也加载出来
		long endTime = getMillis(end_time)+24*60*60*1000;
		//查询类型
		int queryType = Integer.parseInt(query_type);
		//添加订单状态参数
		int orderStatus = Integer.parseInt(order_status);

		//获取数据库连接会话对象
		ResultSet result = statement.executeQuery(sql);
		//格式转换
		DecimalFormat df = new DecimalFormat("#0.00");
		//循环迭代所有数据
		outer:
			while(result.next()){
				//订单日期  yy-MM-dd hh-mm-ss.SSS
				String set_data = result.getString("XSO_SetDate");
				
				//转换成  yy-MM-dd   getCurrentDay(set_data)
				//转换成毫秒值
				long data = getMillis(getCurrentDay(set_data));//------------------------------------
				System.out.println("转换时间"+getCurrentDay(set_data));
				System.out.println("最后时间毫秒值"+data);
				//订单付款状态
				//0:刚下单，等待审核；1 已审核、待付款；2 已付款；3 已进厂；4 正在装车； 5 装货完成； 9 已出厂 
				int order_statu = result.getInt("XSO_Status");
				//首先判断时间，不在时间范围内就重新循环
				//1.在时间范围内 2.订单状态没有选择或与订单状态匹配参数，订单状态为-1时，意味查询所有订单状态数据
				if(((startTime<=data)&&(data<endTime)&&(orderStatus==order_statu))||((startTime<=data)&&(data<=endTime)&&(orderStatus==-1))){
					//品种名称
					String cement_name = result.getString("XSO_CementName");
					if((cement_name==null)||(cement_name.equals(""))){
						cement_name = "null";
					}

					//单个订单产品总价
					//				String total_price = result.getString("XSO_TotalPrice");
					BigDecimal bd_total_price = result.getBigDecimal("XSO_TotalPrice");
					if(bd_total_price==null){
						bd_total_price = new BigDecimal(0);
					}
					//将数据转换成long类型
					//				long total_price_long = bd_total_price.longValue();


					//产品数量
					BigDecimal bd_product_count = result.getBigDecimal("XSO_Number");
					if((bd_product_count==null)){
						bd_product_count = new BigDecimal(0);
					}
					//				//进行数据转换,转换成int类型
					//				long product_count_long = Long.parseLong(product_count);


					//汽车牌照
					String car_code = result.getString("XSO_CarCode");
					if((car_code==null)||(car_code.equals(""))){
						car_code = "null";
					}
					//进行数据筛选
					//0 按品种汇总；1 按日期汇总；2 按车牌汇总；3 按品种 ＋日期；4 按日期＋车牌汇总
					switch (queryType) {
					case 0:
						//品种汇总，品种同，合并数据（名称，数量--，总价--）
						//循环遍历查看集合中是否有相同的品种名称数据，有合并，无新建数据
						for(Map<String, String> map:list_query_data){
							//找到名称相同
							if((map.get("cement_name")).equals(cement_name)){
								//数量合并
								BigDecimal bd_old_product_count = new BigDecimal(map.get("product_count"));
								BigDecimal bd_add_product_count = bd_old_product_count.add(bd_product_count);
								map.put("product_count", df.format(bd_add_product_count));
								//总价合并
								BigDecimal bd_old_total_price = new BigDecimal(map.get("total_price"));
								BigDecimal bd_add_total_price = bd_old_total_price.add(bd_total_price);
								map.put("total_price", df.format(bd_add_total_price.doubleValue()));
								continue outer;
							}
						}
						//没有找到匹配名称的数据，新建map集合，封装新数据
						Map<String, String> map = new HashMap<String, String>();
						map.put("cement_name", cement_name);
						map.put("product_count", df.format(bd_product_count.doubleValue()));
						map.put("total_price", df.format(bd_total_price.doubleValue()));
						list_query_data.add(map);
						break;
					case 1:
						//日期汇总，日期同，合并数据（日期，数量--，总价--）
						//查看集合中是否有相同的日期
						for(Map<String, String> map1:list_query_data){
							if(map1.get("set_data").equals(getCurrentDay(set_data))){//----------------------
								//数量合并
								BigDecimal bd_old_product_count = new BigDecimal(map1.get("product_count"));
								BigDecimal bd_add_product_count = bd_old_product_count.add(bd_product_count);
								map1.put("product_count", df.format(bd_add_product_count));
								//总价合并
								BigDecimal bd_old_total_price = new BigDecimal(map1.get("total_price"));
								BigDecimal bd_add_total_price = bd_old_total_price.add(bd_total_price);
								map1.put("total_price", df.format(bd_add_total_price.doubleValue()));
								continue outer;
							}
						}
						//没有相同日期，则新建map集合封装数据
						Map<String, String> map1 = new HashMap<String, String>();
						//将时间精确到当天
						map1.put("set_data", getCurrentDay(set_data));//---------------------------
						map1.put("product_count", df.format(bd_product_count.doubleValue()));
						map1.put("total_price", df.format(bd_total_price.doubleValue()));
						list_query_data.add(map1);
						break;
					case 2:
						//车牌汇总，车牌同，合并数据（车牌，数量--，总价--）
						//查看车牌是否存在相同
						for(Map<String, String> map2:list_query_data){
							System.out.println("新----"+map2.get("car_code"));
							System.out.println("旧----"+car_code);
							if(map2.get("car_code").equals(car_code)){
								//数量合并
								BigDecimal bd_old_product_count = new BigDecimal(map2.get("product_count"));
								BigDecimal bd_add_product_count = bd_old_product_count.add(bd_product_count);
								map2.put("product_count", df.format(bd_add_product_count));
								//总价合并
								BigDecimal bd_old_total_price = new BigDecimal(map2.get("total_price"));
								BigDecimal bd_add_total_price = bd_old_total_price.add(bd_total_price);
								map2.put("total_price", df.format(bd_add_total_price.doubleValue()));
								continue outer;
							}
						}
						//没有就新建map集合进行数据的封装
						Map<String, String> map2 = new HashMap<String, String>();
						map2.put("car_code", car_code);
						map2.put("product_count", df.format(bd_product_count.doubleValue()));
						map2.put("total_price", df.format(bd_total_price.doubleValue()));
						list_query_data.add(map2);
						break;
					case 3:
						//品种+日期，品种和日期相同合并数据，其他因素忽略（品种，日期，数量--，总价--）
						//查询集合，品种和日期都相同，合并数据
						for(Map<String, String> map3:list_query_data){//-----------------------------------------
							if(map3.get("cement_name").equals(cement_name)&&map3.get("set_data").equals(getCurrentDay(set_data))){
								//数量合并
								BigDecimal bd_old_product_count = new BigDecimal(map3.get("product_count"));
								BigDecimal bd_add_product_count = bd_old_product_count.add(bd_product_count);
								map3.put("product_count", df.format(bd_add_product_count));
								//总价合并
								BigDecimal bd_old_total_price = new BigDecimal(map3.get("total_price"));
								BigDecimal bd_add_total_price = bd_old_total_price.add(bd_total_price);
								map3.put("total_price", df.format(bd_add_total_price.doubleValue()));
								continue outer;
							}
						}
						//否则新建map集合，封装数据
						Map<String, String> map3 = new HashMap<String, String>();
						map3.put("set_data", getCurrentDay(set_data));//------------------------
						map3.put("cement_name", cement_name);
						map3.put("product_count", df.format(bd_product_count.doubleValue()));
						map3.put("total_price", df.format(bd_total_price.doubleValue()));
						list_query_data.add(map3);
						break;
					case 4:
						//日期+车牌，。。。（日期，车牌，数量--，总价--）
						//日期和车牌都相同，合并数据
						for(Map<String, String> map4:list_query_data){//-------------------------------------
							if(map4.get("set_data").equals(getCurrentDay(set_data))&&map4.get("car_code").equals(car_code)){
								//数量合并
								BigDecimal bd_old_product_count = new BigDecimal(map4.get("product_count"));
								BigDecimal bd_add_product_count = bd_old_product_count.add(bd_product_count);
								map4.put("product_count", df.format(bd_add_product_count));
								//总价合并
								BigDecimal bd_old_total_price = new BigDecimal(map4.get("total_price"));
								BigDecimal bd_add_total_price = bd_old_total_price.add(bd_total_price);
								map4.put("total_price", df.format(bd_add_total_price.doubleValue()));
								continue outer;
							}
						}
						//不同，新建map集合封装数据
						Map<String, String> map4 = new HashMap<String, String>();
						map4.put("set_data", getCurrentDay(set_data));//---------------------------
						map4.put("car_code", car_code);
						map4.put("product_count", df.format(bd_product_count.doubleValue()));
						map4.put("total_price", df.format(bd_total_price.doubleValue()));
						list_query_data.add(map4);
						break;
					}
				}
			}

		//添加返回数据状态,状态码1表示成功，0表示失败
		//创建map集合封装状态属性
		Map<String, String> status = new HashMap<String, String>();
		if(list_query_data.size()>0){
			status.put("status_code", "1");
			status.put("query_type", query_type);
			status.put("status_reason", "获取数据成功");
		}else{
			status.put("status_code", "0");
			status.put("query_type", query_type);
			status.put("status_reason", "获取数据失败");
		}
		//新建queryDateBean对象
		QueryDateBean queryBean = new QueryDateBean();
		//对bean对象进行赋值
		queryBean.setStatus(status);
		queryBean.setResult(list_query_data);
		queryBean.setTotal(list_query_data.size()+"");
		//将bean对象转换成jsonobject对象
		JSONObject json = JSONObject.fromObject(queryBean);
		//将json数据返回给客户端
		out_print.print(json.toString());
		result.close();
	}
	/**
	 * 自定义方法：通过字符串类型的字符串转换成毫秒
	 * @param date：日期格式字符串
	 * @return long类型毫秒值
	 * @throws ParseException 
	 */
	private long getMillis(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(date));
		return calendar.getTimeInMillis();
	}

	private long getMillisFromService(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(date));
		return calendar.getTimeInMillis();
	}
	/**
	 * 自定义方法，将精确时间精确到当天
	 * @param time:服务器数据库提供的时间yy-MM-dd hh-mm-ss.SSS
	 * @return
	 * @throws ParseException 
	 */
	private String getCurrentDay(String time) throws ParseException{
		long times = getMillisFromService(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		date.setTime(times);
		return sdf.format(date);
	}
}


