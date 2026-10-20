package com.ha.springbootdemo.common;

import com.ha.springbootdemo.modules.goods.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InitGoods implements CommandLineRunner {

    private final StringRedisTemplate stringRedisTemplate;
    private final GoodsMapper goodsMapper;

    @Override
    public void run(String... args) {
        int num = 100;
        goodsMapper.initGoods(num);
        stringRedisTemplate.opsForValue().set("stock-1", num + "");
    }
}
