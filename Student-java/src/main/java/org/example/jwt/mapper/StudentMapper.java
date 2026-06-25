package org.example.jwt.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.example.jwt.entity.Student;

import java.util.List;

@Mapper
public interface StudentMapper {
    @DS("Student")
    List<Student> findAllMapper();
}
