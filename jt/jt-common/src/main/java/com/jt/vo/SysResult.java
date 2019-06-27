package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

//封装系统的vo对象
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {
	private Integer status;//200标识成功,201表示错误
	private String msg;//后台返回值数据信息
	private Object data;//后台返回任意数据
	
	public static SysResult ok() {
		return new SysResult(200,null,null);
		
	}
	//假如用户需要回传一个data data数据类型是String,不能用该方法
//	public static SysResult ok(String msg) {
//		return new SysResult(200,msg,null);
//		
//	}
	public static SysResult ok(Object data) {
		return new SysResult(200,null,data);
		
	}
	public static SysResult ok(String msg,Object data) {
		return new SysResult(200,msg,data);
		
	}
	public static SysResult fail() {
		return new SysResult(201,null,null);
		
	}
	public static SysResult fail(String msg) {
		return new SysResult(201,msg,null);
		
	}
}
