package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

//标识redis配置类
@Configuration//xml
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	//	@Value("${jedis.host}")
	//	private String host;
	//	@Value("${jedis.port}")
	//	private Integer port;
	//	@Bean
	//	public Jedis jedis() {
	//		return new Jedis(host,port);
	//		
	//	}
	//redis分布机制测试
	/*@Value("${redis.nodes}")
	private String nodes;//ip端口

	@Bean
	public ShardedJedis getShardes() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		//将nodes中的数据进行分组 ip
		String[] node = nodes.split(",");
		for(String nodeArgs:node) {
			String[] args = nodeArgs.split(":");//{IP:端口}
			String nodeIP=args[0];
			int nodePort = Integer.parseInt(args[1]);
			JedisShardInfo info = new JedisShardInfo(nodeIP, nodePort);
			shards.add(info);
		}
		return new ShardedJedis(shards);
	}*/
	/*@Value("${redis.masterName}")
	private String masterName;
	@Value("${redis.sentinelNodes}")
	private String sentinelNodes;
	//通过哨兵机制实现redis操作
	@Bean
	public JedisSentinelPool getPool() {
		Set<String> sentinel = new HashSet<>();
		String[] nodes=sentinelNodes.split(",");
		for(String sNode:nodes) {
			sentinel.add(sNode);
		}
		return new JedisSentinelPool(masterName,sentinel);

	}*/
	@Value("${redis.nodes}")
	private String redisNodes;
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		//node="ip:端口"
		String[] node = redisNodes.split(",");
		for(String infonode: node) {
			String host = infonode.split(":")[0];
			int port = Integer.parseInt(infonode.split(":")[1]);
			nodes.add(new HostAndPort(host,port));
		}
		return new JedisCluster(nodes);
	}
}
