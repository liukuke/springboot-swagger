package com.liu.swagger.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户实体类")
public class User {

    private String name;

    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
