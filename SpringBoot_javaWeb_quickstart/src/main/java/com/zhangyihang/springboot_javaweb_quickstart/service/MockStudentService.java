package com.zhangyihang.springboot_javaweb_quickstart.service;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;

import java.util.ArrayList;
import java.util.List;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.StudentCreateRequest;
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
    @Override
    public Student findById(String studentId) {
        for (Student student : findAll()) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }

        return null;
    }
    @Override
    public int create(StudentCreateRequest request) {
        throw new UnsupportedOperationException("模拟查询服务暂不支持新增学生");
    }
}