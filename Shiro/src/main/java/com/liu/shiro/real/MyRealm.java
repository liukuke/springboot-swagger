package com.liu.shiro.real;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义realm用来实现用的认证和授权
 * 父类AuthenticatingRealm只负责用户认证（登录）
 * 父类AuthorizingRealm既可以实现用户认证，又可以实现权限授权
 */
public class MyRealm extends AuthorizingRealm {

    /**
     * Shiro的认证方法我们需要在这个方法中来获取用户的信息（从数据库中）
     * @param authenticationToken 用户登录时的Token（令牌），这个对象中将存放着我们用户在浏览器中存放的账号和密码
     * @return 返回一个AuthenticationInfo 对象，这个返回以后Shiro会调用这个对象中的一些方法来完成对密码的验证 密码是由Shiro
     * @throws AuthenticationException
     * 如果认证失败Shiro就会抛出AuthenticationException 我们也可以手动自己抛出这个AuthenticationException
     * 以及它的任意子异常类不通的异常类型可以认证过程中的不通错误情况我们需要根据异常类型来为用户返回特定的响应数据
     *  AuthenticationException 异常的子类  可以我们自己抛出
     *      AccountException 账号异常  可以我们自己抛出
     *      UnknownAccountException 账号不存在的异常  可以我们自己抛出
     *      LockedAccountException  账号异常锁定异常  可以我们自己抛出
     *      IncorrectCredentialsException  密码错误异常 这个异常会在Shiro进行密码验证时抛出
     *  注意：如果异常不够用，可以自己定义异常，需要继承AuthenticationException异常类。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 将AuthenticationToken强转成UsernamePasswordToken 这样获取账号和密码更加的方便
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 获取用户在浏览器中输入的账号
        String username = null;
        if (token.getUsername()!=null){
            username=token.getUsername();
        }
        // 获取用户在浏览器中输入的密码
        String password = null;
        if (token.getPassword()!=null){
            password=new String(token.getPassword());
        }
        // 认证账号,正常情况我们需要这里从数据库中获取账号的信息，以及其他关键数据，例如账号是否被冻结等等
        String dbName1="admin";
        String dbName2="zhangsan";
        // 判断账户是否存在
        if (!dbName1.equals(username) && !dbName2.equals(username) && !"user".equals(username)){
            // 抛出账号不存在异常
            throw new UnknownAccountException();
        }
        /**
         * 认证账号，这里应该根据从数据库中获取数来的数据进行逻辑判断，判断当前账号是否可用
         * IP是否允许等等，根据不同的逻可以抛出不同的异常
         */
        if (dbName2.equals(username)){
            // 抛出账号锁定异常
            throw new LockedAccountException();
        }

        /**
         * 对用户输入的密码进行加密,
         */
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 设置加密原则
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 设置加密次数
        hashedCredentialsMatcher.setHashIterations(2);
        // 将设置后的加密规则放入
        this.setCredentialsMatcher(hashedCredentialsMatcher);

        String dbPassword="123456";
        //数据库密码加密 使用Shiro的加密工具
        //参数 1 为加密算法 我们选择MD5加密
        //参数 2 为被加密的数据的数据
        //参数 3 为加密时的盐值 ，用于改变加密后数据结果，不同盐的加密结果是不同的
        //      通常这个盐值需要选择一个表中唯一的数据例如表中的账号
        //参数 4 为需要对数据使用指定的算法加密多少次
        /**
         * 注意：
         * 1、通常数据库中存放的数据不应该是明码123456 而是加密后的数据例如e10adc3949ba59abbe56e057f20f883e，这是使用MD5加密后的123456，如果数据库中的密码已经是加密后的那么这里可以不选择进行加密。
         * 2、如果数据库中的密码已经加密那么页面中传递数据前必须要对密码进行加密才能传递，否则无法可能会登录失败。
         * 3、如果选择加密传递那么页面和数据库中的密码加密次数以及盐必须相同，否则登录一定失败
         * 注意:
         * 建议浏览器传递数据时就是加密数据，数据库中存在的数据也是加密数据，我们必须保证前端传递的数据
         * 和数据主库中存放的数据加密次数以及盐的规则都是完全相同的否则认证失财
         */
        SimpleHash simpleHash = new SimpleHash("MD5",dbPassword,"",2);
        System.out.println(simpleHash);

        /** 创建密码认证对象，由Shiro自动认证密码
         *  参数1 数据库中的账号（或页面账号均可）
         *  参数2 为数据库中读取数据来的密码
         *  参数3 为当前Realm的名字
         *  如果 密码认证成功则返回一个用户身份对象，如果密码认证失败则会抛出异常IncorrectCredentialsException
         */
        return new SimpleAuthenticationInfo(username,simpleHash,getName());
    }

    /**
     * 用户授权的方法，当用户认证通过后每次访问需要授权的请求时候就会自动执行这个方法。
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("------授权---------");
        // 获取用户的账号，一般是在数据库中获取数据
        Object userAccount = principalCollection.getPrimaryPrincipal();
        // 创建角色set集合,应该来自数据库
        Set<String> roles=new HashSet<>();
        if ("admin".equals(userAccount)){
            roles.add("admin");
            roles.add("user");
        }
        if ("user".equals(userAccount)){
            roles.add("user");
        }

        // 创建权限
        Set<String> permissions=new HashSet<>();

        if ("admin".equals(userAccount)){
            // 添加一个权限admin:add只是一种命名风格表示admin下的add功能
            permissions.add("admin:add");
        }


        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 设置角色信息
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        return info;
    }
}
