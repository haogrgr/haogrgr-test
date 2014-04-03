package com.haogrgr.test.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haogrgr.test.dao.ResourceDao;
import com.haogrgr.test.main.GetUrls;
import com.haogrgr.test.model.ResourceCommand;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String test(HttpServletRequest request, Model model) throws Exception {
        return "home";
    }

    @RequestMapping(value = "/add")
    public String addUrlPage(HttpServletRequest request, Model model) throws Exception {
        List<String> urls = GetUrls.readUrls();
        request.setAttribute("urls", urls);
        return "add_url";
    }
    
    @RequestMapping(value = "/do/add")
    public String addUrl(ResourceCommand commond) throws Exception {
        ResourceDao dao = new ResourceDao();
        
        for (int i = 0; i < commond.getUrl().size(); i++) {
            String url = commond.getUrl().get(i);
            String name = commond.getName().get(i);
            String desc = commond.getDesc().get(i);
            dao.insert(name, url, desc);
        }
        
        return "home";
    }
    
    @RequestMapping(value = "/path/{a}/{b}")
    public String testPath(@PathVariable("a") Double a, @PathVariable("b") Double b){
        System.out.println(a);
        System.out.println(b);
        return "home";
    }
}
