package com.ha.springbootdemo.modules.goods.service.impl;

import com.ha.springbootdemo.common.Result;
import com.ha.springbootdemo.modules.goods.entity.Order;
import com.ha.springbootdemo.modules.goods.mapper.GoodsMapper;
import com.ha.springbootdemo.modules.goods.mapper.OrderMapper;
import com.ha.springbootdemo.modules.goods.service.SeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service("syncSeckill")
public class SeckillServiceImpl implements SeckillService {

    private final GoodsMapper goodsMapper;
    private final OrderMapper orderMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Long> seckillScriptLua;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> executeSeckill(Long userId, Long goodsId) {

        // 访问redis
//        Long leftStock = stringRedisTemplate.opsForValue().decrement("stock-" + goodsId);
//        if (leftStock < 0) {
//            stringRedisTemplate.opsForValue().increment("stock-" + goodsId);
//            return Result.success("已售罄");
//        }

        String stockKey = "stock-" + goodsId;
        // 组装 KEYS 和 ARGV
        List<String> keys = Collections.singletonList(stockKey);
        String[] args = new String[]{String.valueOf(1)};
        // 执行脚本
        long result = stringRedisTemplate.execute(seckillScriptLua, keys, args);

        if (result == -1) {
            return Result.success("商品未上架");
        } else if (result == 0) {
            return Result.success("手慢了，商品已售罄！");
        } else {
            String leftStock = stringRedisTemplate.opsForValue().get(stockKey);
            // 扣减库存
            assert leftStock != null;
            goodsMapper.updateGoods(Long.parseLong(leftStock));
            // 生成订单
            Order order = new Order();
            order.setGoodsId(goodsId);
            order.setUserId(userId);
            orderMapper.generateOrder(order);
            return Result.success("排队中，请稍后查看订单...");
        }
    }
}
