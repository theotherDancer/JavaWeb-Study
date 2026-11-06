package com.zhangyihang.springboot_javaweb_quickstart.Controller;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.Result;
import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;
import com.zhangyihang.springboot_javaweb_quickstart.pojo.StudentCreateRequest;
import com.zhangyihang.springboot_javaweb_quickstart.service.StudentQueryService;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/students/{studentId}")
    public Result findById(
            @PathVariable("studentId") String studentId) throws SQLException {

        Student student = studentService.findById(studentId);

        if (student == null) {
            return new Result(false, "未找到学生", null);
        }

        return new Result(true, "查询成功", student);
    }
    @PostMapping("/students")
    public Result create(
            @RequestBody StudentCreateRequest request) throws SQLException {

        try {
            int rows = studentService.create(request);

            return new Result(true, "新增成功", rows);

        } catch (IllegalArgumentException e) {
            return new Result(false, e.getMessage(), null);

        } catch (SQLException e) {
            // MySQL 1062：主键或唯一键重复
            if (e.getErrorCode() == 1062) {
                return new Result(false, "学号或其他唯一字段重复", null);
            }

            throw e;
        }
    }

}