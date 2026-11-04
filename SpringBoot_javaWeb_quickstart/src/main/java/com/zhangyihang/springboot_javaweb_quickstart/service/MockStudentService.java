package com.zhangyihang.springboot_javaweb_quickstart.service;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
//@Service
public class MockStudentService implements StudentQueryService {

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();

        students.add(new Student("mock001", "模拟学生小猫"));
        students.add(new Student("mock002", "模拟学生小狗"));

        return students;
    }
}