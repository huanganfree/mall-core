package com.ha.springbootdemo.modules.goods.service.impl;

import com.ha.springbootdemo.common.Result;
import com.ha.springbootdemo.modules.goods.entity.Order;
import com.ha.springbootdemo.modules.goods.mapper.GoodsMapper;
import com.ha.springbootdemo.modules.goods.mapper.OrderMapper;
import com.ha.springbootdemo.modules.goods.service.SeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service("syncSeckill")
public class SeckillServiceImpl implements SeckillService {

    private final GoodsMapper goodsMapper;
    private final OrderMapper orderMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> executeSeckill(Long userId, Long goodsId) {
//        直接打到数据库
//        Goods goods = goodsMapper.selectById(goodsId);
//        if (goods == null) {
//            return Result.success("商品不存在");
//        }
//        // 判断库存是否足够
//        if (goods.getStock() <= 0) {
//            return Result.success("已售罄");
//        }

//        访问redis
        Long leftStock = stringRedisTemplate.opsForValue().decrement("stock-" + goodsId);
        if (leftStock < 0) {
            stringRedisTemplate.opsForValue().increment("stock-" + goodsId);
            return Result.success("已售罄");
        }
        // 扣减库存（阶段一的盲目更新写法）
        goodsMapper.updateGoods(leftStock);
        // 生成订单
        Order order = new Order();
        order.setGoodsId(goodsId);
        order.setUserId(userId);
        orderMapper.generateOrder(order);
        return Result.success("秒杀成功！订单已生成");
    }
}
