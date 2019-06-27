package com.jt.controller;

import java.io.File;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;

@Controller
public class FileController {
	@Autowired
	private FileService fileService;
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws Exception {
		//1.获取标签input下的name属性
		String inputName = fileImage.getName();
		System.out.println("1:"+inputName);
		//2.获取文件的名称
		String fileName = fileImage.getOriginalFilename();
		//3.获取文件夹路径
		File Dirfile = new File("D:/1-jt/image/");
		if(!Dirfile.exists()) {
			//创建文件
			Dirfile.mkdirs();
		}
		//4.完成上传文件
		fileImage.transferTo(new File("D:/1-jt/image/"+fileName));
		return "redirect:/file.jsp";
		
	}
	//实现上传文件
	@RequestMapping("/pic/upload")
	@ResponseBody
	public ImageVO uploadFile(MultipartFile uploadFile) {
		return fileService.updateFile(uploadFile);
		
	}
}
