package com.haogrgr.test.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {

	@RequestMapping(value = "/")
	public String test(HttpServletRequest request, Model model) {
		return "home";
	}

	@ResponseBody
	@RequestMapping("/json")
	public String json() {
		return "{fun:function(){alert(1);}}";
	}

	@ResponseBody
	@RequestMapping(value = "/path/{param}")
	public String path(@PathVariable String param) {
		System.out.println(param);
		return "home";
	}

	@ResponseBody
	@RequestMapping(value = "/upload")
	public String upload(MultipartFile file) {
		System.out.println(file.getName());
		return "name:" + file.getName();
	}

}
