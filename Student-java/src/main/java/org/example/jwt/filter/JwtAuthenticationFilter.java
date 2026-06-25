package org.example.jwt.filter;

import org.example.jwt.util.JwtUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        //将所有请求和响应都通过filterChain进行处理
        //FilterChain是一个链，用于将多个过滤器连接起来，实现请求的过滤和处理

        // 登录接口不拦截
        if ("/auth/login".equals(request.getRequestURI()))//判断请求URI是否为登录接口的URI
        {
            filterChain.doFilter(request, response);//放行
            return;//直接结束过滤，不继续处理后续过滤器
        }

        String header = request.getHeader("Authorization");//获取Authorization头，header格式为"Bearer xxx.xxx.xxx"
        if (header != null && header.startsWith("Bearer "))//判断Authorization头是否存在且以"Bearer "开头
        {
            String token = header.substring(7);//从Authorization头中提取token
            if (jwtUtil.validateToken(token)) {//1.校验token是否有效
                String username = jwtUtil.getUsernameFromToken(token);//8.调用getUsernameFromToken方法从token中解析用户名，存入username变量
                request.setAttribute("username", username);//10.将用户名放入request attribute，后续业务使用
                filterChain.doFilter(request, response);//放行
                return;
            }
        }

        // 未认证，返回 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\": \"未登录或 token 无效\"}");
    }
}