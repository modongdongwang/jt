package com.jt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

public class TestRedisShards {
	/**
	 * 操作时需要将多台redis当做一台
	 * 
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo info1 = new JedisShardInfo("192.168.190.13",6379);
		JedisShardInfo info2 = new JedisShardInfo("192.168.190.13",6380);
		JedisShardInfo info3 = new JedisShardInfo("192.168.190.13",6381);
		shards.add(info1);
		shards.add(info2);
		shards.add(info3);
		//操作分片redis对象的工具类
		
		ShardedJedis shardedJedis = new ShardedJedis(shards);
		shardedJedis.set("1902", "1902你最棒");
		System.out.println(shardedJedis.get("1902"));
	}
}
