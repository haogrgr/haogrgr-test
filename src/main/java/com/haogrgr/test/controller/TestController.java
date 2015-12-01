package com.haogrgr.test.controller;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haogrgr.test.main.Test;
import com.haogrgr.test.pojo.PageInfo;
import com.haogrgr.test.server.TestService;

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

	@RequestMapping("/json2")
	public @ResponseBody Object jsontest2(PageInfo<Test> pageInfo) {
		System.out.println(pageInfo.getTotal());
		return pageInfo;
	}

	@RequestMapping("/valid")
	public @ResponseBody Object valid(@Valid Vlid v, BindingResult result, Model model) {
		System.out.println(v);
		return v;
	}

	public static class Vlid {
		@NotNull
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}
