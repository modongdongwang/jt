package com.jt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jt.pojo.ItemDesc;
import com.jt.pojo.User;

import redis.clients.jedis.Jedis;

public class TestRedis {
	//String 类型的操作方式
	@Test
	public void testString() {
		Jedis jedis = new Jedis("192.168.190.13",6379);
		jedis.set("1902", "1902班");
		System.out.println(jedis.get("1902"));
	}
	@Test
	public void testTimeOut() throws Exception {
		Jedis jedis = new Jedis("192.168.190.13",6379);
		jedis.setex("aa", 2, "bb");
		System.out.println(jedis.get("aa"));
		Thread.sleep(3000);
		//key不存在时,才使用
		Long result = jedis.setnx("aa", "bb");
		System.out.println("获取输出数据"+result+":"+jedis.get("aa"));
	}
	//实现对象转化json
	@Test
	public void objectToJson() throws Exception {
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(100L).setItemDesc("ccc");
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(100L).setItemDesc("ccc");
		List<ItemDesc> list = new ArrayList<ItemDesc>();
		list.add(itemDesc1);
		list.add(itemDesc2);
		ObjectMapper mapper = new ObjectMapper();
		//转化json时,必须用get/set方法
		String json = mapper.writeValueAsString(list);
		System.out.println("转化为json"+json);
		//将json转化为对象
		Jedis jedis = new Jedis("192.168.190.13",6379);
		jedis.set("itemDescList", json);
		//获取数据 (通过k获取)
		String string=jedis.get("itemDescList");
		List<ItemDesc> value = mapper.readValue(string, list.getClass());
		System.out.println("测试对象:"+value);
	}
	/**
	 * 3.利用redis保存业务数据 数据库
	 * 数据库数据:对象object
	 * string类型要求只能存储字符串类型
	 * item--->json--->字符串
	 */
	@Test
	public void testObject() {
		Jedis jedis = new Jedis("192.168.190.13",6379);
	}

	//将对象转化为json串
	/**
	 * 1.首先获取对象的getxxx方法
	 * 2.将get去掉之后,首字母小写获取属性名称
	 * 3.之后将属性名称:属性值进行拼接
	 * 4.形成json串(字符串)
	 * @throws Exception
	 */
	@Test
	public void testToJSON() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		User user = new User();
		user.setId(1);
		user.setName("马云");
		user.setSex("男");
		String json = mapper.writeValueAsString(user);
		System.out.println(json);
	}
	/**
	 * 1.获取json串
	 * 2.通过json串获取json中的key
	 * 3.根据class类型反射机制获取对象
	 * 4.根据key调用setkey方法为对象赋值
	 * 5.转化成对象
	 * 当程序中的转换对象时,如多遇到未知属性,则忽略
           @JsonIgnoreProperties(ignoreUnknown = true)
	 * @throws Exception
	 */
	@Test
	public void testToUser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		User user = new User();
		user.setId(1);
		user.setName("马云");
		user.setSex("男");
		//以下方法实现json串转换为对象
		String json = mapper.writeValueAsString(user);
		User user1 = mapper.readValue(json, User.class);
		System.out.println(user1);
	}
}
