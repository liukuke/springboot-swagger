package com.liu.swagger3.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("学生类")
public class Student {

    private String name;
    private Integer age;
    private String sex;
}
