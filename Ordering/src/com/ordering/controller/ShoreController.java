package com.ordering.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.ordering.mapper.MenuMapper;
import com.ordering.po.Menu;
import com.ordering.po.OrdersVo;
import com.ordering.po.Shore;
import com.ordering.po.ShoreCustom;
import com.ordering.po.Shoretype;
import com.ordering.po.Type;
import com.ordering.po.User;
import com.ordering.service.MenuTypeService;
import com.ordering.service.OrderingService;
import com.ordering.service.ShoreService;
import com.ordering.util.UploadUtil;

@Controller
@RequestMapping("/shore")
public class ShoreController {
	//�̼�Controller
	
	@Autowired
	private ShoreService shoreService;
	@Autowired
	private MenuTypeService menuTypeService;
	@Autowired
	private  OrderingService orderingService;

	
	//���̼�ע��ҳ�棺
	@RequestMapping("/toshoreRegister")
	public String toshoreRegister(HttpServletRequest request){
		List<Shoretype> shoreTypeList=shoreService.selectAllType();
		
		request.setAttribute("shoreTypeList", shoreTypeList);
		return "/shoreRegisterView";
	}
	
	//�̼�ע���ύ��
	@RequestMapping("/shoreRegister")
	public String shoreRegister(HttpServletRequest request,Shore shore,String password1){
		//���ע����Ϣ�Ƿ�����
		String error="";
		if("".equals(shore.getAddress()) || "".equals(shore.getPassword()) || "".equals(shore.getShorename()) || "".equals(shore.getUsername()))
		{	
			error="�������̼���Ϣ";
			request.setAttribute("error", error);
			return this.toshoreRegister(request);
		}else{
			if(password1.equals(shore.getPassword()))
			{	//����һ��
				if(shoreService.check(shore.getUsername()))
				{
					//����û����Ƿ���ڣ�
					error="�õ�½�˺��Ѵ��ڣ�������ע��";
					request.setAttribute("error", error);
					return this.toshoreRegister(request);
				}else{
					shoreService.insert(shore);
					return "/ShoreLogin";
				}
			}else{
				//���벻һ�£�
				error="���벻һ�£���������д";
				request.setAttribute("error", error);
				return this.toshoreRegister(request);
			}
		}
	}
	
	//���̼ҵ�½ҳ�棺
	@RequestMapping("/toShoreLogin")
	public String toShoreLogin(){
		return "/ShoreLogin";
	}
	
	//�̼ҵ�½�ύ��
	@RequestMapping("/login")
	public String login(HttpServletRequest request,Shore shore){
		//��ѯ�û���
		ShoreCustom loginShore=shoreService.selectShore(shore);
		if(loginShore!=null){
			request.getSession().setAttribute("MyShore",loginShore);
			//��½�ɹ������̼Ҹ���ҳ�棺
			return "/ShoreMainView";
		}else{
			String error="�˺��������";
			request.setAttribute("error",error);
			return "/ShoreLogin";
		}
	}
	
	//�����̼���ҳ�棺
	@RequestMapping("/backMain")
	public String backMain(){
		return "/ShoreMainView";
	}
	
	//���޸��̼ҽ��棺
	@RequestMapping("/toUpdateShore")
	public String toUpdateShore(HttpServletRequest request){
		List<Shoretype> shoreTypeList=shoreService.selectAllType();
		
		request.setAttribute("shoreTypeList", shoreTypeList);
		return "/UpdateShore";
	}
	
	//�޸��ύ��
	@RequestMapping("/UpdateShore")
	public String UpdateShore(HttpServletRequest request,Shore shore){
		//�޸�
		shoreService.updateShore(shore);
		//���½��û�����session��
		ShoreCustom loginShore=shoreService.selectShoreById(shore);
		request.getSession().setAttribute("MyShore",loginShore);
		//�����̼���ҳ��
		return "/ShoreMainView";
	}
	
	//���޸�ͷ��ҳ�棺
	@RequestMapping("/updShoreHead")
	public String updShoreHead(HttpServletRequest request){
		return "/updHeadView";
	}
	
