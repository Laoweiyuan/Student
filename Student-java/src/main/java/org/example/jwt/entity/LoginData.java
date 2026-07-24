package org.example.jwt.entity;

import lombok.Data;

@Data
public class LoginData {
    private Long userId;
    private String username;
    private String token;
}
