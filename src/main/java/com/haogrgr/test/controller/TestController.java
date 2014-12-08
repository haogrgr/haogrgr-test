package com.haogrgr.test.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haogrgr.test.main.Test;
import com.haogrgr.test.server.TestService;
import com.haogrgr.test.util.PageInfo;

@Controller
@RequestMapping("/test")
public class TestController {

	@Resource
	private TestService testService;

	@RequestMapping("/index")
	public String test() {
		testService.testExp();
		return null;
	}
	
	@RequestMapping("/json")
	public @ResponseBody Object jsontest(@RequestBody PageInfo<Test> pageInfo) {
		System.out.println(pageInfo.getTotal());
		return pageInfo;
	}

}
