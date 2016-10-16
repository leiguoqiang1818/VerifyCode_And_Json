package com.wsd.javaBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个javaBean类，用于生成查询订单生成json数据
 * @author leiguoqiang
 * QQ 274764936
 */
public class QueryDateBean {
	/**
	 * json数据返回状态属性：status_code(状态码)，status_reason(原因)，query_type(查询类型)
	 */
	private Map<String, String> status = new HashMap<String, String>();
	/**
	 * json数据返回订单详细信息集合,map集合封装了订单信息的名称和值
	 */
	private List<Map<String,String>> result = new ArrayList<Map<String,String>>();
	/**
	 * json数据返回的订单总数
	 */
	private String total;
	
	public QueryDateBean() {
		super();
	}
	public Map<String, String> getStatus() {
		return status;
	}
	public void setStatus(Map<String, String> status) {
		this.status = status;
	}
	public List<Map<String,String>> getResult() {
		return result;
	}
	public void setResult(List<Map<String,String>> result) {
		this.result = result;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
}
