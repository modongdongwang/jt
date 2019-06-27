package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 需求:
	 * 根据用户传递的数据进行校验
	 * 返回值:
	 * 	SysResult 其中data表示boolean值	
	 * 			  true 表示数据已存在
	 * 			  false 表示数据可以使用
	 * 调用方式:
	 * 前台页面采用jsonp方法调用
	 * 最终返回值必须经过特殊格式封装 JSONPObject
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,
			@PathVariable Integer type,
			String callback) {
		JSONPObject object = null;
		try {
			boolean flag = userService.checkUser(param,type);
			object = new JSONPObject(callback, SysResult.ok(flag));			
		} catch (Exception e) {
			e.printStackTrace();
			object = new JSONPObject(callback, SysResult.fail());
		}									
		return object;

	}
	/*@RequestMapping("/register")
	public SysResult saveUser(String json) {
		try {
			User user = ObjectMapperUtil.toObject(json, User.class);
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}*/
	//利用跨域实现用户信息回显
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback) {
		String userJSON = jedisCluster.get(ticket);
		if(StringUtils.isEmpty(userJSON)) {
			//回传数据需要经过200判断SysResult对象
			return new JSONPObject(callback,SysResult.fail());
		}
		return new JSONPObject(callback,SysResult.ok(userJSON));
	}
}
