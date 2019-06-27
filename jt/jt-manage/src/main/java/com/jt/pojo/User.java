package com.jt.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
//当程序中的转换对象时,如多遇到未知属性,则忽略
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	//研究转化的方法
	private Integer id;
	private String name;
	private String sex;
	public Integer getIds() {
		return id;
		
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", sex=" + sex + "]";
	}
	
	
}
