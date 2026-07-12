package org.example.redis.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor          // 无参构造（Redis 反序列化必须）
@AllArgsConstructor         // 全参构造
public class User implements Serializable {
    private String name;
    private int age;
}