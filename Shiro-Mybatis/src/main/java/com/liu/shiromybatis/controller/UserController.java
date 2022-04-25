package com.liu.shiromybatis.controller;


import com.liu.shiromybatis.dao.UserDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 *
 * @author Administrator
 */
@Controller
public class UserController {

    @RequestMapping("/")
    public String toLogin(){
        return "login";
    }


    @RequestMapping("/login")
    public String login(String username, String password, Model model){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        if (!subject.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);
            try {
                subject.login(token);
            } catch (UnknownAccountException e) {
                model.addAttribute("errorMessage","账号不对");
                return "login";
            } catch (LockedAccountException e) {
                model.addAttribute("errorMessage","账号被锁定");
                return "login";
            } catch (IncorrectCredentialsException e) {
                model.addAttribute("errorMessage","密码错误");
                return "login";
            }
        }
        return "redirect:success";
    }

    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/";
    }

    @RequestMapping("/success")
    public String toSuccess(){
        return "success";
    }


    @RequestMapping("/admin/test")
    @RequiresRoles(value = {"admin"})
    @ResponseBody
    public String adminTest(){
        return "admin-test";
    }

    @RequestMapping("/admin/add")
    @RequiresRoles(value = {"admin"})
    @RequiresPermissions(value = {"admin:add"})
    @ResponseBody
    public String adminAdd(){
        return "admin-add";
    }

    @RequestMapping("/user/test")
    @RequiresRoles(value = {"user"})
    @ResponseBody
    public String userTest(){
        return "user-test";
    }

    @RequestMapping("/user/add")
    @RequiresRoles(value = {"user"})
    @RequiresPermissions(value = {"user:add"})
    @ResponseBody
    public String userAdd(){
        return "user-add";
    }

    @ExceptionHandler(value = {AuthorizationException.class})
    public String authorizationException(){
        return "noAuthority";
    }
}
