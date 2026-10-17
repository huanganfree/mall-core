package com.ha.springbootdemo.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt") // 关键：指定前缀是 "jwt"
public class JwtProperties {
    // 这里的属性名必须和 application.yml 里的 key 完全一致
    private String secret;
    private Long expire;
}
