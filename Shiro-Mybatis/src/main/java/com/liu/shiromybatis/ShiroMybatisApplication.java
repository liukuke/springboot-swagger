package com.liu.shiromybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.liu.shiromybatis.dao"})
public class ShiroMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroMybatisApplication.class, args);
    }

}
