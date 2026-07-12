package org.example.redis.service;

import org.example.redis.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Cacheable(value = "userCache", key = "#id")
    public User getUserById(int id) {
        // 模拟从数据库查询
        System.out.println("从数据库查询...");
        return new User("User" + id, 20 + id);
    }

    @Cacheable(value = "users", key = "#id")       // 使用 1 小时 TTL
    public User getUser(int id) {
        System.out.println("从数据库查询...");
        return new User("User" + id, 20 + id);
    }

    @Cacheable(value = "products", key = "#id")    // 使用 10 分钟 TTL
    public User getProduct(int id) {
        System.out.println("从数据库查询...");
        return new User("User" + id, 20 + id);
    }

    @Cacheable(value = "orders", key = "#id")      // 使用 30 秒 TTL
    public User getOrder(int id) {
        System.out.println("从数据库查询...");
        return new User("User" + id, 20 + id);
    }

    @Cacheable(value = "unknown", key = "#id")     // 未配置，使用全局默认 30 分钟
    public User getSomething(int id) {
        System.out.println("从数据库查询...");
        return new User("User" + id, 20 + id);
    }
}