package com.jt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class testSentinel {
	
	@Test
	public void test01() {
		//masterName  代表主机的变量名称
		//sentinels set<string> ip:端口
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.190.13:26379");
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = sentinelPool.getResource();
		jedis.set("aa", "端午节后没有假期啦.....");
		System.out.println(jedis.get("aa"));
		jedis.close();//关闭连接
	}
}
