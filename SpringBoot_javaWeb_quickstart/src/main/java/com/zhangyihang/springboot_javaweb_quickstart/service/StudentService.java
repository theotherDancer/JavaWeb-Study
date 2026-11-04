package com.zhangyihang.springboot_javaweb_quickstart.service;

import com.zhangyihang.springboot_javaweb_quickstart.dao.StudentDao;
import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;
@Service
public class StudentService implements StudentQueryService {
    @Override
    public List<Student> findAll() throws SQLException {
        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException("请配置 DB_PASSWORD 环境变量");
        }

        String url = "jdbc:mysql://localhost:3306/campus_learning";

        try (Connection connection =
                     DriverManager.getConnection(url, "root", password)) {

            return StudentDao.findAll(connection);
        }
    }
}