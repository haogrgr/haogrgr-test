package com.haogrgr.test.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haogrgr.test.util.MapBuilder;
import com.haogrgr.test.util.PageInfo;

@Controller
@RequestMapping("/pagination")
public class PaginationController {
	
	@RequestMapping("")
	public String indexPage(){
		return "pagination/index";
	}
	
	@ResponseBody
	@RequestMapping("/data")
	public Object testJson(Integer page, Integer rows) {
		PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(page, rows);
		pageInfo.setRows(getMapList(pageInfo));
		return pageInfo;
	}

	private List<Map<String, String>> getMapList(PageInfo<?> pageInfo) {
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		for (int i = pageInfo.getBegin(); i < pageInfo.getEnd() * pageInfo.getPageNo(); i++) {
			rows.add(MapBuilder.make("id", "id" + i).build("name", "name" + i));
		}
		return rows;
	}

}
