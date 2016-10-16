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
 * servlet:���ڴ����ѯ������ϸ��Ϣʹ�ã��Ż�json����
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
	 * ���ݽ�ϣ���װ��ѯ�������
	 */
	private List<Map<String, String>> list_query_data = new ArrayList<Map<String,String>>();
	/**
	 * ��������ӡ���������
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
		//������Ӧ��
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json;charset=UTF-8");
		response.setHeader("Cache-control", "no-cache");
		out_print = response.getWriter();
		String sql = "select * from QuJinKunGang..XS_Order_ABase";
		// �������ݿ�,���Ҵ����������
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
		//������Ӧ��
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json;charset=UTF-8");
		response.setHeader("Cache-control", "no-cache");
		out_print = response.getWriter();
		String sql = "select * from QuJinKunGang..XS_Order_ABase";
		// �������ݿ�,���Ҵ����������
		try {
			handleSqlserver(sql,request);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		out_print.flush();
		out_print.close();
	}

	/**
	 * �Զ��巽�����������ݿ����棬���Ҵ����������
	 * @param out:��ӡ���������
	 * @throws ParseException 
	 */
	private void handleSqlserver(String sql,HttpServletRequest request) throws ParseException{
		Connection conn = null;
		Statement statement = null;
		try {
			//�������ݿ�����
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			//��ȡ���ݿ����Ӷ���
			String url = "jdbc:sqlserver://localhost:1433;DatebaseName=QuJinKunGang";
			String userName = "sa";
			String passWord = "654321";
			//��ȡ�������ݻỰ����
			conn = DriverManager.getConnection(url, userName, passWord);
			//��ȡ�������ݿ�Ự����
			statement = conn.createStatement();
			//�������ݴ���
			handleDate(statement,sql,request);
			//�ر�������ͻỰ����
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
	 * �Զ��巽���������ݿ���в�ѯ����
	 * @param out����ӡ���������
	 * @param statement���������ݻỰ����
	 * @throws SQLException 
	 * @throws UnsupportedEncodingException 
	 * @throws ParseException 
	 */
	private void handleDate(Statement statement,String sql,HttpServletRequest request) throws SQLException, UnsupportedEncodingException, ParseException{
		//�������������
		list_query_data.clear();
		//�����������ȡ�������
		request.setCharacterEncoding("utf-8");
		//��ʽ2016-8-15
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		//��ʽ1��2��3��4��5
		//0 ��Ʒ�ֻ��ܣ�1 �����ڻ��ܣ�2 �����ƻ��ܣ�3 ��Ʒ�� �����ڣ�4 �����ڣ����ƻ���
		String query_type = request.getParameter("query_type");
		//0:���µ����ȴ���ˣ�1 ����ˡ������2 �Ѹ��3 �ѽ�����4 ����װ���� 5 װ����ɣ� 9 �ѳ��� 
		String order_status = request.getParameter("order_status");

		//��ʼʱ��
		long startTime = getMillis(start_time);
		//����ʱ�����1�죬�ѵ��������Ҳ���س���
		long endTime = getMillis(end_time)+24*60*60*1000;
		//��ѯ����
		int queryType = Integer.parseInt(query_type);
		//��Ӷ���״̬����
		int orderStatus = Integer.parseInt(order_status);

		//��ȡ���ݿ����ӻỰ����
		ResultSet result = statement.executeQuery(sql);
		//��ʽת��
		DecimalFormat df = new DecimalFormat("#0.00");
		//ѭ��������������
		outer:
			while(result.next()){
				//��������  yy-MM-dd hh-mm-ss.SSS
				String set_data = result.getString("XSO_SetDate");
				
				//ת����  yy-MM-dd   getCurrentDay(set_data)
				//ת���ɺ���ֵ
				long data = getMillis(getCurrentDay(set_data));//------------------------------------
				System.out.println("ת��ʱ��"+getCurrentDay(set_data));
				System.out.println("���ʱ�����ֵ"+data);
				//��������״̬
				//0:���µ����ȴ���ˣ�1 ����ˡ������2 �Ѹ��3 �ѽ�����4 ����װ���� 5 װ����ɣ� 9 �ѳ��� 
				int order_statu = result.getInt("XSO_Status");
				//�����ж�ʱ�䣬����ʱ�䷶Χ�ھ�����ѭ��
				//1.��ʱ�䷶Χ�� 2.����״̬û��ѡ����붩��״̬ƥ�����������״̬Ϊ-1ʱ����ζ��ѯ���ж���״̬����
				if(((startTime<=data)&&(data<endTime)&&(orderStatus==order_statu))||((startTime<=data)&&(data<=endTime)&&(orderStatus==-1))){
					//Ʒ������
					String cement_name = result.getString("XSO_CementName");
					if((cement_name==null)||(cement_name.equals(""))){
						cement_name = "null";
					}

					//����������Ʒ�ܼ�
					//				String total_price = result.getString("XSO_TotalPrice");
					BigDecimal bd_total_price = result.getBigDecimal("XSO_TotalPrice");
					if(bd_total_price==null){
						bd_total_price = new BigDecimal(0);
					}
					//������ת����long����
					//				long total_price_long = bd_total_price.longValue();


					//��Ʒ����
					BigDecimal bd_product_count = result.getBigDecimal("XSO_Number");
					if((bd_product_count==null)){
						bd_product_count = new BigDecimal(0);
					}
					//				//��������ת��,ת����int����
					//				long product_count_long = Long.parseLong(product_count);


					//��������
					String car_code = result.getString("XSO_CarCode");
					if((car_code==null)||(car_code.equals(""))){
						car_code = "null";
					}
					//��������ɸѡ
					//0 ��Ʒ�ֻ��ܣ�1 �����ڻ��ܣ�2 �����ƻ��ܣ�3 ��Ʒ�� �����ڣ�4 �����ڣ����ƻ���
					switch (queryType) {
					case 0:
						//Ʒ�ֻ��ܣ�Ʒ��ͬ���ϲ����ݣ����ƣ�����--���ܼ�--��
						//ѭ�������鿴�������Ƿ�����ͬ��Ʒ���������ݣ��кϲ������½�����
						for(Map<String, String> map:list_query_data){
							//�ҵ�������ͬ
							if((map.get("cement_name")).equals(cement_name)){
								//�����ϲ�
								BigDecimal bd_old_product_count = new BigDecimal(map.get("product_count"));
								BigDecimal bd_add_product_count = bd_old_product_count.add(bd_product_count);
								map.put("product_count", df.format(bd_add_product_count));
								//�ܼۺϲ�
								BigDecimal bd_old_total_price = new BigDecimal(map.get("total_price"));
								BigDecimal bd_add_total_price = bd_old_total_price.add(bd_total_price);
								map.put("total_price", df.format(bd_add_total_price.doubleValue()));
								continue outer;
							}
						}
						//û���ҵ�ƥ�����Ƶ����ݣ��½�map���ϣ���װ������
						Map<String, String> map = new HashMap<String, String>();
						map.put("cement_name", cement_name);
						map.put("product_count", df.format(bd_product_count.doubleValue()));
						map.put("total_price", df.format(bd_total_price.doubleValue()));
						list_query_data.add(map);
						break;
					case 1:
						//���ڻ��ܣ�����ͬ���ϲ����ݣ����ڣ�����--���ܼ�--��
						//�鿴�������Ƿ�����ͬ������
						for(Map<String, String> map1:list_query_data){
							if(map1.get("set_data").equals(getCurrentDay(set_data))){//----------------------
								//�����ϲ�
								BigDecimal bd_old_product_count = new BigDecimal(map1.get("product_count"));
								BigDecimal bd_add_product_count = bd_old_product_count.add(bd_product_count);
								map1.put("product_count", df.format(bd_add_product_count));
								//�ܼۺϲ�
								BigDecimal bd_old_total_price = new BigDecimal(map1.get("total_price"));
								BigDecimal bd_add_total_price = bd_old_total_price.add(bd_total_price);
								map1.put("total_price", df.format(bd_add_total_price.doubleValue()));
								continue outer;
							}
						}
						//û����ͬ���ڣ����½�map���Ϸ�װ����
						Map<String, String> map1 = new HashMap<String, String>();
						//��ʱ�侫ȷ������
						map1.put("set_data", getCurrentDay(set_data));//---------------------------
						map1.put("product_count", df.format(bd_product_count.doubleValue()));
						map1.put("total_price", df.format(bd_total_price.doubleValue()));
						list_query_data.add(map1);
						break;
					case 2:
						//���ƻ��ܣ�����ͬ���ϲ����ݣ����ƣ�����--���ܼ�--��
						//�鿴�����Ƿ������ͬ
						for(Map<String, String> map2:list_query_data){
							System.out.println("��----"+map2.get("car_code"));
							System.out.println("��----"+car_code);
							if(map2.get("car_code").equals(car_code)){
								//�����ϲ�
								BigDecimal bd_old_product_count = new BigDecimal(map2.get("product_count"));
								BigDecimal bd_add_product_count = bd_old_product_count.add(bd_product_count);
								map2.put("product_count", df.format(bd_add_product_count));
								//�ܼۺϲ�
								BigDecimal bd_old_total_price = new BigDecimal(map2.get("total_price"));
								BigDecimal bd_add_total_price = bd_old_total_price.add(bd_total_price);
								map2.put("total_price", df.format(bd_add_total_price.doubleValue()));
								continue outer;
							}
						}
						//û�о��½�map���Ͻ������ݵķ�װ
						Map<String, String> map2 = new HashMap<String, String>();
						map2.put("car_code", car_code);
						map2.put("product_count", df.format(bd_product_count.doubleValue()));
						map2.put("total_price", df.format(bd_total_price.doubleValue()));
						list_query_data.add(map2);
						break;
					case 3:
						//Ʒ��+���ڣ�Ʒ�ֺ�������ͬ�ϲ����ݣ��������غ��ԣ�Ʒ�֣����ڣ�����--���ܼ�--��
						//��ѯ���ϣ�Ʒ�ֺ����ڶ���ͬ���ϲ�����
						for(Map<String, String> map3:list_query_data){//-----------------------------------------
							if(map3.get("cement_name").equals(cement_name)&&map3.get("set_data").equals(getCurrentDay(set_data))){
								//�����ϲ�
								BigDecimal bd_old_product_count = new BigDecimal(map3.get("product_count"));
								BigDecimal bd_add_product_count = bd_old_product_count.add(bd_product_count);
								map3.put("product_count", df.format(bd_add_product_count));
								//�ܼۺϲ�
								BigDecimal bd_old_total_price = new BigDecimal(map3.get("total_price"));
								BigDecimal bd_add_total_price = bd_old_total_price.add(bd_total_price);
								map3.put("total_price", df.format(bd_add_total_price.doubleValue()));
								continue outer;
							}
						}
						//�����½�map���ϣ���װ����
						Map<String, String> map3 = new HashMap<String, String>();
						map3.put("set_data", getCurrentDay(set_data));//------------------------
						map3.put("cement_name", cement_name);
						map3.put("product_count", df.format(bd_product_count.doubleValue()));
						map3.put("total_price", df.format(bd_total_price.doubleValue()));
						list_query_data.add(map3);
						break;
					case 4:
						//����+���ƣ������������ڣ����ƣ�����--���ܼ�--��
						//���ںͳ��ƶ���ͬ���ϲ�����
						for(Map<String, String> map4:list_query_data){//-------------------------------------
							if(map4.get("set_data").equals(getCurrentDay(set_data))&&map4.get("car_code").equals(car_code)){
								//�����ϲ�
								BigDecimal bd_old_product_count = new BigDecimal(map4.get("product_count"));
								BigDecimal bd_add_product_count = bd_old_product_count.add(bd_product_count);
								map4.put("product_count", df.format(bd_add_product_count));
								//�ܼۺϲ�
								BigDecimal bd_old_total_price = new BigDecimal(map4.get("total_price"));
								BigDecimal bd_add_total_price = bd_old_total_price.add(bd_total_price);
								map4.put("total_price", df.format(bd_add_total_price.doubleValue()));
								continue outer;
							}
						}
						//��ͬ���½�map���Ϸ�װ����
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

		//��ӷ�������״̬,״̬��1��ʾ�ɹ���0��ʾʧ��
		//����map���Ϸ�װ״̬����
		Map<String, String> status = new HashMap<String, String>();
		if(list_query_data.size()>0){
			status.put("status_code", "1");
			status.put("query_type", query_type);
			status.put("status_reason", "��ȡ���ݳɹ�");
		}else{
			status.put("status_code", "0");
			status.put("query_type", query_type);
			status.put("status_reason", "��ȡ����ʧ��");
		}
		//�½�queryDateBean����
		QueryDateBean queryBean = new QueryDateBean();
		//��bean������и�ֵ
		queryBean.setStatus(status);
		queryBean.setResult(list_query_data);
		queryBean.setTotal(list_query_data.size()+"");
		//��bean����ת����jsonobject����
		JSONObject json = JSONObject.fromObject(queryBean);
		//��json���ݷ��ظ��ͻ���
		out_print.print(json.toString());
		result.close();
	}
	/**
	 * �Զ��巽����ͨ���ַ������͵��ַ���ת���ɺ���
	 * @param date�����ڸ�ʽ�ַ���
	 * @return long���ͺ���ֵ
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
	 * �Զ��巽��������ȷʱ�侫ȷ������
	 * @param time:���������ݿ��ṩ��ʱ��yy-MM-dd hh-mm-ss.SSS
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


