package com.liu.shiromybatis.realm;

import com.liu.shiromybatis.dao.UserDao;
import com.liu.shiromybatis.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {
    @Resource
    private UserDao userDao;


    public MyRealm(){

    }

    /**
     * 认证
     * @param authenticationToken
     * @return AuthenticationInfo是一个接口所以返回的是他的一个实现类SimpleAuthenticationInfo
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取用户信息（用户名+密码）
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 直接使用authenticationToken获取用户名
        String name = (String) authenticationToken.getPrincipal();
        // 使用UsernamePasswordToken获取用户名
        String username = null;
        if (token.getUsername()!=null){
            username=token.getUsername();
        }
        User user = userDao.getUser(username);

        if (user==null){
            throw new UnknownAccountException();
        }else if (user.getStatus()==1){
            throw new LockedAccountException();
        }

        return new SimpleAuthenticationInfo(username,user.getPassword(),this.getName());
    }

    /**
     * 授权
     * @param principalCollection
     * @return AuthorizationInfo是一个接口所以返回的是他的一个实现类simpleAuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取账号
        String username = (String) principalCollection.getPrimaryPrincipal();

        User user = userDao.getUser(username);

        Set<String> roles = new HashSet<>();
        roles.add(user.getRole());
        Set<String> permissions = new HashSet<>();
        permissions.add(user.getPermission());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }


}
