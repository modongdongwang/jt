package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private JedisCluster jedisCluster;
	//导入dubbo的用户接口
	@Reference(timeout = 3000,check = false)
	private DubboUserService userService;
	/*@Autowired
	private UserService userService;*/
	@RequestMapping("/{moduleName}")
	public String index(@PathVariable String moduleName) {
		return moduleName;
	}
	@RequestMapping("/doRegister")
	@ResponseBody
	
	public SysResult saveUser(User user) {
		try {
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
		
		
	}
	/**
	 * 实现用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult login(User user,HttpServletResponse reponse) {
		try {
			//调用sso系统获取秘钥
			String token = userService.findUserByUP(user);
			//如果数据不为null时,将数据保存到cookie中
			//cookie中的key为固定值JT_TICKET
			if(!StringUtils.isEmpty(token)) {
				Cookie cookie = new Cookie("JT_TICKET",token);
				cookie.setMaxAge(7*24*3600);//生命周期
				cookie.setPath("/");//cookie的权限用"/"
				cookie.setDomain("jt.com");//要求所有的www.jt.com数据共享
				//利用response
				reponse.addCookie(cookie);
				return SysResult.ok();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}
	/**
	 * 实现用户的登出操作
	 * 1.删除redis   request对象~~~cokie中~~~JT_TICKET
	 * 2.删除cookie
	 * @return
	 */
	@RequestMapping("/logout")
	@ResponseBody
	public String logout(HttpServletRequest request,HttpServletResponse reponse) {
		
		Cookie[] cookies = request.getCookies();
		if(cookies.length !=0) {
			String token = null;
			for(Cookie cookie:cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
			//判断数据是否有值  删除redis/删除cookie
			if(!StringUtils.isEmpty(token)) {
				jedisCluster.del(token);
				
				Cookie cookie = new Cookie("JT_TICKET","");
				cookie.setMaxAge(0);//立即删除数据cookie
				cookie.setPath("/");
				cookie.setDomain("jt.com");
				reponse.addCookie(cookie);
			}
		}
		
		//当用户登出时,页面重定向到系统中
		return "redirect:/";
		
	}
}
