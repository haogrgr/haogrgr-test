package com.haogrgr.test.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haogrgr.test.model.PageInfo;

@Controller
public class HomeController {
    
    @RequestMapping(value = "/")
    public String test(HttpServletRequest request, Model model) throws Exception {
        return "home";
    }
    
    @RequestMapping(value = "/process")
    public String process(String name, HttpServletRequest request, Model model) throws Exception {
        System.err.println(name);
        return "home";
    }
    
    @RequestMapping(value = "/path/{a}/{b}")
    public String testPath(@PathVariable("a") Double a, @PathVariable("b") Double b) {
        System.out.println(a);
        System.out.println(b);
        return "home";
    }
    
    @RequestMapping(value = "/resp")
    public String testResponseWrite(HttpServletResponse response){
        try {
            response.getWriter().write("success");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @ResponseBody
    @RequestMapping(value = "/json")
    public Object testJson(Integer page, Integer rows){
        PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String,String>>(page, rows);
        pageInfo.setRows(getMapList(pageInfo));
        return pageInfo;
    }
    
    private List<Map<String, String>> getMapList(PageInfo<?> pageInfo){
        List<Map<String, String>> rows = new ArrayList<Map<String,String>>(); 
        for (int i = pageInfo.getBegin(); i < pageInfo.getEnd()*pageInfo.getPageNo(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", "id" + i);
            map.put("name", "name" + i);
            rows.add(map);
        }
        return rows;
    }
    
}
