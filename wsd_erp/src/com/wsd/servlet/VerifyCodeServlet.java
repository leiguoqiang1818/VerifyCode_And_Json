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
 * servlet:处理获取验证码的请求
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
		//设置响应头信息，设置编码格式
		response.setCharacterEncoding("utf-8");
		//设置浏览器不缓存
		response.setHeader("Cache-Control", "no-cache");
		//设置文本类型
		response.setContentType("text/html;charset=utf-8");
		//得到verifyCode对象
		VerifyCode verifyCode = new VerifyCode(75, 30, 20);
		//得到bufferImage对象
		BufferedImage image = verifyCode.getImage();
		//将验证码图片的文字信息保存到session域中，生命存在整个服务器生命周期中
		request.getSession().setAttribute("verify_code", verifyCode.getText());
		System.out.println((String)request.getSession().getAttribute("verify_code"));
		//将bufferImage对象写入reponse.getoutputstream中，响应给客户端个
		verifyCode.output(image, response.getOutputStream());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
