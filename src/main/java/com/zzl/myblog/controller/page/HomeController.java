package com.zzl.myblog.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/admin/home")
    public String home() {
        return "admin/home";
    }

    @GetMapping("/login")
    public String index() {
        return "public/login";
    }
}