package com.liu.shiromybatis.realm;

import com.liu.shiromybatis.dao.UserDao;
import com.liu.shiromybatis.entity.User;
import com.liu.shiromybatis.utils.EncryptUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {
    @Resource
    private UserDao userDao;


    public MyRealm(){
        // 指定密码匹配方式，可以直接写名字，也可以通过自定义的util工具类调用属性
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher(EncryptUtil.HASH_ALGORITHM_NAME);
        // 指定密码迭代次数，可以直接写数字，也可以通过自定义的util工具类调用属性
        hashedCredentialsMatcher.setHashIterations(EncryptUtil.ITERATION);
        // 使用父层方法使加密生效
        setCredentialsMatcher(hashedCredentialsMatcher);
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

        return new SimpleAuthenticationInfo(username,user.getPassword(), ByteSource.Util.bytes(user.getSalt()),this.getName());
    }

    /**
     * 授权
     * @param principalCollection
     * @return AuthorizationInfo是一个接口所以返回的是他的一个实现类simpleAuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取用户账号
        String username = (String) principalCollection.getPrimaryPrincipal();
        // 从数据库获取用户所有信息
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
