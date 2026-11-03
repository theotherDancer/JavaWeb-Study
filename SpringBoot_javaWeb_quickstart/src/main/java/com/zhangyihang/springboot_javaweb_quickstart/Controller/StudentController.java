package com.zhangyihang.springboot_javaweb_quickstart.Controller;

import com.zhangyihang.springboot_javaweb_quickstart.dao.StudentDao;
import com.zhangyihang.springboot_javaweb_quickstart.pojo.Result;
import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@RestController
public class StudentController {

    @GetMapping("/students")
    public Result findAll() throws SQLException {
        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException("请配置 DB_PASSWORD 环境变量");
        }

        String url = "jdbc:mysql://localhost:3306/campus_learning";

        try (Connection connection =
                     DriverManager.getConnection(url, "root", password)) {

            List<Student> students = StudentDao.findAll(connection);

            return new Result(true, "查询成功", students);
        }
    }
}