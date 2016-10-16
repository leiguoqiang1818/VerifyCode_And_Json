package com.wsd.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wsd.image.VerifyCode;

/**
 * @author leiguoqiang
 * QQ 274764936
 * servlet:�����ȡ��֤�������
 * Servlet implementation class VerifyCodeServlet
 */
@WebServlet("/VerifyCodeServlet")
public class VerifyCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyCodeServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("static-access")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//������Ӧͷ��Ϣ�����ñ����ʽ
		response.setCharacterEncoding("utf-8");
		//���������������
		response.setHeader("Cache-Control", "no-cache");
		//�����ı�����
		response.setContentType("text/html;charset=utf-8");
		//�õ�verifyCode����
		VerifyCode verifyCode = new VerifyCode(75, 30, 20);
		//�õ�bufferImage����
		BufferedImage image = verifyCode.getImage();
		//����֤��ͼƬ��������Ϣ���浽session���У�����������������������������
		request.getSession().setAttribute("verify_code", verifyCode.getText());
		System.out.println((String)request.getSession().getAttribute("verify_code"));
		//��bufferImage����д��reponse.getoutputstream�У���Ӧ���ͻ��˸�
		verifyCode.output(image, response.getOutputStream());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
