package com.liu.swagger.controller;

import com.liu.swagger.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "哈哈")
public class HelloWorldController {

    @ApiOperation("你好")
    @GetMapping("/hello")
    public String Hello(String name){
        return "Hello World          "+name;
    }
    @PostMapping("/login")
    public User login(String name,String ps){

        return new User(name,ps);
    }
}
