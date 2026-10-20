package com.ha.springbootdemo.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.algorithms.Algorithm;
import com.ha.springbootdemo.common.JwtProperties;
import com.ha.springbootdemo.common.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor()
public class TokenInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;
    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头中获取 Authorization 字段
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.getWriter().write("No token provided or invalid token");
            return false; // 拦截，不放行
        }

        try {

            String token = authHeader.substring(7);
            //  校验 Token 是否合法、是否过期
            JWT.require(Algorithm.HMAC256(jwtProperties.getSecret())).build().verify(token);
            Long userId = jwtUtils.getUserId(token);
            Long roleId = jwtUtils.getRoleId(token);
            request.setAttribute("userId", userId);
            request.setAttribute("roleId", roleId);
            return true; // 校验通过，放行
        } catch (JWTVerificationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.getWriter().write("Invalid or expired token");
            return false; // 校验失败，拦截
        }
    }
}
