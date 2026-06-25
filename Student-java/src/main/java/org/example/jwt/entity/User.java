package org.example.jwt.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;   // 数据库中存的是加密后的密文
}