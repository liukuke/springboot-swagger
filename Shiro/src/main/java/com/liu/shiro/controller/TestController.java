package com.liu.shiro.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Shiro注解
 * @RequiresPermissions() 用于判断当前用户是否有这个权限
 * @RequiresRoles() 用于判断当前用户是否是这个角色
 * @RequiresAuthentication 用于认证
 * String[] roles={""};
 * subject.checkRoles(roles);验证当前用户是否具有指定的角色
 * String[] permissions={""};
 * subject.checkPermissions(permissions);验证当前用户是否具有当前指定的角色
 */

@Controller
public class TestController {

    @RequestMapping("/")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model){
        // 创建一个shiro的Subject对象，利用这个对象来完成用户的登录认证
        Subject subject= SecurityUtils.getSubject();
        // 登出，在登录之前先清一遍缓存
        // 缺点：如果用户是误操作，还需要在查一次数据库
        subject.logout();
        // 判断当前用户是否已经认证过，如果已经认证过着不需要认证如果没有认证过则进入if完成认证
        if (!subject.isAuthenticated()){
            // 创建一个用户账号和密码的Token对象，并设置用户输入的账号和密码
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            try {

                // 调用login后Shiro就会自动执行我们自定义的Realm中的认证方法
                // 当认证失败时候会抛出各种异常，例如账号不存在或密码错误等等，我们需要根据不同的异常类型来判断用户的登录状态并给与友好的信息提示
                subject.login(usernamePasswordToken);
            } catch (UnknownAccountException e) {
                model.addAttribute("errorMessage","账号错误");
                return "login";
            }catch (LockedAccountException e){
                model.addAttribute("errorMessage","账号被锁定");
                return "login";
            }catch (IncorrectCredentialsException e){
                model.addAttribute("errorMessage","密码错误");
                return "login";
            }
        }

        // forward 请求转发
        // redirect 重定向
        return "redirect:/success";
    }

    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        // 登出方法调用，用于清空登录时的缓存信息，否则无法重复登录
        subject.logout();
        return "redirect:/";
    }

    @RequestMapping("/success")
    public String loginSuccess(){
        return "success";
    }

    @RequestMapping("/noquanxian")
    public String noQuanXian(){
        return "noQuanXian";
    }

    @RequestMapping("/admin/test")
    /**
     * 这个注解是shiro提供的用来标记当前类或当前方法必须要什么角色
     * 属性：
     *     value:用来设置一个或多个角色名。
     */
    @RequiresRoles(value = {"admin"})
    @ResponseBody
    public String adminTest(){
        return "AdminTest请求";
    }

    @RequestMapping("/admin/add")
    @RequiresPermissions(value = {"admin:add"})
    @ResponseBody
    public String adminAdd(){

        return "AdminTestAdd请求";
    }

    @RequiresRoles(value = {"user"})
    @RequestMapping("/user/test")
    @ResponseBody
    public String userTest(){
        return "UserTest请求";
    }

    /**
     * 配置自定义异常拦截，需要拦截ShiroException或者他的子类
     * 注意：当Shiro出现权限认证失败以后会出现权限不足异常，所以需要抛出权限异常，否则无法转到权限不足的页面
     * @return
     */
    @ExceptionHandler(value = {AuthorizationException.class})
    public String permissonError(Throwable throwable){

        return "redirect:/noquanxian";
    }
}
