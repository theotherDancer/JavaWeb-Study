package com.zhangyihang.springboot_javaweb_quickstart.service;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;
import com.zhangyihang.springboot_javaweb_quickstart.pojo.StudentCreateRequest;

import java.sql.SQLException;
import java.util.List;

public interface StudentQueryService {

    List<Student> findAll() throws SQLException;
    Student findById(String studentId) throws SQLException;
    int create(StudentCreateRequest request) throws SQLException;
}