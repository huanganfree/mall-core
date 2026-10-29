package com.ha.springbootdemo.common;

import com.ha.springbootdemo.modules.goods.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

//初始化同步缓存
@RequiredArgsConstructor
@Component
public class InitGoods implements CommandLineRunner {

    private final StringRedisTemplate stringRedisTemplate;
    private final GoodsMapper goodsMapper;

    @Override
    public void run(String... args) {
        int num = 10;
        goodsMapper.initGoods(num);
        stringRedisTemplate.opsForValue().set("stock-1", num + "");
    }
}
