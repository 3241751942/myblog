package com.zzl.myblog.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PublicController {
    @RequestMapping("/public/{page}")
    public String mapping(@PathVariable String page){
        return "public/"+page;
    }
}
