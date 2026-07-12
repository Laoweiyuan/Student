package org.example.redis.dao.repository;

import org.example.redis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveUser() {
        User user = new User("Jack", 25);
        redisTemplate.opsForValue().set("user:1", user);
        User u = (User) redisTemplate.opsForValue().get("user:1");
        System.out.println(u.getName());
    }
}
