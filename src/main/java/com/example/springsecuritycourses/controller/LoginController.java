package com.example.springsecuritycourses.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

//    @RequestMapping("login")
//    public String login(){
//        log.debug("执行登录方法");
//        return "redirect:main.html";
//    }
    //@Secured("ROLE_abc")
    @PreAuthorize("hasAnyRole('ROLE_abc,Abc')")
    @RequestMapping("toMain")
    public String toMain(){
        return "redirect:main.html";
    }

    @RequestMapping("toError")
    public String toError(){
        return "redirect:error.html";
    }
}
