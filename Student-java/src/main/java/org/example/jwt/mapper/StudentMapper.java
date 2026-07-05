package org.example.jwt.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
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
}
