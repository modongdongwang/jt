package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.until.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Reference(timeout=3000,check=false)
	private DubboCartService cartService;
	@RequestMapping("/show")
	public String findCartListByUser(Model model,HttpServletRequest request) {
		/**User user = (User)request.getAttribute("JT_USER");
		Long userId = user.getId();//暂时写死*/
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList = 
				cartService.findCartListByUser(userId);
		model.addAttribute("cartList", cartList);
		return "cart";	//转发到cart.jsp页面
	}
	/**
	 * 实现购物车数量的修改
	 *规定:如果url参数使用restFul风格获取数据时
	 *介绍参数是对象并且属性匹配,则可以适用对象接收
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
		try {
			Long userId = UserThreadLocal.get().getId();
			cart.setUserId(userId);
			cartService.updateCartNum(cart);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	
		
	}
	/**
	 * 实现购物车的数据删除
	 */
	@RequestMapping("delete/{itemId}")
	public String deleteCart(Cart cart) {
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		//重定向到购物车页面
		return "redirect:/cart/show.html";
	}
	/**
	 * 新增购物车
	 * 页面表单提交发起post请求
	 * 携带购物车参数
	 */
	@RequestMapping("/add/{itemId}")
	public String insertCart(Cart cart) {
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.insertCart(cart);
		//新增数据之后,展现购物车信息.重定向到购物车页面
		return "redirect:/cart/show.html";
	}
}
