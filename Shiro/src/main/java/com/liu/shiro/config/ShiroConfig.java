package com.liu.shiro.config;

import com.liu.shiro.real.MyRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

//Shiro的配置类
@Configuration
public class ShiroConfig {

    /**
     * 配置一个SecurityManager 资源管理器
     * @return
     */
    @Bean
    public SecurityManager securityManager(Realm myRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(myRealm);
        return defaultWebSecurityManager;
    }
    /**
     * 配置一个自定义的Realm的bean，最终将使用这个bean返回的对象来完成我们的认证和授权
     */
    @Bean
    public MyRealm myRealm(){
        MyRealm myRealm = new MyRealm();
        return myRealm;
    }
    /** 配置一个Shiro的过滤器bean，这个bean将配置Shiro相关的一个规则的拦截
     * 例如什么样的请求可以访问什么样的请求不可以访问等等
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        // 创建过滤器bean，创建Shiro的拦截的拦截器 ，用于拦截我们的用户请求
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 配置用户登录请求 如果需要进行登录时shiro就会找到这个请求进入登录页面
        shiroFilterFactoryBean.setLoginUrl("/");
        // 配置登录成功后转向的请求地址
        shiroFilterFactoryBean.setSuccessUrl("/success");
        // 配置没有权限时转向的请求地址
        shiroFilterFactoryBean.setUnauthorizedUrl("/noquanxian");
        // 配置权限规则
        Map<String,String> map=new LinkedHashMap<>();
        // 配置登录请求不需要认证，anon表示某个请求不需要认证
        map.put("/login","anon");
        // 配置登出的请求，登出后会清空当前用户的内存
        map.put("/logout","logout");
        // 配置一个admin开头的所有请求需要登录，authc表示需要登录认证
        map.put("/admin/**","authc");
        // 配置一个user开头的所有请求需要登录，authc表示需要登录认证
        map.put("/user/**","authc");
        // 配置剩余的所有请求全部需要登录验证，（注意：这个要写在最后）
        map.put("/**","authc");
        // 设置权限拦截规则
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

}