	//�޸�ͷ���ύ��
	@RequestMapping("/updShoreImg")
	public String updShoreImg(HttpServletRequest request,Shore shore,MultipartFile ShoreImg) throws IllegalStateException, IOException{
		//ԭʼ����
		String originalFilename=ShoreImg.getOriginalFilename();
		if(ShoreImg!=null && originalFilename!=null && originalFilename.length()>0)
		{
			//�洢ͼƬ������·��
			String img_path="D:\\Ordering\\"+shore.getShoreid()+"\\headImg\\";
			String newFileName=UUID.randomUUID()+originalFilename.substring(originalFilename.lastIndexOf("."));
			//��ͼƬ��
			File newFile=new File(img_path+newFileName);
			File file=new File(img_path);
			//Ŀ¼���ڣ����Ŀ¼��ͼƬ
			if(file.exists())
			{	
				UploadUtil uploadUtil=new UploadUtil();
				uploadUtil.delDir(file);
			}
			//���Ŀ¼�����ڣ��򴴽�Ŀ¼
			if(!newFile.exists())
			{
				newFile.mkdirs();
			}
			
			//���ڴ��е�����д�����
			ShoreImg.transferTo(newFile);
			
			//����ͼƬ����д��shore��
			String url=shore.getShoreid()+"\\headImg\\"+newFileName;
			shore.setShorepic(url);
			
			shoreService.updateShoreImg(shore);
			//���½��û�����session��
			ShoreCustom loginShore=shoreService.selectShoreById(shore);
			request.getSession().setAttribute("MyShore",loginShore);
		}
		return "/updHeadView";
	}
	
	//����ʽ����ҳ�棺
	@RequestMapping("/toMeunType/{shoreid}")
	public ModelAndView toMeunType(@PathVariable("shoreid")int shoreid){
		
		//��ѯ�����̼����в�ʽ�ķ���
		List<Type> typeList= menuTypeService.findAllType(shoreid);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("typeList", typeList);
		modelAndView.setViewName("/MenuType");;
		return modelAndView;
	}
	
	//�������ύ��
	@RequestMapping("/addType")
	public ModelAndView addType(Type type){
		menuTypeService.addType(type);
		
		List<Type> typeList= menuTypeService.findAllType(type.getShoreId());
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("typeList", typeList);
		modelAndView.setViewName("/MenuType");;
		return modelAndView;
	}
	
	//ɾ�����
	@RequestMapping("/delType")
	public ModelAndView delType(int typeid,int shoreid){
		
		menuTypeService.deleteByPrimaryKey(typeid);
		
		List<Type> typeList= menuTypeService.findAllType(shoreid);
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("typeList", typeList);
		modelAndView.setViewName("/MenuType");;
		return modelAndView;
	}
	
	//�޸�����ύ��
	@RequestMapping("/updMenuType")
	public ModelAndView updMenuType(Type type){
		menuTypeService.updateByPrimaryKeySelective(type);
		
		List<Type> typeList= menuTypeService.findAllType(type.getShoreId());
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("typeList", typeList);
		modelAndView.setViewName("/MenuType");;
		return modelAndView;
	}
	
	//����Ӳ˵�ҳ�棺
	@RequestMapping("/toAddMenu/{shoreid}")
	public ModelAndView toAddMenu(@PathVariable("shoreid")int shoreid){
		//��ѯ���еĲ�ʽ���
		List<Type> typeList= menuTypeService.findAllType(shoreid);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("typeList", typeList);
		modelAndView.setViewName("/addMenu");;
		return modelAndView;
	}
	
