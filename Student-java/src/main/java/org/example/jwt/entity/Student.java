package org.example.jwt.entity;

import lombok.Data;

@Data
public class Student {
    private Long studentId;
    private String studentNo;
    private String name;
    private Integer sex;
    private Integer age;
    private Integer version;
}
