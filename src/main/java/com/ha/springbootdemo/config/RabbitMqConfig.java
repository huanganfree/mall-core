package com.ha.springbootdemo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue seckillQueue() {
        return new Queue("seckill.queue", true);
    }
}
