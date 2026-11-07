package com.zhangyihang.springboot_javaweb_quickstart.mapper;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

// 数据访问层接口：由 MyBatis 创建代理对象，并通过集成机制交给 Spring 管理。
@Mapper
public interface StudentMapper {

    // 1. 按学号查询：主键最多匹配一条；找到返回 Student，找不到返回 null。
    // @Select 指定 SQL；#{studentId} 使用下方 @Param 命名的参数，进行参数绑定。
    @Select("""
        SELECT student_id, name
        FROM student
        WHERE student_id = #{studentId}
        """)
    // 构造方法映射：两列依次传入 Student(String studentId, String name)。
    @ConstructorArgs({
            @Arg(column = "student_id", javaType = String.class),
            @Arg(column = "name", javaType = String.class)
    })
    Student findById(@Param("studentId") String studentId);

    // 2. 查询全部：按学号排序，每条记录映射成一个 Student，装入 List。
    // 没有记录时返回空列表；这个方法没有查询参数。
    @Select("""
    SELECT student_id, name
    FROM student
    ORDER BY student_id
    """)
    // 构造方法映射：两列依次传入 Student(String studentId, String name)。
    @ConstructorArgs({
            @Arg(column = "student_id", javaType = String.class),
            @Arg(column = "name", javaType = String.class)
    })
    List<Student> findAll();

    // 3. 新增学生：将参数绑定到 SQL，写入一条学生记录。
// 返回影响行数，成功插入一条时返回 1；不会返回 Student 对象。
    @Insert("""
    INSERT INTO student (student_id, name, age)
    VALUES (#{studentId}, #{name}, #{age})
    """)
    int insert(
            @Param("studentId") String studentId,
            @Param("name") String name,
            @Param("age") Integer age
    );
}