package com.wsd.javaBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * һ��javaBean�࣬�������ɲ�ѯ��������json����
 * @author leiguoqiang
 * QQ 274764936
 */
public class QueryDateBean {
	/**
	 * json���ݷ���״̬���ԣ�status_code(״̬��)��status_reason(ԭ��)��query_type(��ѯ����)
	 */
	private Map<String, String> status = new HashMap<String, String>();
	/**
	 * json���ݷ��ض�����ϸ��Ϣ����,map���Ϸ�װ�˶�����Ϣ�����ƺ�ֵ
	 */
	private List<Map<String,String>> result = new ArrayList<Map<String,String>>();
	/**
	 * json���ݷ��صĶ�������
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