	//��Ӳ˵��ύ��
	@RequestMapping("/addMenu")
	public String addMenu(Menu menu,MultipartFile menuImg,HttpServletRequest request) throws IllegalStateException, IOException{
		shoreService.insertSelective(menu);
		
		//����˵�ͼƬ��
		//ԭʼ����
		String originalFilename=menuImg.getOriginalFilename();
		if(menuImg!=null && originalFilename!=null && originalFilename.length()>0)
		{
			//�洢ͼƬ������·��
			String img_path="D:\\Ordering\\"+menu.getShoreId()+"\\menuImg\\"+menu.getMenuid()+"\\";
			String newFileName=UUID.randomUUID()+originalFilename.substring(originalFilename.lastIndexOf("."));
			//��ͼƬ��
			File newFile=new File(img_path+newFileName);
			//���Ŀ¼�����ڣ��򴴽�Ŀ¼
			if(!newFile.exists())
			{
				newFile.mkdirs();
			}
			
			//���ڴ��е�����д�����
			menuImg.transferTo(newFile);
			
			//����ͼƬ����д��shore��
			String url=menu.getShoreId()+"\\menuImg\\"+menu.getMenuid()+"\\"+newFileName;
			
			menu.setMenupic(url);
			//����ͼƬ��
			shoreService.updateByPrimaryKeySelective(menu);
		}
		
		//��ѯ�û����еĲ�ʽ��
		List<Menu> menuList=shoreService.selectByExample(menu);
		//��ѯ���еĲ�ʽ���
		List<Type> typeList= menuTypeService.findAllType(menu.getShoreId());
		request.setAttribute("menuList", menuList);
		request.setAttribute("typeList", typeList);
		//�ص�������
		return "/MainMenu";
	}
	
	//���˵���ҳ�棺
	@RequestMapping("/toMainMenu/{shoreid}")
	public ModelAndView toMainMenu(@PathVariable("shoreid")int shoreid){
		//��ѯ���еĲ�ʽ���
		List<Type> typeList= menuTypeService.findAllType(shoreid);
		//��ѯ�̼����еĲ�ʽ��
		List<Menu> menuList=shoreService.selectAllMenu(shoreid);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("typeList", typeList);
		modelAndView.addObject("menuList", menuList);
		modelAndView.setViewName("/MainMenu");;
		return modelAndView;
	}
	
	//����������ѯ��ʽ��
	@RequestMapping("/findMenuByType")
	public ModelAndView findMenuByType(int typeid,int shoreid){
		//��ѯ���еĲ�ʽ���
		List<Type> typeList= menuTypeService.findAllType(shoreid);
		//��������ѯ���еĲ�ʽ��
		List<Menu> menuList=shoreService.findMenuByType(typeid);
		Type type=menuTypeService.selectByPrimaryKey(typeid);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("typeList", typeList);
		modelAndView.addObject("menuList", menuList);
		modelAndView.addObject("type", type);
		modelAndView.setViewName("/MainMenu");;
		return modelAndView;
	}
	
	//�����²�ʽҳ�棺
	@RequestMapping("/toUpdMenu/{menuid}")
	public ModelAndView toUpdMenu(@PathVariable("menuid")int menuid){
		
		//����Id��ѯ��ʽ
		Menu menu=shoreService.selectByPrimaryKey(menuid);
		//��ѯ���еĲ�ʽ���
		List<Type> typeList= menuTypeService.findAllType(menu.getShoreId());

		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("typeList", typeList);
		modelAndView.setViewName("/UpdMenu");
		return modelAndView;
	}
	
