package com.ha.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisScriptConfig {

    @Bean
    public DefaultRedisScript<Long> seckillScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 1. 指定 resources 目录下的脚本路径
        redisScript.setLocation(new ClassPathResource("scripts/seckill.lua"));
        // 2. 指定 Lua 脚本 return 返回值的类型（与你在 Lua 中返回的数字对应）
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
