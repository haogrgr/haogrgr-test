package com.haogrgr.test.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String test(HttpServletRequest request, Model model) throws Exception {
        return "home";
    }

    @RequestMapping(value = "/path/{a}/{b}")
    public String testPath(@PathVariable("a") Double a, @PathVariable("b") Double b) {
        System.out.println(a);
        System.out.println(b);
        return "home";
    }
}
