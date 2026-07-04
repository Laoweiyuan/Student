package org.example.jwt.controller;

import org.example.jwt.entity.LoginData;
import org.example.jwt.entity.Result;
import org.example.jwt.entity.User;
import org.example.jwt.service.UserService;
import org.example.jwt.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/user")
    public User testUser() {
        User user = new User();
        user.setUsername("admin");
        return user;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User userinfo) {//1.通过JSON的形式接收用户名和密码
        User user = userService.login(userinfo.getUsername(), userinfo.getPassword());//12.调用UserService的login方法进行登录验证
        if (user == null) {
            return ResponseEntity.status(401).body("用户名或密码错误");//12.如果user为空，返回401状态码和错误信息

        }
        String token = jwtUtil.generateToken(userinfo.getUsername());//13.如果user不为空，调用JwtUtil的generateToken方法生成JWT token
//        Map<String, Object> result = new HashMap<>();//22.创建一个Map，用于存储token及用户信息返回结果，便于前端接收
//        result.put("code", 200);
//        result.put("msg", "登录成功");
//        result.put("id", user.getId().toString());
//        result.put("username", userinfo.getUsername());
//        result.put("token", token);
//        return ResponseEntity.ok(result);//23.返回token及用户信息到前端
        LoginData data = new LoginData();
        data.setId(user.getId());
        data.setUsername(userinfo.getUsername());
        data.setToken(token);
        Result<LoginData> result = Result.success(data);
        return ResponseEntity.ok(result);
       }
}