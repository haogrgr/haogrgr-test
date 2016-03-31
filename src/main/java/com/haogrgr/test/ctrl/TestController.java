package com.haogrgr.test.ctrl;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haogrgr.test.server.TestService;
import com.haogrgr.test.util.Lists;

@Controller
public class TestController {

	@Resource
	private TestService testService;

	@RequestMapping("/test/index")
	public String test() {
		return null;
	}

	@RequestMapping(path = "/test/json")
	public @ResponseBody Object jsontest() {
		System.out.println("xxx");
		return "呵呵";
	}

	@Json("/test/json2")
	public @ResponseBody Object jsontest2() {
		System.out.println("xxx");
		return "呵呵";
	}

	@ResponseBody
	@RequestMapping("/story.json")
	public Object promise() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("heading", "<h1>A story about something</h1>");

		ArrayList<String> array = Lists.array("chapter-1.json", "chapter-2.json", "chapter-3.json", "chapter-4.json",
				"chapter-5.json");
		for (int i = 0; i < array.size(); i++) {
			array.set(i, "/haogrgr-test/mvc/" + array.get(i));
		}
		map.put("chapterUrls", array);

		return map;
	}

	@ResponseBody
	@RequestMapping("/chapter-{index}.json")
	public Object chapter(@PathVariable Integer index) throws Exception {
		Thread.sleep(index * 1000);
		HashMap<String, Object> map = new HashMap<>();
		map.put("chapter", index);
		map.put("html", "<p>Chapter " + index + " text: this is chapter " + index + " </p>");
		return map;
	}
}
