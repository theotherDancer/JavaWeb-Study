package com.zhangyihang.springboot_javaweb_quickstart.Controller;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.Result;
import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;
import com.zhangyihang.springboot_javaweb_quickstart.service.StudentQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
public class StudentController {

    private final StudentQueryService studentService;

    public StudentController(StudentQueryService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public Result findAll() throws SQLException {
        List<Student> students = studentService.findAll();

        return new Result(true, "查询成功", students);
    }
}