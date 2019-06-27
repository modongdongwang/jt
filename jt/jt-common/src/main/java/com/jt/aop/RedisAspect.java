package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.RedisService;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

@Component//将对象交给spring容器管理
@Aspect//标识切面
public class RedisAspect {
	//容器初始化时不需要实例化对象,只有用户使用时才初始化,一般工具中添加该注解
	@Autowired(required = false)
	//private ShardedJedis jedis;
	//private RedisService jedis;
	private JedisCluster jedis;
	//使用该方法可以直接获取注解的对象
	//execution(返回值类型 包名.类名.方法名(参数列表))
	@SuppressWarnings("unchecked")
	@Around("@annotation(cache_find)")
	public Object around(ProceedingJoinPoint joinPoint,Cache_Find cache_find) {
		//1.获取key
		String key = getKey(joinPoint,cache_find);
		//2.获取缓存数据
		String result = jedis.get(key);
		Object data = null;
		//3.判断结果是否有数据
		try {							
			if(StringUtils.isEmpty(result)) {
				//表示缓存中没有数据,查询数据库
				data = joinPoint.proceed();//表示业务方法执行
				//将数据转化为json串
				String json = ObjectMapperUtil.toJSON(data);
				if(cache_find.seconds() == 0)
					jedis.set(key,json);	//表示永不超时
				else
					jedis.setex(key,cache_find.seconds(), json);
				System.out.println("查询数据库");
			}else {
				Class targetClass = getTargetClass(joinPoint);
				data = ObjectMapperUtil.toObject(result, targetClass);
				System.out.println("AOP查询缓存!!!");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return data;
	}
	//获取返回值的类型
	private Class getTargetClass(ProceedingJoinPoint joinPoint) {
		//joinpoint.getSignature():(signature是信号,标识的意思):获取被增强的方法相关信息.
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		
		//return signature.getReturnType();
		Class returnType = signature.getReturnType();
		return returnType;
	}

	/**
	 * 1.判断用户key类型  auto empty
	 * @param joinPoint
	 * @param cache_find
	 * @return
	 */
	private String getKey(ProceedingJoinPoint joinPoint, Cache_Find cache_find) {
		//1.获取key类型
		KEY_ENUM key_ENUM = cache_find.keyType();
		//2.判断key类型
		if(key_ENUM.equals(KEY_ENUM.EMPTY)) {
			//表示使用用户自己的key
			return cache_find.key();
		}
		//表示用户的key需要拼接  key+"_"+第一个参数
		String strArgs = String.valueOf(joinPoint.getArgs()[0]);
		String key =  cache_find.key()+"_"+strArgs;
		return key;

	}
}