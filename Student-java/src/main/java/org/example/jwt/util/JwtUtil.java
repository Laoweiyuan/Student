package org.example.jwt.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expiration;

    // 从配置文件注入 secret 和过期时间
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        //将secret转换为字节数组，此时还没有生成密钥，等generateToken方法调用时才会生成密钥
        this.expiration = expiration;
        //配置JwtUtil的时候就将过期时间赋值给this.expiration
    }

    // 生成 token，只放用户名
    public String generateToken(String username) {
        Date now = new Date();//14.获取当前时间
        Date expiryDate = new Date(now.getTime() + expiration);//15.计算过期时间

        return Jwts.builder()//16.使用Jwts.builder()方法构建JWT token
                .setSubject(username)//17.设置JWT token的主体为用户名
                .setIssuedAt(now)//18.设置JWT token的签发时间为当前时间
                .setExpiration(expiryDate)//19.设置JWT token的过期时间为计算出的过期时间
                .signWith(key, SignatureAlgorithm.HS256)//20.使用HS256算法对JWT token进行签名
                .compact();//21.压缩JWT token，返回压缩后的字符串
    }



    // 校验 token 是否有效
    public boolean validateToken(String token) {
        try {
            parseClaims(token);//2.调用parseClaims方法解析JWT token，获取Claims对象
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()//3.使用Jwts.parserBuilder()方法构建JWT token解析器
                .setSigningKey(key)//4.设置JWT token解析器的签名密钥为this.key
                .build()//5.构建JWT token解析器
                .parseClaimsJws(token)//6.解析JWT token，将拆开的JWT token与this.key结合，再执行加密，检验是否匹配
                .getBody();//7.解析成功返回Claims对象，该对象只有载荷信息，失败抛出异常JwtException
    }
    // 从 token 中解析用户名
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();//9.调用parseClaims方法解析JWT token，获取Claims对象，从token中解析用户名
    }
}