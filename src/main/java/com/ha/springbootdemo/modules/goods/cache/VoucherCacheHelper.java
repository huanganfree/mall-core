package com.ha.springbootdemo.modules.goods.cache;

import com.ha.springbootdemo.modules.goods.entity.Goods;
import com.ha.springbootdemo.modules.goods.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Random;
import java.util.concurrent.TimeUnit;

//预防缓存穿透
@RequiredArgsConstructor
@Component
public class VoucherCacheHelper {

    private final GoodsMapper goodsMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final String VOUCHER_KEY = "stock-";

    public Goods getVoucherFromCache(Long voucherId) {
        if (voucherId == null || voucherId <= 0) {
            return null;
        }

        String key = VOUCHER_KEY + voucherId;
        String num = stringRedisTemplate.opsForValue().get(key);

//        缓存理由
        if (num != null) {
            return "NULL".equals(num) ? null : new Goods();
        }

//        查数据库
        Goods voucher = goodsMapper.selectById(voucherId);

        if (voucher != null) {
            int ttl = 1800 + new Random().nextInt(300);
//            随机的过期时间窗口
            
            stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(voucher), ttl, TimeUnit.SECONDS);
        } else {
            stringRedisTemplate.opsForValue().set(key, "NULL", 300, TimeUnit.SECONDS);
        }

        return voucher;
    }
}
