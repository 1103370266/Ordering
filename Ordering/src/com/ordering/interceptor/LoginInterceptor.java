package com.ordering.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//������
public class LoginInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {	
		//��ȡ�����url
		String url=request.getRequestURI();
		
		if(url.indexOf("carAndOrder")!=-1){
			//������й�����߹��ﳵ������
			//�ж�session
			HttpSession session=request.getSession();
			//��session��ȡ���û������Ϣ��
			String username=(String) session.getAttribute("username");
			if(username!=null)
			{	//��ݴ��ڣ�����
				return true;
			}else{
				//ִ�е������ʾ�û������Ҫ��֤����ת����½����
				request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);	
				return false;
			}
		}
		return true;		
	}
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3) throws Exception {
	}
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
	}
}
