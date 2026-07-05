package org.example.jwt.service.impl;

import org.example.jwt.entity.Student;
import org.example.jwt.mapper.StudentMapper;
import org.example.jwt.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//将StudentServiceImpl类标记为Spring的组件，将其添加到Spring的Bean工厂中
public class StudentServiceImpl implements StudentService {

    @Autowired//自动注入StudentMapper接口的实现类
    private StudentMapper studentMapper;

    @Override//实现StudentService接口的方法
    public List<Student> findAllService() {
        return studentMapper.findAllMapper();
    }

    @Override//实现StudentService接口的方法
    public Student findByIdService(Integer id) {return studentMapper.findByIdMapper(id);}
    @Override//实现StudentService接口的方法
    public List<Student> findByNameService(String name) {return studentMapper.findByNameMapper(name);}
    @Override//实现StudentService接口的方法
    public Student findSexService(String sex) {return studentMapper.findSexMapper(sex);}
}
