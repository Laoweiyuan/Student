package org.example.jwt.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.jwt.entity.Student;

import java.util.List;

@Mapper
public interface StudentMapper {
    @DS("Student")
    List<Student> findAllMapper();

    @DS("Student")
    Student findByIdMapper(Integer id);

    @DS("Student")
    List<Student> findByNameMapper(String name);
    @DS("Student")
    Student findSexMapper(String sex);
    @DS("Student")
    List<Student> selectByCursor(Integer cursor, Integer size);
    @DS("Student")
    @Insert("<script>" +
            "INSERT INTO student (student_no, name, sex, age, version) VALUES " +
            "<foreach collection='batch' item='s' separator=','>" +
            "(#{s.studentNo}, #{s.name}, #{s.sex}, #{s.age}, 0)" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("batch") List<Student> batch);
}
