package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.until.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Reference(timeout = 3000,check = false)
	private DubboOrderService orderService;
	@Reference(timeout = 3000,check = false)
	private DubboCartService cartService;
	@RequestMapping("/create")
	public String orderCreate(Model model) {
		Long userId = UserThreadLocal.get().getId();
		List<Cart> carts = cartService.findCartListByUser(userId);
		model.addAttribute("carts",carts);
		return "order-cart";
		
	}
	/**
	 * 实现订单的入库
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult insertOrder(Order order) {
		try {
			Long userId = UserThreadLocal.get().getId();
			order.setUserId(userId);
			String orderId = orderService.insertOrder(order);
			if(!StringUtils.isEmpty(orderId)) {
				return SysResult.ok(orderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return SysResult.fail();
	}
	/**
	 * 根据订单信息,查询数据
	 */
	@RequestMapping("/success")
	public String findOrderById(String id,Model model) {
		Order order = orderService.findOrderById(id);
		model.addAttribute("order",order);
		return "success";
		
	}
}
