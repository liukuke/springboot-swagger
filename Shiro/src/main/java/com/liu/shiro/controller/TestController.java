package com.liu.shiro.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @RequestMapping("/")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(){
        // forward 请求转发
        // redirect 重定向
        return "redirect:/success";
    }

    @RequestMapping("/logout")
    public String logout(){
        return "redirect:/";
    }

    @RequestMapping("/success")
    public String loginSuccess(){
        return "success";
    }

    @RequestMapping("noqunxian")
    public String noQuanXian(){
        return "noQuanXian";
    }

    @RequestMapping("/admin/test")
    @ResponseBody
    public String adminTest(){
        return "AdminTest请求";
    }

    @RequestMapping("/user/test")
    @ResponseBody
    public String userTest(){
        return "UserTest请求";
    }
}
