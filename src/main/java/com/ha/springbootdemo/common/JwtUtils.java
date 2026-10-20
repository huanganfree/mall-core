package com.ha.springbootdemo.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    public String generateToken(Long userId, Long roleId, String phone) {
        return JWT.create()
                .withClaim("userId", userId)                               // 存入自定义载荷字段
                .withClaim("phone", phone)
                .withClaim("roleId", roleId)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpire())) // 设置过期时间
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));                           // 使用 HMAC256 算法和密钥签名
    }

    public DecodedJWT verifyToken(String token) {
        // 使用相同的密钥和算法创建一个验证器
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        JWTVerifier verifier = JWT.require(algorithm).build();
        // 如果 Token 过期、被篡改或无效，这里会直接抛出异常
        return verifier.verify(token);
    }

    public Long getUserId(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getClaim("userId").asLong();
    }

    public Long getRoleId(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getClaim("roleId").asLong();
    }

}
