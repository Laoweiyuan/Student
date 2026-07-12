package org.example.redis.controller;

import org.example.redis.dao.repository.UserDAO;
import org.example.redis.entity.User;
import org.example.redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class RedisTestController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    //http://localhost:8081/set?key=name&value=Jack → 写入 Redis
    //http://localhost:8081/get/name → 返回 Jack
    //http://localhost:8081/setex?key=name&value=Jack&seconds=10 → 过期时间10s
    // 存值
    @GetMapping("/set")
    public String setValue(@RequestParam String key,
                           @RequestParam String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return "OK";
    }

    // 取值
    @GetMapping("/get/{key}")
    public String getValue(@PathVariable String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 设置过期时间（比如10秒）
    @GetMapping("/setex")
    public String setWithExpire(@RequestParam String key,
                                @RequestParam String value,
                                @RequestParam long seconds) {
        stringRedisTemplate.opsForValue().set(key, value, seconds, java.util.concurrent.TimeUnit.SECONDS);
        return "OK";
    }

    @Autowired
    private UserDAO userDAO;

    //http://localhost:8081/save-user → 写入 Redis
    @GetMapping("/save-user")
    public String saveUser() {
        userDAO.saveUser();
        return "User saved and fetched, check console.";
    }

    @Autowired
    private UserService userService;
    //http://localhost:8081/get-userCache?id=1 → 从 Redis 取值，没有则写入 Redis
    @GetMapping("/get-userCache")
    public User getUserById(@RequestParam int id) {
        return userService.getUserById(id);
    }

    //http://localhost:8081/get-user?id=1 → 从 Redis 取值，没有则写入 Redis
    @GetMapping("/get-user")
    public User getUser(@RequestParam int id) {
        return userService.getUser(id);
    }


    //http://localhost:8081/get-product?id=1 → 从 Redis 取值，没有则写入 Redis
    @GetMapping("/get-product")
    public User getProduct(@RequestParam int id) {
        return userService.getProduct(id);
    }

    //http://localhost:8081/get-order?id=1 → 从 Redis 取值，没有则写入 Redis
    @GetMapping("/get-order")
    public User getOrder(@RequestParam int id) {
        return userService.getOrder(id);
    }

    //http://localhost:8081/get-something?id=1 → 从 Redis 取值，没有则写入 Redis
    @GetMapping("/get-something")
    public User getSomething(@RequestParam int id) {
        return userService.getSomething(id);
    }

}