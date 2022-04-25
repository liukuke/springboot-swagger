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
    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取用户信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

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
     * @return
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
