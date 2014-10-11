package com.haogrgr.test.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haogrgr.test.server.TestService;

@Controller
@RequestMapping("/test")
public class TestController {

	@Resource
	private TestService testService;

	@RequestMapping("/index")
	public String test() {
		testService.all();
		return null;
	}

}
