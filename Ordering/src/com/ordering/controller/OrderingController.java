package com.ordering.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ordering.po.Menu;
import com.ordering.po.MenuCustom;
import com.ordering.po.Shore;
import com.ordering.po.ShoreCustom;
import com.ordering.po.Shoretype;
import com.ordering.po.Type;
import com.ordering.service.MenuTypeService;
import com.ordering.service.OrderingService;
import com.ordering.service.ShoreService;

@Controller
@RequestMapping("/order")
public class OrderingController {
	
	@Autowired
	private ShoreService shoreService;
	@Autowired
	private MenuTypeService menuTypeService;

	
	//�鿴���е��̵�
	@RequestMapping("/toOrderingView")
	public String toOrderingView(HttpServletRequest request){
		//��ѯ���е��̼����
		List<Shoretype> shoreTypeList=shoreService.selectAllType();
		//��ѯ���е��̼ң�
		List<Shore> shoreList=shoreService.selectAllShore();
		request.setAttribute("shoreTypeList", shoreTypeList);
		request.setAttribute("shoreList", shoreList);
		
		//��������ҳ��
		return "forward:/index.jsp";
	}
	
	//�������id�鿴���е��̵�
	@RequestMapping(value="/findShoreByType/{shoretypeid}",method=RequestMethod.GET)
	public String findShoreByType(HttpServletRequest request,@PathVariable("shoretypeid")int shoretypeid){
		//��ѯ���е��̼����
		List<Shoretype> shoreTypeList=shoreService.selectAllType();
		//�������id��ѯ�̼ң�
		List<Shore> shoreList=shoreService.findShoreByType(shoretypeid);
		
		request.setAttribute("shoreTypeList", shoreTypeList);
		request.setAttribute("shoreList", shoreList);
		
		//��������ҳ��
		return "forward:/index.jsp";
	}
	
	//�����̼ң��ۿ����̼����еĲ�ʽ��
	@RequestMapping("/toShore/{shoreid}")
	public String toShore(@PathVariable("shoreid")int shoreid,HttpServletRequest request){
		//��ѯ���̵꣺
		ShoreCustom shoreCustom=shoreService.selectShoreByPrimaryKey(shoreid);
		//��ѯ���еĲ�ʽ���
		List<Type> typeList= menuTypeService.findAllType(shoreid);
		//��ѯ�̼����еĲ�ʽ��
		List<Menu> menuList=shoreService.selectAllMenu(shoreid);
		
		request.getSession().setAttribute("shore", shoreCustom);
		request.setAttribute("typeList", typeList);
		request.setAttribute("menuList", menuList);
		return "/buyMenu";
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
		modelAndView.setViewName("/buyMenu");
		return modelAndView;
	}
	
	
	//����ʽ����ҳ�棺
	@RequestMapping("/toSeeMenu/{menuid}")
	public ModelAndView toSeeMenu(@PathVariable("menuid")int menuid){
		
		//����Id��ѯ��ʽ,����ѯ
		MenuCustom menu=shoreService.selectMenuById(menuid);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("menu", menu);
		modelAndView.setViewName("/SeeMenu");
		return modelAndView;
	}
	
}
