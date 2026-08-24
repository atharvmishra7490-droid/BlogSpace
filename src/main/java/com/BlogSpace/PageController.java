package com.blogspace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home(){
        return "home";
    }
    
    @GetMapping("/create")
    public String create(){
        return "create";
    }
    
    @GetMapping("/explore")
    public String explore(){
        return "explore";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }
    
    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }
}