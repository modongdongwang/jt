package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

//该类是dubbo实现类
@Service
public class DubboUserServiceImpl implements DubboUserService{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;
	@Override
	public void saveUser(User user) {
		//1.将密码加密
		String md5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5).setEmail(user.getPhone())
		.setCreated(new Date()).setUpdated(user.getUpdated());
		userMapper.insert(user);
		
	}
	/**
	 * 1.校验用户名或密码是否正确
	 * 2.如果数据不正确 返回null
	 * 3.如果数据正确
	 *   3.1生成加秘秘钥:MD5(JT_TICKET+username+当前毫秒数)
	 *   3.2将userDB数据转化为userJSON
	 *   3.3将数据保存到redis中7天
	 *   3.4返回token
	 */
	@Override
	public String findUserByUP(User user) {
		String token = null;
		//1.将密码加密
		String md5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5);
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		User userDB = userMapper.selectOne(queryWrapper);
		//2.判断数据是否正确
		if(userDB != null) {
			token = "JT_TIKET_"+userDB.getUsername()
			+ System.currentTimeMillis();
			token = DigestUtils.md5DigestAsHex(token.getBytes());
			//脱敏处理,一切从业出发
			userDB.setPassword("你猜猜....");
			String json = ObjectMapperUtil.toJSON(userDB);
			//将数据保存到redis中
			jedisCluster.setex(token, 7*24*3600, json);
		}
		return token;
	}
}
