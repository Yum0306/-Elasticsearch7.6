package com.hikarios.elasticsearch.esjd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {


    /**
     * 获取模版首页
     * @return
     */
    @GetMapping({"/","/index"})
    public String index(){
        return "index";
    }
}
