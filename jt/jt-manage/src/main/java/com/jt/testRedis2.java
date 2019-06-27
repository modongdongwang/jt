package com.jt;
import java.util.Map;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
public class testRedis2 {
	@Test
	public void testHash() {
		Jedis jedis = new Jedis("192.168.190.13",6379);
		jedis.hset("user", "id", "200");
		jedis.hset("user", "name", "龙小云");
		jedis.hset("user", "sex", "女");
		String id = jedis.hget("user", "id");
		String name = jedis.hget("user", "name");
		String sex = jedis.hget("user", "sex");
		System.out.println(id);
		System.out.println(name);
		System.out.println(sex);
		Map<String, String> user = jedis.hgetAll("user");
		System.out.println(user);
	}
	//编写list集合
	@Test
	public void list() {
		Jedis jedis = new Jedis("192.168.190.13",6379);
		jedis.lpush("list", "1","2","3","4","5");
		String rpop = jedis.rpop("list");
		System.out.println(rpop);
	}
	//redis事务控制
	@Test
	public void TestTx() {
		Jedis jedis = new Jedis("192.168.190.13",6379);
		Transaction transaction = jedis.multi();//开启事务
		try {
			transaction.set("ww", "www");
			transaction.set("aa", null);
			transaction.exec();//提交事务
		} catch (Exception e) {
			transaction.discard();//事务回滚
		}
	}
}
