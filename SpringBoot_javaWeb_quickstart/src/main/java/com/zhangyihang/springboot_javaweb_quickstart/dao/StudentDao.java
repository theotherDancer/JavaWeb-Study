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
    public static Student findById(
            Connection connection, String studentId) throws SQLException {

        String sql =
                "SELECT student_id, name FROM student WHERE student_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String id = resultSet.getString("student_id");
                    String name = resultSet.getString("name");

                    return new Student(id, name);
                }

                return null;
            }
        }
    }
    public static int insert(
            Connection connection,
            String studentId,
            String name,
            int age) throws SQLException {

        String sql =
                "INSERT INTO student (student_id, name, age) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentId);
            statement.setString(2, name);
            statement.setInt(3, age);

            return statement.executeUpdate();
        }
    }
}