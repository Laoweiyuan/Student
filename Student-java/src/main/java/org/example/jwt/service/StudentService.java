package org.example.jwt.service;

import org.example.jwt.entity.PageResult;
import org.example.jwt.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAllService();
    Student findByIdService(Long studentId);
    List<Student> findByNameService(String name);
    Student findSexService(String sex);
    PageResult<Student> pageByCursor(Integer cursor, int size);
}
