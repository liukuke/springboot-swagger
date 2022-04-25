package com.liu.shiromybatis.dao;

import com.liu.shiromybatis.entity.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return
     */
    User getUser(String username);

}
