package com.zhangyihang.springboot_javaweb_quickstart.dao;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    public static List<Student> findAll(Connection connection)
            throws SQLException {

        List<Student> students = new ArrayList<>();

        String sql =
                "SELECT student_id, name FROM student ORDER BY student_id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("student_id");
                String name = resultSet.getString("name");

                Student student = new Student(id, name);
                students.add(student);
            }
        }

        return students;
    }
}