package com.ordering.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ordering.po.Shore;
import com.ordering.po.Shoretype;
import com.ordering.po.User;
import com.ordering.service.ShoreService;
import com.ordering.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ShoreService shoreService;
	
	//��ע��ҳ��
	@RequestMapping("/toregister")
	public String toregister(){
		return "/register";
	}
	
	//�ύע��
	@RequestMapping("/register")
	public String register(HttpServletRequest request,User user,String password1){
		//���ע����Ϣ�Ƿ�����
		String error="";
		if("".equals(user.getAddress()) || "".equals(user.getMobile()) || "".equals(user.getPassword()) || "".equals(user.getRealname()) || "".equals(user.getUsername()))
		{	
			error="�����Ƹ�����Ϣ";
			request.setAttribute("error", error);
			return "/register";
		}
		else{
			
			if(password1.equals(user.getPassword())){
				//����û����Ƿ���ڣ�
				if(userService.check(user.getUsername()))
				{
					error="�û����Ѵ��ڣ�������ע��";
					request.setAttribute("error", error);
					return "/register";
				}else{
					userService.insert(user);
					return "/login";
					
				}
			}else{
				//���벻һ�£�
				error="�������벻һ��,������ע��";
				request.setAttribute("error", error);
				return "/register";
			}

		}
	}
	
	
	//�û���½��
	@RequestMapping("/tologin")
	public String tologin(){
		return "/login";
	}
	
	//��½�ύ��
	@RequestMapping("/login")
	public String login(HttpServletRequest request,User user){
		User loginUser=userService.selectByExample(user);
		if(loginUser!=null){
			String username=loginUser.getUsername();
			request.getSession().setAttribute("user",loginUser);
			request.getSession().setAttribute("USER",loginUser);
			request.getSession().setAttribute("username",username);

			
			//��ѯ���е��̼����
			List<Shoretype> shoreTypeList=shoreService.selectAllType();
			//��ѯ���е��̼ң�
			List<Shore> shoreList=shoreService.selectAllShore();
			request.setAttribute("shoreTypeList", shoreTypeList);
			request.setAttribute("shoreList", shoreList);
			
			//�����߼���ͼ����
			return "forward:/index.jsp"; 
		}else{
			String error="�˺��������";
			request.setAttribute("error",error);
			return "/login";
		}
	}
	
	//�˳���½
	@RequestMapping("/logout")
	public String logout(HttpSession sesison) throws Exception{
		sesison.invalidate();
		return "forward:/index.jsp";	
		
	}
	
	//��������ҳ��
	@RequestMapping("/toOrderingView")
	public String toOrderingView(){
		//��ѯ���е��̼����
		
		//��Ѷ���е��̼ң�
		
		
		//��������ҳ��
		return "/register";
	}
}
