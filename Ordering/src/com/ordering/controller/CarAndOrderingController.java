package com.ordering.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ordering.po.Car;
import com.ordering.po.CarVo;
import com.ordering.po.Menu;
import com.ordering.po.Orderdetail;
import com.ordering.po.Orders;
import com.ordering.po.OrdersVo;
import com.ordering.po.Shore;
import com.ordering.po.ShoreCustom;
import com.ordering.po.Shoretype;
import com.ordering.po.Type;
import com.ordering.service.MenuTypeService;
import com.ordering.service.OrderingService;
import com.ordering.service.ShoreService;

@Controller
@RequestMapping("/carAndOrder")
public class CarAndOrderingController {
	
	@Autowired
	private ShoreService shoreService;
	@Autowired
	private MenuTypeService menuTypeService;
	@Autowired
	private  OrderingService orderingService;
	
	
	//������Ʒ���¶�����һ����Ʒֱ���¶�����
	@RequestMapping("/buyMenu")
	public String buyMenu(HttpServletRequest request,int menuid){
		//����menuid��ѯ��ʽ�ļ�Ǯ
		Menu menu=shoreService.selectByPrimaryKey(menuid);
		//�����ݷŵ�request�������ȥ��
		request.setAttribute("menu",menu);
		
		//����д������Ϣҳ��;
		return "/WriteMessage";
	}
	
	//������Ʒȷ���ύ��
	@RequestMapping("/sure")
	public String sure(HttpServletRequest request,Orders orders,Orderdetail orderdetail){
		
		//���ö����ύ��ʱ�䣺
		Date date = new Date();
        java.sql.Date createtime =new java.sql.Date(date.getTime());
        orders.setCreatetime(createtime);

        //��Ӷ��������ݿ⣺
        orderingService.sure(orders);
        //��Ӷ�����ϸ�����ݿ⣺
        orderdetail.setOrderId(orders.getOrderid());
        orderdetail.setMenuNum(orders.getNumber());
        orderingService.confirm(orderdetail);
        
        request.setAttribute("orderid", orders.getOrderid());
        request.setAttribute("shoreid", orders.getShoreId());
		//���¶����ɹ���ҳ��;
		return "/OrderSuccess";
	}
	
	//�鿴������Ʒ����ɹ�����ϸ��Ϣ��
	@RequestMapping("/seeOrder/{orderid}")
	public String seeOrder(HttpServletRequest request,@PathVariable("orderid")int orderid){
		
		//���ݶ���id���ж���ѯ������һ��queryVo����
		OrdersVo orders = orderingService.findOrderById(orderid);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(orders.getCreatetime());
		
		request.setAttribute("dateString", dateString);
		request.setAttribute("orders", orders);
		
		//����������ҳ��;
		return "/ordertailView";
	}
	
