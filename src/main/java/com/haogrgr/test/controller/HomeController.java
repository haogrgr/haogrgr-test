package com.haogrgr.test.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {

	@RequestMapping(value = "/")
	public String test(HttpServletRequest request, Model model) throws Exception {
		return "home";
	}

	@ResponseBody
	@RequestMapping(value = "/upload/{type}")
	public String test(@PathVariable String type, @RequestParam(value = "file", required = false) MultipartFile file) {
		System.out.println(type);
		System.out.println(file.getName());
		return "home";
	}

	@RequestMapping(value = "/process")
	public String process(String name, HttpServletRequest request, Model model) throws Exception {
		System.err.println(name);
		return "home";
	}

	@RequestMapping(value = "/resp")
	public String testResponseWrite(HttpServletResponse response) {
		try {
			response.getWriter().write("success");
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
