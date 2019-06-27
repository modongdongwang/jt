package com.jt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HelloController {
	@RequestMapping("/getMsg")
	public String testTomcat() {
		return "这是8092服务器";
		
	}
			
}
