package com.springboot.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.ResponseBody;

@Controller
// @ResponseBody
public class IndexController {
    @RequestMapping("/")
    public String index(){
        return "index.html";
    }
}
