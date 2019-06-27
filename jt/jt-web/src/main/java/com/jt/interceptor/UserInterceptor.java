package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.jt.pojo.User;
import com.jt.until.UserThreadLocal;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;
@Component//将拦截器交给spring容器管理
public class UserInterceptor implements HandlerInterceptor{
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 在spring4版本中要求必须重写3个方法,不管是否需要
	 * 在spring5版本中的在接口中添加default属性,则可以省略
	 */
	/**
	 * springMVC调用流程图
	 * 返回值结果:
	 * true:拦截放行
	 * false:请求拦截,重定向登录页面
	 * 业务逻辑:
	 * 1.获取cookie数据
	 * 2.从cookie中获取token(TICKEN)
	 * 3.判断rdis缓存数据服务器是否有数据
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = null;
		// 1.获取cookie对象
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie:cookies) {
			if("JT_TICKET".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}
		//2.判断token是否有效
		if(!StringUtils.isEmpty(token)) {
			//4.判断redis中是否有数据
			String userJSON = jedisCluster.get(token);
			if(!StringUtils.isEmpty(userJSON)) {
				//redis有数据拦截成功
				//将userJSON转化为对象
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				//将数据保存到request域中
				//request.setAttribute("JT_USER",user);
				//用户每次请求都将数据保存到session中,切忌用完关闭
				//request.getSession().setAttribute("JT_USER", user);
				UserThreadLocal.set(user);
				return true;
			}
		}
		//重定向登录页面
		response.sendRedirect("/user/login.html");
		return false;//标识拦截
	}
	//销毁session
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		//request.getSession().removeAttribute("JT_USER");
		//防止数据泄露关闭本地线程参数
		UserThreadLocal.remove();
	}
	
}