	//��ӵ����ﳵ��
	@RequestMapping("/addcar")
	public String addcar(HttpServletRequest request,int shoreid,int userid,int[] menuIdList){
		
		//ȡ��checkbox��menuid,��ӵ����ﳵ��
		for(int menuid:menuIdList){
			Car car=new Car();
			car.setUserId(userid);
			System.out.println(menuid);
			car.setMenuId(menuid);
			//��������
			orderingService.addcar(car);
		}
		
		//�ص�ԭ�����̵�ҳ�棺����ѡ��
		return toShore(shoreid, request);
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
	
	//�鿴�ҵĹ��ﳵ
	@RequestMapping("/mycar")
	public String mycar(int userid,HttpServletRequest request){
		
		//�����û�id�鿴���ﳵ���Լ����ﳵ��Ʒ����Ϣ
		List<CarVo> carList=orderingService.selectCarByUserid(userid);
		request.setAttribute("carList", carList);
		
		//�����ﳵҳ��
		return "/CarView";
	}
	
	//�ڹ��ﳵ�µ���
	@RequestMapping("/book")
	public String book(HttpServletRequest request,int[] caridLisr){
		//���order��Id�ļ���
		List<Integer> oidList=new ArrayList<Integer>();
		//�����ܼۣ�
		int sum=0;
		//������Ʒ����Ŀ
		int count=caridLisr.length;
		
		for(int carid:caridLisr){
			//����carid ����menuid
			CarVo carVo=orderingService.selectCarByCarid(carid);
			//����car��menuid���� menu
			//��menu��shore_id�� orders��shore_id ��
			
			//��ʼ��order����Ϣ��
			Orders order=new Orders();
			order.setShoreId(carVo.getMenu().getShoreId());
			order.setUserId(carVo.getUserId());
			order.setNumber(1);
			order.setPay(0);
			order.setStatu(0);
			order.setPhone("");
			order.setAddress("");
			order.setTotal(carVo.getMenu().getMenuprice());
			order.setUname("");
			Date date = new Date();
	        java.sql.Date createtime =new java.sql.Date(date.getTime());
	        order.setCreatetime(createtime);
			
			//�ܼ�
			sum=sum+carVo.getMenu().getMenuprice();
			//�ύ������
			orderingService.sure(order);
			//������id���뼯����
			oidList.add(order.getOrderid());
			
			//�ύ������ϸ��Ϣ��
			Orderdetail orderdetail=new Orderdetail();
			orderdetail.setMenuId(carVo.getMenuId());
			orderdetail.setOrderId(order.getOrderid());
			orderdetail.setMenuNum(1);
			orderingService.confirm(orderdetail);
		}
		request.setAttribute("oidList", oidList);
		request.setAttribute("sum", sum);
		request.setAttribute("count", count);
		
		//����д��Ϣҳ�棻
		return "/WriteMessage2";
	}
	
	//�����Ʒ��д��ϸ��Ϣ�ύ��
	@RequestMapping("/sure2")
	public String sure2(HttpServletRequest request,Orders orders,int[] oidList){
		
		//���ö����ύ��ʱ�䣺
		Date date = new Date();
        java.sql.Date createtime =new java.sql.Date(date.getTime());
        
        for(int orderid:oidList)
        {
        	orders.setOrderid(orderid);
        	orders.setCreatetime(createtime);
        	//���¶�����
        	orderingService.updateByPrimaryKeySelective(orders);
        }
        
		//���¶����ɹ���ҳ��;
		return "/OrderSuccess2";
	}
	
	//���ҵĶ���ҳ�棺
	@RequestMapping("/toMyOrder")
	public String toMyOrder(HttpServletRequest request,int userid){
		//ͨ���û�id��ѯ���еĶ������Ѽ�¼
		List<OrdersVo> orderList=orderingService.findOrderByuserId(userid);

		request.setAttribute("orderList", orderList);
		//���ҵĶ�������;
		return "/MyOrdering";
	}
	
	
	//��ɶ�����
	@RequestMapping("/finishOrder")
	public String finishOrder(HttpServletRequest request,int orderid,int userid){
		
		//���ݶ���id,��ѯ�������޸Ķ����Ѿ����
		Orders order=orderingService.selectByPrimaryKey(orderid);
		order.setStatu(1);
		orderingService.updateByPrimaryKeySelective(order);
		
		//ͨ���û�id��ѯ���еĶ������Ѽ�¼
		List<OrdersVo> orderList=orderingService.findOrderByuserId(userid);

		request.setAttribute("orderList", orderList);
		//���ҵĶ�������;
		return "/MyOrdering";
	}
	
	//�鿴�Ѿ���ɵĶ�����
	@RequestMapping("/findMyOrderByStatu1")
	public String findMyOrderByStatu1(HttpServletRequest request,int userid){
		//ͨ���û�id��ѯ���еĶ������Ѽ�¼
		List<OrdersVo> orderList=orderingService.findMyOrderByStatu1(userid);
		
		request.setAttribute("orderList", orderList);
		//���ҵĶ�������;
		return "/MyOrdering";
	}
	
	//�鿴�������ѵĶ�����
	@RequestMapping("/findMyOrderByStatu2")
	public String findMyOrderByStatu2(HttpServletRequest request,int userid){
		//ͨ���û�id��ѯ���еĶ������Ѽ�¼
		List<OrdersVo> orderList=orderingService.findMyOrderByStatu2(userid);
		
		request.setAttribute("orderList", orderList);
		//���ҵĶ�������;
		return "/MyOrdering";
	}
	
	//���۶�����
	@RequestMapping("/discussOrder")
	public String discussOrder(HttpServletRequest request,int orderid){
		//���ݶ���Id��ѯ����
		OrdersVo order=orderingService.findOrderById(orderid);
		//�������ŵ��������
		request.setAttribute("order", order);
		//�����۶�������;
		return "/discussView";
	}
	
	//���۶����ύ��
	@RequestMapping("/discussSuccess")
	public String discussSuccess(HttpServletRequest request,Orders order){
		//���ݶ���Id��ѯ����
		orderingService.updateByPrimaryKeySelective(order);
		
		//�����۶����ɹ�����;
		return "/discussSuccess";
	}
}
