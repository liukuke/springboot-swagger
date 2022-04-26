package com.liu.shiromybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String permission;
    /**
     * 1 代表锁定 0 代表不锁定
     */
    private Integer status;
    /**
     * 盐值
     */
    private String salt;
}
