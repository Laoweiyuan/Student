package org.example.jwt.controller;

import org.example.jwt.entity.Student;
import org.example.jwt.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired//自动注入StudentService接口的实现类
    private StudentService studentService;

    @GetMapping("/findAll")
    public List<Student> findAll(){
        return studentService.findAllService();
    }
    @GetMapping("/find/{id}")
    public Student findById(@PathVariable Integer id) {return studentService.findByIdService(id);}
    @GetMapping("/find/name/{name}")
    public List<Student> findByName(@PathVariable String name) {return studentService.findByNameService(name);}
    @GetMapping("/find/sex/{sex}")
    public Student findSex(@PathVariable String sex) {return studentService.findSexService(sex);}
}
