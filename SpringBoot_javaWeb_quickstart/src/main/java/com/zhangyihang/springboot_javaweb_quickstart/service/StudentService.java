package com.zhangyihang.springboot_javaweb_quickstart.service;

import com.zhangyihang.springboot_javaweb_quickstart.dao.StudentDao;
import com.zhangyihang.springboot_javaweb_quickstart.mapper.StudentMapper;
import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.StudentCreateRequest;
import org.springframework.stereotype.Service;
@Service
public class StudentService implements StudentQueryService {
    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

   /* @Override
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
    }*/

    @Override
    public List<Student> findAll() throws SQLException {
        // 通过注入的 Mapper 代理对象查询全部学生。
        // MyBatis 将每条记录映射为 Student，并组成列表返回。
        return studentMapper.findAll();
    }
  /*  @Override
    public Student findById(String studentId) throws SQLException {
        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException("请配置 DB_PASSWORD 环境变量");
        }

        String url = "jdbc:mysql://localhost:3306/campus_learning";

        try (Connection connection =
                     DriverManager.getConnection(url, "root", password)) {

            return StudentDao.findById(connection, studentId);
        }
    }*/
  @Override
  public Student findById(String studentId) throws SQLException {
      return studentMapper.findById(studentId);
  }
    private void validateCreateRequest(StudentCreateRequest request) {
        String studentId = request.getStudentId();
        String name = request.getName();
        Integer age = request.getAge();

        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("学号不能为空");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("姓名不能为空");
        }

        if (age == null || age < 0 || age > 150) {
            throw new IllegalArgumentException("年龄必须填写，且在 0～150 之间");
        }
    }

    /*@Override
    public int create(StudentCreateRequest request) throws SQLException {
        // 1. 校验学号、姓名和年龄
        validateCreateRequest(request);

        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException("请配置 DB_PASSWORD 环境变量");
        }

        String url = "jdbc:mysql://localhost:3306/campus_learning";

        try (Connection connection =
                     DriverManager.getConnection(url, "root", password)) {

            // 2. 检查学号是否已经存在
            Student existing =
                    StudentDao.findById(connection, request.getStudentId());

            if (existing != null) {
                throw new IllegalArgumentException("学号已存在");
            }

            // 3. 保存学生
            int rows = StudentDao.insert(
                    connection,
                    request.getStudentId(),
                    request.getName(),
                    request.getAge()
            );

            // 4. 核对插入结果
            if (rows != 1) {
                throw new SQLException("新增学生的影响行数不是 1");
            }

            return rows;
        }
    }*/
    @Override
    public int create(StudentCreateRequest request) throws SQLException {
        // 1. 保留原有业务校验：学号、姓名不能为空，年龄必须在范围内。
        validateCreateRequest(request);

        // 2. 查询学号是否已存在，给出明确的业务提示。
        Student existing = studentMapper.findById(request.getStudentId());

        if (existing != null) {
            throw new IllegalArgumentException("学号已存在");
        }

        // 3. 通过 Mapper 保存学生，连接和参数绑定由框架处理。
        int rows = studentMapper.insert(
                request.getStudentId(),
                request.getName(),
                request.getAge()
        );

        // 4. 核对影响行数；这项检查本身不提供事务回滚。
        if (rows != 1) {
            throw new SQLException("新增学生的影响行数不是 1");
        }

        return rows;
    }
}