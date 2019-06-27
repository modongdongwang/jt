package com.jt.util;

import com.fasterxml.jackson.databind.ObjectMapper;

//编辑工具类,实现对象与json转化
public class ObjectMapperUtil {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static String toJSON(Object target) {
		String json = null;
		try {
			json = MAPPER.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			//将检测异常转化为运行时异常
			throw new RuntimeException();
		}
		return json;
	}
	 /**
     * 定义泛型方法时，必须在返回值前边加一个<T>，来声明这是一个泛型方法，持有一个泛型T，然后才可以用泛型T作为方法的返回值。
     * Class<T>的作用就是指明泛型的具体类型，而Class<T>类型的变量targetClass，可以用来创建泛型类的对象。
     * 为什么要用变量targetClass来创建对象呢？既然是泛型方法，就代表着我们不知道具体的类型是什么，也不知道构造方法如何，
     * 因此没有办法去new一个对象，但可以利用变量targetClass的newInstance方法去创建对象，也就是利用反射创建对象。
     * 为什么要使用泛型方法呢？因为泛型类要在实例化的时候就指明类型，如果想换一种类型，不得不重新new一次，可能不够灵活；
     * 而泛型方法可以在调用的时候指明类型，更加灵活。
     * @param <T>
     * @param json
     * @param targetClass
     * @return
     */
	public static <T> T toObject(String json,Class<T> targetClass) {
		T target = null;
		//T target = targetClass.newInstance();
		try {
			target = MAPPER.readValue(json, targetClass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return target;
		
	}
}
