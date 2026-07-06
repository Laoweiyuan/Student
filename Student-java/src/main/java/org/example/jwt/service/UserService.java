package org.example.jwt.service;

import org.example.jwt.entity.User;
import org.example.jwt.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);//3.通过mapper根据用户名查询用户
        //7.xml映射返回User对象到login方法中
        //8.判断用户是否存在
        //9.判断密码是否匹配
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {//传入的密码password加密后与数据库中的密码进行匹配
            return user;//11.如果账号密码匹配，返回用户对象到login方法中，登录成功，AuthController接收用户对象
        }
        return null;//10.如果账号密码不匹配，返回null到login方法中，登录失败，AuthController返回null给前端

    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));//1.将用户密码加密后存储到数据库
        if (userMapper.findByUsername(user.getUsername()) != null) {//判断用户名是否存在
            throw new IllegalArgumentException("用户名已存在");
        }
        else if (user.getUsername() == null) {//判断用户名是否为空
            throw new IllegalArgumentException("用户名不能为空");
        }
        else if (user.getPassword() == null) {//判断密码是否为空
            throw new IllegalArgumentException("密码不能为空");
        }
        userMapper.insert(user);//2.调用mapper的insert方法将用户对象插入到数据库
    }
}