	//�޸Ĳ˵��ύ��
	@RequestMapping("/UpdMenu")
	public String UpdMenu(Menu menu,MultipartFile menuImg,HttpServletRequest request) throws IllegalStateException, IOException{
		//���²˵���
		shoreService.updateByPrimaryKeySelective(menu);
		
		//����˵�ͼƬ��
		//ԭʼ����
		String originalFilename=menuImg.getOriginalFilename();
		if(menuImg!=null && originalFilename!=null && originalFilename.length()>0)
		{
			//�洢ͼƬ������·��
			String img_path="D:\\Ordering\\"+menu.getShoreId()+"\\menuImg\\"+menu.getMenuid()+"\\";
			String newFileName=UUID.randomUUID()+originalFilename.substring(originalFilename.lastIndexOf("."));
			//��ͼƬ��
			File newFile=new File(img_path+newFileName);
			File file=new File(img_path);
			//Ŀ¼���ڣ����Ŀ¼��ͼƬ
			if(file.exists())
			{	
				UploadUtil uploadUtil=new UploadUtil();
				uploadUtil.delDir(file);
			}
			//���Ŀ¼�����ڣ��򴴽�Ŀ¼
			if(!newFile.exists())
			{
				newFile.mkdirs();
			}
			
			//���ڴ��е�����д�����
			menuImg.transferTo(newFile);
			
			//����ͼƬ����д��shore��
			String url=menu.getShoreId()+"\\menuImg\\"+menu.getMenuid()+"\\"+newFileName;
			
			menu.setMenupic(url);
			//����ͼƬ��
			shoreService.updateByPrimaryKeySelective(menu);
		}
		
		//��ѯ���еĲ�ʽ���
		List<Type> typeList= menuTypeService.findAllType(menu.getShoreId());
		//��ѯ�̼����еĲ�ʽ��
		List<Menu> menuList=shoreService.selectAllMenu(menu.getShoreId());
		
		request.setAttribute("menuList", menuList);
		request.setAttribute("typeList", typeList);
		//�ص�������
		return "/MainMenu";
	}
	
	//ɾ����ʽ��
	@RequestMapping("/delMenu")
	public ModelAndView delMenu(Menu menu){
		//ɾ���ò�ʽ
		shoreService.deleteByPrimaryKey(menu.getMenuid());
		
		List<Type> typeList= menuTypeService.findAllType(menu.getShoreId());
		List<Menu> menuList=shoreService.selectAllMenu(menu.getShoreId());
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("typeList", typeList);
		modelAndView.addObject("menuList", menuList);
		modelAndView.setViewName("/MainMenu");;
		return modelAndView;
	}
	
	//�˳���½
	@RequestMapping("/logout")
	public String logout(HttpSession sesison) throws Exception{
		sesison.invalidate();
		return "forward:/index.jsp";	
	}
	
	
	//�̼Ҳ鿴����
	@RequestMapping("/findDiscuss/{shoreid}")
	public ModelAndView findDiscuss(@PathVariable("shoreid")int shoreid){
		
		System.out.println("���");
		//�����̵�Id�鿴����
		List<OrdersVo> orderList= orderingService.findOrderByShoreId(shoreid);
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("orderList", orderList);
		
		//���ۿ�����ҳ��
		modelAndView.setViewName("/shoreDiscussView");
		return modelAndView;
	}
	
	//�û��鿴����
	@RequestMapping("/findDiscuss2/{shoreid}")
	public ModelAndView findDiscuss2(@PathVariable("shoreid")int shoreid){
		
		//�����̵�Id�鿴����
		List<OrdersVo> orderList= orderingService.findOrderByShoreId(shoreid);
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("orderList", orderList);
		
		//���û��ۿ�����ҳ��
		modelAndView.setViewName("/shoreDiscussView2");
		return modelAndView;
	}
	
	
	//�̼Ҳ鿴������
	@RequestMapping("/showOrders/{shoreid}")
	public String showOrders(@PathVariable("shoreid")int shoreid,HttpServletRequest request){
		
		List<OrdersVo> orderList= orderingService.findOrderByShoreId(shoreid);
		
		request.setAttribute("orderList", orderList);
		
		return "/shoreOrdersView";
	}
	
	//�̼Ҳ鿴�������飺
	@RequestMapping("/showOrderdetail/{orderId}")
	public String showOrderdetail(@PathVariable("orderId")int orderId,HttpServletRequest request){
		
		OrdersVo orders=orderingService.findOrdertailByOrderid(orderId);
		
		request.setAttribute("orders", orders);
		
		return "showOrderdetailView";
		
	}
	//�̼Ҳ鿴�������飺
	@RequestMapping("/showMyOrderdetail/{orderId}")
	public String showMyOrderdetail(@PathVariable("orderId")int orderId,HttpServletRequest request){
		
		OrdersVo orders=orderingService.findOrdertailByOrderid(orderId);
		
		request.setAttribute("orders", orders);
		
		return "MyOrderdetailView";
		
	}
	
	
}
