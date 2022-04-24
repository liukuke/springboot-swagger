package com.liu.swagger3.controller;

import com.liu.swagger3.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    @GetMapping("/getStudent")
    public List<Student> getStudent(String name){
        Student student = new Student();
        student.setName(name);
        student.setAge(81);
        student.setSex("women");
        Student student1 = new Student();
        student1.setName("ls");
        student1.setAge(81);
        student1.setSex("women");
        Student student2 = new Student();
        student2.setName("ls");
        student2.setAge(81);
        student2.setSex("women");
        Student student3 = new Student();
        student3.setName("ls");
        student3.setAge(81);
        student3.setSex("women");
        List students = new ArrayList();
        students.add(student);
        students.add(student1);
        students.add(student2);
        students.add(student3);

        return students;
    }
}
