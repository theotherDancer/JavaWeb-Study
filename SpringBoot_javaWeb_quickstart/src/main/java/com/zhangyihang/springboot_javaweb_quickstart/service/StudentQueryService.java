package com.zhangyihang.springboot_javaweb_quickstart.service;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentQueryService {

    List<Student> findAll() throws SQLException;
}